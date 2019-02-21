package com.elsevier.q2c.transaction.sender.exception;

public class OpenCircuitOrRecoverableErrorException extends RuntimeException {
	
	private static final long serialVersionUID = -5748274801660943252L;
	
	public OpenCircuitOrRecoverableErrorException() {
        super();
    }
    public OpenCircuitOrRecoverableErrorException(String s) {
        super(s);
    }
    public OpenCircuitOrRecoverableErrorException(String s, Throwable throwable) {
        super(s, throwable);
    }
    public OpenCircuitOrRecoverableErrorException(Throwable throwable) {
        super(throwable);
    }

}
