package com.elsevier.q2c.error.handling.kafka.template;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

interface ErrorHandlingProcessorTemplateInterface<V extends SpecificRecord> {
	
	KStream<String, ? extends SpecificRecord> createStreamForInitialTry(StreamsBuilder builderInitialTrySteamBuilder);
	
}
