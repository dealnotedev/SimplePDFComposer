package biz.dealnote.powercodetestapp;

import biz.dealnote.powercodetestapp.di.AppComponent;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class Injections {
    static AppComponent component;

    public static AppComponent component(){
        if(component == null){
            throw new IllegalStateException();
        }

        return component;
    }
}