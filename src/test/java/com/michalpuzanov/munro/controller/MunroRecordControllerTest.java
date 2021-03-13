package com.michalpuzanov.munro.controller;

import com.michalpuzanov.munro.service.CsvService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MunroRecordController.class)
public class MunroRecordControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CsvService csvService;

    @Test
    public void givenImportedCSV_whenMunroRecordsWithoutParams_thenUseDefaultParams() throws Exception {
        String[] emptyArray = {};

        when(csvService.getMunroRecords(0,"either",0,0, emptyArray)).thenReturn(new ArrayList<>());
        mvc.perform(get("/records")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void givenImportedCSV_whenMinHeightParamIsHigherThanMaxHeightParam_thenRespondErrorStatus400() throws Exception {
        String[] emptyArray = {};

        when(csvService.getMunroRecords(0,"either",100,0, emptyArray)).thenReturn(new ArrayList<>());
        mvc.perform(get("/records").param("minHeight","100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: parameter minHeight is bigger than parameter maxHeight"));
    }
}
