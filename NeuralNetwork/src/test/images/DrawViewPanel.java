package test.images;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JPanel;

import network.NeuralNetwork;


public class DrawViewPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener{
	private double[][] image;
	private ArrayList<Image> images;
	private int currImage;
	private NeuralNetwork net;
	
	public static final int pixelSize = 19;
	public static final int pictureSize = 28;
	
	public static final int dx = (582 - pictureSize*pixelSize)/2;
	public static final int dy = (553 - pictureSize*pixelSize)/2;
	
	public DrawViewPanel() throws IOException {
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		
		this.image = new double[pictureSize][pictureSize];
		this.images = ImageRecognition.loadImages(0, 1000, "C:\\Users\\Leonardo Capozzi\\Documents\\GitHub\\Neural-Networks\\NeuralNetwork\\src\\test\\data\\mnist_test.csv");
		Collections.shuffle(this.images);
		this.currImage = 0; 
		this.net = new NeuralNetwork("imageRecog.txt");
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		
		for(int r = 0; r < pictureSize + 1; r++) {
			g.drawLine(dx, dy+r*pixelSize, dx+pixelSize*pictureSize, dy+r*pixelSize);
		}
		
		for(int c = 0; c < pictureSize + 1; c++) {
			g.drawLine(dx+c*pixelSize, dy, dx+c*pixelSize, dy+pictureSize*pixelSize);
		}
		
		for(int r = 0; r < this.image.length; r++) {
			for(int c = 0; c < this.image[r].length; c++) {
				float color = (float)this.image[r][c];
				g.setColor(new Color(color, color, color));
				g.fillRect(dx+c*pixelSize, dy+r*pixelSize, pixelSize, pixelSize);
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		boolean updateImage = false;
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.currImage++;
			
			updateImage = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(this.currImage > 0) {
				this.currImage--;
			}
			
			updateImage = true;
		}
		
		if(updateImage) {
			Image image = this.images.get(this.currImage);
			
			ArrayList<Double> out = this.net.getOutput(image.image);
			
			double max=-1;
			int max_i=-1;

			for(int i = 0; i < out.size(); i++) {
				if(out.get(i) > max) {
					max = out.get(i);
					max_i = i;
				}
			}

			System.out.println("Net: " + max_i);
			
			for(int i = 0; i < image.value.size(); i++) {
				if(image.value.get(i) == 1.0) {
					System.out.println("Real: " + i);
					break;
				}
			}
			
			System.out.println("\n\n");
			
			for(int r = 0; r < pictureSize; r++) {
				for(int c = 0; c < pictureSize; c++) {
					this.image[r][c] = image.image.get(28*r+c);
				}
			}
			
			repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		requestFocus();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}
