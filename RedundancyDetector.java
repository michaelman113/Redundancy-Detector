import java.util.Arrays;

public class RedundancyDetector {

    // Function to build suffix array for the input string
    private static int[] buildSuffixArray(String s) {
        int n = s.length();
        Integer[] suffixes = new Integer[n];
        int[] suffixArray = new int[n];
        int[] rank = new int[n];
        int[] temp = new int[n];

        for (int i = 0; i < n; i++) {
            suffixes[i] = i;
            rank[i] = s.charAt(i);
        }

        for (int k = 1; k < n; k *= 2) {
            final int K = k;
            Arrays.sort(suffixes, (a, b) -> {
                if (rank[a] != rank[b]) return Integer.compare(rank[a], rank[b]);
                int rankA = a + K < n ? rank[a + K] : -1;
                int rankB = b + K < n ? rank[b + K] : -1;
                return Integer.compare(rankA, rankB);
            });

            temp[suffixes[0]] = 0;
            for (int i = 1; i < n; i++) {
                temp[suffixes[i]] = temp[suffixes[i - 1]];
                if (rank[suffixes[i]] != rank[suffixes[i - 1]] ||
                    suffixes[i] + k >= n ||
                    suffixes[i - 1] + k >= n ||
                    rank[suffixes[i] + k] != rank[suffixes[i - 1] + k]) {
                    temp[suffixes[i]]++;
                }
            }
            System.arraycopy(temp, 0, rank, 0, n);
        }

        for (int i = 0; i < n; i++) {
            suffixArray[i] = suffixes[i];
        }
        return suffixArray;
    }

    // Function to find the longest common prefix array using the suffix array
    private static int[] buildLCPArray(String s, int[] suffixArray) {
        int n = s.length();
        int[] lcp = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) rank[suffixArray[i]] = i;
        for (int i = 0, h = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = suffixArray[rank[i] - 1];
                while (i + h < n && j + h < n && s.charAt(i + h) == s.charAt(j + h)) h++;
                lcp[rank[i]] = h;
                if (h > 0) h--;
            }
        }
        return lcp;
    }

    // Function to find the longest repeated substring using the suffix and LCP arrays
    public static String findLongestRepeatedSubstring(String s) {
        int n = s.length();
        int[] suffixArray = buildSuffixArray(s);
        int[] lcp = buildLCPArray(s, suffixArray);

        int maxLength = 0;
        int startIndex = 0;

        for (int i = 1; i < n; i++) {
            if (lcp[i] > maxLength) {
                maxLength = lcp[i];
                startIndex = suffixArray[i];
            }
        }

        return s.substring(startIndex, startIndex + maxLength);
    }

    public static void main(String[] args) {
        String input = "abracadabra";
        String result = findLongestRepeatedSubstring(input);
        System.out.println(result.length());
        System.out.println(result);
    }
}

