package site.stein197.mcrecipe;

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

	public void create() throws Exception, SQLException {
		if (this.nameIsEmpty())
			throw new Exception("Namespace name is empty");
		if (!this.nameIsValid())
			throw new Exception("Name contains invalid characters. Allowable are 0-9, a-z, - and _");
		// if (this.exists())
		// 	throw new Exception("Namespace with given name alredy exists");
		Database db = Application.getInstance().getDB();
		db.query("INSERT INTO Namespace (name) VALUES ('" + this.name + "')"); // Maybe in case of unique column exception will thrown
		updateList();
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
			updateList();
		}
		return namespaces;
	}

	private static void updateList() {
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

	@Override
	public String toString() {
		return this.name;
	}
}
// TODO Add validator or JFomattedTextField instead
