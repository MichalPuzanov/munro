package com.michalpuzanov.munro.service;


import com.michalpuzanov.munro.domain.MunroRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
public class CsvServiceImplTest {

    @TestConfiguration
    static class CsvServiceImplTestConfig {

        @Bean
        public CsvService csvService() {
            return new CsvServiceImpl();
        }
    }

    @Autowired
    private CsvServiceImpl csvServiceImpl;

    @Test
    public void whenExistCsv_thenImportDataToList() throws IOException {
        boolean result = csvServiceImpl.importCsvData("munrotabtest.csv");

        assertThat(result).isTrue();
        assertThat(csvServiceImpl.getMunroRecords().isEmpty()).isFalse();

    }

    @Test(expected = NullPointerException.class)
    public void whenCsvFileDoesNotExist_thenNullPointerException() throws IOException {
        csvServiceImpl.importCsvData("munrotab.csv");
    }

    @Test
    public void givenCsvDataAreImported_whenAskForAllRecords_thenReturnAllRecords() {
        try {
            boolean given = csvServiceImpl.importCsvData("munrotabtest.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<MunroRecord> results = csvServiceImpl.getMunroRecords();

        assertThat(csvServiceImpl.getMunroRecords().size()).isEqualTo(3);
    }

    @Test
    public void givenCsvDataAreImported_whenAskFor2Records_thenReturnRecords() {
        try {
            boolean given = csvServiceImpl.importCsvData("munrotabtest.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<MunroRecord> results = csvServiceImpl.limitMunroRecords(csvServiceImpl.getMunroRecords(),2);

        assertThat(results.size()).isEqualTo(2);
    }

    @Test
    public void givenCsvDataAreImported_whenMinHeight960_thenReturnRecords() {
        try {
            boolean given = csvServiceImpl.importCsvData("munrotabtest.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<MunroRecord> results = csvServiceImpl.minHeightMunroRecords(csvServiceImpl.getMunroRecords(),960);

        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0).getHeightM()).isGreaterThanOrEqualTo(960);
        assertThat(results.get(1).getHeightM()).isGreaterThanOrEqualTo(960);
    }

    @Test
    public void givenCsvDataAreImported_whenMaxHeight960_thenReturnRecords() {
        try {
            boolean given = csvServiceImpl.importCsvData("munrotabtest.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<MunroRecord> results = csvServiceImpl.maxHeightMunroRecords(csvServiceImpl.getMunroRecords(),960);

        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getHeightM()).isLessThanOrEqualTo(960);
    }

}
