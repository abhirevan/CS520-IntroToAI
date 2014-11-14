import java.io.IOException;
import java.util.*;

import javax.swing.JFrame;

public class FlipGAClient {

	public static int flipCounter = 0;
	public final static int fileNum = 25;

	public FlipGAClient() {
		flipCounter = 0;
	}

	public int[][] elitismStep(int[][] populationset, int[] evaluationResult,
			CnfExp cnfExpSet) {
		int[][] healthy2Samples = new int[2][cnfExpSet.numOfVariables];
		int high1;
		int high2;

		if (evaluationResult[0] > evaluationResult[1]) {
			high1 = 0;
			high2 = 1;
		} else {
			high1 = 1;
			high2 = 0;
		}

		// finds the indices of top 2 healthiest population
		for (int i = 2; i < 10; i++) {
			if (evaluationResult[high2] < evaluationResult[i]) {
				if (evaluationResult[high1] < evaluationResult[i]) {
					high2 = high1;
					high1 = i;
				} else {
					high2 = i;
				}
			}
		}

		// populate healthySamples
		healthy2Samples[0] = populationset[high1];
		healthy2Samples[1] = populationset[high2];

		// System.out.println("Healthy Samples are at index: "+high1+" "+high2);

		return healthy2Samples;
	}

	public int[][] probabilitySelectionStep(int[][] initPopulationSet,
			int[] evaluationResult, CnfExp cnfExpSet) {
		// Evaluate sum f(n)
		double evaluationResultSum = 0;
		for (int i = 0; i < 10; i++) {
			evaluationResultSum += evaluationResult[i];
		}

		// Evaluate p_seq(i) = p_seq(i-1)+p(i)
		double[] probevaluationResult = new double[10];
		// double probevaluationResultSum=0;

		for (int i = 0; i < 10; i++) {
			probevaluationResult[i] = (double) evaluationResult[i]
					/ evaluationResultSum;
			// probevaluationResultSum+=probevaluationResult[i];
		}

		// System.out.println("P(n): "+probevaluationResultSum);
		// printDouble1DArray(probevaluationResult, 10);

		// Generate 8 random numbers & select 8 from population set
		// double[] random8 = new double[8];
		int[] probSelectionIndex = new int[8];
		for (int i = 0; i < 8; i++) {
			double randomNum = Math.random();
			// System.out.println("Random: "+randomNum);
			double pr_sum = 0;

			for (int j = 0; j < 10; j++) {
				pr_sum += probevaluationResult[j];

				if (pr_sum >= randomNum) {
					probSelectionIndex[i] = j;
					break;
				}

			}
		}

		// System.out.println("Indices after probabilistic selection: ");
		// Utility.printInt1DArray(probSelectionIndex, 8);

		// Populate probabilistic selected samples
		int[][] probSelectedSamples = new int[8][cnfExpSet.numOfVariables];

		for (int i = 0; i < 8; i++) {
			probSelectedSamples[i] = initPopulationSet[probSelectionIndex[i]];
		}

		// System.out.println("Probabilistic selected samples: ");
		// Utility.printInt2DArray(probSelectedSamples,8,cnfExpSet.numOfVariables);

		return probSelectedSamples;
	}

	public int[][] uniformCrossOverStep(int[][] probSelectedSamples,
			CnfExp cnfExpSet) {
		int[][] crossOverSamples = new int[8][cnfExpSet.numOfVariables];

		int xIndex = 0;
		int yIndex = 1;

		for (int count = 0; count < 4; count++) {
			for (int bit = 0; bit < cnfExpSet.numOfVariables; bit++) {
				if (Utility.randomNumGeneratorProbability(0.5)) {
					crossOverSamples[xIndex][bit] = probSelectedSamples[xIndex][bit];
					crossOverSamples[yIndex][bit] = probSelectedSamples[yIndex][bit];

				} else {
					crossOverSamples[yIndex][bit] = probSelectedSamples[xIndex][bit];
					crossOverSamples[xIndex][bit] = probSelectedSamples[yIndex][bit];
				}
			}

			// System.out.println("probSelectedSamples");
			// Utility.printInt1DArray(probSelectedSamples[xIndex],
			// cnfExpSet.numOfVariables);
			// Utility.printInt1DArray(probSelectedSamples[yIndex],
			// cnfExpSet.numOfVariables);
			// System.out.println("crossOverSamples");
			// Utility.printInt1DArray(crossOverSamples[xIndex],
			// cnfExpSet.numOfVariables);
			// Utility.printInt1DArray(crossOverSamples[yIndex],
			// cnfExpSet.numOfVariables);

			xIndex += 2;
			yIndex += 2;
		}

		// System.out.println("crossOverSamples");
		// Utility.printInt2DArray(crossOverSamples, 8,
		// cnfExpSet.numOfVariables);

		return crossOverSamples;

	}

	public int[][] disruptiveMutationStep(int[][] crossOverSamples,
			CnfExp cnfExpSet) {
		int[][] disruptiveMutationSamples = new int[8][cnfExpSet.numOfVariables];

		for (int i = 0; i < 8; i++) {
			if (Utility.randomNumGeneratorProbability(0.9))// String is mutated
															// with probabiltiy
															// 0.9
			{
				for (int bit = 0; bit < cnfExpSet.numOfVariables; bit++) {
					if (Utility.randomNumGeneratorProbability(0.5))// Bit is
																	// flipped
																	// with
																	// probability
																	// 0.5
					{
						disruptiveMutationSamples[i][bit] = -1
								* crossOverSamples[i][bit];
						// Incrementing flips due to Disruptive mutation
						flipCounter++;
					} else {
						disruptiveMutationSamples[i][bit] = crossOverSamples[i][bit];
					}
				}
			} else {
				disruptiveMutationSamples[i] = crossOverSamples[i];
			}
		}

		// System.out.println("crossOverSamples");
		// Utility.printInt2DArray(crossOverSamples, 8,
		// cnfExpSet.numOfVariables);
		// System.out.println("disruptiveMutationSamples");
		// Utility.printInt2DArray(disruptiveMutationSamples, 8,
		// cnfExpSet.numOfVariables);

		return disruptiveMutationSamples;
	}

	public int[] flipHeuristicStepPerState(int[] state, CnfExp cnfExpSet) {
		int[] initState = state;

		int[] tempState = initState;

		boolean continueFlip = true;

		// System.out.println("Initial: ");
		// Utility.printInt1DArray(state, state.length);

		while (continueFlip) {
			// Calculate initHeath
			int[] initResult = cnfExpSet.evaluateCNFClauses(initState);
			int initHealth = cnfExpSet.evaluationFunction(initResult);

			// Randlomly generating bitseq
			int[] shuffleBitSequence = new int[cnfExpSet.numOfVariables];

			for (int i = 0; i < cnfExpSet.numOfVariables; i++) {
				shuffleBitSequence[i] = i;
			}

			int tempHealth = initHealth;
			tempState = initState;

			// Flip bit sequence and check if health increases
			for (int i = 0; i < shuffleBitSequence.length; i++) {
				int currBit = shuffleBitSequence[i];
				// Flip bit

				int rollBackValue = tempState[currBit];
				tempState[currBit] = -1 * tempState[currBit];

				// Evaluate the state with the flipped bit
				int[] CNFResult = cnfExpSet.evaluateCNFClauses(tempState);
				if (tempHealth <= cnfExpSet.evaluationFunction(CNFResult)) // This
																			// means
																			// flip
																			// is
																			// accepted
				{
					// initState[currBit]= tempState[currBit];
					tempHealth = cnfExpSet.evaluationFunction(CNFResult);

					// Incrementing flips due to flip Heuristics
					flipCounter++;
				} else {// Flip is discarded
					tempState[currBit] = rollBackValue;
				}
			}

			// System.out.println("initHealth"+initHealth);
			// System.out.println("tempHealth"+tempHealth);

			if (initHealth < tempHealth) {
				initHealth = tempHealth;
				initState = tempState;

			} else {
				continueFlip = false;
			}

		}

		// System.out.println("Final: ");
		// Utility.printInt1DArray(initState, initState.length);

		return initState;

	}

	public int[][] flipHeuristicStep(int[][] disruptiveMutationSamples,
			CnfExp cnfExpSet) {
		int[][] flipHeuristicSamples = new int[8][cnfExpSet.numOfVariables];
		for (int i = 0; i < 8; i++) {
			// System.out.println("flipHeuristicStepPerState:"+ i);
			flipHeuristicSamples[i] = this.flipHeuristicStepPerState(
					disruptiveMutationSamples[i], cnfExpSet);
		}

		// System.out.println("Flip Heuristic Step");

		// Utility.printInt2DArray(flipHeuristicSamples, 8,
		// cnfExpSet.numOfVariables);

		return flipHeuristicSamples;
	}

	public int[] evaluateHealth(int[][] population, int numSamples,
			CnfExp cnfExpSet) {
		int[] evaluationResult = new int[numSamples];
		for (int i = 0; i < numSamples; i++) {
			int[] CNFResult = cnfExpSet.evaluateCNFClauses(population[i]);
			evaluationResult[i] = cnfExpSet.evaluationFunction(CNFResult);
			// System.out.println("Evaluation function is "+i+": "
			// +evaluationResult[i]+" out of "+cnfExpSet.numOfClauses);
		}

		return evaluationResult;
	}

	public void printHealth(int[] evaluationResult, int numSamples,
			CnfExp cnfExpSet) {

		for (int i = 0; i < numSamples; i++) {
			// System.out.println("Evaluation function is "+i+": "
			// +evaluationResult[i]+" out of "+cnfExpSet.numOfClauses);
		}
	}

	public boolean checkIfGoalStateMet(int[][] population, int numSamples,
			CnfExp cnfExpSet) {
		// Check Evaluate
		int[] evaluationResult = evaluateHealth(population, numSamples,
				cnfExpSet);

		for (int i = 0; i < numSamples; i++) {
			if (evaluationResult[i] == cnfExpSet.numOfClauses) {
				// System.out.println("Goal state met at index :"+i);

				// Utility.printInt1DArray(population[i], population[i].length);
				return true;
			}
		}

		// System.out.println("Goal state not met");
		return false;
	}

	public int[][] flipGAIteration(int[][] initPopulationSet, CnfExp cnfExpSet) {
		// Calculate F(n) for input population
		int[] initEvaluationResult = this.evaluateHealth(initPopulationSet, 10,
				cnfExpSet);
		// System.out.println("Health: Initial population set");
		this.printHealth(initEvaluationResult, 10, cnfExpSet);

		// Elitism step- 10 samples -> 2 healthiest samples out
		int[][] healthy2Samples = this.elitismStep(initPopulationSet,
				initEvaluationResult, cnfExpSet);

		// Probability selection step take 10 samples -> 8 samples
		int[][] probSelectedSamples = this.probabilitySelectionStep(
				initPopulationSet, initEvaluationResult, cnfExpSet);

		// Crossover selection step-> 8 samples -> 8 samples
		int[][] crossOverSamples = this.uniformCrossOverStep(
				probSelectedSamples, cnfExpSet);

		// Disruptive Mutation samples -> 8 samples -> 8 samples
		int[][] disruptiveMutationSamples = this.disruptiveMutationStep(
				crossOverSamples, cnfExpSet);

		// System.out.println("Health: disruptiveMutationSamples ");

		// int[] distruptiveMutationSamplesHealth=
		// this.evaluateHealth(disruptiveMutationSamples, 8, cnfExpSet);

		// this.printHealth(distruptiveMutationSamplesHealth, 8, cnfExpSet);

		// Flip Heuristics -> 8 samples -> 8 samples
		int[][] flipHeuristicSamples = this.flipHeuristicStep(
				disruptiveMutationSamples, cnfExpSet);

		// System.out.println("Health: flipHeuristicSamples ");

		// int[]
		// flipHeuristicSamplesHealth=this.evaluateHealth(flipHeuristicSamples,
		// 8, cnfExpSet);
		// this.printHealth(flipHeuristicSamplesHealth, 8, cnfExpSet);

		// Return 2 healtiest samples + 8 flip Heauristic samples -> 10 samples

		int[][] flipGAOutput = new int[10][cnfExpSet.numOfVariables];

		flipGAOutput[0] = healthy2Samples[0];
		flipGAOutput[1] = healthy2Samples[1];

		for (int i = 0; i < 8; i++) {
			flipGAOutput[i + 2] = flipHeuristicSamples[i];
		}

		// System.out.println("Health: Final flipGAIter ");

		int[] flipGAOutputSamplesHealth = this.evaluateHealth(flipGAOutput, 10,
				cnfExpSet);
		this.printHealth(flipGAOutputSamplesHealth, 8, cnfExpSet);

		return flipGAOutput;

	}

	public static long[] callFlipGAAlgo(String filename) throws IOException {
		long[] output = new long[2];

		FlipGAClient flipGAInstance = new FlipGAClient();
		CnfExp cnfExpSet = new CnfExp();

		// String fileName= System.getProperty("user.dir")+"/"+"ip.cnf";

		int[][] clauseExp = cnfExpSet.readFile(filename);

		// populate CNF exp
		cnfExpSet.initCNFRef();
		cnfExpSet.setCnfRef(clauseExp);

		int[][] initPopulationSet = cnfExpSet.prepareVariableValueSet();
		// System.out.println("Initial Populated samples: ");
		// Utility.printInt2DArray(initPopulationSet,10,cnfExpSet.numOfVariables);

		int[][] iterInitPopulationSet = initPopulationSet;

		boolean checkIfGoalMet = false;
		int flipGAiterCount = 1;

		// Measure running time: Start stopwatch

		long startTime = System.currentTimeMillis();

		while (!checkIfGoalMet) {
			// System.out.println("FlipGA at iteration:"+flipGAiterCount);

			// Goal can be met if any of initial population has 100% health
			checkIfGoalMet = flipGAInstance.checkIfGoalStateMet(
					iterInitPopulationSet, 10, cnfExpSet);

			if (checkIfGoalMet) {
				break;
			}

			iterInitPopulationSet = flipGAInstance.flipGAIteration(
					iterInitPopulationSet, cnfExpSet);

			// Goal can be met if after 1 iteration of FlipGA either of the
			// states has 100% health
			checkIfGoalMet = flipGAInstance.checkIfGoalStateMet(
					iterInitPopulationSet, 10, cnfExpSet);

			flipGAiterCount++;
		}

		long stopTime = System.currentTimeMillis();

		// this stores the running time
		output[0] = stopTime - startTime;

		// This stores the Flips required to meet the goal
		output[1] = flipCounter;

		return output;
	}

	public static void main(String[] args) throws IOException {
		String u20Folder = System.getProperty("user.dir") + "/input/"
				+ "uf20-91";
		String u50Folder = System.getProperty("user.dir") + "/input/"
				+ "uf50-218";
		String u75Folder = System.getProperty("user.dir") + "/input/"
				+ "UF75-325";
		String u100Folder = System.getProperty("user.dir") + "/input/"
				+ "uf100-430";

		int[][] flipCounter = new int[4][fileNum];
		long[][] runningTime = new long[4][fileNum];
		int[] avgBitFlips = new int[4];
		int[] medianRunningTime = new int[4];
		int tempSum = 0;

		// Evaluate u20 folder
		for (int i = 1; i <= fileNum; i++) {

			String file = "/uf20-0" + i + ".cnf";
			String fileName = u20Folder + file;
			System.out.print("File:" + file + " read with");
			long[] output = callFlipGAAlgo(fileName);
			System.out.print(" with number of flips: " + output[1]
					+ " and running time(ms) " + output[0]);
			flipCounter[0][i - 1] = (int) output[1];
			runningTime[0][i - 1] = output[0];
			tempSum += output[1];
			System.out.println();
		}
		// Eval avg for u20
		avgBitFlips[0] = tempSum / fileNum;

		// Evaluate u50 folder
		tempSum = 0;
		for (int i = 1; i <= fileNum; i++) {
			String file = "/uf50-0" + i + ".cnf";
			String fileName = u50Folder + file;
			System.out.print("File:" + file + " read with");
			long[] output = callFlipGAAlgo(fileName);
			System.out.print(" with number of flips: " + output[1]
					+ " and running time(ms) " + output[0]);
			flipCounter[1][i - 1] = (int) output[1];
			runningTime[1][i - 1] = output[0];
			tempSum += output[1];
			System.out.println();
		}
		// Eval avg for u50
		avgBitFlips[1] = tempSum / fileNum;

		// Evaluate u75 folder
		tempSum = 0;
		for (int i = 1; i <= fileNum; i++) {
			String file = "/uf75-0" + i + ".cnf";
			String fileName = u75Folder + file;
			System.out.print("File:" + file + " read with");
			long[] output = callFlipGAAlgo(fileName);
			System.out.print(" with number of flips: " + output[1]
					+ " and running time(ms) " + output[0]);
			flipCounter[2][i - 1] = (int) output[1];
			runningTime[2][i - 1] = output[0];
			tempSum += (int) output[1];
			System.out.println();
		}
		// Eval avg for u75
		avgBitFlips[2] = tempSum / fileNum;

		// Evaluate u100 folder
		tempSum = 0;
		for (int i = 1; i <= fileNum; i++) {
			String file = "/uf100-0" + i + ".cnf";
			String fileName = u100Folder + file;
			System.out.print("File:" + file + " read with");
			long[] output = callFlipGAAlgo(fileName);
			System.out.print(" with number of flips: " + output[1]
					+ " and running time(ms) " + output[0]);
			flipCounter[3][i - 1] = (int) output[1];
			runningTime[3][i - 1] = output[0];
			tempSum += (int) output[1];
			System.out.println();
		}
		// Eval avg for u100
		avgBitFlips[3] = tempSum / fileNum;

		System.out.print("Average bitflips for 20,50,75,100 are ");
		Utility.printInt1DArray(avgBitFlips, 4);

		// Sort running time
		for (int i = 0; i < 4; i++) {
			runningTime[i] = Utility.heapSort(runningTime[i], fileNum);
			medianRunningTime[i] = (int)runningTime[i][fileNum / 2];
		}
		
		

		System.out.print("Median running time for 20,50,75,100 are "
				+ runningTime[0][fileNum / 2] + " "
				+ runningTime[1][fileNum / 2] + " "
				+ runningTime[2][fileNum / 2] + " "
				+ runningTime[3][fileNum / 2]);
		
		
		
		
		JFrame f = new JFrame("Average bit flips");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new GraphingData(avgBitFlips));
        f.setSize(600,600);
        f.setLocation(200,200);
        f.setVisible(true);
        
        
    	JFrame l = new JFrame("Median running time");
        l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        l.add(new GraphingData(medianRunningTime));
        l.setSize(600,600);
        l.setLocation(200,200);
        l.setVisible(true);

	}
}
