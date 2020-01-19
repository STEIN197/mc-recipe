package site.stein197.mcrecipe.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import site.stein197.mcrecipe.Application;
import site.stein197.mcrecipe.FileManager;

public class Database {

	private static final String DB_PATH = Application.FOLDER_PATH + "application.db";
	private static Database instance;
	private Connection connection;

	private Database() {
		try {
			String absPath = new FileManager(DB_PATH).loadFile().toString();
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + absPath);
		} catch (Exception ex) {
			Application.instance.showExceptionMessage(ex);
		}
	}

	public static Database getInstance() {
		return instance == null ? instance = new Database() : instance;
	}

	// public ResultSet query(String query) {

	// }
}
