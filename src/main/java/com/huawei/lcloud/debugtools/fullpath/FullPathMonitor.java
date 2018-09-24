package com.huawei.lcloud.debugtools.fullpath;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.StringUtils;

import com.huawei.lcloud.debugtools.exception.DebugToolsException;
import com.huawei.lcloud.debugtools.websocket.IWebSocketServer;

public class FullPathMonitor {
	private static ExecutorService threadpool = Executors.newCachedThreadPool();
	private static ScheduledExecutorService fixTime = Executors.newSingleThreadScheduledExecutor();

	private IWebSocketServer websocketServer;
	private String logTypeName;
	private String kafkaServers;

	private volatile AtomicInteger messageCount_ori = new AtomicInteger();
	private volatile AtomicInteger messageCount_final = new AtomicInteger();
	private volatile Map<String, Integer> filebeatsStatus = new HashMap<String, Integer>();
	
	private GetParameter getter = new GetParameter(this);

	public FullPathMonitor() {

	}

	public FullPathMonitor(IWebSocketServer wsServer, String logTypeName, String kafkaServers) {
		this.websocketServer = wsServer;
		this.logTypeName = logTypeName;
		this.kafkaServers = kafkaServers;
	}

	public void startConsumer() throws DebugToolsException {
		if (StringUtils.isEmpty(this.logTypeName) || StringUtils.isEmpty(this.kafkaServers)) {
			String errormsg = String.format("some parameter is empty! logtypeName: %s, kafkaServers: %s",
					String.valueOf(this.logTypeName), String.valueOf(this.kafkaServers));
			throw new DebugToolsException(errormsg);
		}

		KafkaTopicsubscriber consumer = new KafkaTopicsubscriber(this.logTypeName, this.kafkaServers, this);
		threadpool.execute(new Runnable() {
			@Override
			public void run() {
				consumer.startConsumer();
			}
		});

		fixTime.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				getter.run(websocketServer);
			}
		}, 5, 5, TimeUnit.SECONDS);
	}

	public static void main(String[] args) {
		KafkaTopicsubscriber consumer = new KafkaTopicsubscriber("foo", "localhost:9092", null);
		threadpool.execute(new Runnable() {
			@Override
			public void run() {
				consumer.startConsumer();
			}
		});

		fixTime.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				// GetParameter getter = new GetParameter(this.);
				// getter.run(null);
			}
		}, 5, 5, TimeUnit.SECONDS);
	}

	public AtomicInteger getMessageCount_ori() {
		return messageCount_ori;
	}

	public void setMessageCount_ori(AtomicInteger messageCount_ori) {
		this.messageCount_ori = messageCount_ori;
	}

	public AtomicInteger getMessageCount_final() {
		return messageCount_final;
	}

	public void setMessageCount_final(AtomicInteger messageCount_final) {
		this.messageCount_final = messageCount_final;
	}

	public Map<String, Integer> getFilebeatsStatus() {
		return filebeatsStatus;
	}

	public void setFilebeatsStatus(Map<String, Integer> filebeatsStatus) {
		this.filebeatsStatus = filebeatsStatus;
	}
}
