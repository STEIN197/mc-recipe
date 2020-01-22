package site.stein197.mcrecipe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class NamespacedID {

	private static final String NS_PATH = Application.FOLDER_PATH + "ns.conf";
	
	private static File file;
	private static LinkedList<String> namespaces;

	static {
		try {
			var fManager = new FileManager(NS_PATH);
			file = fManager.loadFile();
		} catch (Exception ex) {
			Application.getInstance().showExceptionMessage(ex);
		}
		if (file != null)
			try {
				readFromFile();
			} catch (IOException ex) {
				Application.getInstance().showExceptionMessage(ex);
			}
	}

	public static void saveChanges() throws IOException {
		var builder = new StringBuilder();
		for (var ns : namespaces) {
			builder
				.append(ns)
				.append("\n");
		}
		var writer = new BufferedWriter(new FileWriter(file, false));
		writer.write(builder.toString());
		writer.close();
	}

	private static void readFromFile() throws IOException {
		var reader = new BufferedReader(new FileReader(file));
		String line = null;
		namespaces = new LinkedList<>();
		while ((line = reader.readLine()) != null)
			namespaces.add(line);
		reader.close();
	}
}
// TODO Add validator or JFomattedTextField instead
