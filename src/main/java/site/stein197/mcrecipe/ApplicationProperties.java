package site.stein197.mcrecipe;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import site.stein197.mcrecipe.sql.Database;

public class ApplicationProperties {

	private HashMap<String, String> properties = new HashMap<>();

	public ApplicationProperties() throws SQLException {
		this.read();
	}

	public String getValue(String key) {
		return this.properties.get(key);
	}

	public String setValue(String key, String value) {
		String old = this.properties.get(key);
		this.properties.put(key, value);
		return old;
	}

	public void saveChanges() throws SQLException {
		Database db = Application.getInstance().getDB();
		db.query("DELETE FROM Property");
		for (var e : this.properties.entrySet()) {
			db.query("INSERT INTO Property (name, value) VALUES ('" + e.getKey() + "', '" + e.getValue() + "')");
		}

	}

	/**
	 * Reads data from database into {@link #properties}
	 */
	private void read() throws SQLException {
		Database db = Application.getInstance().getDB();
		LinkedList<HashMap<String, String>> data = db.getResults("SELECT * FROM Property");
		for (HashMap<String, String> row : data)
			this.properties.put(row.get("name"), row.get("value"));
	}
}
// TODO Remove it. Replace with sqlite