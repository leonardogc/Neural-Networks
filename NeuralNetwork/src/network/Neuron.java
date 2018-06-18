package network;

import java.io.Serializable;
import java.util.ArrayList;

public class Neuron implements Serializable{
	public double activation; //always between 0 and 1, inclusive
	public double bias;
	
	public double dC_dB;
	public double temp_dC_dA;
	public double temp_var;
	
	//connections from other neurons to this neuron
	public ArrayList<NeuronConnection> inputs;
	
	//connections from this neuron to other neurons
	public ArrayList<NeuronConnection> outputs;
	
	public Neuron(double activation, double bias, ArrayList<NeuronConnection> inputs, ArrayList<NeuronConnection> outputs) {
		this.activation = activation;
		this.bias = bias;
		this.inputs = inputs;
		this.outputs = outputs;
		this.dC_dB = 0;
		this.temp_dC_dA = 0;
		this.temp_var = 0;
	}	
	
	public Neuron() {
		this.activation = 0;
		this.bias = 0;
		this.inputs = new ArrayList<>();
		this.outputs = new ArrayList<>();
		this.dC_dB = 0;
		this.temp_dC_dA = 0;
		this.temp_var = 0;
	}
}
