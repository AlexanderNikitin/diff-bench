import algorithm.IntSequencePair;
import algorithm.LevenshteinDiff;
import algorithm.SequenceEqualityMap;

import java.util.Random;

public class Main {
    private static final Random random = new Random();
    private static final int LENGTH = 10000;
    private static final int[] s1 = new int[LENGTH];
    private static final int[] s2 = new int[LENGTH];
    private static SequenceEqualityMap compare;
    public static void main(String[] args) {
        long t0 = System.currentTimeMillis();
        for(int j = 0; j < 20; j++) {
            for (int i = 0; i < LENGTH; i++) {
                s1[i] = random.nextInt(LENGTH / 100);
                s2[i] = random.nextInt(LENGTH / 100);
            }
            SequenceEqualityMap temp = new LevenshteinDiff().compare(new IntSequencePair(s1, s2));
            if (compare == null || compare.getEqualRegions().length < temp.getEqualRegions().length) {
                compare = temp;
            }
        }
        System.err.println(compare.getEqualRegions().length);
        System.err.println(System.currentTimeMillis() - t0);
    }
}