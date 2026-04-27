import edu.macalester.graphics.Line;

public class Gun {
    // The bullets will be coming out of this gun, which means it will use the bullet class {abstraction}.

    public static final double GUN_LENGTH = 25;

    private final Line line;
    private double angle;

    public Gun(double centerX, double centerY, double angleDegrees) {
        if (angleDegrees < 0 || angleDegrees > 180) {
            throw new IllegalArgumentException("angleDegrees must be an angle between 0 and 180 degrees");
        }
        line = new Line(centerX, centerY, centerX, centerY-GUN_LENGTH);
        line.setStrokeWidth(5);
        setAngle(angleDegrees);
    }

    public Line getLine() {
        return line;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angleDegrees) {
        if (angleDegrees < 0 || angleDegrees > 180) {
            return;
        }

        this.angle = angleDegrees;

        double angleRadians = Math.toRadians(angleDegrees);

        double startX = line.getX1();
        double startY = line.getY1();

        double endX = startX + GUN_LENGTH * Math.cos(angleRadians);
        double endY = startY - GUN_LENGTH * Math.sin(angleRadians);

        line.setPosition(endX, endY);
    }

    public void rotateLeft() {
        setAngle(angle + 5);
    }

    public void rotateRight() {
        setAngle(angle - 5);
    }

    public Bullet shoot() {
        double angleRadians = Math.toRadians(angle);

        double startX = line.getX1();
        double startY = line.getY1();

        double endX = startX + GUN_LENGTH * Math.cos(angleRadians);
        double endY = startY - GUN_LENGTH * Math.sin(angleRadians);

        return new Bullet(endX, endY, angle);
    }
}

