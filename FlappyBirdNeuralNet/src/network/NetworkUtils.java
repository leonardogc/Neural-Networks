package network;

import java.io.Serializable;

public class NetworkUtils implements Serializable{
	public static double s(double x) {
		return 1.0/(1.0 + Math.exp(-x));
	}
	
	public static double ds(double x) {
		return Math.exp(-x)/Math.pow(1 + Math.exp(-x), 2);
	}
	
	public static double cost(double out, double expected) {
		return Math.pow(out-expected, 2);
	}
	
	public static double dCost(double out, double expected) {
		return 2*(out-expected);
	}
}
