package cn.Arthur.ZZ.PopStar;

import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class MainView extends View implements Runnable{
	private int textX = 20, textY = 20;
	Bitmap pop;
	Bitmap star;
	Bitmap stars[]=new Bitmap[6];
	Bitmap start,resume,about,noresume;
	Bitmap background,background_game;
	Main mActivity;
	int screenheight=720,screenwidth=480;
	ArrayList<Particle> particleset=new ArrayList<Particle>(); 
	DisplayMetrics screeninformation = new DisplayMetrics();
	int times=0;
	Thread thread;
	Bitmap highscore;
	Random random=new Random();
	private Typeface mFace;   
	int fontsize=100;
	boolean isGone=true;
	int high;
	boolean isResume;
	boolean music;
	Bitmap pmusic,nmusic;
	
	
	public MainView(Context context) {
		super(context);
		mActivity=(Main)context;
		getPicture();
		screeninformation = getResources().getDisplayMetrics();  
		screenwidth = screeninformation.widthPixels;
		screenheight = screeninformation.heightPixels;
		InitFontSize();
		thread=new Thread(this);
		thread.start();
		setFocusable(true);
	}

	/**
	 * 重写父类绘图函数
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		//创建一个画笔的实例
		Paint paint = new Paint();
		DrawBackGround(canvas);
		DrawParticle(canvas);
		ShowInformation(canvas);
		showUI(canvas);
		super.onDraw(canvas);
	}


	public void Gone(){
		isGone=true;
	}
	
	public void Stopped(){
		isGone=false;
	}
	
	
	public void set(int h,boolean i,boolean m){
		high=h;
		isResume=i;
		music =m;
	}
	
	private  void getPicture(){
		pop=getRes(R.drawable.t_pop);
		star=getRes(R.drawable.t_star);
		stars[1]=getRes(R.drawable.blue2);
		stars[2]=getRes(R.drawable.red2);
		stars[3]=getRes(R.drawable.yellow2);
		stars[4]=getRes(R.drawable.purple2);
		stars[5]=getRes(R.drawable.green2);
		start=getRes(R.drawable.startgame);
		resume=getRes(R.drawable.resumegame);
		noresume=getRes(R.drawable.noresume);
		about=getRes(R.drawable.aboutgame);
		background=getRes(R.drawable.background);
		background_game=getRes(R.drawable.background_game);
		highscore=getRes(R.drawable.highscore);
		pmusic=getRes(R.drawable.music);
		nmusic=getRes(R.drawable.nomusic);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//获取用户手指触屏的X坐标赋值与文本的X坐标
		textX = (int)event.getX();
		//获取用户手指触屏的Y坐标赋值与文本的Y坐标
		textY = (int)event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			solve(textX,textY);
		}
		
		//重绘画布
		//postInvalidate();
		return true;
	}

	
	/*
	 * 返回图片资源
	 */
	public Bitmap getRes(int resID) {
		return BitmapFactory.decodeResource(getResources(),resID);
	}
	
	private void DrawBackGround(Canvas canvas){
		Paint paint=new Paint();
		canvas.drawBitmap(background, new Rect(0,0,background.getWidth(),background.getHeight()), new Rect(0,0,screenwidth,screenheight), paint);
	}
	
	/**
	 * 计算字体大小
	 */
	private void InitFontSize(){
		int iheight=(screenheight-screenwidth)/4;
		mFace = Typeface.createFromAsset(getContext().getAssets(),"233.ttf");
		String str="这是个测试";
		Paint countPaint=new Paint();
		countPaint.setTypeface(mFace);
		countPaint.setTextSize(fontsize);
		Rect textBounds = new Rect();
		countPaint.getTextBounds(str, 0, str.length(), textBounds);//get text bounds, that can get the text width and height
		while (textBounds.height()>iheight/2){
			fontsize--;
			countPaint.setTextSize(fontsize);
			countPaint.getTextBounds(str, 0, str.length(), textBounds);
		}
	}
	
	/**
	 * 绘制文字
	 * @param str
	 * @param canvas
	 */
	private void DrawText(String str,Canvas canvas,int x,int y,int size,int color){
		Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.DEV_KERN_TEXT_FLAG);
		countPaint.setColor(color);
		countPaint.setTextSize(size);
		countPaint.setTypeface(mFace);
		//countPaint.setTextAlign(Paint.Align.CENTER);
		Rect textBounds = new Rect();
		countPaint.getTextBounds(str, 0, str.length(), textBounds);//get text bounds, that can get the text width and height
		int textWidth =textBounds.right-textBounds.left;
		while (x+textWidth/2>screenwidth||x-textWidth/2<0){
			countPaint.setTextSize(countPaint.getTextSize()-1);
			countPaint.getTextBounds(str, 0, str.length(), textBounds);
			textWidth =textBounds.right-textBounds.left;
		}
		int textHeight = textBounds.bottom - textBounds.top;
		canvas.drawText(str, x-textWidth/2, y + textHeight/2,countPaint);
	}
	
	private void DrawTextZZ(String str,Canvas canvas,int x,int y,int size,int color){
		Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.DEV_KERN_TEXT_FLAG);
		countPaint.setColor(color);
		countPaint.setTextSize(size);
		countPaint.setTypeface(mFace);
		//countPaint.setTextAlign(Paint.Align.CENTER);
		Rect textBounds = new Rect();
		countPaint.getTextBounds(str, 0, str.length(), textBounds);//get text bounds, that can get the text width and height
		int textWidth =textBounds.right-textBounds.left;
		int textHeight = textBounds.bottom - textBounds.top;
		canvas.drawText(str, x-textWidth, y - textHeight,countPaint);
	}
	
	private void ShowInformation(Canvas canvas){
		Paint paint=new Paint();
		int highscorey=(screenheight-screenwidth)/8*1;
		int iheight=(screenheight-screenwidth)/4;
		//最高分
		canvas.drawBitmap(highscore,new Rect(0,0,highscore.getWidth(),highscore.getHeight()),new Rect(0,highscorey-iheight/4,screenwidth/2,highscorey+iheight/4), paint);
		DrawText(String.valueOf(high),canvas,screenwidth/4*3,highscorey,fontsize,Color.rgb(3, 162, 97));
	}
	
	private void showUI(Canvas canvas){
		Paint paint=new Paint();
		int size=remin(screenwidth/2,screenwidth/3);
		canvas.drawBitmap(pop, new Rect(0,0,pop.getWidth(),pop.getHeight()), new Rect(screenwidth/4-size/2,screenheight/2/3-size/2,screenwidth/4+size/2,screenheight/2/3+size/2), paint);
		canvas.drawBitmap(star, new Rect(0,0,star.getWidth(),star.getHeight()), new Rect(screenwidth/4*3-size/2,screenheight/3-size/2,screenwidth/4*3+size/2,screenheight/3+size/2), paint);
		int width,height;
		height=screenheight/2/6;
		width=height*2;
		canvas.drawBitmap(start, new Rect(0, 0, start.getWidth(),start.getWidth()), new Rect(screenwidth/2-width/2,screenheight/2+height*1-height,screenwidth/2+width/2,screenheight/2+height*1+height), paint);
		if (isResume){
			canvas.drawBitmap(resume, new Rect(0, 0, resume.getWidth(),resume.getWidth()), new Rect(screenwidth/2-width/2,screenheight/2+height*3-height,screenwidth/2+width/2,screenheight/2+height*3+height), paint);
		}else{
			canvas.drawBitmap(noresume, new Rect(0, 0, noresume.getWidth(),noresume.getWidth()), new Rect(screenwidth/2-width/2,screenheight/2+height*3-height,screenwidth/2+width/2,screenheight/2+height*3+height), paint);
		}
		canvas.drawBitmap(about, new Rect(0, 0, about.getWidth(),about.getWidth()), new Rect(screenwidth/2-width/2,screenheight/2+height*5-height,screenwidth/2+width/2,screenheight/2+height*5+height), paint);
		if (music){
			canvas.drawBitmap(pmusic, new Rect(0,0,pmusic.getWidth(),pmusic.getHeight()),new Rect(0,screenheight-pmusic.getHeight(),pmusic.getWidth(),screenheight),paint);
		}else{
			canvas.drawBitmap(nmusic, new Rect(0,0,nmusic.getWidth(),nmusic.getHeight()),new Rect(0,screenheight-nmusic.getHeight(),nmusic.getWidth(),screenheight),paint);
		}
		DrawTextZZ("@南师附中软件开发社 ",canvas,screenwidth,screenheight,fontsize/3,Color.WHITE);
	}
	
	private int remax(int a,int b){
		if (a>b) return a;
		return b;
	}
	
	private int remin(int a,int b){
		if (a<b) return b;
		return a;
	}
	/*
	 * 获得随机数
	 */
	private int getRandom(int min,int max){  
		int ran=Math.abs(random.nextInt());  
		int returnRan=ran%(max-min+1)+min;  
		return returnRan;
	}
	
	/**
	 * 激活粒子
	 */
	private void actParticle(int delta){
		ArrayList<Particle> tempset=new ArrayList<Particle>();
		int count=particleset.size();
		for (int i=0;i<count;i++){
			Particle temp =particleset.get(i);
			temp.act(delta);
			if (temp.getX()<screenheight && temp.getX()>0 && temp.getY()>0 && temp.getY()<=screenwidth) {
				tempset.add(temp);
			}
		}
		particleset=tempset;
	}
	
	/**
	 * 绘制粒子
	 * @param canvas
	 */
	private void DrawParticle(Canvas canvas){
		ArrayList<Particle> temp=particleset; 
		int count=temp.size();
		for (int i=0;i<count;i++){
			temp.get(i).draw(canvas);
		}
	}
	
	/**
	 * 增加粒子
	 * @param picture
	 * @param size
	 * @param x
	 * @param y
	 * @param y_v
	 * @param x_v
	 */
	private void newParticle(Bitmap picture,int size,int x,int y,int y_v,int x_v){
		if (particleset.size()>=300) return;
		Particle temp=new Particle(picture,size,x,y,y_v,x_v);
		particleset.add(temp);
	}
	
	private void solve(int x,int y){
		int width,height;
		height=screenheight/2/6;
		width=height*2;
		if (x>=screenwidth/2-width/2 && y>=screenheight/2+height*1-height && x<=screenwidth/2+width/2 && y<=screenheight/2+height*1+height){
			mActivity.NewGame();
		}
		if (isResume && x>=screenwidth/2-width/2 && y>=screenheight/2+height*3-height && x<=screenwidth/2+width/2 && y<screenheight/2+height*3+height){
			mActivity.ResumeGame();
		}
		if (x>=screenwidth/2-width/2 && y>=screenheight/2+height*5-height && x<=screenwidth/2+width/2 && y<screenheight/2+height*5+height){
			showAbout();
		}
		if (x>=0&&y>=screenheight-pmusic.getHeight()&&x<=pmusic.getWidth()&&y<=screenheight){
			music=!music;
			mActivity.setMusic();
		}
	}
	
	private void showAbout(){
		new AlertDialog.Builder(mActivity)
		.setTitle("关于程序")
		.setIcon(R.drawable.icon)
		.setMessage("软件名称:消灭星星\n"
				+ "来自团队:南京师范大学附属中学——软件开发社\n"
				+ "主程序:ZZ\n"
				+ "图片:ZZ\n"
				+ "音乐:PopStar & IB\n"
				+ "----------------\n"
				+ "-华丽的分界线-\n"
				+ "----------------\n"
				+ "新浪ID:喂你脚下有坑\n"
				+ "百度ID:Z霜之哀伤Z\n"
				+ "QQ:767881816")
		.setPositiveButton("确定", null)
		.show();
	}
	
	public void run() {
		while (true) {
			if (isGone){
				actParticle(20);
				if (++times%20==0){
					for (int i=1;i<=20;i++)newParticle(stars[getRandom(1,5)],getRandom(stars[1].getHeight()/15*1,stars[1].getHeight()/10*3),getRandom(0,screenwidth),getRandom(0,screenheight),-getRandom(3,8),getRandom(-4,4));
					times=0;
				}
				postInvalidate();
			}
			try {
				Thread.sleep(20);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

