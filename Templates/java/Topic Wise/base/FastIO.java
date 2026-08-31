/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * FAST I/O Template - BufferedReader + StringTokenizer + PrintWriter
 * This is the FASTEST Java I/O for competitive programming
 * 
 * USAGE: Copy this template for any problem requiring fast I/O
 * Just add your solve() logic and call nextInt(), nextLong(), out.println()
 *
 * Key optimizations:
 * 1. BufferedReader instead of Scanner (10x faster)
 * 2. StringTokenizer for parsing (faster than split)
 * 3. PrintWriter with StringBuilder for output
 * 4. Shuffle before sort to avoid O(n²) worst case
 */
import java.io.*;
import java.util.*;

public class FastIO {
    // ==================== FAST I/O STATIC MEMBERS ====================
    static BufferedReader br;
    static StringTokenizer st;
    static PrintWriter out;
    static final Random random = new Random();

    // ==================== CONSTANTS ====================
    static final int MOD = 1_000_000_007;
    static final int MOD2 = 998244353;
    static final long INF = (long) 1e18;
    static final double EPS = 1e-9;

    public static void main(String[] args) throws IOException {
        // Initialize I/O - ALWAYS call this first
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(new BufferedOutputStream(System.out));

        // Read number of test cases (comment out for single test case problems)
        int t = nextInt();
        while (t-- > 0) {
            solve();
        }

        // ALWAYS flush at the end
        out.flush();
        out.close();
    }

    // ==================== YOUR SOLUTION HERE ====================
    static void solve() throws IOException {
        // Example: Read input and print output
        int n = nextInt();
        long[] arr = nextLongArray(n);
        
        // Your solution logic here
        
        out.println(n);
    }

    // ==================== INPUT METHODS ====================
    static String next() throws IOException {
        while (st == null || !st.hasMoreTokens()) {
            String line = br.readLine();
            if (line == null) return null;
            st = new StringTokenizer(line);
        }
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

    static String nextLine() throws IOException {
        return br.readLine();
    }

    static char nextChar() throws IOException {
        return next().charAt(0);
    }

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

    static double[] nextDoubleArray(int n) throws IOException {
        double[] arr = new double[n];
        for (int i = 0; i < n; i++) arr[i] = nextDouble();
        return arr;
    }

    static String[] nextStringArray(int n) throws IOException {
        String[] arr = new String[n];
        for (int i = 0; i < n; i++) arr[i] = next();
        return arr;
    }

    static int[][] nextIntMatrix(int n, int m) throws IOException {
        int[][] matrix = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = nextInt();
            }
        }
        return matrix;
    }

    static long[][] nextLongMatrix(int n, int m) throws IOException {
        long[][] matrix = new long[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = nextLong();
            }
        }
        return matrix;
    }

    // ==================== OUTPUT METHODS ====================
    static void printArray(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(arr[i]);
        }
        out.println(sb);
    }

    static void printArray(long[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(arr[i]);
        }
        out.println(sb);
    }

    static void printArray(double[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(arr[i]);
        }
        out.println(sb);
    }

    static void printArray(Object[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(arr[i]);
        }
        out.println(sb);
    }

    static void printList(List<?> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(list.get(i));
        }
        out.println(sb);
    }

    static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            printArray(row);
        }
    }

    static void printMatrix(long[][] matrix) {
        for (long[] row : matrix) {
            printArray(row);
        }
    }

    static void yes() { out.println("YES"); }
    static void no() { out.println("NO"); }
    static void Yes() { out.println("Yes"); }
    static void No() { out.println("No"); }

    // ==================== MATH UTILITIES ====================
    static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }

    static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    static int lcm(int a, int b) {
        return a / gcd(a, b) * b;
    }

    static long modPow(long x, long y, long m) {
        long res = 1;
        x %= m;
        while (y > 0) {
            if ((y & 1) == 1) res = res * x % m;
            x = x * x % m;
            y >>= 1;
        }
        return res;
    }

    static long modPow(long x, long y) {
        return modPow(x, y, MOD);
    }

    static long modInv(long x, long m) {
        return modPow(x, m - 2, m);
    }

    static long modInv(long x) {
        return modPow(x, MOD - 2, MOD);
    }

    static long modAdd(long a, long b, long m) {
        return ((a % m) + (b % m)) % m;
    }

    static long modAdd(long a, long b) {
        return modAdd(a, b, MOD);
    }

    static long modSub(long a, long b, long m) {
        return ((a % m) - (b % m) + m) % m;
    }

    static long modSub(long a, long b) {
        return modSub(a, b, MOD);
    }

    static long modMul(long a, long b, long m) {
        return ((a % m) * (b % m)) % m;
    }

    static long modMul(long a, long b) {
        return modMul(a, b, MOD);
    }

    static long modDiv(long a, long b, long m) {
        return modMul(a, modInv(b, m), m);
    }

    static long modDiv(long a, long b) {
        return modDiv(a, b, MOD);
    }

    // ==================== SORTING (Anti-hack) ====================
    // IMPORTANT: Always shuffle before sorting to avoid O(n²) worst case
    static void shuffleSort(int[] arr) {
        int n = arr.length;
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        Arrays.sort(arr);
    }

    static void shuffleSort(long[] arr) {
        int n = arr.length;
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            long temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        Arrays.sort(arr);
    }

    static void shuffleSort(Integer[] arr) {
        int n = arr.length;
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Integer temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        Arrays.sort(arr);
    }

    static void shuffleSort(Long[] arr) {
        int n = arr.length;
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Long temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        Arrays.sort(arr);
    }

    static <T extends Comparable<T>> void shuffleSort(T[] arr) {
        int n = arr.length;
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            T temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        Arrays.sort(arr);
    }

    static <T> void shuffleSort(List<T> list) {
        Collections.shuffle(list, random);
        Collections.sort(list);
    }

    // ==================== ARRAY UTILITIES ====================
    static void reverse(int[] arr) {
        int i = 0, j = arr.length - 1;
        while (i < j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            i++;
            j--;
        }
    }

    static void reverse(long[] arr) {
        int i = 0, j = arr.length - 1;
        while (i < j) {
            long temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            i++;
            j--;
        }
    }

    static int max(int[] arr) {
        int max = arr[0];
        for (int x : arr) max = Math.max(max, x);
        return max;
    }

    static long max(long[] arr) {
        long max = arr[0];
        for (long x : arr) max = Math.max(max, x);
        return max;
    }

    static int min(int[] arr) {
        int min = arr[0];
        for (int x : arr) min = Math.min(min, x);
        return min;
    }

    static long min(long[] arr) {
        long min = arr[0];
        for (long x : arr) min = Math.min(min, x);
        return min;
    }

    static int sum(int[] arr) {
        int sum = 0;
        for (int x : arr) sum += x;
        return sum;
    }

    static long sum(long[] arr) {
        long sum = 0;
        for (long x : arr) sum += x;
        return sum;
    }

    // ==================== BINARY SEARCH ====================
    // Returns first index where arr[index] >= target
    static int lowerBound(int[] arr, int target) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (arr[mid] >= target) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    // Returns first index where arr[index] > target
    static int upperBound(int[] arr, int target) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (arr[mid] > target) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    static int lowerBound(long[] arr, long target) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (arr[mid] >= target) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    static int upperBound(long[] arr, long target) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (arr[mid] > target) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    // Returns index of target, or -1 if not found
    static int binarySearch(int[] arr, int target) {
        int idx = Arrays.binarySearch(arr, target);
        return idx >= 0 ? idx : -1;
    }

    static int binarySearch(long[] arr, long target) {
        int idx = Arrays.binarySearch(arr, target);
        return idx >= 0 ? idx : -1;
    }

    // ==================== PRIMITIVE COLLECTIONS ====================
    static class IntPair implements Comparable<IntPair> {
        int first, second;
        IntPair(int f, int s) { first = f; second = s; }
        public int compareTo(IntPair o) {
            if (first != o.first) return Integer.compare(first, o.first);
            return Integer.compare(second, o.second);
        }
        public String toString() { return "(" + first + ", " + second + ")"; }
        public boolean equals(Object o) {
            if (!(o instanceof IntPair)) return false;
            IntPair p = (IntPair) o;
            return first == p.first && second == p.second;
        }
        public int hashCode() { return Objects.hash(first, second); }
    }

    static class LongPair implements Comparable<LongPair> {
        long first, second;
        LongPair(long f, long s) { first = f; second = s; }
        public int compareTo(LongPair o) {
            if (first != o.first) return Long.compare(first, o.first);
            return Long.compare(second, o.second);
        }
        public String toString() { return "(" + first + ", " + second + ")"; }
        public boolean equals(Object o) {
            if (!(o instanceof LongPair)) return false;
            LongPair p = (LongPair) o;
            return first == p.first && second == p.second;
        }
        public int hashCode() { return Objects.hash(first, second); }
    }

    static class Triple implements Comparable<Triple> {
        int first, second, third;
        Triple(int f, int s, int t) { first = f; second = s; third = t; }
        public int compareTo(Triple o) {
            if (first != o.first) return Integer.compare(first, o.first);
            if (second != o.second) return Integer.compare(second, o.second);
            return Integer.compare(third, o.third);
        }
        public String toString() { return "(" + first + ", " + second + ", " + third + ")"; }
    }
}
