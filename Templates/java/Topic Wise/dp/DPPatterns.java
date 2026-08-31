/*
 * Author  : Aritra Dutta
 * DP Patterns - Complete Dynamic Programming Library
 *
 * Includes: Classical DP, Digit DP, Bitmask DP, DP Optimizations
 *
 * USAGE: Copy specific patterns you need for your problem
 */
import java.io.*;
import java.util.*;

public class DPPatterns {
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
        out.println(ClassicalDP.countWays(coins, target));
    }

    // ==================== CLASSICAL DP PATTERNS ====================
    static class ClassicalDP {

        // ==================== Coin Change (Min Coins) ====================
        // Time: O(n * target), Space: O(target)
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
        // Time: O(n * target), Space: O(target)
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
        // Time: O(n * capacity), Space: O(capacity)
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

        // ==================== Unbounded Knapsack ====================
        // Time: O(n * capacity), Space: O(capacity)
        static long unboundedKnapsack(int[] weights, long[] values, int capacity) {
            int n = weights.length;
            long[] dp = new long[capacity + 1];

            for (int i = 0; i < n; i++) {
                for (int w = weights[i]; w <= capacity; w++) {
                    dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
                }
            }
            return dp[capacity];
        }

        // ==================== LIS (Longest Increasing Subsequence) ====================
        // O(n²) version
        static int lisN2(int[] arr) {
            int n = arr.length;
            int[] dp = new int[n];
            Arrays.fill(dp, 1);

            for (int i = 1; i < n; i++) {
                for (int j = 0; j < i; j++) {
                    if (arr[j] < arr[i]) {
                        dp[i] = Math.max(dp[i], dp[j] + 1);
                    }
                }
            }

            int max = 0;
            for (int x : dp) max = Math.max(max, x);
            return max;
        }

        // O(n log n) version using binary search
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

        // Reconstruct LIS
        static List<Integer> lisReconstruct(int[] arr) {
            int n = arr.length;
            int[] dp = new int[n];
            int[] parent = new int[n];
            Arrays.fill(dp, 1);
            Arrays.fill(parent, -1);

            int maxLen = 0, maxIdx = 0;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < i; j++) {
                    if (arr[j] < arr[i] && dp[j] + 1 > dp[i]) {
                        dp[i] = dp[j] + 1;
                        parent[i] = j;
                    }
                }
                if (dp[i] > maxLen) {
                    maxLen = dp[i];
                    maxIdx = i;
                }
            }

            List<Integer> lis = new ArrayList<>();
            for (int i = maxIdx; i != -1; i = parent[i]) {
                lis.add(arr[i]);
            }
            Collections.reverse(lis);
            return lis;
        }

        // ==================== LDS (Longest Decreasing Subsequence) ====================
        static int lds(int[] arr) {
            int n = arr.length;
            int[] reversed = new int[n];
            for (int i = 0; i < n; i++) reversed[i] = -arr[i];
            return lis(reversed);
        }

        // ==================== LCS (Longest Common Subsequence) ====================
        // Time: O(n * m), Space: O(n * m)
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

        // Reconstruct LCS
        static String lcsReconstruct(String a, String b) {
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

            StringBuilder sb = new StringBuilder();
            int i = n, j = m;
            while (i > 0 && j > 0) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    sb.append(a.charAt(i - 1));
                    i--; j--;
                } else if (dp[i - 1][j] > dp[i][j - 1]) {
                    i--;
                } else {
                    j--;
                }
            }
            return sb.reverse().toString();
        }

        // ==================== Edit Distance (Levenshtein) ====================
        // Time: O(n * m), Space: O(n * m)
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

        // ==================== Longest Palindromic Subsequence ====================
        // Time: O(n²), Space: O(n²)
        static int longestPalindromicSubsequence(String s) {
            int n = s.length();
            int[][] dp = new int[n][n];

            for (int i = 0; i < n; i++) dp[i][i] = 1;

            for (int len = 2; len <= n; len++) {
                for (int i = 0; i <= n - len; i++) {
                    int j = i + len - 1;
                    if (s.charAt(i) == s.charAt(j)) {
                        dp[i][j] = 2 + (len == 2 ? 0 : dp[i + 1][j - 1]);
                    } else {
                        dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                    }
                }
            }
            return dp[0][n - 1];
        }

        // ==================== Longest Palindromic Substring ====================
        // Time: O(n²), Space: O(n²)
        static String longestPalindromicSubstring(String s) {
            int n = s.length();
            boolean[][] dp = new boolean[n][n];
            int start = 0, maxLen = 1;

            for (int i = 0; i < n; i++) dp[i][i] = true;

            for (int i = 0; i < n - 1; i++) {
                if (s.charAt(i) == s.charAt(i + 1)) {
                    dp[i][i + 1] = true;
                    start = i;
                    maxLen = 2;
                }
            }

            for (int len = 3; len <= n; len++) {
                for (int i = 0; i <= n - len; i++) {
                    int j = i + len - 1;
                    if (s.charAt(i) == s.charAt(j) && dp[i + 1][j - 1]) {
                        dp[i][j] = true;
                        start = i;
                        maxLen = len;
                    }
                }
            }
            return s.substring(start, start + maxLen);
        }

        // ==================== Subset Sum ====================
        // Time: O(n * target), Space: O(target)
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

        // Count subsets with given sum
        static int countSubsets(int[] arr, int target) {
            int[] dp = new int[target + 1];
            dp[0] = 1;

            for (int x : arr) {
                for (int i = target; i >= x; i--) {
                    dp[i] = (dp[i] + dp[i - x]) % MOD;
                }
            }
            return dp[target];
        }

        // ==================== Partition Equal Subset Sum ====================
        static boolean canPartition(int[] arr) {
            int sum = 0;
            for (int x : arr) sum += x;
            if (sum % 2 != 0) return false;
            return subsetSum(arr, sum / 2);
        }

        // ==================== Grid Paths (Count ways) ====================
        // Time: O(n * m), Space: O(n * m)
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

        // ==================== Maximum Subarray Sum (Kadane's) ====================
        // Time: O(n), Space: O(1)
        static long maxSubarraySum(long[] arr) {
            long maxSoFar = arr[0], maxEndingHere = arr[0];

            for (int i = 1; i < arr.length; i++) {
                maxEndingHere = Math.max(arr[i], maxEndingHere + arr[i]);
                maxSoFar = Math.max(maxSoFar, maxEndingHere);
            }
            return maxSoFar;
        }

        // Maximum Product Subarray
        static long maxProductSubarray(long[] arr) {
            long maxSoFar = arr[0], minSoFar = arr[0], result = arr[0];

            for (int i = 1; i < arr.length; i++) {
                long temp = Math.max(arr[i], Math.max(maxSoFar * arr[i], minSoFar * arr[i]));
                minSoFar = Math.min(arr[i], Math.min(maxSoFar * arr[i], minSoFar * arr[i]));
                maxSoFar = temp;
                result = Math.max(result, maxSoFar);
            }
            return result;
        }

        // ==================== Egg Dropping Puzzle ====================
        // Time: O(n * k²), Space: O(n * k)
        static int eggDrop(int eggs, int floors) {
            int[][] dp = new int[eggs + 1][floors + 1];

            for (int i = 0; i <= eggs; i++) dp[i][0] = 0;
            for (int j = 0; j <= floors; j++) dp[0][j] = (int) INF;

            for (int i = 1; i <= eggs; i++) {
                for (int j = 1; j <= floors; j++) {
                    dp[i][j] = (int) INF;
                    for (int k = 1; k <= j; k++) {
                        dp[i][j] = Math.min(dp[i][j], 1 + Math.max(dp[i - 1][k - 1], dp[i][j - k]));
                    }
                }
            }
            return dp[eggs][floors];
        }

        // Optimized Egg Drop using binary search - O(n * k * log k)
        static int eggDropOptimized(int eggs, int floors) {
            int[][] dp = new int[eggs + 1][floors + 1];

            for (int i = 0; i <= eggs; i++) dp[i][0] = 0;
            for (int j = 0; j <= floors; j++) dp[0][j] = (int) INF;

            for (int i = 1; i <= eggs; i++) {
                for (int j = 1; j <= floors; j++) {
                    int lo = 1, hi = j, ans = (int) INF;
                    while (lo <= hi) {
                        int mid = (lo + hi) / 2;
                        int broken = dp[i - 1][mid - 1];
                        int notBroken = dp[i][j - mid];
                        ans = Math.min(ans, 1 + Math.max(broken, notBroken));
                        if (broken < notBroken) lo = mid + 1;
                        else hi = mid - 1;
                    }
                    dp[i][j] = ans;
                }
            }
            return dp[eggs][floors];
        }

        // ==================== Matrix Chain Multiplication ====================
        // Time: O(n³), Space: O(n²)
        static int matrixChainMultiplication(int[] dims) {
            int n = dims.length - 1;
            int[][] dp = new int[n][n];

            for (int len = 2; len <= n; len++) {
                for (int i = 0; i <= n - len; i++) {
                    int j = i + len - 1;
                    dp[i][j] = (int) INF;
                    for (int k = i; k < j; k++) {
                        dp[i][j] = Math.min(dp[i][j],
                            dp[i][k] + dp[k + 1][j] + dims[i] * dims[k + 1] * dims[j + 1]);
                    }
                }
            }
            return dp[0][n - 1];
        }
    }

    // ==================== DIGIT DP ====================
    static class DigitDP {

        // Count numbers in [0, n] with property P
        // Example: Count numbers without consecutive 1s in binary
        static long countWithoutConsecutiveOnes(int n) {
            String binary = Integer.toBinaryString(n);
            int len = binary.length();
            int[][][] dp = new int[len][2][2]; // [pos][tight][prevOne]

            for (int[][] row : dp) {
                for (int[] cell : row) Arrays.fill(cell, -1);
            }

            return digitDpHelper(0, 1, 0, binary, dp);
        }

        static int digitDpHelper(int pos, int tight, int prevOne, String s, int[][][] dp) {
            if (pos == s.length()) return 1;
            if (dp[pos][tight][prevOne] != -1) return dp[pos][tight][prevOne];

            int limit = tight == 1 ? s.charAt(pos) - '0' : 1;
            int ans = 0;

            for (int d = 0; d <= limit; d++) {
                if (prevOne == 1 && d == 1) continue; // Skip consecutive 1s
                int newTight = (tight == 1 && d == limit) ? 1 : 0;
                ans += digitDpHelper(pos + 1, newTight, d == 1 ? 1 : 0, s, dp);
            }

            return dp[pos][tight][prevOne] = ans;
        }

        // Count numbers in [L, R] with digit sum = target
        static long countWithDigitSum(int L, int R, int target) {
            return countDigitSumUpTo(R, target) - countDigitSumUpTo(L - 1, target);
        }

        static long countDigitSumUpTo(int n, int target) {
            String s = String.valueOf(n);
            int len = s.length();
            long[][][] dp = new long[len][target + 1][2]; // [pos][sum][tight]

            for (long[][] row : dp) {
                for (long[] cell : row) Arrays.fill(cell, -1);
            }

            return digitSumHelper(0, 0, 1, s, target, dp);
        }

        static long digitSumHelper(int pos, int sum, int tight, String s, int target, long[][][] dp) {
            if (sum > target) return 0;
            if (pos == s.length()) return sum == target ? 1 : 0;
            if (dp[pos][sum][tight] != -1) return dp[pos][sum][tight];

            int limit = tight == 1 ? s.charAt(pos) - '0' : 9;
            long ans = 0;

            for (int d = 0; d <= limit; d++) {
                int newTight = (tight == 1 && d == limit) ? 1 : 0;
                ans += digitSumHelper(pos + 1, sum + d, newTight, s, target, dp);
            }

            return dp[pos][sum][tight] = ans;
        }

        // Count numbers divisible by K in range [L, R]
        static long countDivisible(int L, int R, int K) {
            return countDivisibleUpTo(R, K) - countDivisibleUpTo(L - 1, K);
        }

        static long countDivisibleUpTo(int n, int K) {
            String s = String.valueOf(n);
            int len = s.length();
            long[][][] dp = new long[len][K][2];

            for (long[][] row : dp) {
                for (long[] cell : row) Arrays.fill(cell, -1);
            }

            return divisibleHelper(0, 0, 1, s, K, dp);
        }

        static long divisibleHelper(int pos, int rem, int tight, String s, int K, long[][][] dp) {
            if (pos == s.length()) return rem == 0 ? 1 : 0;
            if (dp[pos][rem][tight] != -1) return dp[pos][rem][tight];

            int limit = tight == 1 ? s.charAt(pos) - '0' : 9;
            long ans = 0;

            for (int d = 0; d <= limit; d++) {
                int newTight = (tight == 1 && d == limit) ? 1 : 0;
                int newRem = (rem * 10 + d) % K;
                ans += divisibleHelper(pos + 1, newRem, newTight, s, K, dp);
            }

            return dp[pos][rem][tight] = ans;
        }
    }

    // ==================== BITMASK DP ====================
    static class BitmaskDP {

        // Traveling Salesman Problem - O(n² * 2^n)
        static int tsp(int[][] dist, int start) {
            int n = dist.length;
            int[][] dp = new int[1 << n][n];
            int INF = (int) 1e9;

            for (int[] row : dp) Arrays.fill(row, INF);
            dp[1 << start][start] = 0;

            for (int mask = 0; mask < (1 << n); mask++) {
                for (int u = 0; u < n; u++) {
                    if ((mask & (1 << u)) == 0) continue;
                    if (dp[mask][u] == INF) continue;

                    for (int v = 0; v < n; v++) {
                        if ((mask & (1 << v)) != 0) continue;
                        int newMask = mask | (1 << v);
                        dp[newMask][v] = Math.min(dp[newMask][v], dp[mask][u] + dist[u][v]);
                    }
                }
            }

            int ans = INF;
            int fullMask = (1 << n) - 1;
            for (int i = 0; i < n; i++) {
                if (i != start) {
                    ans = Math.min(ans, dp[fullMask][i] + dist[i][start]);
                }
            }
            return ans;
        }

        // Count ways to tile 2×n grid with 2×1 dominoes
        static long countTilings(int n) {
            if (n % 2 != 0) return 0;
            int m = n / 2;

            long[][] dp = new long[m + 1][1 << (n / 2 + 1)];
            dp[0][0] = 1;

            for (int i = 0; i < m; i++) {
                for (int mask = 0; mask < (1 << (n / 2 + 1)); mask++) {
                    if (dp[i][mask] == 0) continue;
                    // Try all ways to fill column i
                    generateTilings(0, mask, 0, i, dp);
                }
            }

            return dp[m][0];
        }

        static void generateTilings(int row, int mask, int newMask, int col, long[][] dp) {
            if (row == dp[0].length / 2) {
                dp[col + 1][newMask] += dp[col][mask];
                return;
            }

            if ((mask & (1 << row)) != 0) {
                generateTilings(row + 1, mask, newMask, col, dp);
            } else {
                // Place horizontal domino
                if (row + 1 < dp[0].length / 2 && (mask & (1 << (row + 1))) == 0) {
                    generateTilings(row + 2, mask, newMask, col, dp);
                }
                // Place vertical domino
                generateTilings(row + 1, mask, newMask | (1 << row), col, dp);
            }
        }

        // Maximum independent set on small graph - O(2^n * n)
        static int maxIndependentSet(int n, int[] adjMask) {
            int[] dp = new int[1 << n];
            Arrays.fill(dp, -1);

            return maxISHelper(0, n, adjMask, dp);
        }

        static int maxISHelper(int mask, int n, int[] adjMask, int[] dp) {
            if (mask == (1 << n) - 1) return 0;
            if (dp[mask] != -1) return dp[mask];

            int max = 0;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) {
                    // Check if vertex i can be added (no neighbors already in set)
                    if ((adjMask[i] & mask) == 0) {
                        max = Math.max(max, 1 + maxISHelper(mask | (1 << i), n, adjMask, dp));
                    }
                }
            }
            return dp[mask] = max;
        }
    }

    // ==================== DP OPTIMIZATIONS ====================
    static class DPOptimizations {

        // Convex Hull Trick - for DP of form: dp[i] = min/max(a[j] * x[i] + b[j]) + C[i]
        static class ConvexHullTrick {
            static class Line {
                long m, b;
                Line(long m, long b) { this.m = m; this.b = b; }
                long eval(long x) { return m * x + b; }
            }

            Deque<Line> hull = new ArrayDeque<>();

            // Check if line l2 is unnecessary (l1, l2, l3 form upper hull)
            boolean isBad(Line l1, Line l2, Line l3) {
                return (l3.b - l1.b) * (l1.m - l2.m) <= (l2.b - l1.b) * (l1.m - l3.m);
            }

            // Add line with decreasing slope
            void addLine(long m, long b) {
                Line newLine = new Line(m, b);
                while (hull.size() >= 2) {
                    Line l2 = hull.removeLast();
                    Line l1 = hull.removeLast();
                    if (!isBad(l1, l2, newLine)) {
                        hull.addLast(l1);
                        hull.addLast(l2);
                        break;
                    }
                    hull.addLast(l1);
                }
                if (hull.size() < 2) hull.addLast(newLine);
                else {
                    Line l2 = hull.removeLast();
                    Line l1 = hull.removeLast();
                    if (isBad(l1, l2, newLine)) {
                        hull.addLast(l1);
                        hull.addLast(newLine);
                    } else {
                        hull.addLast(l1);
                        hull.addLast(l2);
                        hull.addLast(newLine);
                    }
                }
            }

            // Query minimum at x (for increasing x)
            long query(long x) {
                while (hull.size() >= 2 && hull.peekFirst().eval(x) >= hull.toArray(new Line[0])[1].eval(x)) {
                    hull.removeFirst();
                }
                return hull.peekFirst().eval(x);
            }
        }

        // Knuth Optimization - for DP of form: dp[i][j] = min(dp[i][k] + dp[k+1][j]) + C[i][j]
        // Reduces O(n³) to O(n²)
        static int[][] knuthOptimization(int n, int[][] cost) {
            int[][] dp = new int[n + 1][n + 1];
            int[][] opt = new int[n + 1][n + 1];

            for (int i = 1; i <= n; i++) {
                dp[i][i] = 0;
                opt[i][i] = i;
            }

            for (int len = 2; len <= n; len++) {
                for (int i = 1; i <= n - len + 1; i++) {
                    int j = i + len - 1;
                    dp[i][j] = (int) INF;

                    for (int k = opt[i][j - 1]; k <= opt[i + 1][j]; k++) {
                        int val = dp[i][k] + dp[k + 1][j] + cost[i][j];
                        if (val < dp[i][j]) {
                            dp[i][j] = val;
                            opt[i][j] = k;
                        }
                    }
                }
            }
            return dp;
        }

        // Divide and Conquer Optimization - for DP of form: dp[i][j] = min(dp[i-1][k] + C[k+1][j])
        // Reduces O(n²k) to O(nk log n)
        static void dncOptimization(int n, int k, long[][] dp, long[][] cost) {
            compute(1, n, 0, n - 1, k - 1, dp, cost);
        }

        static void compute(int i, int j, int optL, int optR, int prev, long[][] dp, long[][] cost) {
            if (i > j) return;

            int mid = (i + j) / 2;
            int opt = optL;
            dp[prev + 1][mid] = INF;

            for (int t = optL; t <= Math.min(mid, optR); t++) {
                long val = dp[prev][t] + cost[t + 1][mid];
                if (val < dp[prev + 1][mid]) {
                    dp[prev + 1][mid] = val;
                    opt = t;
                }
            }

            compute(i, mid - 1, optL, opt, prev, dp, cost);
            compute(mid + 1, j, opt, optR, prev, dp, cost);
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

    static int[] nextIntArray(int n) throws IOException {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = nextInt();
        return arr;
    }

    static long[] nextLongArray(int n) throws IOException {
        long[] arr = new long[n];
        for (int i = 0; i < n; i++) arr[i] = nextLong();
        return arr;
    }
}
