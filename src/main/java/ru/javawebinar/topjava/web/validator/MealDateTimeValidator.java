package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.web.SecurityUtil;

@Component
public class MealDateTimeValidator implements Validator {

    @Autowired
    private MealRepository repository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class clazz) {
        return Meal.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Meal meal = (Meal) target;
        Meal foundedMeal = null;
        foundedMeal = repository.getByDateTime(meal.getDateTime(), SecurityUtil.authUserId());
        if (foundedMeal != null && (meal.isNew() || (!meal.isNew() && meal.getId() != foundedMeal.getId()))) {
            errors.rejectValue("dateTime", "meal.doublicateDateTime",
                    messageSource.getMessage("meal.doublicateDateTime", new Object[]{}, LocaleContextHolder.getLocale()));
        }
    }
}