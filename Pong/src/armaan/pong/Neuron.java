package armaan.pong;

public class Neuron {

	private double sum;
	
	public double output (double[] input) {
		sum = 0;
		for (int i = 0; i < input.length; i++) {
			sum += input[i];
		}
		return 1 / (1 + Math.pow(2.7182818284590452353602874713527, -sum));
	}
	
	public double getSum() {
		return sum;
	}
}
