import java.util.ArrayList;
import java.util.Arrays;

public class MisrepresentationGame {
	final public static ArrayList<Character> ISSUE = new ArrayList<>(Arrays.asList('A', 'B', 'C'));

	public void preferenceElicitation(Agent agent1, Agent agent2, Genom g){
		for(int i = 1; i < ISSUE.size(); i++){
			// 比較する論点を指定
			char issue1 = ISSUE.get(i-1);
			char issue2 = ISSUE.get(i);
			
			System.out.println("agent1:" + agent1.compareIssue(issue1, issue2));
			System.out.println("agent2:" + agent2.compareIssue(issue1, issue2));
		}
	}

	public void deal(Agent agent1, MisrepresentingAgent agent2, Genom g){
		ArrayList<Character> item1 = new ArrayList<>();
		ArrayList<Character> item2 = new ArrayList<>();
		
		item1.add(ISSUE.get(0));
		item2.add(ISSUE.get(1));
		item2.add(ISSUE.get(2));
		
		agent1.calculateUtility(item1); // Selfish agentの効用を計算
		agent2.calculateUtility(item2);	// Misrepresenting agentの本来の効用を計算
		agent2.calculateFakeUtility(item2, agent1);	// Misrepresenting agentの見せかけの効用を計算
	}
}
