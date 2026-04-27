import edu.macalester.graphics.Line;
import edu.macalester.graphics.Point;


public class Gun {
    // The bullets will be coming out of this gun, which means it will use the bullet class {abstraction}.

    public static final double GUN_LENGTH = 25;

    private final Line line;
    private double angle;

    public Gun(double centerX, double centerY, double angleDegrees) {
        if (angleDegrees < 0 || angleDegrees > 180) {
            throw new IllegalArgumentException("angleDegrees must be an angle between 0 and 180 degrees");
        }

        line = new Line(centerX, centerY, 0, 0);
        line.setStrokeWidth(5);
        setAngle(angleDegrees);


public class Gun {
    public static final double CANNON_LENGTH = 25;

    private final Line line;
    private double angle;

    public Gun(double centerX, double centerY, double angleDegrees) {
        if (angleDegrees < 0 || angleDegrees > 180) {
            throw new IllegalArgumentException("angleDegrees must be an angle between 0 and 180 degrees");
        }

        line = new Line(centerX, centerY, 0, 0);
        line.setStrokeWidth(5);
        setAngle(angleDegrees);
    }

    private void setAngle(double angle) {
        this.angle = angle;

        double angleInRadians = Math.toRadians(angle);
        double x2 = line.getX1() + GUN_LENGTH * Math.cos(angleInRadians);
        double y2 = line.getY1() + GUN_LENGTH * -Math.sin(angleInRadians);
        line.setEndPosition(x2, y2);
