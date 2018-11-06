import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class SubTesting {
	private double delta = 0.0000001;
	@Test
	public void test() {
		
		ArrayList<Job> j = new ArrayList<Job>();
		/*	Job 9: has value 29 and deadline 10
			Job 4: has value 24 and deadline 12
			Job 2: has value 21 and deadline 0
			Decay factor: 0.804
		 * */
		Job a = new Job(9, 29, 10);
		Job b = new Job(4, 24, 12);
		Job c = new Job(2, 21, 0);
		Job d = new Job(8, 23, 2);
		//d should not switch
		//at the end of shiftDown, should have a->1, b->2
		j.add(a);
		j.add(b);
		j.add(c);
		j.add(d);
		Scheduling test = new Scheduling(j, 4, 0.804);
		int[] matchedEdges = new int[4];
		matchedEdges[0] = 9;
		matchedEdges[1] = 4;
		matchedEdges[2] = 8;
		
		a.matchedSlot = 0;
		b.matchedSlot = 1;
		d.matchedSlot = 2;
		double testVal = test.shiftDown(a, 3, matchedEdges);

		double expect = test.weight(a, 1) + test.weight(b, 3) + test.weight(d, 2);
		assertTrue(testVal <= expect + delta && testVal >= expect - delta);
	}

}
