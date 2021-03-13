package com.michalpuzanov.munro.service;

import com.michalpuzanov.munro.domain.MunroRecord;

import java.io.IOException;
import java.util.List;

public interface CsvService {

    boolean importCsvData(String resourceName) throws IOException;
    List<MunroRecord> getMunroRecords(long size, String search, double minHeight, double maxHeight, String[] sort);

}
