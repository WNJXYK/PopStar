package cn.Arthur.ZZ.PopStar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/* 
 * 这个类的作用就是定义粒子，并写构造函数把粒子初始化 
 */  
public class Particle {  
    Bitmap picture;  //设置粒子的贴图  
    int size;  //粒子大小  
    double vertical_v;  //粒子的竖向速度  
    double horizontal_v;    //水平速度  
    double g_v;//重力加速度
    int currentX;   //实时X位置  
    int currentY;   //实时Y位置  
      
    public Particle(Bitmap picture, int size,int currentX, int currentY, double vertical_v, double horizontal_v){  
        this.picture = picture;  
        this.size = size;  
        this.vertical_v = vertical_v;  
        this.horizontal_v = horizontal_v;  
        this.currentX = currentX;  
        this.currentY = currentY;   
        this.g_v=9.8;
    }  
    
    public void act(int delta){
    	vertical_v+=9.8*delta/500;
    	currentX+=vertical_v;
    	currentY+=horizontal_v;
    }
    
    public int getX(){
    	return currentX;
    }
    public int getY(){
    	return currentY;
    }
    
    public void draw(Canvas canvas){
    	Paint paint = new Paint();
    	canvas.drawBitmap(picture, new Rect(0,0,picture.getHeight(),picture.getHeight()), new Rect(currentY-size/2,currentX-size/2,currentY+size/2,currentX+size/2), paint);
    }
}  