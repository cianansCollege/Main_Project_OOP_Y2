package ie.tudublin;

import ddf.minim.AudioBuffer;
import ddf.minim.AudioInput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class Display extends PApplet {

    Minim m;
    AudioInput ai;
    AudioPlayer ap;
    AudioBuffer b;

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
    }

    float lerpedAvg = 0;
    int countStickman = 1;

    @Override
    public void draw() {
        colorMode(HSB);
        background(0);
        stroke(255);
        
        float centreX = width / 2;
        float centreY = height / 2;

        int sideSize = 300;
        float sideSizeLeftMidX = sideSize /2;
        float sideSizeRightMidX = width - (sideSize /2);

        int middleSize = width - sideSize * 2;

        float blueBoxHeight = height/3;

        // for(int i = 0 ; i < b.size() ; i ++)
        // {
        //     float hue = map(i, 0, b.size() , 0, 256);
        //     stroke(hue, 255, 255);
        //     noFill();
        // }

        

        float sum = 0;
        for(int i = 0; i < b.size(); i++) {
            sum += abs(b.get(i));
        }
        float avgAmplitude = sum / b.size();
        System.out.println(avgAmplitude + '\n');

        // if(avgAmplitude > 0.01 && avgAmplitude < 0.015){
        //     System.out.println("true");
        //     stroke(255);
        //     circle(centreX, centreY, 100);
        // }
        
        float circleSize = map(avgAmplitude, 0, 1, 0, min(width, height));

        for (int i = 0; i < 10; i++) {
            fill(0);
            stroke(255);
            circle(sideSizeLeftMidX, centreY, circleSize-10*i);
            circle(sideSizeRightMidX, centreY, circleSize-10*i);

            circle(sideSizeLeftMidX, centreY + (centreY/2), circleSize-10*i);
            circle(sideSizeRightMidX, centreY + (centreY/2), circleSize-10*i);

            circle(sideSizeLeftMidX, centreY - (centreY/2), circleSize-10*i);
            circle(sideSizeRightMidX, centreY - (centreY/2), circleSize-10*i);
        }

        //stickman time
        //stickman dance moves : 0 = Start pose, 1 = flex left, 2 = flex both, 3 = reset
        //arm length is half an arm ie. from shoulder to elbow and from elbow to hand
        float armLength = middleSize/6;
        float headRadius = height/14;
        float bodyLength = armLength*2;
        float legLength = bodyLength;
        float bodyStart = blueBoxHeight+(2*headRadius);
        float bodyEnd = bodyStart + bodyLength;

        //head
        circle(centreX ,blueBoxHeight+headRadius , headRadius*2);
        //body
        line(centreX, bodyStart, centreX, bodyEnd);
        //upper arm
        line(centreX, bodyStart, centreX + armLength, bodyStart);
        line(centreX, bodyStart, centreX - armLength, bodyStart);
        //legs
        line(centreX, bodyEnd, centreX + armLength, bodyEnd + legLength);
        line(centreX, bodyEnd, centreX - armLength, bodyEnd + legLength);

         

        //lower arm
        if(countStickman == 0){
            line(centreX + armLength, bodyStart, centreX - armLength*2, bodyStart);
            line(centreX + armLength, bodyStart, centreX + armLength*2, bodyStart);
        }
        if(countStickman == 1){
            line(centreX - armLength, bodyStart, centreX - armLength, bodyStart - armLength);
            line(centreX + armLength, bodyStart, centreX + armLength*2, bodyStart);
        }
        if(countStickman == 2){
            line(centreX - armLength, bodyStart, centreX - armLength, bodyStart - armLength);
            line(centreX + armLength, bodyStart, centreX + armLength, bodyStart - armLength);
            countStickman = 0;
        }
        
        if(avgAmplitude > .001){
                    countStickman = countStickman + 1;
                    System.out.println("true");
                }
    }

    float lerped = 0;
}
