package logic;

import java.util.ArrayList;
import java.util.Random;

import network.NeuralNetwork;

public class Game {
	public static final double g = 0.2;
	
	public static final double width = 100;
	public static final double height = 100;
	
	public static final double birdSpeed = 10;
	public static final double birdX = 10;
	public static final double birdBoostUp = 10;
	public static final double birdRadius = 10;
	
	public static final double openingSize = 10;
	public static final double distanceBetweenPipes = 30;
	public static final double pipeWidth = 10;
	
	private ArrayList<Bird> birds;
	private ArrayList<Bird> deadBirds;
	private ArrayList<Pipe> pipes;
	
	private Random rand;
	
	public Game(int nBirds) {
		this.birds = new ArrayList<>();
		this.deadBirds = new ArrayList<>();
		this.pipes = new ArrayList<>();
		
		for(int i = 0; i < nBirds; i++) {
			this.birds.add(new Bird(birdX, height/2, 0, birdRadius, new NeuralNetwork(new int[] {4, 10, 1})));
		}
		
		this.pipes.add(new Pipe(pipeWidth, width+pipeWidth/2, rand.nextInt((int)(height-openingSize))+openingSize/2,openingSize));
	}
	
	public void update(double t) {
		double d = birdSpeed*t;
		
		for(int i = 0; i < this.pipes.size(); i++) {
			
		}
	}
}
