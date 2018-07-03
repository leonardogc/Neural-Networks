package network;

import java.io.Serializable;
import java.util.ArrayList;

public class Neuron implements Serializable{
	public double activation;
	public double z;
	public double bias;
	
	public double dC_dB;
	public double temp_dC_dA;
	public double temp_dC_dZ;
	
	//connections from other neurons to this neuron
	public ArrayList<NeuronConnection> inputs;	
	
	public Neuron() {
		this.activation = 0;
		this.bias = 0;
		this.inputs = new ArrayList<>();
		this.dC_dB = 0;
		this.temp_dC_dA = 0;
		this.temp_dC_dZ = 0;
		this.z = 0;
	}
	
	public Neuron(double bias) {
		this.activation = 0;
		this.bias = bias;
		this.inputs = new ArrayList<>();
		this.dC_dB = 0;
		this.temp_dC_dA = 0;
		this.temp_dC_dZ = 0;
		this.z = 0;
	}
}
