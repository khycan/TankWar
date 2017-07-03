package game.Weapon;

import core.components.OBJParser;
import core.components.Texture;
import core.components.Transformation;

public class Gun extends Weapon {

	public Gun(String name) {
		super(name);
		// �̵� ���� ����
		_transform.setPhysics(100.0f, 0.0f, 0.0f);
		
		// �޽� ����
		try {
			_mesh = OBJParser.LoadData("bullet.obj");
			_mesh.setTexture(new Texture("gun.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// ���� ����
		_type = "gun";
		_power = 20;
		_price = 10;
		_range = 300.0f;
		_transform.setSpeed(60.0f);
	}

}
