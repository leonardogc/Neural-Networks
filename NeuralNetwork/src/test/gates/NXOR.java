package test.gates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import network.NeuralNetwork;
import utils.NetworkUtils;

public class NXOR {
	public static void main(String[] args) {
		/*NeuralNetwork net = new NeuralNetwork("nxor.txt");
		
		ArrayList<Double> in = new ArrayList<>();
		in.add(1.0);
		in.add(1.0);
		
		ArrayList<Double> out = net.getOutput(in);
		
		for(int i = 0; i < out.size(); i++) {
			System.out.println(out.get(i));
		}*/
		
		NeuralNetwork net = new NeuralNetwork(new int[] {2, 4, 1});
		
		ArrayList<ArrayList<Double>> list = new ArrayList<>();
		
		ArrayList<Double> c00_i = new ArrayList<>();
		ArrayList<Double> c00_o = new ArrayList<>();
		
		ArrayList<Double> c01_i = new ArrayList<>();
		ArrayList<Double> c01_o = new ArrayList<>();
		
		ArrayList<Double> c10_i = new ArrayList<>();
		ArrayList<Double> c10_o = new ArrayList<>();
		
		ArrayList<Double> c11_i = new ArrayList<>();
		ArrayList<Double> c11_o = new ArrayList<>();
		
		c00_i.add(0.0);
		c00_i.add(0.0);
		c00_o.add(1.0);
		
		c01_i.add(0.0);
		c01_i.add(1.0);
		c01_o.add(0.0);
		
		c10_i.add(1.0);
		c10_i.add(0.0);
		c10_o.add(0.0);
		
		c11_i.add(1.0);
		c11_i.add(1.0);
		c11_o.add(1.0);
		
		list.add(c00_i);
		list.add(c00_o);
		
		list.add(c01_i);
		list.add(c01_o);
		
		list.add(c10_i);
		list.add(c10_o);
		
		list.add(c11_i);
		list.add(c11_o);
		
		Random r = new Random();

		int nTrEx = 8;

		for(int it = 0; it < 10000; it++) {
			//calculate cost
			
			double averageCost = 0;
			
			for(int i = 0; i < nTrEx; i++) {
				int n = 2*r.nextInt(4);

				ArrayList<Double> out = net.getOutput(list.get(n));
				
				for(int s = 0; s < out.size(); s++) {
					averageCost+=NetworkUtils.cost(out.get(s), list.get(n+1).get(s));
				}
			}
			
			averageCost/=nTrEx;
			
			System.out.println("Cost: " + averageCost);
			//
			
			net.resetGradients();

			for(int i = 0; i < nTrEx; i++) {
				int n = 2*r.nextInt(4);

				net.updateGradients(list.get(n), list.get(n+1));
			}

			net.updateWeightsAndBias(4.0);
		}

		try {
			net.toFile("nxor.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
