package com.huawei.lcloud.debugtools.fullpath;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.huawei.lcloud.debugtools.entity.KafkaMetric;
import com.huawei.lcloud.debugtools.websocket.IWebSocketServer;

public class GetParameter {
	private static Logger logger = LoggerFactory.getLogger(GetParameter.class);

	FullPathMonitor monitor;
	public GetParameter(FullPathMonitor monitor) {
		this.monitor = monitor;
	}

	/**
	 * 向终端（websocket）发送监控信息
	 * @param websocket
	 */
	public void run(IWebSocketServer websocket) {
		if (websocket != null) {
			KafkaMetric metric = new KafkaMetric();
			metric.setTopic_final_count(this.monitor.getMessageCount_final().get());
			metric.setTopic_ori_count(this.monitor.getMessageCount_final().get());
			Gson gson = new Gson();
			String message = gson.toJson(metric);
			try {
				websocket.sendMessage(message);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		logger.info("message count final is: " + this.monitor.getMessageCount_final().get());
		logger.info("message count ori is: " + this.monitor.getMessageCount_final().get());
	}
}