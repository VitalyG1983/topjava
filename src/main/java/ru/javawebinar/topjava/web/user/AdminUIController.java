package ru.javawebinar.topjava.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.converter.DateTimeFormatters;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUIController extends AbstractUserController {

    @Override
    @GetMapping
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestParam String name,
                       @RequestParam String email,
                       @RequestParam String password) {
        super.create(new User(null, name, email, password, Role.USER));
    }

    @PostMapping ("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update( @PathVariable int id, @RequestParam String name, @RequestParam String email,
                       @RequestParam String password, @RequestParam boolean enabled, @RequestParam String registered) {
         DateTimeFormatters.LocalDateFormatter dateFormatter = new DateTimeFormatters.LocalDateFormatter();
         LocalDate localDate = dateFormatter.parse(registered, Locale.getDefault());
        super.update(new User(id, name, email, password, SecurityUtil.authUserCaloriesPerDay(), enabled, Date.valueOf(registered), Collections.singleton(Role.USER)), id);
    }

 /*   @PostMapping ("/{id}")    //  рабочий
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update( @PathVariable int id, HttpServletRequest request) {
        final Map<String, String[]> parameterMap = request.getParameterMap();
        final Object name = request.getAttribute("name");
        //super.update(new User(id, name, email, password, SecurityUtil.authUserCaloriesPerDay(), enabled, Date.valueOf(registered), Collections.singleton(Role.USER)), id);
    }*/

 /*   @PostMapping ( consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user) {
        super.update(user, user.id());
    }*/
}
