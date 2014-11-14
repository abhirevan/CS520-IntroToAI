import java.util.Comparator;

public class NodeComparatorSmallerG implements Comparator<Node> {

	@Override
	public int compare(Node o1, Node o2) {
		int f1_totalDist = o1.getG_distFromStart() + o1.getH_heurDistFromGoal();
		int f2_totalDist = o2.getG_distFromStart() + o2.getH_heurDistFromGoal();

		if (f1_totalDist < f2_totalDist) {
			return -1;
		} else if (f1_totalDist > f2_totalDist) {
			return 1;
		} else {
			if (o1.getG_distFromStart() < o2.getG_distFromStart()) {
				return 1;
			} else if(o1.getG_distFromStart() > o2.getG_distFromStart()) {
				return -1;
			}
			return 0;
		}

	}
}
