import java.util.ArrayList;

public class MaxWeightMatching {
	//some nodes might not be covered, choose the most expensive edges that don't repeat time slots or jobs. All time slots will be covered.
	private Scheduling instOf;
	private ArrayList<Edge> edges;
	public MaxWeightMatching(Scheduling x) {
		instOf = x;
		edges = new ArrayList<Edge>();
		setEdges(edges);
	}
	
	private void setEdges(ArrayList<Edge> e) {
		for(Job j:instOf.getJobs()) {
			int t = 0; 
			while(t <= j.deadline) {
				e.add(new Edge(j, t, instOf.weight(j, t)));
				t++;
			}
			
		}
		
	}
	
	public void addEdgeToSet(ArrayList<Edge> currCombo, ArrayList<Edge> remEdges) {
		//choose every possible combination, so just pick the first one and remove all edges with that time slot and job.
		if(remEdges.size() == 0)
			return;
		Edge jobToSchedule = remEdges.get(0);
		currCombo.add(jobToSchedule);
		for(int i = remEdges.size()-1; i>= 0; i--) {
			if(remEdges.get(i).job.jobID == jobToSchedule.job.jobID) {
				remEdges.remove(i);
				//if the edge is coming out of the added job, remove it
				
			}
			else if(remEdges.get(i).time == jobToSchedule.time) {
				remEdges.remove(i);
			}
			
		}
		
	}
	
	public double addEdgeToSet2(ArrayList<Edge> maxCombo, ArrayList<Edge> currCombo, ArrayList<Edge> remEdges, double max) {
		//choose every possible combination, so just pick the first one and remove all edges with that time slot and job.
		if(remEdges.size() == 0) {
			return valueOfCurrCombo(currCombo);
		}
		Edge jobToSchedule = remEdges.get(0);
		currCombo.add(jobToSchedule);
		for(int i = remEdges.size()-1; i>= 0; i--) {
			if(remEdges.get(i).job.jobID == jobToSchedule.job.jobID) {
				remEdges.remove(i);
				//if the edge is coming out of the added job, remove it
				
			}
			else if(remEdges.get(i).time == jobToSchedule.time) {
				remEdges.remove(i);
			}
		}
		double temp = max;
		ArrayList<Edge> tempRemEdges = new ArrayList<Edge>();
		for(Edge e: remEdges) {
			Edge x = new Edge(e.job, e.time, e.weight);
			tempRemEdges.add(x);
		}
		
		for(Edge e: remEdges) {
			double recMax = addEdgeToSet2(maxCombo, currCombo, tempRemEdges, max);
			if(temp < recMax) {
				temp = recMax;
				maxCombo = currCombo;
			}
		}
		return temp;
	}

	
	public double valueOfCurrCombo(ArrayList<Edge> currCombo) {
		double total = 0;
		for(Edge e : currCombo) {
			total += e.weight;
		}
		return total;
	}
	public void findMaxOfAllCombo() {
		
		double max = 0;
		ArrayList<Edge> currentList = new ArrayList<Edge>();
		ArrayList<Edge> tempRem = new ArrayList<Edge>();	
		ArrayList<Edge> maxCombo = currentList;
		for(Edge e:edges) {
			setEdges(tempRem);
			currentList.add(e);
			for(int i = tempRem.size()-1; i>= 0; i--) {
				if(tempRem.get(i).job.jobID == e.job.jobID) {
					tempRem.remove(i);
					//if the edge is coming out of the added job, remove it
					
				}
				else if(tempRem.get(i).time == e.time) {
					tempRem.remove(i);
				}
				
			}
			
			
			
			
			while(tempRem.size()>0) {
				addEdgeToSet(currentList, tempRem);
			}
			double temp = valueOfCurrCombo(currentList);
			if(temp > max) {
				max = temp;
				maxCombo = currentList;
			}
			currentList = new ArrayList<Edge>();
			
		}
		for(Edge e: maxCombo) {
			System.out.println("Job " + e.job.jobID + " is scheduled for " + e.time + " with value " + e.weight);
		}
		
		
		System.out.println("Weight: "+max);
			
		
	}
	

}
class Edge {
	public Job job;
	public int time;
	public double weight;
	Edge(Job x, int time, double w){
		job = x;
		this.time = time;
		weight = w;
	}
}