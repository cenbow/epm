package br.net.woodstock.epm.signer.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class FSTree {

	public FSTree() {
		super();
	}

	@Test
	public void test1() throws Exception {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		for (File file : fsv.getRoots()) {
			System.out.println(file.getAbsolutePath());
			Icon icon = fsv.getSystemIcon(file);
			if (icon != null) {
				ImageIcon imageIcon = (ImageIcon) icon;
				BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
				bufferedImage.getGraphics().drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
				ImageIO.write(bufferedImage, "gif", new FileOutputStream("/tmp/icon.gif"));
			}
		}
	}

}
