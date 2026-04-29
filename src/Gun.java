import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Rectangle;

import java.awt.Color;

public class Gun extends GraphicsGroup {
    private static final double BODY_WIDTH = 50;
    private static final double BODY_HEIGHT = 20;
    private static final double BARREL_WIDTH = 10;
    private static final double BARREL_HEIGHT = 30;

    public Gun(double x, double y) {
        setPosition(x, y);

        Rectangle body = new Rectangle(0, 0, BODY_WIDTH, BODY_HEIGHT);
        body.setFillColor(Color.DARK_GRAY);
        add(body);

        Rectangle barrel = new Rectangle((BODY_WIDTH - BARREL_WIDTH) / 2, -BARREL_HEIGHT, BARREL_WIDTH, BARREL_HEIGHT);
        barrel.setFillColor(new Color(80, 80, 80));
        add(barrel);
    }

    public Bullet shoot() {
        double bulletX = getX() + (BODY_WIDTH - 8) / 2;
        double bulletY = getY() - BARREL_HEIGHT - 8;
        return new Bullet(bulletX, bulletY, 8);
    }
}
