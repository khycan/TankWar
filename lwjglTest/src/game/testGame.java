package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import core.CONSTANTS;
import core.GameObject;
import core.IGameLogic;
import core.WindowMngr;
import core.mouseEvent;
import core.components.OBJParser;
import core.components.Texture;
import core.components.Transformation;
import core.systems.Renderer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class testGame extends IGameLogic {
    private final Renderer renderer;
    private ThreadMessenger _tm;
    
    public testGame(ThreadMessenger tm) {
    	_tm = tm;
    	renderer = new Renderer();
    }
    
	@Override
    public void init(WindowMngr window) throws Exception {
		// player ����
		player Player = new player("player", this, _tm);
		attach(Player);
		
        // ��ũ ����
        enemyTank enemy = new enemyTank("enemy", this, _tm);
        attach(enemy);
		
		// ���� ����
		ground land = new ground("ground", 500.0f, 500.0f, 1.0f);
		land._mesh = OBJParser.LoadData("ground.obj");
		land._mesh.setTexture(new Texture("ground.png"));
        attach(land);
        
		// ���� ����
        Cube cube = new Cube("cube", 1.0f, 1.0f, 1.0f);
        cube._transform.setPosition(-30.0f, 1.0f, -20.0f);
		cube._mesh = OBJParser.LoadData("cube.obj");
		cube._mesh.setTexture(new Texture("woodCube.png"));
        attach(cube);
        
        // �ϴ� ����
        skybox sb = new skybox("skybox", 500.0f, 500.0f, 500.0f);
        attach(sb);
        
        // �� ������Ʈ �ʱ�ȭ
        for(String key : _gobjects.keySet()) {
        	_gobjects.get(key).init();
        }
        
		renderer.init();
    }
    
    @Override
    public void input(WindowMngr window, mouseEvent mouse, float interval) {
    	// ��, �� �̵� ���� Ű �ڵ鸵
        if ( window.isKeyPressed(GLFW_KEY_W) ) {
        	_gobjects.get("player")._transform.speedup(interval);
        } else if ( window.isKeyPressed(GLFW_KEY_S) ) {
        	_gobjects.get("player")._transform.speeddown(interval);
        }
        
    	if (window.isKeyPressed(GLFW_KEY_A)) {
    		_gobjects.get("player")._transform.rotateYAntiClockWise(interval);
    	} else if (window.isKeyPressed(GLFW_KEY_D)) {
        	_gobjects.get("player")._transform.rotateYClockWise(interval);
        }
        
    	// �ҷ� ����
        if (mouse.isLeftButtonPressed()) {
        	((player)_gobjects.get("player")).fire(interval);
        }
    }

    @Override
    public void update(float interval) {
    	// �ҷ� ����ġ
    	((Tank)_gobjects.get("player")).attachFiredWeapons();
    	((Tank)_gobjects.get("enemy")).attachFiredWeapons();
    	
    	// game object ��ȸ�ϸ� update
        Iterator<String> keyIterator = _gobjects.keySet().iterator();
        while (keyIterator.hasNext()) {
        	String key = keyIterator.next();
        	_gobjects.get(key).update(interval);
        }
        
        // �ҷ� ���� ó��
        ((Tank)_gobjects.get("player")).checkBulletDeath();
        ((Tank)_gobjects.get("enemy")).checkBulletDeath();
    }

    @Override
    public void render(WindowMngr window) {
        if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        renderer.render(window, _gobjects);
    }

}
