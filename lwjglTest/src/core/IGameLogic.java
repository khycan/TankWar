package core;

import java.util.HashMap;

abstract public class IGameLogic {
	protected HashMap<String, GameObject> _gobjects = new HashMap<String, GameObject>();
	
	// gameobject�߰�
	public void attach(GameObject go) {
		for (int i=0; i<100; i++) {
			if (!_gobjects.containsKey(go.getName())) { // �ش� Ű���� �������� ���� ���
				_gobjects.put(go.getName(), go);
				break;
			}
			
			String tempName = go.getName();
			go.setName(tempName+i);
		}
	}
	
	// gameobject����
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
