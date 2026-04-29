import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Rectangle;


import java.awt.Color;
import java.util.Random;

public class HarmfulTarget extends GraphicsGroup implements Target {
    private static final double WIDTH = 65;
    private static final double HEIGHT = 35;
    private static final Color COLOR = new Color(220, 50, 50);

    private double dx;
    private double dy;

    public HarmfulTarget(double x, double y) {
        setPosition(x, y);
        Rectangle rect = new Rectangle(0, 0, WIDTH, HEIGHT);
        rect.setFillColor(COLOR);
        add(rect);
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

    public int getPointValue() {
        return 10;
    }
}



    



