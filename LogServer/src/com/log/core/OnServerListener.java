package com.log.core;

import java.io.IOException;

public interface OnServerListener {

	void onServerStarted(String ip, int port) throws IOException;
	
	void onServerError(String msg);
	
	void onServerStoped();
}
