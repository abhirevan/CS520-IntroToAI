import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;



public class MazeCreator_DFS {
	private final int mapWidth = 101;
	private final int mapHeight = 101;
	private Node[][] map;
	private static List<Node> visited = new ArrayList<Node>();
	private static Stack<Node> nodeStack = new Stack<Node>();

	public Node[][] getMap() {
		return map;
	}

	public void setMap(Node[][] map) {
		this.map = map;
	}

	public List<Node> getVisited() {
		return visited;
	}

	public void setVisited(List<Node> visited) {
		this.visited = visited;
	}

	public int randomGenerator(int n) {
		int randomNum = (int) (Math.random() * n);
		return randomNum;
	}

	public Node[][] initializeMap() {
		Node[][] map = new Node[mapHeight][mapWidth];
		for (int i = 0; i < this.mapHeight; i++) {
			for (int j = 0; j < this.mapWidth; j++) {
				Node n = new Node(i, j);
				map[i][j] = n;
			}
		}
		return map;
	}

	public Node generateRandomNode(int gridWidth, int gridHeight) {
		int x_coord = (int) Math.floor(Math.random() * gridWidth);
		int y_coord = (int) Math.floor(Math.random() * gridHeight);
		Node n = new Node(x_coord, y_coord);
		return n;
	}

	public int probabilityMarkBlocked() {
		int randomNum = (int) (Math.random() * 10);
		if (randomNum < 3)
			return 1;
		return 0;
	}

	public Node[][] createMap(Node[][] map) {
		Node rndmNode = generateRandomNode(mapWidth, mapHeight);
		while (visited.size() < ((mapWidth) * (mapHeight))) {
			int x_val = rndmNode.getX();
			int y_val = rndmNode.getY();
			if (!visited.contains(map[x_val][y_val])) {
				nodeStack.push(map[x_val][y_val]);
				// visited.size() < ((mapWidth - 1) * (mapHeight - 1))
				while (!nodeStack.empty()) {
					map = mazeCreator(map, new Node(-1, -1));
				}
			}
		}
		return map;
	}

	public Node[][] mazeCreator(Node[][] map, Node initialNode) {
		if (nodeStack.size() > 0) {
			Node n = nodeStack.pop();
			if (!initialNode.equals(new Node(-1, -1))) {
				n.setParentNode(initialNode);
			} else {
				n.setParentNode(new Node(-1, -1));
			}
			int x = n.getX();
			int y = n.getY();
			if (y + 1 < mapWidth) {
				int x_right = x;
				int y_right = y + 1;
				Node n_right = map[x_right][y_right];
				n.setRight(n_right);
			}
			if (x - 1 >= 0) {
				int x_top = x - 1;
				int y_top = y;
				Node n_top = map[x_top][y_top];
				n.setTop(n_top);
			}
			if (y - 1 >= 0) {
				int x_left = x;
				int y_left = y - 1;
				Node n_left = map[x_left][y_left];
				n.setLeft(n_left);
			}
			if (x + 1 < mapHeight) {
				int x_bottom = x + 1;
				int y_bottom = y;
				Node n_bottom = map[x_bottom][y_bottom];
				n.setBottom(n_bottom);
			}
			map[n.getX()][n.getY()] = n;
			if (!visited.contains(n)) {
				visited.add(n);
				int probBlocked = probabilityMarkBlocked();
				if (probBlocked == 0) {
					n.setObstacle(false);
				} else {
					n.setObstacle(true);
				}
				map[n.getX()][n.getY()] = n;
			}
			List<Node> tempList = new ArrayList<Node>();
			int count = 0;
			if (n.getTop() != null && !visited.contains(n.getTop())) {
				tempList.add(n.getTop());
				count++;
			}
			if (n.getBottom() != null && !visited.contains(n.getBottom())) {
				tempList.add(n.getBottom());
				count++;
			}
			if (n.getRight() != null && !visited.contains(n.getRight())) {
				tempList.add(n.getRight());
				count++;
			}
			if (n.getLeft() != null && !visited.contains(n.getLeft())) {
				tempList.add(n.getLeft());
				count++;
			}
			for (int i = count - 1; i >= 0; i--) {
				int randomIndex = randomGenerator(i);
				nodeStack.push(tempList.get(randomIndex));
				tempList.remove(randomIndex);
			}
		}
		return map;
	}

	public static void main(String args[]) {
		MazeCreator_DFS mazeCreator = new MazeCreator_DFS();
		Node[][] map = mazeCreator.initializeMap();
		Node n = null;
		BufferedWriter out = null;
		map = mazeCreator.createMap(map);
		// System.out.println(visited.size());
		JFrame f = new JFrame("Maze");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DrawMaze drawMaze = new DrawMaze();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for (int i = 0; i < mazeCreator.mapWidth; i++) {
			for (int j = 0; j < mazeCreator.mapHeight; j++) {
				System.out.print((map[i][j]).getX() + ","
						+ (map[i][j]).getY() + (map[i][j]).isObstacle()
						+ "     ");
				if (map[i][j].isObstacle()) {
					drawMaze.fillCell(i, j);
				}
			}
			System.out.println();
		}
		//f.add(drawMaze);
		JPanel container = new JPanel();
		JScrollPane scrollPane = new JScrollPane(drawMaze);
		scrollPane.setPreferredSize(new Dimension(1010, 650));
		container.add(scrollPane, BorderLayout.CENTER);
		
		f.add(container);
		f.pack();
		f.setVisible(true);

	}
}
