import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class LearningAnnealing {
    double temp;
    double tempMax = 10000;
    double tempMin = .01;
    double delta;
    double eOld;
    double eNew;
    int iMax = 10;
    Random rand = new Random();
    int[][] tempMove = new int[3][3];
    double annealingRate = 0.90;

    LearningAnnealing() {
        this.tempMax = (double)rand.nextInt(1000-10)+10;
    }

    public void LearningAlgorithm(){
        double successRateGoal = 0.05;
        Queue<Boolean> lastjMaxMoves = new LinkedList<Boolean>();
        double currentSuccessRate = 0;
        int jMax = 100;
        double deltaEnergy;
        while ((currentSuccessRate < successRateGoal) /*|| (tempMax < 1000000000)*/){
            for (int i = 0 ; i < jMax ; i++){
                //System.out.println(1);
                eightPuzzle puzzle = new eightPuzzle();
                deltaEnergy = annealingAlg(puzzle);
                boolean isGoal = false;
                if (deltaEnergy == 0){
                    isGoal = true;
                }
                lastjMaxMoves.add(isGoal);
                if (lastjMaxMoves.size() > jMax){
                    lastjMaxMoves.remove();
                }
            }
            int numGoal = 0;
            for(int k = 0; k < jMax; k++){
                //System.out.println(2);
                Boolean temp = lastjMaxMoves.peek();
                //System.out.println(temp);
                if (temp == true){
                    numGoal++;
                }
                lastjMaxMoves.remove();
            }
            System.out.println(numGoal);
            currentSuccessRate =(double)numGoal/(double)jMax;
            System.out.println(currentSuccessRate);
            System.out.println(successRateGoal);
            System.out.println(tempMax);
            //System.out.println(iMax);
            System.out.println();
            if (currentSuccessRate < successRateGoal){
                tempMax = tempMax * Math.pow((1-(successRateGoal - currentSuccessRate)),-1 );
                System.out.println(tempMax);
                System.out.println();
                //iMax = iMax * (int)Math.pow((1-(successRateGoal - currentSuccessRate)),-1 );
                //iMax = iMax + 3;
                /*annealingRate = annealingRate + 0.0001;
                System.out.println(annealingRate);
                System.out.println();*/
            }
            //System.out.println(3);
        }
        System.out.println(tempMax);
        //System.out.println(iMax);
        //System.out.println(annealingRate);
    }

    private double annealingAlg(eightPuzzle Y){
        for (temp = tempMax; temp >= tempMin; temp = nextTemp(temp)){
            for (int i = 0 ; i <= iMax; i++){
                tempMove = Y.successorFunction().clone();
                eOld = Y.E;
                eNew = Y.energyFunction(tempMove);
                delta = eNew - eOld;
                if (delta<0){
                    double random = rand.nextDouble();
                    if (random > Math.exp(delta/temp)){
                        //reject bad move
                    }else{
                        //accept bad move
                        Y.acceptSuccesor(tempMove.clone());
                    }
                }else{
                    //always accept good moves
                    Y.acceptSuccesor(tempMove.clone());

                }
            }
        }
        //Y.printPuzzle();
        return Y.goalEnergy - Y.E;

    }
    private double nextTemp(double tempIn){
        return tempIn*annealingRate;
    }
}

