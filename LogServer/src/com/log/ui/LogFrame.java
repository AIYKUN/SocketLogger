package com.log.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogFrame extends JFrame implements Runnable {


	JButton cleanLog;
	
	Socket clientSocket;
	private JTextArea logTextArea;
	JScrollPane logScrollPane;
	JScrollBar mJScrollBar;

	public LogFrame(Socket socket) {
		this.clientSocket = socket;

		cleanLog = new JButton("�����־");
		cleanLog.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				logTextArea.setText("");
			}
		});
		this.add(cleanLog, BorderLayout.NORTH);
		
		logTextArea = new JTextArea();
		// �й�����������������װ�����ı���
		logScrollPane = new JScrollPane(logTextArea);
		logScrollPane.setBounds(230, 0, 250, 80);

		mJScrollBar = logScrollPane.getVerticalScrollBar(); //�õ��˸�JScrollBar
		this.add(logScrollPane, BorderLayout.CENTER);

		// ���ñ���
		this.setTitle("�ͻ���Log");
		// ���ô��ڵĹرղ���
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		// ���ô��ڴ�С
		this.setSize(750, 500);
		// ���ô��ھ��У����ڴ��ڴ�С���棬null��ʾ����
		this.setLocationRelativeTo(null);
		// ����������Ϊ��ʾ,Ҫд�����һ��
		this.setVisible(true);
		new Thread(this).start();
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				// ���붯��
				//
				try {
					clientSocket.close();
				} catch (IOException e1) {
				}
			}

		});
	}
	
	private void printLog(String log) {
		logTextArea.append("-> " + log + "\r\n");
		mJScrollBar.setValue(logTextArea.getHeight());
	}

	/**
	 */
	private static final long serialVersionUID = 4331499123157236577L;

	public void run() {
		try {
			InputStream is = clientSocket.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			InputStreamReader isr = new InputStreamReader(bis, "utf-8");
			BufferedReader reader = new BufferedReader(isr);
			do {
				String c = reader.readLine();
				if (c == null) {
					break;
				}
				printLog(c);
			} while (true);
		} catch (IOException e) {
			printLog(e.getMessage());
		} finally {
			try {
				printLog("\n�Ͽ��ͻ�������");
				clientSocket.close();
			} catch (IOException e) {
			}
		}
	}

}
