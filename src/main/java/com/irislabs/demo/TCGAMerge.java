package com.irislabs.demo;

import com.irislabs.sheet.FileSheet;
import com.irislabs.sheet.SheetEntry;
import com.irislabs.sheet.SheetMerger;
import com.irislabs.sheet.SheetWriter;
import com.irislabs.stream.Numeric;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: spartango
 * Date: 6/14/14
 * Time: 11:03.
 */
public class TCGAMerge {
    private static final Path   PATH   = FileSystems.getDefault()
                                                    .getPath("/Users/spartango/Dropbox/Iris/TCGA Clinical/");
        private static final String OUTPUT = "/Users/spartango/Dropbox/Iris/TCGA Clinical/";
//    private static final String OUTPUT = "/Users/spartango/Downloads/";

    public static void main(String[] args) throws IOException {
        final Map<String, List<Path>> filesByCancer = Files.list(PATH)
                                                           .filter(path -> path.toString().contains("nationwide"))
                                                           .collect(Collectors.groupingBy(path -> {
                                                               String[] parts = path.toString().split("_");
                                                               return parts[parts.length - 1].split("\\.")[0];
                                                           }));

        filesByCancer.forEach((cancer, paths) -> {
            SheetMerger merger = new SheetMerger();
            paths.stream()
                 .map(Path::toString)
                 .sorted()
                 .peek(System.out::println)
                 .map(path -> {
                     try {
                         return new FileSheet(path);
                     } catch (Exception e) {
                         System.out.println("Error loading " + cancer + " " + path + ": " + e);
                         return null;
                     }
                 }).filter(sheet -> sheet != null)
                 .forEach(sheet -> {
                     try {
                         merger.append(sheet);
                     } catch (Exception e) {
                         System.out.println("Error appending " + cancer + ": " + e);
                     }
                 });

            final Collection<SheetEntry> merged = merger.getMerged();

            try {
                SheetWriter writer = new SheetWriter(OUTPUT + cancer + "_merged.txt", merger.getFields());
                merged.forEach(writer);
            } catch (Exception e) {
                System.out.println("Error writing " + cancer + ": " + e);
            }

            try {
                SheetWriter numeric = new SheetWriter(OUTPUT + cancer + "_numeric.txt", merger.getFields());
                merged.stream().map(Numeric.numerify(merger.getFields())).forEach(numeric);
            } catch (Exception e) {
                System.out.println("Error numeric " + cancer + ": " + e);
            }
        });

    }
}
