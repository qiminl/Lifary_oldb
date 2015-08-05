package com.example.liuqimin.lifary;

/**
 * Created by liuqimin on 15-07-04.
 */
public class User {

    private int _id;
    private String _username;
    private String _password;

    public User(){}

    public User(String username, String password){
        _username = username;
        _password = password;

    }
    public User(int id, String username, String password){
        _id = id;
        _username = username;
        _password = password;
    }

    public void setID(int id){
        _id = id;
    }
    public int getID(){
        return _id;
    }
    public void setUsername(String username){
        _username = username;
    }
    public String getUsername(){
        return _username;
    }
    public void setPassword(String password){
        _password = password;
    }

    public String getPassword(){
        return _password;
    }

}
