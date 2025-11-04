package ru.nikitinsky.MySecondSpringBootApp.service;

import org.junit.jupiter.api.Test;
import ru.nikitinsky.MySecondSpringBootApp.model.Positions;

import static org.junit.jupiter.api.Assertions.*;

class AnnualBonusServiceImplTest {

    @Test
    void calculate() {

        Positions position = Positions.HR;
        double bonus = 2.0;
        int workDays = 243;
        double salary = 100000.00;

        double result = new AnnualBonusServiceImpl().calculate(position, salary, bonus, workDays);

        double expected = 360493.8271604938;
        assertEquals(result, expected);
    }
}