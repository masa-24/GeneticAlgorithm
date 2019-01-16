import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MisrepresentationGame {
	final public static ArrayList<Character> ISSUE = new ArrayList<>(Arrays.asList('A', 'B', 'C'));
	Random rand = new Random();
	ArrayList<Pair<Character, Integer>> SelfishAgentRevealedPreference = new ArrayList<>();
	ArrayList<Pair<Character, Integer>> MisrepresentingAgentRevealedPreference = new ArrayList<>();
	
	public void preferenceElicitation(Agent agent1, Agent agent2, Genom g){
		for(int i = 1; i < ISSUE.size(); i++){
			// 比較する論点を指定
			char issue1 = ISSUE.get(i-1);
			char issue2 = ISSUE.get(i);
			Pair<String, Pair<Character, Character>> agent1revealed = new Pair<>();
			Pair<String, Pair<Character, Character>> agent2revealed = new Pair<>();
			
			if(g.getGenom().get(0)){ //遺伝子0番目：preferenceをrevealする順番のランダム化
				if(rand.nextBoolean()){
					agent1revealed = agent1.compareIssue(issue1, issue2);
					agent2revealed = ((MisrepresentingAgent)agent2).compareIssue(issue1, issue2, agent1);
					System.out.println("agent1:" + agent1revealed.getLeft());
					System.out.println("agent2:" + agent2revealed.getLeft());
				}else{
					agent1revealed = agent1.compareIssue(issue1, issue2);
					agent2revealed = agent2.compareIssue(issue1, issue2);
					System.out.println("agent2:" + agent2revealed.getLeft());
					System.out.println("agent1:" + agent1revealed.getLeft());
				}
			}else if(g.getGenom().get(1)){ //遺伝子1番目：同時に選好を公開
				agent1revealed = agent1.compareIssue(issue1, issue2);
				agent2revealed = agent2.compareIssue(issue1, issue2);
				System.out.println("agent1:" + agent1revealed.getLeft());
				System.out.println("agent2:" + agent2revealed.getLeft());				
			}else{ //遺伝子0番目がfalseのときは毎回同じ順番で選好を公開
				agent1revealed = agent1.compareIssue(issue1, issue2);
				agent2revealed = ((MisrepresentingAgent)agent2).compareIssue(issue1, issue2, agent1);
				System.out.println("agent1:" + agent1revealed.getLeft());
				System.out.println("agent2:" + agent2revealed.getLeft());
			}
			reasoning(agent1revealed.getRight(), SelfishAgentRevealedPreference);
			reasoning(agent2revealed.getRight(), MisrepresentingAgentRevealedPreference);
		}
		/*
		System.out.println("自己中心的エージェント--------");
		for(int i = 0; i < SelfishAgentRevealedPreference.size(); i++){
			System.out.println(SelfishAgentRevealedPreference.get(i).getLeft() + ", " + SelfishAgentRevealedPreference.get(i).getRight());
		}
		System.out.println("Misrepresenting agent----");
		for(int i = 0; i < MisrepresentingAgentRevealedPreference.size(); i++){
			System.out.println(MisrepresentingAgentRevealedPreference.get(i).getLeft() + ", " + MisrepresentingAgentRevealedPreference.get(i).getRight());
		}
		*/
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
	
	public double negotiation(Genom g){
		double result = 0.0;	//評価値
		ArrayList<Pair<Character, Integer>> selfishAgentPref = new ArrayList<>();
		ArrayList<Pair<Character, Integer>> misrepresentingAgentPref = new ArrayList<>();
		
		// 自己中心的なエージェントの選好を用意
		for(int i = 0; i < ISSUE.size(); i++){
			Pair<Character, Integer> temp = new Pair<>();
			temp.setBoth(ISSUE.get(i), i+1);
			selfishAgentPref.add(temp);
		}
		// Misrepresenting agentの選好を用意
		for(int i = 0; i < ISSUE.size(); i++){
			Pair<Character, Integer> temp = new Pair<>();
			temp.setBoth(ISSUE.get(i), ISSUE.size()-i);
			misrepresentingAgentPref.add(temp);
		}
		
		// エージェント生成
		SelfishAgent sAgent = new SelfishAgent(selfishAgentPref);
		MisrepresentingAgent mAgent = new MisrepresentingAgent(misrepresentingAgentPref);
		
		preferenceElicitation(sAgent, mAgent, g);
		deal(sAgent, mAgent, g);

		result = 1.0 / (double)(Math.abs(mAgent.getFakeUtility() - sAgent.getUtility()) + 1.0);
		
		return result;
	}
	
	public void reasoning(Pair<Character, Character> p, ArrayList<Pair<Character, Integer>> revealedPref){
		Pair<Character, Integer> higher = new Pair<>();
		Pair<Character, Integer> lower = new Pair<>();
		int higherIndex = -1; //選好の重みが高いほうのindex
		int lowerIndex = -1; //選好の重みが低い方のindex

		for(int i = 0; i < revealedPref.size(); i++){
			if(p.getLeft() == revealedPref.get(i).getLeft()){
				higherIndex = i;
			}else if(p.getRight() == revealedPref.get(i).getLeft()){
				lowerIndex = i;
			}
		}
		
		if(higherIndex != -1 && lowerIndex != -1){
			//矛盾していた場合
			if(revealedPref.get(higherIndex).getRight() < revealedPref.get(lowerIndex).getRight()){
				int highValue = revealedPref.get(higherIndex).getRight();
				int lowValue = revealedPref.get(lowerIndex).getRight();

				higher.setBoth(p.getRight(), highValue);
				lower.setBoth(p.getLeft(), lowValue);
				revealedPref.set(higherIndex, higher);
				revealedPref.set(lowerIndex, lower);
			}
		}else if(higherIndex != -1 && lowerIndex == -1){
			//選好の重みが高い方が存在し，低い方が存在しない場合，高い方-1の値とともに格納する
			lower.setBoth(p.getRight(), revealedPref.get(higherIndex).getRight()-1);
			revealedPref.add(lower);
		}else if(higherIndex == -1 && lowerIndex != -1){
			//選好の重みが高い方が存在せず，低いほうが存在する場合，低い方+1の値とともに格納する
			higher.setBoth(p.getLeft(), revealedPref.get(lowerIndex).getRight()+1);
			revealedPref.add(higher);
		}else{
			higher.setBoth(p.getLeft(), 2);
			lower.setBoth(p.getRight(), 1);
			revealedPref.add(higher);
			revealedPref.add(lower);
		}
	}
}
