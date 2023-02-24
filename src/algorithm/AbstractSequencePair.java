package algorithm;

public abstract class AbstractSequencePair<SequenceType> implements SequencePair {
    protected final SequenceType s1;
    protected final SequenceType s2;

    protected AbstractSequencePair(SequenceType s1, SequenceType s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    public int getLength1() {
        return getLength(s1);
    }

    @Override
    public int getLength2() {
        return getLength(s2);
    }

    protected abstract int getLength(SequenceType s);
}
