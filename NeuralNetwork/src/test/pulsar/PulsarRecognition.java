package test.pulsar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import network.NetworkUtils;
import network.NeuralNetwork;

public class PulsarRecognition {
	public static int inputVarsN = 8;
	
	public static ArrayList<Pulsar> loadPulsars(int from, int to, String path) throws IOException{		
		double[] range = new double[inputVarsN*2];
		
		for(int i = 0; i < range.length; i++) {
			if(i % 2 == 0) {
				range[i] = Double.POSITIVE_INFINITY;
			}
			else {
				range[i] = Double.NEGATIVE_INFINITY;
			}
		}
		
		String line="";
		String[] sLine;

		File file = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		for(int i = 0; i < from; i++) {
			br.readLine();
		}
		
		ArrayList<Pulsar> pulsars = new ArrayList<>();
		
		int counter = 0;
		
		while((line = br.readLine()) != null && counter < (to-from)) {
			sLine = line.split(",");
			
			ArrayList<Double> in = new ArrayList<>();
			ArrayList<Double> out = new ArrayList<>();
			
			double v;
			
			for(int i = 0; i < sLine.length - 1; i++) {
				v = Double.parseDouble(sLine[i]);
				in.add(v);
				
				if(v < range[2*i]) {
					range[2*i] = v;
				}
				
				if(v > range[2*i+1]) {
					range[2*i+1] = v;
				}
			}
			
			v = Double.parseDouble(sLine[sLine.length - 1]);
			out.add(v);
			
			pulsars.add(new Pulsar(in, out));
			
			counter++;
		}
		
		for(int i = 0; i < pulsars.size(); i++) {
			ArrayList<Double> in = pulsars.get(i).in;
			
			double v;
			
			for(int index = 0; index < in.size(); index++) {
				v = in.get(index);
				in.remove(index);
				
				v = (v-range[2*index])/(range[2*index+1]-range[2*index]);
				in.add(index, v);
			}
		}
		
		br.close();
		
		return pulsars;
	}
	
	public static void main(String[] args) throws IOException {
		//TRAIN
		ArrayList<Pulsar> pulsars = loadPulsars(0, 20000, "C:\\Users\\pc\\Desktop\\NeuralNetwork\\pulsars\\HTRU_2.csv");
		
//		ArrayList<Pulsar> positives = new ArrayList<>();
//		ArrayList<Pulsar> negatives = new ArrayList<>();
//		
//		ArrayList<Pulsar> pulsars = new ArrayList<>();
//		
//		for(int i = 0; i < allPulsars.size(); i++) {
//			Pulsar p = allPulsars.get(i);
//			
//			if(p.out.get(0) == 1) {
//				positives.add(p);
//			}
//			else {
//				negatives.add(p);
//			}
//		}
//		
//		Collections.shuffle(positives);
//		Collections.shuffle(negatives);
//		
//		while(positives.size() > 0 && negatives.size() > 0) {
//			pulsars.add(positives.get(0));
//			pulsars.add(negatives.get(0));
//			
//			positives.remove(0);
//			negatives.remove(0);
//		}
		
		Collections.shuffle(pulsars);
		
		NeuralNetwork net = new NeuralNetwork(new int[] {8, 50, 20, 1});
		//NeuralNetwork net = new NeuralNetwork("pulsarRecog.txt");
		
		int nTrEx = 1000;
		int index = 0;
		
		int costN = 200;

		for(int it = 0; it < 2000; it++) {
			//calculate cost
			
			double averageCost = 0;
			
			for(int i = 0; i < costN && i < pulsars.size(); i++) {
				ArrayList<Double> out = net.getOutput(pulsars.get(i).in);
				
				for(int s = 0; s < out.size(); s++) {
					averageCost+=NetworkUtils.cost(out.get(s), pulsars.get(i).out.get(s));
				}
			}
			
			averageCost/=costN;
			
			System.out.println("Cost: " + averageCost + " It: " + it);
			////
			
			net.resetGradients();

			for(int i = 0; i < nTrEx && index < pulsars.size(); i++, index++) {
				net.updateGradients(pulsars.get(index).in, pulsars.get(index).out);
			}

			net.updateWeightsAndBias(2.0);
			
			if(index >= pulsars.size()) {
				index = 0;
				Collections.shuffle(pulsars);
			}
		}
		
		net.toFile("pulsarRecog.txt");
		
		
		//TEST
		/*ArrayList<Pulsar> pulsars = loadPulsars(0, 20000, "C:\\Users\\pc\\Desktop\\NeuralNetwork\\pulsars\\HTRU_2.csv");

		NeuralNetwork net = new NeuralNetwork("pulsarRecog.txt");
		
		double right = 0;
		double wrong = 0;
		
		double missed = 0;

		for(int n = 0; n < pulsars.size(); n++) {
			ArrayList<Double> out = net.getOutput(pulsars.get(n).in);
			
			double v = out.get(0) >= 0.5 ? 1.0 : 0.0;
			
			if(v == pulsars.get(n).out.get(0)) {
				right++;
			}
			else {
				wrong++;
				
				if(pulsars.get(n).out.get(0) == 1.0) {
					missed++;
				}
			}
		}
		
		System.out.println("Accuracy: " + (right*100)/(right+wrong));
		System.out.println("Missed " + missed + " stars :(");*/
	}
}
