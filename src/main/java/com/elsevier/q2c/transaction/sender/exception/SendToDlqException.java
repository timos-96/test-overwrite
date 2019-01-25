package com.elsevier.q2c.transaction.sender.exception;

public class SendToDlqException extends RuntimeException {
	
	private static final long serialVersionUID = -5748274801660943252L;
	
	public SendToDlqException() {
        super();
    }
    public SendToDlqException(String s) {
        super(s);
    }
    public SendToDlqException(String s, Throwable throwable) {
        super(s, throwable);
    }
    public SendToDlqException(Throwable throwable) {
        super(throwable);
    }

}
