package core.components;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import core.CONSTANTS;
import core.WindowMngr;

public class Camera {
	private Matrix4f _matView;
	private Matrix4f _matProjection;
	
	private Vector3f _vPosition;
	public Vector3f _vForward;
	private Vector3f _vAngle;
	
	public Camera() {
		_matView = new Matrix4f().identity();
		_matProjection = new Matrix4f().identity();
		_vPosition = new Vector3f(0.0f, 0.0f, 5.0f);
		_vAngle = new Vector3f(0.0f, 0.0f, 0.0f);
		_vForward = new Vector3f(0.0f, 0.0f, 0.0f);
		
		// 투영행렬 셋
		setPerspectiveMatrix();
	}
	public void init() { }
	
	public Matrix4f getViewMatrix() { return _matView; }
	public Matrix4f getProjectionMatrix() { return _matProjection; }
	
	public void setPosition(Vector3f p) { _vPosition.x = p.x; _vPosition.y = p.y; _vPosition.z = p.z; }
	public void setForward(Vector3f f) { _vForward.x = f.x; _vForward.y = f.y; _vForward.z = f.z; }
	public void setAngle(Vector3f a) { _vAngle.x = a.x; _vAngle.y = a.y; _vAngle.z = a.z; }

	public Vector3f getAngle() { return _vAngle; }
	
	// 원근 투영 행렬 설정 (교체사항 없음)
	private void setPerspectiveMatrix() {
		float aspectRatio = (float) 1080 / 760;
		_matProjection = _matProjection.perspective((float) Math.toRadians(60.0f), aspectRatio, CONSTANTS.CAMERA_NEEDS.Z_NEAR, CONSTANTS.CAMERA_NEEDS.Z_FAR);
	}
	
	// 뷰 행렬 설정
	private void updateViewMatrix() {
		/*_matView = _matView.identity();
		_matView = _matView.rotateX((float)Math.toRadians(-_vAngle.x));
		_matView = _matView.rotateY((float)Math.toRadians(-_vAngle.y));
		_matView = _matView.rotateZ((float)Math.toRadians(-_vAngle.z));
		_matView = _matView.translate(-_vPosition.x, -_vPosition.y, -_vPosition.z);*/
		
		Vector3f right = new Vector3f(0.0f, 0.0f, 0.0f);
		_vForward.cross(0.0f, 1.0f, 0.0f, right);
		
		Vector3f up = new Vector3f(0.0f, 0.0f, 0.0f);
		right.cross(_vForward, up);
		
		Vector3f back = new Vector3f(-_vForward.x, -_vForward.y, -_vForward.z);
		
		_matView = _matView.identity();
		_matView.m00(right.x);
		_matView.m01(up.x);
		_matView.m02(back.x);
		_matView.m10(right.y);
		_matView.m11(up.y);
		_matView.m12(back.y);
		_matView.m20(right.z);
		_matView.m21(up.z);
		_matView.m22(back.z);
		
		_matView.translate(-_vPosition.x, -_vPosition.y, -_vPosition.z);
	}

	public void update(float interval) {
		updateViewMatrix();
	}
}
