package com.giant.mindplates.common.exception.file;

public class FileAlreadyExistException extends RuntimeException {
    
	public FileAlreadyExistException(String message) {
        super(message);
    }

    public FileAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
