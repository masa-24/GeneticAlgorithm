import java.util.ArrayList;

public class SelfishAgent implements Agent{
	private ArrayList<Pair<Character, Integer>> preference = new ArrayList<>();
    private int utility;
    
    public SelfishAgent(ArrayList<Pair<Character, Integer>> preference) {
    	this.preference.addAll(preference);
    	utility = 0;
	}
    
    public ArrayList<Pair<Character, Integer>> getPreference(){
    	return this.preference;
    }
    
	public void setPreference(ArrayList<Pair<Character, Integer>> preference){
        this.preference.addAll(preference);
	}

	public int getUtility(){
		return this.utility;
	}

	public void setUtility(int utility){
        this.utility = utility;
    }

    public void calculateUtility(ArrayList<Character> issue){
    	for(int i = 0; i < issue.size(); i++){
    		for(int j = 0; j < preference.size(); j++){
    			if(issue.get(i) == preference.get(j).getLeft()){
    				utility += preference.get(j).getRight();
    			}
    		}
    	}
	}
    
    // デバッグ用
    public String revealPreference(){
    	String result = "";
    	for(int i = 0; i < preference.size(); i++){
    		result += preference.get(i).getLeft();
    		if(i != preference.size()-1){
    			result += " > ";
    		}
    	}
    	return result;    	
    }
    
    public String compareIssue(char issue1, char issue2){
    	ArrayList<Pair<Character, Integer>> partPref = new ArrayList<>();
    	char better = '?';
    	char worse = '?';

    	for(int i = 0; i < preference.size(); i++){
    		if(issue1 == preference.get(i).getLeft() || issue2 == preference.get(i).getLeft()){
    			partPref.add(preference.get(i));
    		}
    	}
    	if(partPref.size() != 2){
    		System.err.println("error: " + partPref + ", partPref.size:" + partPref.size());
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
