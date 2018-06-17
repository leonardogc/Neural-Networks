package network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
	
	public NeuralNetwork(String file) {
		this.layers = new ArrayList<>();
		
		try {
			fromFile(file);
		} catch (IOException e) {
			System.out.println("Could not read File :(");
		}
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
					NeuronConnection conn = new NeuronConnection(from, to); 
					
					from.outputs.add(conn);
					to.inputs.add(conn);
				}
			}
		}
	}

	public void toFile(String s) throws IOException {
		System.out.println("Saving...");

		File file = new File(s);
		FileOutputStream os = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(os); 

		osw.write("nl;"+this.layers.size()+";\n");

		for(int i = 0; i < this.layers.size(); i++) {
			osw.write("nn;"+this.layers.get(i).neurons.size()+";\n");
		}

		for(int l = 1; l < this.layers.size(); l++) {
			Layer layer = this.layers.get(l);
			
			for(int n = 0; n < layer.neurons.size(); n++) {
				Neuron neuron = layer.neurons.get(n);
				osw.write("n;" + neuron.bias + ";");
				
				for(int pn = 0; pn < neuron.inputs.size(); pn++) {
					osw.write(neuron.inputs.get(pn).weight + ";");
				}
				
				osw.write("\n");
			}
		}

		osw.close();
		System.out.println("Saved!");
	}

	
	private void fromFile(String s) throws IOException {
		String line="";
		String[] sLine;

		File file = new File(s);
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		line = br.readLine();
		sLine = line.split(";");
		
		int[] neuronsPerLayer = new int[Integer.parseInt(sLine[1])];
		
		for(int i = 0; i < neuronsPerLayer.length; i++) {
			line = br.readLine();
			sLine = line.split(";");
			
			neuronsPerLayer[i] = Integer.parseInt(sLine[1]);
		}
		
		createNetwork(neuronsPerLayer);
		
		for(int l = 1; l < this.layers.size(); l++) {
			Layer layer = this.layers.get(l);
			
			for(int n = 0; n < layer.neurons.size(); n++) {
				Neuron neuron = layer.neurons.get(n);
				line = br.readLine();
				sLine = line.split(";");
				
				neuron.bias = Double.parseDouble(sLine[1]);
				
				for(int pn = 0; pn < neuron.inputs.size(); pn++) {
					neuron.inputs.get(pn).weight = Double.parseDouble(sLine[pn+2]);
				}
			}
		}
		
		br.close();
	}
}
