
// This will be the bullet that will inherit the Ellipse class from klit graphics

import java.awt.Color;

import edu.macalester.graphics.Ellipse;

public class Bullet extends Ellipse {
    private double dy;

    private static final Color BULLET_COLOR = Color.ORANGE;

    // The bullet will be coming out of the gun, which means it will use the gun class {abstraction}.
    public Bullet(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius, radius);

        this.setFillColor(BULLET_COLOR);
        this.setFilled(true);
        this.setStroked(false);

        this.dy = -10;
    }

    public void move() {
        this.moveBy(0, dy);
    }

    public double getDx() {
        return 0;
    }

    public double getDy() {
        return dy;
    }

    public void setVelocity(double dy) {
        this.dy = dy;
    }
}
