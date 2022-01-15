package org.dema.lab4.controller;

import org.dema.lab4.entity.HistoryItem;
import org.dema.lab4.entity.User;
import org.dema.lab4.repository.HistoryRepository;
import org.dema.lab4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
public class HomePageController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @RequestMapping(value = "/get_history", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public List<HistoryItem> getHistory() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        final User user = userRepository.getByUsername(username);
        return user.getHistory();
    }

    @RequestMapping(value = "/add_history_item", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<?> addHistoryItem(HistoryItem item, BindingResult result) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<HistoryItem>> violations = validator.validate(item);
        if (violations.isEmpty()) {
            item.setIsHit(isHit(item));
            historyRepository.save(item);
            final String username = SecurityContextHolder.getContext().getAuthentication().getName();
            final User user = userRepository.getByUsername(username);
            user.getHistory().add(item);
            userRepository.save(user);
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("\n"));
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }
    // 3 логику
    private boolean isHit(HistoryItem item) {
        double x = item.getX();
        double y = item.getY();
        double r = item.getR();
        if (x > 0) {
            if (y > 0) {
                return y <= r - 2 * x;
            } else {
                return r * r <= (x * x + y * y);
            }
        } else {
            if (y > 0) {
                return x >= -r && y <= r / 2;
            } else {
                return false;
            }
        }
    }
}
