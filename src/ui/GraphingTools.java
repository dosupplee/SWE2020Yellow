package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class GraphingTools {

	public final String TIME_GRAPH_ENDING = "_RAW.csv";
	public final String XY_GRAPH_ENDING = "_xy graph.csv";
	public final String RESULTS_TXT = "Simulation Results.txt";
	public final String FOOD_SETTINGS_NAME = "Food's in Simulation.csv";

	/**
	 * Deletes all the graph files in current directory
	 * 
	 * @param outputLog
	 */
	public void deleteGraphFiles() {
		ArrayList<String> lsOutput = lsCommand(); // get contents of directory
		// filter -> get the graph file names
		List<String> graphFileNames = lsOutput.stream()
				.filter(e -> e.endsWith(TIME_GRAPH_ENDING) || e.endsWith(XY_GRAPH_ENDING) || e.endsWith(FOOD_SETTINGS_NAME))
				.collect(Collectors.toList());
		

		// delete the files
		for (String fileName : graphFileNames) {
			File file = new File(fileName); // file to delete
			if (file.exists()) {
				file.delete(); // delete it
			}
		}

	}


	/**
	 * Returns the names of the graph files and sim results file in the current
	 * directory
	 * 
	 * @return
	 */
	public String[] getGraphFileNames() {
		ArrayList<String> lsOutput = lsCommand(); // get contents of directory
		// filter
		List<String> graphFileNames = lsOutput.stream()
				.filter(e -> e.endsWith(TIME_GRAPH_ENDING) || e.endsWith(XY_GRAPH_ENDING) || e.endsWith(RESULTS_TXT) || e.endsWith(FOOD_SETTINGS_NAME))
				.collect(Collectors.toList());
		

		// convert to array
		String[] graphFileNamesArray = new String[graphFileNames.size()];
		graphFileNamesArray = graphFileNames.toArray(graphFileNamesArray);
		return graphFileNamesArray;
	}

	/**
	 * Zips up all the 'filePaths' files in in a given directory and puts them into
	 * 'zipFile'
	 * 
	 * @param filePaths
	 * @param zipFile
	 */
	public void zipFiles(String[] filePaths, File zipFile) {
		// System.out.println(Arrays.toString(filePaths));
		try {
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (String aFile : filePaths) {
				zos.putNextEntry(new ZipEntry(new File(aFile).getName()));

				byte[] bytes = Files.readAllBytes(Paths.get(aFile));
				zos.write(bytes, 0, bytes.length);
				zos.closeEntry();
			}

			zos.close();

		} catch (FileNotFoundException ex) {
			System.err.println("A file does not exist: " + ex);
		} catch (IOException ex) {
			System.err.println("I/O error: " + ex);
		}
	}

	/**
	 * Returns the contents of the working directory
	 * 
	 * @return
	 */
	public ArrayList<String> lsCommand() {
		ArrayList<String> fileNames = new ArrayList<>();

		try // get all the files in directory
		{
			String lscmd = "ls"; // bash cmd to get the names of the contents in the working directory
			Process p = Runtime.getRuntime().exec(new String[] { "bash", "-c", lscmd }); // execute the command
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			// parse through the output
			String line = reader.readLine();
			while (line != null) {
				fileNames.add(line);
				line = reader.readLine();
			}
		} catch (IOException e1) {
			System.err.println("Pblm found1.");
		} catch (InterruptedException e2) {
			System.err.println("Pblm found2.");
		}

		return fileNames;
	}

}
