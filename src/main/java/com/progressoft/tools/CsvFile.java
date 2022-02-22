package com.progressoft.tools;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class CsvFile {

    public static String csvPath;
    public static String destPath;

    public CsvFile(String csvPath, String destPath) {
        if(destPath != null)
            this.destPath = destPath;
        if(csvPath != null)
            this.csvPath = csvPath;
    }

    /**
     *
     * @param  RequiredCol        finding the Required Colum in CSV file
     * @return Read the CSV file and Search for the required Colum, if it is not found,
     * it will be return column Not Exist
     * and If found, the value will be returned
     *
     */

    public static int[] read(String RequiredCol) throws IOException {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(csvPath);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("source file not found");
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        // finding the index the colToNormalize
        String line = bufferedReader.readLine();
        int index = -1;
        String []SplitWords = line.split(",");

        for(int i = 0; i < SplitWords.length; i++) {
            if(SplitWords[i].equals(RequiredCol)) {
                index = i;
                break;
            }
        }
        if(index == -1) {
            throw new IllegalArgumentException("column "+RequiredCol+" not found");
        }
        line = bufferedReader.readLine();
        ArrayList<String> tempCol = new ArrayList<String>();
        while(line != null) {
            SplitWords = line.split(",");
            tempCol.add(SplitWords[index]);
            line = bufferedReader.readLine();
        }
        int []Colum = tempCol.stream().mapToInt(Integer::parseInt).toArray();
        bufferedReader.close();
        fileReader.close();
        return Colum;
    }

    /**
     *
     * @param  RequiredCol        finding the Required Colum in CSV file
     * @return Write to a CSV file
     */

    public static void write(BigDecimal[] data, String RequiredCol, String add) {
        try {

            FileReader fileReader = null;
            try {
                fileReader = new FileReader(csvPath);
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException();
            }
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();
            String [] SplitWords = line.split(",");
            String []newTokens = new String[SplitWords.length + 1];
            boolean found = false;

            // find the target
            int index = -1;
            for(int i = 0; i < SplitWords.length; i++) {
                newTokens[i + (index != -1 ? 1 : 0)] = SplitWords[i];
                if(SplitWords[i].equals(RequiredCol)) {
                    index = i;
                    newTokens[i + 1] = SplitWords[i] + add;
                }
            }
            String newLines = String.join(",", newTokens) + "\n";
            line = bufferedReader.readLine();

            int dataIndex = 0;
            while(line != null) {
                SplitWords = line.split(",");
                boolean foundInValues = false;
                for(int i = 0; i < SplitWords.length; i++) {
                    newTokens[i + (foundInValues ? 1 : 0)] = SplitWords[i];
                    if(i == index) {
                        foundInValues = true;
                        newTokens[i + 1] = data[dataIndex++].setScale(2, RoundingMode.HALF_EVEN).toString();
                    }
                }
                newLines = newLines.concat(String.join(",", newTokens) + "\n");
                line = bufferedReader.readLine();
            }
            FileWriter writer = new FileWriter(destPath);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(newLines);
            bufferWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
