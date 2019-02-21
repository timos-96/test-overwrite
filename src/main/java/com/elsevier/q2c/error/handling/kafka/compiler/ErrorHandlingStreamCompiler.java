package com.elsevier.q2c.error.handling.kafka.compiler;

import java.time.ZonedDateTime;

import org.apache.avro.specific.SpecificRecord;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.streams.kstream.KStream;

import com.elsevier.q2c.avro.utilities.Avro2JsonConverter;
import com.elsevier.q2c.schema.avro.dlq.DlqEvents;
import com.elsevier.q2c.transaction.sender.exception.FailedToSendToDlqException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Requires V to be a subclass of SpecificRecord.
 * 
 * @author spiliopoulosv
 *
 */
@Slf4j
public abstract class ErrorHandlingStreamCompiler<V extends SpecificRecord> {
	
	String sourceTopic;
	
	String serviceName;

	/**
	 * Returns something that is a subclass of SpecificRecord
	 */
	public abstract KStream<String, ? extends SpecificRecord> stream(KStream<String, V> kStream);

	protected DlqEvents wrapToDlqEvent(SpecificRecord event, Exception ex) {
		String payload;
		try {
			payload = Avro2JsonConverter.getJsonStringNoUnionTypes(event);
		} catch (Exception e) {
			log.error("We cannot wrap the event {} to a DlqEvent - so we rethrow to block the partition", event, ex);
			throw new FailedToSendToDlqException(e);
		}
		// push the wrapped failed event to the DLQ topic
		return DlqEvents.newBuilder()
							.setType(event.getClass().getSimpleName())
							.setException(ExceptionUtils.getStackTrace(ex))
							.setExceptionRootCause(ExceptionUtils.getRootCauseMessage(ex))
							.setMessage(ex.getMessage())
							.setDate(ZonedDateTime.now().toInstant().toEpochMilli())
							.setPayload(payload)
							.setSourceTopic(getSourceTopic())
							.setService(getServiceName()).build();
	}

	public String getSourceTopic() {
		return sourceTopic;
	}

	public void setSourceTopic(String sourceTopic) {
		this.sourceTopic = sourceTopic;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
}
