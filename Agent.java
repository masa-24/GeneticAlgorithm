import java.util.ArrayList;

public interface Agent {
	ArrayList<Pair<Character, Integer>> preference = new ArrayList<>();
	int utility = 0;
	
	ArrayList<Pair<Character, Integer>> getPreference();
	void setPreference(ArrayList<Pair<Character, Integer>> preference);
	
	int getUtility();
	void setUtility(int utility);
	
	void calculateUtility(ArrayList<Character> issue);
	
	// left: 選好の好みを人間がわかりやすいようにStringで返す
	// right: 選好をシステム的に推論するorderedなデータを返す
	Pair<String, Pair<Character, Character>> compareIssue(char issue1, char issue2);
}
