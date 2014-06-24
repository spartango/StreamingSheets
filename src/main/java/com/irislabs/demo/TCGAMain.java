package com.irislabs.demo;

import com.irislabs.sheet.FileSheet;
import com.irislabs.sheet.SheetEntry;
import com.irislabs.sheet.SheetWriter;
import com.irislabs.stream.Filters;
import com.irislabs.stream.MultiCollectors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Author: spartango
 * Date: 6/14/14
 * Time: 22:20.
 */
public class TCGAMain {
    public static void main(String[] args) throws IOException {
        FileSheet patients = new FileSheet("dcis/Run_04_10_19_43.txt");
        System.out.println(patients.fields());

        SheetWriter writer = new SheetWriter("dcis/numeric.txt", patients.fields());

        Map<String, AtomicInteger> counterMap = new HashMap<>();
        Map<String, Integer> numericMap = new TreeMap<>();
        patients.stream().skip(1).map(patient -> {
            SheetEntry numeric = new SheetEntry();
            patient.entrySet().stream().forEach(entry -> {
                String value = entry.getValue();
                String key = entry.getKey();
                try {
                    numeric.put(key, Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    if (!counterMap.containsKey(key)) {
                        counterMap.put(key, new AtomicInteger(0));
                    }

                    if (!numericMap.containsKey(value)) {
                        numericMap.put(value, counterMap.get(key).getAndIncrement());
                    }

                    numeric.put(key, numericMap.get(value));
                }
            });
            return numeric;
        }).forEach(writer);

        System.out.println(patients.stream()
                                   .filter(Filters.containsKeys("radiation_treatment_adjuvant",
                                                                "tumor_status",
                                                                "her2_ihc_score"))
                                   .collect(MultiCollectors.groupingByKey("tumor_status",
                                                                          MultiCollectors.groupingByKey(
                                                                                  "her2_ihc_score",
                                                                                  Collectors.counting()))));

    }
}
