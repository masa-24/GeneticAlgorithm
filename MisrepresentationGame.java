import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MisrepresentationGame {
	final public static ArrayList<Character> ISSUE = new ArrayList<>(Arrays.asList('A', 'B', 'C'));
	Random rand = new Random();
	ArrayList<Pair<Character, Integer>> SelfishAgentRevealedPreference = new ArrayList<>();
	ArrayList<Pair<Character, Integer>> MisrepresentingAgentRevealedPreference = new ArrayList<>();
	ArrayList<String> issuePermutation = new ArrayList<>();
	
	public void preferenceElicitation(Agent agent1, Agent agent2, Genom g){
		int count = 0;
		
		for(int i = 0; i < ISSUE.size(); i++){
			for(int j = i+1; j < ISSUE.size(); j++){
				// 比較する論点を指定
				char issue1 = ISSUE.get(i);
				char issue2 = ISSUE.get(j);
				Pair<Character, Character> agent1revealed = new Pair<>();
				Pair<Character, Character> agent2revealed = new Pair<>();
				
				if(g.getGenom().get(0)){ //遺伝子0番目：preferenceをrevealする順番のランダム化
					if(rand.nextBoolean()){
						agent1revealed = agent1.compareIssue(issue1, issue2);
						agent2revealed = ((MisrepresentingAgent)agent2).compareIssue(issue1, issue2, agent1);
						System.out.println("agent1: " + agent1revealed.getLeft() + " > " + agent1revealed.getRight());
						System.out.println("agent2: " + agent2revealed.getLeft() + " > " + agent2revealed.getRight() + " (misrepresent)");
					}else{
						agent1revealed = agent1.compareIssue(issue1, issue2);
						agent2revealed = agent2.compareIssue(issue1, issue2);
						System.out.println("agent2: " + agent2revealed.getLeft() + " > " + agent2revealed.getRight());
						System.out.println("agent1: " + agent1revealed.getLeft() + " > " + agent1revealed.getRight());
					}
				}else if(g.getGenom().get(1)){ //遺伝子1番目：同時に選好を公開
					agent1revealed = agent1.compareIssue(issue1, issue2);
					agent2revealed = agent2.compareIssue(issue1, issue2);
					System.out.println("agent1: " + agent1revealed.getLeft() + " > " + agent1revealed.getRight());
					System.out.println("agent2: " + agent2revealed.getLeft() + " > " + agent2revealed.getRight());
				}else if(g.getGenom().get(2)){ //遺伝子2番目：交互に選好を公開
					if(count%2 == 0){
						agent1revealed = agent1.compareIssue(issue1, issue2);
						agent2revealed = ((MisrepresentingAgent)agent2).compareIssue(issue1, issue2, agent1);
						System.out.println("agent1: " + agent1revealed.getLeft() + " > " + agent1revealed.getRight());
						System.out.println("agent2: " + agent2revealed.getLeft() + " > " + agent2revealed.getRight() + " (misrepresent)");						
					}else{
						agent1revealed = agent1.compareIssue(issue1, issue2);
						agent2revealed = agent2.compareIssue(issue1, issue2);
						System.out.println("agent2: " + agent2revealed.getLeft() + " > " + agent2revealed.getRight());
						System.out.println("agent1: " + agent1revealed.getLeft() + " > " + agent1revealed.getRight());						
					}
					count++;
				}else{ //デフォルトは毎回同じ順番で選好を公開
					agent1revealed = agent1.compareIssue(issue1, issue2);
					agent2revealed = ((MisrepresentingAgent)agent2).compareIssue(issue1, issue2, agent1);
					System.out.println("agent1: " + agent1revealed.getLeft() + " > " + agent1revealed.getRight());
					System.out.println("agent2: " + agent2revealed.getLeft() + " > " + agent2revealed.getRight() + " (misrepresent)");
				}
				reasoning(agent1revealed, SelfishAgentRevealedPreference);
				reasoning(agent2revealed, MisrepresentingAgentRevealedPreference);				
			}
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
	
	public Pair<Integer, Integer> deal(Agent agent1, Agent agent2, Genom g){
		Pair<Integer, Integer> result = new Pair<>();
		
		permutation(arrayListToString(ISSUE), "");
		for(int i = 0; i < issuePermutation.size(); i++){
			for(int j  = 1; j < ISSUE.size()-1; j++){
				ArrayList<Character> item1 = stringToArrayList(issuePermutation.get(i).substring(0, j));
				ArrayList<Character> item2 = stringToArrayList(issuePermutation.get(i).substring(j));
				
				int agent1Util = calcUtil(SelfishAgentRevealedPreference, item1);
				int agent2Util = calcUtil(MisrepresentingAgentRevealedPreference, item2);
				if(g.getGenom().get(3)){ //遺伝子3番目：嘘ついてもバレる
					agent1Util = calcUtil(agent1.getPreference(), item1);
					agent2Util = calcUtil(agent2.getPreference(), item2);
				}
				//System.err.println("agent1 recieved: " + item1 + ", agent1 utility: " + agent1Util);
				//System.err.println("agent2 recieved: " + item2 + ", agent2 utility: " + agent2Util);
				//System.err.println("-------------------------------------------");
				if(agent1Util == agent2Util){
					result.setBoth(agent1Util, agent2Util);
					return result;
				}
			}
		}
		result.setBoth(0, 0);
		return result;
	}
	
	//論点の分配パターンの組み合わせを計算
	public void permutation(String q, String ans){	
		if(q.length() <= 1){
			//System.out.println(ans + q);
			issuePermutation.add(ans + q);
		}else{
			for(int i = 0; i < q.length(); i++){
				permutation(q.substring(0, i) + q.substring(i+1), ans + q.charAt(i));
			}
		}
	}
	
	public String arrayListToString(ArrayList<Character> list){
		String s = "";
		
		for(int i = 0; i < list.size(); i++){
			s += list.get(i);
		}
		
		return s;
	}
	
	public ArrayList<Character> stringToArrayList(String s){
		ArrayList<Character> list = new ArrayList<>();
		
		for(int i = 0; i < s.length(); i++){
			list.add(s.charAt(i));
		}
		
		return list;
	}
	
	public int calcUtil(ArrayList<Pair<Character, Integer>> pref, ArrayList<Character> portion){
		int utility = 0;
		
		for(int i = 0; i < portion.size(); i++){
			for(int j = 0; j < pref.size(); j++){
				if(portion.get(i) == pref.get(j).getLeft()){
					utility += pref.get(j).getRight();
				}
			}
		}
		return utility;
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
		Pair<Integer, Integer> utility = deal(sAgent, mAgent, g);

		result = 1.0 / (double)(Math.abs(utility.getRight() - utility.getLeft()) + 1.0);
		
		System.out.println("result: " + result + ", agent1: " + utility.getLeft() + ", agent2: " + utility.getRight());
		return result;
	}
	
	//公開された選好から全体の選好を推定する関数
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
			if(revealedPref.get(higherIndex).getRight() < revealedPref.get(lowerIndex).getRight()){
				// 既に宣言した選好について述べていて，かつ既に宣言したものと矛盾する選好であった場合
				int highValue = revealedPref.get(higherIndex).getRight();
				int lowValue = revealedPref.get(lowerIndex).getRight();

				higher.setBoth(p.getRight(), highValue);
				lower.setBoth(p.getLeft(), lowValue);
				revealedPref.set(higherIndex, higher);
				revealedPref.set(lowerIndex, lower);
			}else if(revealedPref.get(higherIndex).getRight() == revealedPref.get(lowerIndex).getRight()){
				// 既に宣言した選好について述べていて，処理的におかしい部分を見つけた場合
				boolean isExistLowerValue = false;
				boolean isExistHigherValue = false;
				
				// 順番がおかしくならないように，どう調整すればいいか調べる
				for(int i = 0; i < revealedPref.size(); i++){
					if(revealedPref.get(i).getRight() == revealedPref.get(higherIndex).getRight()+1){
						isExistHigherValue = true;
					}
					if(revealedPref.get(i).getRight() == revealedPref.get(lowerIndex).getRight()-1){
						isExistLowerValue = true;
					}
				}
				
				if(isExistHigherValue && !isExistLowerValue){ // 重みが高いほうが既に存在していた場合
					lower.setBoth(p.getRight(), revealedPref.get(lowerIndex).getRight()-1);
					revealedPref.set(lowerIndex, lower);					
				}else if(!isExistHigherValue && isExistLowerValue){ // 低いほうが既に存在していた場合
					higher.setBoth(p.getLeft(), revealedPref.get(higherIndex).getRight()+1);
					revealedPref.set(higherIndex, higher);					
				}else{
					System.err.println("isExistHigherValue: " + isExistHigherValue + ", isExistLowerValue: " + isExistLowerValue);
					System.exit(-1);
				}				
			}
		}else if(higherIndex != -1 && lowerIndex == -1){
			// 選好の重みが高い方が存在し，低い方が存在しない場合，高い方-1の値とともに格納する
			// 重みの値がずれないようにまず全体を+1する
			for(int i = 0; i < revealedPref.size(); i++){
				Pair<Character, Integer> temp = new Pair<>();
				temp.setBoth(revealedPref.get(i).getLeft(), revealedPref.get(i).getRight()+1);
				revealedPref.set(i, temp);
			}
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
