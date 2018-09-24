package com.huawei.lcloud.debugtools.entity;

import java.util.HashMap;
import java.util.Map;

public class KafkaMetric {
	private int topic_ori_count;
	private int topic_final_count;
	private int lag_count;
	private Map<String, Integer> filebeatsStatus = new HashMap<String, Integer>();

	public int getLag_count() {
		return lag_count;
	}

	public void setLag_count(int lag_count) {
		this.lag_count = lag_count;
	}

	public int getTopic_ori_count() {
		return topic_ori_count;
	}

	public void setTopic_ori_count(int topic_ori_count) {
		this.topic_ori_count = topic_ori_count;
	}

	public int getTopic_final_count() {
		return topic_final_count;
	}

	public void setTopic_final_count(int topic_final_count) {
		this.topic_final_count = topic_final_count;
	}

	public Map<String, Integer> getFilebeatsStatus() {
		return filebeatsStatus;
	}

	public void setFilebeatsStatus(Map<String, Integer> filebeatsStatus) {
		this.filebeatsStatus = filebeatsStatus;
	}
}