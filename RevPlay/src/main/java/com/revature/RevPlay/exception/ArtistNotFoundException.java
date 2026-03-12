package com.revature.RevPlay.exception;

public class ArtistNotFoundException extends RuntimeException{

    public ArtistNotFoundException(String message){
        super(message);
    }
}
