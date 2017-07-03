package game;

import org.joml.Vector3f;

import core.CONSTANTS;
import core.IGameLogic;
import core.components.Camera;
import core.components.OBJParser;
import core.components.Texture;
import game.Weapon.Weapon;

public class enemyTank extends Tank {
	
	// 탱크의 현재 행동값을 저장할 놈
	private int _action = -1;
	private float _eachActionTime = 0.0f;

	public enemyTank(String name, IGameLogic go, ThreadMessenger tm) {
		super(name, go, tm);
		
		// 기본 위치 설정
		_transform.setPosition(30.0f, 0.0f, -30.0f);
		_transform.setPhysics(40.0f, 20.0f, 120.0f);
		
		// 메쉬 설정
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
		_transform._vCollisionPosition.x = _transform._vPosition.x;  // 적탱크의 충돌 검사 위치 업데이트
		_transform._vCollisionPosition.y = 2.0f;                     // 플레이어와 눈높이를 맞추기 위해 y값을 다르게 함
		_transform._vCollisionPosition.z = _transform._vPosition.z;
		sendMyInfo(1);

		checkHittingEnemy();
	}
///////////////////////////////////////////////////////
	
	// 탱크 AI
	public void autoMove(float deltaTime) {
		// 시간 더함
		_eachActionTime += deltaTime;
		
		// 플레이어를 바라보는 벡터
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
		
		// 플레이어를 보는 벡터와 탱크의 방향벡터를 내적
		float dot = vheadToPlayer.dot(_transform._vForward.x, _transform._vForward.y, _transform._vForward.z);
		
		if (_eachActionTime > 2.0f) {
			float action = (float)Math.random();
			if (action < 0.25f) { // 시계방향 회전
				_action = CONSTANTS.ENEMY_ACTION.ROTATE_CLOCK;
			} else if (action < 0.5f) { // 반시계방향 회전
				_action = CONSTANTS.ENEMY_ACTION.ROTATE_ANTICLOCK;
			} else if (action < 0.9f) { // 앞으로 이동
				_action = CONSTANTS.ENEMY_ACTION.MOVE_FORWARD;
			} else {                   // 뒤로 이동
				_action = CONSTANTS.ENEMY_ACTION.MOVE_BACK;
			}
			
			// 행동 시간 초기화
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
			if (dot < (float)(15.0f * Math.PI / 180.0) && dot >= 0.0f) { // 각이 15도 보다 작을 경우 발사
				fire(deltaTime);
			}
			_transform.rotateYAntiClockWise(deltaTime);
			break;
		case CONSTANTS.ENEMY_ACTION.ROTATE_CLOCK:
			if (dot > (float)Math.cos(Math.toRadians(8.0)) && dot > (float)Math.cos(Math.toRadians(352.0))) { // 각이 15도 보다 작을 경우 발사
				fire(deltaTime);
			}
			_transform.rotateYClockWise(deltaTime);
			break;
		}
	}
	
	// 총알이 적을 맞추었는지 검사
	public void checkHittingEnemy() {
		if(_registeredWeapons.size() > 0) {
			// 플레이어의 충돌 좌표 얻어옴
			Vector3f playerPosition = _gobjects.getGO("player")._transform._vCollisionPosition;
			float playerRadius = _gobjects.getGO("player")._transform._radius;
			
			for (Weapon w : _registeredWeapons) {
				if(isCollided(w._transform._vCollisionPosition, playerPosition, w._transform._radius + playerRadius)) {
					w._alive = false; // 총알 죽음 상태로
					sendlog("적군이 "+w.getType()+"로 플레이어을 격추!!");
					((Tank)_gobjects.getGO("player"))._money -= w.getPower(); // 불렛의 파워만큼 적의 돈을 없앰
					if (((Tank)_gobjects.getGO("player"))._money < 0)
						((Tank)_gobjects.getGO("player"))._money = 0;         // 0보다 작으면 0으로
					_money += w.getPower();                                   // 불렛의 파워만큼 돈을 얻음
				}
			}
		}
	}
}
