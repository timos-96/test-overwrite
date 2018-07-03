package com.elsevier.q2c.error.handling.kafka.template;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

interface ErrorHandlingProcessorTemplateInterface<V> {
	
	KStream<String, V> createStreamForInitialTry(StreamsBuilder builderInitialTrySteamBuilder);
	
	KStream<String, V> createStreamForFirstRetry(StreamsBuilder builderRetry1SteamBuilder);
	
	KStream<String, V> createStreamForSecondRetry(StreamsBuilder builderRetry2SteamBuilder);

}
