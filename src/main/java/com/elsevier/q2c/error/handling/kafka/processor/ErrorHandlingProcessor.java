package com.elsevier.q2c.error.handling.kafka.processor;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;

import com.elsevier.q2c.error.handling.kafka.util.ErrorHandlingUtils;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

public abstract class ErrorHandlingProcessor<FromValueType extends SpecificRecord, 
											SuccessValueType extends SpecificRecord> 
				implements ErrorHandlingProcessorInterface<FromValueType, SuccessValueType> {
		
	public static String DLQ_SUFFIX = ".dlq";

	@Autowired
	public SpecificAvroSerde<FromValueType> eventSerdeFrom;
	
	@Autowired
	public SpecificAvroSerde<SuccessValueType> eventSerdeSuccess;
	
	public KStream<String, ? extends SpecificRecord> enableInitialTryHandling(KStream<String, ? extends SpecificRecord> kStream) {
		return enablingErrorHandling(kStream, getDlqTopic());
	}

	@SuppressWarnings("unchecked")
	private KStream<String, ? extends SpecificRecord> enablingErrorHandling(KStream<String, 
																	? extends SpecificRecord> kStream,
																String errorTopic) {
		KStream<String, ? extends SpecificRecord>[] branches = kStream.branch(getSuccessPredicate(), 
																			  getFailurePredicate(),
																			  getFailbackPredicate());
		// forward to proper topics
		((KStream<String, SuccessValueType>)branches[0]).to(getTargetTopicSuccess(), Produced.with(Serdes.String(), eventSerdeSuccess));
		((KStream<String, FromValueType>)branches[1]).to(errorTopic, Produced.with(Serdes.String(), eventSerdeFrom));
		return kStream;
	}

	@Override
	public String getDlqTopic() {
		return ErrorHandlingUtils.removeDotTSuffix(getConsumeFrom()).concat(DLQ_SUFFIX);
	}
}
