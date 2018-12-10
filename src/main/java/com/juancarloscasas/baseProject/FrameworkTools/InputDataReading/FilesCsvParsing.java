package com.juancarloscasas.baseProject.FrameworkTools.InputDataReading;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Assert;

public class FilesCsvParsing {
	public static ArrayList<String[]> processCsv(String csvPath) {
		String line = "";
		String separator = ";";
		ArrayList<String[]> csvContent = new ArrayList<String[]>(150);
		
		try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {

            while ((line = br.readLine()) != null) {

                // use punto y coma as separator
                csvContent.add(line.split(separator));
            }

        } catch (IOException e) {
            e.printStackTrace();
            
            Assert.fail("Csv not found or error on processing");
        }
		
		return csvContent;
	}
	
}
