package site.stein197.mcrecipeeditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

public class ApplicationProperties {

	private static final String PROPERTIES_PATH = Application.FOLDER_PATH + "properties.conf";
	private static ApplicationProperties instance;
	private HashMap<String, String> properties;
	private File file;

	private ApplicationProperties() throws IllegalArgumentException, URISyntaxException, IOException {
		this.load();
		this.read();
	}

	public static ApplicationProperties getInstance() throws IllegalArgumentException, URISyntaxException, IOException {
		if (instance == null)
			instance = new ApplicationProperties();
		return instance;
	}

	public String getValue(String key) {
		return this.properties.get(key);
	}

	public String setValue(String key, String value) {
		String old = this.properties.get(key);
		this.properties.put(key, value);
		return old;
	}

	public void saveChanges() throws IOException {
		var builder = new StringBuilder();
		for (var e : this.properties.entrySet()) {
			builder
				.append(e.getKey())
				.append("=")
				.append(e.getValue())
				.append("\n");
		}
		var writer = new BufferedWriter(new FileWriter(this.file, false));
		writer.write(builder.toString());
		writer.close();
	}

	private void load() throws IllegalArgumentException, URISyntaxException, IOException {
		var manager = new FileManager(PROPERTIES_PATH);
		this.file = manager.loadFile();
	}

	/**
	 * Reads file into {@link #properties}.
	 */
	private void read() throws IOException {
		var reader = new BufferedReader(new FileReader(this.file));
		String line = null;
		this.properties = new HashMap<>();
		do {
			line = reader.readLine();
			if (line == null)
				break;
			String[] parts = line.split("=");
			if (parts.length < 2)
				continue;
			this.properties.put(parts[0], parts[1]);
		} while (line != null);
		reader.close();
	}
}
// TODO What if file has illegal format
