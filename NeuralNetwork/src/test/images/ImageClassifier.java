package test.images;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import network.NetworkUtils;
import network.NeuralNetwork;

public class ImageClassifier {
	public static final int types = 3;
	public static final int imagePixels = 28*28;

	public static ArrayList<Image> loadImages(int from, int to, String path, int value) throws IOException{
		Path fileLocation = Paths.get(path);
		byte[] data = Files.readAllBytes(fileLocation);
		
		ArrayList<Image> images = new ArrayList<>();
		
		int counter=0;
		
		int c=79+from*imagePixels;
		
		while(data.length-c >= imagePixels && counter < (to-from)) {
			ArrayList<Double> image = new ArrayList<>();
			
			for(int i = 0; i < imagePixels; i++) {
				image.add(Byte.toUnsignedInt(data[c])/255.0);
				c++;
			}

			images.add(new Image(image, value, types));
			counter++;
			
			if(counter % 1000 == 0) {
				System.out.println(counter);
			}
		}
		
		return images;
	}
	
	public static void main(String[] args) throws IOException {
		//TRAIN
		int from = 0;
		int to = from + 8000;
		
		ArrayList<Image> images = new ArrayList<>();
		images.addAll(loadImages(from, to, "C:\\Users\\pc\\Desktop\\NeuralNetwork\\quickdraw\\eiffeltower.npy", 0));
		images.addAll(loadImages(from, to, "C:\\Users\\pc\\Desktop\\NeuralNetwork\\quickdraw\\monalisa.npy", 1));
		images.addAll(loadImages(from, to, "C:\\Users\\pc\\Desktop\\NeuralNetwork\\quickdraw\\car.npy", 2));

		Collections.shuffle(images);

		NeuralNetwork net = new NeuralNetwork(new int[] {imagePixels, 50, 40, 20, types});
		//NeuralNetwork net = new NeuralNetwork("imageClass.txt");
		
		int nTrEx = 100;
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
		
		net.toFile("imageClass.txt");
		
		
		//TEST AND SEE
		
		/*int from = 10000;
		int to = from + 2000;

		ArrayList<Image> images = new ArrayList<>();
		images.addAll(loadImages(from, to, "C:\\Users\\pc\\Desktop\\NeuralNetwork\\quickdraw\\eiffeltower.npy", 0));
		images.addAll(loadImages(from, to, "C:\\Users\\pc\\Desktop\\NeuralNetwork\\quickdraw\\monalisa.npy", 1));
		images.addAll(loadImages(from, to, "C:\\Users\\pc\\Desktop\\NeuralNetwork\\quickdraw\\car.npy", 2));
		
		Collections.shuffle(images);

		NeuralNetwork net = new NeuralNetwork("imageClass.txt");

		for(int n = 0; n < 20; n++) {

			for(int r = 0; r < 28; r++) {
				for(int c = 0; c < 28; c++) {
					if(images.get(n).image.get(28*r+c) < 0.5) {
						System.out.print("  ");
					}
					else {
						System.out.print("o ");
					}
				}
				System.out.println();
			}


			ArrayList<Double> out = net.getOutput(images.get(n).image);
			
			
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
		
		//TEST %

		/*int from = 10000;
		int to = from + 2000;

		ArrayList<Image> images = new ArrayList<>();
		images.addAll(loadImages(from, to, "C:\\Users\\pc\\Desktop\\NeuralNetwork\\quickdraw\\eiffeltower.npy", 0));
		images.addAll(loadImages(from, to, "C:\\Users\\pc\\Desktop\\NeuralNetwork\\quickdraw\\monalisa.npy", 1));
		images.addAll(loadImages(from, to, "C:\\Users\\pc\\Desktop\\NeuralNetwork\\quickdraw\\car.npy", 2));

		Collections.shuffle(images);

		NeuralNetwork net = new NeuralNetwork("imageClass.txt");
		
		int total = 0;
		int right = 0;

		for(int n = 0; n < images.size(); n++) {
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
			
			if(real == max_i) {
				right++;
			}
			
			total++;
		}
		
		System.out.println("Right: " + right*100.0/total + "%");*/

	}
}
