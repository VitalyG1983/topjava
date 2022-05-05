package ru.javawebinar.topjava;

import org.springframework.lang.NonNull;
import org.springframework.test.context.ActiveProfilesResolver;

//http://stackoverflow.com/questions/23871255/spring-profiles-simple-example-of-activeprofilesresolver
public class ActiveDbProfileResolver implements ActiveProfilesResolver {
    static  Class<?> aClassStatic;

    @Override
    public @NonNull
    String[] resolve(@NonNull Class<?> aClass) {
        if (aClassStatic == null) {
            aClassStatic = aClass;
            return new String[]{Profiles.getActiveDbProfile()};
        }
        if (EnvConfig.stnEnv == null)
            return new String[]{""};
        return EnvConfig.stnEnv.getActiveProfiles();
    }
}