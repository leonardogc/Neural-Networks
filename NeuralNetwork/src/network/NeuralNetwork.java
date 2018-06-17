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
		
		for(int layer = 1; layer < this.layers.size(); layer++) {
			Layer prevLayer = this.layers.get(layer-1);
			Layer currLayer = this.layers.get(layer);
			
			for(int cn = 0; cn < currLayer.neurons.size(); cn++) {
				Neuron to = currLayer.neurons.get(cn);
				
				for(int pn = 0; pn < prevLayer.neurons.size(); pn++) {
					Neuron from = prevLayer.neurons.get(pn);
					//need to change weight
					NeuronConnection conn = new NeuronConnection(from, to, 1); 
					
					from.outputs.add(conn);
					to.inputs.add(conn);
				}
				
				//bias
				to.inputs.add(new NeuronConnection(null, to, 1));
			}
		}
	}
}
