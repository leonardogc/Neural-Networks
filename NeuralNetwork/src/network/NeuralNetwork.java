package network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class NeuralNetwork implements Serializable{
	public ArrayList<Layer> layers;
	private double trainingEx;
	
	public NeuralNetwork(int[] neuronsPerLayer) {
		this.layers = new ArrayList<>();
		this.trainingEx = 0;
		
		createNetwork(neuronsPerLayer);
	}
	
	public NeuralNetwork(String file) {
		this.layers = new ArrayList<>();
		this.trainingEx = 0;
		
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
		
		Random r = new Random();
		
		for(int layer = 1; layer < this.layers.size(); layer++) {
			Layer prevLayer = this.layers.get(layer-1);
			Layer currLayer = this.layers.get(layer);
			
			double variance = 2.0/(prevLayer.neurons.size()+currLayer.neurons.size());
			
			for(int cn = 0; cn < currLayer.neurons.size(); cn++) {
				Neuron to = currLayer.neurons.get(cn);
				
				for(int pn = 0; pn < prevLayer.neurons.size(); pn++) {
					Neuron from = prevLayer.neurons.get(pn);
					NeuronConnection conn = new NeuronConnection(from, to, r.nextGaussian()*variance); 
					
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
	
	public ArrayList<Double> getOutput(ArrayList<Double> inputs) {
		if(inputs.size() != this.layers.get(0).neurons.size()) {
			System.out.println("Wrong input size");
			return null;
		}
		
		feedForward(inputs);
		
		ArrayList<Double> outputs = new ArrayList<>();
		
		Layer lastLayer = this.layers.get(this.layers.size() - 1);
		
		for(int i = 0; i < lastLayer.neurons.size(); i++) {
			outputs.add(lastLayer.neurons.get(i).activation);
		}
		
		return outputs;
	}
	
	private void feedForward(ArrayList<Double> inputs){
		Layer inputLayer = this.layers.get(0);
		
		for(int n = 0; n < inputLayer.neurons.size(); n++) {
			inputLayer.neurons.get(n).activation = inputs.get(n);
		}
		
		for(int l = 1; l < this.layers.size(); l++) {
			Layer layer = this.layers.get(l);
			
			for(int n = 0; n < layer.neurons.size(); n++) {
				double activation = 0;
				Neuron neuron = layer.neurons.get(n);
				
				activation+=neuron.bias;
				
				for(int pn = 0; pn < neuron.inputs.size(); pn++) {
					NeuronConnection conn = neuron.inputs.get(pn);
					activation+=conn.weight*conn.from.activation;
				}
				
				neuron.z = activation;
				neuron.activation = NetworkUtils.s(activation);
			}
		}
	}
	
	public void resetGradients() {
		for(int l = 1; l < this.layers.size(); l++) {
			Layer layer = this.layers.get(l);
			for(int n = 0; n < layer.neurons.size(); n++) {
				Neuron neuron = layer.neurons.get(n);
				
				neuron.dC_dB = 0;
				
				for(int pn = 0; pn < neuron.inputs.size(); pn++) {
					neuron.inputs.get(pn).dC_dW = 0;
				}
			}
		}
		
		this.trainingEx = 0;
	}
	
	
	public void updateGradients(ArrayList<Double> trainingInputs, ArrayList<Double> expectedOutputs) {
		if(trainingInputs.size() != this.layers.get(0).neurons.size()) {
			System.out.println("Wrong input size");
			return;
		}
		
		if(expectedOutputs.size() != this.layers.get(this.layers.size()-1).neurons.size()) {
			System.out.println("Wrong output size");
			return;
		}
		
		feedForward(trainingInputs);

		for(int l = this.layers.size() - 1; l > 0; l--) {
			Layer layer = this.layers.get(l);

			for(int n = 0; n < layer.neurons.size(); n++) {
				Neuron neuron = layer.neurons.get(n);

				if(l == this.layers.size() - 1) {
					neuron.temp_dC_dA = NetworkUtils.dCost(neuron.activation, expectedOutputs.get(n));
				}

				neuron.temp_var = NetworkUtils.ds(neuron.z)*neuron.temp_dC_dA;
				neuron.dC_dB += neuron.temp_var;

				for(int pn = 0; pn < neuron.inputs.size(); pn++) {
					NeuronConnection conn = neuron.inputs.get(pn);

					conn.dC_dW += conn.from.activation*conn.to.temp_var;
					
					if(n == 0) {
						conn.from.temp_dC_dA = 0;
					}
					
					conn.from.temp_dC_dA += conn.weight*conn.to.temp_var;
				}
			}
		}
		
		this.trainingEx++;
	}
	
	public void updateWeightsAndBias(double learningRate) {
		if(this.trainingEx == 0) {
			return;
		}
		
		for(int l = 1; l < this.layers.size(); l++) {
			Layer layer = this.layers.get(l);
			for(int n = 0; n < layer.neurons.size(); n++) {
				Neuron neuron = layer.neurons.get(n);
				
				neuron.bias -= learningRate*neuron.dC_dB/this.trainingEx;
				
				for(int pn = 0; pn < neuron.inputs.size(); pn++) {
					NeuronConnection conn = neuron.inputs.get(pn);
					conn.weight -= learningRate*conn.dC_dW/this.trainingEx;
				}
			}
		}
	}
	
	public NeuralNetwork copy() {
		int[] size = new int[this.layers.size()];
		
		for(int i = 0; i < this.layers.size(); i++) {
			size[i] = this.layers.get(i).neurons.size();
		}
		
		NeuralNetwork copy = new NeuralNetwork(size);
		
		for(int l = 0; l < copy.layers.size(); l++) {
			Layer layer = copy.layers.get(l);
			Layer thisLayer = this.layers.get(l);
			
			for(int n = 0; n < layer.neurons.size(); n++) {
				Neuron neuron = layer.neurons.get(n);
				Neuron thisNeuron = thisLayer.neurons.get(n);
				
				neuron.bias = thisNeuron.bias;
				
				for(int pn = 0; pn < neuron.inputs.size(); pn++) {
					NeuronConnection conn = neuron.inputs.get(pn);
					NeuronConnection thisConn = thisNeuron.inputs.get(pn);
					
					conn.weight = thisConn.weight;
				}
			}
		}
		
		return copy;
	}
	
	
	public static void main(String[] args) {
		NeuralNetwork n = new NeuralNetwork(new int[] {2, 3, 2});
		
		ArrayList<Double> in = new ArrayList<>();
		in.add(1.0);
		in.add(2.0);
		
		ArrayList<Double> out = n.getOutput(in);
		
		if(out == null) {
			System.out.println("Oh no!");
		}
		else {
			for(int i = 0; i < out.size(); i++) {
				System.out.println(out.get(i));
			}
		}
	}
}
