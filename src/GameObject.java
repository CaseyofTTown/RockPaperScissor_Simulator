import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class GameObject {
    int x, y;
    String type;
    BufferedImage image;

    public GameObject(int x, int y, String type, String imagePath) {
        this.x = x;
        this.y = y;
        this.type = type;
        loadImage(imagePath);
    }

    private void loadImage(String imagePath) {
        try {
            System.out.println("Loading image: " + imagePath);
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath));
            if (image == null) {
                throw new IOException("Image not found: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void update();

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int newWidth = 50;  // Desired width
        int newHeight = 50; // Desired height
        g2d.drawImage(image, x, y, newWidth, newHeight, null);
    }

    public boolean intersects(GameObject other) {
        return this.getBounds().intersects(other.getBounds());
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 50, 50); // Width and height
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }
}
