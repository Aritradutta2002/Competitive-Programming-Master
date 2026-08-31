# Java Competitive Programming Templates
## Complete Algorithm Library for Codeforces / CSES / AtCoder

### Author: Aritra Dutta
### Target: Codeforces Expert / CSES Complete

---

## 📁 Folder Structure (NEW — Organized by Topic)

> **Note:** The templates have been reorganized into topic-based subfolders for easier navigation. Legacy flat files (e.g., `GraphAlgorithms.java`, `DataStructures.java`, `MathTemplate.java`, etc.) are still available in the root `Templates/java/` folder for **backward compatibility**. New development and updates will target the subfolder versions.

```
Templates/java/
├── base/
│   └── FastIO.java                  # Fast I/O + Basic Utilities
├── graph/
│   ├── GraphTraversal.java          # BFS, DFS, Topo Sort, Bipartite, SCC, Bridges
│   ├── ShortestPath.java            # Dijkstra, Bellman-Ford, Floyd-Warshall
│   ├── MSTAndDSU.java               # Kruskal, Prim, Disjoint Set Union
│   └── AdvancedGraph.java           # Max Flow (Dinic), Matching, Advanced Graph
├── number_theory/
│   ├── ModArithmetic.java           # Modular add/sub/mul/pow/inv, GCD, LCM, CRT
│   ├── Combinatorics.java           # nCr, nPr, Catalan, Stirling, Bell
│   ├── PrimesAndSieve.java          # Sieve, SPF, Factorize, Miller-Rabin, Euler Phi
│   └── MatrixAndTransforms.java     # Matrix Exponentiation, FFT/NTT, Gaussian Elim
├── data_structures/
│   ├── SegmentTree.java             # Point/Range update, Lazy Propagation, Min/Max
│   ├── FenwickTree.java             # BIT, 2D BIT, Prefix Sums
│   └── SparseTableAndSqrt.java      # RMQ Sparse Table, Sqrt Decomposition
├── dp/
│   └── DPPatterns.java              # Classical, Digit, Bitmask, Grid, DP Optimizations
├── strings/
│   ├── StringMatching.java          # Z-Function, KMP, Hashing, Manacher
│   └── StringStructures.java        # Trie, Aho-Corasick, Suffix Array/Automaton, Lyndon
├── trees/
│   └── TreeAlgorithms.java          # LCA, Euler Tour, HLD, Centroid Decomp, Tree DP
├── geometry/
│   └── Geometry.java                # Points, Lines, Polygons, Convex Hull, Pick's Thm
├── advanced/
│   └── AdvancedTechniques.java      # Game Theory, Ternary Search, Mo's, Meet in Middle
└── utils/
    └── Utilities.java               # Pair/Triple/Quad, Range, Comparators, Quick DS
```

### Legacy Files (Backward Compatibility)
The following flat files remain in `Templates/java/` for projects and scripts that reference the old paths:

```
Templates/java/
├── FastIO.java
├── GraphAlgorithms.java
├── DataStructures.java
├── MathTemplate.java
├── DPTemplate.java
├── StringAlgorithms.java
├── TreeAlgorithms.java
├── Geometry.java
├── AdvancedTopics.java
└── Utilities.java
```

---

## 🚀 Quick Start

### Compilation
```bash
javac base/FastIO.java
java -cp base FastIO
```

### For Contests (Single File Submission)
1. Copy the template you need from the appropriate subfolder
2. Rename class to `Main` (required for most judges)
3. Add your solution logic in `solve()` method

### Example Usage
```java
import java.io.*;
import java.util.*;

public class Main {
    static BufferedReader br;
    static StringTokenizer st;
    static PrintWriter out;

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
        long[] arr = nextLongArray(n);
        // Your solution here
        out.println(n);
    }

    // Copy I/O methods from base/FastIO.java
    static String next() throws IOException {
        while (st == null || !st.hasMoreTokens())
            st = new StringTokenizer(br.readLine());
        return st.nextToken();
    }

    static int nextInt() throws IOException { return Integer.parseInt(next()); }
    static long nextLong() throws IOException { return Long.parseLong(next()); }
    static long[] nextLongArray(int n) throws IOException {
        long[] arr = new long[n];
        for (int i = 0; i < n; i++) arr[i] = nextLong();
        return arr;
    }
}
```

---

## 📚 Template Guide

### 1. `base/FastIO.java` — **START HERE FOR MOST PROBLEMS**

| Feature | Description |
|---------|-------------|
| `BufferedReader` | 10x faster than Scanner |
| `StringTokenizer` | Fast token parsing |
| `PrintWriter` | Buffered output |
| `shuffleSort()` | Anti-hack sorting |
| Binary Search | `lowerBound()`, `upperBound()` |
| Math Utils | GCD, LCM, modular arithmetic |
| Pair/Triple | Custom data types |

**When to use:** Default template for 80% of problems

---

### 2. `graph/` — Graph Theory Library

#### `graph/GraphTraversal.java`

| Algorithm | Complexity | Use Case |
|-----------|------------|----------|
| BFS | O(V + E) | Shortest path (unweighted) |
| DFS | O(V + E) | Traversal, connectivity |
| Topological Sort | O(V + E) | DAG ordering |
| Kosaraju SCC | O(V + E) | Strongly connected components |
| Bridges | O(V + E) | Critical edges |
| Articulation Points | O(V + E) | Critical vertices |
| Bipartite Check | O(V + E) | 2-coloring |

#### `graph/ShortestPath.java`

| Algorithm | Complexity | Use Case |
|-----------|------------|----------|
| Dijkstra | O((V + E) log V) | Shortest path (non-negative) |
| Bellman-Ford | O(VE) | Shortest path (negative edges) |
| Floyd-Warshall | O(V³) | All-pairs shortest path |

#### `graph/MSTAndDSU.java`

| Algorithm | Complexity | Use Case |
|-----------|------------|----------|
| DSU | O(α(V)) | Connectivity, MST |
| Kruskal | O(E log E) | Minimum Spanning Tree |
| Prim | O((V + E) log V) | Minimum Spanning Tree |

#### `graph/AdvancedGraph.java`

| Algorithm | Complexity | Use Case |
|-----------|------------|----------|
| Dinic Max Flow | O(V²E) | Maximum flow |

**When to use:** Any graph problem (CSES Graph Algorithms, Codeforces Div2 C+)

---

### 3. `data_structures/` — Range Query Structures

#### `data_structures/SegmentTree.java`

| Structure | Operations | Complexity |
|-----------|------------|------------|
| Segment Tree | Point update, Range query | O(log n) |
| Lazy Segment Tree | Range update, Range query | O(log n) |
| Min/Max SegTree | Range minimum/maximum | O(log n) |

#### `data_structures/FenwickTree.java`

| Structure | Operations | Complexity |
|-----------|------------|------------|
| BIT (Fenwick) | Point update, Prefix sum | O(log n) |
| 2D BIT | 2D range queries | O(log²n) |

#### `data_structures/SparseTableAndSqrt.java`

| Structure | Operations | Complexity |
|-----------|------------|------------|
| Sparse Table | RMQ (static) | O(1) query, O(n log n) build |
| Sqrt Decomposition | Range queries | O(√n) |
| Order Statistic Tree | K-th element | O(log n) |
| Coordinate Compression | Value compression | O(n log n) |

**When to use:** Range query problems (CSES Range Queries, Codeforces Div2 D+)

---

### 4. `number_theory/` — Math & Number Theory Library

#### `number_theory/ModArithmetic.java`

| Topic | Functions |
|-------|-----------|
| Modular Arithmetic | `modAdd`, `modSub`, `modMul`, `modPow`, `modInv` |
| Number Theory | `gcd`, `lcm`, `extendedGCD`, `crt`, `phi`, `mobius` |

#### `number_theory/Combinatorics.java`

| Topic | Functions |
|-------|-----------|
| Combinatorics | `nCr`, `nPr`, `catalan`, `stirling2`, `bell` |

#### `number_theory/PrimesAndSieve.java`

| Topic | Functions |
|-------|-----------|
| Primes | `sieve`, `getPrimes`, `computeSPF`, `factorize`, `isPrimeMillerRabin` |

#### `number_theory/MatrixAndTransforms.java`

| Topic | Functions |
|-------|-----------|
| Matrix | `multiply`, `power`, `fibonacci`, `linearRecurrence` |
| FFT/NTT | Polynomial multiplication |
| Gaussian Elimination | System of linear equations |

**When to use:** Math problems (CSES Mathematics, Codeforces Number Theory)

---

### 5. `dp/DPPatterns.java`

| Pattern | Problems |
|---------|----------|
| Classical DP | Coin Change, Knapsack, LIS, LCS, Edit Distance |
| Digit DP | Count numbers with property |
| Bitmask DP | TSP, Independent Set |
| Grid DP | Paths, obstacles |
| DP Optimizations | Convex Hull Trick, Knuth, D&C |

**When to use:** Dynamic Programming problems (CSES DP, Codeforces DP)

---

### 6. `strings/` — String Processing Library

#### `strings/StringMatching.java`

| Algorithm | Complexity | Use Case |
|-----------|------------|----------|
| Z-Function | O(n) | Pattern matching |
| KMP | O(n + m) | Pattern matching |
| String Hashing | O(1) | Substring comparison |
| Manacher | O(n) | Longest palindrome |

#### `strings/StringStructures.java`

| Algorithm | Complexity | Use Case |
|-----------|------------|----------|
| Trie | O(m) | Prefix queries |
| Aho-Corasick | O(n + m + z) | Multi-pattern matching |
| Suffix Array | O(n log²n) | Suffix queries |
| Suffix Automaton | O(n) | Substring queries |
| Lyndon Factorization | O(n) | Cyclic shifts |

**When to use:** String problems (CSES Strings, Codeforces String)

---

### 7. `trees/TreeAlgorithms.java`

| Algorithm | Complexity | Use Case |
|-----------|------------|----------|
| LCA (Binary Lifting) | O(log n) query | Lowest common ancestor |
| Euler Tour | O(1) LCA | Subtree queries |
| Tree Diameter | O(n) | Longest path |
| Tree Centers | O(n) | Balanced root |
| Subtree Size | O(n) | Subtree queries |
| Heavy-Light Decomposition | O(log²n) | Path queries |
| Centroid Decomposition | O(n log n) | Path queries |
| Tree DP | O(n) | Tree optimization |

**When to use:** Tree problems (CSES Trees, Codeforces Trees)

---

### 8. `geometry/Geometry.java`

| Feature | Description |
|---------|-------------|
| Point/Line/Segment | Basic geometric objects |
| Circle | Intersection, tangents |
| Polygon | Area, centroid, containment |
| Convex Hull | Monotone Chain, Graham Scan |
| Closest Pair | Divide & conquer |
| Rotating Calipers | Diameter of convex polygon |
| Pick's Theorem | Lattice point polygons |

**When to use:** Geometry problems (Codeforces Geometry)

---

### 9. `advanced/AdvancedTechniques.java`

| Topic | Algorithms |
|-------|------------|
| Game Theory | Nim, Grundy Numbers, Minimax, Alpha-Beta |
| Ternary Search | Unimodal optimization |
| Simulated Annealing | Approximate optimization |
| Randomized Algorithms | QuickSelect, Miller-Rabin, Karger's |
| Meet in the Middle | Subset sum optimization |
| Mo's Algorithm | Range query optimization |

**When to use:** Specialized problems requiring advanced techniques

---

### 10. `utils/Utilities.java`

| Class | Purpose |
|-------|---------| 
| `Pair`, `Triple`, `Quad` | Multi-value containers |
| `IntPair`, `LongPair` | Primitive versions (faster) |
| `Range` | Interval operations |
| `DSU` | Disjoint Set Union |
| `SegmentTree` | Basic segment tree |
| `FenwickTree` | Basic BIT |
| `RMQ` | Range Minimum Query |
| `Comparators` | Custom comparators |

**When to use:** Need quick data structures without full template

---

## 🎯 Problem Category Mapping

| Problem Type | Primary Template | Secondary Template |
|--------------|------------------|-------------------|
| Array/Sorting | `base/FastIO` | `data_structures/` |
| Graph Traversal | `graph/GraphTraversal` | `utils/Utilities` |
| Shortest Path | `graph/ShortestPath` | `data_structures/` |
| MST / Connectivity | `graph/MSTAndDSU` | `graph/GraphTraversal` |
| Max Flow / Matching | `graph/AdvancedGraph` | `graph/GraphTraversal` |
| Trees | `trees/TreeAlgorithms` | `data_structures/` |
| Dynamic Programming | `dp/DPPatterns` | `base/FastIO` |
| String Matching | `strings/StringMatching` | `data_structures/` |
| String Structures | `strings/StringStructures` | `strings/StringMatching` |
| Modular Arithmetic | `number_theory/ModArithmetic` | `base/FastIO` |
| Combinatorics | `number_theory/Combinatorics` | `number_theory/ModArithmetic` |
| Primes / Sieve | `number_theory/PrimesAndSieve` | `number_theory/ModArithmetic` |
| Matrix / FFT | `number_theory/MatrixAndTransforms` | `number_theory/ModArithmetic` |
| Segment Tree | `data_structures/SegmentTree` | `utils/Utilities` |
| BIT / Prefix Sums | `data_structures/FenwickTree` | `utils/Utilities` |
| RMQ / Sqrt | `data_structures/SparseTableAndSqrt` | `utils/Utilities` |
| Geometry | `geometry/Geometry` | `base/FastIO` |
| Game Theory | `advanced/AdvancedTechniques` | `graph/GraphTraversal` |

---

## ⚡ Key Optimizations

### Java I/O (CRITICAL for CSES)
```java
// ALWAYS use this pattern
static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
static StringTokenizer st;
static PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));

// Read input
static String next() throws IOException {
    while (st == null || !st.hasMoreTokens())
        st = new StringTokenizer(br.readLine());
    return st.nextToken();
}

// ALWAYS flush at the end
out.flush();
out.close();
```

### Anti-Hack Sorting
```java
// ALWAYS shuffle before sorting to avoid O(n²) worst case
static void shuffleSort(int[] arr) {
    Random random = new Random();
    for (int i = arr.length - 1; i > 0; i--) {
        int j = random.nextInt(i + 1);
        int temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;
    }
    Arrays.sort(arr);
}
```

### Stack Size (for deep recursion)
```java
// Run in new Thread with increased stack size
new Thread(null, new Runnable() {
    public void run() {
        new Main().solve();
    }
}, "1", 1 << 26).start();
```

---

## 📖 Common Patterns

### Multiple Test Cases
```java
int t = nextInt();
while (t-- > 0) solve();
```

### Graph Input (0-indexed)
```java
int n = nextInt(), m = nextInt();
List<List<Integer>> adj = new ArrayList<>();
for (int i = 0; i < n; i++) adj.add(new ArrayList<>());

for (int i = 0; i < m; i++) {
    int u = nextInt() - 1, v = nextInt() - 1;
    adj.get(u).add(v);
    adj.get(v).add(u); // Remove for directed
}
```

### Coordinate Compression
```java
int[] sorted = arr.clone();
Arrays.sort(sorted);
Map<Integer, Integer> compress = new HashMap<>();
int rank = 0;
for (int x : sorted) {
    if (!compress.containsKey(x)) compress.put(x, ++rank);
}
for (int i = 0; i < n; i++) arr[i] = compress.get(arr[i]);
```

### Binary Search Pattern
```java
int lo = 0, hi = n;
while (lo < hi) {
    int mid = (lo + hi) / 2;
    if (check(mid)) hi = mid;
    else lo = mid + 1;
}
return lo;
```

---

## 🏆 CSES Problem Set Mapping

### Introductory Problems
- **Template:** `base/FastIO.java`

### Sorting and Searching
- **Template:** `base/FastIO.java` + Binary Search

### Dynamic Programming
- **Template:** `dp/DPPatterns.java`

### Graph Algorithms
- **Templates:** `graph/GraphTraversal.java`, `graph/ShortestPath.java`, `graph/MSTAndDSU.java`, `graph/AdvancedGraph.java`

### Range Queries
- **Templates:** `data_structures/SegmentTree.java`, `data_structures/FenwickTree.java`, `data_structures/SparseTableAndSqrt.java`

### Tree Algorithms
- **Template:** `trees/TreeAlgorithms.java`

### Mathematics
- **Templates:** `number_theory/ModArithmetic.java`, `number_theory/Combinatorics.java`, `number_theory/PrimesAndSieve.java`, `number_theory/MatrixAndTransforms.java`

### String Algorithms
- **Templates:** `strings/StringMatching.java`, `strings/StringStructures.java`

### Geometry
- **Template:** `geometry/Geometry.java`

### Advanced Techniques
- **Templates:** `advanced/AdvancedTechniques.java` + `data_structures/SegmentTree.java`

---

## 💡 Tips for Success

1. **Read Carefully** - Edge cases matter (n=1, constraints)
2. **Think First** - 10 min thinking > 1 hour debugging
3. **Start Simple** - Brute force → Optimize
4. **Choose Right DS** - Map vs Array vs Set
5. **Watch Overflow** - Use `long` when needed
6. **Test Edge Cases** - n=1, max values, all same
7. **Time Complexity** - 10⁸ ops ≈ 1 second
8. **Practice** - CSES → Codeforces → AtCoder

---

## 🔧 Local Testing

### Input/Output Redirection
```bash
# Run with input file
java Main < input.txt

# Run with input and output files
java Main < input.txt > output.txt

# Compare with expected output
diff output.txt expected.txt
```

### Debug Template (Local Only)
```java
static void dbg(Object... arr) {
    for (Object o : arr) System.err.print(o + " ");
    System.err.println();
}
```

---

## 📝 Contest Checklist

- [ ] Fast I/O initialized
- [ ] Output flushed at end
- [ ] Arrays sized correctly (n+1 vs n)
- [ ] 0-indexed vs 1-indexed consistent
- [ ] Long used for large values
- [ ] Modulo applied correctly
- [ ] Edge cases considered
- [ ] Time complexity acceptable

---

## 🎓 Learning Resources

- **CSES Problem Set:** https://cses.fi/problemset/
- **Codeforces:** https://codeforces.com/
- **AtCoder:** https://atcoder.jp/
- **CP-Algorithms:** https://cp-algorithms.com/
- **USACO Guide:** https://usaco.guide/

---

Good luck in your competitive programming journey! 🚀

*Last Updated: March 2026*
