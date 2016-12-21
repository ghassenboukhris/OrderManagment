package com.example.ghassen.orderyoyo;

/**
 * Created by ghassen on 01/10/2016.
 */
public class AccessToken {
    public String access_token;

    public long expires_in;

    public AccessToken(String access_token,long expires_in)
    {
        this.access_token=access_token;

        this.expires_in=expires_in;


    }

}
