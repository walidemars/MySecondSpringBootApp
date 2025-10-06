package ru.nikitinsky.MySecondSpringBootApp.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.nikitinsky.MySecondSpringBootApp.exception.ValidationFailedException;

@Service
public interface ValidationService {
    void isValid(BindingResult bindingResult) throws ValidationFailedException;
}
