package com.example.filesearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileSearchApp {
	String path;
	String regex;
	String zipFileName;
	Pattern pattern;

	public static void main(String[] args) {
		FileSearchApp app = new FileSearchApp();

		switch( Math.min(args.length, 3)) {
		case 0:
			System.out.println("USAGE: FileSearchApp path [regex] [zipfile]");
			return;
		case 3: app.setZipFileName(args[2]);
		case 2: app.setRegex(args[1]);
		case 1: app.setPath(args[0]);
		}
		try {
			app.walkDirectory(app.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void walkDirectory(String path) throws IOException {
		walkDirectoryJava8(path);
	}

	public boolean searchFile(File file) throws IOException {
		return searchFileJava8(file);
	}

	public void addFileToZip(File file) {
		System.out.println("addFileToZip: " + file);
	}



	








	public void walkDirectoryJava8(String path) throws IOException {
		Files.walk(Paths.get(path))
		.forEach(f -> processFile(f.toFile()));
	}

















	public void processFile(File file) {
		try {
			if (searchFile(file)) {
				addFileToZip(file);
			}
		} catch (IOException|UncheckedIOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error processing file: " + 
					file + ": " + e);
		}
	}













	public boolean searchFileJava6(File file) throws FileNotFoundException {
		boolean found = false;
		Scanner scanner = new Scanner(file, "UTF-8");
		while (scanner.hasNextLine()){
			found = searchText(scanner.nextLine());
			if (found) { break; }
		}
		scanner.close();
		return found;
	}







	public boolean searchFileJava7(File file) throws IOException {
		List<String> lines = Files.readAllLines(file.toPath(), 
				StandardCharsets.UTF_8);
		for (String line : lines) {
			if (searchText(line)) {
				return true;
			}
		}
		return false;
	}











	public boolean searchFileJava8(File file) throws IOException {
		return Files.lines(file.toPath(), StandardCharsets.UTF_8)
				.anyMatch(t -> searchText(t));
	}












	public boolean searchText(String text) {
		return (this.getRegex() == null) ?  true :
			this.pattern.matcher(text).matches();
	}





	public String getRelativeFilename(File file, File baseDir) {
		String fileName = file.getAbsolutePath().substring(
				baseDir.getAbsolutePath().length());

		// IMPORTANT: the ZipEntry file name must use "/", not "\".
		fileName = fileName.replace('\\', '/');

		while (fileName.startsWith("/")) {
			fileName = fileName.substring(1);
		}

		return fileName;
	}














	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
		this.pattern = Pattern.compile(regex);
	}

	public String getZipFileName() {
		return zipFileName;
	}

	public void setZipFileName(String zipFileName) {
		this.zipFileName = zipFileName;
	}

}
