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

| Template | Use Case |
|----------|----------|
| `FastTemplate.java` | **Default** - Most problems (BufferedReader + PrintWriter) |
| `GraphTemplate.java` | BFS, DFS, Dijkstra, DSU |
| `DPTemplate.java` | Common DP patterns |
| `MathTemplate.java` | nCr, Modular Arithmetic, Sieve |
| `SegTreeTemplate.java` | Segment Tree with Lazy Propagation |
| `BITTemplate.java` | Fenwick Tree |
| `StringTemplate.java` | KMP, Z-function, Hashing, Trie |

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
