package utils;

import java.io.Serializable;

public class NetworkUtils implements Serializable{
	public static double s(double x) {
		return 1.0/(1.0 + Math.exp(-x));
	}
	
	public static double ds(double s) {
		return s*(1-s);
	}
}
