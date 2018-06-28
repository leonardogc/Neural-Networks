package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import network.Layer;
import network.NeuralNetwork;
import network.Neuron;
import network.NeuronConnection;

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
	
	public static final double passPercentage = 1.0;
	public static final double crossProb = 1.0;
	public static final double mutationProb = 0.04;
	public static final double bestBirdsN = 10;
	
	public static int[] nn = new int[] {3, 6, 1};
	
	public ArrayList<Bird> birds;
	private ArrayList<Bird> deadBirds;
	public ArrayList<Pipe> pipes;
	
	private int gen;
	
	private Random rand;
	
	public Game(int nBirds) {
		this.birds = new ArrayList<>();
		this.deadBirds = new ArrayList<>();
		this.pipes = new ArrayList<>();
		this.rand = new Random();
		this.gen = 1;
		
		for(int i = 0; i < nBirds; i++) {
			this.birds.add(new Bird(birdX, height/2, 0, birdRadius, new NeuralNetwork(nn)));
		}

		newPipe();
	}

	public void update(double t) {
		updatePipes(t);
		updateBirds(t);

		if(this.birds.size() == 0) {
			nextGen();
		}
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
		double totalFitness = 0;
		
		ArrayList<NeuralNetwork> selected = new ArrayList<>();
		ArrayList<NeuralNetwork> cross = new ArrayList<>();
		ArrayList<NeuralNetwork> bestBirds = new ArrayList<>();
		
		Collections.sort(this.deadBirds, new Comparator<Bird>() {
			@Override
			public int compare(Bird o1, Bird o2) {
				if(o1.fitness > o2.fitness) {
					return -1;
				}
				
				if(o1.fitness < o2.fitness) {
					return 1;
				}
				
				return 0;
			}
		});
		
		for(int i = 0; i < this.deadBirds.size() && i < bestBirdsN; i++) {
			bestBirds.add(this.deadBirds.get(i).brain.copy());
		}
		
		for(int i = 0; i < this.deadBirds.size(); i++) {
			totalFitness+=this.deadBirds.get(i).fitness;
		}
		
		for(int i = 0; i < this.deadBirds.size(); i++) {
			this.deadBirds.get(i).fitness/=totalFitness;
		}
		
		for(int i = 1; i < this.deadBirds.size(); i++) {
			this.deadBirds.get(i).fitness+=this.deadBirds.get(i-1).fitness;
		}
		
		while(selected.size() < this.deadBirds.size()*passPercentage-bestBirdsN) {
			double v = this.rand.nextDouble();
			
			for(int i = 0; i < this.deadBirds.size(); i++) {
				Bird b = this.deadBirds.get(i);
				
				if(i == 0) {
					if(v >= 0 && v < b.fitness) {
						selected.add(b.brain.copy());
						break;
					}
				}
				else {
					if(v >= this.deadBirds.get(i-1).fitness && v < b.fitness) {
						selected.add(b.brain.copy());
						break;
					}
				}
			}
		}
		
		for(int i = 0; i < selected.size(); i++) {
			double v = this.rand.nextDouble();
			
			if(v < crossProb) {
				cross.add(selected.get(i));
				selected.remove(i);
				i--;
			}
		}
		
		if(cross.size() % 2 != 0) {
			selected.add(cross.get(cross.size()-1));
			cross.remove(cross.size()-1);
		}
		
		while(cross.size() > 0) {
			NeuralNetwork b1 = cross.get(0);
			NeuralNetwork b2 = cross.get(1);
			
			cross.remove(0);
			cross.remove(0);
			
			for(int l = 1; l < b1.layers.size() && l < b2.layers.size(); l++) {
				Layer layer1 = b1.layers.get(l);
				Layer layer2 = b2.layers.get(l);
				
				for(int n = 0; n < layer1.neurons.size() && n < layer2.neurons.size(); n++) {
					Neuron neuron1 = layer1.neurons.get(n);
					Neuron neuron2 = layer2.neurons.get(n);
					
					for(int pn = 0; pn < neuron1.inputs.size() && pn < neuron2.inputs.size(); pn++) {
						NeuronConnection conn1 = neuron1.inputs.get(pn);
						NeuronConnection conn2 = neuron2.inputs.get(pn);
						
						double v = this.rand.nextDouble();

						double w1 = conn1.weight;
						double w2 = conn2.weight;

						conn1.weight = w1*v+w2*(1.0-v);
						conn2.weight = w1*(1.0-v)+w2*v;

						v = this.rand.nextDouble();

						if(v < mutationProb) {
							conn1.weight+=(this.rand.nextDouble()*2.0)-1.0;
						}

						v = this.rand.nextDouble();

						if(v < mutationProb) {
							conn2.weight+=(this.rand.nextDouble()*2.0)-1.0;
						}
					}
				}
			}
			
			selected.add(b1);
			selected.add(b2);
		}
		
		ArrayList<Bird> nextBirds = new ArrayList<>();
		
		while(selected.size() > 0) {
			nextBirds.add(new Bird(birdX, height/2, 0, birdRadius, selected.get(0)));
			selected.remove(0);
		}
		
		while(bestBirds.size() > 0) {
			nextBirds.add(new Bird(birdX, height/2, 0, birdRadius, bestBirds.get(0)));
			bestBirds.remove(0);
		}
		
		while(nextBirds.size() < this.deadBirds.size()) {
			nextBirds.add(new Bird(birdX, height/2, 0, birdRadius, new NeuralNetwork(nn)));
		}
		
		this.deadBirds.clear();
		this.birds.clear();
		this.pipes.clear();
		
		this.birds.addAll(nextBirds);
		newPipe();
		
		this.gen++;
		
		System.out.println("Gen " + gen);
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
