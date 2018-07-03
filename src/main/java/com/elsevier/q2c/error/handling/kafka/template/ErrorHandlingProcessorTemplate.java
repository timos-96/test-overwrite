package com.elsevier.q2c.error.handling.kafka.template;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;

import com.elsevier.q2c.error.handling.kafka.compiler.ErrorHandlingStreamCompiler;
import com.elsevier.q2c.error.handling.kafka.processor.ErrorHandlingProcessor;
import com.elsevier.q2c.error.handling.kafka.util.ErrorHandlingUtils;

public abstract class ErrorHandlingProcessorTemplate<V extends SpecificRecord> 
				extends ErrorHandlingProcessor<V> implements ErrorHandlingProcessorTemplateInterface<V> {

	@Autowired
	ErrorHandlingStreamCompiler<V> streamCompiler;
	
	public KStream<String, V> createStreamForInitialTry(StreamsBuilder builderInitialTrySteamBuilder) {
		KStream<String, V> kStream = builderInitialTrySteamBuilder.stream(getConsumeFrom(), 
											Consumed.with(Serdes.String(), eventSerde));
		kStream = enableInitialTryHandling(streamCompiler.stream(kStream));
		return kStream;
	}
	
	public KStream<String, V> createStreamForFirstRetry(StreamsBuilder builderRetry1SteamBuilder) {
		KStream<String, V> kStreamRetry1 = builderRetry1SteamBuilder.stream(
				ErrorHandlingUtils.removeDotTSuffix(getConsumeFrom()).concat("-retry1.t"), 
				Consumed.with(Serdes.String(), eventSerde));
		kStreamRetry1 = enableRetry1Handling(streamCompiler.stream(kStreamRetry1));
		return kStreamRetry1;
	}
	
	public KStream<String, V> createStreamForSecondRetry(StreamsBuilder builderRetry2SteamBuilder) {
		KStream<String, V> kStreamRetry2 = builderRetry2SteamBuilder.stream(
				ErrorHandlingUtils.removeDotTSuffix(getConsumeFrom()).concat("-retry2.t"), 
				Consumed.with(Serdes.String(), eventSerde));
		kStreamRetry2 = enableRetry2Handling(streamCompiler.stream(kStreamRetry2));
		return kStreamRetry2;
	}
	
}
