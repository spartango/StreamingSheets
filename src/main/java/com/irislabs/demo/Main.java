package com.irislabs.demo;

import com.irislabs.sheet.FileSheet;
import com.irislabs.sheet.SheetWriter;

import java.io.IOException;

/**
 * Author: spartango
 * Date: 4/15/14
 * Time: 12:03 AM.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        FileSheet sheet = new FileSheet("test.tsv");

        SheetWriter writer = new SheetWriter(System.currentTimeMillis() + ".tsv");

        System.out.println("Headers: " + sheet.fields());
        sheet.stream()
             .filter(entry -> entry.get("provider_type").equals("Pathology"))
             .forEach(writer);
        System.out.println("Complete!");
    }
}
