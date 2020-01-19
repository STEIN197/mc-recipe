package site.stein197.mcrecipe.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import site.stein197.mcrecipe.Application;
import site.stein197.mcrecipe.FileManager;

public class Database {

	private static final String DB_PATH = Application.FOLDER_PATH + "application.db";
	private static final String SETUP_PATH = "/setup.sql";
	private static Database instance;
	private Connection connection;

	private Database() {
		try {
			boolean exists = this.exists();
			String absPath = new FileManager(DB_PATH).loadFile().toString();
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + absPath);
			if (!exists)
				this.setupSchema();
		} catch (Exception ex) {
			Application.instance.showExceptionMessage(ex);
		}
	}

	public static Database getInstance() {
		return instance == null ? instance = new Database() : instance;
	}

	public ResultSet query(String query) throws SQLException {
		Statement s = this.connection.createStatement();
		return s.executeQuery(query);
	}

	private boolean exists() {
		return new File(FileManager.CURRENT_DIRECTORY, DB_PATH).exists();
	}

	private void setupSchema() throws IOException, SQLException, URISyntaxException {
		URL url = FileManager.getResource(SETUP_PATH);
		var inputStream = new FileInputStream(new File(url.toURI()));
		byte[] data = inputStream.readAllBytes();
		var builder = new StringBuilder();
		for (byte b : data)
			builder.append((char) b);
		this.query(Arrays.toString(data));
		// System.out.println(builder.toString());
		inputStream.close();
	}
}
