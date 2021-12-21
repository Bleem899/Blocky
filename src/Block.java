/**
 * The "node" for a Block in the game.
 */
public class Block {
    private int xPos, yPos; // Top-left corner of the block
    private int size; // Height and width of the *square* block
    private MyColor color;
    private int level;
    private boolean selected;
    private static int maxDepth;

    //    The children are stored in the order
    //    upper-right, upper-left, lower-left, lower-right.
    private Block[] children;

    /**
     * Initialize the Block.
     *
     * @param xPos     the Block's left coordinate
     * @param yPos     the Block's top coordinate
     * @param size     the size of the Block
     * @param color    the Block's color
     * @param level    the distance to the root
     * @param maxDepth the maximum depth allowed in this tree
     */
    public Block(int xPos, int yPos, int size, MyColor color, int level, int maxDepth) {
        // Initialize this Block to be an un highlighted block.
        // Use the provided position, size, level, and color to initialize the Block.
        this.xPos = xPos;
        this.yPos = yPos;
        this.size = size;
        this.color = color;
        this.level = level;
    }

    /**
     * Swaps the children in the x direction.
     *
     * @return false if the block has no children, true otherwise
     */
    public boolean horizontalSwap() {
        Block temp;

        if(hasChildren()) {
            temp = children[0];
            children[0] = children[1];
            children[1] = temp;

            temp = children[3];
            children[3] = children[2];
            children[2] = temp;

            updateChildLocations();

            for (int i = 0; i < 4; i++) {
                children[i].horizontalSwap();
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * Swaps the children in the y direction.
     *
     * @return false if no children, true otherwise
     */
    public boolean verticalSwap() {
        Block temp;

        if(hasChildren()) {
            temp = children[0];
            children[0] = children[3];
            children[3] = temp;

            temp = children[1];
            children[1] = children[2];
            children[2] = temp;

            updateChildLocations();

            for (int i = 0; i < 4; i++) {
                children[i].verticalSwap();
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * Rotates the Block's children clockwise.
     *
     * @return false if no children, true otherwise
     */
    public boolean rotateClockwise() {
        Block temp;

        if(hasChildren()) {
            temp = children[0];
            children[0] = children[1];
            children[1] = children[2];
            children[2] = children[3];
            children[3] = temp;

            updateChildLocations();

            for (int i = 0; i < 4; i++) {
                children[i].rotateClockwise();
            }
            return true;
        }else {
            return false;
        }
    }

    /**
     * Rotate the Block's children counter clockwise.
     *
     * @return false if no children, true otherwise
     */


    public boolean rotateCounterclockwise() {
        Block temp;

        if(hasChildren()) {
            temp = children[0];
            children[0] = children[3];
            children[3] = children[2];
            children[2] = children[1];
            children[1] = temp;

            updateChildLocations();

            for (int i = 0; i < 4; i++) {
                getChildren()[i].rotateCounterclockwise();
            }
            return true;
        }else {
            return false;
        }
    }

    /**
     * This method breaks the current Block into four randomly colored children.
     *
     * @return false if the Block is at level zero or at the maximum level, true otherwise
     */
    public boolean smash() {
        // A Block can be smashed iff it is not the top-level Block and it
        // is not already at the level of the maximum depth.

        if(level() != 0 || level() != maxDepth){
            addChildren();
            return true;
        } else {
            return false;
        }

    }

    /**
     * Update the position and size of each of the children within this Block.
     * Also selects a random color for each of the children.
     * <p>
     * Ensure that each child is consistent with the position and size of its
     * parent Block.
     * <p>
     * The order is 0 = top right, 1 = top left, 2 = bottom left, 3 = bottom right.
     */
    public void updateChildLocations() {

        children[0].xPos = xPos() + size/2;
        children[0].yPos = yPos();
        children[0].size = size()/2;
        children[0].level = level()+1;

        children[1].xPos = xPos();
        children[1].yPos = yPos();
        children[1].size = size()/2;
        children[1].level = level()+1;

        children[2].xPos = xPos();
        children[2].yPos = yPos() + size/2;
        children[2].size = size()/2;
        children[2].level = level()+1;

        children[3].xPos = xPos() + size/2;
        children[3].yPos = yPos() + size/2;
        children[3].size = size()/2;
        children[3].level = level()+1;
    }

    public void setSelected() {
        selected = true;
    }

    public int xPos() {
        return xPos;
    }

    public int yPos() {
        return yPos;
    }

    public int size() {
        return size;
    }

    public int level() {
        return level;
    }

    public void clearSelected() {
        selected = false;
    }

    public boolean hasChildren() {
        if(getChildren() == null){
            return false;
        }
        return true;
    }

    public Block[] getChildren() {
        return children;
    }

    public MyColor getColor(){
        return color;
    }

    public void addChildren() {
        children = new Block[4];
        children[0] = new Block(xPos() + size()/2, yPos(), size()/2, BlockyTree.COLOR_LIST[(int)(Math.random() * 4)], level() + 1, maxDepth);
        children[1] = new Block(xPos(), yPos(), size()/2, BlockyTree.COLOR_LIST[(int)(Math.random() * 4)], level() + 1, maxDepth);
        children[2] = new Block(xPos(), yPos() + size()/2, size()/2, BlockyTree.COLOR_LIST[(int)(Math.random() * 4)], level() + 1, maxDepth);
        children[3] = new Block(xPos() + size()/2, yPos() + size()/2, size()/2, BlockyTree.COLOR_LIST[(int)(Math.random() * 4)], level() + 1, maxDepth);
    }
}