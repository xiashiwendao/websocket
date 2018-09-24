package com.huawei.lcloud.debugtools.fullpath;

import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaTopicsubscriber {
	private String topic_ori_name = "";
	private String topic_final_name = "";
	private final static String SURRFIX_ORI = "_ori";
	private final static String SURRFIX_FINAL = "_final";

	private String kafkaServers = "";
	private FullPathMonitor parent;

	/**
	 * 
	 * @param logType日志类型
	 * @param kafkaServers
	 */
	public KafkaTopicsubscriber(String logType, String kafkaServers, FullPathMonitor parent) {
		this.topic_ori_name = logType + SURRFIX_ORI;
		this.topic_final_name = logType + SURRFIX_FINAL;
		this.kafkaServers = kafkaServers;
		this.parent = parent;
	}

	KafkaConsumer<String, String> consumer;

	public void startConsumer() {
		Properties props = new Properties();
		props.put("bootstrap.servers", this.kafkaServers);
		props.put("group.id", UUID.randomUUID().toString());
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		this.consumer = new KafkaConsumer<String, String>(props);
		this.consumer.top

		consumer.subscribe(Arrays.asList(this.topic_final_name, this.topic_ori_name));
		System.out.println("start listening... ");
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			try {

				for (ConsumerRecord<String, String> record : records) {
					if (record.topic().equals(this.topic_final_name))
						this.parent.getMessageCount_final().incrementAndGet();
					else
						this.parent.getMessageCount_ori().incrementAndGet();
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
}
