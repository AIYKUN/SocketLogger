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
		
		config = new JLabel("服务器配置                                   ");
		ipConfig = new JLabel("ip");
		ipTextField = new JTextField(ServerConfig.SERVER_HOST);
		portConfig = new JLabel("端口");
		portTextField = new JTextField(""+ServerConfig.SERVER_PORT);
		portTextField.addKeyListener(new KeyAdapter(){  
            public void keyTyped(KeyEvent e) {  
                int keyChar = e.getKeyChar();                 
                if(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9){  
                }else{  
                    e.consume(); //关键，屏蔽掉非法输入  
                }  
            }  
        });  
		flowLayout.setBounds(0, 0, 530, 1000);
		serverStatus = new JLabel("");
		serverConfig = new JLabel(ServerConfig.SERVER_HOST + ":"+ServerConfig.SERVER_PORT);
		
		startServer = new JButton("开启服务");
		stopServer = new JButton("停止服务");
		cleanLog = new JButton("清空日志");
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
		

		// 有滚动条的容器，用来装多行文本框
		JScrollPane logScrollPane = new JScrollPane(runLog);
		logScrollPane.setBounds(0, 0, 250, 80);

		this.add(logScrollPane, BorderLayout.CENTER);
		

		// 设置标题
		this.setTitle("Log服务器");
		// 设置窗口的关闭策略
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 设置窗口大小
		this.setSize(350, 500);
		this.setResizable(false);
		// 设置窗口居中，放在窗口大小后面，null表示桌面
		this.setLocationRelativeTo(null);
		// 将窗口设置为显示,要写在最后一句
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
