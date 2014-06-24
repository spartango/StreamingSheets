package com.irislabs.demo;

import com.irislabs.sheet.FileSheet;
import com.irislabs.sheet.SheetWriter;
import com.irislabs.stream.Numeric;

import java.io.IOException;

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
        patients.stream().map(Numeric.numerify(patients.fields())).forEach(writer);
    }
}
