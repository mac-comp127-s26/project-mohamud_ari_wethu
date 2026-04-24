import edu.macalester.graphics.CanvasWindow;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TargetGame {
    private static final int CANVAS_WIDTH = 600;
    private static final int CANVAS_HEIGHT = 800;
    private static final double UPPER_BOUND = CANVAS_HEIGHT / 2.0;

    private CanvasWindow canvas;
    private Bullet bullet;
    private List<Target> targets;

    public TargetGame() {
        // Initialize the canvas and game elements
        canvas = new CanvasWindow("Environmental Awareness Target", CANVAS_WIDTH, CANVAS_HEIGHT);

        // Create the bullet and add it to the canvas
        bullet = new Bullet(290, 740, 10);
        canvas.add(bullet);

        // Create targets and add them to the canvas
        targets = new ArrayList<>();
        Random rand = new Random();

        // Create 3 harmful targets
        for (int i = 0; i < 3; i++) {
            double x = rand.nextDouble() * (CANVAS_WIDTH - 40);
            double y = rand.nextDouble() * (UPPER_BOUND - 20);
            HarmfulTarget t = new HarmfulTarget(x, y);
            targets.add(t);
            canvas.add(t);
        }

        // Create 3 life on earth targets
        for (int i = 0; i < 3; i++) {
            double x = rand.nextDouble() * (CANVAS_WIDTH - 14);
            double y = rand.nextDouble() * (UPPER_BOUND - 14);
            LifeOnEarthTarget t = new LifeOnEarthTarget(x, y);
            targets.add(t);
            canvas.add(t);
        }

        // Create 2 extra lives targets
        for (int i = 0; i < 2; i++) {
            double x = rand.nextDouble() * (CANVAS_WIDTH - 14);
            double y = rand.nextDouble() * (UPPER_BOUND - 14);
            ExtraLivesTarget t = new ExtraLivesTarget(x, y);
            targets.add(t);
            canvas.add(t);
        }

        // Start the animation loop
        canvas.animate(() -> {
            bullet.move();
            for (Target target : targets) {
                target.move(CANVAS_WIDTH, UPPER_BOUND);
            }
        });
    }

    public static void main(String[] args) {
        new TargetGame();
    }
}
