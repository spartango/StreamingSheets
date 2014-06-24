package com.irislabs.demo;

import com.irislabs.sheet.FileSheet;
import com.irislabs.sheet.SheetEntry;
import com.irislabs.sheet.SheetWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: spartango
 * Date: 6/14/14
 * Time: 22:20.
 */
public class NumericMain {
    public static void main(String[] args) throws IOException {
        FileSheet patients = new FileSheet(
                "/Users/spartango/Dropbox/Iris/TCGA Clinical/nationwidechildrens.org_clinical_patient_gbm.txt");
        System.out.println(patients.fields());

        SheetWriter writer = new SheetWriter("gbm_numeric.txt", patients.fields());

        Map<String, AtomicInteger> counterMap = new HashMap<>();
        Map<String, Integer> numericMap = new TreeMap<>();
        patients.fields().forEach(field -> counterMap.put(field, new AtomicInteger(0)));

        patients.stream().map(patient -> {
            SheetEntry newEntry = new SheetEntry();
            patient.forEach((key, value) -> {
                if (StringUtils.isNumeric(value)) {
                    // Try to parse the value as is
                    newEntry.put(key, Double.parseDouble(value));
                } else {
                    // If this value hasn't been seen before
                    String kvPair = key + "_" + value;

                    if (!numericMap.containsKey(kvPair)) {
                        int newValue = counterMap.get(key).getAndIncrement();
                        numericMap.put(kvPair, newValue);
                    }

                    newEntry.put(key, numericMap.get(kvPair));
                }
            });
            return newEntry;
        }).forEach(writer);
    }
}
