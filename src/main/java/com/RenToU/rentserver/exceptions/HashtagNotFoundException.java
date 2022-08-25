package com.RenToU.rentserver.exceptions;

public class HashtagNotFoundException extends RuntimeException{
    public HashtagNotFoundException(Long id){
        super("Hashtag Not Found"+ id);
    }
    public HashtagNotFoundException(String name){
        super("Hashtag Not Found"+ name);
    }
}
