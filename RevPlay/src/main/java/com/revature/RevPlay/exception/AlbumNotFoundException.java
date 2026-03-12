package com.revature.RevPlay.exception;

public class AlbumNotFoundException extends RuntimeException{
    public AlbumNotFoundException(String message){
        super(message);
    }
}
