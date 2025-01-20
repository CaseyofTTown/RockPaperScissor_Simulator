import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;

public class Simulation extends Canvas implements Runnable {
    private List<GameObject> objects;
    private boolean running;
    private Thread thread;

    public Simulation() {
        objects = new ArrayList<>();
        Random rand = new Random();

        int numObjects = 15; // Example number of each type
        for (int i = 0; i < numObjects; i++) {
            objects.add(new Rock(rand.nextInt(750), rand.nextInt(550))); // Adjust for new size
            objects.add(new Paper(rand.nextInt(750), rand.nextInt(550)));
            objects.add(new Scissors(rand.nextInt(750), rand.nextInt(550)));
        }
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            update();
            render();
            try {
                Thread.sleep(32); // Roughly 60 updates per second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        for (GameObject obj : objects) {
            obj.update();
        }

        // Collision handling
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                GameObject obj1 = objects.get(i);
                GameObject obj2 = objects.get(j);

                if (obj1.intersects(obj2)) {
                    handleCollision(obj1, obj2);
                }
            }
        }
    }

    private void handleCollision(GameObject obj1, GameObject obj2) {
        if (obj1.type.equals("Rock") && obj2.type.equals("Scissors")) {
            objects.set(objects.indexOf(obj2), new Rock(obj2.x, obj2.y));
        } else if (obj1.type.equals("Scissors") && obj2.type.equals("Paper")) {
            objects.set(objects.indexOf(obj2), new Scissors(obj2.x, obj2.y));
        } else if (obj1.type.equals("Paper") && obj2.type.equals("Rock")) {
            objects.set(objects.indexOf(obj2), new Paper(obj2.x, obj2.y));
        } else if (obj2.type.equals("Rock") && obj1.type.equals("Scissors")) {
            objects.set(objects.indexOf(obj1), new Rock(obj1.x, obj1.y));
        } else if (obj2.type.equals("Scissors") && obj1.type.equals("Paper")) {
            objects.set(objects.indexOf(obj1), new Scissors(obj1.x, obj1.y));
        } else if (obj2.type.equals("Paper") && obj1.type.equals("Rock")) {
            objects.set(objects.indexOf(obj1), new Paper(obj1.x, obj1.y));
        }
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());

        for (GameObject obj : objects) {
            obj.render(g);
        }

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Rock Paper Scissors Simulation");
       
        Simulation simulation = new Simulation();
        frame.add(simulation);
        frame.setSize(800, 600); // Set window size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        simulation.start();
    }
}
