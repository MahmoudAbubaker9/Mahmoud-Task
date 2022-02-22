package com.progressoft.tools;

import jakarta.enterprise.util.AnnotationLiteral;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainNormalizer implements Normalizer {

    String csvPath;
    String destPath;

    @Override
    public ScoringSummary zscore(Path csvPath, Path destPath, String colToStandardize) {
        int[] data = checkData(csvPath, destPath, colToStandardize);
        MainScoringSummary summaryResult = new MainScoringSummary(data);
        summaryResult.mainCalculation();
        BigDecimal[] standardized = new BigDecimal[data.length];
        for(int i = 0; i < data.length; i++) {
            BigDecimal number = new BigDecimal(data[i]);
            standardized[i] = number.subtract(summaryResult.mean()).divide(summaryResult.standardDeviation(), RoundingMode.HALF_EVEN).setScale(2, RoundingMode.HALF_EVEN);
        }
        CsvFile.write(standardized, colToStandardize, "_z");
        return summaryResult;
    }

    @Override
    public ScoringSummary minMaxScaling(Path csvPath, Path destPath, String colToNormalize) {

        int[] data = checkData(csvPath, destPath, colToNormalize);
        MainScoringSummary summaryResult = new MainScoringSummary(data);
        BigDecimal[] normalized = new BigDecimal[data.length];
        for(int i = 0; i < data.length; i++) {
            BigDecimal number = new BigDecimal(data[i]);
            normalized[i] = number.subtract(summaryResult.min()).divide(summaryResult.max().subtract(summaryResult.min()), RoundingMode.HALF_EVEN);
        }
        CsvFile.write(normalized, colToNormalize, "_mm");
        return summaryResult;

    }


    private int[] checkData(Path csvPath, Path destPath, String colToNormalize) {
        if(destPath == null)
            destPath = Paths.get("no_exists");
        if(csvPath == null)
            csvPath = Paths.get("no_exists");
        if(colToNormalize == null)
            colToNormalize = "no_col";
        CsvFile CsvFile = new CsvFile(csvPath.toString(), destPath.toString());
        int []data = new int[0];
        try {
            data = CsvFile.read(colToNormalize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

}