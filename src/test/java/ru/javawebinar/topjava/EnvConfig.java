package ru.javawebinar.topjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.StandardEnvironment;

import javax.annotation.PostConstruct;

import static ru.javawebinar.topjava.ActiveDbProfileResolver.aClassStatic;

public class EnvConfig {

    static StandardEnvironment stnEnv;

    @Autowired
    public EnvConfig(StandardEnvironment stnEnv) {
        EnvConfig.stnEnv = stnEnv;
    }

   @PostConstruct
    public void init(){
        //get the env properties or throw injected bean to init other bean
        ActiveDbProfileResolver defProfile = new ActiveDbProfileResolver();
       // defProfile.resolve(aClassStatic);
    }
}