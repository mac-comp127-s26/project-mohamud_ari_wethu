import edu.macalester.graphics.CanvasWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TargetGame {
    private static final int CANVAS_WIDTH = 600;
    private static final int CANVAS_HEIGHT = 800;
    private static final double UPPER_BOUND = CANVAS_HEIGHT / 2.0;

    private CanvasWindow canvas;
    private Gun gun;
    private List<Bullet> bullets;
    private List<Target> targets;

    public TargetGame() {
        canvas = new CanvasWindow("Environmental Awareness Target", CANVAS_WIDTH, CANVAS_HEIGHT);

        gun = new Gun(275, 740);
        canvas.add(gun);

        bullets = new ArrayList<>();
        targets = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < 3; i++) {
            double x = rand.nextDouble() * (CANVAS_WIDTH - 65);
            double y = rand.nextDouble() * (UPPER_BOUND - 35);
            HarmfulTarget t = new HarmfulTarget(x, y);
            targets.add(t);
            canvas.add(t);
        }

        for (int i = 0; i < 3; i++) {
            double x = rand.nextDouble() * (CANVAS_WIDTH - 28);
            double y = rand.nextDouble() * (UPPER_BOUND - 28);
            LifeOnEarthTarget t = new LifeOnEarthTarget(x, y);
            targets.add(t);
            canvas.add(t);
        }

        for (int i = 0; i < 2; i++) {
            double x = rand.nextDouble() * (CANVAS_WIDTH - 28);
            double y = rand.nextDouble() * (UPPER_BOUND - 28);
            ExtraLivesTarget t = new ExtraLivesTarget(x, y);
            targets.add(t);
            canvas.add(t);
        }

        // Gun follows the mouse
        canvas.onMouseMove(e -> moveGunTo(e.getPosition().getX()));

        // Click to shoot
        canvas.onMouseDown(e -> {
            Bullet bullet = gun.shoot();
            bullets.add(bullet);
            canvas.add(bullet);
        });

        canvas.animate(() -> {
            for (Target target : targets) {
                target.move(CANVAS_WIDTH, UPPER_BOUND);
            }
            List<Bullet> offScreen = new ArrayList<>();
            for (Bullet b : bullets) {
                b.move();
                if (b.getY() + b.getHeight() < 0) {
                    canvas.remove(b);
                    offScreen.add(b);
                }
            }
            bullets.removeAll(offScreen);
        });
    }

    private void moveGunTo(double mouseX) {
        double x = mouseX - gun.getWidth() / 2;
        gun.setX(Math.max(0, Math.min(CANVAS_WIDTH - gun.getWidth(), x)));
    }

    public static void main(String[] args) {
        new TargetGame();
    }
}
