package ru.javawebinar.topjava.service.jdbc;

import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
//@Ignore
public class JdbcUserServiceTest extends AbstractUserServiceTest {
    @Autowired
    public static Environment environment;

    @BeforeClass
    public static void checkEnv() throws Exception {
        //environment..getProperty();
        Assume.assumeTrue(true);
    }

    @Before
    public void initt() {
        //get the env properties or throw injected bean to init other bean
        ActiveDbProfileResolver defProfile = new ActiveDbProfileResolver();
        // defProfile.resolve(aClassStatic);
    }
}