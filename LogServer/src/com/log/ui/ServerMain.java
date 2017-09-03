package com.log.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.log.core.OnServerListener;
import com.log.core.ServerConfig;
import com.log.core.SocketProduce;

public class ServerMain extends JFrame {


	SocketProduce produce = new SocketProduce();
	
	private static final long serialVersionUID = -7690831390332419059L;
	
	JPanel flowLayout;
	private JLabel config;
	private JLabel ipConfig;
	private JLabel portConfig;
	private JLabel serverStatus;
	private JLabel serverConfig;
	JTextField ipTextField;
	JTextField portTextField;
	JButton startServer;
	JButton stopServer;
	JButton cleanLog;
	JTextArea runLog;
	
	public ServerMain() {
		
		this.setLayout(new BorderLayout());
		flowLayout = new JPanel();
		flowLayout.setLayout(new MyVFlowLayout());
		
		config = new JLabel("����������                                   ");
		ipConfig = new JLabel("ip");
		ipTextField = new JTextField(ServerConfig.SERVER_HOST);
		portConfig = new JLabel("�˿�");
		portTextField = new JTextField(""+ServerConfig.SERVER_PORT);
		portTextField.addKeyListener(new KeyAdapter(){  
            public void keyTyped(KeyEvent e) {  
                int keyChar = e.getKeyChar();                 
                if(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9){  
                }else{  
                    e.consume(); //�ؼ������ε��Ƿ�����  
                }  
            }  
        });  
		flowLayout.setBounds(0, 0, 530, 1000);
		serverStatus = new JLabel("");
		serverConfig = new JLabel(ServerConfig.SERVER_HOST + ":"+ServerConfig.SERVER_PORT);
		
		startServer = new JButton("��������");
		stopServer = new JButton("ֹͣ����");
		cleanLog = new JButton("�����־");
		runLog = new JTextArea();

		startServer.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				String ip = ipTextField.getText();
				if(ip == null || ip.trim().length() == 0) {
					ip = ServerConfig.SERVER_HOST;
				}
				
				int port = ServerConfig.SERVER_PORT;
				String sPort = portTextField.getText();
				if (sPort != null && sPort.trim().length() >= 0) {
					try {
						port = Integer.parseInt(sPort);
					} catch (Exception e) {
						port = ServerConfig.SERVER_PORT;
					}
				}
				produce.setOnServerListener(new OnServerListener() {
					
					public void onServerStarted(String ip, int port) throws IOException {
						serverStatus.setText("server started!");
						serverConfig.setText(ip + ":" + port);
					}

					public void onServerStoped() {
						serverStatus.setText("server stoped!");
					}

					public void onServerError(String msg) {
						runLog.append(""+msg);
						runLog.append("\r\n");
					}
				});
				produce.startService(ip, port);
			}
		});
		stopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				serverStatus.setText("server stopping....");
				produce.stopService();
			}
		});
		cleanLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runLog.setText("");
			}
		});
		
		flowLayout.add(config);
		flowLayout.add(ipConfig);
		flowLayout.add(ipTextField);
		flowLayout.add(portConfig);
		flowLayout.add(portTextField);
		
		flowLayout.add(startServer);
		flowLayout.add(stopServer);
		flowLayout.add(cleanLog);

		flowLayout.add(serverConfig);
		flowLayout.add(serverStatus);
		flowLayout.add(runLog);
		
		this.add(flowLayout, BorderLayout.WEST);
		

		// �й�����������������װ�����ı���
		JScrollPane logScrollPane = new JScrollPane(runLog);
		logScrollPane.setBounds(0, 0, 250, 80);

		this.add(logScrollPane, BorderLayout.CENTER);
		

		// ���ñ���
		this.setTitle("Log������");
		// ���ô��ڵĹرղ���
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// ���ô��ڴ�С
		this.setSize(350, 500);
		this.setResizable(false);
		// ���ô��ھ��У����ڴ��ڴ�С���棬null��ʾ����
		this.setLocationRelativeTo(null);
		// ����������Ϊ��ʾ,Ҫд�����һ��
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				produce.stopService();
				try {
					Thread.sleep(ServerConfig.SOCKET_TIMEOUT+1);
				} catch (InterruptedException e1) {
				}
				produce.release();
			}

		});
		
	}
	
	

	public static void main(String[] args) {

		new ServerMain();
	}

}
