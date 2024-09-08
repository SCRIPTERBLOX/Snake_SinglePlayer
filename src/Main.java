import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Main extends JFrame implements KeyListener {
    private BufferedImage image;
    private JPanel panel;
    private int[][] defSnake = {{500, 300}, {500, 325}, {500, 350}, {500, 375}, {500, 400}};

    int[][] snake = defSnake;

    int[] food;
    int width = 1000;
    int height = 700;
    private String dir = "UP";
    private String nextDir = "UP";

    public Main() {
        // Set the title of the JFrame
        super("Snake");

        // Set the size of the JFrame
        setSize(width, height);

        // Close the application when the JFrame is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a BufferedImage with the specified width and height
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Initialize the snake position
        snake = new int[][]{{500, 300}, {500, 325}, {500, 350}, {500, 375}, {500, 400}};

        // Create a JPanel for custom painting
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };

        // Set the background color of the panel
        panel.setBackground(Color.DARK_GRAY);

        // Add the panel to the JFrame
        add(panel);

        // Set up a timer to update the game state
        // Set up a timer to update the game state
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Spawn the food
                if (food == null) {
                    int[] pos = getRPos();
                    boolean found = false;

                    int[] finalPos = pos;
                    do {
                        pos = getRPos();
                    } while (Arrays.stream(snake).anyMatch(row -> Arrays.equals(row, finalPos)));

                    pos = finalPos;

                    int posX = pos[0];
                    int posY = pos[1];

                    posX *= 25;
                    posY *= 25;

                    food = new int[]{posX, posY};
                }

                // Move the snake
                dir = nextDir;

                int[] head = new int[2];
                head[0] = snake[0][0];
                head[1] = snake[0][1];

                if (dir.equals("UP")) {
                    head[1] -= 25;
                } else if (dir.equals("DOWN")) {
                    head[1] += 25;
                } else if (dir.equals("RIGHT")) {
                    head[0] += 25;
                } else if (dir.equals("LEFT")) {
                    head[0] -= 25;
                }

                int[][] newSnake;
                if (head[0] == food[0] && head[1] == food[1]) {
                    // If the snake eats the food, increase the length by 1
                    newSnake = new int[snake.length + 1][2];
                    newSnake[0] = head;
                    System.arraycopy(snake, 0, newSnake, 1, snake.length);
                    snake = newSnake;
                    food = null; // Food should disappear after being eaten
                } else {
                    // Otherwise, keep the length the same
                    newSnake = new int[snake.length][2];
                    newSnake[0] = head;
                    System.arraycopy(snake, 0, newSnake, 1, snake.length - 1);
                    snake = newSnake;
                }

                // Collision detection
                head = snake[0];
                for (int i = 1; i < snake.length; i++) {
                    if (head[0] == snake[i][0] && head[1] == snake[i][1]) {
                        System.out.println("GAME OVER");
                        snake = defSnake;
                        dir = "UP";
                        break;
                    }
                }

                // Out-of-bounds detection
                if (head[0] >= width || head[1] >= height || head[0] < 0 || head[1] < 0) {
                    System.out.println("GAME OVER");
                    snake = defSnake;
                    dir = "UP";
                }

                // Clear the image first
                Graphics2D g2d = image.createGraphics();
                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRect(0, 0, image.getWidth(), image.getHeight());

                // Draw the food
                if (food != null) {
                    g2d.setColor(Color.RED);
                    g2d.fillRect(food[0], food[1], 25, 25);
                }

                // Draw the snake
                for (int i = 0; i < snake.length; i++) {
                    Color color = (i == 0) ? new Color(0, 100, 0) : new Color(0, 200, 0);
                    g2d.setColor(color);
                    g2d.fillRect(snake[i][0], snake[i][1], 25, 25);
                }

                g2d.dispose();

                // Request the panel to repaint
                panel.repaint();
            }
        });

        timer.start();

        // Add the key listener to the JFrame
        addKeyListener(this);

        // Make the JFrame visible
        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No implementation needed
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Handle arrow key presses to change snake direction
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (!(dir == "DOWN")) {
                nextDir = "UP";
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (!(dir == "UP")) {
                nextDir = "DOWN";
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (!(dir == "LEFT")) {
                nextDir = "RIGHT";
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (!(dir == "RIGHT")) {
                nextDir = "LEFT";
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No implementation needed
    }

    public static void main(String[] args) {
        // Run the application
        SwingUtilities.invokeLater(Main::new);
    }
    public static int[] getRPos() {
        int[] pos;

        int maxX = (1000 - 1) / 25;
        int maxY = (700 - 1) / 25;

        int posX = (int) (Math.random() * (maxX + 1));
        int posY = (int) (Math.random() * (maxY + 1));

        pos = new int[]{posX, posY};

        return pos;
    }
}