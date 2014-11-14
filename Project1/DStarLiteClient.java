import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class DStarLiteClient {
	public List<Node> callDStarLite(Node[][] grid,Node startNode, Node goalNode){
		DStarLite dlite = new DStarLite();
		dlite.init(startNode.getX(), startNode.getY(), goalNode.getX(), goalNode.getY());
		for(int i=0;i<101;i++){
			for(int j=0;j<101;j++){
				if(grid[i][j].isObstacle()){
					dlite.updateCell(i, j, -1);
				}
			}
		}
		List<Node> path = new ArrayList<Node>();
		dlite.replan();
		List<State> states = dlite.getPath();
		for(State x:states){
			path.add(new Node(x.x,x.y));
		}
		System.out.println("Path Length: "+path.size());
		return path;
	}
	
	public static void main(String args[]) {
		MazeCreator_DFS mazeCreator = new MazeCreator_DFS();
		RepeatedAStarGreaterGTester repAStar = new RepeatedAStarGreaterGTester();
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
			tempStartNode = mazeCreator.generateRandomNode(101,101);
			startNode = gridMap[tempStartNode.getX()][tempStartNode.getY()];
			if (startNode.isObstacle() || startNode.isGoal()) {
				startFound = false;
			} else {
				startFound = true;
			}
		}
		while (!goalFound) {
			tempGoalNode = mazeCreator.generateRandomNode(101,101);
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
			drawPath = repAStar.callRepeatedAStarLargeG(gridMap, startNode,
					goalNode);
		} catch (AStarException a) {
			System.out.println(a.getMessage());
			drawPath = new ArrayList<Node>();
		}
		System.out.println();
		drawMaze.startNode(startNode.getX(), startNode.getY());
		drawMaze.goalNode(goalNode.getX(), goalNode.getY());
		for (int i = 0; i < 101; i++) {
			for (int j = 0; j < 101; j++) {
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
