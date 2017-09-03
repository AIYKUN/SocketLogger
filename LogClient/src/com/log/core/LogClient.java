package com.log.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;

public class LogClient {

	private final static String DEFAULT_HOST = "127.0.0.1";
	private final static int DEFAULT_PORT = 9090;

	InetSocketAddress inetSocketAddress;
	private Socket socket;

	BufferedWriter bufferedWriter = null;

	private int errorConnectCount = 0;

	public LogClient() {
		this(DEFAULT_PORT);
	}

	public LogClient(int port) {
		this(DEFAULT_HOST, port);
	}

	public LogClient(String ip, int port) {
		bindIpAndPort(ip, port);
	}

	public void bindIpAndPort(String ip, int port) {
		if (ip == null || ip.trim().length() == 0) {
			ip = DEFAULT_HOST;
		}
		if (port <= 0) {
			port = DEFAULT_PORT;
		}
		inetSocketAddress = new InetSocketAddress(ip, port);
	}

	public LogClient(File configFile) {
		if (configFile != null && configFile.exists()) {
			Properties properties = new Properties();
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(configFile);
				properties.load(fileInputStream);
				String host = properties.getProperty("host");
				String port = properties.getProperty("port");
				if (host == null || host.length() == 0) {
					host = DEFAULT_HOST;
				}
				int iPort = DEFAULT_PORT;
				if (port != null && port.length() >= 0) {
					try {
						iPort = Integer.parseInt(port);
					} catch (Throwable throwable) {
					}
				}
				bindIpAndPort(host, iPort);
			} catch (FileNotFoundException e) {
				bindIpAndPort(DEFAULT_HOST, 0);
			} catch (Throwable e) {
				bindIpAndPort(DEFAULT_HOST, 0);
			} finally {
				if (fileInputStream != null) {
					try {
						fileInputStream.close();
					} catch (IOException e) {
					}
				}
			}
		} else {
			bindIpAndPort(DEFAULT_HOST, 0);
		}
	}

	public void connectServer() {
		try {
			socket = new Socket();
			socket.setSoTimeout(10000);
			socket.connect(inetSocketAddress);
			OutputStream outputStream = socket.getOutputStream();
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					outputStream, "utf-8"));
		} catch (IOException e) {
			bufferedWriter = null;

			errorConnectCount++;
			if (errorConnectCount % 20 == 0) {
				errorConnectCount = 0;
				System.out.println("连接服务器失败");
			}
		}
	}

	private void doSendLog(String log) throws IOException {
		if (bufferedWriter != null) {
			bufferedWriter.write(log);
			bufferedWriter.write("\n");
			bufferedWriter.flush();
		}
	}

	public void sendLog(String log) {
		if (bufferedWriter == null) {
			connectServer();
		}

		try {
			doSendLog(log);
		} catch (IOException e) {
			bufferedWriter = null;
			connectServer();
			try {
				doSendLog(log);
			} catch (IOException ea) {
				bufferedWriter = null;
				System.out.println(log);
			}
		}
	}

	public void release() {
		if (bufferedWriter != null) {
			try {
				bufferedWriter.flush();
			} catch (IOException e) {
			}
			bufferedWriter = null;
		}
		
		if (socket != null) {
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
			}
		}
	}
}
