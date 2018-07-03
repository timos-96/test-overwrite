package com.elsevier.q2c.error.handling.kafka.compiler;

import org.apache.kafka.streams.kstream.KStream;

public abstract class ErrorHandlingStreamCompiler<V> {
	
	public abstract KStream<String, V> stream(KStream<String, V> kStream);

}
