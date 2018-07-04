package com.elsevier.q2c.error.handling.kafka.compiler;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.streams.kstream.KStream;

/**
 * 
 * Requires V to be a subclass of SpecificRecord.
 * 
 * @author spiliopoulosv
 *
 */
public abstract class ErrorHandlingStreamCompiler<V extends SpecificRecord> {
	
	/**
	 * Returns something that is a subclass of SpecificRecord
	 */
	public abstract KStream<String, ? extends SpecificRecord> stream(KStream<String, V> kStream);

}
