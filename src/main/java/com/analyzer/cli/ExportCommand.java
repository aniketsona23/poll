package com.analyzer.cli;

import com.analyzer.core.Index;
import com.analyzer.model.ClassInfo;
import com.containers.GenericList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class ExportCommand implements Command {
    @Override
    public void execute(String[] args, Index index) {
        String format = "json";
        String outFile = "report.json";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--format") && i + 1 < args.length) {
                format = args[i + 1];
            } else if (args[i].equals("--out") && i + 1 < args.length) {
                outFile = args[i + 1];
            }
        }

        if (format.equalsIgnoreCase("json")) {
            exportJson(index, outFile);
        } else if (format.equalsIgnoreCase("csv")) {
            exportCsv(index, outFile);
        } else {
            System.err.println("Unsupported format: " + format);
        }
    }

    private void exportJson(Index index, String outFile) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(outFile)) {
            gson.toJson(index.getClasses(), writer);
            System.out.println("Exported JSON to " + outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportCsv(Index index, String outFile) {
        try (FileWriter writer = new FileWriter(outFile)) {
            writer.write("Class,Package,File,Methods,Fields\n");
            GenericList<ClassInfo> classes = index.getClasses();
            for (int i = 0; i < classes.size(); i++) {
                ClassInfo c = classes.get(i);
                writer.write(String.format("%s,%s,%s,%d,%d\n",
                        c.getName(), c.getPackageName(), c.getFilePath(), c.getMethodCount(), c.getFieldCount()));
            }
            System.out.println("Exported CSV to " + outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
