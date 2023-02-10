package com.example.smsdemo.models;


public class LogSession {
    private static User user;
    private static boolean logStatus, regStatus;


    public static void setUser(User newUser){
        user = newUser;
        if (newUser==null){
            logStatus=false;
            regStatus=false;
        }else {
            logStatus=user.isOnline;
            regStatus=user.isRegistered;
        }

    }
    public static User getUser(){
        return user;
    }
    public static boolean isUserSet(){
        return user!=null;
    }

    public static boolean isOnline(){
        return logStatus;
    }



}
