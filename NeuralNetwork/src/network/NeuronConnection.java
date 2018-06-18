package network;

import java.io.Serializable;

public class NeuronConnection implements Serializable{
	public Neuron from;
	public Neuron to;
	
	public double weight;
	public double dC_dW;
	
	
	public NeuronConnection(Neuron from, Neuron to, double weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
		this.dC_dW = 0;
	}
	
	public NeuronConnection(Neuron from, Neuron to) {
		this.from = from;
		this.to = to;
		this.weight = 0;
		this.dC_dW = 0;
	}
}
