package com.irislabs.sheet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Author: spartango
 * Date: 4/14/14
 * Time: 11:44 PM.
 */
public class SheetWriter implements Consumer<SheetEntry> {
    private File   target;
    private String delimiter;

    private FileWriter writer;

    public SheetWriter(String path) throws IOException {
        this(new File(path), "\t");
    }

    public SheetWriter(File target, String delimiter) throws IOException {
        this.target = target;
        this.delimiter = delimiter;

        writer = new FileWriter(target);
    }

    public void write(SheetEntry str) throws IOException {
        StringBuilder builder = new StringBuilder();
        str.values().forEach(value -> {
            builder.append(value);
            builder.append(delimiter);
        });

        // Close the line
        builder.append("\n");
        writer.write(builder.toString());
    }

    @Override public void accept(SheetEntry sheetEntry) {
        try {
            write(sheetEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        writer.close();
    }
}
