package game;

import core.*;

public class GMain {

	public static void main(String[] args) {
		ThreadMessenger tm = new ThreadMessenger();
		
		framework fm = new framework();
		fm.init(tm);
		
		try {
            boolean vSync = true;
            IGameLogic gameLogic = new testGame(tm);
            GFramework gameEng = new GFramework("GAME", 1080, 760, vSync, gameLogic);
            gameEng.start();
            
            fm.run(gameEng);
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
	}

}
