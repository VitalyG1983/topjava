package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UserUtil;

import java.util.Locale;

import static ru.javawebinar.topjava.util.exception.ErrorType.VALIDATION_ERROR;

public class UserValidator implements Validator {

    @Autowired
    private UserService userService;

    @Autowired
    MessageSource messageSource;

    @Override
    public boolean supports(Class clazz) {
        return UserTo.class.isAssignableFrom(clazz) || User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user;
        User userByEmail = null;
        if (target.getClass().equals(UserTo.class)) {
            user = UserUtil.createFromTo((UserTo) target);
        } else {
            user = (User) target;
        }
        if (user.isNew()) {
            try {
                userByEmail = userService.getByEmail(user.getEmail());
            } catch (Exception e) {
                //e.printStackTrace();
            }
            //   update
        } else try {
            String newEmail = user.getEmail();
            String oldEmail = userService.get(user.getId()).getEmail();
            if (!oldEmail.equals(newEmail)) {
                userByEmail = userService.getByEmail(newEmail);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        if (userByEmail != null) {
            errors.rejectValue("email", String.valueOf(VALIDATION_ERROR),
                    messageSource.getMessage("user.doublicateEmail", new Object[]{}, Locale.getDefault()));
        }
    }
}
