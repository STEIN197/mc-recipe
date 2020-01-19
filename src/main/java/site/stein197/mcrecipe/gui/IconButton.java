package site.stein197.mcrecipe.gui;

import site.stein197.mcrecipe.FileManager;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.awt.event.MouseAdapter;

public class IconButton extends JButton {

	private static final byte SIZE = 32;

	public IconButton(String path, String title) {
		super();
		try {
			URL iconUrl = FileManager.getResource(path);
			BufferedImage icon = ImageIO.read(iconUrl);
			this.setIcon(new ImageIcon(iconUrl));
			this.setSize(new Dimension(icon.getWidth(), icon.getHeight()));
		} catch (Exception ex) {
			this.setIcon(new ImageIcon(FileManager.getResource("/images/icon_question.png")));
			this.setSize(new Dimension(SIZE, SIZE));
		}
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setMargin(null);
		this.setContentAreaFilled(false);
		this.setToolTipText(title);
		this.addMouseListener(new MouseHandler());
	}

	private class MouseHandler extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			IconButton.this.setBorder(BorderFactory.createLoweredBevelBorder());
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			IconButton.this.setBorder(BorderFactory.createRaisedBevelBorder());
		}
	}
}
