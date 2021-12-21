/**
 * A template for scoring goals.
 */
public abstract class Goal {
    private MyColor color;

    /**
     * Initialize this goal to have the given target colour.
     *
     * @param targetColor the player's color to be maximized
     */
    public Goal(MyColor targetColor) {
        color = targetColor;
    }

    /**
     * Return the current score for this goal on the given board.
     * The score is always greater than or equal to 0.
     */
    public abstract int score(BlockyTree blockyTree);

    /**
     * Return a description of this goal.
     *
     * @return the goal string
     */
    public abstract String description();

    /**
     * Returns the color assigned to the goal.
     *
     * @return the color
     */
    public MyColor getColor() {
        return color;
    }
}