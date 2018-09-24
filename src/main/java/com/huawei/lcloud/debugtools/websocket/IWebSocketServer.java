package com.huawei.lcloud.debugtools.websocket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

public interface IWebSocketServer {

	/**
	 * 连接建立成功调用的方法
	 */
	void onOpen(Session session);
	// //连接打开时执行
	// @OnOpen
	// public void onOpen(@PathParam("user") String user, Session session) {
	// currentUser = user;
	// System.out.println("Connected ... " + session.getId());
	// }

	/**
	 * 连接关闭调用的方法
	 */
	void onClose();

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message
	 *            客户端发送过来的消息
	 */
	void onMessage(String message, Session session);

	/**
	 * 
	 * @param session
	 * @param error
	 */
	void onError(Session session, Throwable error);

	void sendMessage(String message) throws IOException;

}