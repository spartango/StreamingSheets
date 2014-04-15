package com.irislabs.sheet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
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

    public SheetWriter(String path, List<String> header) throws IOException {
        this(new File(path), "\t", header);
    }

    public SheetWriter(File target, String delimiter, List<String> header) throws IOException {
        this.target = target;
        this.delimiter = delimiter;

        writer = new FileWriter(target);
        writeHeader(header);
    }

    private void writeHeader(List<String> header) throws IOException {
        StringBuilder builder = new StringBuilder();
        header.forEach(value -> {
            builder.append(value);
            builder.append(delimiter);
        });

        // Close the line
        builder.append("\n");
        writer.write(builder.toString());
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
