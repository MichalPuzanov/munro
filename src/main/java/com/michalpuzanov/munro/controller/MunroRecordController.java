package com.michalpuzanov.munro.controller;

import com.michalpuzanov.munro.domain.MunroRecord;
import com.michalpuzanov.munro.service.CsvService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MunroRecordController {

    private final CsvService csvService;

    public MunroRecordController(CsvService csvService) {
        this.csvService = csvService;
    }

    @GetMapping("/records")
    public List<MunroRecord> findMunroRecords(@RequestParam(required = false,defaultValue = "0") long limit,
                                   @RequestParam(required = false,defaultValue = "either") String search,
                                   @RequestParam(required = false,defaultValue = "0") long minHeight,
                                   @RequestParam(required = false,defaultValue = "0") long maxHeight,
                                   @RequestParam(required = false,defaultValue = "") String[] sort) {

        return csvService.getMunroRecords(limit,search,minHeight,maxHeight,sort);
    }


}
