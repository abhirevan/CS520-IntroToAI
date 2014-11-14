
public class Assignment1Client {

	private final int mapWidth = 101;
	private final int mapHeight = 101;

	public static void main(String[] args) {

		MazeCreator_DFS mazeCreator = new MazeCreator_DFS();
		Assignment1Client a = new Assignment1Client();
		RepeatedAStarGreaterGTester repAStar = new RepeatedAStarGreaterGTester();
		RepeatedAStarSmallerGTester repAStarSmallG = new RepeatedAStarSmallerGTester();
		BackRepeatedAStarTester backAStar = new BackRepeatedAStarTester();
		AdaptiveAStarTester adapAStar = new AdaptiveAStarTester();
		Node[][] gridMap = mazeCreator.initializeMap();
		gridMap = mazeCreator.createMap(gridMap);
		try {
			boolean startFound = false;
			boolean goalFound = false;
			Node tempStartNode = new Node(-1, -1);
			Node tempGoalNode = new Node(-1, -1);
			Node startNode = new Node(-1, -1);
			Node goalNode = new Node(-1, -1);

			while (!startFound) {
				tempStartNode = mazeCreator.generateRandomNode(a.mapWidth,
						a.mapHeight);
				startNode = gridMap[tempStartNode.getX()][tempStartNode.getY()];
				if (startNode.isObstacle() || startNode.isGoal()) {
					startFound = false;
				} else {
					startFound = true;
				}
			}

			while (!goalFound) {
				tempGoalNode = mazeCreator.generateRandomNode(a.mapWidth,
						a.mapHeight);
				goalNode = gridMap[tempGoalNode.getX()][tempGoalNode.getY()];
				if (goalNode.isObstacle() || goalNode.isStart()) {
					goalFound = false;
				} else {
					goalFound = true;
				}
			}
			repAStar.callRepeatedAStarLargeG(gridMap, startNode, goalNode);
			repAStarSmallG
					.callRepeatedAStarSmallG(gridMap, startNode, goalNode);
			backAStar.callBackRepeatedAStarLargeG(gridMap, startNode, goalNode);
			adapAStar.callAdaptiveAStar(gridMap, startNode, goalNode);
		} catch (AStarException exception) {
			System.out.println(exception.getMessage());
		} catch (NumberFormatException numExc) {
			System.out.println("Invalid Input");
		}
	}

}
