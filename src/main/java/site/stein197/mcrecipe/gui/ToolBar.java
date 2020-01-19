package site.stein197.mcrecipe.gui;

import javax.swing.JToolBar;

import net.miginfocom.swing.MigLayout;
import site.stein197.mcrecipe.Application;

import java.awt.event.ActionEvent;

public class ToolBar extends JToolBar {

	public static final ToolBar instance = new ToolBar();

	/** Saves all changes to files */
	private final IconButton btnSaveChanges = new IconButton("/images/music_disc_wait.png", "Save changes");
	private final IconButton btnNewItem = new IconButton("/images/item_grass.png", "Add a new item");
	private final IconButton btnNewRecipe = new IconButton("/images/crafting_table_front_plus.png", "Add a recipe");
	private final IconButton btnNewMachine = new IconButton("/images/anvil_base_plus.png", "Add a machine");
	private final IconButton btnNewNSName = new IconButton("/images/nether_portal_plus.png", "Add a new namespace");

	private ToolBar() {
		this.setFloatable(false);
		this.setMargin(null);
		this.setBorder(null);
		this.setLayout(new MigLayout());
		this.setListeners();
		this.addControls();
	}
	
	private void setListeners() {
		this.btnSaveChanges.addActionListener(this::saveChanges);
		this.btnNewItem.addActionListener(this::addItem);
		this.btnNewRecipe.addActionListener(this::addRecipe);
		this.btnNewNSName.addActionListener(this::addNamespace);
	}

	private void addControls() {
		this.add(this.btnSaveChanges);
		this.add(this.btnNewItem);
		this.add(this.btnNewRecipe);
		this.add(this.btnNewMachine);
		this.add(this.btnNewNSName);
	}

	private void saveChanges(ActionEvent e) {} // TODO

	/**
	 * Shows item creation dialog on click
	 * @param e
	 */
	private void addItem(ActionEvent e) {
		new ItemDialog(Application.instance.frame, true).setVisible(true);
	}

	private void addRecipe(ActionEvent e) {} // TODO

	private void addNamespace(ActionEvent e) {} // TODO
}
