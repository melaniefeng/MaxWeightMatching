import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
public class Driver {
	public static void main(String[] args) {
		int numJobs = 55;
		int timeSlots = numJobs;
		int maxValue = 3000;
		double beta = 0;
		ArrayList<Job> jobs = new ArrayList<Job>();
		//jobs 1 2 3
	
		int randomDead = ThreadLocalRandom.current().nextInt(0, timeSlots);
		int randomValue = ThreadLocalRandom.current().nextInt(0, maxValue);
		beta = Math.random();
		beta = beta*1000;
		beta = Math.round(beta);
		beta = beta/1000;
		
		for(int i = 0; i < numJobs; i++) {
			jobs.add(new Job(i,randomValue, randomDead));
			randomValue = ThreadLocalRandom.current().nextInt(0, maxValue);
			randomDead = ThreadLocalRandom.current().nextInt(0, timeSlots);
		}
		
			/*jobs.add(new Job(1, 2*10, 1));
			jobs.add(new Job(0, 3*10, 2));		
			jobs.add(new Job(2, 1*10, 1));	*/
		//in future, randomly generate list of int and sort them.
		Scheduling test = new Scheduling(jobs, timeSlots, beta);
		for(Job x:jobs) {
			System.out.println("Job " + x.jobID + ": has value " + x.value + " and deadline " + x.deadline);
		}
		System.out.println("Max weight: "+test.maxWeight());
		//System.out.println("Max weight 2 by jobs: "+test.alsoMaxWeight());
		System.out.println("Decay factor: " + beta);
		test.setAllMatchings();
		MaxWeightMatching bruteForce = new MaxWeightMatching(test);
		bruteForce.findMaxOfAllCombo();
		
		
		
		/*
		double[][] weightedEdge = new double[jobs.size()][timeSlots];
		for(Job temp : jobs) {
			int t = 0;
			while(t <= temp.deadline) {
				weightedEdge[temp.jobID][t] = -1*test.weight(temp, t);
				//weightedEdge[jobID][t] holds the weight of job temp at time t, -1 because the thing i found online is minWeight
				t++;
			}
			
		}
		HungarianAlgorithm checker = new HungarianAlgorithm(weightedEdge);
		int[] matchedTimes = checker.execute();
		//matchedTimes[jobID] = time the job is allotted
		for(int i = matchedTimes.length-1; i >=0; i--) {
			System.out.println("Job" + i + " is scheduled for "+ matchedTimes[i]);
		}
		
		*/
		
		
		
	}
	
	
	
}
