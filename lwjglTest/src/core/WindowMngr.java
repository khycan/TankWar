package core;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class WindowMngr {
	private final String title;

    private int width;

    private int height;

    private long windowHandle;

    private boolean resized;

    private boolean vSync;

    public WindowMngr(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized = false;
    }

    public void init() {
        // ���� �߻��� ǥ���� callback ���� �⺻�� System.err
        GLFWErrorCallback.createPrint(System.err).set();

        // GLFW �ʱ�ȭ. ��κ��� GLFW�Լ��� �� �Լ��� ���ľ� ����� ���� ����
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // ������ ����
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // resize callback ����
        glfwSetWindowSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });

        // Ű callback ����. Ű �̺�Ʈ �߻��� ���� ���� ��.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // Main loop���� ó����
            }
        });

        // ����� ���� get.
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // ���� â�� ����� ��ġ�ϰ� ����
        glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        if (isvSync()) {
            // v-sync�� �Ҵ� (�׷���ī�� �ӵ� >>>>>>> ����� refresh �ӵ� �̱⶧���� vsync�� �Ѹ� ����Ϳ� �����.)
            glfwSwapInterval(1);
        }

        // ������ show
        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        // ȭ�� �����
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }
    
    public long getWindowHandle() { return windowHandle; }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }
    
    public boolean isKeyReleased(int keyCode) {
    	return glfwGetKey(windowHandle, keyCode) == GLFW_RELEASE;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }
    

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    public void update() {
    	// �̰� ȣ��Ǿ� ����ۿ� �׷���ī�尡 �׸� �����Ͱ� ����Ϳ� ��µ�
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }
}
