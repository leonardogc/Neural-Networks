package logic;

import network.NeuralNetwork;

public class Bird {
	public double x;
	public double y;
	public double vy;
	public double r;
	public double distance;
	public NeuralNetwork brain;
	
	public boolean jump;
	
	public Bird(double x, double y, double vy, double r, NeuralNetwork brain) {
		this.x = x;
		this.y = y;
		this.vy = vy;
		this.r = r;
		this.distance = 0;
		this.brain = brain;
		
		this.jump = false;
	}
}
