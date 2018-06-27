package logic;

import java.util.ArrayList;
import java.util.Random;

import network.NeuralNetwork;

public class Game {
	public static final double g = 800;
	public static final double maxSpeed = 600;
	
	public static final double width = 500;
	public static final double height = 300;
	
	public static final double birdSpeed = 100;
	public static final double birdX = 20;
	public static final double birdBoostUp = 300;
	public static final double birdRadius = 10;
	
	public static final double openingSize = 100;
	public static final double distanceBetweenPipes = 100;
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
			this.birds.add(new Bird(birdX, height/2, 0, birdRadius, new NeuralNetwork(new int[] {3, 6, 1})));
		}
		
		this.pipes.add(new Pipe(pipeWidth, width+pipeWidth/2, this.rand.nextInt((int)(height-openingSize))+openingSize/2,openingSize));
	}
	
	public void update(double t) {
		if(this.birds.size() == 0) {
			nextGen();
		}
		updatePipes(t);
		updateBirds(t);
	}
	
	private void updateBirds(double t) {
		double d = birdSpeed*t;
		
		for(int i = 0; i < this.birds.size(); i++) {
			Bird b = this.birds.get(i);
			
			b.distance+=d;
			
			b.vy += g*t;
			b.vy = Math.min(maxSpeed, b.vy);
			b.y += b.vy*t;
			
			b.think(calculateInputs(b));
			
			if(b.jump) {
				b.jump = false;
				b.vy = -birdBoostUp;
			}
			
			if(b.y < b.r || b.y > height-b.r) {
				b.calculateFitness(calculateInputs(b).get(1));
				this.deadBirds.add(b);
				this.birds.remove(i);
				i--;
				continue;
			}
			
			for(int i2 = 0; i2 < this.pipes.size(); i2++) {
				if(this.pipes.get(i2).collision(b)) {
					b.calculateFitness(calculateInputs(b).get(1));
					this.deadBirds.add(b);
					this.birds.remove(i);
					i--;
					break;
				}
			}
			
		}
	}
	
	private void updatePipes(double t) {
		double d = birdSpeed*t;

		if(this.pipes.size() == 0) {
			newPipe();
		}
		
		for(int i = 0; i < this.pipes.size(); i++) {
			this.pipes.get(i).x-=d;
		}
		
		Pipe pipe;
		
		pipe = this.pipes.get(0);

		if(pipe.x < -pipe.width/2) {
			this.pipes.remove(0);
		}
		
		pipe = this.pipes.get(this.pipes.size()-1);
		
		if(width-(pipe.x+pipe.width/2) > distanceBetweenPipes) {
			newPipe();
		}
	}
	
	private void nextGen() {
		
	}
	
	//inputs are x to pipe, y to pipe, bird vel
	private ArrayList<Double> calculateInputs(Bird bird){
		ArrayList<Double> inputs = new ArrayList<>();
		
		for(int i = 0; i < this.pipes.size(); i++) {
			Pipe pipe = this.pipes.get(i);
			
			if(Math.abs(pipe.x - bird.x) < pipe.width/2+bird.r) {
				inputs.add(0.0);
				inputs.add(((pipe.openingY-bird.y)+height)/(2*height));
				break;
			}
			else if(pipe.x - bird.x > 0) {
				inputs.add((pipe.x - pipe.width/2 - bird.x)/width);
				inputs.add(((pipe.openingY-bird.y)+height)/(2*height));
				break;
			}
		}
		
		inputs.add((bird.vy+maxSpeed)/(2*maxSpeed));
		
		return inputs;
	}

	

	private void newPipe() {
		this.pipes.add(new Pipe(pipeWidth, width+pipeWidth/2, this.rand.nextInt((int)(height-openingSize))+openingSize/2,openingSize));
	}
	
	public void jump() {
		if(this.birds.size() > 0) {
			//this.birds.get(0).jump = true;
		}
	}
}
