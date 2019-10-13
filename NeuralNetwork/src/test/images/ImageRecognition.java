package test.images;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import network.NetworkUtils;
import network.NeuralNetwork;

public class ImageRecognition {
	
	public static ArrayList<Image> loadImages(int from, int to, String path) throws IOException{
		String line="";
		String[] sLine;

		File file = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		for(int i = 0; i < from; i++) {
			br.readLine();
		}
		
		ArrayList<Image> images = new ArrayList<>();
		
		int counter=0;
		
		while((line = br.readLine()) != null && counter < (to-from)) {
			sLine = line.split(",");
			
			int value = Integer.parseInt(sLine[0]);
			
			ArrayList<Double> image = new ArrayList<>();
			
			for(int i = 1; i < sLine.length; i++) {
				image.add(Double.parseDouble(sLine[i])/255.0);
			}

			images.add(new Image(image, value, 10));
			counter++;
			
			if(counter % 1000 == 0) {
				System.out.println(counter);
			}
		}
		
		br.close();
		
		return images;
	}
	
	public static void main(String[] args) throws IOException {
		//TRAIN
		ArrayList<Image> images = loadImages(0, 60000, "C:\\Users\\Leonardo Capozzi\\Documents\\GitHub\\Neural-Networks\\NeuralNetwork\\src\\test\\data\\mnist_train.csv");

		Collections.shuffle(images);

		NeuralNetwork net = new NeuralNetwork(new int[] {28*28, 16, 16, 10});
		//NeuralNetwork net = new NeuralNetwork("imageRecog.txt");
		
		int nTrEx = 200;
		int index = 0;
		
		int costN = 200;

		for(int it = 0; it < 2000; it++) {
			//calculate cost
			
			double averageCost = 0;
			
			for(int i = 0; i < costN && i < images.size(); i++) {
				ArrayList<Double> out = net.getOutput(images.get(i).image);
				
				for(int s = 0; s < out.size(); s++) {
					averageCost+=NetworkUtils.cost(out.get(s), images.get(i).value.get(s));
				}
			}
			
			averageCost/=costN;
			
			System.out.println("Cost: " + averageCost + " It: " + it);
			////
			
			net.resetGradients();

			for(int i = 0; i < nTrEx && index < images.size(); i++, index++) {
				net.updateGradients(images.get(index).image, images.get(index).value);
			}

			net.updateWeightsAndBias(4.0);
			
			if(index >= images.size()) {
				index = 0;
				Collections.shuffle(images);
			}
		}
		
		net.toFile("imageRecog.txt");
		
		
		//TEST

		/*ArrayList<Image> images = loadImages(0, 10000, "C:\\Users\\Leonardo Capozzi\\Documents\\GitHub\\Neural-Networks\\NeuralNetwork\\src\\test\\data\\mnist_test.csv");

		NeuralNetwork net = new NeuralNetwork("imageRecog.txt");

		double total = 0;
		double right = 0;
		
		for(int n = 0; n < 10000; n++) {

			ArrayList<Double> out = net.getOutput(images.get(n).image);
			
			
			double max=-1;
			int max_i=-1;
			
			for(int i = 0; i < out.size(); i++) {
				if(out.get(i) > max) {
					max = out.get(i);
					max_i = i;
				}
			}
			
			int real = -1;
			
			for(int i2 = 0; i2 < images.get(n).value.size(); i2++) {
				if(images.get(n).value.get(i2) == 1.0) {
					real = i2;
					break;
				}
			}
			
			if(max_i == real) {
				right++;
			}
			
			total++;
		}

		System.out.println("Accuracy: " + right*100/total + "%");*/
	}
}
