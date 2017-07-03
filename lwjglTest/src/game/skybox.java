package game;

import java.io.IOException;

import core.components.OBJParser;
import core.components.Texture;

public class skybox extends Cube {
    public skybox(String name, float width, float depth, float height) {
		super(name, width, depth, height);
		
	    _transform.setPosition(0.0f, 0.0f, 0.0f);
		try {
			_mesh = OBJParser.LoadData("skybox.obj");
			_mesh.setTexture(new Texture("skybox.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
