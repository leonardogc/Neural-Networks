package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import logic.Bird;
import logic.Game;
import logic.Pipe;

public class GraphicInterface extends JPanel implements KeyListener, MouseListener{
	
	public Game game;
	public static final int nBirds = 2000;
	public boolean playing;
	private LoopThread thread;
	
	private int dx = (int)(584 - Game.width)/2;
	private int dy = (int)(361 - Game.height)/2;
	
	
	public GraphicInterface() {
		addKeyListener(this);
		addMouseListener(this);
		
		this.playing = false;
		this.game = new Game(nBirds);
		
		this.thread = new LoopThread(this);
		this.thread.start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		
		g.drawLine(dx, dy, (int)(dx + Game.width), dy);
		g.drawLine(dx, (int)(dy + Game.height), (int)(dx + Game.width), (int)(dy + Game.height));
		
		g.setColor(new Color(255, 0, 0));
		
		for(int i = 0; i < this.game.birds.size(); i++) {
			Bird b = this.game.birds.get(i);
			g.fillOval((int)(dx+b.x-b.r), (int)(dy+b.y-b.r), (int)(2*b.r), (int)(2*b.r));
		}
		
		g.setColor(Color.BLACK);

		for(int i = 0; i < this.game.pipes.size(); i++) {
			Pipe p = this.game.pipes.get(i);

			g.fillRect((int)(dx+p.x-p.width/2), dy, (int)p.width, (int)(p.openingY-p.openingSize/2));
			g.fillRect((int)(dx+p.x-p.width/2), (int)(dy+p.openingY+p.openingSize/2), (int)p.width, (int)(Game.height-p.openingY-p.openingSize/2));
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.requestFocus();
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

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_R) {
			this.playing = false;
			this.game = new Game(nBirds);
			repaint();
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(!this.playing) {
				this.playing = true;
			}
			else {
				this.game.jump();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
