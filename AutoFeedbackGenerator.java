package com.feedback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AutoFeedbackGenerator {

	public static void main(String[] args) {
		AutoFeedbackGenerator afg = new AutoFeedbackGenerator();
		
		try {
			afg.generateAllInDirectory(new File(args[0]));
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage: AutoFeedbackGenerator <Path_To_Root_Directory>");
		}
	}

	public void generateAllInDirectory(File dir) {
		File[] files = dir.listFiles();
		Properties prop = new Properties();
		
		for (File file : files) {
			if (file.isDirectory()) {
				generateAllInDirectory(file);
			}
			if (file.getAbsolutePath().endsWith("params.properties")) {
				try (InputStream stream = new FileInputStream(file)) {
					prop.load(stream);
					List<File> submissionFiles = getSubmissionFiles(dir);
					for (File submissionFile : submissionFiles) {
						prop.setProperty("filename", submissionFile.getAbsolutePath());
						try {
							// Call main method
							String[] args = convertPropToArgs(prop);
							for(String arg : args) {
								System.out.print(arg + " ");
							}
							System.out.println();
						} catch (NullPointerException e) {
							System.out.println("NullPointerException in: " + submissionFile);
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String[] convertPropToArgs(Properties prop) {
		List<String> argsList = new ArrayList<String>();

		argsList.add(prop.getProperty("regulator"));
		argsList.add(prop.getProperty("filename"));
		argsList.add(prop.getProperty("niType"));
		argsList.add(prop.getProperty("niValue"));
		argsList.add(prop.getProperty("declNo"));
		argsList.add(prop.getProperty("repPeriod"));

		return argsList.toArray(new String[0]);
	}

	public List<File> getSubmissionFiles(File dir) {
		File[] files = dir.listFiles();
		List<File> submissionFiles = new ArrayList<File>();

		for (File file : files) {
			if (file.getAbsolutePath().endsWith(".xml.gz")) {
				submissionFiles.add(file);
			}
		}

		return submissionFiles;
	}

}
