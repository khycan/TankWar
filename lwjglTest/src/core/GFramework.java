package core;


public class GFramework implements Runnable {
    private final WindowMngr window;

    public final Thread gameLoopThread;

    private final Timer timer;

    private final IGameLogic gameLogic;
    
    private final mouseEvent mouseevent;

    public GFramework(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) throws Exception {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new WindowMngr(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
        timer = new Timer();
        mouseevent = new mouseEvent();
    }

    public void start() {
        String osName = System.getProperty("os.name");
        // Mac������ GLFW�Լ����� main���� �����忡�� ������ ���ϰ� �ϴ� ��찡 �־� �˻��ϴ� �κ�
        if ( osName.contains("Mac") ) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        }
    }
    
    public void interrupt() {
    	gameLoopThread.interrupt();
    }

    protected void init() throws Exception {
        window.init();
        timer.init();
        gameLogic.init(window);
        mouseevent.init(window);
    }

    protected void gameLoop() {
        float deltaTime;

        boolean running = true;
        while (running && !window.windowShouldClose() && !Thread.currentThread().isInterrupted()) {
            deltaTime = timer.getElapsedTime();

            input(deltaTime);

            update(deltaTime);

            render();
        }
    }

    protected void input(float deltaTime) {
        gameLogic.input(window, mouseevent, deltaTime);
    }

    protected void update(float deltaTime) {
        gameLogic.update(deltaTime);
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }
}
