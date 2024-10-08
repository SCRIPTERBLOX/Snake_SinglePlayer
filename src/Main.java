import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

public class Main extends JFrame implements KeyListener {
    private BufferedImage image;
    private JPanel panel;
    private final int[][] defSnake = {{500, 300}, {500, 325}, {500, 350}, {500, 375}, {500, 400}};

    int[][] snake = defSnake;

    int[] food;
    public static int width = 1000;
    public static int height = 700;
    private String dir = "UP";
    private String nextDir = "UP";

    public Main(int speed, String name) {
        super("Snake");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        snake = new int[][]{{500, 300}, {500, 325}, {500, 350}, {500, 375}, {500, 400}};
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        panel.setBackground(Color.DARK_GRAY);
        add(panel);

        Timer timer = new Timer(speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                // Spawn the food if not already present
                if (food == null) {
                    do {
                        food = getRPos();
                    } while (isPositionInSnake(food));
                }

                // Move the snake
                dir = nextDir;
                int[] head = new int[2];
                head[0] = snake[0][0];
                head[1] = snake[0][1];

                switch (dir) {
                    case "UP":
                        head[1] -= 25;
                        break;
                    case "DOWN":
                        head[1] += 25;
                        break;
                    case "RIGHT":
                        head[0] += 25;
                        break;
                    case "LEFT":
                        head[0] -= 25;
                        break;
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
                        FileReadWriter.write(name+".txt", "C:\\Users\\scrip\\Documents\\Games\\Snake_SinglePlayer\\src\\Scores", String.valueOf(snake.length));
                        snake = defSnake;
                        dir = "UP";
                        break;
                    }
                }

                // Out-of-bounds detection
                if (head[0] >= width || head[1] >= height || head[0] < 0 || head[1] < 0) {
                    System.out.println("GAME OVER");
                    FileReadWriter.write(name, "C:\\Users\\scrip\\Documents\\Games\\Snake_SinglePlayer\\src\\Scores", String.valueOf(snake.length));
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
                panel.repaint();
            }
        });

        timer.start();
        addKeyListener(this);
        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                if (!dir.equals("DOWN")) nextDir = "UP";
                break;
            case KeyEvent.VK_S:
                if (!dir.equals("UP")) nextDir = "DOWN";
                break;
            case KeyEvent.VK_D:
                if (!dir.equals("LEFT")) nextDir = "RIGHT";
                break;
            case KeyEvent.VK_A:
                if (!dir.equals("RIGHT")) nextDir = "LEFT";
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String name = now.format(formatter);
        int speed = 500; // Default speed
        if (args.length > 0) {
            try {
                speed = Integer.parseInt(args[0]);
                if (speed <= 0) {
                    System.err.println("Speed must be a positive integer.");
                    System.exit(1);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid speed argument. Using default value.");
            }
        }
        int finalSpeed = speed;
        SwingUtilities.invokeLater(() -> new Main(finalSpeed, name));
    }

    public static int[] getRPos() {
        int[] pos;
        int maxX = (width / 25)-1;
        int maxY = (height / 25)-1;
        int posX = (int) (Math.random() * maxX);
        int posY = (int) (Math.random() * maxY);
        pos = new int[]{posX * 25, posY * 25};
        return pos;
    }

    public boolean isPositionInSnake(int[] pos) {
        return Arrays.stream(snake).anyMatch(part -> Arrays.equals(part, pos));
    }
}