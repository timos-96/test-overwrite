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

	@Autowired
	public SpecificAvroSerde<FromValueType> eventSerdeFrom;
	
	@Autowired
	public SpecificAvroSerde<SuccessValueType> eventSerdeSuccess;
	
	public KStream<String, ? extends SpecificRecord> enableInitialTryHandling(KStream<String, ? extends SpecificRecord> kStream) {
		return enablingErrorHandling(kStream, "-retry1.t");
	}

	public KStream<String, ? extends SpecificRecord> enableRetry1Handling(KStream<String, ? extends SpecificRecord> kStream) {
		return enablingErrorHandling(kStream, "-retry2.t");
	}

	public KStream<String, ? extends SpecificRecord> enableRetry2Handling(KStream<String, ? extends SpecificRecord> kStream) {
		return enablingErrorHandling(kStream, "-dlq.t");
	}

	@SuppressWarnings("unchecked")
	private KStream<String, ? extends SpecificRecord> enablingErrorHandling(KStream<String, 
																	? extends SpecificRecord> kStream,
																String topicSuffix) {
		KStream<String, ? extends SpecificRecord>[] branches = kStream.branch(getSuccessPredicate(), 
																			  getFailurePredicate(),
																			  getFailbackPredicate());
		// forward to proper topics
		((KStream<String, SuccessValueType>)branches[0]).to(getTargetTopicSuccess(), Produced.with(Serdes.String(), eventSerdeSuccess));
		((KStream<String, FromValueType>)branches[1]).to(ErrorHandlingUtils.removeDotTSuffix(getConsumeFrom()).concat(topicSuffix),
				Produced.with(Serdes.String(), eventSerdeFrom));
		return kStream;
	}
}
