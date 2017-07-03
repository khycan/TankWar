package core.systems;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import core.CONSTANTS;
import core.GameObject;
import core.WindowMngr;
import game.Cube;
import game.player;
import core.components.*;

public class Renderer {
    private Matrix4f _matView;
    private Matrix4f _matWorld;
    private Matrix4f _matProjection;
	
	private ShaderProgram _shaderMngr;
	
    public Renderer() {
    	
    }
    
    public void init() {
    	// ���� �׽�Ʈ Ȱ��
    	glEnable(GL_DEPTH_TEST);
    	// ���̴� ������ �ʱ�ȭ
    	try {
    		_shaderMngr = new ShaderProgram();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
    public void render(WindowMngr window, HashMap<String, GameObject> go) {
    	clear();
    	
    	// ������� �ε�
    	_matProjection = ((player)go.get("player"))._camera.getProjectionMatrix();
    	// ����� �ε�
    	_matView = ((player)go.get("player"))._camera.getViewMatrix();
    	
    	// game object ��ȸ�ϸ� render
        Iterator<String> keyIterator = go.keySet().iterator();
        while (keyIterator.hasNext()) {
        	String currentObj = keyIterator.next();
        	
        	// ���� �׸����
        	if (go.get(currentObj).getDrawMode() == CONSTANTS.OBJECT_DRAWMODE.REGULAR) {
	        	_shaderMngr.loadShader(go.get(currentObj)._mesh.getShader());
	        	
	        	///////////////////////////////////////////////
	        	// ���̴� ���ε�
	        	_shaderMngr.bind();
	        	
	        	_shaderMngr.createUniform("world");
	        	_shaderMngr.createUniform("view");
	        	_shaderMngr.createUniform("projection");
	        	_shaderMngr.createUniform("texture_sampler");
	        	
	    		_shaderMngr.setUniform("world", go.get(currentObj)._transform.getWorldMatrix());
	    		_shaderMngr.setUniform("view", _matView);
	    		_shaderMngr.setUniform("projection", _matProjection);
	    		_shaderMngr.setUniform("texture_sampler", 0);

	    		// mesh �׸�
	    		go.get(currentObj).render();
	    		
				///////////////////////////////////////////////
				// ���̴� ����ε�
				_shaderMngr.unbind();
				
        	}
        }
    }
    
    void renderSkybox(GameObject skybox) {
    	_shaderMngr.loadShader(skybox._mesh.getShader());
    	
    	///////////////////////////////////////////////
    	// ���̴� ���ε�
    	_shaderMngr.bind();
    	
    	_shaderMngr.createUniform("world");
    	_shaderMngr.createUniform("projection");
    	_shaderMngr.createUniform("texture_sampler");
    	
		_shaderMngr.setUniform("world", skybox._transform.getWorldMatrix());
		_shaderMngr.setUniform("projection", _matProjection);
		_shaderMngr.setUniform("texture_sampler", 0);

		// mesh �׸�
		skybox._mesh.render();
		
		///////////////////////////////////////////////
		// ���̴� ����ε�
		_shaderMngr.unbind();
    }
}
