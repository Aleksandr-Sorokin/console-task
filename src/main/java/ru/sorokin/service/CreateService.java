package ru.sorokin.service;

import ru.sorokin.model.entity.Criterias;
import ru.sorokin.model.entity.Statistics;

import java.io.File;

public interface CreateService {
    String createFileCriteria(Criterias criterias, File fileOutput);

    String createFileStat(Statistics statistics, File fileOutput);
}
