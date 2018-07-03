package com.elsevier.q2c.error.handling.kafka.processor;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;

public interface ErrorHandlingProcessorInterface<V extends SpecificRecord> {
	
	KStream<String, V> enableInitialTryHandling(KStream<String, V> kStream);
	
	KStream<String, V> enableRetry1Handling(KStream<String, V> kStream);
	
	KStream<String, V> enableRetry2Handling(KStream<String, V> kStream);
	
	Predicate<? super String, ? super V> getSuccessPredicate();
	
	Predicate<? super String, ? super V> getFailurePredicate();
	
	Predicate<? super String, ? super V> getFailbackPredicate();
	
	String getTargetTopicSuccess();
	
	String getConsumeFrom();

}
