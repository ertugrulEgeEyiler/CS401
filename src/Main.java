import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String[] newArray = generateRandomWords();
        cluster(newArray);
    }

    public static String[] generateRandomWords() {
        String[] randomStrings = new String[100];
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            char[] word = new char[random.nextInt(8) + 3]; // words of length 3 through 10. (1 and 2 letter words are boring.)
            for (int j = 0; j < word.length; j++) {
                word[j] = (char) ('a' + random.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        return randomStrings;
    }
    public static void cluster(String[] toCluster) {
        ArrayList<String> letA = new ArrayList<>();

        for (String word : toCluster) {
            boolean found = false;
            for (char letter : word.toCharArray()) {
                if (letter == 'a') {
                    letA.add(word);
                    found = true;
                    break;
                }

            }
        }

        for (String s : letA) {
            if (s != null) {
                System.out.println(s);
            }
        }
    }
}