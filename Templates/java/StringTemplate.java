/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * STRING Template - KMP, Z-function, Hashing, Trie
 * Uses optimized I/O for CSES acceptance
 */
import java.io.*;
import java.util.*;

public class StringTemplate {
    static BufferedReader br;
    static StringTokenizer st;
    static PrintWriter out;
    
    static final long MOD = 1_000_000_007;
    static final long BASE = 31;
    
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
        
        List<Integer> matches = findPattern(text, pattern);
        out.println(matches.size());
    }
    
    // ==================== Z-Function ====================
    static int[] zFunction(String s) {
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
        int[] z = zFunction(combined);
        List<Integer> matches = new ArrayList<>();
        int plen = pattern.length();
        
        for (int i = plen + 1; i < combined.length(); i++) {
            if (z[i] == plen) matches.add(i - plen - 1);
        }
        return matches;
    }
    
    // ==================== KMP (Prefix Function) ====================
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
    
    // KMP pattern matching
    static List<Integer> kmpSearch(String text, String pattern) {
        String combined = pattern + "#" + text;
        int[] pi = prefixFunction(combined);
        List<Integer> matches = new ArrayList<>();
        int plen = pattern.length();
        
        for (int i = plen + 1; i < combined.length(); i++) {
            if (pi[i] == plen) matches.add(i - 2 * plen);
        }
        return matches;
    }
    
    // ==================== String Hashing ====================
    static class StringHash {
        long[] hash, pw;
        long base, mod;
        
        StringHash(String s, long base, long mod) {
            this.base = base;
            this.mod = mod;
            int n = s.length();
            hash = new long[n + 1];
            pw = new long[n + 1];
            pw[0] = 1;
            
            for (int i = 0; i < n; i++) {
                hash[i + 1] = (hash[i] * base + s.charAt(i) - 'a' + 1) % mod;
                pw[i + 1] = pw[i] * base % mod;
            }
        }
        
        StringHash(String s) {
            this(s, BASE, MOD);
        }
        
        // Get hash of substring [l, r] (0-indexed, inclusive)
        long getHash(int l, int r) {
            return (hash[r + 1] - hash[l] * pw[r - l + 1] % mod + mod) % mod;
        }
    }
    
    // ==================== Trie ====================
    static class Trie {
        int[][] children;
        int[] cnt, prefixCnt;
        int nodeCount;
        
        Trie(int maxNodes) {
            children = new int[maxNodes][26];
            cnt = new int[maxNodes];
            prefixCnt = new int[maxNodes];
            nodeCount = 1;
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
    }
    
    // ==================== Manacher's Algorithm (Palindromes) ====================
    static int[] manacher(String s) {
        StringBuilder t = new StringBuilder("#");
        for (char c : s.toCharArray()) { t.append(c); t.append('#'); }
        
        int n = t.length();
        int[] p = new int[n];
        int c = 0, r = 0;
        
        for (int i = 0; i < n; i++) {
            if (i < r) p[i] = Math.min(r - i, p[2 * c - i]);
            while (i - p[i] - 1 >= 0 && i + p[i] + 1 < n && 
                   t.charAt(i - p[i] - 1) == t.charAt(i + p[i] + 1)) p[i]++;
            if (i + p[i] > r) { c = i; r = i + p[i]; }
        }
        return p;
    }
    
    // Longest palindromic substring length
    static int longestPalindrome(String s) {
        int[] p = manacher(s);
        int max = 0;
        for (int x : p) max = Math.max(max, x);
        return max;
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
