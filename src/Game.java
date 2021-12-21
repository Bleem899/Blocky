import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The game UI.  It handles the flow of the game, cycling between the players.
 */
public class Game extends JFrame {
    private BlockyPanel blockyPanel;
    private BlockyTree blockyTree;
    private Player[] players;
    private JLabel playerLabel;
    private JLabel status;
    private int turnsRemaining;

    private int currentPlayer;

    /**
     * Create a game.
     *
     * @param numPlayers the number of players
     * @param depth      the maximum level of the Blocky tree
     * @param numTurns   the number of turns allowed in the game
     */
    public Game(int numPlayers, int depth, int numTurns) {
        turnsRemaining = numTurns;

        setTitle("Blocky Game");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        blockyTree = new BlockyTree(depth);
        blockyTree.buildRandomTree();

        MyColor[][] F = blockyTree.flatten();

        for (int i = 0; i < F.length; i++) {
            for (int j = 0; j < F[i].length; j++) {
                System.out.println(F[i][j]);
            }
        }

        players = new Player[numPlayers];
        for (int i = 0; i < players.length; i++) {
            Goal g;

            if (Math.random() < .5) {
                g = new BlobGoal(BlockyTree.COLOR_LIST[(int) (Math.random() * 4)]);
            } else {
                g = new PerimeterGoal(BlockyTree.COLOR_LIST[(int) (Math.random() * 4)]);
            }
            players[i] = new Player(i, g, blockyTree);
        }
        currentPlayer = 0;

        add(buildPanel());

        setVisible(true);
    }

    /**
     * Constructs the UI on the main panel.
     *
     * @return the panel that was constructed
     */
    private JPanel buildPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Build the main panel.
        blockyPanel = new BlockyPanel(blockyTree);
        panel.add(blockyPanel, BorderLayout.CENTER);

        // This is the top label telling whose turn.
        playerLabel = new JLabel("<html><h1 style=\"color:blue\">Turns left: " +
                turnsRemaining + ", Player " + (currentPlayer + 1) + "'s turn");
        panel.add(playerLabel, BorderLayout.NORTH);

        // This is the static instructions panel.
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.add(new JLabel("<html>Click to select a block<br/>" +
                "S: smash<br/>" +
                "H/V: horizontal/vertical swap<br/>" +
                "Left/Right: CCW/CW rotation</html>"));
        panel.add(instructionsPanel, BorderLayout.EAST);

        // This label contains the score and will contain status in the case of bad moves.
        status = new JLabel();
        String statusString = buildStatus();
        status.setText("<html>" + statusString + "<br/></html>");
        add(status, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Iterates through the players and builds a string of each of their status's.
     *
     * @return a string representing all players goals and scores
     */
    private String buildStatus() {
        String statusString = "";
        for (int i = 0; i < players.length; i++) {
            statusString += "Player " + (i + 1) + " " + players[i] + "<br/>";
        }
        statusString += "<br/>&nbsp;";
        return statusString;
    }

    /**
     * A panel that handles clicks and keyevents regarding the main gameboard.
     */
    public class BlockyPanel extends JPanel {
        private BlockyTree blockyTree;

        /**
         * Constructor that draws the tree and handles subsequent mouse and key events.
         *
         * @param blockyTree a preinitialized BlockyTree object
         */
        public BlockyPanel(BlockyTree blockyTree) {
            this.blockyTree = blockyTree;

            setFocusable(true); // Necessary to handle key events.

            addMouseListener(new ClickListener());
            addKeyListener(new KeyPressListener());
        }

        /**
         * An override to make the tree drawable.
         *
         * @param g the current graphics context
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            blockyTree.draw(g);
        }

        /**
         * Handles clicks on the gameboard.
         */
        class ClickListener extends MouseAdapter {
            /**
             * Fired when the mouse is pressed.
             *
             * @param evt the event from pressing the mouse button
             */
            public void mousePressed(MouseEvent evt) {
                blockyTree.processClick(evt.getX(), evt.getY());

                repaint();
            }
        }

        /**
         * Handles key presses on the gameboard.
         */
        class KeyPressListener implements KeyListener {
            @Override
            /**
             * Fired when a key is pressed.
             * @param e the event from pressing the key
             */
           public void keyPressed(KeyEvent e) {
               boolean success;
               switch (e.getKeyChar()) {
                   case 'h':
                       case 'H':
                           success = blockyTree.horizontalSwap();
                           break;
                           case 'v':
                               case 'V':
                                   success = blockyTree.verticalSwap();
                                   break;
                                   case 's':
                                       case 'S':
                                           success = blockyTree.smash(players[currentPlayer]);
                                           break;
                                           default:
                                               switch (e.getKeyCode()) {
                                                   case KeyEvent.VK_LEFT:
                                                       success = blockyTree.rotateCounterclockwise();
                                                       break;
                                                       case KeyEvent.VK_RIGHT:
                                                           success = blockyTree.rotateClockwise();
                                                           break;
                                                           default:
                                                               success = false;
                                                               break;
                                               }
                                               break;
               }
               if (success) {
                   currentPlayer = (currentPlayer + 1) % players.length;
                   playerLabel.setText("<html><h1 style=\"color:blue\">Turns left: " +
                           turnsRemaining + ", Player " + (currentPlayer + 1) + "'s turn");

                   String statusString = buildStatus();
                   status.setText("<html>" + statusString + "<br/></html>");
                   if (currentPlayer == 0) {
                       turnsRemaining--;
                   }

                   if (turnsRemaining == 0) {
                       int max = 0;
                       for (int i = 1; i < players.length; i++) {
                           if (players[i].getScore() > players[max].getScore()) {
                               max = i;
                           }
                       }

                       JOptionPane.showMessageDialog(null, "Player " + (max + 1) + " wins!");
                   }
               } else {  // Show an error message if move was unsuccessful.
                   String statusString = buildStatus();
                   status.setText("<html>" + statusString + "<span style=\"color: red\">Invalid move</span></html>");
               }

               repaint();
           }

           @Override
           public void keyTyped(KeyEvent e) { // Ignored.
           }

           @Override
           public void keyReleased(KeyEvent e) { // Ignored.
           }
        }
    }
}
