package core.systems;

import	static	org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.joml.*;
import org.lwjgl.system.MemoryStack;

import core.Utils;


public class ShaderProgram {
	private int programId;
	private	int	vertexShaderId;
	private	int	fragmentShaderId;
	
	private HashMap<String, Integer> _uniforms;
	
	public ShaderProgram() throws Exception {
		programId = glCreateProgram();
		if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
		_uniforms = new HashMap<String, Integer>();
	}
	
	public void loadShader(String shaderName) {
		String resourcePath = "Resources/shaders/";
		
		// vertex, fragment 쉐이더 생성 및 링크
		try {
			createVertexShader(Utils.loadResource(resourcePath + shaderName + ".vs"));
			createFragmentShader(Utils.loadResource(resourcePath + shaderName + ".fs"));
			link();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createVertexShader(String shaderCode) throws Exception {
		vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
	}
	
	public void createFragmentShader(String shaderCode) throws Exception {
		fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
	}
	
	protected int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderId = glCreateShader(shaderType);
		if (shaderId == 0) {
			throw new Exception("Error creating shader. Type : " + shaderType);
		}

		glShaderSource(shaderId, shaderCode);
		glCompileShader(shaderId);
		
		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
			throw new Exception("Error compiling Shader code : " + glGetShaderInfoLog(shaderId, 1024));
		}
		
		glAttachShader(programId, shaderId);
		
		return shaderId;
	}
	
	public void link() throws Exception {
		glLinkProgram(programId);
		if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
			throw new Exception("Error linking Shader code :" + glGetProgramInfoLog(programId, 1024));
		}
		
		if (vertexShaderId != 0) {
			glDetachShader(programId, vertexShaderId);
		}
		if (fragmentShaderId != 0) {
			glDetachShader(programId, fragmentShaderId);
		}
		
		glValidateProgram(programId);
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
			System.err.println("Warning validating Shader code : " + glGetProgramInfoLog(programId, 1024));
		}
	}
	
	public void bind() {
		glUseProgram(programId);
	}
	
	public void unbind() {
		glUseProgram(0);
	}
	
	public void cleanup() {
		unbind();
		if (programId != 0) {
			glDeleteProgram(programId);
		}
	}
	
	public void createUniform(String uniformName) {
		int uniformLocation = glGetUniformLocation(programId, uniformName);
		
		if (uniformLocation < 0) {
			System.err.println("Could not find uniform : "+uniformName);
			return;
		}
		_uniforms.put(uniformName, uniformLocation);
	}
	public void setUniform(String uniformName, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			value.get(fb);
			glUniformMatrix4fv(_uniforms.get(uniformName), false, fb);
		}
	}
	public void setUniform(String uniformName, int value) {
		glUniform1i(_uniforms.get(uniformName), value);
	}

}
