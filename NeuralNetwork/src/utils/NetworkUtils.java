package utils;

import java.io.Serializable;

public class NetworkUtils implements Serializable{
	public static double sigmoid(double x) {
		return 1.0/(1.0 + Math.exp(-x));
	}
}
