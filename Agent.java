import java.util.ArrayList;

public interface Agent {
	ArrayList<Pair<Character, Integer>> preference = new ArrayList<>();
	int utility = 0;
	
	ArrayList<Pair<Character, Integer>> getPreference();
	void setPreference(ArrayList<Pair<Character, Integer>> preference);
	
	int getUtility();
	void setUtility(int utility);
	
	int calculateUtility(ArrayList<Character> issue);
	
	// left: 選好の重みが高い方
	// right: 選好の重みが低い方
	Pair<Character, Character> compareIssue(char issue1, char issue2);
	//x番目に好みな選好を返す
	char compareIssue(int x);
}
