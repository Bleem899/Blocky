import java.awt.*;

/**
 * An enumerated type to hold any potential colors.
 */
public enum MyColor {
    NONE (-1, -1, -1, "None"),
    BLUE(34, 113, 178, "Blue"),
    GREEN(53, 155, 115, "Green"),
    RED(213, 50, 0, "Red"),
    YELLOW(240, 228, 66, "Yellow"),
    BRIGHT_BLUE(61, 183, 233, "Bright Blue"),
    // Pink is used for debugging since it is easy to see
    PINK(247, 72, 165, "Pink"),
    BLACK(0, 0, 0, "Black");

    private final int red;
    private final int green;
    private final int blue;
    private final String name;

    /**
     * Constructs a color from the enum list above.
     * @param red
     * @param green
     * @param blue
     * @param name a string version of the name, used primarily to let a
     *             player know what color they were assigned
     */
    MyColor(int red, int green, int blue, String name) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.name = name;
    }

    /**
     * Builds the Color object as identified by the MyColor.
     * @return
     */
    public Color color() {
        return new Color(red, green, blue);
    }

    /**
     * Returns a string representation of a color.
     * @return
     */
    public String toString() {
        return name;
    }
}
