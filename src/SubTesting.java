import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class SubTesting {

	@Test
	public void test() {
		ArrayList<Job> j = new ArrayList<Job>();
		/*	Job 9: has value 29 and deadline 10
			Job 4: has value 24 and deadline 12
			Job 2: has value 21 and deadline 0
			Decay factor: 0.804
		 * */
		
		j.add(new Job(9, 29, 10));
		j.add(new Job(4, 24, 12));
		j.add(new Job(2, 21, 0));
		Scheduling test = new Scheduling(j, 3, 0.804);
		double testVal = test.shiftDown(test.getJobByName(9), 2);
		double expect = test.weight(test.getJobByName(9), 1) + test.weight(test.getJobByName(4), 2);
		assertTrue(testVal == expect);
	}

}
