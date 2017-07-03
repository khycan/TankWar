package core.components;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.system.MemoryUtil;

import core.CONSTANTS;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh{
	private final int vaoId;
	private final List<Integer> _vboIdList;
	private final int vertexCount;
	
	private Texture _texture;
	private String _Shader = "basicObject";
	
	public Mesh(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		FloatBuffer verticesBuffer = null;
		FloatBuffer textureCoordsBuffer = null;
		FloatBuffer normalsBuffer = null;
		IntBuffer indicesBuffer = null;
		
		try {
			vertexCount = indices.length;
			_vboIdList = new ArrayList();
			
			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);
			
			// 위치 버텍스 VBO
			int vboId = glGenBuffers();
			_vboIdList.add(vboId);
			verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
			verticesBuffer.put(positions).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			
			// 텍스쳐 좌표 VBO
			vboId = glGenBuffers();
			_vboIdList.add(vboId);
			textureCoordsBuffer = MemoryUtil.memAllocFloat(textureCoords.length);
			textureCoordsBuffer.put(textureCoords).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
			
			// 법선 VBO
			vboId = glGenBuffers();
			_vboIdList.add(vboId);
			normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
			normalsBuffer.put(normals).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
			
			// 인덱스 VBO
			vboId = glGenBuffers();
			_vboIdList.add(vboId);
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);			

			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
			
		} finally {
			if(verticesBuffer != null) {
				MemoryUtil.memFree(verticesBuffer);
			}
			if(indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
			if(textureCoordsBuffer != null) {
				MemoryUtil.memFree(textureCoordsBuffer);
			}
		}
	}
	
	public Mesh(float[] positions, float[] textureCoords, int[] indices) {
		FloatBuffer verticesBuffer = null;
		FloatBuffer textureCoordsBuffer = null;
		FloatBuffer normalsBuffer = null;
		IntBuffer indicesBuffer = null;
		
		try {
			vertexCount = indices.length;
			_vboIdList = new ArrayList();
			
			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);
			
			// 위치 버텍스 VBO
			int vboId = glGenBuffers();
			_vboIdList.add(vboId);
			verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
			verticesBuffer.put(positions).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			
			// 텍스쳐 좌표 VBO
			vboId = glGenBuffers();
			_vboIdList.add(vboId);
			textureCoordsBuffer = MemoryUtil.memAllocFloat(textureCoords.length);
			textureCoordsBuffer.put(textureCoords).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
			
			// 인덱스 VBO
			vboId = glGenBuffers();
			_vboIdList.add(vboId);
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);			

			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
			
		} finally {
			if(verticesBuffer != null) {
				MemoryUtil.memFree(verticesBuffer);
			}
			if(indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
			if(textureCoordsBuffer != null) {
				MemoryUtil.memFree(textureCoordsBuffer);
			}
		}
	}
	
	public int getVaoId() {
		return vaoId;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public void setTexture(Texture texture) { _texture = texture; }
	public Texture getTexture() { return _texture; }
	public void setShader(String s) { _Shader = s; }
	public String getShader() { return _Shader; }
	
	
	public void render() {
        // 텍스쳐 활성
        glActiveTexture(GL_TEXTURE0);
        // 텍스쳐 바인딩
        glBindTexture(GL_TEXTURE_2D, _texture.getId());

        // mesh 렌더링
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // 상태 되돌림
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);  // 텍스쳐 unbind
    }
	
	public void cleanup() {
		glDisableVertexAttribArray(0);
		
		// vbo 삭제
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		for (int vboId : _vboIdList) {
			glDeleteBuffers(vboId);
		}
		
		_texture.cleanup();
		
		// vao 삭제
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}
}
