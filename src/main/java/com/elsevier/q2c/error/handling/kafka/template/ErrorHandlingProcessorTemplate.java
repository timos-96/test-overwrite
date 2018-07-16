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

public abstract class ErrorHandlingProcessorTemplate<FromValueType extends SpecificRecord, SuccessValueType extends SpecificRecord> 
				extends ErrorHandlingProcessor<FromValueType, SuccessValueType> 
				implements ErrorHandlingProcessorTemplateInterface<FromValueType> {

	@Autowired
	ErrorHandlingStreamCompiler<FromValueType> streamCompiler;
	
	public KStream<String, ? extends SpecificRecord> createStreamForInitialTry(StreamsBuilder builderInitialTrySteamBuilder) {
		KStream<String, FromValueType> kStream = builderInitialTrySteamBuilder.stream(getConsumeFrom(), 
											Consumed.with(Serdes.String(), eventSerdeFrom));
		return enableInitialTryHandling(streamCompiler.stream(kStream));
	}
	
	public KStream<String, ? extends SpecificRecord> createStreamForFirstRetry(StreamsBuilder builderRetry1SteamBuilder) {
		KStream<String, FromValueType> kStreamRetry1 = builderRetry1SteamBuilder.stream(
				ErrorHandlingUtils.removeDotTSuffix(getConsumeFrom()).concat(ErrorHandlingProcessor.RETRY1_SUFFIX), 
				Consumed.with(Serdes.String(), eventSerdeFrom));
		return enableRetry1Handling(streamCompiler.stream(kStreamRetry1));
	}
	
	public KStream<String, ? extends SpecificRecord> createStreamForSecondRetry(StreamsBuilder builderRetry2SteamBuilder) {
		KStream<String, FromValueType> kStreamRetry2 = builderRetry2SteamBuilder.stream(
				ErrorHandlingUtils.removeDotTSuffix(getConsumeFrom()).concat(ErrorHandlingProcessor.RETRY2_SUFFIX), 
				Consumed.with(Serdes.String(), eventSerdeFrom));
		return enableRetry2Handling(streamCompiler.stream(kStreamRetry2));
	}
	
}
