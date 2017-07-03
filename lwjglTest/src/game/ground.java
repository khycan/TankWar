package game;

public class ground extends Cube {

	public ground(String name, float width, float depth, float height) {
		super(name, width, depth, height);
		
		_transform.setPosition(0.0f, -1.0f, 0.0f);
	}

}
