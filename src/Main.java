import ddf.minim.Minim;
import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main extends PApplet {

    public static void main(String[] args) {
        PApplet.main("Main", args);
    }
    int num;
    public ArrayList<Ball> balls = new ArrayList<>();
    PImage img;
    PImage imgch;//chanyeol
    PImage imgpk;//pikachu
    PImage imghc;//haochuan
    PImage imgjy;//junyao
    PImage imgsh;//shanghong;
    int score=0;
    int gameStartTime;
    int timeCountdown=30;
    int matchPair=0;//count the pair matched
    boolean gameStart=false;
    int countStart;
    public ArrayList<Ball> compareBalls = new ArrayList<>();
    public ArrayList<PImage> imgList=new ArrayList<>();
    @Override
    public void setup() {
        img = loadImage("data/pokeball.png");
        imgch = loadImage("data/chanyeol.png");
        imgpk = loadImage("data/pika.png");
        imghc = loadImage("data/haochuan.png");
        imgjy = loadImage("data/junyao.png");
        imgsh = loadImage("data/hong.png");
        imgList.add(imgch);
        imgList.add(imgpk);
        imgList.add(imghc);
        imgList.add(imgjy);
        imgList.add(imgsh);
        imgList.add(imgch);
        imgList.add(imgpk);
        imgList.add(imghc);
        imgList.add(imgjy);
        imgList.add(imgsh);
        Collections.shuffle(imgList);//random the order of imgList
        Ani.init(this);
        num = 10;
        //calculate the position of the balls
        //you can assign by yourself
        for (int i = 0; i < num; i++) {
            int x = (i % 5 * 120) + 60;
            int y = i / 5 * 200 + 100;
            println("" + x + "," + y);
            int radius = 50;
                Ball b = new Ball(this, x, y, radius, imgList.get(i));//get the img from the img List
                balls.add(b);
        }

    }

    @Override
    public void draw() {
        background(51);
        textSize(30);
        fill(255,152,0);
        text("Score:",10,40);
        text("Timer:",550,40);
        text(score,100,40);
        if(gameStart) {
            timeCountdown=(int)(30-(millis()-gameStartTime)/1000);//start count down from 30
            text(timeCountdown,650,40);

        }
        else
            text(timeCountdown,650,40);
        if(matchPair==5 && (millis()-countStart)>1000) {//game end
                gameStart = false;
                text("YOU WIN!!!",280,240);
        }
        else if(timeCountdown==0)
        {
            gameStart=false;
            text("Time's OUT",270,200);
            text("GAME OVER...",260,240);
        }
        else
        {
            for (Ball b : balls) {
                //physics calculation
                if(gameStart)
                {
                    b.update();
                    b.checkBoundaryCollision();
                }

                //draw ball
                b.display();
                imageMode(CENTER);
                if(b.imgShow==false)
                    image(img,b.position.x,b.position.y,95,95);
                else
                    image(b.getImg(),b.position.x,b.position.y,b.imgWidth,b.imgHeight);
            }
            compare();
            int i, j;
            for (i = 0, j = 0; i < balls.size(); i++) {
                for (j = i + 1; j < balls.size(); j++) {
                    balls.get(i).checkCollision(balls.get(j));
                }
            }        }

    }
    public void compare()
    {
        if(compareBalls.size()==2)
        {
            if(compareBalls.get(0).getImg()==compareBalls.get(1).getImg()) {
                Ani.to(compareBalls.get(0), (float)1.5, "imgHeight", 0);//animation of picture disappear
                Ani.to(compareBalls.get(0), (float)1.5, "imgWidth", 0);
                Ani.to(compareBalls.get(1), (float)1.5,"imgHeight",0);
                Ani.to(compareBalls.get(1), (float)1.5,"imgWidth",0);
                Ani.to(compareBalls.get(0), 2, "radius", 0);
                Ani.to(compareBalls.get(1), 2,"radius",0);
                if(compareBalls.get(0).getImg()==imghc)// if the picture is haochuan you can get 50 points
                    score=score+50;
                score=score+5;
                matchPair++;
                compareBalls.clear();//clean the compare array
            }
            else
            {
                if((millis()-countStart)>300) {//show the img for 0.3 second
                    compareBalls.get(0).imgShow = false;
                    compareBalls.get(1).imgShow = false;
                    score=score-5;
                    compareBalls.clear();//clean the compare array
                }
            }

        }
    }
    @Override

    public void mousePressed() {
        println("mouse pressed");
        //TODO iterate through the balls and check if the mouse is inside the ball
        for(Ball b : balls)
        {
            if(b.checkMouseClicked())
            {
                if(gameStart==true)//only if game start you can play the game
                    compareBalls.add(b);
                if(compareBalls.size()==2)//start the count time of the picture show
                    countStart=millis();
                System.out.println("clicked success");
            }
        }
    }
    @Override
    public void keyPressed()
    {
        if(key=='s')//press s to start
        {
            gameStart=true;
            gameStartTime=millis();
        }
    }
    @Override
    public void settings() {
        super.settings();
        size(720, 480);
    }
}

