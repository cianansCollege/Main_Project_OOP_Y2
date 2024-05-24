package ie.tudublin;

import ddf.minim.AudioBuffer;
import ddf.minim.AudioInput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class flowers extends PApplet {

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

    // Method to draw a square
    public void makeSquare(float x, float y, float size, int r, int g, int b) {
        fill(r, g, b);
        rectMode(CENTER);
        rect(x, y, size, size);
    }

    // Method to animate dancing squares
    public void dancingSquares(int squarePositions, float thisX, float thisY, int size, int distanceApart, boolean reverse) {
        makeSquare(thisX, thisY - distanceApart, size, 101, 97, 118);
        makeSquare(thisX + distanceApart, thisY, size, 101, 97, 118);
        makeSquare(thisX, thisY + distanceApart, size, 101, 97, 118);
        makeSquare(thisX - distanceApart, thisY, size, 101, 97, 118);

        if (!reverse) {
            // Forward animation of squares
            if (squarePositions == 0) {
                makeSquare(thisX, thisY + distanceApart, size, 254, 255, 190);
            } else if (squarePositions == 1 * slowSpeed) {
                makeSquare(thisX - distanceApart, thisY, size, 254, 255, 190);
            } else if (squarePositions == 2 * slowSpeed) {
                makeSquare(thisX, thisY - distanceApart, size, 254, 255, 190);
            } else if (squarePositions == 3 * slowSpeed) {
                makeSquare(thisX + distanceApart, thisY, size, 254, 255, 190);
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
        System.err.println(audioBuffer);
        colorMode(RGB);
        background(248, 241, 255);
        stroke(255);

        float centreX = width / 2;
        float centreY = height / 2;
        int rotationSpeed = 18;
        float rotation = map(audioPlayer.position(), 0, audioPlayer.length(), 0, 360) * rotationSpeed;

        // Draw main flower and additional flowers
        flower(centreX, centreY, 30, rotation);
        for (int i = 50; i < width; i += 50) {
            flower(i, 50, 15, rotation);
            flower(i, height - 50, 15, rotation);
        }

        // Animate dancing squares
        dancingSquares(dancingSquaresCount % (4 * slowSpeed), 400, 266, 40, 80, false);
        dancingSquares(dancingSquaresCount % (4 * slowSpeed), width - 400, height - 266, 40, 80, false);
        dancingSquares(dancingSquaresCount % (4 * slowSpeed), 400, 266, 10, 20, false); // Reverse direction
        dancingSquares(dancingSquaresCount % (4 * slowSpeed), width - 400, height - 266, 10, 20, false); // Reverse direction
        dancingSquaresCount++;

        int sideSize = 300;
        float sideSizeLeftMidX = sideSize / 2;
        float sideSizeRightMidX = width - (sideSize / 2);

        float sum = 0;
        for (int i = 0; i < audioBuffer.size(); i++) {
            sum += abs(audioBuffer.get(i));
        }
        float avgAmplitude = sum / audioBuffer.size();
        float circleSize = map(avgAmplitude, 0, 1, 0, min(width, height));

        // Draw circles based on audio amplitude
        for (int i = 0; i < 10; i++) {
            fill(0);
            stroke(255);
            circle(sideSizeLeftMidX, centreY, circleSize - 10 * i);
            circle(sideSizeRightMidX, centreY, circleSize - 10 * i);
            circle(sideSizeLeftMidX, centreY + (centreY / 2), circleSize - 10 * i);
            circle(sideSizeRightMidX, centreY + (centreY / 2), circleSize - 10 * i);
            circle(sideSizeLeftMidX, centreY - (centreY / 2), circleSize - 10 * i);
            circle(sideSizeRightMidX, centreY - (centreY / 2), circleSize - 10 * i);
        }
    }
}
