import java.lang.reflect.Array;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        System.out.println("Hello and welcome!");
        ArrayList<Integer> toCluster = generateArray();
        cluster(toCluster);
    }

    public static ArrayList<Integer> generateArray(){
        Random random = new Random(1);
        ArrayList<Integer> array = new ArrayList<Integer>(100);
        for (int i = 0; i < 100; i++){
            int number = random.nextInt(2);
            array.add(i,number);
        }

        return array;
    }

    public static void cluster(ArrayList<Integer> toCluster){

    }
}