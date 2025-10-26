package ru.nikitinsky.MySecondSpringBootApp.service;

import org.springframework.stereotype.Service;
import ru.nikitinsky.MySecondSpringBootApp.model.Request;

@Service
public interface ModifyRequestService {
    void modify(Request request);
}
