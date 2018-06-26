package logic;

import java.util.ArrayList;
import java.util.Random;

import network.NeuralNetwork;

public class Game {
	public static final double g = 300;
	
	public static final double width = 500;
	public static final double height = 300;
	
	public static final double birdSpeed = 100;
	public static final double birdX = 20;
	public static final double birdBoostUp = 100;
	public static final double birdRadius = 10;
	
	public static final double openingSize = 100;
	public static final double distanceBetweenPipes = 60;
	public static final double pipeWidth = 50;
	
	public ArrayList<Bird> birds;
	private ArrayList<Bird> deadBirds;
	public ArrayList<Pipe> pipes;
	
	private Random rand;
	
	public Game(int nBirds) {
		this.birds = new ArrayList<>();
		this.deadBirds = new ArrayList<>();
		this.pipes = new ArrayList<>();
		this.rand = new Random();
		
		for(int i = 0; i < nBirds; i++) {
			this.birds.add(new Bird(birdX, height/2, 0, birdRadius, new NeuralNetwork(new int[] {4, 10, 1})));
		}
		
		this.pipes.add(new Pipe(pipeWidth, width+pipeWidth/2, this.rand.nextInt((int)(height-openingSize))+openingSize/2,openingSize));
	}
	
	public void update(double t) {
		double d = birdSpeed*t;
		
		for(int i = 0; i < this.pipes.size(); i++) {
			this.pipes.get(i).x-=d;
			
			if(this.pipes.get(i).x < -this.pipes.get(i).width/2) {
				
			}
		}
		
		for(int i = 0; i < this.birds.size(); i++) {
			this.birds.get(i).distance+=d;
			
			this.birds.get(i).vy += g*t;
			this.birds.get(i).y += this.birds.get(i).vy*t;
			
			if(this.birds.get(i).jump) {
				this.birds.get(i).jump = false;
				this.birds.get(i).vy = -birdBoostUp;
			}
			
			for(int i2 = 0; i2 < this.pipes.size(); i2++) {
				if(this.pipes.get(i2).collision(this.birds.get(i))) {
					this.deadBirds.add(this.birds.get(i));
					this.birds.remove(i);
					i--;
					break;
				}
			}
			
		}
		  
	}
	
	public void jump() {
		if(this.birds.size() > 0) {
			this.birds.get(0).jump = true;
		}
	}
}
