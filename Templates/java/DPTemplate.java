/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * DP Template - Common DP patterns for CSES/CF
 * Uses optimized I/O for CSES acceptance
 */
import java.io.*;
import java.util.*;

public class DPTemplate {
    static BufferedReader br;
    static StringTokenizer st;
    static PrintWriter out;
    
    static final int MOD = 1_000_000_007;
    static final long INF = (long) 1e18;
    
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(new BufferedOutputStream(System.out));
        
        solve();
        
        out.flush();
        out.close();
    }
    
    static void solve() throws IOException {
        // Example: Coin Change
        int n = nextInt(), target = nextInt();
        int[] coins = nextIntArray(n);
        out.println(countWays(coins, target));
    }
    
    // ==================== Coin Change (Min Coins) ====================
    static int minCoins(int[] coins, int target) {
        int[] dp = new int[target + 1];
        Arrays.fill(dp, (int) 1e9);
        dp[0] = 0;
        
        for (int i = 1; i <= target; i++) {
            for (int c : coins) {
                if (c <= i && dp[i - c] + 1 < dp[i]) {
                    dp[i] = dp[i - c] + 1;
                }
            }
        }
        return dp[target] >= 1e9 ? -1 : dp[target];
    }
    
    // ==================== Coin Change (Count Ways) ====================
    static long countWays(int[] coins, int target) {
        long[] dp = new long[target + 1];
        dp[0] = 1;
        
        for (int c : coins) {
            for (int i = c; i <= target; i++) {
                dp[i] = (dp[i] + dp[i - c]) % MOD;
            }
        }
        return dp[target];
    }
    
    // ==================== 0/1 Knapsack ====================
    static long knapsack01(int[] weights, long[] values, int capacity) {
        int n = weights.length;
        long[] dp = new long[capacity + 1];
        
        for (int i = 0; i < n; i++) {
            for (int w = capacity; w >= weights[i]; w--) {
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
            }
        }
        return dp[capacity];
    }
    
    // ==================== LIS (Longest Increasing Subsequence) ====================
    static int lis(int[] arr) {
        List<Integer> dp = new ArrayList<>();
        
        for (int x : arr) {
            int pos = Collections.binarySearch(dp, x);
            if (pos < 0) pos = -(pos + 1);
            
            if (pos == dp.size()) dp.add(x);
            else dp.set(pos, x);
        }
        return dp.size();
    }
    
    // ==================== LCS (Longest Common Subsequence) ====================
    static int lcs(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[n][m];
    }
    
    // ==================== Edit Distance ====================
    static int editDistance(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], 
                                   Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        return dp[n][m];
    }
    
    // ==================== Subset Sum ====================
    static boolean subsetSum(int[] arr, int target) {
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        
        for (int x : arr) {
            for (int i = target; i >= x; i--) {
                dp[i] = dp[i] || dp[i - x];
            }
        }
        return dp[target];
    }
    
    // ==================== Grid Paths (Count ways) ====================
    static long gridPaths(int n, int m, char[][] grid) {
        long[][] dp = new long[n][m];
        dp[0][0] = (grid[0][0] == '.') ? 1 : 0;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == '*') { dp[i][j] = 0; continue; }
                if (i > 0) dp[i][j] = (dp[i][j] + dp[i - 1][j]) % MOD;
                if (j > 0) dp[i][j] = (dp[i][j] + dp[i][j - 1]) % MOD;
            }
        }
        return dp[n - 1][m - 1];
    }
    
    // ==================== FAST I/O ====================
    static String next() throws IOException {
        while (st == null || !st.hasMoreTokens())
            st = new StringTokenizer(br.readLine());
        return st.nextToken();
    }
    
    static int nextInt() throws IOException { return Integer.parseInt(next()); }
    static long nextLong() throws IOException { return Long.parseLong(next()); }
    
    static int[] nextIntArray(int n) throws IOException {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = nextInt();
        return arr;
    }
}
