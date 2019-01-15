import java.util.ArrayList;
import java.util.Collections;

public class Main {	
	public static void main(String[] args){
		GA ga = new GA();

		//一番最初の現行世代個体集団を生成
		ArrayList<Genom> currentGenerationIndividualGroup = new ArrayList<>(); //現行世代個体集団
		for(int i = 0; i < GA.GENOM_NUM; i++){
			currentGenerationIndividualGroup.add(ga.createGenom());
		}

		ArrayList<Genom> eliteGenes = new ArrayList<>(); //エリート集団
		ArrayList<Genom> offsprings = new ArrayList<>(); //子孫
		ArrayList<Genom> nextGenerationIndividualGroup = new ArrayList<>(); //次世代個体集団
		for(int count = 1; count < GA.MAX_GENERATION+1; count++){
			for(int i = 0; i < currentGenerationIndividualGroup.size(); i++){
				//double eval = ga.evaluation(currentGenerationIndividualGroup.get(i));
				double eval = ga.negotiation(currentGenerationIndividualGroup.get(i));
				currentGenerationIndividualGroup.get(i).setEvaluation(eval); //現行世代のi番目の遺伝子に評価値を設定
			}
			System.out.println("交渉問題なし．終了します");
			System.exit(-1);
			//eliteGenes = ga.selection(currentGenerationIndividualGroup); //エリートを選択
			eliteGenes = ga.stochasticUniversalSampling(currentGenerationIndividualGroup);
			//子孫を生成
			for(int i = 1; i < GA.OFFSPRING_NUM; i++){
				offsprings.addAll(ga.crossover(eliteGenes.get(i), eliteGenes.get(i-1)));
			}
			// 次世代個体集団を現行世代，エリート，子孫から生成
			// 次世代個体集団すべての個体に突然変異を施す
			nextGenerationIndividualGroup.addAll(ga.mutation(ga.nextGenerationGeneCreate(currentGenerationIndividualGroup, offsprings)));
			
			// 各個体適用度を配列化
			ArrayList<Double> fits = new ArrayList<>();
			double sum = 0.0;
			for(int i = 0; i < currentGenerationIndividualGroup.size(); i++){
				fits.add(currentGenerationIndividualGroup.get(i).getEvaluation());
				sum += currentGenerationIndividualGroup.get(i).getEvaluation();
				//System.out.println("遺伝子: " + encodeGenom(currentGenerationIndividualGroup.get(i).getGenom()) + ", 評価値: " + currentGenerationIndividualGroup.get(i).getEvaluation());
			}
			// 進化結果を評価
			double min = Collections.min(fits);
			double max = Collections.max(fits);
			double average = sum / (double)fits.size();
			
			// 現行世代の進化結果を出力
			System.out.println("第" + count + "世代の結果");
			System.out.println("最小適合度 :" + min);
			System.out.println("最大適合度 :" + max);
			System.out.println("平均適合度 :" + average);
		
			// 現行世代と次世代を入れ替え
			currentGenerationIndividualGroup.clear();
			currentGenerationIndividualGroup.addAll(nextGenerationIndividualGroup);
			// 各ArrayListを初期化
			if(count < GA.MAX_GENERATION){
				eliteGenes.clear();
				offsprings.clear();
				nextGenerationIndividualGroup.clear();				
			}
		}
		System.out.println("最も優れた個体は" + decodeGenom(eliteGenes.get(0).getGenom()) + ", 評価値は " + eliteGenes.get(0).getEvaluation());
	}
	
	//Booleanである遺伝子を 0/1のString に直す関数．1がtrue, 0がfalse
	public static String decodeGenom(ArrayList<Boolean> genomList){
		String s = "";
		for(int i = 0; i < GA.GENOM_LENGTH; i++){
			if(genomList.get(i)){
				s += 1;
			}else{
				s += 0;
			}
		}
		return s;
	}
}
