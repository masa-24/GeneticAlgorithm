import java.util.ArrayList;

public interface Agent {
	ArrayList<Pair<Character, Integer>> preference = new ArrayList<>();
	int utility = 0;
	
	ArrayList<Pair<Character, Integer>> getPreference();
	void setPreference(ArrayList<Pair<Character, Integer>> preference);
	
	int getUtility();
	void setUtility(int utility);
	
	void calculateUtility(ArrayList<Character> issue);
	String compareIssue(char issue1, char issue2);
}
