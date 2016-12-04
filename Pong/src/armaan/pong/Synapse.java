package armaan.pong;

public class Synapse {
	private double weight = Math.random();
	
	public void adjustWeight(double adjustment) {
		weight += adjustment;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double newWeight) {
		weight = newWeight;
	}
	public double getOutput (double input) {
		return input * weight;
	}
}
