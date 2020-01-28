package site.stein197.mcrecipe.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;

import site.stein197.mcrecipe.Application;
import site.stein197.mcrecipe.FileManager;

public class Database {

	private static final String DB_PATH = Application.FOLDER_PATH + "application.db";
	private static final String SETUP_PATH = "/setup.sql";
	private Connection connection;
	private boolean connected = false;

	public void connect() throws IOException, SQLException {
		if (this.connected)
			return;
		var fManager = new FileManager(DB_PATH);
		boolean dbExists = fManager.fileExists();
		if (!dbExists) {
			fManager.createFile();
		}
		this.connection = DriverManager.getConnection("jdbc:sqlite:" + fManager.toString());
		if (!dbExists) {
			this.setupSchema();
		}
		this.connected = true;
	}

	public void query(String query) throws SQLException, SQLTimeoutException {
		Statement s = this.connection.createStatement();
		s.executeUpdate(query);
	}

	public LinkedList<HashMap<String, String>> getResults(String query) throws SQLException {
		Statement stmt = this.connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		ResultSetMetaData meta = rs.getMetaData();
		int columnsCount = meta.getColumnCount();
		var list = new LinkedList<HashMap<String, String>>();
		while (rs.next()) {
			var row = new HashMap<String, String>();
			for (int i = 1; i <= columnsCount; i++)
				row.put(meta.getColumnLabel(i), rs.getString(i));
			list.add(row);
		}
		rs.close();
		stmt.close();
		return list;
	}

	private void setupSchema() throws IOException {
		URL url = FileManager.getResource(SETUP_PATH);
		try {
			var inputStream = new FileInputStream(new File(url.toURI()));
			byte[] data = inputStream.readAllBytes();
			var builder = new StringBuilder();
			for (byte b : data)
				builder.append((char) b);
			this.query(builder.toString());
			inputStream.close();
		} catch (URISyntaxException ex) {
			Application.getInstance().showExceptionMessage("Failed to open file input stream", ex);
		} catch (SQLTimeoutException ex) {
			Application.getInstance().showExceptionMessage("DB timeout exceeded", ex);
		} catch (SQLException ex) {
			Application.getInstance().showExceptionMessage(ex);
		}
	}
}
