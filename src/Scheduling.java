import java.util.ArrayList;
import java.util.Comparator;

public class Scheduling{
	private double decay = 0;
	private ArrayList<Job> jobs;
	private int totalSlots;
	//private ArrayList<Double> matchedEdges = new ArrayList<Double>();
	private int[] matchedEdges;
	private double[] matchedWeights;
	private int[] matchedJobs;
	//matchedEdges[timeSlot] = jobID, job scheduled at that timeSlot
	//weight of the edge is job.value*decay^timeSlot
	public Scheduling(ArrayList<Job> jobs, int numTimeSlots, double decayFactor) {
		//try to maximize weight of all edges
		decay = decayFactor;
		totalSlots = numTimeSlots;
		this.jobs = jobs;
		jobs.sort(new valueCompare());
		matchedEdges = new int[numTimeSlots];
		matchedWeights = new double[numTimeSlots];
		matchedJobs = new int[numTimeSlots];
		match();
		
	}
	public double getDecay() {
		return decay;
	}
	public ArrayList<Job> getJobs(){
		return jobs;
	}
	public int getTotalSlot() {
		return totalSlots;
	}
	public double weight(Job x, int time) {
		if(x.deadline >= time)
			return x.value*Math.pow(decay, time);
		return 0;
		
	}
	public double shiftDown(Job x, int nextAvail) {
		Job toShift = x;
		double newWeight = 0;
		int toShiftTime = toShift.matchedSlot;
		for(int i = toShiftTime+1; i < nextAvail; i++) {
			
			Job jobAtI = getJobByName(matchedEdges[i]);
			
			if(jobAtI.matchedSlot < jobAtI.deadline) {
				//jobAtI can possibly shift down too, now the job toShift is jobAtI
				toShift.matchedSlot = jobAtI.matchedSlot;
				jobAtI.matchedSlot = -1; //jobAtI becomes unmatched
				matchedEdges[toShift.matchedSlot] = jobAtI.jobID;
				matchedEdges[jobAtI.matchedSlot] = -1;
				matchedWeights[toShift.matchedSlot] = weight(toShift, toShift.matchedSlot);
				matchedWeights[jobAtI.matchedSlot] = 0;
				newWeight += weight(toShift, toShift.matchedSlot);
				toShift = jobAtI;
				//point to Shift to be the jobAtI.
			}
			else {
				newWeight += weight(jobAtI, jobAtI.matchedSlot);
			}
			// otherwise, toShift will skip jobAtI.
				
			
			//if you can shift directly down, do that. otherwise, skip the timeslot that cannot be taken.
			
		}
		
	
		toShift.matchedSlot = nextAvail;
		newWeight += weight(toShift, toShift.matchedSlot);
		matchedEdges[toShift.matchedSlot] = toShift.jobID;
		matchedWeights[toShift.matchedSlot] = weight(toShift, toShift.matchedSlot);
	
			
		
		return newWeight;
		
	}
	public Job getJobByName(int jobName) {
		for(int i = 0; i < jobs.size(); i++) {
			if(jobName == jobs.get(i).jobID)
				return jobs.get(i);
		}
		return null;
	}
	public double switchDown() {
		double newWeight = 0;
		
		
		
		return newWeight;
	}
	public void match() {
		int nextAvail = 0;
		
		//a job with higher value never becomes unmatched to make room for a lower value job (?)
		for(int i = 0; i < jobs.size(); i++) {
			Job x = jobs.get(i);
			if(nextAvail <= x.deadline) {
				x.matchedSlot = nextAvail;
				matchedEdges[nextAvail] = x.jobID;
				matchedWeights[nextAvail] = weight(x, nextAvail);
				nextAvail++;
				
			}
			//if next available is empty, let it be matched
			else {	//nextAvail is already passed Job x's deadline, it wants to switch with someone before it. 
				boolean switched = false;
				switchCheck: for(int k = i-1; k >= 0; k--) {	//this loop could be replaced with storing an array at each timeslot of acceptable jobs?
					Job possSwitch = jobs.get(k);
					if(possSwitch.matchedSlot < 0) {
						continue switchCheck;	//if job above is not matched, should check job above that
					}				
					double highNew = weight(possSwitch, nextAvail);
					double xNew = weight(x, possSwitch.matchedSlot);
		
					if(highNew + xNew > matchedWeights[possSwitch.matchedSlot]) {
						//it's better to switch than to not schedule job x
						//BUT it's better to shift down everything (if possible) than to switch 
						int xTime = possSwitch.matchedSlot;
						possSwitch.matchedSlot = nextAvail;
						x.matchedSlot = xTime;
						matchedEdges[nextAvail] = possSwitch.jobID;
						matchedWeights[nextAvail] = highNew;
						matchedEdges[xTime] = x.jobID;
						matchedWeights[xTime] = xNew;
						nextAvail++;
						switched = true;
						break;
					}

				}
				if(!switched) {
					x.matchedSlot = -1;
					
				}
				
			}
			
		}
		
	}
	public void setAllMatchings() {

		for(Job j: jobs) {
			if(j.matchedSlot >= 0)
				System.out.println(j.jobID + " is scheduled for time slot " + j.matchedSlot + " with value " + matchedWeights[j.matchedSlot]);
			else
				System.out.println(j.jobID + " is unmatched");
			//matchedJobs[j.jobID] = j.matchedSlot;
		}
		
		
		
		
	}
	public double maxWeight() {
		double total = 0;
		for(int i = 0; i < matchedWeights.length; i++){
			total += matchedWeights[i];
		}
		return total;
	}
	public double alsoMaxWeight() {
		double out = 0;
		for(Job x:jobs) {
			out+= weight(x, x.matchedSlot);
		}
		return out;
	}
	public void printMatchedEdges() {
		
		for(int i = 0; i < matchedEdges.length; i++){
			System.out.println(matchedEdges[i] + " is scheduled for time slot " + i + " with value " + matchedWeights[i]);
		}		
	}
	
	//calculate the weight of each each and run hungarian(?) on the graph to see if the two are the same
	
	
	

	
}

class Job{
	public int jobID;
	public int value;
	public int matchedSlot;
	public int deadline;
	public Job(int a, int b, int c) {
		jobID = a;
		value = b;
		deadline = c;
		matchedSlot = -1;
		
	}
	
	// job can have matchedSlot <= deadline
}

class valueCompare implements Comparator<Job>{
	public int compare(Job a, Job b) {
        return b.value - a.value;
        //this sorts array in decreasing order (returns -1 when a is larger, so places a in front of b)
    }
}


//maybe use this?
class timeSlot{
	private int[] acceptableJobs;
	//if(accetableJobs[jobID] == 1, job is acceptable.
	public timeSlot(int totalJobs) {
		
		
	}
}

