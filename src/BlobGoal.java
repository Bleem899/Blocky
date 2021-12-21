/**
 * A goal to create the largest connected blob of this goal's target
 * colour, anywhere within the Block.
 */
public class BlobGoal extends Goal {

    public BlobGoal(MyColor targetColor) {

        super(targetColor);
    }

    /**
     * Compute the largest blob score for the current tree and the targetColor.
     *
     * @param blockyTree the tree to be scored
     * @return the size of the largest blob
     */
    @Override
    public int score(BlockyTree blockyTree) {

        int maxSize;

        MyColor targetColor = getColor();
        MyColor[][] array = blockyTree.flatten();
        int[][] intArray = new int[array.length][array.length];

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                intArray[i][j] = -1;
            }
        }

        maxSize = scoreKernel(intArray, array, 0, 0, targetColor);

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {

                int value = scoreKernel(intArray, array, i, j, targetColor);

                if(value > maxSize) {
                    maxSize = value;
                }
            }
        }

        return maxSize;
    }

    private int scoreKernel(int[][] intArray, MyColor[][] array, int row, int col, MyColor color){
        int blobSize;

        if(row < 0 || col < 0 || row >= intArray.length || col >= intArray.length){
            blobSize = 0;
        }else{
            if (array[row][col] == color && intArray[row][col] == -1) {
                intArray[row][col] = 1;
                blobSize = 1 + scoreKernel(intArray, array, row + 1, col, color)
                        + scoreKernel(intArray, array, row - 1, col, color)
                        + scoreKernel(intArray, array, row, col + 1, color)
                        + scoreKernel(intArray, array, row, col - 1, color);
            }else {
                intArray[row][col] = 0;
                blobSize = 0;
            }
        }

        return blobSize;
    }

    /**
     * A short description of the scoring goal.
     *
     * @return the description
     */
    @Override
    public String description() {
        return "Largest Blob";
    }
}