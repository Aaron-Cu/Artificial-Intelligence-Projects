import java.util.*;

//Finds a goal word from a random order of letters, can only switch adjacent letters.
public class RealWorldImplementation {
    public static void main(String[] args) {
        String goal = "Hydrostatics";
        System.out.println("Goal word:       "+goal);
        scrambledWord testWord = new scrambledWord(goal);
        System.out.println("Starting state:  "+testWord.toString());
        System.out.println("Starting Energy: "+testWord.energyFunction(testWord.scrambled));
        wordalgorithm alg = new wordalgorithm();
        alg.annealingAlg(testWord);
        System.out.println("Result:          " +testWord.toString());
        System.out.println("Result Energy:   "+testWord.energyFunction(testWord.scrambled));
    }
}
    //Node class for current state
     class scrambledWord{
        String goalWord;
        Random rand = new Random();
        List<Character> scrambled = new ArrayList<>();
        List<Character> characterList = new ArrayList<>();

        //Generates a random starting position
        public scrambledWord(String word){
            goalWord = word;
            char[] letters = goalWord.toCharArray();
            for (int i = 0 ; i < letters.length; i++){
                characterList.add(letters[i]);
            }

            List<Character> characterListTemp = new ArrayList<>();
            for (int i = 0; i < characterList.size() ; i++){
                characterListTemp.add(characterList.get(i));
            }

            while (characterListTemp.size() != 0 ){
                int j = rand.nextInt(characterListTemp.size());
                scrambled.add(characterListTemp.get(j));
                characterListTemp.remove(j);
            }

        }

        //returns current state as a string
        public String toString(){
            String temp = new String();
            for (int i = 0; i < scrambled.size() ; i++){
                temp = temp + scrambled.get(i);
            }
            return temp;
        }

        //returns the energery of the current state
        public double energyFunction(List<Character> input) {
            double e = 0;
            for (int i = 0 ; i < input.size() ; i++){
                if (input.get(i) == characterList.get(i) ){
                    e= e +1;
                }
            }
            return e;
        }

        //returns a random successor state for the current state
        public List<Character> successorFunction(){
            List<Character> temp = new ArrayList<>();
            for (int i = 0; i < scrambled.size() ; i++){
                temp.add(scrambled.get(i));
            }
            int secondChar;
            int firstChar = rand.nextInt(scrambled.size()-1);
            if (firstChar == scrambled.size()-1){
                secondChar = firstChar-1;
            } else if (firstChar == 0) {
                secondChar = 1;
            }else{
                if (rand.nextDouble()< .5){
                    secondChar = firstChar - 1;

                }else{
                    secondChar = firstChar + 1;
                }
            }

            Character tempChar;
            tempChar = temp.get(firstChar);
            temp.set(firstChar, temp.get(secondChar));
            temp.set(secondChar, tempChar);
            return temp;
        }

        //set the current state to a successor
        public void acceptSuccessor(List<Character> successor){
            scrambled = successor;
        }

    }

    //Simulated annealing algorithm
     class wordalgorithm{
        double temp;
        double tempMax = 300000;
        double tempMin = .01;
        double delta;
        double eOld;
        double eNew;
        int iMax = 55;
        Random rand = new Random();
        List<Character> tempMove = new ArrayList<>();



        public void annealingAlg(scrambledWord Y){
            for (temp = tempMax; temp >= tempMin; temp = nextTemp(temp)){
                for (int i = 0 ; i <= iMax; i++){
                    tempMove = Y.successorFunction();
                    eOld = Y.energyFunction(Y.scrambled);
                    eNew = Y.energyFunction(tempMove);
                    delta = eNew - eOld;
                    if (delta<0){
                        double random = rand.nextDouble();
                        //System.out.println(random);
                        if (random > Math.exp(delta/temp)){
                            //reject bad move
                        }else{
                            //accept bad move
                            Y.acceptSuccessor(tempMove);
                        }
                    }else{
                        //always accept good moves
                        Y.acceptSuccessor(tempMove);

                    }
                }
            }

        }

        //reduces temp
        private double nextTemp(double tempIn){
            return tempIn*0.99;
        }
    }

