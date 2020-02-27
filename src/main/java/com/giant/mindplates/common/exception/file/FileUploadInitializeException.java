package com.giant.mindplates.common.exception.file;

public class FileUploadInitializeException extends RuntimeException {
    
	public FileUploadInitializeException(String message) {
        super(message);
    }

    public FileUploadInitializeException(String message, Throwable cause) {
        super(message, cause);
    }

}
