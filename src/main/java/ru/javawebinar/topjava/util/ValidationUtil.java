package ru.javawebinar.topjava.util;


import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.*;
import java.util.HashSet;
import java.util.Set;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static <T> void preSave(T object, String operation) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);

        if (constraintViolations.size() > 0) {
            Set<ConstraintViolation<?>> propagatedViolations =
                    new HashSet<ConstraintViolation<?>>(constraintViolations.size());
            Set<String> classNames = new HashSet<String>();
            for (ConstraintViolation<?> violation : constraintViolations) {
                //LOG.trace(violation);
                propagatedViolations.add(violation);
                classNames.add(violation.getLeafBean().getClass().getName());
            }
            StringBuilder builder = new StringBuilder();
            builder.append("Validation failed for classes ");
            builder.append(classNames);
            builder.append(" during ");
            builder.append(operation).append(" operation");
            builder.append("\nList of constraint violations:[\n");
            for (ConstraintViolation<?> violation : constraintViolations) {
                builder.append("\t").append(violation.toString()).append("\n");
            }
            builder.append("]");

            throw new ConstraintViolationException(
                    builder.toString(), propagatedViolations
            );
        }
    }
}