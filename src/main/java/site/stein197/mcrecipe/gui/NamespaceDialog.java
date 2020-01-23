package site.stein197.mcrecipe.gui;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import site.stein197.mcrecipe.NamespacedID;

import java.awt.Dimension;

public class NamespaceDialog extends JDialog {

	private static final Dimension SIZE = new Dimension(300, 120);
	private static final String TITLE = "Add a new namespace";

	/** {@code true} if new item is being created. False if we edit existing one. */
	private final boolean isNew;
	private final JTextField nameField = new JTextField();

	public NamespaceDialog(JFrame owner, boolean isNew) {
		this.isNew = isNew;
		this.setupGUI(owner);
	}

	private void setupGUI(JFrame owner) {
		this.setTitle(TITLE);
		this.setSize(SIZE);
		this.setResizable(false);
		var root = new JPanel(new MigLayout("wrap 2", "[][fill]"));
		this.setContentPane(root);
		root.add(new JLabel("Name*: "));
		root.add(this.nameField, "pushx");
		var addBtn = new JButton("Add");
		root.add(addBtn, "span 2, growx 100");
		this.setLocationRelativeTo(owner);
	}
}
