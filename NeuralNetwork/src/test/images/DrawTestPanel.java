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
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JPanel;

import network.NeuralNetwork;


public class DrawTestPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener{
	private double[][] image;
	private NeuralNetwork net;
	
	public static final int pixelSize = 12;
	public static final int pictureSize = 28;
	
	public static final int dx = (384 - pictureSize*pixelSize)/2;
	public static final int dy = (361 - pictureSize*pixelSize)/2;
	
	public DrawTestPanel() {
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		
		this.image = new double[pictureSize][pictureSize];
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
				if(this.image[r][c] != 0) {
					g.fillRect(dx+c*pixelSize, dy+r*pixelSize, pixelSize, pixelSize);
				}
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(e.getX() >= dx + pixelSize * pictureSize  || e.getX() <= dx) {
			return;
		}

		if(e.getY() >= dy + pixelSize * pictureSize  || e.getY() <= dy) {
			return;
		}
		
		int row = (e.getY()-dy)/pixelSize;
		int col = (e.getX()-dx)/pixelSize;
		
		
		this.image[row][col] = 1;
		
		if(row != 0) {
			this.image[row-1][col] = 1;
		}
		
		if(col != 0) {
			this.image[row][col-1] = 1;
		}
		
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_E) {
			ArrayList<Double> in = new ArrayList<>();
			
			for(int r = 0; r < this.image.length; r++) {
				for(int c = 0; c < this.image[r].length; c++) {
					in.add(this.image[r][c]);
				}
			}

			ArrayList<Double> out = net.getOutput(in);


			double max=-1;
			int max_i=-1;

			for(int i = 0; i < out.size(); i++) {
				if(out.get(i) > max) {
					max = out.get(i);
					max_i = i;
				}
			}

			System.out.println("You wrote a " + max_i);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_R) {
			for(int r = 0; r < this.image.length; r++) {
				for(int c = 0; c < this.image[r].length; c++) {
					this.image[r][c] = 0;
				}
			}
			
			repaint();
		}
		
		/*if(e.getKeyCode() == KeyEvent.VK_F) {
		    FileWriter fw;
			try {
				fw = new FileWriter("2.txt", true);
			    fw.write("2");
			    
			    for(int r = 0; r < this.image.length; r++) {
					for(int c = 0; c < this.image[r].length; c++) {
						fw.write("," + (this.image[r][c] > 0 ? 255.0 : 0.0));
					}
				}
			    
			    fw.write("\n");
			    
			    fw.close();
			    System.out.println("Saved!");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		}*/
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
