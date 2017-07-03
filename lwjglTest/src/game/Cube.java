package game;

import core.CONSTANTS;

import core.GameObject;
import core.Utils;
import core.WindowMngr;
import core.components.Camera;
import core.components.Mesh;
import core.components.OBJParser;
import core.components.Texture;
import core.components.Transformation;
import core.systems.ShaderProgram;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector3f;

public class Cube extends GameObject {
	protected float _width;
	protected float _depth;
	protected float _height;
	
	////////////////////
	// ÄÄÆ÷³ÍÆ®
	
	public Cube(String name, float width, float depth, float height) {
		super(name);
		_transform = new Transformation(0.0f, 0.0f, 0.0f);
		_transform.setScale(width, height, depth);
	}
	
	@Override
	public void update(float deltaTime) {
		_transform.update(deltaTime);
	}

	@Override
	public void cleanup() {

	}

	@Override
	public void init() throws Exception {
	}

}
