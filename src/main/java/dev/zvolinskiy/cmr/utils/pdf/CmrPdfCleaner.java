package dev.zvolinskiy.cmr.utils.pdf;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class CmrPdfCleaner {
    private CmrPdfCleaner() {
    }

    public static void cleanUpPdf() {
        File folder = new File(System.getProperty("user.dir"));
        Arrays.stream(Objects.requireNonNull(folder.listFiles())).filter(f -> f.getName().endsWith(".pdf")).forEach(File::delete);
    }
}
