package com.juancarloscasas.baseProject.FrameworkTools.Constants;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public final class FileFolderTypeConstants {
	private static Calendar calendar = Calendar.getInstance();
	public static Date date = calendar.getTime();

	public static enum FolderType {
		SCREENSHOTS(System.getProperty("user.dir") + File.separator + "SeleniumTestScreenshots",
				5),
		LOGFILES(System.getProperty("user.dir") + File.separator + "SeleniumLogFiles",
				5),
		DOWNLOADS(System.getProperty("user.dir") + File.separator + "SeleniumDownloads",
				5);
		
		public String folderLocation;
		public int maximunNumberOfFiles;
		private FolderType(String folderLocation, int maximunNumberOfFiles) {
			this.folderLocation = folderLocation;
			this.maximunNumberOfFiles = maximunNumberOfFiles;
		}
	}

}
