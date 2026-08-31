import java.io.*;
import java.util.*;

/**
 * CodeForces Java Template (Refined)
 * Features: Compact FastIO, Anti-hack Sort, Array Utils, YES/NO helpers.
 */
public class CodeForcesTemplate {
    static FastReader in = new FastReader();
    static PrintWriter out = new PrintWriter(System.out);
    static final Random random = new Random();
    static final int MOD = 1_000_000_007;

    public static void main(String[] args) {
        int t = 1;
        try { String s = in.next(); if (s != null) t = Integer.parseInt(s); } catch (Exception e) {}
        while (t-- > 0) solve();
        out.flush(); out.close();
    }

    static void solve() {
        // Your logic here
        int n = in.nextInt();
        int[] a = in.readIntArray(n);
        
        // printArray(a);
    }

    // ==================== UTILS ====================
    static void yes() { out.println("YES"); }
    static void no() { out.println("NO"); }
    static void printArray(int[] a) { for(int i=0; i<a.length; i++) out.print(a[i] + (i==a.length-1 ? "" : " ")); out.println(); }
    static void printArray(long[] a) { for(int i=0; i<a.length; i++) out.print(a[i] + (i==a.length-1 ? "" : " ")); out.println(); }
    
    static void shuffleSort(int[] a) {
        int n = a.length;
        for (int i = 0; i < n; i++) { int r = i + random.nextInt(n - i); int tmp = a[r]; a[r] = a[i]; a[i] = tmp; }
        Arrays.sort(a);
    }

    static long gcd(long a, long b) { return b == 0 ? a : gcd(b, a % b); }
    static long power(long x, long y, long m) {
        long res = 1; x %= m;
        while (y > 0) { if ((y & 1) == 1) res = (res * x) % m; y >>= 1; x = (x * x) % m; }
        return res;
    }

    // ==================== COMPACT FAST I/O ====================
    static class FastReader {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        String next() {
            while (st == null || !st.hasMoreElements()) { try { String line = br.readLine(); if (line == null) return null; st = new StringTokenizer(line); } catch (IOException e) { e.printStackTrace(); } }
            return st.nextToken();
        }
        int nextInt() { return Integer.parseInt(next()); }
        long nextLong() { return Long.parseLong(next()); }
        double nextDouble() { return Double.parseDouble(next()); }
        int[] readIntArray(int n) { int[] a = new int[n]; for (int i = 0; i < n; i++) a[i] = nextInt(); return a; }
        long[] readLongArray(int n) { long[] a = new long[n]; for (int i = 0; i < n; i++) a[i] = nextLong(); return a; }
    }
}
