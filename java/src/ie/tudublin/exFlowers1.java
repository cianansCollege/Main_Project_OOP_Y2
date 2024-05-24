package ie.tudublin;

import ddf.minim.AudioBuffer;
import ddf.minim.AudioInput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class exFlowers1 extends PApplet {

    Minim m;
    AudioInput ai;
    AudioPlayer ap;
    AudioBuffer b;
    

    public void flower(float centreX, float centreY, float diam, float rotation) {
        pushMatrix();
        translate(centreX, centreY);
        rotate(radians(rotation));
        
        stroke(0);
        

        // Diagonal petal
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
    
    int dancingSquaresCount = 0;
    
    public void makeSquare(float box1_x, float box1_y, int box_size, int colour_r, int colour_g, int colour_b){
        fill(colour_r, colour_g, colour_b);

        float box1_x1 = box1_x - box_size, box1_y1 = box1_y - box_size;
        float box1_x2 = box1_x + box_size, box1_y2 = box1_y - box_size;
        float box1_x3 = box1_x - box_size, box1_y3 = box1_y + box_size;
        float box1_x4 = box1_x + box_size, box1_y4 = box1_y + box_size;
        line(box1_x1, box1_y1, box1_x2, box1_y2);
        line(box1_x2, box1_y2, box1_x4, box1_y4);
        line(box1_x4, box1_y4, box1_x3, box1_y3);
        line(box1_x3, box1_y3, box1_x1, box1_y1);
    }

    public void dancingSquares(int squarePositions, float thisX, float thisY){
        int distanceApart = 80;
        if (squarePositions == 0) {
            //sq 1
            makeSquare(thisX, thisY - distanceApart, 30, 0, 0, 0);
            //sq 2
            makeSquare(thisX, thisY + distanceApart, 30, 255, 255, 255);
        }
        else if (squarePositions == 1) {
            //sq 1
            makeSquare(thisX + distanceApart, thisY, 30, 0, 0, 0);
            //sq 2
            makeSquare(thisX - distanceApart, thisY, 30, 255, 255, 255);
        }
        else if (squarePositions == 2) {
            //sq 1
            makeSquare(thisX, thisY + distanceApart, 30, 0, 0, 0);
            //sq 2
            makeSquare(thisX, thisY - distanceApart, 30, 255, 255, 255);
        }
        else if (squarePositions == 3) {
            //sq 1
            makeSquare(thisX - distanceApart, thisY, 30, 0, 0, 0);
            //sq 2
            makeSquare(thisX + distanceApart, thisY, 30, 255, 255, 255);
        }
        else{
            System.out.println("Error with squares");
        }
    }

    @Override
    public void settings() {
        size(1200, 800);
    }

    @Override
    public void setup() {
        m = new Minim(this);
        ap = m.loadFile("data/spotifydown.com - Echo.mp3"); // Ensure the path is correct
        ap.play();
        b = ap.mix;
        frameRate(12);
    }

    @Override
    public void draw() {
        
        colorMode(RGB);
        background(101, 97, 118);
        stroke(255);

        float centreX = width / 2;
        float centreY = height / 2;
        float rotation = map(ap.position(), 0, ap.length(), 0, 360); // Example: map audio position to rotation

        flower(centreX, centreY, 30, rotation);
        for (int i = 50; i < width; i = i + 50) {
            flower(i, 50, 15, rotation);
            flower(i, height - 50, 15, rotation);
        }

        dancingSquares(dancingSquaresCount%4, 400, 266);
        dancingSquaresCount++;

        int sideSize = 300;
        float sideSizeLeftMidX = sideSize / 2;
        float sideSizeRightMidX = width - (sideSize / 2);

        float sum = 0;
        for (int i = 0; i < b.size(); i++) {
            sum += abs(b.get(i));
        }
        float avgAmplitude = sum / b.size();
        float circleSize = map(avgAmplitude, 0, 1, 0, min(width, height));

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
