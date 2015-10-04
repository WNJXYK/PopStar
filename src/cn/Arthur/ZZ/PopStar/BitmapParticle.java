package cn.Arthur.ZZ.PopStar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class BitmapParticle {
	String text;
	int start_sizey;
	int end_sizey;
	int start_sizex;
	int end_sizex;
	int start_x;
	int start_y;
	int end_x;
	int end_y;
	int movetime;
	int maxmovetime;
	int staytime;
	int frozentime;
	int color;
	int screenwidth;
	Bitmap pic;
	public BitmapParticle(Bitmap pic,int ssx,int ssy,int esx,int esy,int sx,int sy,int ex,int ey,int ft,int mt,int st){
		this.pic=pic;
		this.start_sizex=ssx;
		this.start_sizey=ssy;
		this.end_sizex=esx;
		this.end_sizey=esy;
		this.start_x=sx;
		this.start_y=sy;
		this.end_x=ex;
		this.end_y=ey;
		this.frozentime=ft;
		this.movetime=mt;
		this.maxmovetime=mt;
		this.staytime=st+1;
	}
	
	public void act(int delta){
		if (frozentime>0){
			frozentime-=delta;
		}else if (movetime>0){
			movetime-=delta;
		}else{
			staytime-=delta;
		}
	}
	
	public void draw(Canvas canvas){
		Paint paint=new Paint();
		if (frozentime>0){
			
		}else if (movetime<=0){
			canvas.drawBitmap(pic, new Rect(0,0,pic.getWidth(),pic.getHeight()), new Rect(end_x-end_sizex/2,end_y-end_sizey/2,end_x+end_sizex/2,end_y+end_sizey/2), paint);
		}else{
			double progress=1.0f-(double)movetime/maxmovetime;
			int currentx=start_x+(int)((double)(end_x-start_x)*(double)progress);
			int currenty=start_y+(int)((double)(end_y-start_y)*(double)progress);
			int currentsx=start_sizex+(int)((double)(end_sizex-start_sizex)*(double)progress);
			int currentsy=start_sizey+(int)((double)(end_sizey-start_sizey)*(double)progress);
			canvas.drawBitmap(pic, new Rect(0,0,pic.getWidth(),pic.getHeight()), new Rect(currentx-currentsx/2,currenty-currentsy/2,currentx+currentsx/2,currenty+currentsy/2), paint);
		}
	}
	
	public int getStaytime(){
		return this.staytime;
	}

}
