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
	protected List<Weapon> _notRegisteredWeapons = null; // 게임 업데이트전 해쉬맵에 추가될 무기 (발사된 무기)
	protected List<Weapon> _registeredWeapons = null;    // 게임 오브젝트 해쉬맵에 추가된 무기 (발사된 무기)
	
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
		
		_money = 1000; // 1000억원
		_wholePower = 0;
		_numOfweapons = 0;
		_laserNumber = 0;
		_bombNumber = 0;
		_gunNumber = 0;
		
		_transform = new Transformation(0.0f, 0.0f, 0.0f);
		_transform._radius = 2.0f;
		// 속도관련 값들 설정
		_transform.setPhysics(30.0f, 10.0f, 120.0f);
		
		// 자식 오브젝트 컨테이너 생성
		_weapons = new ArrayList<>();
		_registeredWeapons = new ArrayList<>();
		_notRegisteredWeapons = new ArrayList<>();
		
		// 발사 후 경과시간 저장할 변수 초기화 (드르륵 쏘는거 막기 위함)
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
	
	// 메인 스레드로 정보 보냄
	public void sendMyInfo(int index) {
		_tm.sendtoMain(String.format("%s,%d,%d,%d,%d,%d,(%f %f %f)", name, _money, _laserNumber*40+_bombNumber*30+_gunNumber*20, _laserNumber, _bombNumber, _gunNumber
				, _transform._vPosition.x, _transform._vPosition.y, _transform._vPosition.z), index);
	}
	
	// 메인 스레드로 정보 보냄
	public void sendlog(String log) {
		_tm.sendtoMain(log, 2);
	}
	
	// 발사
	public void fire(float deltaTime) {	
		if (_weapons.size() <= 0) {  // 자식 객체(총알)이 없을 경우 발사 안함
			return;
		}
		
		if (_fireDeltaTime <= 0.1f) { // 
			_fireDeltaTime += deltaTime;
			return;
		}
		
		// 총알 등록
		Weapon b = (Weapon)_weapons.get(0);
		b._transform.setPosition(_transform._vCollisionPosition);
		b._localForwardX = _transform.getForward().x;
		b._localForwardY = _transform.getForward().y;
		b._localForwardZ = _transform.getForward().z;
		
		// 보유 개수 수정
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
	
	// 발사된 무기 오브젝트 해쉬맵에 등록
	public void attachFiredWeapons() {
		for (Weapon w : _notRegisteredWeapons) {
			_registeredWeapons.add(w);
			_gobjects.attach(w);
		}
		
		_notRegisteredWeapons.clear();
	}
	
	// 무기 구매 처리
	public void purchase(int index) {
		String receipt = _tm.popInSubMailBox(index);
		
		if (receipt != null && !receipt.equals("")) {
			String[] tokens = receipt.split(",");
			
			// 레이저
			for(int i=0; i<Integer.parseInt(tokens[0]); i++) {
				Laser l = new Laser("laser");
				if(_money - l.getPrice() < 0) {
					sendlog("잔액 부족! \n 일부 품목 구매 불가");
					return;
				}
				_laserNumber++;
				_money -= l.getPrice();
				_weapons.add(l);
			}
			// 폭탄
			for(int i=0; i<Integer.parseInt(tokens[1]); i++) {
				Bomb b = new Bomb("bomb");
				if(_money - b.getPrice() < 0) {
					sendlog("잔액 부족! \n 일부 품목 구매 불가");
					return;
				}
				_bombNumber++;
				_money -= b.getPrice();
				_weapons.add(b);
			}
			// 총
			for(int i=0; i<Integer.parseInt(tokens[2]); i++) {
				Gun g = new Gun("bomb");
				if(_money - g.getPrice() < 0) {
					sendlog("잔액 부족! \n 일부 품목 구매 불가");
					return;
				}
				_gunNumber++;
				_money -= g.getPrice();
				_weapons.add(g);
			}
			
			// 무기 섞음
			Collections.shuffle(_weapons);
		}
	}
	
	// 발사된 총알들의 사거리, 충돌등의 이유로 "죽음"상태 체크 후 제거
	public void checkBulletDeath() {
		if(_registeredWeapons.size() > 0) {
			for (int i=_registeredWeapons.size() - 1; i>=0; i--) {
				if (!_registeredWeapons.get(i)._alive) { // 죽음 상태(false)일때 제거
					_gobjects.detach(_registeredWeapons.get(i).getName());
					_registeredWeapons.remove(i);
				}
			}
		}
	}
	
	// 두 점사이 거리 구하여 충돌 여부 확인
	public boolean isCollided(Vector3f first, Vector3f second, float radiusSum) {
		// 두 점 사이 거리 구함
		float distance = (float) Math.sqrt(((float)Math.pow(first.x - second.x, 2) + (float)Math.pow(first.y - second.y, 2) + (float)Math.pow(first.z - second.z, 2)));
		if (distance < radiusSum) {
			return true;
		}
		
		return false;
	}
	
	// 죽었는지 확인
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
