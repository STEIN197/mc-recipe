package site.stein197.mcrecipe;

import site.stein197.mcrecipe.gui.ToolBar;
import site.stein197.mcrecipe.sql.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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

	public static final Application instance = new Application();
	public static final String TITLE = "Minecraft Recipe Editor";
	public static final String FOLDER_PATH = "./mcrecipe/";

	public final JFrame frame = new JFrame(TITLE);
	private ApplicationProperties properties;

	public static void main(String... args) throws Exception {
		Database.getInstance();
	}

	private Application() {
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.loadProperties();
		this.setupGUI();
		this.setListeners();
		this.frame.setVisible(true);
	}

	/**
	 * Tells user about happened exceptions.
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

	/**
	 * Loads configuration file.
	 * @param path Relative path to file.
	 * @return {@code true} if configuration was successfully loaded.
	 */
	private void loadProperties() {
		try {
			this.properties = ApplicationProperties.getInstance();
		} catch (Exception ex) {
			this.showExceptionMessage(ex.getMessage(), ex);
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
				} catch (IOException ex) {
					Application.this.showExceptionMessage(ex);
				}
			}
		});
	}
}
