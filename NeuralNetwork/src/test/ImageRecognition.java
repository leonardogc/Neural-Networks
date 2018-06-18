package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import network.NeuralNetwork;
import utils.NetworkUtils;

public class ImageRecognition {
	public ArrayList<Double> image;
	public ArrayList<Double> value;
	
	public ImageRecognition(ArrayList<Double> image, int value) {
		this.image = image;
		this.value = new ArrayList<>();
		
		for(int i = 0; i < 10; i++) {
			if(i == value) {
				this.value.add(1.0);
			}
			else {
				this.value.add(0.0);
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		//TRAIN
		String line="";
		String[] sLine;

		File file = new File("C:\\Users\\pc\\Desktop\\NeuralNetwork\\mnist_train.csv");
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		ArrayList<ImageRecognition> images = new ArrayList<>();
		
		int counter=0;
		
		while((line = br.readLine()) != null && counter < 30000) {
			sLine = line.split(",");
			
			int value = Integer.parseInt(sLine[0]);
			
			ArrayList<Double> image = new ArrayList<>();
			
			for(int i = 1; i < sLine.length; i++) {
				image.add(Double.parseDouble(sLine[i])/255.0);
			}

			images.add(new ImageRecognition(image, value));
			counter++;
			
			if(counter % 1000 == 0) {
				System.out.println(counter);
			}
		}

		Collections.shuffle(images);

		NeuralNetwork net = new NeuralNetwork(new int[] {28*28, 16, 16, 10});
		//NeuralNetwork net = new NeuralNetwork("imageRecog.txt");
		
		int nTrEx = 100;
		int index = 0;

		for(int it = 0; it < 2000; it++) {
			//calculate cost
			
			double averageCost = 0;
			int index2 = index;
			
			for(int i = 0; i < nTrEx && index2 < images.size(); i++, index2++) {
				ArrayList<Double> out = net.feedForward(images.get(index2).image);
				
				for(int s = 0; s < out.size(); s++) {
					averageCost+=NetworkUtils.cost(out.get(s), images.get(index2).value.get(s));
				}
			}
			
			averageCost/=nTrEx;
			
			System.out.println("Cost: " + averageCost + " It: " + it);
			//
			
			net.resetGradients();

			for(int i = 0; i < nTrEx && index < images.size(); i++, index++) {
				net.updateGradients(images.get(index).image, images.get(index).value);
			}

			net.updateWeightsAndBias(4.0, nTrEx);
			
			if(index >= images.size()) {
				index = 0;
				Collections.shuffle(images);
			}
		}
		
		net.toFile("imageRecog.txt");
		
		
		//TEST
		
		/*String line="";
		String[] sLine;

		File file = new File("C:\\Users\\pc\\Desktop\\NeuralNetwork\\mnist_test.csv");
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		ArrayList<ImageRecognition> images = new ArrayList<>();
		
		int counter=0;
		
		while((line = br.readLine()) != null && counter < 200) {
			sLine = line.split(",");
			
			int value = Integer.parseInt(sLine[0]);
			
			ArrayList<Double> image = new ArrayList<>();
			
			for(int i = 1; i < sLine.length; i++) {
				image.add(Double.parseDouble(sLine[i])/255.0);
			}

			images.add(new ImageRecognition(image, value));
			counter++;
		}

		NeuralNetwork net = new NeuralNetwork("imageRecog.txt");

		for(int n = 0; n < 20; n++) {

			for(int r = 0; r < 28; r++) {
				for(int c = 0; c < 28; c++) {
					if(images.get(n).image.get(28*r+c) == 0) {
						System.out.print("  ");
					}
					else {
						System.out.print("o ");
					}
				}
				System.out.println();
			}


			ArrayList<Double> out = net.feedForward(images.get(n).image);
			
			
			double max=-1;
			int max_i=-1;
			
			for(int i = 0; i < out.size(); i++) {
				if(out.get(i) > max) {
					max = out.get(i);
					max_i = i;
				}
			}
			
			System.out.println("Net: " + max_i);
			
			for(int i2 = 0; i2 < images.get(n).value.size(); i2++) {
				if(images.get(n).value.get(i2) == 1.0) {
					System.out.println("Real: " + i2);
					break;
				}
			}
		}*/


	}
}
