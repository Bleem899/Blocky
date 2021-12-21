import java.awt.*;

/**
 * The tree representing the game state.
 */
public class BlockyTree {
    public static final MyColor[] COLOR_LIST = {MyColor.BLUE, MyColor.GREEN,
            MyColor.RED, MyColor.YELLOW};
    private Block root;
    private int maxDepth;
    private Block currentlySelected; // Remember which Block is selected.

    /**
     * Initializes the game tree.  It ensures there is one random Block.
     *
     * @param maxDepth the limiting factor to the depth of the tree
     */
    public BlockyTree(int maxDepth) {
        int size = 400;
        this.maxDepth = maxDepth;

        root = new Block(0, 0, size, COLOR_LIST[(int)(Math.random() * 3)], 0, maxDepth);

        root.setSelected();
        currentlySelected = root;
    }

    /**
     * Creates a random tree.
     * <p>
     * If a Block is not yet at its maximum depth, it can be subdivided;
     * this function must decide whether or not to actually do so. To decide:
     * - Use Math.random() to generate a random number in the interval [0, 1).
     * - Subdivide if the random number is less than Math.exp(-0.25 * level),
     * where level is the level of the Block within the tree.
     * - If a Block is not going to be subdivided, use a random integer to pick a
     * color for it from the list of colors in COLOR_LIST.
     */
    public void buildRandomTree() {
        buildRandomTreeKernel(root);
    }

    private void buildRandomTreeKernel(Block ref){
        if(Math.random() < Math.exp(-0.25 * ref.level())){
            if(ref.level() <= maxDepth) {
                ref.addChildren();
                for (int i = 0; i < 4; i++) {
                    buildRandomTreeKernel(ref.getChildren()[i]);
                }
            }
        }
    }

    /**
     * Returns a two-dimensional list representing this Block as rows and columns of unit cells.
     * <p>
     * Returns a 2d array, L, where,
     * for 0 <= i, j < 2^{max_depth - self.level}
     * - L[i] represents column i and
     * - L[i][j] represents the unit cell at column i and row j.
     * Each unit cell stores the MyColor of the block at the cell location[i][j].
     * <p>
     * L[0][0] represents the unit cell in the upper left corner of the Block.
     * <p>
     * A single block in the tree may be represented by multiple elements in the array.
     *
     * @return the flattened (array) version of the tree
     */
    public MyColor[][] flatten() {
        MyColor[][] L = new MyColor[(int)Math.pow(2, maxDepth - root.level() + 1)][(int)Math.pow(2, maxDepth - root.level() + 1)];
        int sizeOfBox = (int)Math.pow(2, maxDepth - root.level() + 1);
        return flattenKernel(root, 0, 0, L, sizeOfBox);
    }

    private MyColor[][] flattenKernel(Block ref, int row, int col, MyColor[][] array, int aSize){

        if(ref.hasChildren()) {

            flattenKernel(ref.getChildren()[0], row, col + aSize / 2, array, aSize / 2);
            flattenKernel(ref.getChildren()[1], row, col, array, aSize /2);
            flattenKernel(ref.getChildren()[2], row + aSize / 2, col, array, aSize / 2);
            flattenKernel(ref.getChildren()[3], row + aSize / 2, col + aSize / 2, array, aSize /2);

        }else{

            for (int i = row; i < aSize + row; i++) {
                for (int j = col; j < aSize + col; j++) {
                    array[i][j] = ref.getColor();
                }
            }

        }

        return array;

    }

    /**
     * Draws the tree.
     *
     * @param g the graphics context
     */
    public void draw(Graphics g) {
        drawKernel(g, root);
        g.setColor(new Color(61, 233, 233, 100));
        g.fillRect(currentlySelected.xPos(), currentlySelected.yPos(), currentlySelected.size(), currentlySelected.size());
        }

    /**
     * Private method to draw the tree rooted at ref.
     * <p>
     * Use g.setColor(...), g.fillRect(x, y, w, h), and g.drawLine(x1, y1, x2, y2).
     * To set the strokeWeight, you (unfortunately) have to use:
     * ((Graphics2D) g).setStroke(new BasicStroke(2));
     *
     * @param g
     * @param ref
     */
    public void drawKernel(Graphics g, Block ref) {
        if (ref.hasChildren()) {
            for (int i = 0; i < 4; i++) {
                drawKernel(g, ref.getChildren()[i]);
            }
        } else {

            g.setColor(ref.getColor().color());
            g.fillRect(ref.xPos(), ref.yPos(), ref.size(), ref.size());

            g.setColor(Color.BLACK);
            g.drawLine(ref.xPos(), ref.yPos(), ref.xPos() + ref.size(), ref.yPos());
            g.drawLine(ref.xPos(), ref.yPos() + ref.size(), ref.xPos(), ref.yPos() + ref.size());
            g.drawLine(ref.xPos(), ref.yPos() + ref.size(), ref.xPos() + ref.size(), ref.yPos() + ref.size());
            g.drawLine(ref.xPos() + ref.size(), ref.yPos(), ref.xPos() + ref.size(), ref.yPos() + ref.size());
            ((Graphics2D) g).setStroke(new BasicStroke(1));
        }
    }

    /**
     * Replaces the currently selected Block with four random children.
     *
     * @param player the current player to check if allowed to smash
     * @return false if the player has a smash remaining, otherwise returns
     * the result of calling Block's smash
     */
    public boolean smash(Player player) {
        if (player.isSmashUsed()) {
            return false;
        }else{
            if (currentlySelected.smash()) {
                player.usedSmash();
                return true;
            }else{
                return false;
            }
        }
    }

    /**
     * Swaps the currently selected Block's contents in the x direction.
     * Simply calls Block's horizontalSwap.
     *
     * @return result of Block's horizontalSwap
     */
    public boolean horizontalSwap() {
        return currentlySelected.horizontalSwap();
    }

    /**
     * Swaps the currently selected Block's contents in the y direction.
     * Simply calls Block's horizontalSwap.
     *
     * @return result of Block's verticalSwap
     */
    public boolean verticalSwap() {
        return currentlySelected.verticalSwap();
    }

    /**
     * Rotates the Block's children in a counter clockwise direction.
     * Simply calls Block's rotateCounterclockwise.
     *
     * @return result of Block's rotateCounterClockwise
     */
    public boolean rotateCounterclockwise() {
        return currentlySelected.rotateCounterclockwise();
    }


    /**
     * Rotates the Block's children in a clockwise direction.
     * Simply calls Block's rotateClockwise.
     *
     * @return result of Block's rotateClockwise
     */
    public boolean rotateClockwise() {
        return currentlySelected.rotateClockwise();
    }

    /**
     * Handles selecting the correct Block, based on the most recent click and
     * who was selected before the click
     *
     * @param x coordinate of the click
     * @param y coordinate of the click
     */
    public void processClick(int x, int y) {
        if (x < 0 || x > root.xPos() + root.size() || y < 0 || y > root.yPos() + root.size()) {
            System.out.println("Clicked outside of game board.");
            return;
        }

        if (currentlySelected == null) {
            currentlySelected = root;
            root.setSelected();
        } else {
            if (withinBlock(currentlySelected, x, y)) {
                if (currentlySelected.hasChildren()) {
                    // If so, try to go one level deeper below that block.
                    // I.e., find the child of the currently that was clicked (if one exists) and select it.
                    currentlySelected.clearSelected();
                    for (Block block : currentlySelected.getChildren()) { // Could be a while, but...
                        if (withinBlock(block, x, y)) {
                            block.setSelected();
                            currentlySelected = block;
                        }
                    }
                }
            } else {
                currentlySelected.clearSelected();
                currentlySelected = root;
                root.setSelected();
            }
        }
    }

    /**
     * Helper function to determine if the (x,y) position is within the given Block.
     *
     * @param ref the Block to be checked
     * @param x   coordinate to be checked
     * @param y   coordinate to be checked
     * @return whether the (x,y) falls within the Block
     */
    private boolean withinBlock(Block ref, int x, int y) {
        // Check to see if the click was within the block.
        int currMinX = ref.xPos();
        int currMaxX = ref.xPos() + ref.size();
        int currMinY = ref.yPos();
        int currMaxY = ref.yPos() + ref.size();

        return x >= currMinX && x < currMaxX && y >= currMinY && y < currMaxY;
    }
}