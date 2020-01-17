package site.stein197.mcrecipeeditor;

import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.Point;

public class Application {

	public static final Application instance = new Application();
	public static final String TITLE = "Minecraft Recipe Editor";
	public static final String PROPERTIES_PATH = "./mc.conf";

	private final JFrame frame = new JFrame(TITLE);
	private ApplicationProperties properties;

	public static void main(String... args) throws Exception {
		
	}

	private Application() {
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		var propertiesAreLoaded = this.loadProperties();
		if (!propertiesAreLoaded)
			this.showErrorMessage("Failed to load properties", new Exception());
		this.setupGUI();
		this.setupToolbar();
		this.setListeners();
		this.frame.setVisible(true);
	}

	/**
	 * Tells user about happened exceptions.
	 * @param message Message to be shown to user.
	 * @param ex Exception that led to error.
	 */
	public void showErrorMessage(String message, Exception ex) {
		message = message == null ? "" : message;
		var builder = new StringBuilder(message);
		for (StackTraceElement e : ex.getStackTrace()) {
			builder
				.append("\n")
				.append(e.toString());
		}
		JOptionPane.showMessageDialog(this.frame, builder.toString(), ex.getClass().getCanonicalName(), JOptionPane.ERROR_MESSAGE);
	}

	private void setListeners() {
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
					Application.this.showErrorMessage(ex.getMessage(), ex);
				}
			}
		});
	}

	private void setupGUI() {
		this.frame.setIconImage(new ImageIcon(FileManager.getResource("/images/grass_side.png")).getImage());
		var root = new JPanel();
		root.setLayout(new MigLayout());
		this.frame.setContentPane(root);
		this.setupLocation();
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

	private void setupToolbar() {
		var toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setMargin(new Insets(10, 10, 10, 10));
		var addBtn = new JButton(new ImageIcon(FileManager.getResource("/images/icon_plus.png")));
		addBtn.setSize(new Dimension(16, 16));
		addBtn.setBorder(null);
		addBtn.setMargin(new Insets(0, 0, 0, 0));
		toolbar.add(addBtn);
		toolbar.setAlignmentX(java.awt.Container.LEFT_ALIGNMENT);
		this.frame.getContentPane().add(toolbar);
	}

	/**
	 * Loads configuration file.
	 * @param path Relative path to file.
	 * @return {@code true} if configuration was successfully loaded.
	 */
	private boolean loadProperties() {
		try {
			this.properties = ApplicationProperties.getInstance();
			return true;
		} catch (Exception ex) {
			this.showErrorMessage(ex.getMessage(), ex);
			return false;
		}
	}
}
