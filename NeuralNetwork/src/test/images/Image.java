package test.images;

import java.util.ArrayList;

public class Image {
	public ArrayList<Double> image;
	public ArrayList<Double> value;
	
	public Image(ArrayList<Double> image, int value, int size) {
		this.image = image;
		this.value = new ArrayList<>();
		
		for(int i = 0; i < size; i++) {
			if(i == value) {
				this.value.add(1.0);
			}
			else {
				this.value.add(0.0);
			}
		}
		
		this.image.trimToSize();
		this.value.trimToSize();
	}
}
