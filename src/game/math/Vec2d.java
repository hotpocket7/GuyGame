package game.math;
import static java.lang.Math.*;

import com.sun.istack.internal.NotNull;

import java.util.Comparator;

public class Vec2d {

    public double x, y;

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2d(Vec2d v) {
        this(v.x, v.y);
    }

    public Vec2d(double a) {
        this(a, a);
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

    public Vec2d rotate(Vec2d v, double angle) {
        Vec2d temp = subtract(v);
        double a = atan2(temp.y, temp.x);
        double mag = temp.magnitude();
        return v.add(mag * cos(toRadians(angle) + a), mag * sin(toRadians(angle) + a));
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

    public boolean equals(Vec2d v) {
        return Double.compare(v.x, x) == 0 && Double.compare(v.y, y) == 0;
    }

    public double distance(Vec2d v) {
        return subtract(v).magnitude();
    }

    public String toString() {
        return String.format("(%f, %f)", x, y);
    }
}
