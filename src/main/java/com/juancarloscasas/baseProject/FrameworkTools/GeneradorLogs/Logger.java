package com.juancarloscasas.baseProject.FrameworkTools.GeneradorLogs;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.io.FileUtils;

import com.juancarloscasas.baseProject.FrameworkTools.InputDataReading.FilesPropertiesAccess;

/**
 *
 * This class is oriented towards the creation of a log file that can be created for debugging purposes. It stores
 * the traces in memory until the save method is called, moment in which the log file is created and closed so operations
 * are faster
 * 
 */
public final class Logger {

	/**
	 * Un informe esta compuesto principalmente de un Lista de Strings que llamaremos trazas (como un log),
	 * iremos utilizando estas trazas para generar un fichero de texto para los test unitarios y para los 
	 */
	private static Queue<String> __traces;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd-HHmm");
	static Calendar calendar = Calendar.getInstance();
	static Date date = calendar.getTime();
	
	// constructor
	protected static void init(String descriptorGrupoPruebas) {
		write("-- Trazas realizadas para el Grupo de Pruebas: " + descriptorGrupoPruebas);
	}
	
	public static Queue<String> getTraces() {
		if (__traces == null) {
			__traces = new LinkedList<String>();
			__traces.add("********************************************************************");
			__traces.add("** Test Log Record: Date of execution: "+ sdf.format(date) + ". **");
			__traces.add("*******************************************************************");
		}
		return __traces;
	};

	/**
	 * Writes a new line to the Log record for future saving
	 * @param traza: Line that will be added to the log record for that session. (STRING)
	 */
	public static void write(String traceLine) {
		getTraces().add(traceLine);
	}
	
	public static void raiseTestFailure(String failureIndicator) {
		write("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		write("!! FAILURE OCCURRED IN  TEST EXECUTION !!");
		write("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		write(failureIndicator);
		write("##################");
		write("##################");
		write("");
	}
	
	public static void raiseError(String errorTraceLine) {
		write("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		write("!! INTERNAL ERROR TOOK PLACE IN TEST EXECUTION !!");
		write("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		write(errorTraceLine);
		write("####################");
		write("");
	}

	public static void raiseMinor(String errorTraceLine) {
		write("!! MINOR ERROR TOOK PLACE IN TEST EXECUTION;" +
				errorTraceLine);
		write("");
	}

	public static void logSuccess(String errorTraceLine) {
		write("-- Satisfactory action ;" +
				errorTraceLine);
		write("");
	}

	/**
	 * Function that stores all log traces in a file.
	 */
	public static void save() {
		// Ruta de logs y fichero temporal
		File carpeta = new File(FilesPropertiesAccess.getPropertyValue("logKeepingFolder", "driverParameters.properties"));
		carpeta.mkdirs();
		File scrFile = new File(carpeta.getAbsolutePath() + File.separator + "ExecLogs_" + sdf.format(date) + ".txt");

        while (! getTraces().isEmpty())
        	try	{
        		FileUtils.writeStringToFile(scrFile, getTraces().poll() + "\r\n", "UTF-8", true);
        	} catch (IOException e) { 
        		e.printStackTrace();
        	}
	}

}