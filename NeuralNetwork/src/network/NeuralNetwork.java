package network;

import java.io.Serializable;
import java.util.ArrayList;

public class NeuralNetwork implements Serializable{
	private ArrayList<Layer> layers;
	
	public NeuralNetwork(ArrayList<Layer> layers) {
		this.layers = layers;
	}
	
	public NeuralNetwork(int[] neuronsPerLayer) {
		this.layers = new ArrayList<>();
		
		createNetwork(neuronsPerLayer);
	}
	
	private void createNetwork(int[] neuronsPerLayer) {
		for(int i = 0; i < neuronsPerLayer.length; i++) {
			Layer layer = new Layer();
			
			for(int n = 0; n < neuronsPerLayer[i]; n++) {
				layer.neurons.add(new Neuron());
			}
			
			this.layers.add(layer);
		}
		
		
	}
}
