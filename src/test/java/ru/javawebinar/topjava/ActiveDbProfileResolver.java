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
            return Profiles.getActiveDbProfile();
        }
        //дальше код не дойдет,
        // если убрать DATAJPA из getActiveDbProfile(), то Spring выдаст ошибку инициализации бина для репозитория,
        //тк не сможет подобрать нужный класс DataJpaMealRepository для MealRepository
        //  В переменной EnvConfig.stnEnv можем посмотреть текущий активный профиль для БД,
        //  но эта переменная инициализируется только после инициализации всего @ContextConfiguration
        if (EnvConfig.stnEnv == null)
            return new String[]{""};
        return EnvConfig.stnEnv.getActiveProfiles();
    }
}