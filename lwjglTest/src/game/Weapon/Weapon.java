package game.Weapon;

import java.util.HashMap;

import core.GameObject;
import core.components.Transformation;

public class Weapon extends GameObject {
	// 알수없는 버그로(플레이어의 벡터값이 안받아짐) 이렇게 처리
	public float _localForwardX;
	public float _localForwardY;
	public float _localForwardZ;
	
	public boolean _alive; // 충돌, 범위 벗어남등의 이유로 제거해야할 경우 false가 된다
	
	protected String _type;
	protected int _price;
	protected int _power;
	protected float _range;         // 무기별 사정거리
	protected float _wholeDistance; // 전체 이동거리; 사정거리보다 커지면 탄을 해제한다.
	
	public int getPrice() { return _price; }
	public int getPower() { return _power; }
	public String getType() { return _type; }
	public void updateMovingDistance(float deltaTime) {
		_wholeDistance += deltaTime*_transform._speed;
		
		if(_wholeDistance >= _range) { // 설정된 범위보다 커질 경우 죽음 상태로 전환
			_alive = false;
		}
	}
////////////////////////////////////////////////////
	public Weapon(String name) {
		super(name);
		_alive = true;
		_wholeDistance = 0.0f;
		_transform = new Transformation(0.0f, 0.0f, 0.0f);
		_transform._radius = 1.0f;
	}
	
	@Override
	public void init() throws Exception {

	}

	@Override
	public void update(float deltaTime) {
		_transform.update(deltaTime);
		_transform._vForward.x = _localForwardX;
		_transform._vForward.y = _localForwardY;
		_transform._vForward.z = _localForwardZ;
		move(deltaTime);
		_transform.setCollisionPosition(_transform._vPosition); // 충돌 검사 위치도 업데이트
		updateMovingDistance(deltaTime);
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

}
