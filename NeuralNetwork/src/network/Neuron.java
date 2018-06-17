package network;

import java.io.Serializable;
import java.util.ArrayList;

public class Neuron implements Serializable{
	public double activation;
	
	//connections from other neurons to this neuron
	public ArrayList<NeuronConnection> inputs;
	
	//connections from this neuron to other neurons
	public ArrayList<NeuronConnection> outputs;
	
	public Neuron(double activation, ArrayList<NeuronConnection> inputs, ArrayList<NeuronConnection> outputs) {
		this.activation = activation;
		this.inputs = inputs;
		this.outputs = outputs;
	}	
	
	public Neuron() {
		this.activation = 0;
		this.inputs = new ArrayList<>();
		this.outputs = new ArrayList<>();
	}
}
