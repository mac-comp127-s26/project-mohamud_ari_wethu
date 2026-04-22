import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Rectangle;

import java.awt.Color;
import java.util.Random;

public class Target extends GraphicsGroup {
    private static final double TARGET_WIDTH = 40;
    private static final double TARGET_HEIGHT = 20;
    private static final double CIRCLE_RADIUS = 15;
    private static final double NUM_TARGETS_PER_ROW = 10;
    private static final double RECTANGLE_HEIGHT = 10;
    private static final double RECTANGLE_WIDTH = 40;
    private static final double START_Y = 30;
    private static final double NUM_ROWS = 5;
    private static final double SPACING = 10;

    private Rectangle rect;
    private Ellipse circle; 

public static GraphicsGroup Targets() {
    GraphicsGroup layer = new GraphicsGroup();
     double layerY = START_Y;
        for (int i = 0; i < NUM_ROWS; i++) {
            generateTargetRow(layer, layerY);
            layerY = layerY + TARGET_HEIGHT + SPACING;
        }
        return layer;
}

 private static void generateTargetRow(GraphicsGroup group, double startY) {
        double targetX = 8;
        //double targetY = startY;
        Color rowColor = randColor();

    for (int col = 0; col < NUM_TARGETS_PER_ROW; col++) {
        Target target = new Target(targetX, startY);
        target.setMainColor(rowColor);
        group.add(target.rect);
        targetX = targetX + TARGET_WIDTH + SPACING;
        }
    }
 

 private static Color randColor() {
        Random rand = new Random();
        return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    public Target(double x, double y) {
        this.setPosition(x,y);
        rect = new Rectangle(0, 0, TARGET_WIDTH, TARGET_HEIGHT);
        rect.setFillColor(new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
    }}