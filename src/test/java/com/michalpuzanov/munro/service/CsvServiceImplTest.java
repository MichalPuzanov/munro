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
import java.util.stream.Collectors;

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
    public void whenDoesNotExistCsv_thenNullPointerException() throws IOException {
        csvServiceImpl.importCsvData("munrotab.csv");
    }

    @Test
    public void givenCsvDataAreImported_whenAskForAllRecords_thenReturnAllRecords() {
        importCsv();
        List<MunroRecord> results = csvServiceImpl.getMunroRecords();

        assertThat(csvServiceImpl.getMunroRecords().size()).isEqualTo(610);
    }

    private void importCsv() {
        try {
            boolean given = csvServiceImpl.importCsvData("munrotabtest.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCsvDataAreImported_whenAskFor2Records_thenReturnRecords() {
        importCsv();
        List<MunroRecord> results = csvServiceImpl.limitMunroRecords(csvServiceImpl.getMunroRecords().stream(),2).collect(Collectors.toList());

        assertThat(results.size()).isEqualTo(2);
    }

    @Test
    public void givenCsvDataAreImported_whenMinHeight960_thenReturnRecords() {
        importCsv();
        List<MunroRecord> results = csvServiceImpl.minHeightMunroRecords(csvServiceImpl.getMunroRecords().stream(),960).collect(Collectors.toList());

        assertThat(results.size()).isEqualTo(395);
        results.forEach(( result -> assertThat(result.getHeightM()).isGreaterThanOrEqualTo(960)));
    }

    @Test
    public void givenCsvDataAreImported_whenMaxHeight960_thenReturnRecords() {
        importCsv();
        List<MunroRecord> results = csvServiceImpl.maxHeightMunroRecords(csvServiceImpl.getMunroRecords().stream(),960).collect(Collectors.toList());

        assertThat(results.size()).isEqualTo(220);
        results.forEach(( result -> assertThat(result.getHeightM()).isLessThanOrEqualTo(960)));
    }

    @Test
    public void givenCsvDataAreImported_whenSearchFor_thenReturnJustSearchRecords() {
        importCsv();
        List<MunroRecord> results = csvServiceImpl.searchMunroRecords(csvServiceImpl.getMunroRecords().stream(), "Munro").collect(Collectors.toList());

        results.forEach(( result -> assertThat(result.getYearPost1997()).isEqualTo("MUN")));
        int mungoSize = results.size();
        results = csvServiceImpl.searchMunroRecords(csvServiceImpl.getMunroRecords().stream(), "Munro Top").collect(Collectors.toList());
        results.forEach(( result -> assertThat(result.getYearPost1997()).isEqualTo("TOP")));
        int topSize = results.size();
        results = csvServiceImpl.searchMunroRecords(csvServiceImpl.getMunroRecords().stream(), "either").collect(Collectors.toList());
        int eitherSize = results.size();
        assertThat(mungoSize + topSize).isEqualTo(eitherSize);
    }

    @Test
    public void givenCsvDataAreImported_whenNoSort_returnNoSortRecords() {
        importCsv();
        String[] sortArray = {};
        List<MunroRecord> results = csvServiceImpl.sortMunroRecords(csvServiceImpl.getMunroRecords().stream(),sortArray).collect(Collectors.toList());

        int size = results.size();
        assertThat(size).isEqualTo(csvServiceImpl.getMunroRecords().size());
        assertThat(results).isEqualTo(csvServiceImpl.getMunroRecords());
    }

    @Test
    public void givenCsvDataAreImported_whenSortByName_returnRecordsSortedByName() {
        importCsv();
        String[] sortArray = {"name"};

        List<MunroRecord> results = csvServiceImpl.sortMunroRecords(csvServiceImpl.getMunroRecords().stream(),sortArray).collect(Collectors.toList());

        int size = results.size();
        assertThat(size).isEqualTo(csvServiceImpl.getMunroRecords().size());
        assertThat(results).isNotEqualTo(csvServiceImpl.getMunroRecords());
        String lastName = results.get(0).getName();
        String curName = "";
        for (int i = 1; i < size ; i++){
            curName = results.get(i).getName();
            assertThat(lastName).isLessThanOrEqualTo(curName);
            lastName = curName;
        }
    }

    @Test
    public void givenCsvDataAreImported_whenSortByNameDesc_returnRecordsSortedByNameDesc() {
        importCsv();
        String[] sortArray = {"name desc"};

        List<MunroRecord> results = csvServiceImpl.sortMunroRecords(csvServiceImpl.getMunroRecords().stream(),sortArray).collect(Collectors.toList());

        int size = results.size();
        assertThat(size).isEqualTo(csvServiceImpl.getMunroRecords().size());
        assertThat(results).isNotEqualTo(csvServiceImpl.getMunroRecords());
        String lastName = results.get(0).getName();
        String curName = "";
        for (int i = 1; i < size ; i++){
            curName = results.get(i).getName();
            assertThat(lastName).isGreaterThanOrEqualTo(curName);
            lastName = curName;
        }
    }

    @Test
    public void givenCsvDataAreImported_whenSortByHeight_returnRecordsSortedByHeightMeters() {
        importCsv();
        String[] sortArray = {"height"};

        List<MunroRecord> results = csvServiceImpl.sortMunroRecords(csvServiceImpl.getMunroRecords().stream(),sortArray).collect(Collectors.toList());

        int size = results.size();
        assertThat(size).isEqualTo(csvServiceImpl.getMunroRecords().size());
        assertThat(results).isNotEqualTo(csvServiceImpl.getMunroRecords());
        double lastHeight = results.get(0).getHeightM();
        double curHeight = 0;
        for (int i = 1; i < size ; i++){
            curHeight = results.get(i).getHeightM();
            assertThat(lastHeight).isLessThanOrEqualTo(curHeight);
            lastHeight = curHeight;
        }
    }

    @Test
    public void givenCsvDataAreImported_whenSortByHeightDesc_returnRecordsSortedByHeightMetersDesc() {
        importCsv();
        String[] sortArray = {"height desc"};

        List<MunroRecord> results = csvServiceImpl.sortMunroRecords(csvServiceImpl.getMunroRecords().stream(),sortArray).collect(Collectors.toList());

        int size = results.size();
        assertThat(size).isEqualTo(csvServiceImpl.getMunroRecords().size());
        assertThat(results).isNotEqualTo(csvServiceImpl.getMunroRecords());
        double lastHeight = results.get(0).getHeightM();
        double curHeight = 0;
        for (int i = 1; i < size ; i++){
            curHeight = results.get(i).getHeightM();
            assertThat(lastHeight).isGreaterThanOrEqualTo(curHeight);
            lastHeight = curHeight;
        }
    }

    @Test
    public void givenCsvDataAreImported_whenSortByHeightAndName_returnRecordsSortedByHeightMetersAndName() {
        importCsv();
        String[] sortArray = {"height","name"};

        List<MunroRecord> results = csvServiceImpl.sortMunroRecords(csvServiceImpl.getMunroRecords().stream(),sortArray).collect(Collectors.toList());

        int size = results.size();
        assertThat(size).isEqualTo(csvServiceImpl.getMunroRecords().size());
        assertThat(results).isNotEqualTo(csvServiceImpl.getMunroRecords());
        double lastHeight = results.get(0).getHeightM();
        String lastName = results.get(0).getName();
        double curHeight = 0;
        String curName = "";
        for (int i = 1; i < size ; i++){
            curHeight = results.get(i).getHeightM();
            curName = results.get(i).getName();
            assertThat(lastHeight).isLessThanOrEqualTo(curHeight);
            if (lastHeight == curHeight) assertThat(lastName).isLessThanOrEqualTo(curName);
            lastHeight = curHeight;
            lastName = curName;
        }
    }

    @Test
    public void givenCsvDataAreImported_whenSortByNameAndHeight_returnRecordsSortedByNameAndHeightMeters() {
        importCsv();
        String[] sortArray = {"name","height"};

        List<MunroRecord> results = csvServiceImpl.sortMunroRecords(csvServiceImpl.getMunroRecords().stream(),sortArray).collect(Collectors.toList());

        int size = results.size();
        assertThat(size).isEqualTo(csvServiceImpl.getMunroRecords().size());
        assertThat(results).isNotEqualTo(csvServiceImpl.getMunroRecords());
        double lastHeight = results.get(0).getHeightM();
        String lastName = results.get(0).getName();
        double curHeight = 0;
        String curName = "";
        for (int i = 1; i < size ; i++){
            curHeight = results.get(i).getHeightM();
            curName = results.get(i).getName();
            assertThat(lastName).isLessThanOrEqualTo(curName);
            if (lastName == curName) assertThat(lastHeight).isLessThanOrEqualTo(curHeight);
            lastHeight = curHeight;
            lastName = curName;
        }
    }

    @Test
    public void givenCsvDataAreImported_whenSortByNameDescAndHeight_returnRecordsSortedByNameDescAndHeightMeters() {
        importCsv();
        String[] sortArray = {"name desc","height"};

        List<MunroRecord> results = csvServiceImpl.sortMunroRecords(csvServiceImpl.getMunroRecords().stream(),sortArray).collect(Collectors.toList());

        int size = results.size();
        assertThat(size).isEqualTo(csvServiceImpl.getMunroRecords().size());
        assertThat(results).isNotEqualTo(csvServiceImpl.getMunroRecords());
        double lastHeight = results.get(0).getHeightM();
        String lastName = results.get(0).getName();
        double curHeight = 0;
        String curName = "";
        for (int i = 1; i < size ; i++){
            curHeight = results.get(i).getHeightM();
            curName = results.get(i).getName();
            assertThat(lastName).isGreaterThanOrEqualTo(curName);
            if (lastName == curName) assertThat(lastHeight).isLessThanOrEqualTo(curHeight);
            lastHeight = curHeight;
            lastName = curName;
        }
    }

    @Test
    public void givenCsvDataAreImported_whenSortByNameAndHeightDesc_returnRecordsSortedByNameAndHeightMetersDesc() {
        importCsv();
        String[] sortArray = {"name desc","height"};

        List<MunroRecord> results = csvServiceImpl.sortMunroRecords(csvServiceImpl.getMunroRecords().stream(),sortArray).collect(Collectors.toList());

        int size = results.size();
        assertThat(size).isEqualTo(csvServiceImpl.getMunroRecords().size());
        assertThat(results).isNotEqualTo(csvServiceImpl.getMunroRecords());
        double lastHeight = results.get(0).getHeightM();
        String lastName = results.get(0).getName();
        double curHeight = 0;
        String curName = "";
        for (int i = 1; i < size ; i++){
            curHeight = results.get(i).getHeightM();
            curName = results.get(i).getName();
            assertThat(lastName).isGreaterThanOrEqualTo(curName);
            if (lastName == curName) assertThat(lastHeight).isLessThanOrEqualTo(curHeight);
            lastHeight = curHeight;
            lastName = curName;
        }
    }
}
