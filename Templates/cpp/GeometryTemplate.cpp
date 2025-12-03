/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * GEOMETRY Template - Points, Lines, Convex Hull
 */
#include <bits/stdc++.h>
using namespace std;

using ll = long long;
using ld = long double;

constexpr ld EPS = 1e-9;
constexpr ld PI = acos(-1.0);

#define rep(i, a, b) for (int i = (a); i < (b); ++i)
#define sz(x) (int)(x).size()

// ==================== Point ====================
template<typename T>
struct Point {
    T x, y;
    
    Point() : x(0), y(0) {}
    Point(T x, T y) : x(x), y(y) {}
    
    Point operator+(const Point& p) const { return {x + p.x, y + p.y}; }
    Point operator-(const Point& p) const { return {x - p.x, y - p.y}; }
    Point operator*(T k) const { return {x * k, y * k}; }
    Point operator/(T k) const { return {x / k, y / k}; }
    
    bool operator<(const Point& p) const {
        return x < p.x || (x == p.x && y < p.y);
    }
    bool operator==(const Point& p) const {
        return x == p.x && y == p.y;
    }
    
    T dot(const Point& p) const { return x * p.x + y * p.y; }
    T cross(const Point& p) const { return x * p.y - y * p.x; }
    T norm2() const { return x * x + y * y; }
    ld norm() const { return sqrt((ld)norm2()); }
    
    // Rotate by angle theta (radians)
    Point rotate(ld theta) const {
        return {x * cos(theta) - y * sin(theta), x * sin(theta) + y * cos(theta)};
    }
};

using Pt = Point<ll>;
using Ptd = Point<ld>;

// Cross product of vectors OA and OB
template<typename T>
T cross(const Point<T>& O, const Point<T>& A, const Point<T>& B) {
    return (A - O).cross(B - O);
}

// ==================== Distance ====================
template<typename T>
ld dist(const Point<T>& a, const Point<T>& b) {
    return (a - b).norm();
}

// ==================== Convex Hull (Andrew's Monotone Chain) ====================
vector<Pt> convexHull(vector<Pt> pts) {
    int n = sz(pts);
    if (n < 3) return pts;
    
    sort(pts.begin(), pts.end());
    
    vector<Pt> hull;
    
    // Lower hull
    for (int i = 0; i < n; i++) {
        while (sz(hull) >= 2 && cross(hull[sz(hull)-2], hull[sz(hull)-1], pts[i]) <= 0)
            hull.pop_back();
        hull.push_back(pts[i]);
    }
    
    // Upper hull
    int lower_size = sz(hull);
    for (int i = n - 2; i >= 0; i--) {
        while (sz(hull) > lower_size && cross(hull[sz(hull)-2], hull[sz(hull)-1], pts[i]) <= 0)
            hull.pop_back();
        hull.push_back(pts[i]);
    }
    
    hull.pop_back();  // Remove last point (duplicate of first)
    return hull;
}

// ==================== Polygon Area (Shoelace) ====================
template<typename T>
ld polygonArea(const vector<Point<T>>& pts) {
    T area = 0;
    int n = sz(pts);
    rep(i, 0, n) {
        area += pts[i].cross(pts[(i + 1) % n]);
    }
    return abs((ld)area) / 2.0;
}

// ==================== Point in Polygon ====================
// Returns: -1 = outside, 0 = on boundary, 1 = inside
template<typename T>
int pointInPolygon(const Point<T>& p, const vector<Point<T>>& poly) {
    int n = sz(poly);
    int winding = 0;
    
    rep(i, 0, n) {
        Point<T> a = poly[i], b = poly[(i + 1) % n];
        
        // Check if on edge
        if (cross(a, b, p) == 0 && 
            min(a.x, b.x) <= p.x && p.x <= max(a.x, b.x) &&
            min(a.y, b.y) <= p.y && p.y <= max(a.y, b.y)) {
            return 0;
        }
        
        if (a.y <= p.y) {
            if (b.y > p.y && cross(a, b, p) > 0) winding++;
        } else {
            if (b.y <= p.y && cross(a, b, p) < 0) winding--;
        }
    }
    
    return winding != 0 ? 1 : -1;
}

// ==================== Line Intersection ====================
// Returns true if lines AB and CD intersect, stores intersection in P
bool lineIntersection(Ptd A, Ptd B, Ptd C, Ptd D, Ptd& P) {
    ld d = (B - A).cross(D - C);
    if (abs(d) < EPS) return false;  // Parallel
    
    ld t = (C - A).cross(D - C) / d;
    P = A + (B - A) * t;
    return true;
}

void solve() {
    int n;
    cin >> n;
    
    vector<Pt> pts(n);
    rep(i, 0, n) cin >> pts[i].x >> pts[i].y;
    
    vector<Pt> hull = convexHull(pts);
    cout << sz(hull) << '\n';
    for (auto& p : hull) cout << p.x << ' ' << p.y << '\n';
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int t = 1;
    // cin >> t;
    while (t--) solve();
    
    return 0;
}
