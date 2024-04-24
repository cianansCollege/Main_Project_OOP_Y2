package ie.tudublin;

import ddf.minim.AudioBuffer;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class Sound1 extends PApplet {

    Minim m;
    AudioPlayer ap;
    AudioBuffer b;

    int maxCircles = 36; // Maximum number of circles
    float[] circleX;
    float[] circleY;
    float[] speedX;
    float[] speedY;
    boolean[] bubbleActive; // Track if bubbles are active
    int bubbleColor = color(150, 200, 255, 150); // Default bubble color

    @Override
    public void settings() {
        size(1200, 800);
    }

    @Override
    public void setup() {
        m = new Minim(this);
        ap = m.loadFile("BodyGroove_MixMcVersion.mp3", 1024);
        ap.play();
        b = ap.mix;

        circleX = new float[maxCircles];
        circleY = new float[maxCircles];
        speedX = new float[maxCircles];
        speedY = new float[maxCircles];
        bubbleActive = new boolean[maxCircles]; // Initialize the active bubble array

        for (int i = 0; i < maxCircles; i++) {
            bubbleActive[i] = true; // All bubbles start as active
            circleX[i] = random(width);
            circleY[i] = random(height);
            speedX[i] = random(-2, 2);
            speedY[i] = random(-2, 2);
        }
    }

    @Override
    public void draw() {
        background(0, 0, 0);

        for (int i = 0; i < maxCircles; i++) {
            if (bubbleActive[i]) {
                // Bounce off the edges
                if (circleX[i] < 0 || circleX[i] > width) {
                    speedX[i] *= -1; // Reverse horizontal direction
                }
                if (circleY[i] < 0 || circleY[i] > height) {
                    speedY[i] *= -1; // Reverse vertical direction
                }

                // Update positions
                circleX[i] += speedX[i];
                circleY[i] += speedY[i];

                // Calculate size based on the average amplitude
                float avgAmplitude = calculateAverageAmplitude();
                float circleSize = (map(avgAmplitude, 0, 1, 0, min(width, height) / 10))*4;

                // Draw bubbles with selected color
                noStroke();
                fill(bubbleColor);
                ellipse(circleX[i], circleY[i], circleSize, circleSize);
            }
        }

        // Check if all bubbles are popped and if so stop the music
        if (areAllBubblesPopped() && ap.isPlaying()) {
            ap.pause(); // Stop the music
        }
    }

    @Override
public void keyPressed() {
    switch (key) {
        case '1':
            bubbleColor = color(150, 200, 255, 150); // Blue
            break;
        case '2':
            bubbleColor = color(255, 0, 0, 150); // Red
            break;
        case '3':
            bubbleColor = color(255, 255, 0, 150); // Yellow
            break;
        case '4':
            bubbleColor = color(0, 255, 0, 150); // Green
            break;
        
    }
}

    //Calculate average amplitude
    private float calculateAverageAmplitude() {
        float sum = 0;
        for (int i = 0; i < b.size(); i++) {
            sum += abs(b.get(i));
        }
        return sum / b.size();
    }

    //Check if all bubbles are popped
    private boolean areAllBubblesPopped() {
        for (boolean active : bubbleActive) {
            if (active) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        PApplet.main("ie.tudublin.Sound1");
    }
}
