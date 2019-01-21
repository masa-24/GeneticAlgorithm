import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    public int calculateUtility(ArrayList<Character> issue){
    	int result = 0;
    	
    	for(int i = 0; i < issue.size(); i++){
    		for(int j = 0; j < preference.size(); j++){
    			if(issue.get(i) == preference.get(j).getLeft()){
    				result += preference.get(j).getRight();
    			}
    		}
    	}
    	
    	return result;
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
    
    public Pair<Character, Character> compareIssue(char issue1, char issue2){
    	Pair<Character, Character> result = new Pair<>();
    	ArrayList<Pair<Character, Integer>> partPref = new ArrayList<>();
    	char higher = '?';
    	char lower = '?';

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
    		higher = partPref.get(0).getLeft();
    		lower = partPref.get(1).getLeft();
    	}else{
    		higher = partPref.get(1).getLeft();
    		lower = partPref.get(0).getLeft();
    	}
    	result.setBoth(higher, lower);
    	
    	return result;
    }
    
    public char compareIssue(int x){
    	//選好を重み順にソート
    	Collections.sort(preference, new Comparator<Pair<Character, Integer>>() {
    		public int compare(Pair<Character, Integer> pref1, Pair<Character, Integer> pref2){
    			return pref1.getRight() - pref2.getRight();
    		}
		});
    	Collections.reverse(preference);
    	
    	for(int i = 0; i < MisrepresentationGame.ISSUE.size(); i++){
    		if(x == preference.get(i).getRight()){
    			return preference.get(i).getLeft();
    		}
    	}
    	return ' ';
    }
}
