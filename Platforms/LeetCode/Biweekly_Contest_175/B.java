
/*
 * Author  : Aritra Dutta
 * Platform  : Leetcode Biweekly Contest 175
 * Created : 31.01.2026 19:51
 */
import java.io.*;
import java.util.*;

public class B {
    static BufferedReader br;
    static StringTokenizer st;
    static PrintWriter out;

    static final int MOD = 1_000_000_007;
    static final int MOD2 = 998244353;
    static final long INF = (long) 1e18;
    static final Random random = new Random();

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(new BufferedOutputStream(System.out));

        int t = nextInt();
        while (t-- > 0) {
            solve();
        }

        out.flush();
        out.close();
    }

    static void solve() throws IOException {
        int n = nextInt();

    }

    // ==================== FAST I/O ====================
    static String next() throws IOException {
        while (st == null || !st.hasMoreTokens())
            st = new StringTokenizer(br.readLine());
        return st.nextToken();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    static long nextLong() throws IOException {
        return Long.parseLong(next());
    }

    static double nextDouble() throws IOException {
        return Double.parseDouble(next());
    }

    static int[] nextIntArray(int n) throws IOException {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++)
            arr[i] = nextInt();
        return arr;
    }
}