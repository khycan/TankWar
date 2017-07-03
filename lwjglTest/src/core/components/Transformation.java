package core.components;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import core.CONSTANTS;

public class Transformation{
	public Vector3f _vPosition;
	public Vector3f _vCollisionPosition;
	public float _radius; // 구 충돌검사를 위한 반지름
	
	public Vector3f _vForward;
	private Vector3f _angle;
	
	private Matrix4f _matWorld;
	private Vector3f _vScaleValue; 
	
	private boolean _ismovingForward = false;
	private boolean _ismovingBack = false;
	
	public float _speed = 0.0f;  // 0 m/s
	private float MAX_SPEED = 0.0f;
	private float _accel = 0.0f;
	private float _rotationSpeed = 0.0f;  // 30도/s
	
	public Transformation(Vector3f p) {
		_vPosition = p;
		_vCollisionPosition = new Vector3f(p.x,p.y,p.z);
		_matWorld = new Matrix4f();
		_angle = new Vector3f(0.0f, 0.0f, 0.0f);
		_vForward = new Vector3f(0.0f, 0.0f, -1.0f);
		_vScaleValue = new Vector3f(1.0f, 1.0f, 1.0f);
	}
	public Transformation(float x, float y, float z) {
		_vPosition = new Vector3f(x,y,z);
		_vCollisionPosition = new Vector3f(x,y,z);
		_matWorld = new Matrix4f();
		_angle = new Vector3f(0.0f, 0.0f, 0.0f);
		_vForward = new Vector3f(0.0f, 0.0f, -1.0f);
		_vScaleValue = new Vector3f(1.0f, 1.0f, 1.0f);
	}
	
	public void setPhysics(float max_speed, float accel, float rotationspeed) {
		MAX_SPEED = max_speed;
		_accel = accel;
		_rotationSpeed = rotationspeed;
	}
	
	// 방향 벡터 업데이트
	public void updateForwardVector() {
		Vector4f newForward = new Vector4f(0.0f, 0.0f, -1.0f, 0.0f).mul(_matWorld);
		_vForward.x = newForward.x;
		_vForward.y = newForward.y;
		_vForward.z = newForward.z;
		
		_vForward = _vForward.normalize();
	}
	public void move(float deltaTime) {
		// 속도가 0이면 이동 안함
		if (_speed == 0.0f) return;
		
		if (_ismovingForward || _ismovingBack) { // 움직이는 중
			_speed = (Math.abs(_speed)>MAX_SPEED)?MAX_SPEED*Math.signum(_speed):_speed;
		} else { // 멈추는중 or 멈춤
			_speed -= _accel*Math.signum(_speed)*deltaTime;
			_speed = (_speed*Math.signum(_speed)<=0.05f)?0.0f:_speed;
		}
		
		// 이동
		_vPosition = _vPosition.add(_vForward.mul(_speed*deltaTime));
		_vForward.mul(Math.signum(_speed));
		
		// 방향벡터 보정
		_vForward.normalize();
		// 플래그 내림
		_ismovingForward = false;
		_ismovingBack = false;
	}

	public void speedup(float deltaTime) {
		_speed += _accel*deltaTime;
		_ismovingForward = true;
	}
	public void speeddown(float deltaTime) {
		_speed -= _accel*deltaTime;
		_ismovingBack = true;
	}
	
	// 시계방향으로 Y축 회전
	public void rotateYClockWise(float deltaTime) {
		_angle.y -= deltaTime * _rotationSpeed;
		
		if (_angle.y < 0.0f) {
			_angle.y += 360.0f;
		} else {
			_angle.y = _angle.y%360.0f;
		}
	}
	public void rotateYAntiClockWise(float deltaTime) {
		_angle.y += deltaTime * _rotationSpeed;
		
		if (_angle.y < 0.0f) {
			_angle.y += 360.0f;
		} else {
			_angle.y = _angle.y%360.0f;
		}
	}
	
	// 월드 행렬 생성
	public void updateWorldMatrix() {
		_matWorld = _matWorld.identity();
		_matWorld = _matWorld.translate(_vPosition);
		_matWorld = _matWorld.rotateX((float)Math.toRadians(_angle.x));
		_matWorld = _matWorld.rotateY((float)Math.toRadians(_angle.y));
		_matWorld = _matWorld.rotateZ((float)Math.toRadians(_angle.z));
		_matWorld = _matWorld.scale(_vScaleValue);
	}
	
	public Vector3f getPosition() { return _vPosition; }
	public Vector3f getForward() { return _vForward; }
	public Vector3f getAngle() { return _angle; }
	public Matrix4f getWorldMatrix() { return _matWorld; }
	public void setPosition(float x, float y, float z) { _vPosition.x = x; _vPosition.y = y; _vPosition.z = z; }
	public void setPosition(Vector3f p) { _vPosition.x = p.x; _vPosition.y = p.y; _vPosition.z = p.z; }
	public void setCollisionPosition(Vector3f c) { _vCollisionPosition.x = c.x; _vCollisionPosition.y = c.y; _vCollisionPosition.z = c.z; }
	public void setForward(Vector3f f) { _vForward.x = f.x; _vForward.y = f.y; _vForward.z = f.z; }
	public void setAngle(Vector3f a) { _angle.x = a.x; _angle.y = a.y; _angle.z = a.z; }
	public void enableMovingForward() { _ismovingForward = true;}
	public void disableMovingForward() { _ismovingForward = false;}
	public boolean getMovingForward() { return _ismovingForward; }
	public void enableMovingBack() { _ismovingBack = true;}
	public void disableMovingBack() { _ismovingBack = false;}
	public boolean getMovingBack() { return _ismovingBack; }
	public void setScale(float x, float y, float z) { _vScaleValue.x = x; _vScaleValue.y = y; _vScaleValue.z = z; }
	public void setSpeed(float speed) { _speed = speed; }
	
	
	public void update(float interval) {
		updateWorldMatrix();
		updateForwardVector();
	}
}
