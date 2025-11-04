package ru.nikitinsky.MySecondSpringBootApp.service;

import com.sun.source.tree.BreakTree;
import org.springframework.stereotype.Service;
import ru.nikitinsky.MySecondSpringBootApp.model.Positions;

@Service
public class QuarterBonusService {
    public double calculate(Positions position, double salary, double quarterBonus) {
        if (!position.isManager()) {
            throw new IllegalArgumentException("Квартальная премия доступна только для менеджеров. " +
                    "Сотрудник с должностью " + position + " не является менеджером.");
        }
        return salary * quarterBonus * position.getPositionCoefficient();
    }
}
