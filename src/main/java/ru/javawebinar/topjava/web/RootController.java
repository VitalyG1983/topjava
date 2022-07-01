package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

@Controller
public class RootController {
    private static final Logger log = LoggerFactory.getLogger(RootController.class);

    @GetMapping("/")
    public String root() {
        log.info("root");
        return "redirect:meals";
    }

    //    @Secured("ROLE_ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String getUsers() {
        log.info("users");
        return "users";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest req, HttpServletResponse res) throws IOException {
        log.info("login");
        final String lang = req.getParameter("lang");
        final Locale locale = LocaleContextHolder.getLocale();
        final String language = req.getLocale().getLanguage();
        if (req.getParameter("lang") == null) {
            //return "redirect:login?lang=" + LocaleContextHolder.getLocale();
            return "redirect:login?lang=" + req.getLocale().getLanguage();
        }
        return "login";
    }

    @GetMapping("/meals")
    public String getMeals() {
        log.info("meals");
        return "meals";
    }


}
