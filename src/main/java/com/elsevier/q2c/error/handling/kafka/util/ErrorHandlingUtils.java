package com.elsevier.q2c.error.handling.kafka.util;

import java.util.Optional;

public class ErrorHandlingUtils {
	
	public static String removeDotTSuffix(String s) {
		return Optional.ofNullable(s).filter(str -> str.length() != 0 && str.contains(".t"))
				.map(str -> str.substring(0, str.length() - 2)).orElse(s);
	}
	
	public static String getRetry1TopicFor(String topic) {
		return removeDotTSuffix(topic).concat(".retry1");
	}
	
	public static String getRetry2TopicFor(String topic) {
		return removeDotTSuffix(topic).concat(".retry2");
	}
	
	public static String getDlqTopicFor(String topic) {
		return removeDotTSuffix(topic).concat(".dlq");
	}

}
