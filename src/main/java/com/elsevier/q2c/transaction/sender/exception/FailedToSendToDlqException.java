package com.elsevier.q2c.transaction.sender.exception;

public class FailedToSendToDlqException extends RuntimeException {
	
	private static final long serialVersionUID = -5748274801660943252L;
	
	public FailedToSendToDlqException() {
        super();
    }
    public FailedToSendToDlqException(String s) {
        super(s);
    }
    public FailedToSendToDlqException(String s, Throwable throwable) {
        super(s, throwable);
    }
    public FailedToSendToDlqException(Throwable throwable) {
        super(throwable);
    }

}
