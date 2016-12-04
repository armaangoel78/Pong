package armaan.pong;

import java.util.Scanner;

public class AI {
	
	private Neuron[][] neurons = {
			{new Neuron(), new Neuron(), new Neuron()},
			{new Neuron(), new Neuron()},
			{new Neuron()}
			};
	
	private Synapse[][][] synapses = {
			{{new Synapse(), new Synapse()}, {new Synapse(), new Synapse()}, {new Synapse(), new Synapse()}},
			{{new Synapse(), new Synapse(), new Synapse()}, {new Synapse(), new Synapse(), new Synapse()}},
			{{new Synapse(), new Synapse()}}
	};
	
	private double data[][][] = {
			{{1, 0}, {0}},
			{{0, 0}, {.5}},
			{{0, 1}, {1}}
			};
	private double learningRate = 1;
	public double[][] outputs = {{0, 0, 0},
			  				      {0, 0},
			                      {0}};
	public double error;
	
	/*public void run () {
		int epoch = 0;
		while (true) {
			for (int i = 0; i < data.length; i++ ) {
				forewardProp(data[i][0]);
				//System.out.println(neurons[2][0].getSum());
				backProp(i, data[i][0]);
				if (epoch % 1 == 0) System.out.println(errors[i]);
			}
			epoch++;
			
			if (checkError(.01)) break;
		}
		
		while (true) {
			Scanner s = new Scanner(System.in);
			double[] inputs = {s.nextDouble(), s.nextDouble()};
			forewardProp(inputs);
			System.out.println(outputs[2][0]);
		}
		
		
	}*/
	
	public void update(double input[]) {
		forewardProp(input);
		backProp(input);
	}
	
	private void changeOutputWeights() {
		for (int i = 0; i < synapses[2][0].length; i++) {
			double adjustment = getSigmoidDeriv(neurons[2][0].getSum()) * 
					outputs[1][i] *
					error *
					learningRate;
			synapses[2][0][i].adjustWeight(adjustment);
			if (Double.isNaN(synapses[2][0][i].getWeight())) synapses[2][0][i].setWeight(Math.random());
		}
	}
	
	private void changeHidden1Weights() {
		for (int i = 0; i < synapses[1].length; i ++) {
			for (int x = 0; x < synapses[1][i].length; x++) {
				double adjustment = getSigmoidDeriv(neurons[2][0].getSum()) * 
						synapses[2][0][i].getWeight() *
						getSigmoidDeriv(neurons[1][i].getSum()) *
						outputs[0][x] * 
						error *
						learningRate;
				synapses[1][i][x].adjustWeight(adjustment);
				if (Double.isNaN(synapses[1][i][x].getWeight())) synapses[1][i][x].setWeight(Math.random());

			}
		}
	}
	
	private void changeHidden0Weights(double[] input) {
		for (int i = 0; i < synapses[0].length; i ++) {
			for (int x = 0; x < synapses[0][i].length; x++) {
				double adjustment = getSigmoidDeriv(neurons[2][0].getSum()) * 
						synapses[2][0][x].getWeight() *
						getSigmoidDeriv(neurons[1][x].getSum()) *
						synapses[1][x][i].getWeight() * 
						getSigmoidDeriv(neurons[0][x].getSum()) *
						input[x] *
						error *
						learningRate;
				synapses[0][i][x].adjustWeight(adjustment);
				if (Double.isNaN(synapses[0][i][x].getWeight())) synapses[0][i][x].setWeight(Math.random());
			}
		}
	}
	
	private void backProp(double input[]) {
		error = (input[0] > input[1] ? 0 : (input[0] < input[1] ? 1 : .5)) - outputs[2][0];
		changeOutputWeights();
		changeHidden0Weights(input);
		changeHidden1Weights();
	}
	public void forewardProp(double inputs[]) {
		for (int i = 0; i < outputs.length; i++) {
			for (int x = 0; x < outputs[i].length; x++) {
				double weightedInputs[] = new double[synapses[i][x].length];
				for (int y = 0; y < weightedInputs.length; y++) {
					weightedInputs[y] = synapses[i][x][y].getOutput(inputs[y]);
				}
				outputs[i][x] = neurons[i][x].output(weightedInputs);
			}
			inputs = outputs[i];
		}
	}
	
	private double getSigmoidDeriv(double input) { //derivative of sigmoid/logistic
		double numerator = Math.pow(2.7182818284590452353602874713527, input);
		double denomantor = Math.pow((numerator + 1), 2);
		
		return numerator/denomantor;
	}
	private boolean checkError(double margin) {
		return error - margin < 0;
	}
}