import java.util.ArrayList;

public class MisrepresentingAgent extends SelfishAgent{
	private int fakeUtility = 0;

	public MisrepresentingAgent(ArrayList<Pair<Character, Integer>> preference) {
		super(preference);
		fakeUtility = 0;
	}
	
	public int getFakeUtility(){
		return this.fakeUtility;
	}
	
	public void setFakeUtility(int utility){
		fakeUtility = utility;
	}
	
	public void calculateFakeUtility(ArrayList<Character> issue, Agent opponentAgent){
		for(int i = 0; i < issue.size(); i++){
			for(int j = 0; j < opponentAgent.getPreference().size(); j++){
				if(issue.get(i) == opponentAgent.getPreference().get(j).getLeft()){
					fakeUtility += opponentAgent.getPreference().get(j).getRight();
				}
			}
		}
	}
	
	public Pair<String, Pair<Character, Character>> compareIssue(char issue1, char issue2, Agent opponentAgent){
		return opponentAgent.compareIssue(issue1, issue2);
	}
}
