# Competitive Programming Templates
## Target: Codeforces Expert / CSES

### Author: Aritra Dutta

---

## Quick Start

### C++ Compilation
```bash
g++ -std=c++17 -O2 -o sol solution.cpp
```

### Java Compilation
```bash
javac Solution.java
java Solution
```

---

## Template Guide

### C++ Templates (`templates/cpp/`)

| Template | Use Case |
|----------|----------|
| `FastTemplate.cpp` | **Default** - Most problems |
| `GraphTemplate.cpp` | BFS, DFS, Dijkstra, DSU, Topological Sort |
| `DPTemplate.cpp` | Coin Change, Knapsack, LIS, LCS, Edit Distance |
| `MathTemplate.cpp` | nCr, Modular Arithmetic, Sieve, Matrix Exponentiation |
| `SegTreeTemplate.cpp` | Point Update, Range Query, Lazy Propagation |
| `BITTemplate.cpp` | Fenwick Tree, Inversions |
| `StringTemplate.cpp` | KMP, Z-function, Hashing, Trie, Manacher |
| `TreeTemplate.cpp` | LCA, Euler Tour, Tree DP, Diameter |

### Java Templates (`templates/java/`)

Templates are organized into subfolders by topic:

#### `base/` — Core I/O + Utilities (START HERE)

| Template | Use Case |
|----------|----------|
| `base/FastIO.java` | **Default** - Core I/O + utilities |

#### `graph/` — Graph Theory

| Template | Use Case |
|----------|----------|
| `graph/GraphTraversal.java` | BFS, DFS, Bipartite, Topo Sort |
| `graph/ShortestPath.java` | Dijkstra, Bellman-Ford, Floyd |
| `graph/MSTAndDSU.java` | DSU, Kruskal, Prim |
| `graph/AdvancedGraph.java` | SCC, Bridges, AP, MaxFlow |

#### `number_theory/` — Number Theory & Math

| Template | Use Case |
|----------|----------|
| `number_theory/ModArithmetic.java` | Mod ops, ExtGCD, CRT |
| `number_theory/Combinatorics.java` | nCr, nPr, Catalan, Stirling |
| `number_theory/PrimesAndSieve.java` | Sieve, SPF, Miller-Rabin, Totient |
| `number_theory/MatrixAndTransforms.java` | Matrix exp, FFT, NTT, Gaussian |

#### `data_structures/` — Data Structures

| Template | Use Case |
|----------|----------|
| `data_structures/SegmentTree.java` | Generic, Lazy, Min/Max SegTree |
| `data_structures/FenwickTree.java` | BIT 1D, 2D |
| `data_structures/SparseTableAndSqrt.java` | Sparse Table, Sqrt Decomp |

#### `dp/` — Dynamic Programming

| Template | Use Case |
|----------|----------|
| `dp/DPPatterns.java` | Classical, Digit, Bitmask, Optimizations |

#### `strings/` — String Algorithms

| Template | Use Case |
|----------|----------|
| `strings/StringMatching.java` | KMP, Z-func, Hashing, Manacher |
| `strings/StringStructures.java` | Trie, Aho-Corasick, Suffix Array/Automaton |

#### `trees/` — Tree Algorithms

| Template | Use Case |
|----------|----------|
| `trees/TreeAlgorithms.java` | LCA, HLD, Centroid, Euler Tour, Tree DP |

#### `geometry/` — Computational Geometry

| Template | Use Case |
|----------|----------|
| `geometry/Geometry.java` | Point, Line, Polygon, Convex Hull |

#### `advanced/` — Advanced Techniques

| Template | Use Case |
|----------|----------|
| `advanced/AdvancedTechniques.java` | Game Theory, Mo's, Meet in Middle |

#### `utils/` — Utility Classes

| Template | Use Case |
|----------|----------|
| `utils/Utilities.java` | Pair, Triple, DSU, Range, Comparators |

#### Legacy Templates (Backward Compatible)

> **Note:** Legacy files are located in the root `java/` folder (not in subfolders).

| Template | Use Case |
|----------|----------|
| `FastTemplate.java` | Basic Fast I/O template |
| `GraphTemplate.java` | Basic graph algorithms |
| `DPTemplate.java` | Common DP patterns |
| `MathTemplate.java` | nCr, Modular Arithmetic, Sieve |
| `SegTreeTemplate.java` | Segment Tree with Lazy Propagation |
| `BITTemplate.java` | Fenwick Tree |
| `StringTemplate.java` | KMP, Z-function, Hashing, Trie |
| `TreeTemplate.java` | LCA, Tree basics |

---

## Key Optimizations for CSES/Codeforces

### Java I/O (CRITICAL for CSES)
```java
// ALWAYS use this pattern - 10x faster than Scanner
static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
static StringTokenizer st;
static PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));

static String next() throws IOException {
    while (st == null || !st.hasMoreTokens())
        st = new StringTokenizer(br.readLine());
    return st.nextToken();
}

static int nextInt() throws IOException { return Integer.parseInt(next()); }
```

### Java Output (CRITICAL)
```java
// Use StringBuilder for multiple outputs
StringBuilder sb = new StringBuilder();
for (int i = 0; i < n; i++) {
    sb.append(arr[i]).append(' ');
}
out.println(sb);

// ALWAYS flush at the end
out.flush();
out.close();
```

### Java Sorting (Anti-hack)
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

### C++ Fast I/O
```cpp
ios_base::sync_with_stdio(false);
cin.tie(nullptr);
```

---

## Java Template Usage

For detailed Java template documentation, see [`java/README_JAVA.md`](java/README_JAVA.md)

### Quick Reference

| Problem Category | Primary Template | Secondary Template |
|-----------------|------------------|-------------------|
| Array/Sorting | `base/FastIO.java` | `data_structures/SegmentTree.java` |
| Graph | `graph/GraphTraversal.java` | `utils/Utilities.java` |
| Shortest Path | `graph/ShortestPath.java` | `graph/GraphTraversal.java` |
| MST / DSU | `graph/MSTAndDSU.java` | `utils/Utilities.java` |
| Tree | `trees/TreeAlgorithms.java` | `data_structures/SegmentTree.java` |
| DP | `dp/DPPatterns.java` | `base/FastIO.java` |
| String | `strings/StringMatching.java` | `strings/StringStructures.java` |
| Math | `number_theory/ModArithmetic.java` | `number_theory/Combinatorics.java` |
| Primes / Sieve | `number_theory/PrimesAndSieve.java` | `number_theory/ModArithmetic.java` |
| Geometry | `geometry/Geometry.java` | `base/FastIO.java` |
| Range Queries | `data_structures/SegmentTree.java` | `data_structures/FenwickTree.java` |
| Game Theory | `advanced/AdvancedTechniques.java` | `graph/GraphTraversal.java` |

---

## Common Patterns

### Multiple Test Cases
```cpp
int t;
cin >> t;
while (t--) solve();
```

### Graph Input (0-indexed)
```cpp
int n, m;
cin >> n >> m;
vector<vector<int>> adj(n);
for (int i = 0; i < m; i++) {
    int u, v;
    cin >> u >> v;
    u--; v--;  // Convert to 0-indexed
    adj[u].push_back(v);
    adj[v].push_back(u);  // Remove for directed
}
```

### Coordinate Compression
```cpp
vector<int> vals = arr;
sort(vals.begin(), vals.end());
vals.erase(unique(vals.begin(), vals.end()), vals.end());
for (int& x : arr) {
    x = lower_bound(vals.begin(), vals.end(), x) - vals.begin();
}
```

---

## CSES Problem Categories

1. **Introductory** → `FastTemplate`
2. **Sorting and Searching** → `FastTemplate` + Binary Search
3. **Dynamic Programming** → `DPTemplate`
4. **Graph Algorithms** → `GraphTemplate`
5. **Range Queries** → `SegTreeTemplate` or `BITTemplate`
6. **Tree Algorithms** → `TreeTemplate`
7. **Mathematics** → `MathTemplate`
8. **String Algorithms** → `StringTemplate`
9. **Geometry** → Custom (not included)
10. **Advanced Techniques** → Combine templates

---

## Tips for Expert Level

1. **Read the problem carefully** - Edge cases matter
2. **Think before coding** - 10 min thinking > 1 hour debugging
3. **Start with brute force** - Then optimize
4. **Use appropriate data structures** - Map vs Set vs Array
5. **Watch for overflow** - Use `long long` when in doubt
6. **Test with edge cases** - n=1, n=max, all same values
7. **Time complexity matters** - 10^8 operations ≈ 1 second

Good luck! 🚀
