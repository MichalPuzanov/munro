package com.michalpuzanov.munro;

import com.michalpuzanov.munro.service.CsvService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MunroApplication implements InitializingBean {

    @Autowired
    private CsvService csvService;

    public static void main(String[] args) {
        SpringApplication.run(MunroApplication.class, args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        csvService.importCsvData("munrotab_v6.2.csv");
    }
}
