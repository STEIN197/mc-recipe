package site.stein197.mcrecipe;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Loads resources and files required by the app.
 */
public class FileManager {

	public static File CURRENT_DIRECTORY;

	private final String path;
	private final File file;

	static {
		var url = FileManager.class.getProtectionDomain().getCodeSource().getLocation();
		try {
			CURRENT_DIRECTORY = new File(url.toURI()).getParentFile();
		} catch (URISyntaxException ex) {
			Application.getInstance().showExceptionMessage(ex);
		}
	}

	/**
	 * 
	 * @param path Path relative to current jar directory. Like assets/image.png.
	 * @throws IllegalArgumentException
	 */
	public FileManager(String path) throws IllegalArgumentException {
		if (path == null || path.length() == 0)
			throw new IllegalArgumentException("Empty path string");
		this.path = path;
		this.file = new File(CURRENT_DIRECTORY, path);
	}

	/**
	 * Creates file anyway.
	 * @return
	 * @throws IOException
	 * @throws SecurityException
	 */
	public File loadFile() throws IOException, SecurityException {
		if (!this.fileExists()) {
			this.createFile();
		}
		return this.file;
	}

	public boolean fileExists() {
		return this.file.exists();
	}

	public boolean createFile() throws IOException, SecurityException {
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
				new File(CURRENT_DIRECTORY, dirPath).mkdirs();
			}
		}
		this.file.createNewFile();
		return created;
	}

	@Override
	public String toString() {
		return this.file.toString();
	}

	public static URL getResource(String path) throws NullPointerException {
		return FileManager.class.getResource(path);
	}
}
