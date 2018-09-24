package com.huawei.lcloud.debugtools.websocket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@ServerEndpoint("/websocket/{user}")
@ServerEndpoint(value = "/websocket")
@Component
public class WebSocketServer implements IWebSocketServer {
	private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	// concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	private static ScheduledExecutorService service;

	/* (non-Javadoc)
	 * @see com.huawei.lcloud.debugtools.websocket.IWebSocketServer#onOpen(javax.websocket.Session)
	 */
	@Override
	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		webSocketSet.add(this); // 加入set中
		addOnlineCount(); // 在线数加1
		log.info("有新连接加入！当前在线人数为" + getOnlineCount());
		try {
			sendMessage("连接成功");
		} catch (IOException e) {
			log.error("websocket IO异常");
		}

		service = Executors.newScheduledThreadPool(1);
		service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					sendInfo("Hello!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}, 3, 3, TimeUnit.SECONDS);

	}
	// //连接打开时执行
	// @OnOpen
	// public void onOpen(@PathParam("user") String user, Session session) {
	// currentUser = user;
	// System.out.println("Connected ... " + session.getId());
	// }

	/* (non-Javadoc)
	 * @see com.huawei.lcloud.debugtools.websocket.IWebSocketServer#onClose()
	 */
	@Override
	@OnClose
	public void onClose() {
		webSocketSet.remove(this); // 从set中删除
		subOnlineCount(); // 在线数减1
		log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
	}

	/* (non-Javadoc)
	 * @see com.huawei.lcloud.debugtools.websocket.IWebSocketServer#onMessage(java.lang.String, javax.websocket.Session)
	 */
	@Override
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("来自客户端的消息:" + message);

		// 群发消息
		for (IWebSocketServer item : webSocketSet) {
			try {
				item.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.huawei.lcloud.debugtools.websocket.IWebSocketServer#onError(javax.websocket.Session, java.lang.Throwable)
	 */
	@Override
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("发生错误");
		error.printStackTrace();
	}

	/* (non-Javadoc)
	 * @see com.huawei.lcloud.debugtools.websocket.IWebSocketServer#sendMessage(java.lang.String)
	 */
	@Override
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	/**
	 * 群发自定义消息
	 */
	public static void sendInfo(String message) throws IOException {
		log.info(message);
		for (IWebSocketServer item : webSocketSet) {
			try {
				item.sendMessage(message);
			} catch (IOException e) {
				continue;
			}
		}
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketServer.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocketServer.onlineCount--;
	}
}
