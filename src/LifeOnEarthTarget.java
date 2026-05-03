import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Rectangle;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class LifeOnEarthTarget extends GraphicsGroup implements Target {
    private static final double SIZE = 45;
    private static final Color COLOR = new Color(30, 144, 255);

    private double dx;
    private double dy;

    public LifeOnEarthTarget(double x, double y) {
        setPosition(x, y);
        Rectangle square = new Rectangle(0, 0, SIZE, SIZE);
        square.setFillColor(COLOR);
        add(square);

        GraphicsText label = new GraphicsText("L", SIZE / 2 - 6, SIZE / 2 + 8);
        label.setFillColor(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label);

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

    @Override
    // Circle-rectangle collision detection
    public boolean intersects(Bullet bullet) {
        double bulletCenterX = bullet.getX() + bullet.getWidth() / 2;
        double bulletCenterY = bullet.getY() + bullet.getHeight() / 2;

        double rectLeft   = getX();
        double rectRight  = getX() + getWidth();
        double rectTop    = getY();
        double rectBottom = getY() + getHeight();

        double closestX = Math.max(rectLeft,  Math.min(bulletCenterX, rectRight));
        double closestY = Math.max(rectTop,   Math.min(bulletCenterY, rectBottom));

        double distX = bulletCenterX - closestX;
        double distY = bulletCenterY - closestY;
        double radius = bullet.getWidth() / 2;

        return (distX * distX + distY * distY) < (radius * radius);
    }

    @Override
    public void multiplySpeed(double factor) {
        dx *= factor;
        dy *= factor;
    }

    public int getPointValue() {
        return 5;
    }
}
