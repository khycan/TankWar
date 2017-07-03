package game;

import java.util.ArrayList;
import java.util.List;

public class ThreadMessenger {
	private String[] _mainThreadMailBox;
	private String[] _subThreadMailBox;
	
	public ThreadMessenger() {
		_mainThreadMailBox = new String[3];
		for(int i=0; i<_mainThreadMailBox.length; i++) {
			_mainThreadMailBox[i] = new String("");
		}
		
		_subThreadMailBox = new String[2];
		for(int i=0; i<_subThreadMailBox.length; i++) {
			_subThreadMailBox[i] = new String("");
		}
	}
	
	public void sendtoMain(String message, int index) {
		_mainThreadMailBox[index] = message;
	}
	
	public void sendtoSub(String message, int index) {
		_subThreadMailBox[index] = message;
	}
	
	// ���� ������� ���Ϲڽ��� �޽��� ��
	public String popInMainMailBox(int index) {
		if(_mainThreadMailBox[index].length() <= 0) {
			return null;
		}
		
		String ret = _mainThreadMailBox[index];
		
		_mainThreadMailBox[index] = ""; // mailbox �ʱ�ȭ
		
		return ret;
	}
	
	// ���� ������� ���Ϲڽ��� �޽��� ��
	public String popInSubMailBox(int index) {
		if(_subThreadMailBox[index].length() <= 0) {
			return null;
		}
		
		String ret = _subThreadMailBox[index];
		
		_subThreadMailBox[index] = ""; // mailbox �ʱ�ȭ
		
		return ret;
	}
}
