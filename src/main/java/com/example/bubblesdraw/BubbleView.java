package com.example.bubblesdraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;


public class BubbleView extends ImageView implements View.OnTouchListener {

    MediaPlayer bubbleSound = MediaPlayer.create(getContext(), R.raw.bubblesound);

    private Random rand = new Random();
    private ArrayList<Bubble> bubbleList;
    private int size = 50;

    private int delay = 25;
    private Paint paint = new Paint();
    private Handler handler = new Handler();

    public BubbleView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        bubbleList = new ArrayList<Bubble>();
        setOnTouchListener(this);

    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            for(Bubble bubbles: bubbleList)
                bubbles.update();
            invalidate();
        }
    };

    protected void onDraw(Canvas canvas){
        for(Bubble bubbles: bubbleList)
            bubbles.draw(canvas);
        handler.postDelayed(run, delay);

    }

    public void testBubbles(){
        for(int i = 0; i < 1; i++){
            int x = rand.nextInt(1000);
            int y = rand.nextInt(1000);
            int size = rand.nextInt(100);
            bubbleList.add(new Bubble(x, y, size));
        }
        invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        for (int n = 0; n< motionEvent.getPointerCount(); n++) {
            int x = (int) motionEvent.getX(n);
            int y = (int) motionEvent.getY(n);
            int Size = rand.nextInt(size) + size;
            bubbleList.add(new Bubble(x, y, Size));
            bubbleSound.start();
        }
        return true;
    }

    private class Bubble{
        private int x;
        private int y;
        private int size;
        private int color;
        private int xSpeed, ySpeed;
        private final int MAX_SPEED = 15;


        public Bubble(int X, int Y, int Size){
            x = X;
            y = Y;
            size = Size;
            color = Color.argb(rand.nextInt(256),
                    rand.nextInt(256),
                    rand.nextInt(256),
                    rand.nextInt(256));
            xSpeed = rand.nextInt(MAX_SPEED * 2 + 1) - MAX_SPEED;
            ySpeed = rand.nextInt(MAX_SPEED * 2 + 1) - MAX_SPEED;

            //xSpeed = ySpeed = 2;

            if(xSpeed == 0){
                xSpeed = rand.nextInt(MAX_SPEED * 2 + 1) - MAX_SPEED;
            }

            if(ySpeed == 0){
                ySpeed = rand.nextInt(MAX_SPEED * 2 + 1) - MAX_SPEED;
            }
        }

        public void draw(Canvas canvas){
            paint.setColor(color);
            canvas.drawOval(x - size/2, y - size/2, x + size/2, y + size/2, paint);

        }

        public void update(){
            x += xSpeed;
            y += ySpeed;

            if(x - size/2 <= 0 || x + size/2 >= getWidth())
                xSpeed = -xSpeed;

            if(y - size/2 <= 0 || y + size/2 >= getHeight())
                ySpeed = -ySpeed;
        }
    }
}
