package ru.nikitinsky.MySecondSpringBootApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.nikitinsky.MySecondSpringBootApp.model.Response;
import ru.nikitinsky.MySecondSpringBootApp.util.DateTimeUtil;

import java.util.Date;

@Slf4j
@Service
@Qualifier("ModifySystemTimeResponseService")
public class ModifySystemTimeResponseService
            implements ModifyResponseService {

    @Override
    public Response modify(Response response) {

        String time = DateTimeUtil.getCustomFormat().format(new Date());
        response.setSystemTime(time);
        log.info("ModifySystemTimeResponseService: systemTime изменён -> {}", time);

        return response;
    }
}
