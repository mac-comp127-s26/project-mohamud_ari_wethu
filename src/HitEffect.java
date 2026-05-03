import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Rectangle;

import java.awt.Color;
import java.awt.Font;

public class HitEffect extends GraphicsGroup {

    public enum Type { HARMFUL, LIFE_EARTH, EXTRA_LIFE }

    private final Type type;
    private int frame = 0;
    private int maxFrames;

    private Ellipse burst;       // HARMFUL
    private Rectangle flash;     // LIFE_EARTH
    private Ellipse glow;        // EXTRA_LIFE
    private GraphicsText plusText; // EXTRA_LIFE

    // All constructors take the CENTER of the target.
    public HitEffect(double centerX, double centerY, Type type) {
        this.type = type;

        switch (type) {
            case HARMFUL -> {
                maxFrames = 22;
                // Circle starts at target size (radius 20) then expands outward
                burst = new Ellipse(-20, -20, 40, 40);
                burst.setFillColor(new Color(255, 120, 0, 210));
                burst.setStroked(false);
                add(burst);
            }
            case LIFE_EARTH -> {
                maxFrames = 16;
                // Yellow rectangle centered on the target (75 wide, 45 tall)
                flash = new Rectangle(-37, -22, 75, 44);
                flash.setFillColor(new Color(255, 240, 60, 200));
                flash.setStroked(false);
                add(flash);
            }
            case EXTRA_LIFE -> {
                maxFrames = 28;
                // Green glow circle starting at target size (radius 14)
                glow = new Ellipse(-14, -14, 28, 28);
                glow.setFillColor(new Color(60, 255, 120, 200));
                glow.setStroked(false);
                add(glow);
                plusText = new GraphicsText("+1", -8, 6);
                plusText.setFont(new Font("Arial", Font.BOLD, 16));
                plusText.setFillColor(Color.WHITE);
                add(plusText);
            }
        }

        setPosition(centerX, centerY);
    }

    public void update() {
        frame++;
        double p = (double) frame / maxFrames; // 0 → 1 over the animation

        switch (type) {
            case HARMFUL -> {
                // Ring expands from radius 20 to 65, color shifts orange → yellow, alpha fades
                double r = 20 + p * 45;
                burst.setX(-r);
                burst.setY(-r);
                burst.setSize(r * 2, r * 2);
                int green = (int) Math.min(255, 120 + 135 * p);
                int alpha = (int) (210 * (1 - p));
                burst.setFillColor(new Color(255, green, 0, Math.max(0, alpha)));
            }
            case LIFE_EARTH -> {
                // Rectangle blooms outward from target bounds then fades
                double exp = p * 12;
                flash.setX(-37 - exp);
                flash.setY(-22 - exp);
                flash.setSize(75 + exp * 2, 44 + exp * 2);
                int alpha = (int) (200 * (1 - p));
                flash.setFillColor(new Color(255, 240, 60, Math.max(0, alpha)));
            }
            case EXTRA_LIFE -> {
                // Glow expands while whole group floats upward; everything fades
                double r = 14 + p * 16;
                glow.setX(-r);
                glow.setY(-r);
                glow.setSize(r * 2, r * 2);
                moveBy(0, -1.5);
                int alpha = (int) (200 * (1 - p));
                glow.setFillColor(new Color(60, 255, 120, Math.max(0, alpha)));
                plusText.setFillColor(new Color(255, 255, 255, Math.max(0, alpha)));
            }
        }
    }

    public boolean isDone() {
        return frame >= maxFrames;
    }
}
