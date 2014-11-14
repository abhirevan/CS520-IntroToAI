import java.io.IOException;

public class FlipGA {
	
	public static int flipCounter=0;
	
	public int[][] elitismStep(int[][] populationset, int[] evaluationResult, CnfExp cnfExpSet)
	{
		int[][] healthy2Samples= new int[2][cnfExpSet.numOfVariables];
		int high1;
		int high2;
		
		if(evaluationResult[0] > evaluationResult[1])
		{
			high1=0;
			high2=1;
		}
		else{
			high1=1;
			high2=0;
		}
		
		//finds the indices of top 2 healthiest population
		for(int i=2;i<10;i++)
		{
			if(evaluationResult[high2]<evaluationResult[i])
			{
				if(evaluationResult[high1]<evaluationResult[i])
				{
					high2=high1;
					high1=i;
				}
				else
				{
					high2=i;
				}
			}
		}
		
		//populate healthySamples
		healthy2Samples[0]=populationset[high1];
		healthy2Samples[1]=populationset[high2];
		
		System.out.println("Healthy Samples are at index: "+high1+" "+high2);
		
		return healthy2Samples;
	}
	
	public int[][] probabilitySelectionStep(int[][] initPopulationSet,int[] evaluationResult,CnfExp cnfExpSet)
	{
				//Evaluate sum f(n)
				double evaluationResultSum=0;
				for(int i=0;i<10;i++)
				{
					evaluationResultSum+=evaluationResult[i];
				}
				
				// Evaluate p_seq(i) = p_seq(i-1)+p(i)
				double[] probevaluationResult = new double[10];
				//double probevaluationResultSum=0;
				
				for(int i=0;i<10;i++)
				{
					probevaluationResult[i] = (double)evaluationResult[i]/evaluationResultSum;
					//probevaluationResultSum+=probevaluationResult[i];
				}
				
				//System.out.println("P(n): "+probevaluationResultSum);
				//printDouble1DArray(probevaluationResult, 10);
				
				//Generate 8 random numbers & select 8 from population set
				//double[] random8 = new double[8];
				int[] probSelectionIndex = new int[8];
				for(int i =0;i<8;i++)
				{
					double randomNum=Math.random();
					//System.out.println("Random: "+randomNum);
					double pr_sum = 0;
					
					for(int j=0;j<10;j++)
					{
						pr_sum+=probevaluationResult[j];
						
						if(pr_sum>=randomNum)
						{
							probSelectionIndex[i]=j;
							break;
						}
						
					}
				}
				
				System.out.println("Indices after probabilistic selection: ");
				Utility.printInt1DArray(probSelectionIndex, 8);
				
				//Populate probabilistic selected samples
				int[][] probSelectedSamples= new int[8][cnfExpSet.numOfVariables];
				
				for(int i =0 ;i<8;i++)
				{
					probSelectedSamples[i]= initPopulationSet[probSelectionIndex[i]];
				}
				
				System.out.println("Probabilistic selected samples: ");
				Utility.printInt2DArray(probSelectedSamples,8,cnfExpSet.numOfVariables);
				
				return probSelectedSamples;
	}
	
	public int[][] uniformCrossOverStep(int[][] probSelectedSamples,CnfExp cnfExpSet)
	{
		int[][] crossOverSamples=new int[8][cnfExpSet.numOfVariables];
	
		int xIndex=0;
		int yIndex=1;
		
		for(int count =0;count<4;count++)
		{
			for(int bit=0;bit<cnfExpSet.numOfVariables;bit++)
			{
				if(Utility.randomNumGeneratorProbability(0.5))
				{
					crossOverSamples[xIndex][bit]=probSelectedSamples[xIndex][bit];
					crossOverSamples[yIndex][bit]=probSelectedSamples[yIndex][bit];
					
				}
				else
				{
					crossOverSamples[yIndex][bit]=probSelectedSamples[xIndex][bit];
					crossOverSamples[xIndex][bit]=probSelectedSamples[yIndex][bit];
				}
			}
			
			//System.out.println("probSelectedSamples");
			//Utility.printInt1DArray(probSelectedSamples[xIndex], cnfExpSet.numOfVariables);
			//Utility.printInt1DArray(probSelectedSamples[yIndex], cnfExpSet.numOfVariables);
			//System.out.println("crossOverSamples");
			//Utility.printInt1DArray(crossOverSamples[xIndex], cnfExpSet.numOfVariables);
			//Utility.printInt1DArray(crossOverSamples[yIndex], cnfExpSet.numOfVariables);
			
			xIndex+=2;
			yIndex+=2;
		}
		
		System.out.println("crossOverSamples");
		Utility.printInt2DArray(crossOverSamples, 8, cnfExpSet.numOfVariables);
		
		return crossOverSamples;
		
	}

	public int[][] disruptiveMutationStep(int[][] crossOverSamples,CnfExp cnfExpSet)
	{
		int[][] disruptiveMutationSamples = new int[8][cnfExpSet.numOfVariables];
		
		for(int i =0;i<8;i++)
		{
			if(Utility.randomNumGeneratorProbability(0.9))// String is mutated with probabiltiy 0.9
			{
				for(int bit=0;bit<cnfExpSet.numOfVariables;bit++)
				{
					if(Utility.randomNumGeneratorProbability(0.5))//Bit is flipped with probability 0.5
					{
						disruptiveMutationSamples[i][bit] = -1*crossOverSamples[i][bit];
						//Incrementing flips due to Disruptive mutation
						flipCounter++;
					}
					else{
						disruptiveMutationSamples[i][bit] = crossOverSamples[i][bit];
					}
				}
			}
			else
			{
				disruptiveMutationSamples[i]=crossOverSamples[i];
			}
		}
		
		//System.out.println("crossOverSamples");
		//Utility.printInt2DArray(crossOverSamples, 8, cnfExpSet.numOfVariables);
		System.out.println("disruptiveMutationSamples");
		Utility.printInt2DArray(disruptiveMutationSamples, 8, cnfExpSet.numOfVariables);
		
		return disruptiveMutationSamples;
	}
	
	public int[] flipHeuristicStepPerState(int [] state,CnfExp cnfExpSet)
	{
		int[] initState = state;
		
		int[] tempState = initState;
		
		boolean continueFlip=true;
		
		System.out.println("Initial: ");
		Utility.printInt1DArray(state, state.length);

		while(continueFlip)
		{
			//Calculate initHeath
			int[] initResult= cnfExpSet.evaluateCNFClauses(initState);
			int initHealth = cnfExpSet.evaluationFunction(initResult);
			
			//Randlomly generating bitseq
			int[] shuffleBitSequence= new int[cnfExpSet.numOfVariables];
					
			for(int i=0; i< cnfExpSet.numOfVariables; i++)
			{
				shuffleBitSequence[i]=i;
			}
			
			int tempHealth= initHealth;
			tempState = initState;
			
			//Flip bit sequence and check if health increases
			for(int i =0 ;i <shuffleBitSequence.length;i++)
			{
				int currBit = shuffleBitSequence[i];
				//Flip bit
				
				int rollBackValue = tempState[currBit];
				tempState[currBit] = -1*tempState[currBit];

				//Evaluate the state with the flipped bit
				int[] CNFResult= cnfExpSet.evaluateCNFClauses(tempState);
				if(tempHealth <= cnfExpSet.evaluationFunction(CNFResult)) //This means flip is accepted
				{
					//initState[currBit]= tempState[currBit];
					tempHealth = cnfExpSet.evaluationFunction(CNFResult);
					
					//Incrementing flips due to flip Heuristics
					flipCounter++;
				}
				else{//Flip is discarded
					tempState[currBit]=rollBackValue;
				}
			}
			
			System.out.println("initHealth"+initHealth);
			System.out.println("tempHealth"+tempHealth);
			
			
			if(initHealth < tempHealth)
			{
				initHealth = tempHealth;
				initState = tempState;
				
			}
			else
			{
				continueFlip = false;
			}
				
		}
		
		System.out.println("Final: ");
		Utility.printInt1DArray(initState, initState.length);
		
		return initState;
		
		
	}
	
	
	public int[][] flipHeuristicStep(int[][] disruptiveMutationSamples,CnfExp cnfExpSet)
	{
		int[][] flipHeuristicSamples = new int[8][cnfExpSet.numOfVariables];
		for(int i =0 ;i < 8;i++)
		{
			System.out.println("flipHeuristicStepPerState:"+ i);
			flipHeuristicSamples[i]=flipHeuristicStepPerState(disruptiveMutationSamples[i], cnfExpSet);
		}
		
		System.out.println("Flip Heuristic Step");
		
		Utility.printInt2DArray(flipHeuristicSamples, 8, cnfExpSet.numOfVariables);
		
		return flipHeuristicSamples;
	}
	
	public int[] evaluateHealth(int[][] population,int numSamples,CnfExp cnfExpSet)
	{
		int[] evaluationResult= new int[numSamples];
		for(int i =0;i<numSamples;i++)
		{
			int[] CNFResult= cnfExpSet.evaluateCNFClauses(population[i]);
			evaluationResult[i] = cnfExpSet.evaluationFunction(CNFResult);
			//System.out.println("Evaluation function is "+i+": " +evaluationResult[i]+" out of "+cnfExpSet.numOfClauses);
		}
		
		return evaluationResult;
	}
	
	public void printHealth(int[] evaluationResult,int numSamples,CnfExp cnfExpSet)
	{
		
		for(int i =0;i < numSamples;i++)
		{
			System.out.println("Evaluation function is "+i+": " +evaluationResult[i]+" out of "+cnfExpSet.numOfClauses);
		}
	}
	
	public boolean checkIfGoalStateMet(int[][] population,int numSamples,CnfExp cnfExpSet)
	{
		//Check Evaluate 
		int[] evaluationResult=evaluateHealth(population,numSamples,cnfExpSet);
		
		for(int i =0;i<numSamples;i++)
		{
			if(evaluationResult[i]==cnfExpSet.numOfClauses)
			{
				System.out.println("Goal state met at index :"+i);
				
				Utility.printInt1DArray(population[i], population[i].length);
				return true;
			}
		}
		
		System.out.println("Goal state not met");
		return false;
	}
	
	
	
	
	public int[][] flipGAIteration(int[][] initPopulationSet,CnfExp cnfExpSet)
	{
		// Calculate F(n) for input population
		int[] initEvaluationResult = this.evaluateHealth(initPopulationSet, 10, cnfExpSet);		
		System.out.println("Health: Initial population set");
		this.printHealth(initEvaluationResult, 10, cnfExpSet);
		
		//Elitism step- 10 samples -> 2 healthiest samples out
		int[][] healthy2Samples= this.elitismStep(initPopulationSet, initEvaluationResult, cnfExpSet);
		
		//Probability selection step take 10 samples -> 8 samples
		int[][] probSelectedSamples= this.probabilitySelectionStep(initPopulationSet, initEvaluationResult, cnfExpSet);
		
		//Crossover selection step-> 8 samples -> 8 samples
		int[][] crossOverSamples= this.uniformCrossOverStep(probSelectedSamples, cnfExpSet);
				
		//Disruptive Mutation samples -> 8 samples -> 8 samples		
		int[][] disruptiveMutationSamples = this.disruptiveMutationStep(crossOverSamples, cnfExpSet);

		//System.out.println("Health: disruptiveMutationSamples ");
				
		//int[] distruptiveMutationSamplesHealth= this.evaluateHealth(disruptiveMutationSamples, 8, cnfExpSet);
				
		//this.printHealth(distruptiveMutationSamplesHealth, 8, cnfExpSet);
				
		//Flip Heuristics -> 8 samples -> 8 samples
		int[][] flipHeuristicSamples = this.flipHeuristicStep(disruptiveMutationSamples, cnfExpSet);

		//System.out.println("Health: flipHeuristicSamples ");
				
		//int[] flipHeuristicSamplesHealth=this.evaluateHealth(flipHeuristicSamples, 8, cnfExpSet);
		//this.printHealth(flipHeuristicSamplesHealth, 8, cnfExpSet);
		
		//Return 2 healtiest samples + 8 flip Heauristic samples -> 10 samples
		
		int[][] flipGAOutput = new int[10][cnfExpSet.numOfVariables];
		
		flipGAOutput[0]=healthy2Samples[0];
		flipGAOutput[1]=healthy2Samples[1];
		
		for(int i=0;i<8;i++)
		{
			flipGAOutput[i+2]=flipHeuristicSamples[i];
		}
		
		System.out.println("Health: Final flipGAIter ");
		
		int[] flipGAOutputSamplesHealth=this.evaluateHealth(flipGAOutput, 10, cnfExpSet);
		this.printHealth(flipGAOutputSamplesHealth, 8, cnfExpSet);
		
		return flipGAOutput;
				
	}
	
	public static void main(String[] args) throws IOException {
		
		FlipGA flipGAInstance = new FlipGA();
		CnfExp cnfExpSet = new CnfExp();
		
		String fileName= System.getProperty("user.dir")+"/"+"ip.cnf";
		
		int[][] clauseExp = cnfExpSet.readFile(fileName);
		
		//populate CNF exp
		cnfExpSet.initCNFRef();
		cnfExpSet.setCnfRef(clauseExp);
		
		int[][] initPopulationSet = cnfExpSet.prepareVariableValueSet();
		System.out.println("Initial Populated samples: ");
		Utility.printInt2DArray(initPopulationSet,10,cnfExpSet.numOfVariables);
		
		int[][] iterInitPopulationSet = initPopulationSet;
		
		boolean checkIfGoalMet= false;
		int flipGAiterCount = 1;
		
		while(!checkIfGoalMet)
		{
			System.out.println("FlipGA at iteration:"+flipGAiterCount);
			
			//Goal can be met if any of initial population has 100% health
			checkIfGoalMet = flipGAInstance.checkIfGoalStateMet(iterInitPopulationSet, 10, cnfExpSet);
			
			if(checkIfGoalMet)
			{
				break;
			}
			
			iterInitPopulationSet= flipGAInstance.flipGAIteration(iterInitPopulationSet, cnfExpSet);
			
			//Goal can be met if after 1 iteration of FlipGA either of the states has 100% health
			checkIfGoalMet = flipGAInstance.checkIfGoalStateMet(iterInitPopulationSet, 10, cnfExpSet);
			
			flipGAiterCount++;
		}
		
		System.out.println("Number of flips required to meetgoal "+flipCounter);
		
	}
	
}
