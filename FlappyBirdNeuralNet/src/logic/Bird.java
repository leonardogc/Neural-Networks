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
		ArrayList<Double> output = brain.getOutput(inputs);
		
		if(output.get(0) > 0.5) {
			this.jump = true;
		}
	}
	
	public void calculateFitness(double y) {
		fitness = distance - Math.abs(0.5-y);
	}
}
