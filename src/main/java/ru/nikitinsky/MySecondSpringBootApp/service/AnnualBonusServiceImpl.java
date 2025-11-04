package ru.nikitinsky.MySecondSpringBootApp.service;

import org.springframework.stereotype.Service;
import ru.nikitinsky.MySecondSpringBootApp.model.Positions;

import java.time.Year;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {
    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays) {
        int daysInYear = isLeapYear() ? 366 : 365;
        return salary * bonus * 365 * positions.getPositionCoefficient() / workDays;
    }

    private boolean isLeapYear() {
        return Year.now().isLeap();
    }
}
