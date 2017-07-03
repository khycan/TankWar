package game.Weapon;

import java.io.IOException;
import java.util.HashMap;

import org.joml.Vector3f;

import core.GameObject;
import core.components.OBJParser;
import core.components.Texture;
import core.components.Transformation;

public class Laser extends Weapon {
	public Laser(String name) {
		super(name);
		// 이동 관련 설정
		_transform.setPhysics(100.0f, 0.0f, 0.0f);
		
		// 메쉬 설정
		try {
			_mesh = OBJParser.LoadData("bullet.obj");
			_mesh.setTexture(new Texture("laser.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 무기 설정
		_type = "laser";
		_power = 40;
		_price = 30;
		_range = 400.0f;
		_transform.setSpeed(100.0f);
	}
}
