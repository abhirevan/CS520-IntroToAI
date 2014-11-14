import java.awt.BorderLayout;
import java.awt.Dimension;
//import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AdaptiveAStarTester {

	private final int mapWidth = 101;
	private final int mapHeight = 101;
	private static int visitedCount = 0;
	private static List<Node> visitedNodes = new ArrayList<Node>();

	public int manhattanDist(Node src, Node dest) {
		return ((Math.abs(dest.getX() - src.getX()) + (Math.abs(dest.getY()
				- src.getY()))));
	}

	public Node initiateNode(Node n, String s) {
		if (s.equalsIgnoreCase("start")) {
			n.setStart(true);
			n.setG_distFromStart(0);
		} else if (s.equals("goal")) {
			n.setGoal(true);
		}
		return n;
	}

	public List<Node> getNeighbors(Node n, Node[][] map) {
		List<Node> neighbors = new ArrayList<Node>();
		int x = n.getX();
		int y = n.getY();
		if (!((x - 1) < 0)) {
			n.setLeft(map[x - 1][y]);
			neighbors.add(n.getLeft());
		}
		if (!((y - 1) < 0)) {
			n.setTop(map[x][y - 1]);
			neighbors.add(n.getTop());
		}
		if (!((x + 1) > (mapWidth - 1))) {
			n.setRight(map[x + 1][y]);
			neighbors.add(n.getRight());
		}
		if (!((y + 1) > (mapHeight - 1))) {
			n.setBottom(map[x][y + 1]);
			neighbors.add(n.getBottom());
		}
		return neighbors;
	}

	public Node[][] initializeHeuristicDistance(Node[][] map, Node startNode,
			Node goalNode) {
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				Node temp = map[i][j];
				// temp.setG_distFromStart(manhattanDist(startNode, temp));
				temp.setH_heurDistFromGoal(manhattanDist(temp, goalNode));
				map[i][j] = temp;
			}
		}
		return map;
	}

	public boolean constructPath(Node[][] map, Node startNode, Node goalNode)
			throws AStarException {
		Node currentNode = new Node(-1, -1);
		List<Node> visited = new ArrayList<Node>();
		// PathNodeList pathSet = new PathNodeList();
		Comparator<Node> comparator = new NodeComparatorLargerG();
		PriorityQueue<Node> pathSet = new PriorityQueue<Node>(101, comparator);
		pathSet.add(startNode);
		// pathSet.insert(startNode);
		List<Node> neighbors = new ArrayList<Node>();
		while (pathSet.size() > 0) {
			currentNode = pathSet.remove();
			if (currentNode.equals(goalNode)) {
				return true;
			}
			visited.add(currentNode);
			visitedCount++;
			neighbors = getNeighbors(currentNode, map);
			int g_dist_neighbor = 0;
			for (Node x : neighbors) {
				if (visited.contains(x) || x.isObstacle()) {
					continue;
				} else {
					g_dist_neighbor = currentNode.getG_distFromStart() + 1;
					if (!pathSet.contains(x)
							|| g_dist_neighbor < x.getG_distFromStart()) {
						x.setG_distFromStart(g_dist_neighbor);
						x.setParentNode(currentNode);
						if (pathSet.contains(x)) {
							pathSet.remove(x);
						}
						pathSet.add(x);
					}
				}
			}
		}
		throw new AStarException("A Path could not be found");
		// return false;
	}

	public Node[][] incrementalMap(Node[][] map, Node[][] completeMap,
			Node currentNode) {
		/*
		 * map[currentNode.getX()][currentNode.getY()] = completeMap[currentNode
		 * .getX()][currentNode.getY()];
		 */
		List<Node> neighbors = getNeighbors(currentNode, completeMap);
		for (Node neighbor : neighbors) {
			if (neighbor.isObstacle()) {
				map[neighbor.getX()][neighbor.getY()].setObstacle(true);
			}
		}
		return map;
	}

	public Node[][] goalAwareMap(Node[][] map, Node[][] completeMap,
			Node goalNoad) {
		map[goalNoad.getX()][goalNoad.getY()] = completeMap[goalNoad.getX()][goalNoad
				.getY()];
		List<Node> neighbors = getNeighbors(goalNoad, completeMap);
		for (Node neighbor : neighbors) {
			if (neighbor.isObstacle()) {
				map[neighbor.getX()][neighbor.getY()].setObstacle(true);
			}
		}
		return map;
	}

	public Node[][] initializeG_Distance(Node[][] map, Node startNode) {
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				Node temp = map[i][j];
				// temp.setG_distFromStart(manhattanDist(startNode, temp));
				temp.setG_distFromStart(Integer.MAX_VALUE);
				map[i][j] = temp;
			}
		}
		map[startNode.getX()][startNode.getY()].setG_distFromStart(0);
		return map;
	}

	public List<Node> displayPath(Node[][] map, Node startNode, Node goalNode) {
		List<Node> path = new ArrayList<Node>();
		List<Node> drawPath = new ArrayList<Node>();
		startNode = map[startNode.getX()][startNode.getY()];
		goalNode = map[goalNode.getX()][goalNode.getY()];
		Node n = new Node(goalNode);
		path.add(n);
		while (!n.equals(startNode)) {
			n = new Node(
					map[n.getParentNode().getX()][n.getParentNode().getY()]);
			path.add(n);
			if (!n.equals(startNode) && !n.equals(goalNode)) {
				drawPath.add(n);
			}
		}
		return drawPath;
	}

	public Stack<Node> traceBackPath(Node[][] map, Node startNode, Node goalNode) {
		Node n = goalNode;
		Stack<Node> drawPath = new Stack<Node>();
		startNode = map[startNode.getX()][startNode.getY()];
		goalNode = map[goalNode.getX()][goalNode.getY()];
		while (!n.equals(startNode)) {
			n = n.getParentNode();
			if (!n.equals(startNode) && !n.equals(goalNode)) {
				drawPath.push(n);
			}
		}
		return drawPath;
	}

	public Node[][] cleanseMap(Node[][] map) {
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				if (visitedNodes.contains(map[i][j])) {
					Node tempNode = new Node(visitedNodes.get(visitedNodes
							.indexOf(map[i][j])));
					map[i][j] = tempNode;
				}
			}
		}
		return map;
	}

	public Node[][] updateAdaptiveHeuristics(Node[][] map, int pathCost) {
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				if (map[i][j].getG_distFromStart() != Integer.MAX_VALUE) {
					int newHeuristics = pathCost
							- map[i][j].getG_distFromStart();
					map[i][j].setH_heurDistFromGoal(newHeuristics);
				}
			}
		}

		return map;
	}

	public List<Node> callAdaptiveAStar(Node[][] gridMap, Node startNode,
			Node goalNode) throws AStarException {
		MazeCreator_DFS mazeCreator = new MazeCreator_DFS();
		AdaptiveAStarTester adapAStar = new AdaptiveAStarTester();
		Node[][] incrementalMap = mazeCreator.initializeMap();
		Node tempStartNode = new Node(-1, -1);
		startNode = adapAStar.initiateNode(startNode, "start");
		incrementalMap[startNode.getX()][startNode.getY()] = startNode;
		startNode.setH_heurDistFromGoal(adapAStar.manhattanDist(startNode,
				goalNode));
		gridMap = adapAStar.initializeHeuristicDistance(gridMap, startNode,
				goalNode);
		goalNode = adapAStar.initiateNode(goalNode, "goal");
		incrementalMap[goalNode.getX()][goalNode.getY()] = goalNode;
		incrementalMap = adapAStar.incrementalMap(incrementalMap, gridMap,
				startNode);

		incrementalMap = adapAStar.initializeHeuristicDistance(incrementalMap,
				startNode, goalNode);
		boolean pathExists;
		List<Node> drawPath = new ArrayList<Node>();
		Stack<Node> pathStack = new Stack<Node>();
		tempStartNode = startNode;
		while (!tempStartNode.equals(goalNode.getParentNode())) {
			pathExists = adapAStar.constructPath(incrementalMap, tempStartNode,
					goalNode);
			if (pathExists) {

				drawPath = adapAStar.displayPath(incrementalMap, tempStartNode,
						goalNode);

				pathStack = adapAStar.traceBackPath(incrementalMap,
						tempStartNode, goalNode);

				int pathCost = 0;

				if (drawPath.size() > 0) {
					pathCost = drawPath.size() + 1;
				}

				incrementalMap = adapAStar.updateAdaptiveHeuristics(
						incrementalMap, pathCost);

				while (!pathStack.empty()) {
					Node n = pathStack.pop();
					incrementalMap = adapAStar.incrementalMap(incrementalMap,
							gridMap, n);
					if (!n.isObstacle()) {

						tempStartNode = new Node(n);
						adapAStar.visitedNodes.add(tempStartNode);
					} else {
						tempStartNode = new Node(
								adapAStar.visitedNodes
										.get(adapAStar.visitedNodes.indexOf(n
												.getParentNode())));
						tempStartNode.setH_heurDistFromGoal(pathCost
								- tempStartNode.getG_distFromStart());
						incrementalMap[tempStartNode.getX()][tempStartNode
								.getY()] = tempStartNode;
						break;
					}
				}
			}
			incrementalMap = adapAStar.initializeG_Distance(incrementalMap,
					tempStartNode);
		}
		incrementalMap = adapAStar.cleanseMap(incrementalMap);
		drawPath = adapAStar.displayPath(incrementalMap, startNode, goalNode);
		System.out.println("Adaptive A* for S(" + startNode.getX() + "," + ""
				+ startNode.getY() + ") -> G(" + goalNode.getX() + ","
				+ goalNode.getY() + ")");
		int size = drawPath.size() + 1;
		System.out.println("Path Length :" + size);
		System.out.println("Visited nodes: " + visitedCount);
		return drawPath;
	}

	public static void main(String args[]) {
		MazeCreator_DFS mazeCreator = new MazeCreator_DFS();
		AdaptiveAStarTester repAStar = new AdaptiveAStarTester();
		Node[][] gridMap = mazeCreator.initializeMap();
		gridMap = mazeCreator.createMap(gridMap);
		boolean startFound = false;
		boolean goalFound = false;
		Node tempStartNode = new Node(-1, -1);
		Node tempGoalNode = new Node(-1, -1);
		Node startNode = new Node(-1, -1);
		Node goalNode = new Node(-1, -1);
		JPanel container = new JPanel();
		DrawMaze drawMaze = new DrawMaze();
		JFrame f = new JFrame("Maze");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JScrollPane scrollPane = new JScrollPane(drawMaze);
		scrollPane.setPreferredSize(new Dimension(1010, 650));
		container.add(scrollPane, BorderLayout.CENTER);
		while (!startFound) {
			tempStartNode = mazeCreator.generateRandomNode(repAStar.mapWidth,
					repAStar.mapHeight);
			startNode = gridMap[tempStartNode.getX()][tempStartNode.getY()];
			if (startNode.isObstacle() || startNode.isGoal()) {
				startFound = false;
			} else {
				startFound = true;
			}
		}
		while (!goalFound) {
			tempGoalNode = mazeCreator.generateRandomNode(repAStar.mapWidth,
					repAStar.mapHeight);
			goalNode = gridMap[tempGoalNode.getX()][tempGoalNode.getY()];
			if (goalNode.isObstacle() || goalNode.isStart()) {
				goalFound = false;
			} else {
				goalFound = true;
			}
		}
		System.out.println("Start Node..." + startNode.getX() + ","
				+ startNode.getY());
		System.out.println("Stop Node..." + goalNode.getX() + ","
				+ goalNode.getY());
		List<Node> drawPath = new ArrayList<Node>();
		try {
			drawPath = repAStar.callAdaptiveAStar(gridMap, startNode, goalNode);
		} catch (AStarException a) {
			System.out.println(a.getMessage());
			drawPath = new ArrayList<Node>();
		}
		System.out.println();
		drawMaze.startNode(startNode.getX(), startNode.getY());
		drawMaze.goalNode(goalNode.getX(), goalNode.getY());
		for (int i = 0; i < repAStar.mapWidth; i++) {
			for (int j = 0; j < repAStar.mapHeight; j++) {
				if (gridMap[i][j].isObstacle()) {
					drawMaze.fillCell(i, j);
				}
			}
		}

		for (Node pathNode : drawPath) {
			drawMaze.drawPath(pathNode.getX(), pathNode.getY());
		}
		f.add(container);
		f.pack();
		f.setVisible(true);
	}
}
