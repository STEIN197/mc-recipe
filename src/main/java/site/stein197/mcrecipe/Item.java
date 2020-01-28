package site.stein197.mcrecipe;

import java.io.File;

public class Item {

	private int ID;
	private NamespacedID ns;
	private String nsID;
	private String name;
	private File image;

	public Item(int ID, NamespacedID ns, String nsID, String name, String imageName) {
		this.ID = ID;
		this.ns = ns;
		this.nsID = nsID;
		this.name = name;
	}

	public void create() {}
}
