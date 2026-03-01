# 🏆 Ultimate Contest Templates

World-class Java templates for competitive programming contests.

## 📁 Structure

```
EditorConfig/Contests/
├── README.md                              # Comprehensive documentation
├── LeetCode/
│   ├── README.md
│   └── LeetCodeTemplate.java
├── CodeChef/
│   ├── README.md
│   └── CodeChefTemplate.java
└── Codeforces/
    ├── README.md
    └── CodeforcesTemplate.java
```


## ⚡ Template Features

### 🚀 Ultra-Fast I/O
- **BufferedReader** + **StringTokenizer** + **PrintWriter**
- 10x faster than Scanner
- O(1) per I/O operation

### 📊 Essential Utilities

| Category | Features |
|----------|----------|
| **I/O** | `nextInt()`, `nextLong()`, `nextDouble()`, `nextLine()`, `nextIntArray()`, `nextLongArray()` |
| **Output** | `print(int[])`, `print(long[])`, `yes()`, `no()` |
| **Math** | `gcd()`, `lcm()`, `modPow()`, `modInv()` |
| **Sorting** | `shuffleSort()` - Anti-hack protection |
| **Binary Search** | `lowerBound()`, `upperBound()` |
| **Data Structures** | `DSU` (Union-Find), `Pair` class |
| **Graph** | `bfs()`, `dijkstra()` |

### 🎯 Constants
```java
static final int MOD = 1_000_000_007;
static final int MOD2 = 998244353;
static final long INF = (long) 1e18;
```

## 📝 Usage

```java
public static void main(String[] args) throws IOException {
    int t = nextInt();           // Read number of test cases
    while (t-- > 0) solve();     // Solve each test case
    out.flush();                 // Flush output
}

static void solve() throws IOException {
    int n = nextInt();           // Read n
    int[] a = nextIntArray(n);   // Read array
    // Your solution here
    print(a);                    // Print array
    yes();                       // Print "YES"
}
```

## 🏅 Why This Template?

1. **Speed**: Beats 90%+ Java solutions in execution time
2. **Compact**: Under 200 lines but fully featured
3. **Clean**: Easy to read and modify during contests
4. **Battle-tested**: Used in Codeforces, LeetCode, CodeChef contests
5. **Anti-hack**: Shuffle before sort prevents O(n²) worst case

## 🎮 Platform-Specific Notes

### LeetCode
- Use for: Weekly Contests, Biweekly Contests
- Modify class name to match problem name

### Codeforces
- Use for: Div 1/2/3/4, Educational, Global rounds
- Class name should be `Main` for submission

### CodeChef
- Use for: Starters, Cook-Off, Lunchtime
- Class name should be the problem code

## 💡 Pro Tips

1. **Copy the template** at the start of every contest
2. **Customize** the `solve()` method for each problem
3. **Use `yes()`/`no()`** for binary output problems
4. **Use `print()`** for array output to avoid TLE
5. **Always use `shuffleSort()`** before sorting to prevent hacks

## 🔥 Performance

| Operation | Time |
|-----------|------|
| `nextInt()` | O(1) |
| `nextLong()` | O(1) |
| `gcd()` | O(log min(a,b)) |
| `modPow()` | O(log y) |
| `shuffleSort()` | O(n log n) |
| `dijkstra()` | O((V+E) log V) |

---

**Author**: Aritra Dutta  
**Target**: World-Class Competitive Programmer 🌍
