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
    private static final int SPEED_RAMP_INTERVAL = 600; // ~10 seconds at 60fps

    private CanvasWindow canvas;
    private Gun gun;
    private List<Bullet> bullets;
    private List<Target> targets;

    private int lives;
    private int score;
    private int wave;
    private int frameCount;
    private int speedLevel;
    private boolean gameOver;
    private boolean playerWon;

    private GraphicsText livesText;
    private GraphicsText scoreText;
    private GraphicsText waveText;
    private GraphicsText harmfulCountText;
    private GraphicsText speedText;
    private GraphicsGroup overlay;
    private List<HitEffect> animations;

    public TargetGame() {
        canvas = new CanvasWindow("Environmental Awareness Target", CANVAS_WIDTH, CANVAS_HEIGHT);

        gun = new Gun(275, 740);
        canvas.add(gun);

        bullets = new ArrayList<>();
        targets = new ArrayList<>();
        animations = new ArrayList<>();
        lives = MAX_LIVES;
        score = 0;
        wave = 1;
        frameCount = 0;
        speedLevel = 1;
        gameOver = false;
        playerWon = false;

        waveText = new GraphicsText("Wave: 1", 10, 25);
        waveText.setFont(new Font("Arial", Font.BOLD, 18));
        waveText.setFillColor(Color.BLACK);

        harmfulCountText = new GraphicsText("Enemies: 0", 200, 25);
        harmfulCountText.setFont(new Font("Arial", Font.BOLD, 18));
        harmfulCountText.setFillColor(new Color(200, 0, 0));

        scoreText = new GraphicsText("Score: 0", 460, 25);
        scoreText.setFont(new Font("Arial", Font.BOLD, 18));
        scoreText.setFillColor(new Color(0, 120, 0));

        livesText = new GraphicsText("Lives: " + lives, 10, CANVAS_HEIGHT - 15);
        livesText.setFont(new Font("Arial", Font.BOLD, 20));
        livesText.setFillColor(Color.BLACK);

        speedText = new GraphicsText("Speed: Lv.1", 390, CANVAS_HEIGHT - 15);
        speedText.setFont(new Font("Arial", Font.BOLD, 18));
        speedText.setFillColor(new Color(130, 0, 200));

        createTargets();

        canvas.add(waveText);
        canvas.add(harmfulCountText);
        canvas.add(scoreText);
        canvas.add(livesText);
        canvas.add(speedText);

        updateHarmfulCount();

        canvas.onMouseMove(e -> moveGunTo(e.getPosition().getX()));

        canvas.onMouseDown(e -> {
            if (gameOver) return;
            Bullet bullet = gun.shoot();
            bullets.add(bullet);
            canvas.add(bullet);
        });

        canvas.onKeyDown(e -> {
            if (!gameOver) return;
            if (e.getKey() == Key.Y) {
                if (playerWon) {
                    wave++;           // advance to harder wave on win
                } else {
                    wave = 1;         // reset wave on loss
                    score = 0;
                }
                restartGame();
            } else if (e.getKey() == Key.N) {
                canvas.closeWindow();
            }
        });

        canvas.animate(() -> {
            if (gameOver) return;

            // Speed ramp: targets get 15% faster every ~10 seconds
            frameCount++;
            if (frameCount % SPEED_RAMP_INTERVAL == 0) {
                speedLevel++;
                speedText.setText("Speed: Lv." + speedLevel);
                for (Target t : targets) {
                    t.multiplySpeed(1.15);
                }
            }

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
                        if (!(t instanceof LifeOnEarthTarget)) {
                            targetsToRemove.add(t);
                        }
                        handleTargetHit(t);
                        break;
                    }
                }
            }

            for (Bullet b : bulletsToRemove) canvas.remove(b);
            for (Target t : targetsToRemove) canvas.remove((GraphicsGroup) t);
            bullets.removeAll(bulletsToRemove);
            targets.removeAll(targetsToRemove);

            if (!targetsToRemove.isEmpty()) {
                updateHarmfulCount();
                checkWin();
            }

            // Tick all hit animations; remove finished ones
            List<HitEffect> doneEffects = new ArrayList<>();
            for (HitEffect e : animations) {
                e.update();
                if (e.isDone()) doneEffects.add(e);
            }
            for (HitEffect e : doneEffects) canvas.remove(e);
            animations.removeAll(doneEffects);
        });
    }

    private void handleTargetHit(Target t) {
        GraphicsGroup g = (GraphicsGroup) t;
        double cx = g.getX() + g.getWidth() / 2;
        double cy = g.getY() + g.getHeight() / 2;

        HitEffect.Type effectType;
        if (t instanceof HarmfulTarget) {
            score += 10;
            scoreText.setText("Score: " + score);
            effectType = HitEffect.Type.HARMFUL;
        } else if (t instanceof LifeOnEarthTarget) {
            score = Math.max(0, score - 5);
            scoreText.setText("Score: " + score);
            loseLife();
            effectType = HitEffect.Type.LIFE_EARTH;
        } else {
            gainLife();
            effectType = HitEffect.Type.EXTRA_LIFE;
        }

        HitEffect effect = new HitEffect(cx, cy, effectType);
        animations.add(effect);
        canvas.add(effect);
    }

    private void updateHarmfulCount() {
        long count = targets.stream()
            .filter(t -> t instanceof HarmfulTarget)
            .count();
        harmfulCountText.setText("Enemies: " + count);
    }

    private void checkWin() {
        if (gameOver) return;
        long harmfulRemaining = targets.stream()
            .filter(t -> t instanceof HarmfulTarget)
            .count();
        if (harmfulRemaining == 0) {
            playerWon = true;
            gameOver = true;
            showWin();
        }
    }

    private void createTargets() {
        Random rand = new Random();
        int harmfulCount = Math.min(3 + (wave - 1), 7); // 3–7 enemies based on wave

        for (int i = 0; i < harmfulCount; i++) {
            double x = rand.nextDouble() * (CANVAS_WIDTH - 40);
            double y = rand.nextDouble() * (UPPER_BOUND - 40);
            HarmfulTarget t = new HarmfulTarget(x, y);
            targets.add(t);
            canvas.add(t);
        }

        for (int i = 0; i < 3; i++) {
            double x = rand.nextDouble() * (CANVAS_WIDTH - 75);
            double y = rand.nextDouble() * (UPPER_BOUND - 45);
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

        // Each wave beyond 1 starts with faster targets (+25% per wave)
        if (wave > 1) {
            double waveMult = 1.0 + (wave - 1) * 0.25;
            for (Target t : targets) {
                t.multiplySpeed(waveMult);
            }
        }
    }

    private void loseLife() {
        lives--;
        livesText.setText("Lives: " + lives);
        if (lives <= 0 && !gameOver) {
            gameOver = true;
            playerWon = false;
            showGameOver();
        }
    }

    private void gainLife() {
        lives++;
        livesText.setText("Lives: " + lives);
    }

    private void showWin() {
        overlay = new GraphicsGroup();

        Rectangle bg = new Rectangle(0, 230, CANVAS_WIDTH, 290);
        bg.setFillColor(new Color(0, 80, 0, 210));
        overlay.add(bg);

        GraphicsText title = new GraphicsText("YOU WIN!", 155, 325);
        title.setFont(new Font("Arial", Font.BOLD, 56));
        title.setFillColor(new Color(255, 220, 0));
        overlay.add(title);

        GraphicsText waveCleared = new GraphicsText("Wave " + wave + " cleared!   Score: " + score, 80, 385);
        waveCleared.setFont(new Font("Arial", Font.BOLD, 22));
        waveCleared.setFillColor(Color.WHITE);
        overlay.add(waveCleared);

        GraphicsText prompt = new GraphicsText("Y = Next Wave      N = Quit", 105, 445);
        prompt.setFont(new Font("Arial", Font.PLAIN, 22));
        prompt.setFillColor(Color.WHITE);
        overlay.add(prompt);

        canvas.add(overlay);
    }

    // Similar to showWin but with different text/colors
    private void showGameOver() {
        overlay = new GraphicsGroup();

        Rectangle bg = new Rectangle(0, 250, CANVAS_WIDTH, 240);
        bg.setFillColor(new Color(0, 0, 0, 210));
        overlay.add(bg);

        GraphicsText title = new GraphicsText("GAME OVER", 105, 340);
        title.setFont(new Font("Arial", Font.BOLD, 52));
        title.setFillColor(Color.RED);
        overlay.add(title);

        GraphicsText finalScore = new GraphicsText("Final Score: " + score, 185, 395);
        finalScore.setFont(new Font("Arial", Font.BOLD, 24));
        finalScore.setFillColor(Color.YELLOW);
        overlay.add(finalScore);

        GraphicsText prompt = new GraphicsText("Y = Restart      N = Quit", 110, 450);
        prompt.setFont(new Font("Arial", Font.PLAIN, 22));
        prompt.setFillColor(Color.WHITE);
        overlay.add(prompt);

        canvas.add(overlay);
    }

    // Resets game state and UI for a new game or next wave
    private void restartGame() {
        canvas.remove(overlay);
        overlay = null;

        // Remove all existing targets and bullets
        for (Target t : targets) canvas.remove((GraphicsGroup) t);
        targets.clear();

        // Remove bullets after targets so we don't accidentally remove targets that were just added
        for (Bullet b : bullets) canvas.remove(b);
        bullets.clear();

        lives = MAX_LIVES;
        frameCount = 0;
        speedLevel = 1;
        gameOver = false;
        playerWon = false;

        for (HitEffect e : animations) canvas.remove(e);
        animations.clear();

        createTargets();

        // Re-add HUD on top so it renders above new targets
        canvas.remove(waveText);
        canvas.remove(harmfulCountText);
        canvas.remove(scoreText);
        canvas.remove(livesText);
        canvas.remove(speedText);

        // Update HUD text to reflect reset state
        waveText.setText("Wave: " + wave);
        livesText.setText("Lives: " + lives);
        scoreText.setText("Score: " + score);
        speedText.setText("Speed: Lv.1");

        // Add HUD back after targets so it renders on top
        canvas.add(waveText);
        canvas.add(harmfulCountText);
        canvas.add(scoreText);
        canvas.add(livesText);
        canvas.add(speedText);

        updateHarmfulCount();
    }

    // Moves the gun horizontally to follow the mouse, keeping it within canvas bounds
    private void moveGunTo(double mouseX) {
        double x = mouseX - gun.getWidth() / 2;
        gun.setX(Math.max(0, Math.min(CANVAS_WIDTH - gun.getWidth(), x)));
    }

    public static void main(String[] args) {
        new TargetGame();
    }
}
