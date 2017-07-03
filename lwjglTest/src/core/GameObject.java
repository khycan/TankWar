package core;

import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import core.components.Mesh;
import core.components.Transformation;

abstract public class GameObject {
	protected IGameLogic _gobjects = null;
	protected String name;
	protected int _drawMode = CONSTANTS.OBJECT_DRAWMODE.REGULAR;
	
	////////////////////
	// 컴포넌트
	public Mesh _mesh;
	public Transformation _transform;
	
	public GameObject(String name) {
		this.name = name;
	}
	
	public String getName() { return name; }
	public void setName(String s) { name = s; }
	
	public abstract void init() throws Exception;
	public abstract void update(float deltaTime);
	public void render() { _mesh.render(); }
	public abstract void cleanup();
/////////////////////////////////////////////////////////
	public int getDrawMode() { return _drawMode; }
	public void move(float deltaTime) {
		// 오브젝트 위치 이동
		_transform.move(deltaTime);
	}
}
