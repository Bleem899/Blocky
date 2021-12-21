/**
 * A goal to achieve the most squares of the goal's color on the perimeter of the board.
 */
public class PerimeterGoal extends Goal {
    public PerimeterGoal(MyColor targetColor) {
        super(targetColor);
    }

    /**
     * Count the number of the targetColor squares that are on the perimeter.
     * Note: the corners count double!
     *
     * @param blockyTree the tree to be scored
     * @return the total number of targetColor "units" on the perimeter
     */
    @Override
    public int score(BlockyTree blockyTree) {
        int points = 0;

        MyColor targetColor = getColor();
        MyColor[][] array = blockyTree.flatten();

        for (int i = 0; i < array.length; i++) {
            if (array[0][i] == targetColor){
                points++;
            }
            if(array[array.length - 1][i] == targetColor) {
                points++;
            }
            if (array[i][0] == targetColor){
                points++;
            }
            if(array[i][array.length - 1] == targetColor) {
                points++;
            }
        }
        return points;
    }

    /**
     * A short description of the scoring goal.
     *
     * @return the description
     */
    @Override
    public String description() {
        return "Perimeter";
    }
}