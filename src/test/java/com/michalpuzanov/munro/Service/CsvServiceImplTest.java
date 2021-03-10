package com.michalpuzanov.munro.Service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

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
    private CsvService csvService;

    @Test
    public void given_ApplicationStart_whenExistCsv_thenImportDataToIterable() {
        boolean result = csvService.importCsvData();

        assertThat(result).isTrue();
    }
}
