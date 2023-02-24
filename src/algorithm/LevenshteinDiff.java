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

        for (int i = 1; i < n; i++) {
            final CacheHolder[] prevCacheLine = cache[(i + 1) % 2];
            final CacheHolder[] currentCacheLine = cache[i % 2];

            for (int j = 1; j < m; j++) {
                final int prevJ = j - 1;

                final CacheHolder prevICurJ = prevCacheLine[j];
                final CacheHolder curIPrevJ = currentCacheLine[prevJ];

                final int a = prevICurJ.count;
                final int b = curIPrevJ.count;

                if (sequencePair.equal(i - 1, prevJ)) {
                    final CacheHolder prevIPrevJ = prevCacheLine[prevJ];
                    final int c = prevIPrevJ.count + 1;
                    if (c >= a && c >= b) {
                        currentCacheLine[j] = new CacheHolder(c, C, prevIPrevJ);
                    }
                } else {
                    if (a == b && i < j || a > b) {
                        currentCacheLine[j] = new CacheHolder(a, A, prevICurJ);
                    } else {
                        currentCacheLine[j] = new CacheHolder(b, B, curIPrevJ);
                    }
                }
            }
        }

        int[][] result = new int[Math.min(sequencePair.getLength1(), sequencePair.getLength2())][];
        int index = result.length;
        CacheHolder current = cache[sequencePair.getLength1() % 2][sequencePair.getLength2()];
        for (int i = sequencePair.getLength1(), j = sequencePair.getLength2();
             i > 0 && j > 0; ) {
            switch (current.side) {
                case C -> result[--index] = new int[]{--i, --j};
                case A -> i--;
                case B -> j--;
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

    private record CacheHolder(int count, int side, CacheHolder prev) {
    }
}
