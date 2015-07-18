package game.math;
import static java.lang.Math.*;

import com.sun.istack.internal.NotNull;

import java.util.Comparator;

public class Vec2d {

    public double x, y;
    public Vec2d angleComparatorInitialVertex;

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2d(){
        x = 0;
        y = 0;
    }

    public void setEqual(@NotNull Vec2d vector) {
        x = vector.x;
        y = vector.y;
    }

    public void setEqual(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void plusEquals(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void plusEquals(Vec2d v) {
        x += v.x;
        y += v.y;
    }

    public Vec2d add(double dx, double dy) {
        return new Vec2d(x + dx, y + dy);
    }

    public Vec2d add(Vec2d v) {
        return new Vec2d(v.x+x, v.y+y);
    }

    public Vec2d subtract(double dx, double dy) {
        return add(-dx, -dy);
    }

    public Vec2d subtract(Vec2d v) {
        return new Vec2d(x-v.x, y-v.y);
    }

    public Vec2d multiply(double scalar) {
        return new Vec2d(scalar * x, scalar * y);
    }

    public Vec2d normalize() {
        double magnitude = magnitude();
        return new Vec2d(x / magnitude, y / magnitude);
    }

    public double magnitude() {
        return sqrt(x*x + y*y);
    }

    //Perpendicular dot product
    public double perpDot(Vec2d v) {
        return x*v.y - y*v.x;
    }

    public double angleRad() {
        if(x == 0) return 0;
        return atan(y/x);
    }

    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

    public static Comparator<Vec2d> angleComparator = new Comparator<Vec2d>() {
        public int compare(Vec2d v1, Vec2d v2) {
            double angle1 = v1.subtract(v1.angleComparatorInitialVertex).angleRad();
            double angle2 = v2.subtract(v2.angleComparatorInitialVertex).angleRad();

            return Double.compare(angle1, angle2);
        }
    };
}
