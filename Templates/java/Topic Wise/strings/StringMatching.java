/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * STRING MATCHING Template - Pattern Matching & Palindrome Algorithms
 * Includes: KMP, Z-function, String Hashing (single + double), Manacher's algorithm
 *
 * USAGE: Import specific classes or copy algorithms you need
 */
import java.io.*;
import java.util.*;

public class StringMatching {
    static BufferedReader br;
    static StringTokenizer st;
    static PrintWriter out;

    static final long MOD = 1_000_000_007;
    static final long BASE = 31;
    static final long MOD2 = 1_000_000_009;
    static final long BASE2 = 37;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(new BufferedOutputStream(System.out));
        solve();
        out.flush();
        out.close();
    }

    static void solve() throws IOException {
        // TODO: implement solution
    }

    // ==================== KMP (Knuth-Morris-Pratt) ====================
    static class KMP {
        // Compute prefix function (pi array)
        // pi[i] = length of longest proper prefix of s[0..i] that is also a suffix
        // Time: O(n)
        static int[] prefixFunction(String s) {
            int n = s.length();
            int[] pi = new int[n];

            for (int i = 1; i < n; i++) {
                int j = pi[i - 1];
                while (j > 0 && s.charAt(i) != s.charAt(j)) j = pi[j - 1];
                if (s.charAt(i) == s.charAt(j)) j++;
                pi[i] = j;
            }
            return pi;
        }

        // Pattern matching using KMP
        // Time: O(n + m)
        static List<Integer> search(String text, String pattern) {
            String combined = pattern + "#" + text;
            int[] pi = prefixFunction(combined);
            List<Integer> matches = new ArrayList<>();
            int plen = pattern.length();

            for (int i = plen + 1; i < combined.length(); i++) {
                if (pi[i] == plen) matches.add(i - 2 * plen);
            }
            return matches;
        }

        // Count occurrences of pattern in text
        static int countOccurrences(String text, String pattern) {
            return search(text, pattern).size();
        }

        // Find first occurrence
        static int findFirst(String text, String pattern) {
            List<Integer> matches = search(text, pattern);
            return matches.isEmpty() ? -1 : matches.get(0);
        }

        // Check if pattern is a substring
        static boolean contains(String text, String pattern) {
            return findFirst(text, pattern) != -1;
        }

        // Find period of string (smallest p such that s[i] = s[i+p] for all valid i)
        static int findPeriod(String s) {
            int n = s.length();
            int[] pi = prefixFunction(s);
            int k = n - pi[n - 1];
            return (n % k == 0) ? k : n;
        }

        // Check if string is periodic (can be formed by repeating a substring)
        static boolean isPeriodic(String s) {
            return findPeriod(s) < s.length();
        }
    }

    // ==================== Z-FUNCTION ====================
    static class ZFunction {
        // Compute Z-array: z[i] = length of longest common prefix between s and s[i:]
        // Time: O(n)
        static int[] compute(String s) {
            int n = s.length();
            int[] z = new int[n];
            int l = 0, r = 0;

            for (int i = 1; i < n; i++) {
                if (i < r) z[i] = Math.min(r - i, z[i - l]);
                while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) z[i]++;
                if (i + z[i] > r) { l = i; r = i + z[i]; }
            }
            return z;
        }

        // Pattern matching using Z-function
        static List<Integer> findPattern(String text, String pattern) {
            String combined = pattern + "$" + text;
            int[] z = compute(combined);
            List<Integer> matches = new ArrayList<>();
            int plen = pattern.length();

            for (int i = plen + 1; i < combined.length(); i++) {
                if (z[i] == plen) matches.add(i - plen - 1);
            }
            return matches;
        }

        // Count distinct substrings using Z-function
        static int countDistinctSubstrings(String s) {
            int count = 0;
            for (int i = 0; i < s.length(); i++) {
                String suffix = s.substring(i);
                int[] z = compute(suffix);
                count += suffix.length();
                for (int j = 1; j < z.length; j++) {
                    count -= z[j];
                }
            }
            return count;
        }
    }

    // ==================== STRING HASHING ====================
    static class StringHashing {
        long[] hash, pw;
        long base, mod;
        String s;

        StringHashing(String s, long base, long mod) {
            this.s = s;
            this.base = base;
            this.mod = mod;
            int n = s.length();
            hash = new long[n + 1];
            pw = new long[n + 1];
            pw[0] = 1;

            for (int i = 0; i < n; i++) {
                hash[i + 1] = (hash[i] * base + s.charAt(i)) % mod;
                pw[i + 1] = pw[i] * base % mod;
            }
        }

        StringHashing(String s) {
            this(s, BASE, MOD);
        }

        // Get hash of substring [l, r] (0-indexed, inclusive)
        long getHash(int l, int r) {
            return (hash[r + 1] - hash[l] * pw[r - l + 1] % mod + mod) % mod;
        }

        // Get hash of entire string
        long getHash() { return hash[s.length()]; }

        // Check if two substrings are equal
        boolean equals(int l1, int r1, int l2, int r2) {
            if (r1 - l1 != r2 - l2) return false;
            return getHash(l1, r1) == getHash(l2, r2);
        }

        // Find longest common prefix of s[i:] and s[j:]
        int lcp(int i, int j, int maxLen) {
            int lo = 0, hi = maxLen;
            while (lo < hi) {
                int mid = (lo + hi + 1) / 2;
                if (equals(i, i + mid - 1, j, j + mid - 1)) lo = mid;
                else hi = mid - 1;
            }
            return lo;
        }

        // Double hashing for extra safety
        static class DoubleHash {
            StringHashing h1, h2;

            DoubleHash(String s) {
                h1 = new StringHashing(s, BASE, MOD);
                h2 = new StringHashing(s, BASE2, MOD2);
            }

            long[] getHash(int l, int r) {
                return new long[]{h1.getHash(l, r), h2.getHash(l, r)};
            }

            boolean equals(int l1, int r1, int l2, int r2) {
                return h1.equals(l1, r1, l2, r2) && h2.equals(l1, r1, l2, r2);
            }
        }
    }

    // ==================== MANACHER'S ALGORITHM ====================
    static class Manacher {
        // Find all palindromic substrings
        // Returns array where d1[i] = number of odd-length palindromes centered at i
        // and d2[i] = number of even-length palindromes centered at i
        static int[][] compute(String s) {
            int n = s.length();
            int[] d1 = new int[n]; // odd length
            int[] d2 = new int[n]; // even length

            // Odd length palindromes
            for (int i = 0, l = 0, r = -1; i < n; i++) {
                int k = (i > r) ? 1 : Math.min(d1[l + r - i], r - i + 1);
                while (0 <= i - k && i + k < n && s.charAt(i - k) == s.charAt(i + k)) k++;
                d1[i] = k--;
                if (i + k > r) { l = i - k; r = i + k; }
            }

            // Even length palindromes
            for (int i = 0, l = 0, r = -1; i < n; i++) {
                int k = (i > r) ? 0 : Math.min(d2[l + r - i + 1], r - i + 1);
                while (0 <= i - k - 1 && i + k < n && s.charAt(i - k - 1) == s.charAt(i + k)) k++;
                d2[i] = k--;
                if (i + k > r) { l = i - k - 1; r = i + k; }
            }

            return new int[][]{d1, d2};
        }

        // Longest palindromic substring
        static String longestPalindrome(String s) {
            int[][] d = compute(s);
            int maxLen = 0, center = 0;
            boolean isOdd = true;

            for (int i = 0; i < s.length(); i++) {
                if (d[0][i] > maxLen) {
                    maxLen = d[0][i];
                    center = i;
                    isOdd = true;
                }
                if (d[1][i] > maxLen) {
                    maxLen = d[1][i];
                    center = i;
                    isOdd = false;
                }
            }

            if (isOdd) {
                int start = center - maxLen + 1;
                return s.substring(start, start + 2 * maxLen - 1);
            } else {
                int start = center - maxLen;
                return s.substring(start, start + 2 * maxLen);
            }
        }

        // Count total palindromic substrings
        static int countPalindromes(String s) {
            int[][] d = compute(s);
            int count = 0;
            for (int x : d[0]) count += x;
            for (int x : d[1]) count += x;
            return count;
        }
    }

    // ==================== FAST I/O ====================
    static String next() throws IOException {
        while (st == null || !st.hasMoreTokens())
            st = new StringTokenizer(br.readLine());
        return st.nextToken();
    }

    static int nextInt() throws IOException { return Integer.parseInt(next()); }
    static long nextLong() throws IOException { return Long.parseLong(next()); }
}
