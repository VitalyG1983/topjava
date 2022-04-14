DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

DELETE FROM meals;

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2020-01-28 10:00:00', 'User Завтрак', 501),
       (100000, '2020-01-30 13:00:00', 'User Обед', 1000),
       (100000, '2020-01-29 20:00:00', 'User Ужин', 500),
       (100001, '2020-12-25 10:00:00', 'Admin Завтрак', 1000),
       (100001, '2020-12-31 13:00:00', 'Admin Обед', 500),
       (100001, '2020-12-31 20:00:00', 'Admin Ужин', 410);

/*public static final Meal userMeal1 = new Meal(USER_MEAL1_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "User Завтрак", 501);
public static final Meal userMeal2 = new Meal(USER_MEAL2_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "User Обед", 1000);
public static final Meal userMeal3 = new Meal(USER_MEAL3_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "User Ужин", 500);
public static final Meal adminMeal1 = new Meal(ADMIN_MEAL1_ID, LocalDateTime.of(2020, Month.DECEMBER, 25, 10, 0), "Admin Завтрак", 1000);
public static final Meal adminMeal2 = new Meal(ADMIN_MEAL2_ID, LocalDateTime.of(2020, Month.DECEMBER, 31, 13, 0), "Admin Обед", 500);
public static final Meal adminMeal3 = new Meal(ADMIN_MEAL3_ID, LocalDateTime.of(2020, Month.DECEMBER, 31, 20, 0), "Admin Ужин", 410);*/
