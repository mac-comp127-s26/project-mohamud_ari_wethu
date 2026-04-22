

import edu.macalester.graphics.CanvasWindow;

public class TargetGame {
    private static final int CANVAS_WIDTH = 600;
    private static final int CANVAS_HEIGHT = 800;

    private CanvasWindow canvas;
    private Bullet bullet;

    public TargetGame() {
        // This will be the main class that will run the game, which means it will use the gun, bullet, and targets classes {abstraction}.
        canvas = new CanvasWindow("Environmental Awareness Target", CANVAS_WIDTH, CANVAS_HEIGHT);

        // Creating the bullet and positioning it within the canvas.
        bullet = new Bullet(290, 740, 10);
        canvas.add(bullet);

        canvas.animate(() -> {
            bullet.move();
        });
    }

    public static void main(String[] args) {
        new TargetGame();
    }
}
