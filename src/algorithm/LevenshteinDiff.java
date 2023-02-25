package algorithm;

import java.util.Arrays;

public class LevenshteinDiff implements Diff {
    private static final int A = 1;
    private static final int B = 2;
    private static final int C = 3;
    private static final CacheHolder NULL = new CacheHolder(0, 0, null);

    @Override
    public SequenceEqualityMap compare(SequencePair sequencePair) {
        final int n = sequencePair.getLength1() + 1;
        final int m = sequencePair.getLength2() + 1;

        CacheHolder[][] cache = new CacheHolder[2][m];
        Arrays.fill(cache[0], NULL);
        cache[1][0] = NULL;
        CacheHolder[] prevCacheLine = cache[0];
        CacheHolder[] currentCacheLine = cache[1];

        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                final int prevJ = j - 1;

                if (sequencePair.equal(i - 1, prevJ)) {
                    final CacheHolder prevIPrevJ = prevCacheLine[prevJ];
                    currentCacheLine[j] = new CacheHolder(prevIPrevJ.count + 1, C, prevIPrevJ);
                } else {
                    final CacheHolder prevICurJ = prevCacheLine[j];
                    final CacheHolder curIPrevJ = currentCacheLine[prevJ];
                    final int a = prevICurJ.count;
                    final int b = curIPrevJ.count;
                    if (a > b || a == b && i < j) {
                        currentCacheLine[j] = new CacheHolder(a, A, prevICurJ);
                    } else {
                        currentCacheLine[j] = new CacheHolder(b, B, curIPrevJ);
                    }
                }
            }

            CacheHolder[] temp = prevCacheLine;
            prevCacheLine = currentCacheLine;
            currentCacheLine = temp;
        }

        int[][] result = new int[Math.min(sequencePair.getLength1(), sequencePair.getLength2())][];
        int index = result.length;
        CacheHolder current = cache[sequencePair.getLength1() % 2][sequencePair.getLength2()];
        for (int i = sequencePair.getLength1(), j = sequencePair.getLength2();
             i > 0 && j > 0; ) {
            switch (current.side) {
                case C:
                    result[--index] = new int[]{--i, --j};
                    break;
                case A:
                    i--;
                    break;
                case B:
                    j--;
                    break;
            }
            current = current.prev;
        }
        if (index > 0) {
            result = Arrays.copyOfRange(result, index, result.length);
        }
        final int[][] finalResult = result;
        return new SequenceEqualityMap() {
            @Override
            public int[][] getEqualRegions() {
                return finalResult;
            }

            @Override
            public int getSequenceCount() {
                return 2;
            }
        };
    }

    private static class CacheHolder {
        public final int count;
        public final int side;
        public final CacheHolder prev;

        public CacheHolder(int count, int side, CacheHolder prev) {
            this.count = count;
            this.side = side;
            this.prev = prev;
        }
    }
}
