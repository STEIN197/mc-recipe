package site.stein197.mcrecipe;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Loads resources and files required by the app.
 */
public class FileManager {

	private static File JAR_FILE;

	private final String path;
	private final File file;

	static {
		var url = FileManager.class.getProtectionDomain().getCodeSource().getLocation();
		try {
			JAR_FILE = new File(url.toURI()).getParentFile();
		} catch (URISyntaxException ex) {
			Application.instance.showExceptionMessage(ex);
		}
	}

	public FileManager(String path) throws IllegalArgumentException, URISyntaxException {
		if (path == null || path.length() == 0)
			throw new IllegalArgumentException("Empty path string");
		this.path = path;
		this.file = new File(JAR_FILE, path);
	}

	public File loadFile() throws IOException, SecurityException {
		if (!this.file.exists()) {
			this.createFile();
		}
		return this.file;
	}

	private void createFile() throws IOException, SecurityException {
		boolean created = false;
		try {
			created = this.file.createNewFile();
		} catch (IOException ex) {
			created = false;
		}
		if (!created) {
			String[] pathParts = this.path.split("/");
			if (pathParts.length > 1) {
				var dirAr = new String[pathParts.length - 1];
				System.arraycopy(pathParts, 0, dirAr, 0, pathParts.length - 1);
				String dirPath = String.join("/", dirAr);
				new File(JAR_FILE, dirPath).mkdirs();
			}
		}
		this.file.createNewFile();
	}

	public static URL getResource(String path) throws NullPointerException {
		return FileManager.class.getResource(path);
	}
}
