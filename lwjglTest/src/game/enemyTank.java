package game;

import org.joml.Vector3f;

import core.CONSTANTS;
import core.IGameLogic;
import core.components.Camera;
import core.components.OBJParser;
import core.components.Texture;
import game.Weapon.Weapon;

public class enemyTank extends Tank {
	
	// ��ũ�� ���� �ൿ���� ������ ��
	private int _action = -1;
	private float _eachActionTime = 0.0f;

	public enemyTank(String name, IGameLogic go, ThreadMessenger tm) {
		super(name, go, tm);
		
		// �⺻ ��ġ ����
		_transform.setPosition(30.0f, 0.0f, -30.0f);
		_transform.setPhysics(40.0f, 20.0f, 120.0f);
		
		// �޽� ����
		try {
			_mesh = OBJParser.LoadData("TTank.obj", -1);
			_mesh.setTexture(new Texture("tankskin.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(float deltaTime) {
		purchase(1);
		
		autoMove(deltaTime);
		
		_transform.update(deltaTime);
		move(deltaTime);
		checkCollisionforSkybox();
		_transform._vCollisionPosition.x = _transform._vPosition.x;  // ����ũ�� �浹 �˻� ��ġ ������Ʈ
		_transform._vCollisionPosition.y = 2.0f;                     // �÷��̾�� �����̸� ���߱� ���� y���� �ٸ��� ��
		_transform._vCollisionPosition.z = _transform._vPosition.z;
		sendMyInfo(1);

		checkHittingEnemy();
	}
///////////////////////////////////////////////////////
	
	// ��ũ AI
	public void autoMove(float deltaTime) {
		// �ð� ����
		_eachActionTime += deltaTime;
		
		// �÷��̾ �ٶ󺸴� ����
		Vector3f vheadToPlayer = new Vector3f(0.0f, 0.0f, 0.0f);
		Tank p = (Tank)_gobjects.getGO("player");
		vheadToPlayer.x = p._transform._vCollisionPosition.x - _transform._vCollisionPosition.x;
		vheadToPlayer.y = p._transform._vCollisionPosition.y - _transform._vCollisionPosition.y;
		vheadToPlayer.z = p._transform._vCollisionPosition.z - _transform._vCollisionPosition.z;
		
		if(vheadToPlayer.length() == 0) {
			vheadToPlayer.x = 0.0f;
			vheadToPlayer.y = 0.0f;
			vheadToPlayer.z = 0.0f;
		} else {
			vheadToPlayer.normalize();
		}
		
		// �÷��̾ ���� ���Ϳ� ��ũ�� ���⺤�͸� ����
		float dot = vheadToPlayer.dot(_transform._vForward.x, _transform._vForward.y, _transform._vForward.z);
		
		if (_eachActionTime > 2.0f) {
			float action = (float)Math.random();
			if (action < 0.25f) { // �ð���� ȸ��
				_action = CONSTANTS.ENEMY_ACTION.ROTATE_CLOCK;
			} else if (action < 0.5f) { // �ݽð���� ȸ��
				_action = CONSTANTS.ENEMY_ACTION.ROTATE_ANTICLOCK;
			} else if (action < 0.9f) { // ������ �̵�
				_action = CONSTANTS.ENEMY_ACTION.MOVE_FORWARD;
			} else {                   // �ڷ� �̵�
				_action = CONSTANTS.ENEMY_ACTION.MOVE_BACK;
			}
			
			// �ൿ �ð� �ʱ�ȭ
			_eachActionTime = 0.0f;
		}
		
		switch(_action) {
		case CONSTANTS.ENEMY_ACTION.MOVE_FORWARD:
			_transform.speedup(deltaTime);
			break;
		case CONSTANTS.ENEMY_ACTION.MOVE_BACK:
			_transform.speeddown(deltaTime);
			break;
		case CONSTANTS.ENEMY_ACTION.ROTATE_ANTICLOCK:
			if (dot < (float)(15.0f * Math.PI / 180.0) && dot >= 0.0f) { // ���� 15�� ���� ���� ��� �߻�
				fire(deltaTime);
			}
			_transform.rotateYAntiClockWise(deltaTime);
			break;
		case CONSTANTS.ENEMY_ACTION.ROTATE_CLOCK:
			if (dot > (float)Math.cos(Math.toRadians(8.0)) && dot > (float)Math.cos(Math.toRadians(352.0))) { // ���� 15�� ���� ���� ��� �߻�
				fire(deltaTime);
			}
			_transform.rotateYClockWise(deltaTime);
			break;
		}
	}
	
	// �Ѿ��� ���� ���߾����� �˻�
	public void checkHittingEnemy() {
		if(_registeredWeapons.size() > 0) {
			// �÷��̾��� �浹 ��ǥ ����
			Vector3f playerPosition = _gobjects.getGO("player")._transform._vCollisionPosition;
			float playerRadius = _gobjects.getGO("player")._transform._radius;
			
			for (Weapon w : _registeredWeapons) {
				if(isCollided(w._transform._vCollisionPosition, playerPosition, w._transform._radius + playerRadius)) {
					w._alive = false; // �Ѿ� ���� ���·�
					sendlog("������ "+w.getType()+"�� �÷��̾��� ����!!");
					((Tank)_gobjects.getGO("player"))._money -= w.getPower(); // �ҷ��� �Ŀ���ŭ ���� ���� ����
					if (((Tank)_gobjects.getGO("player"))._money < 0)
						((Tank)_gobjects.getGO("player"))._money = 0;         // 0���� ������ 0����
					_money += w.getPower();                                   // �ҷ��� �Ŀ���ŭ ���� ����
				}
			}
		}
	}
}
