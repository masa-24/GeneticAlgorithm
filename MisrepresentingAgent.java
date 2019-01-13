import java.util.ArrayList;

public class MisrepresentingAgent extends SelfishAgent implements Agent{
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
	
	public String misrepresentation(char issue1, char issue2, Agent opponentAgent){
    	ArrayList<Pair<Character, Integer>> partPref = new ArrayList<>();
    	char better = '?';
    	char worse = '?';
    	for(int i = 0; i < opponentAgent.getPreference().size(); i++){
    		if(issue1 == opponentAgent.getPreference().get(i).getLeft() || issue2 == opponentAgent.getPreference().get(i).getLeft()){
    			partPref.add(opponentAgent.getPreference().get(i));
    		}
    	}
    	if(partPref.size() != 2){
    		System.out.println("何かおかしい");
    		System.exit(-1);
    	}
    	if(Math.max(partPref.get(0).getRight(), partPref.get(1).getRight()) == partPref.get(0).getRight()){
    		better = partPref.get(0).getLeft();
    		worse = partPref.get(1).getLeft();
    	}else{
    		better = partPref.get(1).getLeft();
    		worse = partPref.get(0).getLeft();
    	}
    	return String.valueOf(better) + " > " + String.valueOf(worse);
	}
}
