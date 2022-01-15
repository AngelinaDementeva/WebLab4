package org.dema.lab4.controller;

import org.dema.lab4.entity.User;
import org.dema.lab4.service.DatabaseUserDetailsService;
import org.dema.lab4.service.UserExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class LoginPageController {

    @Autowired
    private DatabaseUserDetailsService databaseUserDetailsService;

    @PostMapping("/register")
    public String register(User user, HttpServletRequest request, RedirectAttributes attributes) {
        final String redirectHome = "redirect:/home";
        final String redirectLogin = "redirect:/login";
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("\n"));
            attributes.addAttribute("register_error", errorMessage);
            return redirectLogin;
        }
        try {
            databaseUserDetailsService.registerUser(user);
            request.login(user.getUsername(), user.getPassword());
        } catch (UserExistsException e) {
            attributes.addAttribute("register_error", "Пользователь с данным именем уже существует");
            return redirectLogin;
        } catch (ServletException ignored) {}
        return redirectHome;
    }
}
