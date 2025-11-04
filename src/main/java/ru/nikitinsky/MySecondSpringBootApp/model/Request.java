package ru.nikitinsky.MySecondSpringBootApp.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    /* Уникальный идентификатор сообщения */
    @NotBlank(message = "uid не может быть пустым")
    @Size(max = 32, message = "uid должен быть не длиннее 32 символов")
    private String uid;

    /* Уникальный идентификатор операции */
    @NotBlank(message = "operationUid не может быть пустым")
    @Size(max = 32, message = "operationUid должен быть не длиннее 32 символов")
    private String operationUid;

    /* Наименование системы отправителя */
    private Systems systemName;

    /* Время создания сообщения в системе отправителя */
    @NotBlank(message = "systemTime не может быть пустым")
    private String systemTime;

    /* Наименование ресурса */
    private String source;
    /* Должность сотрудника */
    private Positions position;
    /* Заработная плата сотрудника */
    private Double salary;
    /* Коэффициент бонуса */
    private Double bonus;
    /* Количество рабочих дней */
    private Integer worksDay;

    /* Идентификатор коммуникации */
    @Min(value = 1, message = "communicationId должен быть минимум 1")
    @Max(value = 100000, message = "communicationId должен быть максимум 100000")
    private int communicationId;

    /* Идентификатор шаблона */
    private int templateId;
    /* Код продукта */
    private int productCode;
    /* СМС код */
    private int smsCode;

    /* Время получения запроса */
    private Date requestTime;

    @Override
    public String toString() {
        return "{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", position='" + position.name() + '\'' +
                ", salary=" + salary +
                ", bonus=" + bonus +
                ", worksDay=" + worksDay +
                ", communicationId='" + communicationId + '\'' +
                ", templateId='" + templateId + '\'' +
                ", productCode='" + productCode + '\'' +
                ", smsCode='" + smsCode + '\'' +
                '}';
    }
}
