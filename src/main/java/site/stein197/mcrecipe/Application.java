package site.stein197.mcrecipe;

import site.stein197.mcrecipe.gui.ToolBar;
import site.stein197.mcrecipe.sql.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import java.awt.event.WindowAdapter;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.Point;

// import org.sqlite.JDBC;

public class Application {

	public static final String TITLE = "Minecraft Recipe Editor";
	public static final String FOLDER_PATH = "./mcrecipe/";

	private static Application instance;

	public final JFrame frame = new JFrame(TITLE);
	private ApplicationProperties properties;
	private Database db;

	public static void main(String... args) throws Exception {
		instance = new Application();
		instance.setupDatabaseConnection();
		instance.loadProperties();
		instance.setupGUI();
		instance.setListeners();
		instance.frame.setVisible(true);
	}

	private Application() {
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static Application getInstance() {
		return instance == null ? instance = new Application() : instance;
	}

	public Database getDB() {
		return this.db;
	}

	/**
	 * Tells user about happened exceptions.
	 * Exits the app.
	 * @param message Message to be shown to user.
	 * @param ex Exception that led to error.
	 */
	public void showExceptionMessage(String message, Exception ex) {
		message = message == null ? "" : message;
		var builder = new StringBuilder(message);
		builder.append("\n");
		for (StackTraceElement e : ex.getStackTrace()) {
			builder
				.append("\n")
				.append(e.toString());
		}
		JOptionPane.showMessageDialog(this.frame, builder.toString(), ex.getClass().getCanonicalName(), JOptionPane.ERROR_MESSAGE);
		System.out.println(ex);
		System.exit(1);
	}

	public void showExceptionMessage(Exception ex) {
		this.showExceptionMessage(ex.getMessage(), ex);
	}

	private void setListeners() {
		this.onWindowClosing();
	}

	private void setupGUI() {
		this.frame.setIconImage(new ImageIcon(FileManager.getResource("/images/grass_side.png")).getImage());
		var root = new JPanel();
		root.setBackground(new Color(0, 0, 0));
		root.setBorder(null);
		root.setLayout(new MigLayout("insets 0, wrap 1, gap 0", "[grow, fill]", "[][fill]"));
		this.frame.setContentPane(root);
		this.setupLocation();
		this.frame.add(ToolBar.instance);
		this.setupSplitPane();
	}

	private void setupLocation() {
		var size = new Dimension();
		String savedWidth = this.properties.getValue("width");
		String savedHeight = this.properties.getValue("height");
		if (savedWidth != null)
			size.width = Integer.parseInt(savedWidth);
		if (savedHeight != null)
			size.height = Integer.parseInt(savedHeight);

		String savedLeft = this.properties.getValue("left");
		String savedTop = this.properties.getValue("top");
		var location = new Point();
		if (savedLeft != null)
			location.x = Integer.parseInt(savedLeft);
		if (savedTop != null)
			location.y = Integer.parseInt(savedTop);

		String fullscreen = this.properties.getValue("fullscreen");
		boolean isFullscreen = fullscreen == null || fullscreen != null && Boolean.parseBoolean(fullscreen);
		this.frame.setExtendedState(isFullscreen ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);
		this.frame.setLocation(location);
		this.frame.setSize(size);
	}

	private void setupDatabaseConnection() {
		this.db = new Database();
		try {
			db.connect();
		} catch (Exception ex) {
			this.showExceptionMessage("Failed to connect to database", ex);
		}
	}

	private void setupSplitPane() {
		var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(0.5);
		splitPane.setResizeWeight(0.5);
		this.setupLeftSide(splitPane);
		this.frame.add(splitPane, "pushy");
	}

	private void setupLeftSide(JSplitPane splitPane) {
		var pane = new JPanel(new MigLayout("wrap 1", "[grow, fill]"));
		var scrollPane = new JScrollPane(pane);
		// scrollPane.setLayout(new MigLayout("flowy"));
		var label = new JLabel("Search items");
		var searchField = new JTextField();
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.add(label);
		pane.add(searchField, "growx 100");
		splitPane.setLeftComponent(scrollPane);
	}

	private void loadProperties() {
		try {
			this.properties = new ApplicationProperties();
		} catch (SQLException ex) {
			this.showExceptionMessage("Failed to load application properties", ex);
		}
	}

	private void onWindowClosing() {
		this.frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				var properties = Application.this.properties;
				var frame = Application.this.frame;
				properties.setValue("width", Integer.toString(frame.getWidth()));
				properties.setValue("height", Integer.toString(frame.getHeight()));
				Point location = frame.getLocationOnScreen();
				properties.setValue("left", Integer.toString(location.x));
				properties.setValue("top", Integer.toString(location.y));
				properties.setValue("fullscreen", Boolean.toString(frame.getExtendedState() == JFrame.MAXIMIZED_BOTH));
				try {
					Application.this.properties.saveChanges();
				} catch (SQLException ex) {
					Application.this.showExceptionMessage(ex.getMessage(), ex);
				}
			}
		});
	}
}
