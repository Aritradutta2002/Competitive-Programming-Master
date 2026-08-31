/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * STRING ALGORITHMS Template - Complete String Processing Library
 * Includes: KMP, Z-function, Hashing, Trie, Aho-Corasick, Suffix structures,
 *           Manacher, Lyndon factorization, etc.
 *
 * USAGE: Import specific classes or copy algorithms you need
 */
import java.io.*;
import java.util.*;

public class StringAlgorithms {
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
        String text = next();
        String pattern = next();

        List<Integer> matches = KMP.search(text, pattern);
        out.println(matches.size());
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

    // ==================== TRIE ====================
    static class Trie {
        int[][] children;
        int[] cnt, prefixCnt;
        int nodeCount;

        Trie(int maxNodes, int alphabetSize) {
            children = new int[maxNodes][alphabetSize];
            cnt = new int[maxNodes];
            prefixCnt = new int[maxNodes];
            nodeCount = 1;
        }

        Trie(int maxNodes) {
            this(maxNodes, 26);
        }

        void insert(String s) {
            int cur = 0;
            for (char c : s.toCharArray()) {
                int idx = c - 'a';
                if (children[cur][idx] == 0) {
                    children[cur][idx] = nodeCount++;
                }
                cur = children[cur][idx];
                prefixCnt[cur]++;
            }
            cnt[cur]++;
        }

        int countWord(String s) {
            int cur = 0;
            for (char c : s.toCharArray()) {
                int idx = c - 'a';
                if (children[cur][idx] == 0) return 0;
                cur = children[cur][idx];
            }
            return cnt[cur];
        }

        int countPrefix(String s) {
            int cur = 0;
            for (char c : s.toCharArray()) {
                int idx = c - 'a';
                if (children[cur][idx] == 0) return 0;
                cur = children[cur][idx];
            }
            return prefixCnt[cur];
        }

        boolean contains(String s) { return countWord(s) > 0; }
        boolean hasPrefix(String s) { return countPrefix(s) > 0; }

        // Count total words in trie
        int countWords() {
            int total = 0;
            for (int i = 1; i < nodeCount; i++) total += cnt[i];
            return total;
        }
    }

    // ==================== AHO-CORASICK ====================
    static class AhoCorasick {
        int[][] children;
        int[] fail;
        List<Integer>[] output;
        int nodeCount;

        @SuppressWarnings("unchecked")
        AhoCorasick(List<String> patterns) {
            int maxNodes = patterns.stream().mapToInt(String::length).sum() + 1;
            children = new int[maxNodes][26];
            fail = new int[maxNodes];
            output = new List[maxNodes];
            for (int i = 0; i < maxNodes; i++) output[i] = new ArrayList<>();
            nodeCount = 1;

            // Build trie
            for (int i = 0; i < patterns.size(); i++) {
                insert(patterns.get(i), i);
            }

            // Build failure links
            buildFailure();
        }

        void insert(String s, int patternIdx) {
            int cur = 0;
            for (char c : s.toCharArray()) {
                int idx = c - 'a';
                if (children[cur][idx] == 0) {
                    children[cur][idx] = nodeCount++;
                }
                cur = children[cur][idx];
            }
            output[cur].add(patternIdx);
        }

        void buildFailure() {
            Queue<Integer> queue = new ArrayDeque<>();

            // Initialize: depth 1 nodes fail to root
            for (int i = 0; i < 26; i++) {
                if (children[0][i] != 0) {
                    queue.add(children[0][i]);
                }
            }

            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int i = 0; i < 26; i++) {
                    if (children[u][i] != 0) {
                        fail[children[u][i]] = children[fail[u]][i];
                        output[children[u][i]].addAll(output[fail[children[u][i]]]);
                        queue.add(children[u][i]);
                    } else {
                        children[u][i] = children[fail[u]][i];
                    }
                }
            }
        }

        // Find all pattern occurrences in text
        List<int[]> search(String text) {
            List<int[]> matches = new ArrayList<>();
            int cur = 0;

            for (int i = 0; i < text.length(); i++) {
                cur = children[cur][text.charAt(i) - 'a'];
                for (int patternIdx : output[cur]) {
                    matches.add(new int[]{patternIdx, i});
                }
            }
            return matches;
        }

        // Count total occurrences
        int countOccurrences(String text) {
            int count = 0;
            int cur = 0;

            for (int i = 0; i < text.length(); i++) {
                cur = children[cur][text.charAt(i) - 'a'];
                count += output[cur].size();
            }
            return count;
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

    // ==================== SUFFIX ARRAY ====================
    static class SuffixArray {
        Integer[] sa; // Suffix array
        int[] rank; // Rank array
        int[] lcp; // LCP array

        SuffixArray(String s) {
            build(s);
        }

        // O(n log²n) construction
        void build(String s) {
            int n = s.length();
            sa = new Integer[n];
            rank = new int[n];
            int[] newRank = new int[n];

            // Initialize
            for (int i = 0; i < n; i++) {
                sa[i] = i;
                rank[i] = s.charAt(i);
            }

            for (int k = 1; k < n; k <<= 1) {
                final int K = k;
                Arrays.sort(sa, (a, b) -> {
                    if (rank[a] != rank[b]) return Integer.compare(rank[a], rank[b]);
                    int ra = (a + K < n) ? rank[a + K] : -1;
                    int rb = (b + K < n) ? rank[b + K] : -1;
                    return Integer.compare(ra, rb);
                });

                newRank[sa[0]] = 0;
                for (int i = 1; i < n; i++) {
                    int prev = sa[i - 1], curr = sa[i];
                    boolean same = rank[prev] == rank[curr];
                    int prevNext = (prev + K < n) ? rank[prev + K] : -1;
                    int currNext = (curr + K < n) ? rank[curr + K] : -1;
                    same &= prevNext == currNext;
                    newRank[curr] = newRank[prev] + (same ? 0 : 1);
                }

                System.arraycopy(newRank, 0, rank, 0, n);
                if (rank[sa[n - 1]] == n - 1) break;
            }

            // Compute LCP array
            computeLCP(s);
        }

        void computeLCP(String s) {
            int n = s.length();
            lcp = new int[n];
            int k = 0;

            for (int i = 0; i < n; i++) {
                if (rank[i] == n - 1) {
                    k = 0;
                    continue;
                }
                int j = sa[rank[i] + 1];
                while (i + k < n && j + k < n && s.charAt(i + k) == s.charAt(j + k)) k++;
                lcp[rank[i]] = k;
                if (k > 0) k--;
            }
        }

        // Count distinct substrings
        int countDistinctSubstrings() {
            int n = sa.length;
            int total = n * (n + 1) / 2;
            for (int x : lcp) total -= x;
            return total;
        }

        // Find longest common substring of two strings
        static String longestCommonSubstring(String s1, String s2) {
            String combined = s1 + "$" + s2 + "#";
            SuffixArray sa = new SuffixArray(combined);
            int n1 = s1.length(), n2 = s2.length();

            int maxLen = 0, startIdx = 0;
            for (int i = 0; i < sa.lcp.length; i++) {
                int pos1 = sa.sa[i], pos2 = sa.sa[i + 1];
                boolean oneInS1 = pos1 < n1 && pos2 > n1;
                boolean oneInS2 = pos1 > n1 && pos2 < n1;

                if ((oneInS1 || oneInS2) && sa.lcp[i] > maxLen) {
                    maxLen = sa.lcp[i];
                    startIdx = Math.min(pos1, pos2);
                }
            }

            if (startIdx < n1) {
                return combined.substring(startIdx, startIdx + maxLen);
            } else {
                return combined.substring(startIdx, startIdx + maxLen);
            }
        }
    }

    // ==================== SUFFIX AUTOMATON ====================
    static class SuffixAutomaton {
        static class State {
            int len, link;
            Map<Character, Integer> next = new HashMap<>();
            long cnt; // Number of occurrences

            State(int len, int link) {
                this.len = len;
                this.link = link;
            }
        }

        State[] st;
        int sz, last;

        SuffixAutomaton(String s) {
            st = new State[s.length() * 2 + 1];
            st[0] = new State(0, -1);
            sz = 1;
            last = 0;

            for (char c : s.toCharArray()) {
                extend(c);
            }

            // Count occurrences
            countOccurrences();
        }

        void extend(char c) {
            int cur = sz++;
            st[cur] = new State(st[last].len + 1, 0);
            st[cur].cnt = 1;

            int p = last;
            while (p != -1 && !st[p].next.containsKey(c)) {
                st[p].next.put(c, cur);
                p = st[p].link;
            }

            if (p == -1) {
                st[cur].link = 0;
            } else {
                int q = st[p].next.get(c);
                if (st[p].len + 1 == st[q].len) {
                    st[cur].link = q;
                } else {
                    int clone = sz++;
                    st[clone] = new State(st[p].len + 1, st[q].link);
                    st[clone].next = new HashMap<>(st[q].next);
                    st[clone].cnt = 0; // Clone doesn't count as occurrence

                    while (p != -1 && st[p].next.get(c) == q) {
                        st[p].next.put(c, clone);
                        p = st[p].link;
                    }
                    st[q].link = st[cur].link = clone;
                }
            }
            last = cur;
        }

        void countOccurrences() {
            // Sort states by length in decreasing order
            Integer[] order = new Integer[sz];
            for (int i = 0; i < sz; i++) order[i] = i;
            Arrays.sort(order, (a, b) -> Integer.compare(st[b].len, st[a].len));

            for (int i : order) {
                if (st[i].link != -1) {
                    st[st[i].link].cnt += st[i].cnt;
                }
            }
        }

        // Count distinct substrings
        long countDistinctSubstrings() {
            long count = 0;
            for (int i = 1; i < sz; i++) {
                count += st[i].len - st[st[i].link].len;
            }
            return count;
        }

        // Check if string contains substring s
        boolean contains(String s) {
            int cur = 0;
            for (char c : s.toCharArray()) {
                if (!st[cur].next.containsKey(c)) return false;
                cur = st[cur].next.get(c);
            }
            return true;
        }

        // Count occurrences of substring s
        long countOccurrences(String s) {
            int cur = 0;
            for (char c : s.toCharArray()) {
                if (!st[cur].next.containsKey(c)) return 0;
                cur = st[cur].next.get(c);
            }
            return st[cur].cnt;
        }
    }

    // ==================== LYNDON FACTORIZATION ====================
    static class LyndonFactorization {
        // Duval's algorithm - O(n)
        static List<String> compute(String s) {
            List<String> factors = new ArrayList<>();
            int n = s.length(), i = 0;

            while (i < n) {
                int j = i + 1, k = i;
                while (j < n && s.charAt(k) <= s.charAt(j)) {
                    if (s.charAt(k) < s.charAt(j)) {
                        k = i;
                    } else {
                        k++;
                    }
                    j++;
                }

                while (i <= k) {
                    factors.add(s.substring(i, i + j - k));
                    i += j - k;
                }
            }
            return factors;
        }

        // Find minimum cyclic shift
        static String minCyclicShift(String s) {
            s += s;
            List<String> factors = compute(s);
            return factors.get(factors.size() - 1);
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
