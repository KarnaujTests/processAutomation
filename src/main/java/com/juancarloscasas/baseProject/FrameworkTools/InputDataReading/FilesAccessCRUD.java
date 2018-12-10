package com.juancarloscasas.baseProject.FrameworkTools.InputDataReading;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import com.juancarloscasas.baseProject.FrameworkTools.Constants.FileFolderTypeConstants;
import com.juancarloscasas.baseProject.FrameworkTools.Constants.FileFolderTypeConstants.FolderType;
import com.juancarloscasas.baseProject.FrameworkTools.GeneradorLogs.Logger;
 
public class FilesAccessCRUD {
	private static HashMap<FolderType, File> __isBaseFolderAlreadyCreated = new HashMap<FolderType, File>();
	
	public static File getBaseFolder(FolderType folderIdentity) {
		if (! __isBaseFolderAlreadyCreated.containsKey(folderIdentity)) {
			File baseFolder = new File(folderIdentity.folderLocation);
			if (! baseFolder.exists()) {
				folderCreate(folderIdentity.folderLocation);
				
			} else if (FilesAccessCRUD.contarCarpetas(baseFolder) >= folderIdentity.maximunNumberOfFiles) {
				try {
					FilesAccessCRUD.delete(locateOldestFolder(baseFolder));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd-HHmm");
			File newBaseFolder = folderCreate(folderIdentity.folderLocation + File.separator + sdf.format(FileFolderTypeConstants.date));
			
			__isBaseFolderAlreadyCreated.put(folderIdentity, newBaseFolder);
		}
		
		return __isBaseFolderAlreadyCreated.get(folderIdentity);
	}
	
	
    public static Integer contarCarpetas(File sourceFile)
    {	
    	
//    	File file = new File(sourceFile.getAbsolutePath());
    	File[] files = sourceFile.listFiles(new FileFilter() { 
    	    public boolean accept(File f) {
    	        return f.isDirectory();
    	    }
    	});
 
    	return files.length;
    }
 
    public static File folderCreate(String path) {
		File carpeta = new File(path);
		carpeta.mkdirs();
		
		return carpeta;
    }
    
    public static File createFile(String fileNameWithExtension, File folder) {
		File file = new File(folder.getAbsolutePath() + File.separator + fileNameWithExtension);
    	
		return file;
    }
    
    private static int screenshotsCreated;
    public static File saveFile(FolderType tipoFichero, File fileToSave, String referenceName) {
    	try {
	    	switch (tipoFichero) {
			case LOGFILES:
		    	File file = new File(getBaseFolder(tipoFichero).getAbsolutePath() + File.separator + 
		    			referenceName + ".txt");

				return file;
				
			case SCREENSHOTS:
				File carpetaCapturas = new File(getBaseFolder(tipoFichero).getAbsolutePath() + File.separator + 
						referenceName);
				if (! carpetaCapturas.exists()) {
					carpetaCapturas.mkdir();
					screenshotsCreated = 0;
				}
				
				File ficheroCapturaFinal = new File(carpetaCapturas.getAbsolutePath() + File.separator + 
						referenceName + "_" + screenshotsCreated + ".png");
				screenshotsCreated = screenshotsCreated + 1;
				
				FileUtils.copyFile(fileToSave, ficheroCapturaFinal);

				return ficheroCapturaFinal;
				
	    	default:
				Logger.raiseMinor("File type not registered.");
				return null;
			}
		} catch (IOException e) {
			Logger.raiseMinor("An error occured on file saving on directory:" + tipoFichero.folderLocation + ".");
			return null;
	    	
		} catch (Exception e) {
			Logger.raiseMinor("An unknown error occured on file saving on directory:" + tipoFichero.folderLocation + ".");
			return null;
		}
    }

    public static File saveFile(FolderType tipoFichero, BufferedImage imageToSave, String referenceName) {
    	try {
	    	switch (tipoFichero) {
			case SCREENSHOTS:
//				String tiempoEjec = new SimpleDateFormat("yyyy-M-dd-HHmm-ss").format(Calendar.getInstance().getTime());

				File carpetaCapturas = new File(getBaseFolder(tipoFichero).getAbsolutePath() + File.separator + 
						referenceName);
				if (! carpetaCapturas.exists()) {
					carpetaCapturas.mkdir();
					screenshotsCreated = 0;
				}
				
				File ficheroCapturaFinal = new File(carpetaCapturas.getAbsolutePath() + File.separator + 
						referenceName + "_" + screenshotsCreated + ".png");
				screenshotsCreated = screenshotsCreated + 1;
				
				ImageIO.write(imageToSave, "png", ficheroCapturaFinal);

				return ficheroCapturaFinal;
				
	    	default:
				Logger.raiseMinor("File type not registered.");
				Assert.fail();
				return null;
			}
		} catch (IOException e) {
			Logger.raiseMinor("An error occured on file saving on directory:" + tipoFichero.folderLocation + ".");
			return null;
	    	
		} catch (Exception e) {
			Logger.raiseMinor("An unknown error occured on file saving on directory:" + tipoFichero.folderLocation + ".");
			return null;
		}
    }

    public static void delete(File file) throws IOException{
    	if (!file.exists()) {
//    		folderCreate(file.get)
    		
    	} else if(file.isDirectory()){
 
    		//directory is empty, then delete it
    		if(file.list().length==0){
    			
    		   file.delete();
    		   Logger.write("Directory was deleted : " 
    				   + file.getAbsolutePath());
    			
    		} else {
    		   //list all the directory contents
        	   String files[] = file.list();
     
        	   for (String temp : files) {
        	      //construct the file structure
        	      File fileDelete = new File(file, temp);
        		 
        	      //recursive delete
        	      delete(fileDelete);
        	   }
        		
        	   //check the directory again, if empty then delete it
        	   if(file.list().length==0){
           	     file.delete();
           	     Logger.write("Directory is deleted : " 
                                                  + file.getAbsolutePath());
        	   }
    		}
    		
    	} else {
    		//if file, then delete it
    		file.delete();
    		Logger.write("File is deleted : " + file.getAbsolutePath());
    	}
    }
    
    public static File locateOldestFolder(File rootFolder) {
    	
    	File[] folders = rootFolder.listFiles(new FileFilter() {
    	    public boolean accept(File f) {
    	        return f.isDirectory();
    	    }
    	});
    	
    	File oldestFolder = null;
    	long oldestTimeDetected = new Date().getTime(); //now
    	
    	for (File folder : folders) {
    		if (folder.lastModified() < oldestTimeDetected) {
    			oldestFolder = folder;
    			oldestTimeDetected = folder.lastModified();
    		}
    	}
    	
    	return oldestFolder;
    }
    
    /**
     * Recorre la lista de archivos de una carpeta comprobando si alguno de los elementos coincide con un nombre dado por par�metro
     * @param downloadPath 
     * @param fileName 
     * @return 
     */
    public static boolean isFileDownloaded(FolderType downloadPath, String fileName) {
    	
    	String tiempoEsperaDescarga = FilesPropertiesAccess.getPropertyValue("tiempoEsperaCargaElementos", "propiedadesFuncionales.properties");
    	long tiempoInicial = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		long tiempoActual = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    	
	    File dir = new File(getBaseFolder(downloadPath).getAbsolutePath());
	    File[] dir_contents = dir.listFiles();
	    
		while (tiempoActual < tiempoInicial + TimeUnit.SECONDS.toSeconds(Long.parseLong(tiempoEsperaDescarga)))
		{
		    for (int i = 0; i < dir_contents.length; i++) {
		        if (dir_contents[i].getName().equals(fileName)){
		        	return true;
		        }
            }
			tiempoActual = TimeUnit.SECONDS.toSeconds(System.currentTimeMillis());
		}
	    return false;
	}
    
	
}
