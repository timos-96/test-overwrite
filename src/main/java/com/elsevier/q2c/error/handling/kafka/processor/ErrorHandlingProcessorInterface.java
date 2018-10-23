package com.elsevier.q2c.error.handling.kafka.processor;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;

public interface ErrorHandlingProcessorInterface<FromValueType extends SpecificRecord, 
												 SuccessValueType extends SpecificRecord> {
	
	KStream<String, ? extends SpecificRecord> enableInitialTryHandling(KStream<String, ? extends SpecificRecord> kStream);
	
	Predicate<? super String, ? super SpecificRecord> getSuccessPredicate();
	
	Predicate<? super String, ? super SpecificRecord> getFailurePredicate();
	
	Predicate<? super String, ? super SpecificRecord> getFailbackPredicate();
	
	String getTargetTopicSuccess();

	String getConsumeFrom();

	String getDlqTopic();

}
