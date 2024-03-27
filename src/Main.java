import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String[] newArray = generateRandomWords();
        cluster(newArray,generateAlphabet(),0);

    }

    public static String[] generateRandomWords() {
        String[] randomStrings = new String[100];
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            char[] word = new char[random.nextInt(8) + 3];
            for (int j = 0; j < word.length; j++) {
                word[j] = (char) ('a' + random.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        return randomStrings;
    }
    public static void cluster(String[] toCluster, ArrayList<Character> alphabet, int alphabetIndex) {
        ArrayList<String> letA = new ArrayList<>();
        System.out.println("Index: " + alphabet.get(alphabetIndex));
        System.out.println("----------------");
        for (String word : toCluster) {
            for (char letter : word.toCharArray()) {
                if (letter == alphabet.get(alphabetIndex)) {
                    letA.add(word);
                    break;
                }
            }
        }
        for (String s : letA) {
            if (s != null) {
                System.out.println(s);
            }
        }
        System.out.println("--------------------------------------------------------");
        if(alphabetIndex < 25) {
            cluster(toCluster, alphabet, alphabetIndex + 1);
        }
    }

    public static ArrayList<Character> generateAlphabet(){
        ArrayList<Character> alphabeth = new ArrayList<>();
        for(int i = 0; i < 26; i++){
            alphabeth.add((char) ('a' + i));
        }
        return alphabeth;
    }
}