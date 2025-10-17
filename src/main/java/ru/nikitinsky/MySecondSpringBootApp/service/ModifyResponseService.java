package ru.nikitinsky.MySecondSpringBootApp.service;

import org.springframework.stereotype.Service;
import ru.nikitinsky.MySecondSpringBootApp.model.Response;

@Service
public interface ModifyResponseService {

    Response modify(Response response);
}
