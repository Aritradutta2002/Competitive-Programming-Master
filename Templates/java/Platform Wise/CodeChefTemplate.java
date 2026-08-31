import java.io.*;
import java.util.*;

/**
 * CodeChef Java Template (Refined)
 * Features: Compact FastIO, Array Utils, Pair Helpers.
 */
public class Main {
    static FastReader sc = new FastReader();
    static PrintWriter out = new PrintWriter(System.out);

    public static void main(String[] args) {
        int t = 1;
        try { String s = sc.next(); if (s != null) t = Integer.parseInt(s); } catch (Exception e) {}
        while (t-- > 0) solve();
        out.close();
    }

    static void solve() {
        // Your logic here
        // int n = sc.nextInt();
        // int[] a = sc.readIntArray(n);
    }

    // ==================== UTILS ====================
    static void printArray(int[] a) { for(int i=0; i<a.length; i++) out.print(a[i] + (i==a.length-1 ? "" : " ")); out.println(); }
    static void printArray(long[] a) { for(int i=0; i<a.length; i++) out.print(a[i] + (i==a.length-1 ? "" : " ")); out.println(); }
    
    static class Pair {
        int x, y; Pair(int x, int y) { this.x = x; this.y = y; }
    }
    
    static class LongPair {
        long x, y; LongPair(long x, long y) { this.x = x; this.y = y; }
    }

    // ==================== COMPACT FAST I/O ====================
    static class FastReader {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        String next() {
            while (st == null || !st.hasMoreElements()) { try { String line = br.readLine(); if (line == null) return null; st = new StringTokenizer(line); } catch (IOException e) {} }
            return st.nextToken();
        }
        int nextInt() { return Integer.parseInt(next()); }
        long nextLong() { return Long.parseLong(next()); }
        int[] readIntArray(int n) { int[] a = new int[n]; for (int i = 0; i < n; i++) a[i] = nextInt(); return a; }
        long[] readLongArray(int n) { long[] a = new long[n]; for (int i = 0; i < n; i++) a[i] = nextLong(); return a; }
    }
}
