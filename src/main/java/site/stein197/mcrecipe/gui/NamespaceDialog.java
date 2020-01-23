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
import java.awt.event.ActionEvent;

public class NamespaceDialog extends JDialog {

	private static final Dimension SIZE = new Dimension(300, 130);
	private static final String TITLE = "Add a new namespace";

	/** {@code true} if new item is being created. False if we edit existing one. */
	private final boolean isNew;
	private final JTextField nameField = new JTextField();
	private final JLabel errorMsg = new JLabel();

	public NamespaceDialog(JFrame owner, boolean isNew) {
		this.errorMsg.setVisible(false);
		this.errorMsg.setForeground(new java.awt.Color(0xFF, 0, 0));
		this.isNew = isNew;
		this.setupGUI(owner);
	}

	private void setupGUI(JFrame owner) {
		this.setTitle(TITLE);
		this.setSize(SIZE);
		this.setResizable(false);
		var root = new JPanel(new MigLayout("wrap 2", "[][fill]"));
		this.setContentPane(root);
		root.add(this.errorMsg, "span 2, growx 100");
		root.add(new JLabel("Name*: "));
		root.add(this.nameField, "pushx");
		var addBtn = new JButton("Add");
		addBtn.addActionListener(this::addNamespace);
		root.add(addBtn, "span 2, growx 100");
		this.setLocationRelativeTo(owner);
	}

	private void addNamespace(ActionEvent e) {
		String name = this.nameField.getText();
		var ns = new NamespacedID(name);
		boolean exceptionOccured;
		try {
			ns.create();
			exceptionOccured = false;
		} catch (Exception ex) {
			exceptionOccured = true;
			this.errorMsg.setText(ex.getMessage());
			this.errorMsg.setVisible(true);
		}
		if (!exceptionOccured) {
			this.dispose();
		}
	}
}
