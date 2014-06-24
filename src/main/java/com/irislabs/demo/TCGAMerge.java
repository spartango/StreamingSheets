package com.irislabs.demo;

import com.irislabs.sheet.FileSheet;
import com.irislabs.sheet.SheetMerger;
import com.irislabs.sheet.SheetWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Author: spartango
 * Date: 6/14/14
 * Time: 11:03.
 */
public class TCGAMerge {
    public static void main(String[] args) throws IOException {
        FileSheet patient = new FileSheet("prad/patient.txt");
        FileSheet radiation = new FileSheet("prad/radiation.txt");
        FileSheet drug = new FileSheet("prad/drug.txt");
        FileSheet omf = new FileSheet("prad/omf.txt");
        FileSheet cqcf = new FileSheet("prad/cqcf.txt");
        FileSheet nte = new FileSheet("prad/nte.txt");
        FileSheet followup_15 = new FileSheet("prad/followup_10.txt");

        List<FileSheet> sheets = Arrays.asList(patient,
                                               radiation,
                                               drug,
                                               cqcf,
                                               omf,
                                               nte,
                                               followup_15);

        SheetMerger merger = new SheetMerger();
        sheets.forEach(merger::append);

        SheetWriter writer = new SheetWriter("prad/merged.txt", merger.getFields());
        merger.getMerged().forEach(writer);
    }
}
