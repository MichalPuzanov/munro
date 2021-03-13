package com.michalpuzanov.munro.service;

import com.michalpuzanov.munro.domain.MunroRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class CsvServiceImpl implements CsvService{

    private List<MunroRecord> munroRecords;

    public List<MunroRecord> getMunroRecords() {
        return munroRecords;
    }

    public void setMunroRecords(List<MunroRecord> munroRecords) {
        this.munroRecords = munroRecords;
    }

    @Override
    public List<MunroRecord> getMunroRecords(long limit, String search, long minHeight, long maxHeight, String[] sort) {

        Stream<MunroRecord> result = minHeightMunroRecords(getMunroRecords().stream(),minHeight);
        result = maxHeightMunroRecords(result,maxHeight);
        result = searchMunroRecords(result,search);
        result = sortMunroRecords(result,sort);
        result = limitMunroRecords(result,limit);

        return result.collect(Collectors.toList());
    }

    public Stream<MunroRecord> limitMunroRecords(Stream<MunroRecord> currentRecords, long size) {
        if (size == 0){
            return currentRecords;
        }
        return currentRecords.limit(size);
    }

    public Stream<MunroRecord> searchMunroRecords(Stream<MunroRecord> currentRecords, String search){
        if ( search.equals("either")) {
            return currentRecords.filter( (rec) -> (rec.getYearPost1997().equals("TOP") || rec.getYearPost1997().equals("MUN") ) );
        }
        if ( search.equals("Munro")) {
            return currentRecords.filter( (rec) -> rec.getYearPost1997().equals("MUN"));
        }
        if ( search.equals("Munro Top")) {
            return currentRecords.filter( (rec) -> rec.getYearPost1997().equals("TOP"));
        }
        return null;
    }

    public Stream<MunroRecord> minHeightMunroRecords(Stream<MunroRecord> currentRecords, long minHeight){
        if (minHeight == 0){
            return currentRecords;
        }
        return currentRecords.filter( (rec) -> rec.getHeightM() >= minHeight);
    }

    public Stream<MunroRecord> maxHeightMunroRecords(Stream<MunroRecord> currentRecords, long maxHeight){
        if (maxHeight == 0){
            return currentRecords;
        }
        return currentRecords.filter( (rec) -> rec.getHeightM() <= maxHeight);
    }

    public Stream<MunroRecord> sortMunroRecords(Stream<MunroRecord> currentRecords, String[] sort){
        Stream<MunroRecord> result = null;

        Comparator<MunroRecord> heightComparator = Comparator.comparingDouble(MunroRecord::getHeightM);
        Comparator<MunroRecord> nameComparator = Comparator.comparing(MunroRecord::getName);

        if (sort.length == 0){
            result = currentRecords;
        }

        if (sort.length == 1){

            boolean reverse = false;

            if (sort[0].contains("desc")){
                reverse = true;
            }

            if (sort[0].contains("height")) {
              if (reverse) {
                  result = currentRecords.sorted(heightComparator.reversed());
              } else {
                  result = currentRecords.sorted(heightComparator);
              }
            } else {
                if (reverse) {
                    result = currentRecords.sorted(nameComparator.reversed());
                } else {
                    result = currentRecords.sorted(nameComparator);
                }
            }
        }

        if (sort.length == 2) {
            boolean[] reverse = {false,false};
            Comparator<MunroRecord> comparator = null;

            if (sort[0].contains("desc")){
                reverse[0] = true;
            }
            if (sort[1].contains("desc")){
                reverse[1] = true;
            }

            if (sort[0].contains("height")) {

                if (reverse[0]) {
                   comparator =  heightComparator.reversed();
                } else {
                    comparator =  heightComparator;
                }
                if (reverse[1]) {
                    comparator =  comparator.thenComparing(nameComparator).reversed();
                } else {
                    comparator =  comparator.thenComparing(nameComparator);
                }

            } else {

                if (reverse[0]) {
                    comparator = nameComparator.reversed();
                } else {
                    comparator = nameComparator;
                }
                if (reverse[1]) {
                    comparator =  comparator.thenComparing(heightComparator).reversed();
                } else {
                    comparator =  comparator.thenComparing(heightComparator);
                }
            }
           result = currentRecords.sorted(comparator);
        }
        return result;
    }

    @Override
    public boolean importCsvData(String resourceName) throws IOException {
        log.debug("Method started");
        boolean result = false;
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        Reader in = new FileReader(file);
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        munroRecords = new ArrayList<>();
        for (CSVRecord record : records) {
            MunroRecord munroRecord = new MunroRecord();
            munroRecord.setRunningNo(record.get("Running No"));
            munroRecord.setDoBihNumber(record.get("DoBIH Number"));
            munroRecord.setStreetmap(record.get("Streetmap"));
            munroRecord.setGeograph(record.get("Geograph"));
            munroRecord.setHillBagging(record.get("Hill-bagging"));
            munroRecord.setName(record.get("Name"));
            munroRecord.setSmcSection(record.get("SMC Section"));
            munroRecord.setRhbSection(record.get("RHB Section"));
            munroRecord.set_Section(record.get("_Section"));
            munroRecord.setHeightM(record.get("Height (m)").isEmpty() ? 0d : Double.parseDouble(record.get("Height (m)")));
            munroRecord.setHeightFt(record.get("Height (ft)"));
            munroRecord.setMap1_50(record.get("Map 1:50"));
            munroRecord.setMap1_25(record.get("Map 1:25"));
            munroRecord.setGridRef(record.get("Grid Ref"));
            munroRecord.setGridRefXY(record.get("GridRefXY"));
            munroRecord.setXCoord(record.get("xcoord"));
            munroRecord.setYCoord(record.get("ycoord"));
            munroRecord.setYear1891(record.get("1891"));
            munroRecord.setYear1921(record.get("1921"));
            munroRecord.setYear1933(record.get("1933"));
            munroRecord.setYear1953(record.get("1953"));
            munroRecord.setYear1969(record.get("1969"));
            munroRecord.setYear1974(record.get("1974"));
            munroRecord.setYear1981(record.get("1981"));
            munroRecord.setYear1984(record.get("1984"));
            munroRecord.setYear1990(record.get("1990"));
            munroRecord.setYear1997(record.get("1997"));
            munroRecord.setYearPost1997(record.get("Post 1997"));
            munroRecord.setComments(record.get("Comments"));
            munroRecords.add(munroRecord);
        }
        result = true;
        log.debug("Imported " + munroRecords.size() + " records");
        log.debug("Method finished");
        return result;
    }
}
