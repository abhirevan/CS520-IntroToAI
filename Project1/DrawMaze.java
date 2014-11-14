import java.awt.*;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class DrawMaze extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5202421742451581814L;
	Node[][] rect;
	private List<Point> fillCells;
	private Point startNode;
	private Point goalNode;
	private List<Point> drawPath;

	public Node[][] getRect() {
		return rect;
	}

	public void setRect(Node[][] rect) {
		this.rect = rect;
	}

	public DrawMaze(Node[][] map) {
		fillCells = new ArrayList<Point>();
		startNode = new Point(-1, -1);
		goalNode = new Point(-1, -1);
		drawPath = new ArrayList<Point>();
	}

	public DrawMaze() {
		setBorder(BorderFactory.createLineBorder(Color.RED));
		fillCells = new ArrayList<Point>();
		startNode = new Point(-1, -1);
		goalNode = new Point(-1, -1);
		drawPath = new ArrayList<Point>();
	}

	public Dimension getPreferredSize() {
		// return new Dimension(1010, 1010);
		return new Dimension(1010, 1010);
	}

	public void fillCell(int x, int y) {
		fillCells.add(new Point(x, y));
	}

	public void drawPath(int x, int y) {
		drawPath.add(new Point(x, y));
	}

	public void startNode(int x, int y) {
		startNode = new Point(x, y);
	}

	public void goalNode(int x, int y) {
		goalNode = new Point(x, y);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// for (int i = 0; i < 1010; i = i + 10) {
		// for (int j = 0; j < 1010; j = j + 10) {
		/*
		 * for (int i = 0; i < 50; i = i + 10) { for (int j = 0; j < 50; j = j +
		 * 10) { g.drawRect(i, j, 10, 10); } }
		 */
		for (Point fillCell : fillCells) {
			int cellX = 10 + (fillCell.x * 10);
			int cellY = 10 + (fillCell.y * 10);
			g.setColor(Color.RED);
			g.fillRect(cellY, cellX, 10, 10);
		}

		for (Point draw : drawPath) {
			int cellX = 10 + (draw.x * 10);
			int cellY = 10 + (draw.y * 10);
			g.setColor(Color.BLUE);
			g.fillRect(cellY, cellX, 10, 10);
		}

		if (!startNode.equals(new Point(-1, -1))) {
			g.setColor(Color.YELLOW);
			int startNode_cellX = 10 + (startNode.x * 10);
			int startNode_cellY = 10 + (startNode.y * 10);
			g.fillRect(startNode_cellY, startNode_cellX, 10, 10);
		}

		if (!goalNode.equals(new Point(-1, -1))) {
			g.setColor(Color.BLACK);
			int goalNode_cellX = 10 + (goalNode.x * 10);
			int goalNode_cellY = 10 + (goalNode.y * 10);
			g.fillRect(goalNode_cellY, goalNode_cellX, 10, 10);
		}

		g.setColor(Color.BLACK);
		g.drawRect(10, 10, 1010, 1010);
		for (int i = 10; i <= 1010; i += 10) {
			g.drawLine(i, 10, i, 1010);
		}

		for (int i = 10; i <= 1010; i += 10) {
			g.drawLine(10, i, 1010, i);
		}

	}
}
