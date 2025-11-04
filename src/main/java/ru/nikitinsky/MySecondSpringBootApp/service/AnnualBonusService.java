package ru.nikitinsky.MySecondSpringBootApp.service;

import org.springframework.stereotype.Service;
import ru.nikitinsky.MySecondSpringBootApp.model.Positions;

@Service
public interface AnnualBonusService {
    double calculate(Positions positions, double salary, double bonus, int workDays);
}
