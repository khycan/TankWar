package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.joml.Vector3f;

import core.CONSTANTS;
import core.GameObject;
import core.IGameLogic;
import core.components.Transformation;
import game.Weapon.Bomb;
import game.Weapon.Gun;
import game.Weapon.Laser;
import game.Weapon.Weapon;

public class Tank extends GameObject {
	protected List<Weapon> _weapons = null;
	protected List<Weapon> _notRegisteredWeapons = null; // ���� ������Ʈ�� �ؽ��ʿ� �߰��� ���� (�߻�� ����)
	protected List<Weapon> _registeredWeapons = null;    // ���� ������Ʈ �ؽ��ʿ� �߰��� ���� (�߻�� ����)
	
	protected float _fireDeltaTime;
	
	protected int _numOfweapons;
	protected int _laserNumber;
	protected int _bombNumber;
	protected int _gunNumber;
	public int _money;
	protected int _wholePower;
	protected ThreadMessenger _tm;
	
	public Tank(String name, IGameLogic go, ThreadMessenger tm) {
		super(name);
		_tm = tm;
		_gobjects = go;
		
		_money = 1000; // 1000���
		_wholePower = 0;
		_numOfweapons = 0;
		_laserNumber = 0;
		_bombNumber = 0;
		_gunNumber = 0;
		
		_transform = new Transformation(0.0f, 0.0f, 0.0f);
		_transform._radius = 2.0f;
		// �ӵ����� ���� ����
		_transform.setPhysics(30.0f, 10.0f, 120.0f);
		
		// �ڽ� ������Ʈ �����̳� ����
		_weapons = new ArrayList<>();
		_registeredWeapons = new ArrayList<>();
		_notRegisteredWeapons = new ArrayList<>();
		
		// �߻� �� ����ð� ������ ���� �ʱ�ȭ (�帣�� ��°� ���� ����)
		_fireDeltaTime = 1.0f;
	}
	
	public void checkCollisionforSkybox() {
		float myX = _transform._vPosition.x;
		float myZ = _transform._vPosition.z;
		
		if(myX > 498.0f) myX = 498.0f;
		else if(myX < -498.0f) myX = -498.0f;
		
		if(myZ > 498.0f) myZ = 498.0f;
		else if(myZ < -498.0f) myZ = -498.0f;
		
		_transform._vPosition.x = myX;
		_transform._vPosition.z = myZ;
	}
	
	// ���� ������� ���� ����
	public void sendMyInfo(int index) {
		_tm.sendtoMain(String.format("%s,%d,%d,%d,%d,%d,(%f %f %f)", name, _money, _laserNumber*40+_bombNumber*30+_gunNumber*20, _laserNumber, _bombNumber, _gunNumber
				, _transform._vPosition.x, _transform._vPosition.y, _transform._vPosition.z), index);
	}
	
	// ���� ������� ���� ����
	public void sendlog(String log) {
		_tm.sendtoMain(log, 2);
	}
	
	// �߻�
	public void fire(float deltaTime) {	
		if (_weapons.size() <= 0) {  // �ڽ� ��ü(�Ѿ�)�� ���� ��� �߻� ����
			return;
		}
		
		if (_fireDeltaTime <= 0.1f) { // 
			_fireDeltaTime += deltaTime;
			return;
		}
		
		// �Ѿ� ���
		Weapon b = (Weapon)_weapons.get(0);
		b._transform.setPosition(_transform._vCollisionPosition);
		b._localForwardX = _transform.getForward().x;
		b._localForwardY = _transform.getForward().y;
		b._localForwardZ = _transform.getForward().z;
		
		// ���� ���� ����
		if (b.getType().equals("laser")) {
			_laserNumber--;
		} else if (b.getType().equals("bomb")) {
			_bombNumber--;
		} else if (b.getType().equals("gun")) {
			_gunNumber--;
		}
		
		_notRegisteredWeapons.add(b);
		_weapons.remove(0);
		_fireDeltaTime = 0.0f;
	}
	
	// �߻�� ���� ������Ʈ �ؽ��ʿ� ���
	public void attachFiredWeapons() {
		for (Weapon w : _notRegisteredWeapons) {
			_registeredWeapons.add(w);
			_gobjects.attach(w);
		}
		
		_notRegisteredWeapons.clear();
	}
	
	// ���� ���� ó��
	public void purchase(int index) {
		String receipt = _tm.popInSubMailBox(index);
		
		if (receipt != null && !receipt.equals("")) {
			String[] tokens = receipt.split(",");
			
			// ������
			for(int i=0; i<Integer.parseInt(tokens[0]); i++) {
				Laser l = new Laser("laser");
				if(_money - l.getPrice() < 0) {
					sendlog("�ܾ� ����! \n �Ϻ� ǰ�� ���� �Ұ�");
					return;
				}
				_laserNumber++;
				_money -= l.getPrice();
				_weapons.add(l);
			}
			// ��ź
			for(int i=0; i<Integer.parseInt(tokens[1]); i++) {
				Bomb b = new Bomb("bomb");
				if(_money - b.getPrice() < 0) {
					sendlog("�ܾ� ����! \n �Ϻ� ǰ�� ���� �Ұ�");
					return;
				}
				_bombNumber++;
				_money -= b.getPrice();
				_weapons.add(b);
			}
			// ��
			for(int i=0; i<Integer.parseInt(tokens[2]); i++) {
				Gun g = new Gun("bomb");
				if(_money - g.getPrice() < 0) {
					sendlog("�ܾ� ����! \n �Ϻ� ǰ�� ���� �Ұ�");
					return;
				}
				_gunNumber++;
				_money -= g.getPrice();
				_weapons.add(g);
			}
			
			// ���� ����
			Collections.shuffle(_weapons);
		}
	}
	
	// �߻�� �Ѿ˵��� ��Ÿ�, �浹���� ������ "����"���� üũ �� ����
	public void checkBulletDeath() {
		if(_registeredWeapons.size() > 0) {
			for (int i=_registeredWeapons.size() - 1; i>=0; i--) {
				if (!_registeredWeapons.get(i)._alive) { // ���� ����(false)�϶� ����
					_gobjects.detach(_registeredWeapons.get(i).getName());
					_registeredWeapons.remove(i);
				}
			}
		}
	}
	
	// �� ������ �Ÿ� ���Ͽ� �浹 ���� Ȯ��
	public boolean isCollided(Vector3f first, Vector3f second, float radiusSum) {
		// �� �� ���� �Ÿ� ����
		float distance = (float) Math.sqrt(((float)Math.pow(first.x - second.x, 2) + (float)Math.pow(first.y - second.y, 2) + (float)Math.pow(first.z - second.z, 2)));
		if (distance < radiusSum) {
			return true;
		}
		
		return false;
	}
	
	// �׾����� Ȯ��
	public boolean amIdefeated() {
		if ((_money + _laserNumber + _bombNumber + _gunNumber) == 0) {
			return true;
		}
		
		return false;
	}

	//////////////////////////////////////////////////////////
	@Override
	public void init() throws Exception {
	}

	@Override
	public void update(float deltaTime) {
		
	}

	@Override
	public void cleanup() {
		
	}

}
