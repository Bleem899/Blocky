/**
 * A player in the Blocky game.
 */
public class Player {
    private int id;
    private Goal goal;
    private BlockyTree blockyTree;
    private boolean smashUsed;

    /**
     * Initialize this player
     *
     * @param player_id  the number identifying the player
     * @param goal       the players goal (e.g., Blob, Perimeter and color)
     * @param blockyTree a ref to the game's tree
     */
    public Player(int player_id, Goal goal, BlockyTree blockyTree) {
        id = player_id;
        this.goal = goal;
        this.blockyTree = blockyTree;
        smashUsed = false;
    }

    /**
     * Determines the player's current score based on their goal.
     *
     * @return the score
     */
    public int getScore() {
        return goal.score(blockyTree);
    }

    /**
     * Creates a string representation for use on the UI panel.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "(" + getColor() + " " + getGoal().description() + "): " +
                getScore() + ", " + (isSmashUsed() ? " Smash used" : " Smash remaining");
    }

    public Goal getGoal() {
        return goal;
    }

    public MyColor getColor() {
        return goal.getColor();
    }

    public boolean isSmashUsed() {
        return smashUsed;
    }

    /**
     * Ensure the player can not smash again later.
     */
    public void usedSmash() {
        smashUsed = true;
    }
}