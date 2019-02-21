package com.elsevier.q2c.error.handling.kafka.template;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;

import com.elsevier.q2c.error.handling.kafka.compiler.ErrorHandlingStreamCompiler;
import com.elsevier.q2c.error.handling.kafka.processor.ErrorHandlingProcessor;

public abstract class ErrorHandlingProcessorTemplate<FromValueType extends SpecificRecord, SuccessValueType extends SpecificRecord> 
				extends ErrorHandlingProcessor<FromValueType, SuccessValueType> 
				implements ErrorHandlingProcessorTemplateInterface<FromValueType> {

	@Autowired
	ErrorHandlingStreamCompiler<FromValueType> streamCompiler;
	
	public KStream<String, ? extends SpecificRecord> createStreamForInitialTry(StreamsBuilder builderInitialTrySteamBuilder) {
		KStream<String, FromValueType> kStream = builderInitialTrySteamBuilder.stream(getConsumeFrom(), 
											Consumed.with(Serdes.String(), eventSerdeFrom));
		// set the sourceTopic and serviceName on the compiler, so they may be used by the wraptoDlqEvent()
		streamCompiler.setSourceTopic(getConsumeFrom());
		streamCompiler.setServiceName(getServiceName());
		return enableInitialTryHandling(streamCompiler.stream(kStream));
	}
	
}
