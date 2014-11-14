public class Node{

	private Node top;
	private Node bottom;
	private Node left;
	private Node right;
	private Node parentNode;
	private int g_distFromStart;
	private int h_heurDistFromGoal;
	private int x;
	private int y;
	private boolean isObstacle;
	private boolean isGoal;
	private boolean isStart;

	Node(int x, int y) {
		this.x = x;
		this.y = y;
		this.isObstacle = false;
		this.isGoal = false;
		this.isStart = false;
		this.g_distFromStart = Integer.MAX_VALUE;
	}

	Node(Node n) {
		this.x = n.x;
		this.y = n.y;
		this.top = n.top;
		this.bottom = n.bottom;
		this.left = n.left;
		this.right = n.right;
		this.parentNode = n.parentNode;
		this.g_distFromStart = n.g_distFromStart;
		this.isObstacle = n.isObstacle;
		this.isGoal = n.isGoal;
		this.isStart = n.isStart;
	}

	Node(int x, int y, boolean visited, int g_distFromStart,
			boolean isObstacle, boolean isGoal, boolean isStart) {
		this.x = x;
		this.y = y;
		this.g_distFromStart = g_distFromStart;
		this.isObstacle = isObstacle;
		this.isGoal = isGoal;
		this.isStart = isStart;
	}

	public Node getTop() {
		return top;
	}

	public Node getBottom() {
		return bottom;
	}

	public Node getLeft() {
		return left;
	}

	public Node getRight() {
		return right;
	}

	public void setTop(Node top) {
		this.top = top;
	}

	public void setBottom(Node bottom) {
		this.bottom = bottom;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public int getG_distFromStart() {
		return g_distFromStart;
	}

	public void setG_distFromStart(int g_distFromStart) {
		this.g_distFromStart = g_distFromStart;
	}

	public int getH_heurDistFromGoal() {
		return h_heurDistFromGoal;
	}

	public void setH_heurDistFromGoal(int h_heurDistFromGoal) {
		this.h_heurDistFromGoal = h_heurDistFromGoal;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isObstacle() {
		return isObstacle;
	}

	public void setObstacle(boolean isObstacle) {
		this.isObstacle = isObstacle;
	}

	public boolean isGoal() {
		return isGoal;
	}

	public void setGoal(boolean isGoal) {
		this.isGoal = isGoal;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	/*@Override
	public int compareTo(Node o) {
		int f_totalDist = this.g_distFromStart + this.h_heurDistFromGoal;
		int otherTotalDist = o.getG_distFromStart() + o.getH_heurDistFromGoal();

		if (f_totalDist < otherTotalDist) {
			return -1;
		} else if (f_totalDist > otherTotalDist) {
			return 1;
		} else {
			return 0;
		}
	}*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
