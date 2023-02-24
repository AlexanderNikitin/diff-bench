package algorithm;

public class IntSequencePair extends AbstractSequencePair<int[]> {
    public IntSequencePair(int[] s1, int[] s2) {
        super(s1, s2);
    }

    @Override
    public boolean equal(int index1, int index2) {
        return s1[index1] == s2[index2];
    }

    @Override
    protected int getLength(int[] s) {
        return s.length;
    }
}
