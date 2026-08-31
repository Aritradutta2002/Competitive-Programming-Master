/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * ADVANCED TOPICS Template - Game Theory, Randomized Algorithms, Misc
 * Includes: Nim Game, Grundy Numbers, Minimax, Alpha-Beta Pruning,
 *           Simulated Annealing, Ternary Search, etc.
 *
 * USAGE: Import specific classes or copy algorithms you need
 */
import java.io.*;
import java.util.*;

public class AdvancedTopics {
    static BufferedReader br;
    static StringTokenizer st;
    static PrintWriter out;

    static final int MOD = 1_000_000_007;
    static final long INF = (long) 1e18;
    static final double EPS = 1e-9;
    static final Random random = new Random();

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(new BufferedOutputStream(System.out));
        solve();
        out.flush();
        out.close();
    }

    static void solve() throws IOException {
        // Example: Nim Game
        int n = nextInt();
        int[] piles = nextIntArray(n);
        out.println(GameTheory.nim(piles) ? "First" : "Second");
    }

    // ==================== GAME THEORY ====================
    static class GameTheory {

        // ==================== NIM GAME ====================
        // Returns true if first player wins (XOR of all piles != 0)
        static boolean nim(int[] piles) {
            int xor = 0;
            for (int p : piles) xor ^= p;
            return xor != 0;
        }

        // Nim with multiple games (Sprague-Grundy theorem)
        static boolean nimSum(int[] grundy) {
            int xor = 0;
            for (int g : grundy) xor ^= g;
            return xor != 0;
        }

        // ==================== GRUNDY NUMBERS (Mex function) ====================
        // Calculate Grundy number for a game state
        static int mex(Set<Integer> set) {
            int mex = 0;
            while (set.contains(mex)) mex++;
            return mex;
        }

        // Grundy number for Nim with a single pile of size n
        // and allowed moves: remove 1, 2, or 3 stones
        static int grundyNim123(int n, int[] memo) {
            if (n == 0) return 0;
            if (memo[n] != -1) return memo[n];

            Set<Integer> reachable = new HashSet<>();
            if (n >= 1) reachable.add(grundyNim123(n - 1, memo));
            if (n >= 2) reachable.add(grundyNim123(n - 2, memo));
            if (n >= 3) reachable.add(grundyNim123(n - 3, memo));

            return memo[n] = mex(reachable);
        }

        // Grundy number for a game where you can split a pile into two unequal piles
        static int grundySplit(int n, int[] memo) {
            if (n <= 1) return 0;
            if (memo[n] != -1) return memo[n];

            Set<Integer> reachable = new HashSet<>();
            for (int i = 1; i < n; i++) {
                int j = n - i;
                if (i != j) {
                    reachable.add(grundySplit(i, memo) ^ grundySplit(j, memo));
                }
            }
            return memo[n] = mex(reachable);
        }

        // ==================== MINIMAX ====================
        // Simple minimax for two-player zero-sum games
        static int minimax(int depth, boolean isMaximizing, int[] values) {
            if (depth == 0) return values[depth];

            if (isMaximizing) {
                int best = Integer.MIN_VALUE;
                for (int i = 0; i < values.length; i++) {
                    best = Math.max(best, minimax(depth - 1, false, values));
                }
                return best;
            } else {
                int best = Integer.MAX_VALUE;
                for (int i = 0; i < values.length; i++) {
                    best = Math.min(best, minimax(depth - 1, true, values));
                }
                return best;
            }
        }

        // ==================== ALPHA-BETA PRUNING ====================
        // Optimized minimax with alpha-beta pruning
        static int alphaBeta(int depth, boolean isMaximizing, int alpha, int beta, int[] values) {
            if (depth == 0) return values[depth];

            if (isMaximizing) {
                int best = Integer.MIN_VALUE;
                for (int i = 0; i < values.length; i++) {
                    best = Math.max(best, alphaBeta(depth - 1, false, alpha, beta, values));
                    alpha = Math.max(alpha, best);
                    if (beta <= alpha) break; // Prune
                }
                return best;
            } else {
                int best = Integer.MAX_VALUE;
                for (int i = 0; i < values.length; i++) {
                    best = Math.min(best, alphaBeta(depth - 1, true, alpha, beta, values));
                    beta = Math.min(beta, best);
                    if (beta <= alpha) break; // Prune
                }
                return best;
            }
        }

        // ==================== GAME ON GRAPHS ====================
        // Determine winner in a game where players move on a DAG
        // Returns true if starting position is winning
        static boolean gameOnGraph(List<List<Integer>> adj, int start, int[] memo) {
            if (memo[start] != -1) return memo[start] == 1;

            boolean canLose = false;
            for (int next : adj.get(start)) {
                if (!gameOnGraph(adj, next, memo)) {
                    canLose = true;
                    break;
                }
            }

            memo[start] = canLose ? 1 : 0;
            return canLose;
        }

        // ==================== COIN GAME (Pick from ends) ====================
        // Two players pick coins from either end, maximize own sum
        // Returns maximum value first player can get
        static int optimalStrategy(int[] coins) {
            int n = coins.length;
            int[][] dp = new int[n][n];

            for (int gap = 0; gap < n; gap++) {
                for (int i = 0, j = gap; j < n; i++, j++) {
                    int x = (i + 2 <= j) ? dp[i + 2][j] : 0;
                    int y = (i + 1 <= j - 1) ? dp[i + 1][j - 1] : 0;
                    int z = (i <= j - 2) ? dp[i][j - 2] : 0;
                    dp[i][j] = Math.max(coins[i] + Math.min(x, y),
                                       coins[j] + Math.min(y, z));
                }
            }
            return dp[0][n - 1];
        }
    }

    // ==================== TERNARY SEARCH ====================
    static class TernarySearch {

        // Find maximum of unimodal function in range [l, r]
        static double findMax(java.util.function.DoubleUnaryOperator f, double l, double r) {
            for (int i = 0; i < 100; i++) {
                double m1 = l + (r - l) / 3;
                double m2 = r - (r - l) / 3;
                if (f.applyAsDouble(m1) < f.applyAsDouble(m2)) {
                    l = m1;
                } else {
                    r = m2;
                }
            }
            return l;
        }

        // Find minimum of unimodal function in range [l, r]
        static double findMin(java.util.function.DoubleUnaryOperator f, double l, double r) {
            for (int i = 0; i < 100; i++) {
                double m1 = l + (r - l) / 3;
                double m2 = r - (r - l) / 3;
                if (f.applyAsDouble(m1) > f.applyAsDouble(m2)) {
                    l = m1;
                } else {
                    r = m2;
                }
            }
            return l;
        }

        // Integer ternary search for discrete unimodal function
        static int findMaxInt(java.util.function.IntUnaryOperator f, int l, int r) {
            while (r - l > 2) {
                int m1 = l + (r - l) / 3;
                int m2 = r - (r - l) / 3;
                if (f.applyAsInt(m1) < f.applyAsInt(m2)) {
                    l = m1;
                } else {
                    r = m2;
                }
            }
            int best = l;
            for (int i = l + 1; i <= r; i++) {
                if (f.applyAsInt(i) > f.applyAsInt(best)) best = i;
            }
            return best;
        }
    }

    // ==================== SIMULATED ANNEALING ====================
    static class SimulatedAnnealing {

        // Generic simulated annealing optimizer
        static <T> T optimize(Solution<T> initial, double initialTemp, double coolingRate, int iterations) {
            T current = initial.getSolution();
            T best = initial.getSolution();
            double currentEnergy = initial.getEnergy(current);
            double bestEnergy = currentEnergy;
            double temp = initialTemp;

            for (int i = 0; i < iterations; i++) {
                T neighbor = initial.getNeighbor(current);
                double neighborEnergy = initial.getEnergy(neighbor);

                if (neighborEnergy < currentEnergy ||
                    Math.random() < Math.exp(-(neighborEnergy - currentEnergy) / temp)) {
                    current = neighbor;
                    currentEnergy = neighborEnergy;

                    if (neighborEnergy < bestEnergy) {
                        best = neighbor;
                        bestEnergy = neighborEnergy;
                    }
                }

                temp *= coolingRate;
            }

            return best;
        }

        // Interface for problem-specific implementation
        interface Solution<T> {
            T getSolution();
            T getNeighbor(T current);
            double getEnergy(T solution);
        }

        // Example: Traveling Salesman Problem
        static class TSPSolution implements Solution<int[]> {
            double[][] dist;
            int n;

            TSPSolution(double[][] dist) {
                this.dist = dist;
                this.n = dist.length;
            }

            public int[] getSolution() {
                int[] perm = new int[n];
                for (int i = 0; i < n; i++) perm[i] = i;
                shuffle(perm);
                return perm;
            }

            public int[] getNeighbor(int[] current) {
                int[] neighbor = current.clone();
                int i = random.nextInt(n);
                int j = random.nextInt(n);
                int temp = neighbor[i];
                neighbor[i] = neighbor[j];
                neighbor[j] = temp;
                return neighbor;
            }

            public double getEnergy(int[] solution) {
                double total = 0;
                for (int i = 0; i < n; i++) {
                    total += dist[solution[i]][solution[(i + 1) % n]];
                }
                return total;
            }

            void shuffle(int[] arr) {
                for (int i = n - 1; i > 0; i--) {
                    int j = random.nextInt(i + 1);
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    // ==================== RANDOMIZED ALGORITHMS ====================
    static class RandomizedAlgorithms {

        // Randomized QuickSelect - find k-th smallest element in O(n) average
        static int quickSelect(int[] arr, int k) {
            return quickSelect(arr, 0, arr.length - 1, k);
        }

        static int quickSelect(int[] arr, int left, int right, int k) {
            if (left == right) return arr[left];

            int pivotIdx = partition(arr, left, right);
            int rank = pivotIdx - left + 1;

            if (k == rank) return arr[pivotIdx];
            else if (k < rank) return quickSelect(arr, left, pivotIdx - 1, k);
            else return quickSelect(arr, pivotIdx + 1, right, k - rank);
        }

        static int partition(int[] arr, int left, int right) {
            int pivotIdx = left + random.nextInt(right - left + 1);
            int pivot = arr[pivotIdx];

            swap(arr, pivotIdx, right);
            int storeIdx = left;

            for (int i = left; i < right; i++) {
                if (arr[i] < pivot) {
                    swap(arr, i, storeIdx);
                    storeIdx++;
                }
            }
            swap(arr, storeIdx, right);
            return storeIdx;
        }

        static void swap(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }

        // Randomized primality test (Miller-Rabin)
        static boolean isPrime(long n) {
            if (n < 2) return false;
            if (n == 2 || n == 3) return true;
            if (n % 2 == 0) return false;

            long d = n - 1;
            int s = 0;
            while (d % 2 == 0) { d /= 2; s++; }

            // Random witnesses
            for (int i = 0; i < 5; i++) {
                long a = 2 + random.nextInt((int) Math.min(n - 3, 1000000));
                if (!millerRabinTest(a, n, d, s)) return false;
            }
            return true;
        }

        static boolean millerRabinTest(long a, long n, long d, int s) {
            long x = modPow(a, d, n);
            if (x == 1 || x == n - 1) return true;

            for (int r = 1; r < s; r++) {
                x = modMul(x, x, n);
                if (x == n - 1) return true;
            }
            return false;
        }

        static long modPow(long x, long y, long m) {
            long res = 1;
            x %= m;
            while (y > 0) {
                if ((y & 1) == 1) res = modMul(res, x, m);
                x = modMul(x, x, m);
                y >>= 1;
            }
            return res;
        }

        static long modMul(long a, long b, long m) {
            return ((a % m) * (b % m)) % m;
        }

        // Karger's algorithm for minimum cut - O(n²)
        static int kargerMinCut(int[][] adj) {
            int n = adj.length;
            int[] nodes = new int[n];
            for (int i = 0; i < n; i++) nodes[i] = i;

            int remaining = n;
            while (remaining > 2) {
                // Pick random edge
                int u = random.nextInt(remaining);
                int v = random.nextInt(remaining);
                while (u == v || adj[nodes[u]][nodes[v]] == 0) {
                    u = random.nextInt(remaining);
                    v = random.nextInt(remaining);
                }

                // Contract edge
                for (int i = 0; i < remaining; i++) {
                    if (i != u && i != v) {
                        adj[nodes[u]][nodes[i]] += adj[nodes[v]][nodes[i]];
                        adj[nodes[i]][nodes[u]] += adj[nodes[i]][nodes[v]];
                    }
                }

                // Remove node v
                nodes[v] = nodes[remaining - 1];
                remaining--;
            }

            return adj[nodes[0]][nodes[1]];
        }

        // Reservoir sampling - select k random elements from stream
        static int[] reservoirSampling(int[] stream, int k) {
            int[] reservoir = new int[k];
            for (int i = 0; i < k && i < stream.length; i++) {
                reservoir[i] = stream[i];
            }

            for (int i = k; i < stream.length; i++) {
                int j = random.nextInt(i + 1);
                if (j < k) {
                    reservoir[j] = stream[i];
                }
            }
            return reservoir;
        }
    }

    // ==================== MEET IN THE MIDDLE ====================
    static class MeetInTheMiddle {

        // Subset sum using meet in the middle - O(2^(n/2))
        static boolean subsetSum(int[] arr, int target) {
            int n = arr.length;
            int n1 = n / 2, n2 = n - n1;

            Set<Integer> leftSums = new HashSet<>();
            generateSums(arr, 0, n1, 0, leftSums);

            for (int sum : leftSums) {
                if (sum == target) return true;
            }

            Set<Integer> rightSums = new HashSet<>();
            generateSums(arr, n1, n, 0, rightSums);

            for (int rSum : rightSums) {
                if (leftSums.contains(target - rSum)) return true;
            }

            return false;
        }

        static void generateSums(int[] arr, int start, int end, int sum, Set<Integer> sums) {
            if (start == end) {
                sums.add(sum);
                return;
            }
            generateSums(arr, start + 1, end, sum, sums);
            generateSums(arr, start + 1, end, sum + arr[start], sums);
        }

        // Find closest subset sum to target
        static int closestSubsetSum(int[] arr, int target) {
            int n = arr.length;
            int n1 = n / 2, n2 = n - n1;

            List<Integer> leftSums = new ArrayList<>();
            generateSumsList(arr, 0, n1, 0, leftSums);
            Collections.sort(leftSums);

            List<Integer> rightSums = new ArrayList<>();
            generateSumsList(arr, n1, n, 0, rightSums);

            int best = 0;
            for (int rSum : rightSums) {
                int remaining = target - rSum;
                int idx = Collections.binarySearch(leftSums, remaining);
                if (idx >= 0) return target;

                idx = -(idx + 1);
                if (idx < leftSums.size()) {
                    best = Math.max(best, rSum + leftSums.get(idx));
                }
                if (idx > 0) {
                    best = Math.max(best, rSum + leftSums.get(idx - 1));
                }
            }
            return best;
        }

        static void generateSumsList(int[] arr, int start, int end, int sum, List<Integer> sums) {
            if (start == end) {
                sums.add(sum);
                return;
            }
            generateSumsList(arr, start + 1, end, sum, sums);
            generateSumsList(arr, start + 1, end, sum + arr[start], sums);
        }
    }

    // ==================== MO'S ALGORITHM ====================
    static class MosAlgorithm {

        static class Query implements Comparable<Query> {
            int l, r, id, block;

            Query(int l, int r, int id, int blockSize) {
                this.l = l;
                this.r = r;
                this.id = id;
                this.block = l / blockSize;
            }

            public int compareTo(Query o) {
                if (block != o.block) return Integer.compare(block, o.block);
                return (block & 1) == 0 ? Integer.compare(r, o.r) : Integer.compare(o.r, r);
            }
        }

        // Standard Mo's algorithm for range queries
        static int[] solve(int[] arr, int[][] queries) {
            int n = arr.length;
            int q = queries.length;
            int blockSize = (int) Math.sqrt(n);

            Query[] qs = new Query[q];
            for (int i = 0; i < q; i++) {
                qs[i] = new Query(queries[i][0], queries[i][1], i, blockSize);
            }

            Arrays.sort(qs);

            int[] answers = new int[q];
            int currL = 0, currR = -1;
            int currAns = 0;
            int[] freq = new int[100001]; // Adjust based on value range

            for (Query query : qs) {
                while (currL > query.l) {
                    currL--;
                    add(arr[currL], freq);
                }
                while (currR < query.r) {
                    currR++;
                    add(arr[currR], freq);
                }
                while (currL < query.l) {
                    remove(arr[currL], freq);
                    currL++;
                }
                while (currR > query.r) {
                    remove(arr[currR], freq);
                    currR--;
                }
                answers[query.id] = currAns;
            }

            return answers;
        }

        static void add(int val, int[] freq) {
            freq[val]++;
            // Update answer based on problem
        }

        static void remove(int val, int[] freq) {
            freq[val]--;
            // Update answer based on problem
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
}
