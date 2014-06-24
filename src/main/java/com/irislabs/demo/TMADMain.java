package com.irislabs.demo;

import com.google.common.collect.Multimap;
import com.irislabs.sheet.FileSheet;
import com.irislabs.sheet.SheetEntry;
import com.irislabs.sheet.SheetWriter;
import com.irislabs.stream.MultiCollectors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: spartango
 * Date: 5/2/14
 * Time: 4:58 PM.
 */
public class TMADMain {

    public static void main(String[] args) throws IOException {
        FileSheet clinical = new FileSheet("clinical.txt");
        FileSheet images = new FileSheet("images.txt");

        List<String> headers = new ArrayList<>(clinical.fields());
        headers.add("image");
        SheetWriter writer = new SheetWriter("tmadClinical.tsv", headers);

        Multimap<String, SheetEntry> index = images.stream()
                                                   .filter(entry -> entry.get("image").contains(".he/"))
                                                   .collect(MultiCollectors.toMultimap(SheetEntry::getPrimary));

        clinical.stream()
               .flatMap(entry -> index.get(entry.getPrimary())
                                      .stream()
                                      .map(imageEntry -> {
                                          SheetEntry newEntry = new SheetEntry(entry);
                                          String file = imageEntry.get("image").split("/")[1].split("\\.")[0];
                                          newEntry.put("image", file);
                                          return newEntry;
                                      }))
               .forEach(writer);
        writer.close();
    }
}
