/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * COMPUTATIONAL GEOMETRY Template - Complete Geometry Library
 * Includes: Points, Lines, Polygons, Convex Hull, Intersection,
 *           Closest Pair, Rotating Calipers, etc.
 *
 * USAGE: Import specific classes or copy algorithms you need
 */
import java.io.*;
import java.util.*;

public class Geometry {
    static BufferedReader br;
    static StringTokenizer st;
    static PrintWriter out;

    static final double EPS = 1e-9;
    static final double PI = Math.PI;
    static final double INF = 1e18;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(new BufferedOutputStream(System.out));
        solve();
        out.flush();
        out.close();
    }

    static void solve() throws IOException {
        // Example: Convex Hull
        int n = nextInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(nextDouble(), nextDouble());
        }

        List<Point> hull = ConvexHull.monotoneChain(points);
        out.println(hull.size());
    }

    // ==================== POINT CLASS ====================
    static class Point implements Comparable<Point> {
        double x, y;

        Point(double x, double y) { this.x = x; this.y = y; }

        // Vector operations
        Point add(Point o) { return new Point(x + o.x, y + o.y); }
        Point subtract(Point o) { return new Point(x - o.x, y - o.y); }
        Point multiply(double k) { return new Point(x * k, y * k); }
        Point divide(double k) { return new Point(x / k, y / k); }

        // Dot product
        double dot(Point o) { return x * o.x + y * o.y; }

        // Cross product (z-component)
        double cross(Point o) { return x * o.y - y * o.x; }

        // Distance to origin
        double dist() { return Math.sqrt(x * x + y * y); }

        // Squared distance to origin
        double distSq() { return x * x + y * y; }

        // Distance to another point
        double distTo(Point o) { return Math.sqrt((x - o.x) * (x - o.x) + (y - o.y) * (y - o.y)); }

        // Squared distance to another point
        double distToSq(Point o) { return (x - o.x) * (x - o.x) + (y - o.y) * (y - o.y); }

        // Normalize
        Point normalize() {
            double d = dist();
            return new Point(x / d, y / d);
        }

        // Rotate by angle (radians)
        Point rotate(double angle) {
            double cos = Math.cos(angle), sin = Math.sin(angle);
            return new Point(x * cos - y * sin, x * sin + y * cos);
        }

        // Rotate 90 degrees counter-clockwise
        Point rotate90() { return new Point(-y, x); }

        // Rotate 90 degrees clockwise
        Point rotate90CW() { return new Point(y, -x); }

        // Compare by x, then y
        public int compareTo(Point o) {
            if (Math.abs(x - o.x) > EPS) return Double.compare(x, o.x);
            return Double.compare(y, o.y);
        }

        public String toString() { return "(" + x + ", " + y + ")"; }

        public boolean equals(Object o) {
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return Math.abs(x - p.x) < EPS && Math.abs(y - p.y) < EPS;
        }

        public int hashCode() { return Objects.hash(x, y); }
    }

    // ==================== LINE CLASS ====================
    static class Line {
        Point p1, p2;
        double a, b, c; // ax + by + c = 0

        // Line through two points
        Line(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            a = p1.y - p2.y;
            b = p2.x - p1.x;
            c = -a * p1.x - b * p1.y;
        }

        // Line from equation ax + by + c = 0
        Line(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
            if (Math.abs(b) > EPS) {
                p1 = new Point(0, -c / b);
                p2 = new Point(1, -(a + c) / b);
            } else {
                p1 = new Point(-c / a, 0);
                p2 = new Point(-c / a, 1);
            }
        }

        // Check if point is on line
        boolean contains(Point p) {
            return Math.abs(a * p.x + b * p.y + c) < EPS;
        }

        // Distance from point to line
        double distTo(Point p) {
            return Math.abs(a * p.x + b * p.y + c) / Math.sqrt(a * a + b * b);
        }

        // Check if two lines are parallel
        boolean isParallel(Line o) {
            return Math.abs(a * o.b - b * o.a) < EPS;
        }

        // Check if two lines are the same
        boolean isSame(Line o) {
            return isParallel(o) && Math.abs(a * o.c - c * o.a) < EPS;
        }

        // Intersection point with another line
        Point intersection(Line o) {
            double det = a * o.b - b * o.a;
            if (Math.abs(det) < EPS) return null; // Parallel
            return new Point((b * o.c - c * o.b) / det, (c * o.a - a * o.c) / det);
        }

        // Check if line segment intersects with another segment
        static boolean segmentsIntersect(Point a, Point b, Point c, Point d) {
            return ccw(a, b, c) * ccw(a, b, d) <= 0 && ccw(c, d, a) * ccw(c, d, b) <= 0;
        }

        // Counter-clockwise test
        static int ccw(Point a, Point b, Point c) {
            double val = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
            if (val > EPS) return 1;
            if (val < -EPS) return -1;
            return 0;
        }
    }

    // ==================== SEGMENT CLASS ====================
    static class Segment {
        Point p1, p2;

        Segment(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        double length() { return p1.distTo(p2); }
        double lengthSq() { return p1.distToSq(p2); }

        // Check if point is on segment
        boolean contains(Point p) {
            return Math.abs((p.x - p1.x) * (p2.y - p1.y) - (p.y - p1.y) * (p2.x - p1.x)) < EPS
                && Math.min(p1.x, p2.x) - EPS <= p.x && p.x <= Math.max(p1.x, p2.x) + EPS
                && Math.min(p1.y, p2.y) - EPS <= p.y && p.y <= Math.max(p1.y, p2.y) + EPS;
        }

        // Distance from point to segment
        double distTo(Point p) {
            double l2 = lengthSq();
            if (l2 == 0) return p.distTo(p1);

            double t = Math.max(0, Math.min(1, p.subtract(p1).dot(p2.subtract(p1)) / l2));
            Point proj = p1.add(p2.subtract(p1).multiply(t));
            return p.distTo(proj);
        }

        // Intersection point with a line
        Point intersection(Line line) {
            Line segLine = new Line(p1, p2);
            return segLine.intersection(line);
        }
    }

    // ==================== CIRCLE CLASS ====================
    static class Circle {
        Point center;
        double radius;

        Circle(Point center, double radius) {
            this.center = center;
            this.radius = radius;
        }

        Circle(double x, double y, double r) {
            this(new Point(x, y), r);
        }

        double area() { return PI * radius * radius; }
        double circumference() { return 2 * PI * radius; }

        // Check if point is inside circle
        boolean contains(Point p) {
            return p.distTo(center) <= radius + EPS;
        }

        // Check if circle contains another circle
        boolean contains(Circle o) {
            return center.distTo(o.center) + o.radius <= radius + EPS;
        }

        // Check if two circles intersect
        boolean intersects(Circle o) {
            double d = center.distTo(o.center);
            return d <= radius + o.radius + EPS && d >= Math.abs(radius - o.radius) - EPS;
        }

        // Intersection points with another circle
        List<Point> intersection(Circle o) {
            List<Point> points = new ArrayList<>();
            double d = center.distTo(o.center);

            if (d > radius + o.radius + EPS || d < Math.abs(radius - o.radius) - EPS) {
                return points; // No intersection
            }

            double a = (radius * radius - o.radius * o.radius + d * d) / (2 * d);
            double h = Math.sqrt(Math.max(0, radius * radius - a * a));

            Point p2 = center.add(o.center.subtract(center).multiply(a / d));
            points.add(new Point(p2.x + h * (o.center.y - center.y) / d,
                                 p2.y - h * (o.center.x - center.x) / d));

            if (h > EPS) {
                points.add(new Point(p2.x - h * (o.center.y - center.y) / d,
                                     p2.y + h * (o.center.x - center.x) / d));
            }
            return points;
        }

        // Tangent points from external point
        List<Point> tangentsFrom(Point p) {
            List<Point> points = new ArrayList<>();
            double d = p.distTo(center);

            if (d < radius - EPS) return points; // Point inside circle

            double a = radius * radius / d;
            double h = Math.sqrt(Math.max(0, radius * radius - a * a));

            Point p2 = center.add(p.subtract(center).multiply(a / d));
            points.add(new Point(p2.x + h * (p.y - center.y) / d,
                                 p2.y - h * (p.x - center.x) / d));

            if (h > EPS) {
                points.add(new Point(p2.x - h * (p.y - center.y) / d,
                                     p2.y + h * (p.x - center.x) / d));
            }
            return points;
        }
    }

    // ==================== POLYGON CLASS ====================
    static class Polygon {
        List<Point> vertices;
        int n;

        Polygon(List<Point> vertices) {
            this.vertices = vertices;
            this.n = vertices.size();
        }

        // Perimeter
        double perimeter() {
            double sum = 0;
            for (int i = 0; i < n; i++) {
                sum += vertices.get(i).distTo(vertices.get((i + 1) % n));
            }
            return sum;
        }

        // Area using shoelace formula
        double area() {
            double sum = 0;
            for (int i = 0; i < n; i++) {
                Point p1 = vertices.get(i);
                Point p2 = vertices.get((i + 1) % n);
                sum += p1.x * p2.y - p2.x * p1.y;
            }
            return Math.abs(sum) / 2;
        }

        // Check if polygon is convex
        boolean isConvex() {
            boolean hasPos = false, hasNeg = false;
            for (int i = 0; i < n; i++) {
                double cross = vertices.get(i).subtract(vertices.get((i + n - 1) % n))
                              .cross(vertices.get((i + 1) % n).subtract(vertices.get(i)));
                if (cross > EPS) hasPos = true;
                if (cross < -EPS) hasNeg = true;
                if (hasPos && hasNeg) return false;
            }
            return true;
        }

        // Check if point is inside polygon (ray casting)
        boolean contains(Point p) {
            boolean inside = false;
            for (int i = 0, j = n - 1; i < n; j = i++) {
                Point pi = vertices.get(i), pj = vertices.get(j);
                if (((pi.y > p.y) != (pj.y > p.y)) &&
                    (p.x < (pj.x - pi.x) * (p.y - pi.y) / (pj.y - pi.y) + pi.x)) {
                    inside = !inside;
                }
            }
            return inside;
        }

        // Centroid
        Point centroid() {
            double cx = 0, cy = 0, area = area();
            for (int i = 0; i < n; i++) {
                Point p1 = vertices.get(i);
                Point p2 = vertices.get((i + 1) % n);
                double cross = p1.x * p2.y - p2.x * p1.y;
                cx += (p1.x + p2.x) * cross;
                cy += (p1.y + p2.y) * cross;
            }
            return new Point(cx / (6 * area), cy / (6 * area));
        }
    }

    // ==================== CONVEX HULL ====================
    static class ConvexHull {
        // Monotone Chain algorithm - O(n log n)
        static List<Point> monotoneChain(Point[] points) {
            int n = points.length;
            if (n <= 2) {
                List<Point> hull = new ArrayList<>();
                for (Point p : points) hull.add(p);
                return hull;
            }

            Arrays.sort(points);

            List<Point> lower = new ArrayList<>();
            for (Point p : points) {
                while (lower.size() >= 2) {
                    Point a = lower.get(lower.size() - 2);
                    Point b = lower.get(lower.size() - 1);
                    if ((b.x - a.x) * (p.y - a.y) - (b.y - a.y) * (p.x - a.x) <= EPS) {
                        lower.remove(lower.size() - 1);
                    } else break;
                }
                lower.add(p);
            }

            List<Point> upper = new ArrayList<>();
            for (int i = n - 1; i >= 0; i--) {
                Point p = points[i];
                while (upper.size() >= 2) {
                    Point a = upper.get(upper.size() - 2);
                    Point b = upper.get(upper.size() - 1);
                    if ((b.x - a.x) * (p.y - a.y) - (b.y - a.y) * (p.x - a.x) <= EPS) {
                        upper.remove(upper.size() - 1);
                    } else break;
                }
                upper.add(p);
            }

            lower.remove(lower.size() - 1);
            upper.remove(upper.size() - 1);
            lower.addAll(upper);
            return lower;
        }

        // Graham scan - O(n log n)
        static List<Point> grahamScan(Point[] points) {
            int n = points.length;
            if (n <= 2) {
                List<Point> hull = new ArrayList<>();
                for (Point p : points) hull.add(p);
                return hull;
            }

            // Find bottom-left point
            Point pivot = points[0];
            for (Point p : points) {
                if (p.y < pivot.y || (p.y == pivot.y && p.x < pivot.x)) {
                    pivot = p;
                }
            }

            final Point P = pivot;
            Arrays.sort(points, (a, b) -> {
                double cross = (a.x - P.x) * (b.y - P.y) - (a.y - P.y) * (b.x - P.x);
                if (Math.abs(cross) > EPS) return cross > 0 ? -1 : 1;
                return Double.compare(P.distToSq(a), P.distToSq(b));
            });

            List<Point> hull = new ArrayList<>();
            for (Point p : points) {
                while (hull.size() >= 2) {
                    Point a = hull.get(hull.size() - 2);
                    Point b = hull.get(hull.size() - 1);
                    if ((b.x - a.x) * (p.y - a.y) - (b.y - a.y) * (p.x - a.x) <= EPS) {
                        hull.remove(hull.size() - 1);
                    } else break;
                }
                hull.add(p);
            }
            return hull;
        }

        // Convex hull diameter using rotating calipers - O(n)
        static double diameter(List<Point> hull) {
            int n = hull.size();
            if (n <= 2) return hull.get(0).distTo(hull.get(n - 1));

            double maxDist = 0;
            int j = 1;

            for (int i = 0; i < n; i++) {
                Point a = hull.get(i);
                Point b = hull.get((i + 1) % n);

                while (true) {
                    Point c = hull.get(j);
                    Point d = hull.get((j + 1) % n);

                    double area1 = Math.abs((b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x));
                    double area2 = Math.abs((b.x - a.x) * (d.y - a.y) - (b.y - a.y) * (d.x - a.x));

                    if (area2 > area1) {
                        j = (j + 1) % n;
                    } else {
                        break;
                    }
                }

                maxDist = Math.max(maxDist, hull.get(i).distTo(hull.get(j)));
            }
            return maxDist;
        }
    }

    // ==================== CLOSEST PAIR OF POINTS ====================
    static class ClosestPair {
        // Divide and conquer - O(n log n)
        static double find(Point[] points) {
            int n = points.length;
            Point[] sortedByX = points.clone();
            Point[] sortedByY = points.clone();

            Arrays.sort(sortedByX);
            Arrays.sort(sortedByY, (a, b) -> Double.compare(a.y, b.y));

            return rec(sortedByX, sortedByY);
        }

        static double rec(Point[] byX, Point[] byY) {
            int n = byX.length;
            if (n <= 3) {
                double min = Double.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        min = Math.min(min, byX[i].distTo(byX[j]));
                    }
                }
                return min;
            }

            int mid = n / 2;
            Point midPoint = byX[mid];

            Point[] leftX = Arrays.copyOfRange(byX, 0, mid);
            Point[] rightX = Arrays.copyOfRange(byX, mid, n);

            Point[] leftY = new Point[mid];
            Point[] rightY = new Point[n - mid];
            int li = 0, ri = 0;

            for (Point p : byY) {
                if (p.x < midPoint.x - EPS || (Math.abs(p.x - midPoint.x) < EPS && p.y <= midPoint.y)) {
                    leftY[li++] = p;
                } else {
                    rightY[ri++] = p;
                }
            }

            double d1 = rec(leftX, leftY);
            double d2 = rec(rightX, rightY);
            double d = Math.min(d1, d2);

            List<Point> strip = new ArrayList<>();
            for (Point p : byY) {
                if (Math.abs(p.x - midPoint.x) < d) {
                    strip.add(p);
                }
            }

            for (int i = 0; i < strip.size(); i++) {
                for (int j = i + 1; j < strip.size() && strip.get(j).y - strip.get(i).y < d; j++) {
                    d = Math.min(d, strip.get(i).distTo(strip.get(j)));
                }
            }

            return d;
        }
    }

    // ==================== PICK'S THEOREM ====================
    static class PicksTheorem {
        // Area = I + B/2 - 1
        // Where I = interior points, B = boundary points
        // For a polygon with integer coordinates

        static long boundaryPoints(List<Point> polygon) {
            long count = 0;
            int n = polygon.size();
            for (int i = 0; i < n; i++) {
                Point p1 = polygon.get(i);
                Point p2 = polygon.get((i + 1) % n);
                count += gcd(Math.abs((long)(p1.x - p2.x)), Math.abs((long)(p1.y - p2.y)));
            }
            return count;
        }

        static long interiorPoints(List<Point> polygon) {
            double area = 0;
            int n = polygon.size();
            for (int i = 0; i < n; i++) {
                Point p1 = polygon.get(i);
                Point p2 = polygon.get((i + 1) % n);
                area += p1.x * p2.y - p2.x * p1.y;
            }
            area = Math.abs(area) / 2;

            long B = boundaryPoints(polygon);
            return (long)(area - B / 2.0 + 1);
        }

        static long gcd(long a, long b) {
            return b == 0 ? a : gcd(b, a % b);
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
    static double nextDouble() throws IOException { return Double.parseDouble(next()); }
}
