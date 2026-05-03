import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.events.Key;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TargetGame {
    private static final int CANVAS_WIDTH = 600;
    private static final int CANVAS_HEIGHT = 800;
    private static final double UPPER_BOUND = CANVAS_HEIGHT / 2.0;
    private static final int MAX_LIVES = 3;

    private CanvasWindow canvas;
    private Gun gun;
    private List<Bullet> bullets;
    private List<Target> targets;
    private int lives;
    private GraphicsText livesText;
    private boolean gameOver;
    private GraphicsGroup gameOverOverlay;

    public TargetGame() {
        canvas = new CanvasWindow("Environmental Awareness Target", CANVAS_WIDTH, CANVAS_HEIGHT);

        gun = new Gun(275, 740);
        canvas.add(gun);

        bullets = new ArrayList<>();
        targets = new ArrayList<>();
        lives = MAX_LIVES;
        gameOver = false;

        livesText = new GraphicsText("Lives: " + lives, 10, CANVAS_HEIGHT - 15);
        livesText.setFont(new Font("Arial", Font.BOLD, 20));
        livesText.setFillColor(Color.BLACK);

        createTargets();
        canvas.add(livesText); // added last so it always renders on top

        // Gun follows the mouse
        canvas.onMouseMove(e -> moveGunTo(e.getPosition().getX()));

        // Click to shoot
        canvas.onMouseDown(e -> {
            if (gameOver) return;
            Bullet bullet = gun.shoot();
            bullets.add(bullet);
            canvas.add(bullet);
        });

        // Y to restart, N to quit after game over
        canvas.onKeyDown(e -> {
            if (!gameOver) return;
            if (e.getKey() == Key.Y) {
                restartGame();
            } else if (e.getKey() == Key.N) {
                canvas.closeWindow();
            }
        });

        canvas.animate(() -> {
            if (gameOver) return;

            for (Target target : targets) {
                target.move(CANVAS_WIDTH, UPPER_BOUND);
            }

            List<Bullet> bulletsToRemove = new ArrayList<>();
            List<Target> targetsToRemove = new ArrayList<>();

            for (Bullet b : bullets) {
                b.move();
                if (b.getY() + b.getHeight() < 0) {
                    bulletsToRemove.add(b);
                    continue;
                }
                for (Target t : targets) {
                    if (!targetsToRemove.contains(t) && t.intersects(b)) {
                        bulletsToRemove.add(b);
                        targetsToRemove.add(t);
                        if (t instanceof LifeOnEarthTarget) {
                            loseLife();
                        }
                        break;
                    }
                }
            }

            for (Bullet b : bulletsToRemove) canvas.remove(b);
            for (Target t : targetsToRemove) canvas.remove((GraphicsGroup) t);
            bullets.removeAll(bulletsToRemove);
            targets.removeAll(targetsToRemove);
        });
    }

    private void createTargets() {
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
    }

    private void loseLife() {
        lives--;
        livesText.setText("Lives: " + lives);
        if (lives <= 0) {
            showGameOver();
        }
    }

    private void showGameOver() {
        gameOver = true;

        gameOverOverlay = new GraphicsGroup();

        Rectangle bg = new Rectangle(0, 250, CANVAS_WIDTH, 220);
        bg.setFillColor(new Color(0, 0, 0, 200));
        gameOverOverlay.add(bg);

        GraphicsText title = new GraphicsText("GAME OVER", 120, 340);
        title.setFont(new Font("Arial", Font.BOLD, 52));
        title.setFillColor(Color.RED);
        gameOverOverlay.add(title);

        GraphicsText prompt = new GraphicsText("Restart? Press Y (yes) or N (no)", 90, 410);
        prompt.setFont(new Font("Arial", Font.PLAIN, 22));
        prompt.setFillColor(Color.WHITE);
        gameOverOverlay.add(prompt);

        canvas.add(gameOverOverlay);
    }

    private void restartGame() {
        canvas.remove(gameOverOverlay);
        gameOverOverlay = null;

        for (Target t : targets) canvas.remove((GraphicsGroup) t);
        targets.clear();

        for (Bullet b : bullets) canvas.remove(b);
        bullets.clear();

        lives = MAX_LIVES;
        gameOver = false;

        createTargets();
        canvas.remove(livesText);
        canvas.add(livesText); // re-add last so it stays on top
        livesText.setText("Lives: " + lives);
    }

    private void moveGunTo(double mouseX) {
        double x = mouseX - gun.getWidth() / 2;
        gun.setX(Math.max(0, Math.min(CANVAS_WIDTH - gun.getWidth(), x)));
    }

    public static void main(String[] args) {
        new TargetGame();
    }
}
