package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static ru.javawebinar.topjava.util.exception.ErrorType.VALIDATION_ERROR;

public class MealValidator implements Validator {

    @Autowired
    private MealService mealService;

    @Autowired
    MessageSource messageSource;

    @Override
    public boolean supports(Class clazz) {
        return Meal.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Meal meal = (Meal) target;
        Meal foundedMeal = null;
        if (meal.isNew()) {
            try {
                foundedMeal = getMealByDateTime(meal);
            } catch (Exception e) {
                //e.printStackTrace();
            }
            ///////////////////   update   //////////////////
        } else try {
            LocalDateTime newDateTime = meal.getDateTime();
            LocalDateTime oldDateTime = mealService.get(meal.getId(), SecurityUtil.authUserId()).getDateTime();
            if (!oldDateTime.equals(newDateTime)) {
                foundedMeal = getMealByDateTime(meal);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        if (foundedMeal != null) {
            errors.rejectValue("dateTime", String.valueOf(VALIDATION_ERROR),
                    messageSource.getMessage("meal.doublicateDateTime", new Object[]{}, LocaleContextHolder.getLocale()));
        }
    }

    private Meal getMealByDateTime(Meal meal) {
        int userId = SecurityUtil.authUserId();
        List<Meal> mealsDateFiltered = mealService.getBetweenInclusive(meal.getDate(), meal.getDate(), userId);
        return mealsDateFiltered.stream().filter(m -> m.getTime().equals(meal.getTime())).findFirst().orElse(null);
    }
}