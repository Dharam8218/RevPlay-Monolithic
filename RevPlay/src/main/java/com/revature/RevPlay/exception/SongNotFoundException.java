package com.revature.RevPlay.exception;

public class SongNotFoundException extends RuntimeException{

    public SongNotFoundException(String message){
        super(message);
    }
}
