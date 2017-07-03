package core;

import java.util.HashMap;

abstract public class IGameLogic {
	protected HashMap<String, GameObject> _gobjects = new HashMap<String, GameObject>();
	
	// gameobject추가
	public void attach(GameObject go) {
		for (int i=0; i<100; i++) {
			if (!_gobjects.containsKey(go.getName())) { // 해당 키값이 존재하지 않을 경우
				_gobjects.put(go.getName(), go);
				break;
			}
			
			String tempName = go.getName();
			go.setName(tempName+i);
		}
	}
	
	// gameobject제거
	public void detach(String goNameKey) {
		_gobjects.remove(goNameKey);
	}
	
	public GameObject getGO(String key) {
		return _gobjects.get(key);
	}
	
    abstract public void init(WindowMngr window) throws Exception;
    abstract public void input(WindowMngr window, mouseEvent mouse,float interval);
    abstract public void update(float interval);
    abstract public void render(WindowMngr window);
}
