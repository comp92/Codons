package me.Daniel.codons;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class HackIcon {
	//A hacky class to set the icon of a frame to favicon.png
	public HackIcon(JFrame f) throws IOException {
		f.setIconImage(ImageIO.read(getClass().getResource("favicon.png")));
	}
}