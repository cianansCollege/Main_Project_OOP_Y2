package ie.tudublin;

import ddf.minim.AudioBuffer;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class Sound1 extends PApplet {

    Minim m;
    AudioPlayer ap;
    AudioBuffer b;

    int maxCircles = 100; // Maximum number of circles
    float[] circleX;
    float[] circleY;
    float[] speedX;
    float[] speedY;
    boolean[] bubbleActive; // Track if bubbles are active
    int bubbleColor = color(20, 52, 255); // Default bubble color

    Particle[] particles = new Particle[1000];
    int numParticles = 0;

    int numDrops = 100; // Number of raindrops
    float[] dropX, dropY, dropSpeed; // Arrays to store raindrop properties
    float dropGap; // Gap between raindrops
    int[] dropColors; // Array to store raindrop colors

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
        bubbleActive = new boolean[maxCircles];

        dropX = new float[numDrops];
        dropY = new float[numDrops];
        dropSpeed = new float[numDrops];
        dropGap = width / (float) numDrops; // Calculate gap between raindrops
        dropColors = new int[numDrops]; // Array to store raindrop colors

        for (int i = 0; i < maxCircles; i++) {
            bubbleActive[i] = true; // All bubbles start as active
            circleX[i] = random(width);
            circleY[i] = random(height);
            speedX[i] = random(-2, 2);
            speedY[i] = random(-2, 2);
        }

        // Initialize raindrop properties
        for (int i = 0; i < numDrops; i++) {
            dropX[i] = i * dropGap; // Fixed horizontal position
            dropY[i] = random(-height, 0); // Start above the screen
            dropSpeed[i] = 20; // Constant speed for all raindrops
            // Random vibrant color
            int dropColor = color(random(100, 255), random(100, 255), random(100, 255));
            dropColors[i] = dropColor;
        }
    }

    @Override
    public void draw() {
        background(0, 0, 0); // black background
    
        // Draw and update particles
        for (int i = 0; i < numParticles; i++) {
            particles[i].update();
            particles[i].draw();
            
            // Check for collision with bubbles
            for (int j = 0; j < maxCircles; j++) {
                if (bubbleActive[j]) {
                    float distance = dist(particles[i].x, particles[i].y, circleX[j], circleY[j]);
                    if (distance < (min(width, height) / 30)) { // Adjust the radius for particle-bubble collision
                        bubbleActive[j] = false; // Bubble is popped
                        explode(circleX[j], circleY[j]); // Explode at bubble position
                    }
                }
            }
        }
    
        // Simulate rainfall
        for (int i = 0; i < numDrops; i++) {
            dropY[i] += dropSpeed[i]; // Move raindrop downwards
    
            // Reset raindrop if it goes below the screen
            if (dropY[i] > height) {
                dropY[i] = random(-height, 0); // Reset to top
            }
    
            // Draw raindrop with random color
            stroke(dropColors[i]);
            line(dropX[i], dropY[i], dropX[i], dropY[i] + 30); // Vertical line representing raindrop
        }
    
        // Draw bubbles
        for (int i = 0; i < maxCircles; i++) {
            if (bubbleActive[i]) {
                // Bounce off the edges
                if (circleX[i] < 0 || circleX[i] > width) {
                    speedX[i] *= -1; // Reverse horizontal direction
                }
                if (circleY[i] < 0 || circleY[i] > height) {
                    speedY[i] *= -1; // Reverse vertical direction
                }
    
                // randomise bubble speed
                circleX[i] += speedX[i];
                circleY[i] += speedY[i];
    
                // increase size based on the amplitude
                float avgAmplitude = calculateAverageAmplitude();
                float circleSize = (map(avgAmplitude, 0, 1, 0, min(width, height) / 10)) * 10;
    
                // Draw white border around the bubble
                stroke(255); // White color
                strokeWeight(1); // Thickness of the border
                noFill();
                ellipse(circleX[i], circleY[i], circleSize + 6, circleSize + 6);
    
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
    
        // Display "THE END" text in the middle of the screen
        if (areAllBubblesPopped() && !ap.isPlaying()) {
            textSize(64);
            textAlign(CENTER, CENTER);
            fill(255); // White color
            text("THE END", width / 2, height / 2);
        }
    }
    
    


    @Override
    public void keyPressed() {
        switch (key) {
            case '1':
                bubbleColor = color(20, 52, 255); // Blue
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

        // Update particle colors when bubble color changes
        for (int i = 0; i < numParticles; i++) {
            particles[i].setColor(bubbleColor);
        }
    }

    @Override
    public void mousePressed() {
        for (int i = 0; i < maxCircles; i++) {
            if (bubbleActive[i]) {
                float distance = dist(mouseX, mouseY, circleX[i], circleY[i]);
                if (distance < (min(width, height) / 30)) { // Adjust the radius for popping
                    bubbleActive[i] = false; // Bubble is popped
                    explode(circleX[i], circleY[i]); // Explode at bubble position
                }
            }
        }
    }

    // Create explosion particles at position
    void explode(float x, float y) {
        for (int i = 0; i < 10; i++) {
            particles[numParticles++] = new Particle(x, y, bubbleColor);
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

    // Particle class for explosion effect
    class Particle {
        float x, y;  // Position
        float vx, vy;  // Velocity
        float size;  // Size
        int color;  // Color

        Particle(float x, float y, int bubbleColor) {
            this.x = x;
            this.y = y;
            this.vx = random(-2, 2);
            this.vy = random(-2, 2);
            this.size = random(5, 15);
            this.color = bubbleColor; // Set particle color to bubble color
        }

        void update() {
            x += vx;
            y += vy;
            vy += 0.1;
        }

        void draw() {
            noStroke();
            fill(color);
            ellipse(x, y, size, size);
        }

        void setColor(int bubbleColor) {
            this.color = bubbleColor; // Update particle color
        }
    }

    public static void main(String[] args) {
        PApplet.main("ie.tudublin.Sound1");
    }
}
