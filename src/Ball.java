import de.looksgood.ani.Ani;
import processing.core.*;

import java.util.Random;


/**
 * Created by jerry on 2017/4/26.
 */
public class Ball {
    Main parent;
    PVector position;
    PVector velocity;
    PImage img;
    int imgWidth;
    int imgHeight;
    boolean imgShow=false;
    float radius, m;
    public Ball(Main parent,float x, float y, float r_,PImage image) {
        this.parent = parent;
        position = new PVector(x, y);
        velocity = PVector.random2D();
        radius = r_;
        m = radius*0.1f;
        img = image;
        imgHeight=95;
        imgWidth = 95;
    }

    public boolean checkMouseClicked()
    {
        //TODO check if mouse clicked inside the ball, use some high school math, PVector may be useful
        PVector mousePosition = new PVector(parent.mouseX,parent.mouseY);
        PVector distance = PVector.sub(mousePosition,position);
        float distanceMag = distance.mag();
        if(distanceMag < radius)
        {

           // Random random = new Random();
           //Ani.to(this,(float)1.0,"radius",(float)(random.nextInt(30)+10));
            //Ani.to(this,(float)1.0,"r",random.nextInt(256));
           // Ani.to(this,(float)1.0,"g",random.nextInt(256));
            //Ani.to(this,(float)1.0,"b",random.nextInt(256));
            if(parent.gameStart==true)
                imgShow = true;
            return true;

        }
        //TODO use Ani to animate random color and radius if clicked

        //TODO remember to return
        return false;
    }


    public void update() {
        position.add(velocity);
    }

    public void checkBoundaryCollision() {
        if (position.x > parent.width-radius) {
            position.x = parent.width-radius;
            velocity.x *= -1;
        } else if (position.x < radius) {
            position.x = radius;
            velocity.x *= -1;
        } else if (position.y > parent.height-radius) {
            position.y = parent.height-radius;
            velocity.y *= -1;
        } else if (position.y < radius) {
            position.y = radius;
            velocity.y *= -1;
        }
    }

    public void checkCollision(Ball other) {

        // Get distances between the balls components
        PVector distanceVect = PVector.sub(other.position, position);

        // Calculate magnitude of the vector separating the balls
        float distanceVectMag = distanceVect.mag();

        // Minimum distance before they are touching
        float minDistance = radius + other.radius;

        if (distanceVectMag < minDistance) {
            float distanceCorrection = (minDistance-distanceVectMag)/2.0f;
            PVector d = distanceVect.copy();
            PVector correctionVector = d.normalize().mult(distanceCorrection);
            other.position.add(correctionVector);
            position.sub(correctionVector);

            // get angle of distanceVect
            float theta  = distanceVect.heading();
            // precalculate trig values

            float sine = parent.sin(theta);
            float cosine = parent.cos(theta);

      /* bTemp will hold rotated ball positions. You
       just need to worry about bTemp[1] position*/
            PVector[] bTemp = {
                    new PVector(), new PVector()
            };

      /* this ball's position is relative to the other
       so you can use the vector between them (bVect) as the
       reference point in the rotation expressions.
       bTemp[0].position.x and bTemp[0].position.y will initialize
       automatically to 0.0, which is what you want
       since b[1] will rotate around b[0] */
            bTemp[1].x  = cosine * distanceVect.x + sine * distanceVect.y;
            bTemp[1].y  = cosine * distanceVect.y - sine * distanceVect.x;

            // rotate Temporary velocities
            PVector[] vTemp = {
                    new PVector(), new PVector()
            };

            vTemp[0].x  = cosine * velocity.x + sine * velocity.y;
            vTemp[0].y  = cosine * velocity.y - sine * velocity.x;
            vTemp[1].x  = cosine * other.velocity.x + sine * other.velocity.y;
            vTemp[1].y  = cosine * other.velocity.y - sine * other.velocity.x;

      /* Now that velocities are rotated, you can use 1D
       conservation of momentum equations to calculate
       the final velocity along the x-axis. */
            PVector[] vFinal = {
                    new PVector(), new PVector()
            };

            // final rotated velocity for b[0]
            vFinal[0].x = ((m - other.m) * vTemp[0].x + 2 * other.m * vTemp[1].x) / (m + other.m);
            vFinal[0].y = vTemp[0].y;

            // final rotated velocity for b[0]
            vFinal[1].x = ((other.m - m) * vTemp[1].x + 2 * m * vTemp[0].x) / (m + other.m);
            vFinal[1].y = vTemp[1].y;

            // hack to avoid clumping
            bTemp[0].x += vFinal[0].x;
            bTemp[1].x += vFinal[1].x;

      /* Rotate ball positions and velocities back
       Reverse signs in trig expressions to rotate
       in the opposite direction */
            // rotate balls
            PVector[] bFinal = {
                    new PVector(), new PVector()
            };

            bFinal[0].x = cosine * bTemp[0].x - sine * bTemp[0].y;
            bFinal[0].y = cosine * bTemp[0].y + sine * bTemp[0].x;
            bFinal[1].x = cosine * bTemp[1].x - sine * bTemp[1].y;
            bFinal[1].y = cosine * bTemp[1].y + sine * bTemp[1].x;

            // update balls to screen position
            other.position.x = position.x + bFinal[1].x;
            other.position.y = position.y + bFinal[1].y;

            position.add(bFinal[0]);

            // update velocities
            velocity.x = cosine * vFinal[0].x - sine * vFinal[0].y;
            velocity.y = cosine * vFinal[0].y + sine * vFinal[0].x;
            other.velocity.x = cosine * vFinal[1].x - sine * vFinal[1].y;
            other.velocity.y = cosine * vFinal[1].y + sine * vFinal[1].x;
        }
    }
    int imgSize = 45;
    int r = 0,g=0,b=0;
    public void display() {
        parent.noStroke();
        parent.fill(r,g,b);
        parent.ellipse(position.x, position.y, radius*2, radius*2);

    }
    public  PImage getImg()
    {
        return img;
    }
    public PVector getCirclePosititon(){
        return position;
    }
}


