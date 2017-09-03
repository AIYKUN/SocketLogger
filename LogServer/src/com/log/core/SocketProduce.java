package com.log.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import com.log.ui.LogFrame;

public class SocketProduce {

	private OnServerListener listener;
	private static ServerSocket serverSocket = null;

	private boolean serverStoped = true;

	public SocketProduce() {
	}

	private void printlnLog(String log) {
		if (listener != null) {
			listener.onServerError(log);
		}
	}

	public boolean init(String ip, int port) throws IOException {
		if (port <= 0) {
			printlnLog("端口号必须大于0");
			return false;
		}
		if (port > 65535) {
			printlnLog("端口号必须小于65535");
			return false;
		}
		if (serverSocket == null) {
			try {
				serverSocket = new ServerSocket();
			} catch (IOException e) {
				printlnLog(e.getMessage());
			}
		}
		if (serverSocket != null) {
			serverSocket.setSoTimeout(ServerConfig.SOCKET_TIMEOUT);
			serverSocket.setReceiveBufferSize(ServerConfig.BUFFER_SIZE);
			serverSocket.bind(new InetSocketAddress(ip, port));
			if (listener != null) {
				listener.onServerStarted(ip, port);
			}
			return true;
		}

		return false;
	}

	public void release() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
			}
			serverSocket = null;
		}
	}

	public void startService(final String ip, final int port) {
		new Thread() {
			public void run() {
				try {
					if(init(ip, port) && serverSocket.isBound()) {
						serverStoped = false;
						while (!serverStoped) {
							try {
								new LogFrame(serverSocket.accept());
								printlnLog("有客户端连接");
							} catch (SocketTimeoutException e) {
							}
						}
					}
				} catch (IOException e) {
					printlnLog(e.getMessage());
				} finally {
					if (listener != null) {
						listener.onServerStoped();
					}

					release();
				}
			}
		}.start();

	}

	public void stopService() {
		serverStoped = true;
	}

	public void setOnServerListener(OnServerListener listener) {
		this.listener = listener;
	}

}
