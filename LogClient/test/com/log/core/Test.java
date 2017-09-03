package com.log.core;

import java.io.File;
import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {
		
//		LogClient logClient = new LogClient();

//		LogClient logClient = new LogClient(new File("d:\\config.property"));
		LogClient logClient = new LogClient("127.0.0.1", 9090);

		sendLogs(logClient, 100);
	}
	
	private static void sendLogs(LogClient logClient, int count) {
		try {
			do {
				logClient.sendLog("所发abcd生的"+count);
				count--;
				Thread.sleep(300);
			} while (count >= 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			logClient.release();
		}

	}
}
