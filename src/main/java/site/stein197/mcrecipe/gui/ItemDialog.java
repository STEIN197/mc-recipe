package site.stein197.mcrecipe.gui;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import net.miginfocom.swing.MigLayout;
import site.stein197.mcrecipe.NamespacedID;

import java.awt.Dimension;

public class ItemDialog extends JDialog {

	private static final Dimension SIZE = new Dimension(300, 230);
	private static final String TITLE = "Add a new item";

	/** {@code true} if new item is being created. False if we edit existing one. */
	private final boolean isNew;
	private final JFormattedTextField numIDField = new JFormattedTextField();
	private final JTextField stringIDField = new JTextField();
	private final JComboBox namespaces;
	private final JTextField nameField = new JTextField();

	public ItemDialog(JFrame owner, boolean isNew) {
		this.isNew = isNew;
		this.namespaces = new JComboBox(NamespacedID.getList().toArray());
		this.setupGUI(owner);
	}

	private void setupGUI(JFrame owner) {
		this.setTitle(TITLE);
		this.setSize(SIZE);
		this.setResizable(false);
		var root = new JPanel(new MigLayout("wrap 2", "[][fill]"));
		this.setContentPane(root);
		root.add(new JLabel("Numeric ID*: "));
		root.add(this.numIDField, "pushx");
		root.add(new JLabel("Namespace *: "));
		root.add(this.namespaces);
		root.add(new JLabel("Namespaced ID*: "));
		root.add(this.stringIDField);
		root.add(new JLabel("Name*: "));
		root.add(this.nameField, "");
		root.add(new JLabel("Image*: "));
		var fileBtn = new JButton("Add an image");
		root.add(fileBtn);
		var addBtn = new JButton("Add");
		root.add(addBtn, "span 2, growx 100");
		this.setLocationRelativeTo(owner);
	}
}
