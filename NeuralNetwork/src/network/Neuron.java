package network;

import java.io.Serializable;
import java.util.ArrayList;

public class Neuron implements Serializable{
	public double activation; //always between 0 and 1, inclusive
	public double z;
	public double bias;
	
	public double dC_dB;
	public double temp_dC_dA;
	public double temp_var;
	
	//connections from other neurons to this neuron
	public ArrayList<NeuronConnection> inputs;
	
	public Neuron(double activation, double bias, ArrayList<NeuronConnection> inputs) {
		this.activation = activation;
		this.bias = bias;
		this.inputs = inputs;
		this.dC_dB = 0;
		this.temp_dC_dA = 0;
		this.temp_var = 0;
		this.z = 0;
	}	
	
	public Neuron() {
		this.activation = 0;
		this.bias = 0;
		this.inputs = new ArrayList<>();
		this.dC_dB = 0;
		this.temp_dC_dA = 0;
		this.temp_var = 0;
		this.z = 0;
	}
}
