import edu.macalester.graphics.Ellipse;

public interface Target {
    void move(double canvasWidth, double upperBound);
    boolean intersects(Bullet bullet)};

