import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsGroup;

import java.awt.Color;
import java.util.Random;

public class ExtraLivesTarget extends GraphicsGroup implements Target {
    private static final double RADIUS = 14;
    private static final Color COLOR = new Color(0, 200, 0);

    private double dx;
    private double dy;

    public ExtraLivesTarget(double x, double y) {
        setPosition(x, y);
        Ellipse circle = new Ellipse(0, 0, RADIUS * 2, RADIUS * 2);
        circle.setFillColor(COLOR);
        add(circle);
        Random rand = new Random();
        dx = rand.nextBoolean() ? 1.5 : -1.5;
        dy = rand.nextBoolean() ? 1.0 : -1.0;
    }

    @Override
    public void move(double canvasWidth, double upperBound) {
        moveBy(dx, dy);
        double w = getWidth();
        double h = getHeight();
        if (getX() < 0) { setX(0); dx = -dx; }
        else if (getX() + w > canvasWidth) { setX(canvasWidth - w); dx = -dx; }
        if (getY() < 0) { setY(0); dy = -dy; }
        else if (getY() + h > upperBound) { setY(upperBound - h); dy = -dy; }
    }

    public int getExtraLives() {
        return 1;
    }
}
