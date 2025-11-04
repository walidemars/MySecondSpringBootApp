package ru.nikitinsky.MySecondSpringBootApp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {

    /* Уникальный идентификатор сообщения */
    private String uid;

    /* Уникальный идентификатор операции */
    private String operationUid;

    /* Время формирования ответа */
    private String systemTime;

    /* Код выполнения операции */
    private Codes code;

    /* Код ошибки */
    private ErrorCodes errorCode;

    /* Сообщение об ошибке */
    private ErrorMessages errorMessage;
}
