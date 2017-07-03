package core;

import static org.lwjgl.glfw.GLFW.*;

public class mouseEvent {
	private boolean _leftButtonPressed = false;
	
	public mouseEvent() {
		
	}
	
	public void init(WindowMngr window) {
		glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
			_leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
		});
	}
	
	public boolean isLeftButtonPressed() {
		return _leftButtonPressed;
	}
}
