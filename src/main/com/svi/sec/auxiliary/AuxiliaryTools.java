package com.svi.sec.auxiliary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class AuxiliaryTools {

	private static Scanner scanRow;
	private static Scanner scanColumn;
	private static Scanner scanFiles;

	public static void main(String[] args) throws FileNotFoundException {
		String docIdPathName = "C:\\Users\\jmata\\Desktop\\Tool 1\\sample input and output\\doc ids";
		String indexFileName = "C:\\Users\\jmata\\Desktop\\Tool 1\\sample input and output\\index.txt";

		filterFile(docIdPathName, indexFileName);

	}
	
	public static void filterFile(String docIdPathName, String indexFileName) throws FileNotFoundException {
		String indexFile = "index.txt";
		final String outputFileName = "C:\\Users\\jmata\\Desktop\\Tool 1\\sample input and output\\filtered_" + indexFile;

		Map<String, String> filteredData = new LinkedHashMap<String, String>();

		filteredData = filterIndexFileData(readAllDocIds(docIdPathName), readIndexFile(indexFileName));
		try {
			createOutputFile(outputFileName, filteredData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		filteredData.forEach((k, v) -> System.out.println("Key: " + k + " Value: " + v));
		System.out.println("List of Doc Ids: " + readAllDocIds(docIdPathName));

	}

	public static Map<String, String> readIndexFile(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		FileInputStream inputStream = new FileInputStream(file);
		scanRow = new Scanner(inputStream);
		Map<String, String> dataMap = new LinkedHashMap<String, String>();

		while (scanRow.hasNextLine()) {
			String rowData = scanRow.nextLine();

			scanColumn = new Scanner(rowData);
			scanColumn.useDelimiter("\\|");
			if (scanColumn.hasNext()) {
				String docId = scanColumn.next();

				dataMap.put(docId, rowData);

				/*// for checking
				System.out.println(docId);
				dataMap.forEach((k, v) -> System.out.println(k + v));*/
			}
		}
		return dataMap;
	}

	public static List<String> readAllDocIds(String pathName) throws FileNotFoundException {
		List<String> docIds = new ArrayList<>();
		try (Stream<Path> paths = Files.walk(Paths.get(pathName))) {
			paths.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					try {
						readDocId(filePath, docIds);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

		return docIds;
	}

	public static List<String> readDocId(Path filePath, List<String> docIds) throws FileNotFoundException {
		String fileName = filePath.toString();
		FileInputStream inputStream = new FileInputStream(new File(fileName));
		scanFiles = new Scanner(inputStream);

		while (scanFiles.hasNextLine()) {
			String rowData = scanFiles.nextLine();
			docIds.add(rowData);
		}

		return docIds;
	}

	public static Map<String, String> filterIndexFileData(List<String> listOfDocIds,
			Map<String, String> indexFileData) {
		listOfDocIds.forEach((value) -> {
			if (indexFileData.containsKey(value)) {
				indexFileData.remove(value);
			}
		});
		return indexFileData;
	}

	public static void createOutputFile(String pathName, Map<String, String> filteredData) throws IOException {
		Files.write(Paths.get(pathName), filteredData.values(), StandardOpenOption.CREATE);
	}

}
