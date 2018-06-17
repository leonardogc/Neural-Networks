package network;

import java.io.Serializable;
import java.util.ArrayList;

public class Layer implements Serializable{
	public ArrayList<Neuron> neurons;
	
	public Layer(ArrayList<Neuron> neurons) {
		this.neurons = neurons;
	}
	
	public Layer() {
		neurons = new ArrayList<>();
	}
}
