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

        int numCircles = 20; // Number of circles
        circleX = new float[numCircles];
        circleY = new float[numCircles];
        speedX = new float[numCircles];
        speedY = new float[numCircles];

        // Initialize circle positions and speeds
        for (int i = 0; i < numCircles; i++) {
            circleX[i] = random(width);
            circleY[i] = random(height);
            speedX[i] = random(-1, 1);
            speedY[i] = random(-1, 1);
        }
    }

    @Override
    public void draw() {
        colorMode(RGB);
        background(20, 80, 200);
        stroke(0);

        for (int i = 0; i < circleX.length; i++) {
            // Update circle position
            circleX[i] += speedX[i];
            circleY[i] += speedY[i];

            // Check boundaries and bounce if necessary
            if (circleX[i] < 0 || circleX[i] > width) {
                speedX[i] *= -1;
            }
            if (circleY[i] < 0 || circleY[i] > height) {
                speedY[i] *= -1;
            }

            // Draw circle
            fill(0, 40, 90);
            circle(circleX[i], circleY[i], 30);
        }
    }
}
