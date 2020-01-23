package site.stein197.mcrecipe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import site.stein197.mcrecipe.sql.Database;

public class NamespacedID {

	private static LinkedList<NamespacedID> namespaces;
	private static Pattern nameRegex = Pattern.compile("^[a-z0-9\\-_]+$");

	private String name;

	public NamespacedID(String ns) {
		this.name = ns.toLowerCase();
	}

	public void create() {

	}

	private boolean exists() {
		Database db = Application.getInstance().getDB();
		boolean exists = true;
		try {
			exists = db.getResults("SELECT * FROM Namespace WHERE name = '" + this.name + "'").size() > 0;
		} catch (SQLException ex) {
			Application.getInstance().showExceptionMessage(ex);
		}
		return exists;
	}

	private boolean nameIsValid() {
		var matcher = nameRegex.matcher(this.name);
		return matcher.matches();
	}

	private boolean nameIsEmpty() {
		return this.name.isEmpty();
	}

	public static List<NamespacedID> getList() { // Try to return interface instead of class
		if (namespaces == null) {
			namespaces = new LinkedList<>();
			Database db = Application.getInstance().getDB();
			try {
				LinkedList<HashMap<String, String>> data = db.getResults("SELECT * FROM Namespace");
				for (var e : data)
					namespaces.add(new NamespacedID(e.get("name")));
			} catch (Exception ex) {
				Application.getInstance().showExceptionMessage(ex);
			}
		}
		return namespaces;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
// TODO Add validator or JFomattedTextField instead
