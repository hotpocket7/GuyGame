package game.collision;

import game.math.Vec2d;

import java.awt.geom.Line2D;
import java.util.ArrayList;

public class PolygonHitbox extends Hitbox {

    public ArrayList<Vec2d> vertices = new ArrayList<>();
    public ArrayList<Line2D.Double> edges = new ArrayList<>();

    public PolygonHitbox(Vec2d... verts) {
        for(Vec2d vertex : verts) {
            vertices.add(vertex);
        }
        updateEdges();
    }

    public boolean collides(RectangularHitbox hitbox) {
        for(Line2D.Double edge : edges) {
            if(edge.intersects(hitbox.bounds))
                return true;
        }
        return false;
    }

    public boolean collides(PolygonHitbox hitbox) {
        for(Line2D.Double edge1 : edges) {
            for(Line2D.Double edge2 : hitbox.edges) {
                if(edge1.intersectsLine(edge2))
                    return true;
            }
        }
        return false;
    }

    public boolean collides(Line2D.Double line){
        for(Line2D.Double edge : edges) {
            if(edge.intersectsLine(line))
                return true;
        }
        return false;
    }

    public void updateBounds(double dx, double dy) {
        for(Vec2d vert : vertices) {
            vert.plusEquals(dx, dy);
        }
        updateEdges();
    }

    public void updateBounds(Vec2d delta) {
        updateBounds(delta.x, delta.y);
    }

    private void updateEdges() {
        edges = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            int numVerts = vertices.size();
            edges.add(new Line2D.Double(vertices.get(i % numVerts).x, vertices.get(i % numVerts).y,
                    vertices.get((i + 1) % numVerts).x, vertices.get((i + 1) % numVerts).y));
        }
    }

}
