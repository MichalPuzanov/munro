package com.michalpuzanov.munro.controller;

import com.michalpuzanov.munro.service.CsvService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MunroRecordController {

    private final CsvService csvService;

    public MunroRecordController(CsvService csvService) {
        this.csvService = csvService;
    }

    @GetMapping("/records")
    @ApiOperation("Returns list of all Records in the system based on parameters.")
    public ResponseEntity<?> findMunroRecords(
                                    @ApiParam("Limit of records")
                                    @RequestParam(required = false,defaultValue = "0") long limit,
                                    @ApiParam(allowableValues = "Munro,Top,either", defaultValue = "either")
                                    @RequestParam(required = false,defaultValue = "either") String search,
                                    @ApiParam("Minimum height in meters")
                                    @RequestParam(required = false,defaultValue = "0") double minHeight,
                                    @ApiParam("Maximum height in meters")
                                    @RequestParam(required = false,defaultValue = "0") double maxHeight,
                                    @ApiParam(value = "Sort by name/height/combination of both, default is ASC, use desc keyword for DESC",
                                            allowableValues = "name,name-desc,height,height-desc",
                                            allowMultiple = true,
                                            example = "|name|name-desc|name,height|name,height-desc|")
                                    @RequestParam(required = false,defaultValue = "") String[] sort) {

        if ( minHeight > maxHeight) {
            return new ResponseEntity<>("Error: parameter minHeight is bigger than parameter maxHeight", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(csvService.getMunroRecords(limit,search,minHeight,maxHeight,sort), HttpStatus.OK);
    }


}
