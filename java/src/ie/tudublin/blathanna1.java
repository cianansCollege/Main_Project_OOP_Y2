package ie.tudublin;

import ddf.minim.AudioBuffer;
import ddf.minim.AudioInput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class blathanna1 extends PApplet {

    Minim minim;
    AudioInput audioInput;
    AudioPlayer audioPlayer;
    AudioBuffer audioBuffer;

    int slowSpeed = 3; // Factor to slow down the square animation
    int dancingSquaresCount = 0;

    // Method to draw a flower
    public void flower(float centreX, float centreY, float diam, float rotation) {
        pushMatrix();
        translate(centreX, centreY);
        rotate(radians(rotation));

        stroke(0);

        // Diagonal petals
        fill(27, 153, 139);
        float diagDiam = diam * 0.5f;
        circle(diagDiam, -diagDiam, diam);
        circle(diagDiam, diagDiam, diam);
        circle(-diagDiam, diagDiam, diam);
        circle(-diagDiam, -diagDiam, diam);

        // Cross petals
        fill(222, 205, 245);
        circle(0, diam, diam);
        circle(0, -diam, diam);
        circle(diam, 0, diam);
        circle(-diam, 0, diam);

        // Center of the flower
        fill(254, 255, 190);
        circle(0, 0, diam);

        popMatrix();
    }

    // Method to draw a spiral of flowers
    public void spiralOfFlowers(float centreX, float centreY, float diam, float rotation) {
        int numFlowers = 10; // Number of flowers in the spiral
        float angleIncrement = 36; // Degrees to increment each flower's angle
        float distanceIncrement = 20; // Distance to increment from the center
        float sizeIncrement = 5; // Size increment for each subsequent flower

        for (int i = 0; i < numFlowers; i++) {
            float angle = radians(i * angleIncrement);
            float distance = i * distanceIncrement;
            float x = centreX + cos(angle) * distance;
            float y = centreY + sin(angle) * distance;
            flower(x, y, diam + i * sizeIncrement, rotation);
        }
    }

    // Method to draw a square
    public void makeSquare(float x, float y, float size, int r, int g, int b) {
        fill(r, g, b);
        rectMode(CENTER);
        rect(x, y, size, size);
    }

    // Method to animate dancing squares
    public void dancingSquares(int squarePositions, float thisX, float thisY, int size, int distanceApart, boolean reverse) {
        stroke(248, 241, 255);
        makeSquare(thisX, thisY - distanceApart, size, 248, 241, 255);
        makeSquare(thisX + distanceApart, thisY, size, 248, 241, 255);
        makeSquare(thisX, thisY + distanceApart, size, 248, 241, 255);
        makeSquare(thisX - distanceApart, thisY, size, 248, 241, 255);

        if (!reverse) {
            // Forward animation of squares
            if (squarePositions == 0) {
                makeSquare(thisX, thisY + distanceApart, size, 27, 153, 139);
            } else if (squarePositions == 1 * slowSpeed) {
                makeSquare(thisX - distanceApart, thisY, size, 27, 153, 139);
            } else if (squarePositions == 2 * slowSpeed) {
                makeSquare(thisX, thisY - distanceApart, size, 27, 153, 139);
            } else if (squarePositions == 3 * slowSpeed) {
                makeSquare(thisX + distanceApart, thisY, size, 27, 153, 139);
            }
        } else {
            // Reverse animation of squares
            if (squarePositions == 3 * slowSpeed) {
                makeSquare(thisX, thisY + distanceApart, size, 254, 255, 190);
            } else if (squarePositions == 2 * slowSpeed) {
                makeSquare(thisX - distanceApart, thisY, size, 254, 255, 190);
            } else if (squarePositions == 1 * slowSpeed) {
                makeSquare(thisX, thisY - distanceApart, size, 254, 255, 190);
            } else if (squarePositions == 0) {
                makeSquare(thisX + distanceApart, thisY, size, 254, 255, 190);
            }
        }
    }

    @Override
    public void settings() {
        size(1200, 800);
    }

    @Override
    public void setup() {
        minim = new Minim(this);
        audioPlayer = minim.loadFile("data/spotifydown.com - Echo.mp3");
        audioPlayer.play();
        audioBuffer = audioPlayer.mix;
        frameRate(12);
    }

    @Override
    public void draw() {
        colorMode(RGB);
        background(248, 241, 255);
        stroke(255);

        float centreX = width / 2;
        float centreY = height / 2;

        int rotationSpeed = 18;
        float rotation = map(audioPlayer.position(), 0, audioPlayer.length(), 0, 360) * rotationSpeed;

        // Draw main flower and additional flowers
        flower(centreX, centreY, 30, rotation);
        spiralOfFlowers(centreX, centreY, 30, rotation);
    }
}
