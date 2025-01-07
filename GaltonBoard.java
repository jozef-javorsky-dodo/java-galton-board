import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GaltonBoard {

    public static final int DEFAULT_WIDTH = 800;

    public static final int DEFAULT_HEIGHT = 600;

    public static final int DEFAULT_NUM_ROWS = 10;

    public static final int DEFAULT_NUM_BALLS = 1000;

    public static final String DEFAULT_IMAGE_FILENAME = "galton_board.png";

    public static final Color BACKGROUND_COLOR = new Color(128, 0, 128);

    public static final Color BAR_COLOR = new Color(0, 128, 0);

    public static final int BAR_BOTTOM_MARGIN = 20;

    private final int numRows;
    private final int numSlots;
    private final int numBalls;
    private final int[] slotCounts;
    private final int width;
    private final int height;
    private final int barWidth;
    private final int maxBarHeight;

    /**
     * @param numRows
     * @param numBalls
     * @param width
     * @param height
     * @throws IllegalArgumentException
     */

    public GaltonBoard(int numRows, int numBalls, int width, int height) {
        validateParameters(numRows, numBalls, width, height);

        this.numRows = numRows;
        this.numSlots = numRows + 1;
        this.numBalls = numBalls;
        this.width = width;
        this.height = height;
        this.slotCounts = new int[numSlots];
        this.barWidth = width / (numSlots + 1);
        this.maxBarHeight = height - (BAR_BOTTOM_MARGIN * 2);
    }

    public GaltonBoard() {
        this(DEFAULT_NUM_ROWS, DEFAULT_NUM_BALLS, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public void simulate() {
        Random random = new Random();
        for (int i = 0; i < numBalls; i++) {
            int slot = 0;
            for (int j = 0; j < numRows; j++) {
                if (random.nextBoolean()) {
                    slot++;
                }
            }
            slotCounts[slot]++;
        }
    }

    /**
     * @param filename
     * @throws IOException
     * @throws IllegalArgumentException
     */

    public void generateImage(String filename) throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty.");
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, width, height);

        int maxCount = 1;
        for (int count : slotCounts) {
            maxCount = Math.max(maxCount, count);
        }

        g2d.setColor(BAR_COLOR);

        for (int i = 0; i < numSlots; i++) {
            int barHeight = (int) (((double) slotCounts[i] / maxCount) * maxBarHeight);
            int x = i * barWidth + barWidth / 2;
            int y = height - barHeight - BAR_BOTTOM_MARGIN;
            g2d.fillRect(x, y, barWidth, barHeight);
        }

        g2d.dispose();
        File outputFile = new File(filename);
        ImageIO.write(image, "png", outputFile);
    }

    /**
     * @param args
     */

    public static void main(String[] args) {
        GaltonBoard board = new GaltonBoard();
        board.simulate();
        try {
            board.generateImage(DEFAULT_IMAGE_FILENAME);
            System.out.println("Image " + DEFAULT_IMAGE_FILENAME + " generated successfully.");
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param numRows
     * @param numBalls
     * @param width
     * @param height
     * @throws IllegalArgumentException
     */

    private void validateParameters(int numRows, int numBalls, int width, int height) {
        if (numRows <= 0) {
            throw new IllegalArgumentException("Number of rows must be positive.");
        }
        if (numBalls <= 0) {
            throw new IllegalArgumentException("Number of balls must be positive.");
        }
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be positive.");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive.");
        }
    }
}
