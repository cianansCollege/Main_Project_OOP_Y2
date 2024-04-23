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

    int maxCircles = 1000; // Maximum number of circles
    int numCircles = 50;    // Initial number of circles
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
            speedX[i] = random(-1, 1);
            speedY[i] = random(-1, -0.5f);  // Adjusted for upward motion
        }
    }

    @Override
    public void draw() {
        colorMode(RGB);
        background(20, 80, 200);
        stroke(0);

        // Draw and update existing circles
        for (int i = 0; i < numCircles; i++) {
            // Update circle position
            circleX[i] += speedX[i];
            circleY[i] += speedY[i];

            // Check boundaries and reset if out of screen
            if (circleY[i] < -30) {
                circleX[i] = random(width);
                circleY[i] = height;
            }

            // Draw bubble
            fill(0, 40, 90, 100); // Adjusted for transparency
            ellipse(circleX[i], circleY[i], 30, 30); // Circular shape
        }

        // Add new bubbles if the number is less than the maximum
        if (numCircles < maxCircles) {
            circleX[numCircles] = random(width);
            circleY[numCircles] = random(-height, 0); // Randomize starting position above the screen
            speedX[numCircles] = random(-1, 1);
            speedY[numCircles] = random(-1, -0.5f);  // Adjusted for upward motion
            numCircles++; // Increment the count of circles
        }
    }
}
