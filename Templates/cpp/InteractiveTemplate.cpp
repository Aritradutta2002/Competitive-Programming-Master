/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * INTERACTIVE Template - For interactive problems
 * Remember: flush output after each query!
 */
#include <bits/stdc++.h>
using namespace std;

using ll = long long;
using vi = vector<int>;

#define rep(i, a, b) for (int i = (a); i < (b); ++i)

// Query function - modify based on problem
int query(int x) {
    cout << "? " << x << endl;  // endl flushes automatically
    int response;
    cin >> response;
    return response;
}

// Answer function - modify based on problem
void answer(int x) {
    cout << "! " << x << endl;
}

void solve() {
    int n;
    cin >> n;
    
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

int main() {
    // DO NOT use fast I/O for interactive problems!
    // ios_base::sync_with_stdio(false);
    // cin.tie(nullptr);
    
    int t = 1;
    cin >> t;
    while (t--) solve();
    
    return 0;
}
