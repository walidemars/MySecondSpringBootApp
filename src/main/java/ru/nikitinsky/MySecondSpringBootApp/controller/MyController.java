package ru.nikitinsky.MySecondSpringBootApp.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nikitinsky.MySecondSpringBootApp.exception.UnsuportedCodeException;
import ru.nikitinsky.MySecondSpringBootApp.exception.ValidationFailedException;
import ru.nikitinsky.MySecondSpringBootApp.model.*;
import ru.nikitinsky.MySecondSpringBootApp.service.ModifyRequestService;
import ru.nikitinsky.MySecondSpringBootApp.service.ModifyResponseService;
import ru.nikitinsky.MySecondSpringBootApp.service.ValidationService;
import ru.nikitinsky.MySecondSpringBootApp.util.DateTimeUtil;
import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final ModifyRequestService modifySystemNameRequestService;
    private final ModifyRequestService modifySourceRequestService;

    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService,
                         ModifyRequestService modifySystemNameRequestService,
                         ModifyRequestService modifySourceRequestService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifySystemNameRequestService = modifySystemNameRequestService;
        this.modifySourceRequestService = modifySourceRequestService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {

        log.info("Получен request: {}", request);

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        log.info("Создан начальный response: {}", response);

        try {
            validationService.isValid(bindingResult);

            if (request.getUid().equals("123")) {
                log.error("Недопустимый uid: ", request.getUid());
                throw new UnsuportedCodeException("Недопустимый id - 123");
            }

            log.info("Код выполнен успешно");

        } catch (ValidationFailedException e) {
            log.error("Ошибка валидации: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (UnsuportedCodeException e) {
            log.error("Неподдерживаемая ошибка: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Неизвестная ошибка: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response = modifyResponseService.modify(response);
        log.info("Response после модификации: {}", response);

        modifySystemNameRequestService.modify(request);
        modifySourceRequestService.modify(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
