package com.giant.mindplates.common.exception.file;

public class FileExtensionException extends RuntimeException {
    
	public FileExtensionException(String message) {
        super(message);
    }

    public FileExtensionException(String message, Throwable cause) {
        super(message, cause);
    }

}
