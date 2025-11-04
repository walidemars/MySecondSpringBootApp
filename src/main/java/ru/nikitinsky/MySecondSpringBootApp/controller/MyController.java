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
import ru.nikitinsky.MySecondSpringBootApp.service.*;
import ru.nikitinsky.MySecondSpringBootApp.util.DateTimeUtil;
import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final ModifyRequestService modifySystemNameRequestService;
    private final ModifyRequestService modifySourceRequestService;
    private final AnnualBonusService annualBonusService;
    private final QuarterBonusService quarterBonusService;

    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService,
                         ModifyRequestService modifySystemNameRequestService,
                         ModifyRequestService modifySourceRequestService,
                        AnnualBonusService annualBonusService,
                        QuarterBonusService quarterBonusService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifySystemNameRequestService = modifySystemNameRequestService;
        this.modifySourceRequestService = modifySourceRequestService;
        this.annualBonusService = annualBonusService;
        this.quarterBonusService = quarterBonusService;
    }

    /*
     * Обработка входящего запроса feedback
     */
    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {

        log.info("Получен request: {}", request);

        Response response = createInitialResponse(request);

        try {
            processRequest(request, bindingResult);
            log.info("Код выполнен успешно");

        } catch (ValidationFailedException e) {
            return handleValidationError(response, e);
        } catch (UnsuportedCodeException e) {
            return handleUnsupportedError(response, e);
        } catch (Exception e) {
            return handleUnknownError(response, e);
        }

        response = modifyResponseService.modify(response);
        modifyRequestServices(request);

        log.info("Response после модификации: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
     * Создание начального response
     */
    private Response createInitialResponse(Request request) {
         return Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
    }

    private void processRequest(Request request, BindingResult bindingResult)
        throws ValidationFailedException, UnsuportedCodeException {

        validationService.isValid(bindingResult);

        if ("123".equals(request.getUid())) {
            throw new UnsuportedCodeException("Недопустимый id - 123");
        }

        calculateBonus(request);
    }

    private void calculateBonus(Request request) {
        if (request.getPosition() != null && request.getSalary() != null &&
                request.getBonus() != null && request.getWorksDay() != null) {

            // Расчет годовой премии
            double annualBonus = annualBonusService.calculate(
                    request.getPosition(),
                    request.getSalary(),
                    request.getBonus(),
                    request.getWorksDay()
            );
            log.info("Рассчитана годовая премия: {}", annualBonus);

            // Расчет квартальной премии (только для менеджеров)
            if (request.getPosition().isManager()) {
                try {
                    double quarterlyBonus = quarterBonusService.calculate(
                            request.getPosition(),
                            request.getSalary(),
                            request.getBonus() * 0.25 // пример коэффициента
                    );
                    log.info("Рассчитана квартальная премия: {}", quarterlyBonus);
                } catch (IllegalArgumentException e) {
                    log.warn("Квартальная премия не доступна: {}", e.getMessage());
                }
            }
        }
    }

    /*
     * Модификация request сервисами
     */
    private void modifyRequestServices(Request request) {
        modifySystemNameRequestService.modify(request);
        modifySourceRequestService.modify(request);
    }

    /*
     * Обработка ошибки валидации
     */
    private ResponseEntity<Response> handleValidationError(Response response, ValidationFailedException e) {
        log.error("Ошибка валидации: {}", e.getMessage());
        response.setCode(Codes.FAILED);
        response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
        response.setErrorMessage(ErrorMessages.VALIDATION);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /*
     * Обработка неподдерживаемой ошибки
     */
    private ResponseEntity<Response> handleUnsupportedError(Response response, UnsuportedCodeException e) {
        log.error("Неподдерживаемая ошибка: {}", e.getMessage());
        response.setCode(Codes.FAILED);
        response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
        response.setErrorMessage(ErrorMessages.UNSUPPORTED);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /*
     * Обработка неизвестной ошибки
     */
    private ResponseEntity<Response> handleUnknownError(Response response, Exception e) {
        log.error("Неизвестная ошибка: {}", e.getMessage());
        response.setCode(Codes.FAILED);
        response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
        response.setErrorMessage(ErrorMessages.UNKNOWN);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
