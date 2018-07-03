package com.elsevier.q2c.error.handling.kafka.processor;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;

import com.elsevier.q2c.error.handling.kafka.util.ErrorHandlingUtils;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

public abstract class ErrorHandlingProcessor<V extends SpecificRecord> implements ErrorHandlingProcessorInterface<V> {

	@Autowired
	public SpecificAvroSerde<V> eventSerde;
	
	public KStream<String, V> enableInitialTryHandling(KStream<String, V> kStream) {
		return enablingErrorHandling(kStream, "-retry1.t");
	}

	public KStream<String, V> enableRetry1Handling(KStream<String, V> kStream) {
		return enablingErrorHandling(kStream, "-retry2.t");
	}

	public KStream<String, V> enableRetry2Handling(KStream<String, V> kStream) {
		return enablingErrorHandling(kStream, "-dlq.t");
	}

	@SuppressWarnings("unchecked")
	private KStream<String, V> enablingErrorHandling(KStream<String, V> kStream, String topicSuffix) {
		KStream<String, V>[] branches = kStream.branch(getSuccessPredicate(), getFailurePredicate(),
				getFailbackPredicate());
		// forward to proper topics
		branches[0].to(getTargetTopicSuccess(), Produced.with(Serdes.String(), eventSerde));
		branches[1].to(ErrorHandlingUtils.removeDotTSuffix(getConsumeFrom()).concat(topicSuffix),
				Produced.with(Serdes.String(), eventSerde));
		return kStream;
	}
}
