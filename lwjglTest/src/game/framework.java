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
	
	private int _subIndex = 0; // ���� ���Ϲڽ��� �ε���
	
	public framework() {
		_logs = new TextArea[3];
		_frame = null;
	}
	
	public void init(ThreadMessenger tm) {
		// get ������ �޽���
		_tm = tm;
		
        // ������ ����
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
		
		// ���� ���Ž� �˾� �ʱ�ȭ
		_purchaseDlg = new Dialog(_frame, "���� ����", false);
		_purchaseDlg.setSize(750, 100);
		_purchaseDlg.setLayout(new FlowLayout());
		_purchaseDlg.setVisible(false);
		
		// �˾�â �ؽ�Ʈ�ʵ� �ʱ�ȭ
		_laserNum = new TextField(10);
		_bombNum  = new TextField(10);
		_gunNum   = new TextField(10);
		
		// ��ư �ʱ�ȭ
		_allyBuyWeapon = new Button("����");
		_allyBuyWeapon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// ���� ���� ��
				_purchaseDlg.setVisible(true);
				_subIndex = 0;
			}
		});
		_enemyBuyWeapon = new Button("����");
		_enemyBuyWeapon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// ���� ���� ��
				_purchaseDlg.setVisible(true);
				_subIndex = 1;
			}
		});
		_btnOk = new Button("Ȯ��");
		_btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) { // ���� Ȯ�� ���� ��
				// �ƹ��͵� �� ������ ��� 0����
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
		
		_purchaseDlg.add(new Label("Laser(30��, 40ȭ��): "));
		_purchaseDlg.add(_laserNum);
		_purchaseDlg.add(new Label("Bomb(20��, 30ȭ��): "));
		_purchaseDlg.add(_bombNum);
		_purchaseDlg.add(new Label("Gun(10��, 20ȭ��): "));
		_purchaseDlg.add(_gunNum);
		_purchaseDlg.add(_btnOk);
		
		gbcAdd(new Label("�Ʊ�����"), 0, 0, 2, 1);
		gbcAdd(_logs[0], 0, 1, 2, 2);
		gbcAdd(new Label("��������"), 0, 3, 2, 1);
		gbcAdd(_logs[1], 0, 4, 2, 2);
		
		gbcAdd(new Label(""), 2, 0, 1, 1);
		gbcAdd(new Label("�Ʊ� ���ⱸ��"), 2, 1, 1, 1);
		gbcAdd(_allyBuyWeapon, 3, 1, 1, 1);
		gbcAdd(new Label("���� ���ⱸ��"), 2, 2, 1, 1);
		gbcAdd(_enemyBuyWeapon, 3, 2, 1, 1);
		gbcAdd(new Label("�α� ���"), 2, 3, 1, 1);
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
					if (i == 2) { // �α� �ϰ��
						_logs[i].append(msg + "\n");
					} else {
						String[] tokens = msg.split(",");
						
						String ret = new String("");
						ret += "���� �Ӵ� : " + tokens[1] + "\n";
						ret += "�� ȭ�� : " + tokens[2] + "\n";
						ret += "���� ��Ȳ : Laser(" + tokens[3] + "), Bomb(" + tokens[4] + "), Gun(" + tokens[5] +")\n";
						ret += "���� ��ġ : " + tokens[6] + "\n";
						
						_logs[i].setText(ret);
						
						if (Integer.parseInt(tokens[1]) + Integer.parseInt(tokens[2]) == 0 ) { // �¸� ���
							if(i == 0) {
								_logs[2].append("�Ʊ� �й�....!");
							} else {
								_logs[2].append("�Ʊ� �¸�!!!!!!");
							}
							
							// ���ͷ�Ʈ �߻�
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
