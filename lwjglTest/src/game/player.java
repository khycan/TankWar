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
	// ������Ʈ
	public Camera _camera;
	public player(String name, IGameLogic go, ThreadMessenger tm) {
		super(name, go, tm);
		
		// �ȱ׸�
		_drawMode = CONSTANTS.OBJECT_DRAWMODE.NON_DRAW;
		
		// ī�޶� ����
		_camera = new Camera();
		_camera.setPosition(new Vector3f(-150.0f, 2.0f, 150.0f));
		
		// 2m ���� �� ����
		_transform.setPosition(-150.0f, 2.0f, 150.0f);
	}
	
	@Override
	public void update(float deltaTime) {
		purchase(0);
		
		_transform.update(deltaTime);
		move(deltaTime);
		checkCollisionforSkybox();
		checkCollisionforEnemy();
		_transform.setCollisionPosition(_transform._vPosition); // �浹 �˻� ��ġ�� ������Ʈ
		_camera.setPosition(_transform._vPosition);
		_camera.setForward(_transform.getForward());
 		_camera.setAngle(_transform.getAngle());
		_camera.update(deltaTime);
		sendMyInfo(0);
		
		checkHittingEnemy();
	}
///////////////////////////////////////////////////////////
	// ���� �浹�ߴ��� Ȯ��
	public void checkCollisionforEnemy() {
		Vector3f enemyPosition = _gobjects.getGO("enemy")._transform._vCollisionPosition;
		float enemyRadius = _gobjects.getGO("enemy")._transform._radius;
		
		if(isCollided(_transform._vCollisionPosition, enemyPosition, _transform._radius + enemyRadius + 0.5f)) {
			_transform._speed = 0.0f;
		}
	}

	// �Ѿ��� ���� ���߾����� �˻�
	public void checkHittingEnemy() {
		if(_registeredWeapons.size() > 0) {
			// ���� �浹 ��ǥ ����
			Vector3f enemyPosition = _gobjects.getGO("enemy")._transform._vCollisionPosition;
			float enemyRadius = _gobjects.getGO("enemy")._transform._radius;
			
			for (Weapon w : _registeredWeapons) {
				if(isCollided(w._transform._vCollisionPosition, enemyPosition, w._transform._radius + enemyRadius)) {
					w._alive = false; // �Ѿ� ���� ���·�
					sendlog("�Ʊ��� "+w.getType()+"�� ���� ����!!");
					((Tank)_gobjects.getGO("enemy"))._money -= w.getPower(); // �ҷ��� �Ŀ���ŭ ���� ���� ����
					if (((Tank)_gobjects.getGO("enemy"))._money < 0)
						((Tank)_gobjects.getGO("enemy"))._money = 0;         // 0���� ������ 0����
					_money += w.getPower();                                  // �ҷ��� �Ŀ���ŭ ���� ����
				}
			}
		}
	}

}
