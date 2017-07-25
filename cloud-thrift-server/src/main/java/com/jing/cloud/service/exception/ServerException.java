package com.jing.cloud.service.exception;


public class ServerException extends Exception{

    private static final long serialVersionUID = 7701896233590799201L;

    public ServerException(){
        super();
    }
    
    public ServerException(String message) {
        super(message);
    }
    
    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ServerException(Throwable cause) {
        super(cause);
    }
    
    protected ServerException(String message, Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
