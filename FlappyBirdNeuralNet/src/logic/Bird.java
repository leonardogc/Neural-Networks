package logic;

import java.util.ArrayList;

import network.NeuralNetwork;

public class Bird {
	public double x;
	public double y;
	public double vy;
	public double r;
	
	public NeuralNetwork brain;
	
	public boolean jump;
	
	public double distance;
	public double fitness;
	
	public Bird(double x, double y, double vy, double r, NeuralNetwork brain) {
		this.x = x;
		this.y = y;
		this.vy = vy;
		this.r = r;
		this.distance = 0;
		this.brain = brain;
		this.fitness = 0;
		
		this.jump = false;
	}
	
	
	public void think(ArrayList<Double> inputs) {
		ArrayList<Double> output = this.brain.getOutput(inputs);
		
		if(output.get(0) > 0.5) {
			this.jump = true;
		}
	}
	
	public void calculateFitness(double y) {
		this.fitness = this.distance + (Game.height - (Math.abs(0.5-y)*2*Game.height));
		
		if(this.fitness == 0) {
			this.fitness = 0.01;
		}
	}
	
	public Bird copy() {
		Bird copy = new Bird(0,0,0,0,null);
		
		copy.brain = this.brain.copy();
		copy.distance = this.distance;
		copy.fitness = this.fitness;
		copy.jump = this.jump;
		copy.r = this.r;
		copy.vy = this.vy;
		copy.x = this.x;
		copy.y = this.y;
		
		return copy;
	}
}
