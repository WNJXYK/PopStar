package cn.Arthur.ZZ.PopStar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class TextParticle {
	String text;
	int start_size;
	int end_size;
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
	Typeface mFace; 
	public TextParticle(String text,int ss,int es,int sx,int sy,int ex,int ey,int ft,int mt,int st,int col,int sw,Typeface mFace){
		this.text=text;
		this.start_size=ss;
		this.end_size=es;
		this.start_x=sx;
		this.start_y=sy;
		this.end_x=ex;
		this.end_y=ey;
		this.frozentime=ft;
		this.movetime=mt;
		this.maxmovetime=mt;
		this.staytime=st+1;
		this.color=col;
		this.screenwidth=sw;
		this.mFace=mFace;
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
	
	/**
	 * »æÖÆÎÄ×Ö
	 * @param str
	 * @param canvas
	 */
	private void DrawText(String str,Canvas canvas,int x,int y,int size){
		Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.DEV_KERN_TEXT_FLAG);
		countPaint.setColor(this.color);
		countPaint.setTextSize(size);
		countPaint.setTypeface(mFace);
		//countPaint.setTextAlign(Paint.Align.CENTER);
		Rect textBounds = new Rect();
		countPaint.getTextBounds(str, 0, str.length(), textBounds);//get text bounds, that can get the text width and height
		int textWidth =textBounds.right-textBounds.left;
		while (textWidth>screenwidth){
			countPaint.setTextSize(countPaint.getTextSize()-1);
			countPaint.getTextBounds(str, 0, str.length(), textBounds);
			textWidth =textBounds.right-textBounds.left;
		}
		int textHeight = textBounds.bottom - textBounds.top;
		canvas.drawText(str, x-textWidth/2, y + textHeight/2,countPaint);
	}
	
	public void draw(Canvas canvas){
		if (frozentime>0){
			
		}else if (movetime<=0){
			DrawText(text,canvas,end_x,end_y,end_size);
		}else{
			double progress=1.0f-(double)movetime/maxmovetime;
			int currentx=start_x+(int)((double)(end_x-start_x)*(double)progress);
			int currenty=start_y+(int)((double)(end_y-start_y)*(double)progress);
			int currents=start_size+(int)((double)(end_size-start_size)*(double)progress);
			DrawText(text,canvas,currentx,currenty,currents);
		}
	}
	
	public int getStaytime(){
		return this.staytime;
	}

}
