package game.Weapon;

import core.components.OBJParser;
import core.components.Texture;
import core.components.Transformation;

public class Bomb extends Weapon {

	public Bomb(String name) {
		super(name);
		// �̵� ���� ����
		_transform.setPhysics(100.0f, 0.0f, 0.0f);
		
		// �޽� ����
		try {
			_mesh = OBJParser.LoadData("bullet.obj");
			_mesh.setTexture(new Texture("bomb.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// ���� ����
		_type = "bomb";
		_power = 30;
		_price = 20;
		_range = 200.0f;
		_transform.setSpeed(30.0f);
	}

}
