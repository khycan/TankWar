package game;

import java.util.HashMap;

import org.joml.Vector3f;

import core.CONSTANTS;
import core.GameObject;
import core.IGameLogic;
import core.components.Camera;
import core.components.Transformation;
import game.Weapon.Bomb;
import game.Weapon.Gun;
import game.Weapon.Laser;
import game.Weapon.Weapon;

public class player extends Tank {
	
	//////////////////////
	// 컴포넌트
	public Camera _camera;
	public player(String name, IGameLogic go, ThreadMessenger tm) {
		super(name, go, tm);
		
		// 안그림
		_drawMode = CONSTANTS.OBJECT_DRAWMODE.NON_DRAW;
		
		// 카메라 생성
		_camera = new Camera();
		_camera.setPosition(new Vector3f(-150.0f, 2.0f, 150.0f));
		
		// 2m 높이 로 설정
		_transform.setPosition(-150.0f, 2.0f, 150.0f);
	}
	
	@Override
	public void update(float deltaTime) {
		purchase(0);
		
		_transform.update(deltaTime);
		move(deltaTime);
		checkCollisionforSkybox();
		checkCollisionforEnemy();
		_transform.setCollisionPosition(_transform._vPosition); // 충돌 검사 위치도 업데이트
		_camera.setPosition(_transform._vPosition);
		_camera.setForward(_transform.getForward());
 		_camera.setAngle(_transform.getAngle());
		_camera.update(deltaTime);
		sendMyInfo(0);
		
		checkHittingEnemy();
	}
///////////////////////////////////////////////////////////
	// 적과 충돌했는지 확인
	public void checkCollisionforEnemy() {
		Vector3f enemyPosition = _gobjects.getGO("enemy")._transform._vCollisionPosition;
		float enemyRadius = _gobjects.getGO("enemy")._transform._radius;
		
		if(isCollided(_transform._vCollisionPosition, enemyPosition, _transform._radius + enemyRadius + 0.5f)) {
			_transform._speed = 0.0f;
		}
	}

	// 총알이 적을 맞추었는지 검사
	public void checkHittingEnemy() {
		if(_registeredWeapons.size() > 0) {
			// 적의 충돌 좌표 얻어옴
			Vector3f enemyPosition = _gobjects.getGO("enemy")._transform._vCollisionPosition;
			float enemyRadius = _gobjects.getGO("enemy")._transform._radius;
			
			for (Weapon w : _registeredWeapons) {
				if(isCollided(w._transform._vCollisionPosition, enemyPosition, w._transform._radius + enemyRadius)) {
					w._alive = false; // 총알 죽음 상태로
					sendlog("아군이 "+w.getType()+"로 적을 격추!!");
					((Tank)_gobjects.getGO("enemy"))._money -= w.getPower(); // 불렛의 파워만큼 적의 돈을 없앰
					if (((Tank)_gobjects.getGO("enemy"))._money < 0)
						((Tank)_gobjects.getGO("enemy"))._money = 0;         // 0보다 작으면 0으로
					_money += w.getPower();                                  // 불렛의 파워만큼 돈을 얻음
				}
			}
		}
	}

}
