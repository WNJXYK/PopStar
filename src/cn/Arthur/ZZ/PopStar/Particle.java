package cn.Arthur.ZZ.PopStar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/* 
 * ���������þ��Ƕ������ӣ���д���캯�������ӳ�ʼ�� 
 */  
public class Particle {  
    Bitmap picture;  //�������ӵ���ͼ  
    int size;  //���Ӵ�С  
    double vertical_v;  //���ӵ������ٶ�  
    double horizontal_v;    //ˮƽ�ٶ�  
    double g_v;//�������ٶ�
    int currentX;   //ʵʱXλ��  
    int currentY;   //ʵʱYλ��  
      
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