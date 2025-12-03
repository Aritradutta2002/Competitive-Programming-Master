/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * INTERACTIVE Template - For interactive problems
 * Remember: flush output after each query!
 */
import java.io.*;
import java.util.*;

public class InteractiveTemplate {
    static BufferedReader br;
    static StringTokenizer st;
    static PrintWriter out;
    
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out);  // No buffering for interactive!
        
        int t = nextInt();
        while (t-- > 0) {
            solve();
        }
        
        out.close();
    }
    
    static void solve() throws IOException {
        int n = nextInt();
        
        // Binary search example for interactive
        int lo = 1, hi = n;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            int res = query(mid);
            
            if (res == 1) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        
        answer(lo);
    }
    
    // Query function - modify based on problem
    static int query(int x) throws IOException {
        out.println("? " + x);
        out.flush();  // CRITICAL: flush after each query!
        return nextInt();
    }
    
    // Answer function - modify based on problem
    static void answer(int x) {
        out.println("! " + x);
        out.flush();
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
