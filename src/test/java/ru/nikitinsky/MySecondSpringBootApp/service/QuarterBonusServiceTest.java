package ru.nikitinsky.MySecondSpringBootApp.service;

import org.junit.jupiter.api.Test;
import ru.nikitinsky.MySecondSpringBootApp.model.Positions;

import static org.junit.jupiter.api.Assertions.*;

class QuarterBonusServiceTest {

    @Test
    void calculate_ForManager() {

        Positions position = Positions.TL;
        double salary = 100000.00;
        double quarterBonus = 0.25;

        double result = new QuarterBonusService().calculate(position, salary, quarterBonus);

        double expected = 100000.00 * 0.25 * 2.6;
        assertEquals(expected, result);
    }
}