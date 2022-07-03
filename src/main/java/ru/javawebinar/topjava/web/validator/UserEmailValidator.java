package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UserUtil;

import java.util.Objects;

@Component
public class UserEmailValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class clazz) {
        return UserTo.class.isAssignableFrom(clazz) || User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user;
        if (target.getClass().equals(UserTo.class)) {
            user = UserUtil.createFromTo((UserTo) target);
        } else {
            user = (User) target;
        }
        User userByEmail = userRepository.getByEmail(user.getEmail());
        if (userByEmail != null && (user.isNew() || !Objects.equals(user.getId(), userByEmail.getId()))) {
            errors.rejectValue("email", "user.doublicateEmail",
                    messageSource.getMessage("user.doublicateEmail", new Object[]{}, LocaleContextHolder.getLocale()));
        }
    }
}
