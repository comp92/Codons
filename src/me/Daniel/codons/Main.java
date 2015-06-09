package me.Daniel.codons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	private static JFrame frame;
	private static int counter=0;
	private static CopyOnWriteArrayList<JLabel> labels = new CopyOnWriteArrayList<JLabel>();
	private static CopyOnWriteArrayList<JTextField> fields = new CopyOnWriteArrayList<JTextField>();
	public static void main(String[] args) throws IOException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {}
		frame = new JFrame("Codons v1.0 - Daniel Shaw");
		frame.setSize(new Dimension(415,220));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		@SuppressWarnings("unused")
		HackIcon h = new HackIcon(frame); //We use a new class because getting local files is annoying in static contexts.
		frame.setLayout(null);
		final JTextField dnaSeq = newField();
		dnaSeq.setBounds(new Rectangle(70,10,300,16));
		dnaSeq.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		dnaSeq.setToolTipText("Enter a DNA codon sequence here.");
		dnaSeq.addKeyListener(new KeyListener() { //Only allow A, C, G, T in textfield

			@Override
			public void keyPressed(KeyEvent e) {
				JTextField t = (JTextField) e.getSource();
				if(counter==3) {
					counter=0;
					String val = String.valueOf(e.getKeyChar()).toUpperCase();
					if(val.equals("A") || val.equals("G") || val.equals("T") || val.equals("C")) {
						t.setText(t.getText() + " ");
					}
				}
				t.setText(t.getText().toUpperCase());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				JTextField t = (JTextField) e.getSource();
				t.setText(t.getText().toUpperCase());
			}

			@Override
			public void keyTyped(KeyEvent e) {
				switch(String.valueOf(e.getKeyChar()).toUpperCase()) {
					case "A":
					case "G":
					case "T":
					case "C":
						counter++;
						break;
					case "\b":
						counter--;
						break;
					default:
						e.consume();
				}
				System.out.println(counter);
			}
		});
		JLabel dnaLabel = fieldLabel("DNA Code: ");
		dnaLabel.setBounds(new Rectangle(5,10,70,16));
		final JTextField rnaSeq = newField();
		rnaSeq.setBounds(new Rectangle(70,40,300,16));
		rnaSeq.setToolTipText("The mRNA codon sequence will be put here.");
		JLabel rnaLabel = fieldLabel("RNA Code: ");
		rnaSeq.setEditable(false);
		rnaLabel.setBounds(new Rectangle(5,40,70,16));
		final JTextField antiCodonSeq = newField();
		antiCodonSeq.setBounds(new Rectangle(70,70,300,16));
		antiCodonSeq.setToolTipText("The tRNA codon sequence will be put here.");
		JLabel antiCodonLabel = fieldLabel("AntiCodon: ");
		antiCodonSeq.setEditable(false);
		antiCodonLabel.setBounds(new Rectangle(5,70,70,16));
		final JTextArea proteinSeq = new JTextArea();
		proteinSeq.setBounds(new Rectangle(70,100,300,40));
		proteinSeq.setToolTipText("The amino-acid chain will be put here.");
		proteinSeq.setEditable(false);
		proteinSeq.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		proteinSeq.setLineWrap(true);
		proteinSeq.setWrapStyleWord(true);
		proteinSeq.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,12));
		proteinSeq.setBackground(new Color(238,238,238));
		JLabel proteinLabel = fieldLabel("Proteins: ");
		proteinLabel.setBounds(new Rectangle(5,100,70,16));
		JButton clearButton = new JButton("x");
		clearButton.setToolTipText("Clear DNA codon sequence.");
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dnaSeq.setText("");
				counter=0;
			}
		});
		clearButton.setBounds(new Rectangle(370,8,36,20));
		clearButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
		JButton aboutButton = new JButton("About");
		aboutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame about = new JFrame("Codons - About");
				try {
					@SuppressWarnings("unused")
					HackIcon c = new HackIcon(about);
				} catch (IOException e1) {}
				about.setSize(new Dimension(400,220));
				about.setVisible(true);
				about.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				about.setResizable(false);
				JTextArea aboutTxt = new JTextArea();
				aboutTxt.setLineWrap(true);
				aboutTxt.setWrapStyleWord(true);
				aboutTxt.setEditable(false);
				aboutTxt.setBackground(new Color(238,238,238));
				aboutTxt.setHighlighter(null);
				aboutTxt.addMouseMotionListener(new MouseMotionListener() { //Remove text selection
					@Override
					public void mouseMoved(MouseEvent arg0) {}
					@Override
					public void mouseDragged(MouseEvent arg0) {}
				});
				aboutTxt.setText("Codons - A utility that assists in the conversion of DNA codon sequences into: \n" +
				"* mRNA Codon Sequences\n" +
				"* tRNA AntiCodon Sequences\n" +
				"* Protein Chains\n\n" +
				"Usage: Enter a DNA codon sequence into the \"DNA Code\" field, then press convert.\n\n" +
				"Created by Daniel Shaw, 5 June 2015"
				);
				aboutTxt.setEnabled(false);
				aboutTxt.setForeground(Color.BLACK);
				about.add(aboutTxt);
			}
		});
		aboutButton.setBounds(new Rectangle(70,150,130,32));
		JButton convertButton = new JButton("Convert");
		convertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dnaSeq.setText(dnaSeq.getText().toUpperCase());
				rnaSeq.setText("");
				for(char c : dnaSeq.getText().toUpperCase().toCharArray()) {
					if(c=='A') {
						rnaSeq.setText(rnaSeq.getText() + "U");
					} else if(c=='T') {
						rnaSeq.setText(rnaSeq.getText() + "A");
					} else if(c=='G') {
						rnaSeq.setText(rnaSeq.getText() + "C");
					} else if(c=='C') {
						rnaSeq.setText(rnaSeq.getText() + "G");
					} else if(c==' ') {
						rnaSeq.setText(rnaSeq.getText() + " ");
					} else {
						rnaSeq.setText(rnaSeq.getText() + "?");
					}
				}
				antiCodonSeq.setText("");
				for(char c : rnaSeq.getText().toUpperCase().toCharArray()) {
					if(c=='A') {
						antiCodonSeq.setText(antiCodonSeq.getText() + "U");
					} else if(c=='U') {
						antiCodonSeq.setText(antiCodonSeq.getText() + "A");
					} else if(c=='G') {
						antiCodonSeq.setText(antiCodonSeq.getText() + "C");
					} else if(c=='C') {
						antiCodonSeq.setText(antiCodonSeq.getText() + "G");
					} else if(c==' ') {
						antiCodonSeq.setText(antiCodonSeq.getText() + " ");
					} else {
						antiCodonSeq.setText(antiCodonSeq.getText() + "?");
					}
				}
				proteinSeq.setText("Testing - Above for");
				for(int i=0;i<rnaSeq.getText().toUpperCase().length();i++) {
					proteinSeq.setText(proteinSeq.getText() + rnaSeq.getText().toUpperCase().substring(i,i));
				}
			}
		});
		convertButton.setBounds(new Rectangle(240,150,130,32));
		fields.add(dnaSeq);
		fields.add(rnaSeq);
		fields.add(antiCodonSeq);
		labels.add(dnaLabel);
		labels.add(rnaLabel);
		labels.add(antiCodonLabel);
		labels.add(proteinLabel);
		for(JTextField l : fields) {
			l.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
			l.addMouseListener(new MouseListener() { //Onclick (right button)= select all text in field

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==MouseEvent.BUTTON3) {
						JTextField src = (JTextField) e.getSource();
						src.selectAll();
					}
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
				}
			});
			frame.add(l);
		}
		proteinSeq.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON3) {
					JTextArea src = (JTextArea) e.getSource();
					src.selectAll();
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
		});
		for(JLabel l : labels) {
			frame.add(l);
		}
		frame.add(proteinSeq);
		frame.add(convertButton);
		frame.add(aboutButton);
		frame.add(clearButton);
		frame.setVisible(true);
	}
	public static JTextField newField() {
		JTextField field = new JTextField();
		field.setSize(new Dimension(300,16));
		return field;
	}
	public static JLabel fieldLabel(String label) {
		return new JLabel(label);
	}
}
