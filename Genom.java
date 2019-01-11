import java.util.ArrayList;

public class Genom {	
	private ArrayList<Boolean> genomList = new ArrayList<>();
	private double evaluation = 0.0;
	
	public Genom(ArrayList<Boolean> genomList, double evaluation){
		this.genomList = genomList;
		this.evaluation = evaluation;
	}
	
	public ArrayList<Boolean> getGenom(){
		return genomList;
	}
	
	public void setGenom(ArrayList<Boolean> genomList){
		this.genomList = genomList;
	}
	
	public double getEvaluation(){
		return evaluation;
	}
	
	public void setEvaluation(double evaluation){
		this.evaluation = evaluation;
	}
}
