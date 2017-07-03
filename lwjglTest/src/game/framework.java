package game;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import core.GFramework;

public class framework {
	private Frame _frame;
	
	private TextArea[] _logs; // 0: allyInfo, 1: enemyInfo, 2: log
	private Button _allyBuyWeapon;
	private Button _enemyBuyWeapon;
	private Button _btnOk;
	
	private GridBagLayout _gridBagLayout;
	private GridBagConstraints _gbc;
	
	private Dialog _purchaseDlg;
	private TextField _laserNum;
	private TextField _bombNum;
	private TextField _gunNum;
	private ThreadMessenger _tm;
	
	private int _subIndex = 0; // 보낼 메일박스의 인덱스
	
	public framework() {
		_logs = new TextArea[3];
		_frame = null;
	}
	
	public void init(ThreadMessenger tm) {
		// get 스레드 메신저
		_tm = tm;
		
        // 프레임 생성
		_frame = new Frame( "TankSim Project" );
		_frame.setSize( 800, 600 );
		
		initLayout();
		initComponent();
		
		_frame.addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent windowevent ) {
                _frame.dispose();
                System.exit( 0 );
            }
        });
		
		_frame.setVisible(true);
	}
	
	public void initLayout() {
		_gridBagLayout = new GridBagLayout();
		_gbc = new GridBagConstraints();
		_gbc.weightx = 1.0;
		_gbc.weighty = 1.0;
		_gbc.fill = GridBagConstraints.BOTH;
		
		_frame.setLayout(_gridBagLayout);
	}
	
	public void initComponent() {
		for(int i=0; i<_logs.length; i++) {
			_logs[i] = new TextArea(10, 20);
		}
		
		// 무기 구매시 팝업 초기화
		_purchaseDlg = new Dialog(_frame, "무기 구매", false);
		_purchaseDlg.setSize(750, 100);
		_purchaseDlg.setLayout(new FlowLayout());
		_purchaseDlg.setVisible(false);
		
		// 팝업창 텍스트필드 초기화
		_laserNum = new TextField(10);
		_bombNum  = new TextField(10);
		_gunNum   = new TextField(10);
		
		// 버튼 초기화
		_allyBuyWeapon = new Button("구매");
		_allyBuyWeapon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// 구매 누를 시
				_purchaseDlg.setVisible(true);
				_subIndex = 0;
			}
		});
		_enemyBuyWeapon = new Button("구매");
		_enemyBuyWeapon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// 구매 누를 시
				_purchaseDlg.setVisible(true);
				_subIndex = 1;
			}
		});
		_btnOk = new Button("확인");
		_btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) { // 구매 확인 누를 시
				// 아무것도 안 적었을 경우 0으로
				if(_laserNum.getText().equals("")) { _laserNum.setText("0"); }
				if(_bombNum.getText().equals("")) { _bombNum.setText("0"); }
				if(_gunNum.getText().equals("")) { _gunNum.setText("0"); }
				
				String ret = new String("");
				ret = _laserNum.getText() + "," + _bombNum.getText() + "," + _gunNum.getText();
				
				_tm.sendtoSub(ret, _subIndex);
				
				_purchaseDlg.setVisible(false);
				_purchaseDlg.dispose();
				_laserNum.setText("");
				_bombNum.setText("");
				_gunNum.setText("");
			}
		});
		
		_purchaseDlg.add(new Label("Laser(30억, 40화력): "));
		_purchaseDlg.add(_laserNum);
		_purchaseDlg.add(new Label("Bomb(20억, 30화력): "));
		_purchaseDlg.add(_bombNum);
		_purchaseDlg.add(new Label("Gun(10억, 20화력): "));
		_purchaseDlg.add(_gunNum);
		_purchaseDlg.add(_btnOk);
		
		gbcAdd(new Label("아군정보"), 0, 0, 2, 1);
		gbcAdd(_logs[0], 0, 1, 2, 2);
		gbcAdd(new Label("적군정보"), 0, 3, 2, 1);
		gbcAdd(_logs[1], 0, 4, 2, 2);
		
		gbcAdd(new Label(""), 2, 0, 1, 1);
		gbcAdd(new Label("아군 무기구매"), 2, 1, 1, 1);
		gbcAdd(_allyBuyWeapon, 3, 1, 1, 1);
		gbcAdd(new Label("적군 무기구매"), 2, 2, 1, 1);
		gbcAdd(_enemyBuyWeapon, 3, 2, 1, 1);
		gbcAdd(new Label("로그 기록"), 2, 3, 1, 1);
		gbcAdd(_logs[2], 2, 4, 2, 2);
	}
	
	private void gbcAdd(Component obj, int x, int y, int width, int height) {
		_gbc.gridx = x;
		_gbc.gridy = y;
		_gbc.gridwidth = width;
		_gbc.gridheight = height;
		
		_frame.add(obj, _gbc);
	}
	
	public void run(GFramework gm) {
		while(true) {
			for(int i=0; i<3; i++) {
				String msg = _tm.popInMainMailBox(i);
				
				if (msg != null) {
					if (i == 2) { // 로그 일경우
						_logs[i].append(msg + "\n");
					} else {
						String[] tokens = msg.split(",");
						
						String ret = new String("");
						ret += "보유 머니 : " + tokens[1] + "\n";
						ret += "총 화력 : " + tokens[2] + "\n";
						ret += "무기 현황 : Laser(" + tokens[3] + "), Bomb(" + tokens[4] + "), Gun(" + tokens[5] +")\n";
						ret += "현재 위치 : " + tokens[6] + "\n";
						
						_logs[i].setText(ret);
						
						if (Integer.parseInt(tokens[1]) + Integer.parseInt(tokens[2]) == 0 ) { // 승리 요건
							if(i == 0) {
								_logs[2].append("아군 패배....!");
							} else {
								_logs[2].append("아군 승리!!!!!!");
							}
							
							// 인터럽트 발생
							gm.gameLoopThread.interrupt();
						}
					}
				}
			}
			
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
