package ie.tudublin;

import ddf.minim.AudioBuffer;
import ddf.minim.AudioInput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class Sound1 extends PApplet {

    Minim m;
    AudioInput ai;
    AudioPlayer ap;
    AudioBuffer b;

    int maxCircles = 40; // Maximum number of circles
    int numCircles = 20;    // Initial number of circles
    float[] circleX;
    float[] circleY;
    float[] speedX;
    float[] speedY;

    @Override
    public void settings() {
        size(1200, 800);
    }

    @Override
    public void setup() {
        m = new Minim(this);
        ap = m.loadFile("BodyGroove_MixMcVersion.mp3");
        ap.play();
        b = ap.mix;

        circleX = new float[maxCircles];
        circleY = new float[maxCircles];
        speedX = new float[maxCircles];
        speedY = new float[maxCircles];

        // Initialize circle positions and speeds
        for (int i = 0; i < numCircles; i++) {
            circleX[i] = random(width);
            circleY[i] = random(-height, 0); // Randomize starting position above the screen
            speedX[i] = random(-0.5f, 0.5f);      // Adjusted speed
            speedY[i] = random(-0.5f, -0.2f);  // Adjusted speed and adjusted for upward motion
        }
    }

    @Override
    public void draw() {
        // Background color to mimic underwater environment
        background(10, 70, 200); // Dark blue

        // Draw and update existing bubbles
        for (int i = 0; i < numCircles; i++) {
            // Update circle position
            circleX[i] += speedX[i];
            circleY[i] += speedY[i];

            // Check boundaries and reset if out of screen
            if (circleY[i] < -60) {
                circleX[i] = random(width);
                circleY[i] = height;
            }

            // Draw bubble-like circles with semi-transparency
            noStroke();
            fill(150, 200, 255, 150); // Light blue with transparency
            ellipse(circleX[i], circleY[i], 60, 60); // Larger circular shape
        }

        // Add new bubbles if the number is less than the maximum
        if (numCircles < maxCircles) {
            circleX[numCircles] = random(width);
            circleY[numCircles] = random(-height, 0); // Randomize starting position above the screen
            speedX[numCircles] = random(-0.5f, 0.5f);       // Adjusted speed
            speedY[numCircles] = random(-0.5f, -0.2f);   // Adjusted speed and adjusted for upward motion
            numCircles++; // Increment the count of circles
        }
    }

    @Override
    public void mousePressed() {
        // Iterate through bubbles to check for clicks
        for (int i = 0; i < numCircles; i++) {
            // Calculate distance between mouse click and bubble center
            float distance = dist(mouseX, mouseY, circleX[i], circleY[i]);
            
            // If click is within bubble radius, remove bubble
            if (distance < 30) {
                // Remove bubble from arrays
                removeBubble(i);
                break; // Exit loop once a bubble is popped
            }
        }
    }

    // Method to remove popped bubble from arrays
    void removeBubble(int index) {
        // Shift bubbles after removed bubble to fill the gap
        for (int i = index; i < numCircles - 1; i++) {
            circleX[i] = circleX[i + 1];
            circleY[i] = circleY[i + 1];
            speedX[i] = speedX[i + 1];
            speedY[i] = speedY[i + 1];
        }
        
        // Decrement number of bubbles
        numCircles--;
    }

    public static void main(String[] args) {
        PApplet.main("ie.tudublin.Sound1");
    }
}
