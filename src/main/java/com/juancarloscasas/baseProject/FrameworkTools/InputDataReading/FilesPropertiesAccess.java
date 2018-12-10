package com.juancarloscasas.baseProject.FrameworkTools.InputDataReading;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.junit.Assert;

public class FilesPropertiesAccess {
	private static HashMap<String,Properties> __filesProperties;
	public FilesPropertiesAccess()
	{
		__filesProperties = new HashMap<String, Properties>();
	}
	
	public static String getPropertyValue(String propertyKey, String propertiesFile)
	{
		Properties fileProperties = getPropertiesFileData(propertiesFile);
		
		String propertyValue = fileProperties.getProperty(propertyKey);
		if (propertyValue == null){
			Assert.fail("Property key '" + propertyKey + "' not found in the propertiesFile '" + propertiesFile + "'.");
			return null;
		} else {
			return propertyValue;
		}
	}
	
	public static void setPropertyValue(String propertyKey, String propertyValue, String propertiesFile)
	{
		Properties ficheroPropiedades = getPropertiesFileData(propertiesFile);
		
		ficheroPropiedades.setProperty(propertyKey, propertyValue);
	}
	
	private static Properties getPropertiesFileData(String propertiesFileName)
	{
		Properties propertiesFile;
		if (__filesProperties == null)
		{
			__filesProperties = new HashMap<String,Properties>();
		}

		if (!__filesProperties.containsKey("propertiesFile"))
		{
			InputStream propertiesData = null;
			try {
				propertiesFile = new Properties();
				
				try {
					propertiesData = new FileInputStream(propertiesFileName);
				} catch (FileNotFoundException e) {
					Assert.fail("Properties file '" + propertiesFileName + "' not found.");
			}
				
				try {
					propertiesFile.load(propertiesData);
				} catch (IOException e) {
					Assert.fail("Availability problem in accesing properties file '" + propertiesFileName + "'.");
				}
				
			} finally {
				try {
					propertiesData.close();
				} catch (IOException e) {
					Assert.fail("Availability problem in accesing properties file '" + propertiesFileName + "'.");
				}
			}
			
			__filesProperties.put(propertiesFileName, propertiesFile);
		}
		else
		{
			propertiesFile = __filesProperties.get(propertiesFileName);
		}
		
		return propertiesFile;
	}
}
