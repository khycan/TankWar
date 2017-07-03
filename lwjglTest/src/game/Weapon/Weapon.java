package game.Weapon;

import java.util.HashMap;

import core.GameObject;
import core.components.Transformation;

public class Weapon extends GameObject {
	// �˼����� ���׷�(�÷��̾��� ���Ͱ��� �ȹ޾���) �̷��� ó��
	public float _localForwardX;
	public float _localForwardY;
	public float _localForwardZ;
	
	public boolean _alive; // �浹, ���� ������� ������ �����ؾ��� ��� false�� �ȴ�
	
	protected String _type;
	protected int _price;
	protected int _power;
	protected float _range;         // ���⺰ �����Ÿ�
	protected float _wholeDistance; // ��ü �̵��Ÿ�; �����Ÿ����� Ŀ���� ź�� �����Ѵ�.
	
	public int getPrice() { return _price; }
	public int getPower() { return _power; }
	public String getType() { return _type; }
	public void updateMovingDistance(float deltaTime) {
		_wholeDistance += deltaTime*_transform._speed;
		
		if(_wholeDistance >= _range) { // ������ �������� Ŀ�� ��� ���� ���·� ��ȯ
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
		_transform.setCollisionPosition(_transform._vPosition); // �浹 �˻� ��ġ�� ������Ʈ
		updateMovingDistance(deltaTime);
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

}
