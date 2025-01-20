import java.awt.Graphics;
import java.util.Random;

public class Scissors extends GameObject {
	private int dx, dy;
	private Random random;

	public Scissors(int x, int y) {
		super(x, y, "Scissors", "scissors.png");
		this.random = new Random();
		this.dx = (Math.random() > 0.5) ? 2 : -2; // Random initial movement direction
		this.dy = (Math.random() > 0.5) ? 2 : -2;
	}

	@Override
	public void update() {
		if (random.nextInt(100) < 5) {
			dx = random.nextInt(5) - 2;
			dy = random.nextInt(5) - 2;
		}
		// Update position
		x += dx;
		y += dy;

		// Check boundaries and bounce if necessary
		if (x < 0 || x > 780)
			dx = -dx; // Assuming window width is 800
		if (y < 0 || y > 580)
			dy = -dy; // Assuming window height is 600

		// Handle collisions with other GameObjects (to be implemented in Simulation
		// class)
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(image, x, y, null);
	}
}
