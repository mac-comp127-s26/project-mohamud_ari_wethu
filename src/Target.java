import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsObject;

import java.awt.Color;
import java.util.Random;

public class Target extends GraphicsGroup {
    private static final double TARGET_WIDTH = 40;
    private static final double TARGET_HEIGHT = 20;
    private static final double CIRCLE_RADIUS = 7;
    private static final double NUM_TARGETS_PER_ROW = 10;
    private static final double START_Y = 30;
    private static final double NUM_ROWS = 5;
    private static final double SPACING = 10;

    //private Rectangle rect;
   // private Ellipse circle; 
   private GraphicsObject shape;

public static GraphicsGroup createTargets() {
    GraphicsGroup layer = new GraphicsGroup();
    double layerY = START_Y;
        for (int i = 0; i < NUM_ROWS; i++) {
            boolean isCircleRow = (i % 2 != 0);
            generateTargetRow(layer, layerY, isCircleRow);
            layerY = layerY + TARGET_HEIGHT + SPACING;
        }
        return layer;
}

 private static void generateTargetRow(GraphicsGroup group, double startY, boolean useCircle) {
        double targetX = 8;
        Color rowColor = randColor();

    for (int col = 0; col < NUM_TARGETS_PER_ROW; col++) {
        Target target = new Target(targetX, startY, useCircle);
        target.setMainColor(rowColor);
        group.add(target);
        double width = useCircle ? (CIRCLE_RADIUS * 2) : TARGET_WIDTH;
        targetX = targetX + width+ SPACING;
        }
    }
 

 private static Color randColor() {
        Random rand = new Random();
        return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    public Target(double x, double y, boolean isCircle) {
        this.setPosition(x,y);
        if (isCircle) {
            shape = new Ellipse(0, 0, CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2);
        } else {
            shape = new Rectangle(0, 0, TARGET_WIDTH, TARGET_HEIGHT);
        }
            this.add(shape);
    }
     public void setMainColor(Color color) {
        if (shape instanceof Rectangle) {
            ((Rectangle) shape).setFillColor(color);
        } else if (shape instanceof Ellipse) {
            ((Ellipse) shape).setFillColor(color);
        }
    }
        
    }