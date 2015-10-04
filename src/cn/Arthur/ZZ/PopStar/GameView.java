package cn.Arthur.ZZ.PopStar;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GameView extends View implements Runnable {
	private int textX = 20, textY = 20;
	/*
	 * 游戏信息
	 */
	int mapheight=20,mapwidth=20;
	int screenheight=720,screenwidth=480;
	int blocksize=48;
	int picturesize=100;
	int block[][]=new int[25][25];
	int showblockx[][]=new int[25][25];
	int showblocky[][]=new int[25][25];
	int showtime=0;
	int colorlimited=5;
	boolean isclicked=false;
	boolean blockclicked[][]=new boolean[25][25];
	int basescore=0;
	Bitmap blockpicture[][]=new Bitmap[10][5];
	Bitmap blockclickedpicture;
	Bitmap endblock;
	private Thread thread;
	boolean animationflag;
	int animationnum;
	int animationstep;
	int score=0;
	int showscore=0;
	int oldscore=0;
	boolean isend=false;
	boolean finisheddraw=true;
	int highscorey;
	int levelinformationy;
	int scorey;
	int awardy;
	int iheight;
	int fontsize=100;
	int startdelayed;
	int enddelayed;
	int targescore;
	int addtargescore=0;
	int addbasescore=1050;
	int level=0;
	int high=0;
	private Typeface mFace;       
	boolean isWin=false;
	boolean isMenu=false;
	Bitmap winbitmap,losebitmap;
	Bitmap background,background_game;
	Bitmap highscore;
	Bitmap progress_n,progress_r,progress_y,progress_g;
	Bitmap resume,save;
	Main mAcitvity;
	boolean isGone=true;
	ArrayList<Particle> particleset=new ArrayList<Particle>(); 
	ArrayList<TextParticle> textparticleset=new ArrayList<TextParticle>(); 
	ArrayList<BitmapParticle> bitmapparticleset=new ArrayList<BitmapParticle>(); 
	/*
	 * 辅助
	 */
	Random random=new Random();
	DisplayMetrics screeninformation = new DisplayMetrics();
	
	
	/*
	 * 构造函数
	 */
	public GameView(Context context) {
		super(context);
		mAcitvity=(Main)context;
		InitGameInf();
		thread=new Thread(this);
		thread.start();
		Stopped();
		setFocusable(true);
	}
	
	public void InitNewGame(int score,int level,int ts,int h){
		this.level=level;
		this.score=score;
		this.addtargescore=ts;
		this.high=h;
		isMenu=false;
		InitGameData();
	}
	
	/**
	 * 获得基本信息
	 */
	private void InitGameInf(){
		mapheight=10;
		mapwidth=10;
		colorlimited=5;
		addbasescore=1050;
		InitPicture();
		screeninformation = getResources().getDisplayMetrics();  
		screenwidth = screeninformation.widthPixels;
		screenheight = screeninformation.heightPixels;
		blocksize=screenwidth/mapwidth;
		highscorey=(screenheight-screenwidth)/8*1;
		levelinformationy=(screenheight-screenwidth)/8*3;
		scorey=(screenheight-screenwidth)/8*5;
		awardy=(screenheight-screenwidth)/8*7;
		iheight=(screenheight-screenwidth)/4;
		picturesize=blockpicture[1][0].getHeight();
		InitFontSize();
		isMenu=false;
	}
	
	/*
	 * 重新生成游戏信息
	 */
	private void InitGameData(){
		InitBlock();
		oldscore=score;
		mAcitvity.SaveGame(score, level, addtargescore,high);
		animationflag=true;
		animationnum=0;
		animationstep=0;
		showscore=score;
		isend=false;
		finisheddraw=true;
		isclicked=false;
		startdelayed=6500;
		enddelayed=6100;
		level++;
		addtargescore+=(level/4)*625;
		targescore=addbasescore*level+addtargescore;
		particleset.clear();
		textparticleset.clear();
		bitmapparticleset.clear();
		mAcitvity.PlaySound(2147483647);
		newTextParticle("第"+String.valueOf(level)+"关",fontsize,fontsize,-screenwidth/2,screenheight/8*3,screenwidth/2,screenheight/8*3,300,1000,1200,Color.rgb(288, 193,0));
		newTextParticle("目标分数是:"+String.valueOf(targescore),fontsize,fontsize,(int)((double)screenwidth*1.5),screenheight/8*5,screenwidth/2,screenheight/8*5,300,1000,1200,Color.rgb(0, 177,288));
		newTextParticle("3",1,fontsize*2,screenwidth/2,screenheight/2,screenwidth/2,screenheight/2,2500,900,100,Color.WHITE);
		newTextParticle("2",1,fontsize*2,screenwidth/2,screenheight/2,screenwidth/2,screenheight/2,3500,900,100,Color.rgb(237, 250,28));
		newTextParticle("1",1,fontsize*2,screenwidth/2,screenheight/2,screenwidth/2,screenheight/2,4500,900,100,Color.rgb(237, 78,0));
		newTextParticle("Go!",1,fontsize*2,screenwidth/2,screenheight/2,screenwidth/2,screenheight/2,5500,700,300,Color.RED);
	}
	/**
	 * 计算字体大小
	 */
	private void InitFontSize(){
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
	
	/*
	 * 重新生成地图
	 */
	private void InitBlock(){
		for (int i=1;i<=mapheight;i++){
			for (int j=1;j<=mapwidth;j++){
				block[i][j]=getRandom(1,colorlimited);
			}
		}
	}
	
	/*
	 * 读取全部图片
	 */
	private void InitPicture(){
		blockpicture[1][0]=getRes(R.drawable.blue1);
		blockpicture[1][1]=getRes(R.drawable.blue2);
		blockpicture[1][2]=getRes(R.drawable.blue3);
		blockpicture[1][3]=getRes(R.drawable.blue4);
		blockpicture[2][0]=getRes(R.drawable.red1);
		blockpicture[2][1]=getRes(R.drawable.red2);
		blockpicture[2][2]=getRes(R.drawable.red3);
		blockpicture[2][3]=getRes(R.drawable.red4);
		blockpicture[3][0]=getRes(R.drawable.yellow1);
		blockpicture[3][1]=getRes(R.drawable.yellow2);
		blockpicture[3][2]=getRes(R.drawable.yellow3);
		blockpicture[3][3]=getRes(R.drawable.yellow4);
		blockpicture[4][0]=getRes(R.drawable.green1);
		blockpicture[4][1]=getRes(R.drawable.green2);
		blockpicture[4][2]=getRes(R.drawable.green3);
		blockpicture[4][3]=getRes(R.drawable.green4);
		blockpicture[5][0]=getRes(R.drawable.purple1);
		blockpicture[5][1]=getRes(R.drawable.purple2);
		blockpicture[5][2]=getRes(R.drawable.purple3);
		blockpicture[5][3]=getRes(R.drawable.purple4);
		blockclickedpicture=getRes(R.drawable.clicked);
		endblock=getRes(R.drawable.star);
		winbitmap=getRes(R.drawable.win);
		losebitmap=getRes(R.drawable.lose);
		background=getRes(R.drawable.background);
		background_game=getRes(R.drawable.background_game);
		highscore=getRes(R.drawable.highscore);
		progress_n=getRes(R.drawable.progress);
		progress_r=getRes(R.drawable.progress_red);
		progress_y=getRes(R.drawable.progress_yellow);
		progress_g=getRes(R.drawable.progress_green);
		save=getRes(R.drawable.save);
		resume=getRes(R.drawable.resume);
	}
	
	/**
	 * 清空点击
	 */
	private void CleanClickMap(){
		for (int i=1;i<=mapheight;i++){
			for (int j=1;j<=mapwidth;j++){
				blockclicked[i][j]=false;
			}
		}
	}
	
	/**
	 * 泛洪
	 */
	private void FloodFill(int x,int y,int color){
		blockclicked[x][y]=true;
		basescore++;
		int dx[]={0,1,-1,0,0};
		int dy[]={0,0,0,1,-1};
		for (int i=1;i<=4;i++){
			if (x+dx[i]>=1 && x+dx[i]<=mapwidth && y+dy[i]>=1 && y+dy[i]<=mapheight && block[x+dx[i]][y+dy[i]]==color && blockclicked[x+dx[i]][y+dy[i]]==false){
				FloodFill(x+dx[i],y+dy[i],color);
			}
		}
	}
	
	/**
	 * 消除方块
	 */
	private void FloodFillClean(int x,int y,int color){
		for (int i=1;i<=6;i++)newParticle(blockpicture[block[x][y]][1],getRandom(blocksize/20*2,blocksize/10*2),(int)(screenheight-blocksize*mapheight+x*blocksize),(int)y*blocksize,-getRandom(3,8),getRandom(-4,4));
		block[x][y]=0;
		int dx[]={0,1,-1,0,0};
		int dy[]={0,0,0,1,-1};
		for (int i=1;i<=4;i++){
			if (x+dx[i]>=1 && x+dx[i]<=mapwidth && y+dy[i]>=1 && y+dy[i]<=mapheight && block[x+dx[i]][y+dy[i]]==color){
				FloodFillClean(x+dx[i],y+dy[i],color);
			}
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
	
	/**
	 * 增加文字粒子
	 */
	private void newTextParticle(String text,int ss,int es,int sx,int sy,int ex,int ey,int ft,int mt,int st,int col){
		if (textparticleset.size()>=200) return;
		TextParticle temp=new TextParticle(text,ss,es,sx,sy,ex,ey,ft,mt,st,col,screenwidth,mFace);
		textparticleset.add(temp);
	}
	
	/*
	 * 增加图片粒子
	 */
	private void newBitmapParticle(Bitmap pic,int ssx,int ssy,int esx,int esy,int sx,int sy,int ex,int ey,int ft,int mt,int st){
		if (bitmapparticleset.size()>=200) return;
		BitmapParticle temp=new BitmapParticle(pic,ssx,ssy,esx,esy,sx,sy,ex,ey,ft,mt,st);
		bitmapparticleset.add(temp);
	}
	
	//显示最高分和当前关卡
	private void ShowInformation(Canvas canvas){
		double progress=(double)showscore/(double)targescore;
		if (progress>1) progress=1;
		Paint paint=new Paint();
		//最高分
		canvas.drawBitmap(highscore,new Rect(0,0,highscore.getWidth(),highscore.getHeight()),new Rect(0,highscorey-iheight/4,screenwidth/2,highscorey+iheight/4), paint);
		DrawText(String.valueOf(high),canvas,screenwidth/4*3,highscorey,fontsize,Color.rgb(3, 162, 97));
		//当前关卡
		DrawText(String.valueOf("当前关卡："+level),canvas,screenwidth/16*3,levelinformationy,fontsize,Color.rgb(53, 100, 140));
		canvas.drawBitmap(progress_n, new Rect(0,0,progress_n.getWidth(),progress_n.getHeight()), new Rect(screenwidth/2,levelinformationy-iheight/4,screenwidth,levelinformationy+iheight/4),paint);
		if (progress<=0.7){
			canvas.drawBitmap(progress_r, new Rect(0,0,(int)((double)progress_r.getWidth()*(double)progress),progress_r.getHeight()), new Rect(screenwidth/2,levelinformationy-iheight/4,(int)((double)screenwidth/2+(double)screenwidth/2*progress),levelinformationy+iheight/4),paint);
		}else if (progress<=0.9){
			canvas.drawBitmap(progress_y, new Rect(0,0,(int)((double)progress_y.getWidth()*(double)progress),progress_y.getHeight()), new Rect(screenwidth/2,levelinformationy-iheight/4,(int)((double)screenwidth/2+(double)screenwidth/2*progress),levelinformationy+iheight/4),paint);
		}else{
			canvas.drawBitmap(progress_g, new Rect(0,0,(int)((double)progress_g.getWidth()*(double)progress),progress_g.getHeight()), new Rect(screenwidth/2,levelinformationy-iheight/4,(int)((double)screenwidth/2+(double)screenwidth/2*progress),levelinformationy+iheight/4),paint);
		}
		if (progress!=1){
			DrawText(String.valueOf(showscore)+"/"+String.valueOf(targescore),canvas,screenwidth/4*3,levelinformationy,fontsize,Color.GRAY);
		}else{
			DrawText("Pass~",canvas,screenwidth/4*3,levelinformationy,fontsize,Color.GRAY);
		}
		//当前得分
		DrawText("得分："+String.valueOf(showscore),canvas,screenwidth/2,scorey,fontsize+5,Color.rgb(0,132,255));
		//获得分数
		if (isclicked) DrawText(String.valueOf(basescore)+"个星星   "+String.valueOf(getScore(basescore))+"分",canvas,screenwidth/2,awardy,remax(20,fontsize-20),Color.LTGRAY);
	}
	
	/**显示菜单
	 * 
	 */
	private void ShowMenu(Canvas canvas){
		if (isMenu){
			Paint paint=new Paint();
			canvas.drawBitmap(resume, new Rect(0,0,resume.getWidth(),resume.getHeight()), new Rect(screenwidth/2-resume.getWidth()/2,screenheight/8*3-resume.getHeight()*resume.getWidth()/screenwidth,screenwidth/2+resume.getWidth()/2,screenheight/8*3+resume.getHeight()*resume.getWidth()/screenwidth), paint);
			canvas.drawBitmap(save, new Rect(0,0,save.getWidth(),save.getHeight()), new Rect(screenwidth/2-save.getWidth()/2,screenheight/8*5-save.getHeight()*save.getWidth()/screenwidth,screenwidth/2+save.getWidth()/2,screenheight/8*5+save.getHeight()*save.getWidth()/screenwidth), paint);
		}
	}
	/**
	 * 处理菜单
	 */
	private void solveMenu(int x,int y){
		if (x>=screenwidth/2-resume.getWidth()/2 && y>=screenheight/8*3-resume.getHeight()*resume.getWidth()/screenwidth && x<=screenwidth/2+resume.getWidth()/2 && y<=screenheight/8*3+resume.getHeight()*resume.getWidth()/screenwidth){
			isMenu=false;
			return ;
		}
		if (x>=screenwidth/2-save.getWidth()/2 && y>=screenheight/8*5-save.getHeight()*save.getWidth()/screenwidth && x<=screenwidth/2+save.getWidth()/2 && y<=screenheight/8*5+save.getHeight()*save.getWidth()/screenwidth){
			if (isend==true && isWin==false){
				Toast.makeText(mAcitvity, "这时候不可以保存哦~", Toast.LENGTH_SHORT).show();
			}else if(isend==true){
				Toast.makeText(mAcitvity, "这时候保存会丢失进度哦~", Toast.LENGTH_SHORT).show();
			}else{
				mAcitvity.SaveGame(oldscore, level-1, addtargescore-(level/3)*550,high);
				mAcitvity.GoToMainView();
			}
			
		}
	}
	
	/**
	 * 下降方块
	 */
	private void DownBlock(){
		for (int i=1;i<=mapheight;i++){
			for (int j=1;j<=mapwidth;j++){
				showblockx[i][j]=i;
				showblocky[i][j]=j;
			}
		}
		for (int j=1;j<=mapwidth;j++){
			for (int i=mapheight;i>=1;i--){
				int ti=i;
				while(ti+1<=mapheight && block[ti+1][j]==0){
					block[ti+1][j]=block[ti][j];
					block[ti][j]=0;
					showblockx[ti+1][j]=showblockx[ti][j];
					showblocky[ti+1][j]=showblocky[ti][j];
					ti++;
				}
			}
			int tj=j;
			while (tj-1>=1 && block[mapheight][tj-1]==0){
				for (int i=1;i<=mapheight;i++){
					block[i][tj-1]=block[i][tj];
					block[i][tj]=0;
					showblockx[i][tj-1]=showblockx[i][tj];
					showblocky[i][tj-1]=showblocky[i][tj];
				}
				tj--;
			}
		}
		showtime=10;
	}
	
	/**
	 * 判断是否游戏结束
	 */
	private void isFinished(){
		boolean flag=true;
		int dx[]={0,1,-1,0,0};
		int dy[]={0,0,0,1,-1};
		for (int i=1;i<=mapheight;i++){
			for (int j=1;j<=mapwidth;j++){
				if (block[i][j]!=0){
					for (int k=1;k<=4;k++){
						if (i+dx[k]>=1 && i+dx[k]<=mapheight && j+dy[k]>=1 && j+dy[k]<=mapwidth && block[i+dx[k]][j+dy[k]]==block[i][j]){
							flag=false;
							break;
						}
					}
				}
				if (flag==false) break;
			}
			if (flag==false) break;
		}
		isend=flag;
	}
	
	/**
	 * 获得奖励分数
	 */
	private int getBounds(){
		int bounds=2000;
		for (int i=1;i<=mapheight;i++){
			for (int j=1;j<=mapwidth;j++){
				if (block[i][j]!=0) bounds-=200;
				if (bounds<=0){
					bounds=0;
					break;
				}
			}
		}
		return bounds;
	}
	
	
	/**
	 * 游戏结束处理
	 */
	private void SolveEnd(){
		if (isend==true){
			showscore=score;
			int bounds = getBounds();
			newTextParticle("奖励得分:"+String.valueOf(bounds),1,remax(20,fontsize-20),screenwidth/2,scorey+(screenheight/2-scorey)/2,screenwidth/2,scorey+(screenheight/2-scorey)/2,100,300,1000,Color.RED);
			newTextParticle(String.valueOf(bounds),remax(20,fontsize-20),remax(20,fontsize-20),screenwidth/2,scorey+(screenheight/2-scorey)/2,screenwidth/2,scorey,1400,300,1,Color.rgb(255,100,100));
			score+=bounds;
			if (score>targescore){
				isWin=true;
				mAcitvity.PlaySound(4);
				newBitmapParticle(winbitmap,0,0,screenwidth/5*3,screenwidth/5*3/5*3,screenwidth/2,screenheight/2,screenwidth/2,screenheight/2,2000,500,3500);
			}else{
				isWin=false;
				mAcitvity.PlaySound(5);
				newBitmapParticle(losebitmap,0,0,screenwidth/5*3,screenwidth/5*3/5*3,screenwidth/2,screenheight/2,screenwidth/2,screenheight/2,2000,500,3500);
			}
		}
		
	}
	
	/**
	 * 获取增加分数
	 */
	private int getScore(int x){
		int sum=0;
		for (int i=1;i<=x;i++){
			if (0<=i&& i<=2) sum+=10;
			if (2<i && i<=5) sum+=25;
			if (5<i && i<=7) sum+=50;
			if (7<i && i<=12) sum+=75;
			if (12<i) sum+=90;
		}
		return sum;
	}
	
	/**
	 * 处理第一次点击
	 * @param x
	 * @param y
	 */
	private void FirstClick(int x,int y){
		y-=screenheight-blocksize*mapheight;
		x/=blocksize;
		y/=blocksize;
		x++;
		y++;
		if (block[y][x]==0) return;
		CleanClickMap();
		basescore=0;
		FloodFill(y,x,block[y][x]);
		if (basescore>=2){
			mAcitvity.PlaySound(1);
			isclicked=true;
		}else{
			CleanClickMap();
			isclicked=false;
			basescore=0;
		}
	}
	
	/**
	 * 处理第二次点击
	 * @param x
	 * @param y
	 */
	private void SecondClick(int x,int y){
		int tx=x;
		int ty=y;
		y-=screenheight-blocksize*mapheight;
		x/=blocksize;
		y/=blocksize;
		x++;
		y++;
		if (blockclicked[y][x]==false) {
			isclicked=false;
			FirstClick(tx,ty);
		}else{
			mAcitvity.PlaySound(2);
			AddCleanScoreText(tx,ty,basescore);
			score+=getScore(basescore);
			basescore=0;
			FloodFillClean(y,x,block[y][x]);
			DownBlock();
			isclicked=false;
		}
	}

	/**
	 * 消除文字粒子效果
	 */
	private void AddCleanScoreText(int x,int y,int base){
		for (int i=1;i<=base;i++){
			if (0<=i&& i<=2) newTextParticle("10",1,40,x,y,screenwidth/2,scorey,(i-1)*20,600,1,Color.GRAY);
			if (2<i && i<=5) newTextParticle("25",1,40,x,y,screenwidth/2,scorey,(i-1)*20,600,1,Color.LTGRAY);
			if (5<i && i<=7) newTextParticle("50",1,40,x,y,screenwidth/2,scorey,(i-1)*20,600,1,Color.YELLOW);
			if (7<i && i<=12) newTextParticle("75",1,40,x,y,screenwidth/2,scorey,(i-1)*20,600,1,Color.GREEN);
			if (12<i) newTextParticle("90",1,40,x,y,screenwidth/2,scorey,(i-1)*20,600,1,Color.RED);
		}
	}
	
	/*
	 * 绘制背景
	 */
	
	private void DrawBackGround(Canvas canvas){
		Paint paint=new Paint();
		canvas.drawBitmap(background, new Rect(0,0,background.getWidth(),background.getHeight()), new Rect(0,0,screenwidth,screenheight), paint);
	}
	private void DrawBackGroundGame(Canvas canvas){
		Paint paint=new Paint();
		canvas.drawBitmap(background_game, new Rect(0,0,background_game.getWidth(),background_game.getHeight()), new Rect(0,screenheight-screenwidth,screenwidth,screenheight), paint);
	}
	
	/**
	 * 处理点击
	 * @param x 
	 * @param y
	 */
	private void SolveClick(int x,int y){
		if (y>=screenheight-blocksize*mapheight){
			if (isclicked==false){
				FirstClick(x,y);
			}else{
				SecondClick(x,y);
			}
		}
	}
	
	/*
	 * 获得随机数
	 */
	private int getRandom(int min,int max){  
		int ran=Math.abs(random.nextInt());  
		int returnRan=ran%(max-min+1)+min;  
		return returnRan;
	}
	
	/*
	 * 比大小
	 */
	private int remax(int a,int b){
		if (a<b) return b;
		return a;
	}
	
	/*
	 * 返回图片资源
	 */
	public Bitmap getRes(int resID) {
		return BitmapFactory.decodeResource(getResources(),resID);
	}
	
	/**
	 * 下落距离控制
	 * @param canvas
	 */
	private int getDownLength(int x){
		int ans[]={0,0,10,18,24,28,31,32,33,34,35};
		return ans[x];
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
	 * 激活文字粒子
	 */
	private void actTextParticle(int delta){
		ArrayList<TextParticle> tempset=new ArrayList<TextParticle>();
		int count=textparticleset.size();
		for (int i=0;i<count;i++){
			TextParticle temp =textparticleset.get(i);
			temp.act(delta);
			if (temp.getStaytime()>0) {
				tempset.add(temp);
			}
		}
		textparticleset=tempset;
	}
	
	/*
	 * 激活图片粒子
	 */
	private void actBitmapParticle(int delta){
		ArrayList<BitmapParticle> tempset=new ArrayList<BitmapParticle>();
		int count=bitmapparticleset.size();
		for (int i=0;i<count;i++){
			BitmapParticle temp =bitmapparticleset.get(i);
			temp.act(delta);
			if (temp.getStaytime()>0) {
				tempset.add(temp);
			}
		}
		bitmapparticleset=tempset;
	}
	

	
	/*
	 * 动画
	 */
	private void actAnimation(){
		//下落
		if(showtime>=0) showtime--;
		//星星
		if (animationflag==true){
			if ((++animationstep)%6==0){
				animationstep=0;
				animationnum=(animationnum+1)%4;
			}
		}
	}
	
	/**
	 * 激活分数显示
	 */
	private void actScore(){
		if (score>showscore) showscore+=3;
		if (score<showscore) showscore=score;
		if (showscore>high) high=showscore;
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
	 * 绘制文字粒子
	 * @param canvas
	 */
	private void DrawTextParticle(Canvas canvas){
		ArrayList<TextParticle> temp=textparticleset; 
		int count=temp.size();
		for (int i=0;i<count;i++){
			temp.get(i).draw(canvas);
		}
	}
	
	/**
	 * 绘制图片粒子
	 * @param canvas
	 */
	private void DrawBitmapParticle(Canvas canvas){
		ArrayList<BitmapParticle> temp=bitmapparticleset; 
		int count=temp.size();
		for (int i=0;i<count;i++){
			temp.get(i).draw(canvas);
		}
	}
	
	
	/*
	 * 绘制地图
	 */
	private void DrawBlock(Canvas canvas){
		Paint paint=new Paint();
		paint.setColor(Color.BLACK);
		for (int i=1;i<=mapheight;i++){
			for (int j=1;j<=mapwidth;j++){
				//选择显示
				if (isclicked==true && blockclicked[i][j]==true) canvas.drawBitmap(blockclickedpicture, new Rect(0,0,picturesize,picturesize), new Rect((j-1)*blocksize,screenheight-blocksize*mapheight+(i-1)*blocksize,j*blocksize,screenheight-blocksize*mapheight+i*blocksize), paint);
				//选中星星显示
				if ((isclicked&&blockclicked[i][j])==true && block[i][j]!=0 && showtime<=0 && isend==false)canvas.drawBitmap(blockpicture[block[i][j]][animationnum], new Rect(0,0,picturesize,picturesize), new Rect((j-1)*blocksize,screenheight-blocksize*mapheight+(i-1)*blocksize,j*blocksize,screenheight-blocksize*mapheight+i*blocksize), paint);
				//普通星星显示
				if ((isclicked&&blockclicked[i][j])==false && block[i][j]!=0 && showtime<=0 && isend==false)canvas.drawBitmap(blockpicture[block[i][j]][0], new Rect(0,0,picturesize,picturesize), new Rect((j-1)*blocksize,screenheight-blocksize*mapheight+(i-1)*blocksize,j*blocksize,screenheight-blocksize*mapheight+i*blocksize), paint);
				//游戏结束石化星星显示
				if (block[i][j]!=0 && isend==true)canvas.drawBitmap(endblock, new Rect(0,0,picturesize,picturesize), new Rect((j-1)*blocksize,screenheight-blocksize*mapheight+(i-1)*blocksize,j*blocksize,screenheight-blocksize*mapheight+i*blocksize), paint);
				//星星的下落效果
				if (block[i][j]!=0 && showtime>0 && isend==false){
					if (showblockx[i][j]==i && showblocky[i][j]==j){
						canvas.drawBitmap(blockpicture[block[i][j]][0], new Rect(0,0,picturesize,picturesize), new Rect((j-1)*blocksize,screenheight-blocksize*mapheight+(i-1)*blocksize,j*blocksize,screenheight-blocksize*mapheight+i*blocksize), paint);
					}else{
						//下坠效果
						double ofx=showblockx[i][j]-i,ofy=showblocky[i][j]-j;
						ofx*=blocksize;
						ofy*=blocksize;
						ofx=ofx*getDownLength(showtime)/35;
						ofy=ofy*getDownLength(showtime)/35;
						canvas.drawBitmap(blockpicture[block[i][j]][0], new Rect(0,0,picturesize,picturesize), new Rect((int)((j-1)*blocksize+ofy),(int)(screenheight-blocksize*mapheight+(i-1)*blocksize+ofx),(int)(j*blocksize+ofy),(int)(screenheight-blocksize*mapheight+i*blocksize+ofx)), paint);
					}
				}
			}
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
	
	/*
	 * 重写父类绘图函数(non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	protected void onDraw(Canvas canvas) {
		//日常处理
		finisheddraw=false;
		DrawBackGround(canvas);
		//游戏处理
		if (startdelayed>0){
			//游戏开始前
			//背景
		}else if(isend==true && enddelayed>0){
			//游戏结束后
			DrawBackGroundGame(canvas);
			//游戏信息
			ShowInformation(canvas);
			DrawBlock(canvas);
			
		}else if (isend==true && enddelayed<=0){
			//延迟结束后
			if (isWin==false) DrawText("点击屏幕返回",canvas,screenwidth/2,screenheight/2,fontsize,Color.WHITE);
		}else{
			//游戏开始时
			DrawBackGroundGame(canvas);
			//游戏信息
			ShowInformation(canvas);
			//绘制方块
			DrawBlock(canvas);
		}
		

		
		//日常处理
		//绘制粒子
		DrawParticle(canvas);
		//绘制文字粒子
		DrawTextParticle(canvas);
		//绘制图片粒子
		DrawBitmapParticle(canvas);
		ShowMenu(canvas);
		super.onDraw(canvas);
		finisheddraw=true;
	}

	/*
	 * 返回键
	 * @see android.view.View#onKeyDown(int, android.view.KeyEvent)
	 */
	public void KeyBack(){
		isMenu=!isMenu;
	}


	/*
	 * 重写触屏事件函数(non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	public boolean onTouchEvent(MotionEvent event) {
		
		
			
		int x = (int)event.getX();
		int y = (int)event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			textX = x;
			textY = y;
			if (isend==false && startdelayed<=0 && isMenu==false) SolveClick(x,y);
			if (isMenu==true) solveMenu(x,y);
			if (isend==true && isWin==false && enddelayed<=0 && isMenu==false) {
				mAcitvity.NoSaveGame(high);
				mAcitvity.GoToMainView();
			}
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			textX = x;
			textY = y;
			//if (y>=screenheight-blocksize*mapheight && isend==false && startdelayed<=0 && isMenu==false) FirstClick(x,y);
		}
		//重绘画布
		//invalidate();
		return true;
	}
	
	public void Gone(){
		isGone=true;
	}
	
	public void Stopped(){
		isGone=false;
	}
	
	public void run() {
		while (true) {
			if (isGone){
				//日常处理
				
				//粒子效果
				actParticle(20);
				//文字粒子
				actTextParticle(20);
				//图片粒子
				actBitmapParticle(20);
				//动画
				actAnimation();
				//得分
				actScore();
				
				//游戏处理
				if (startdelayed>0){
					//游戏开始前
					startdelayed-=20;
				}else if(isend==true && enddelayed>0){
					//游戏结束后
					enddelayed-=20;
				}else if (isend==true && enddelayed<=0){
					//延迟结束后
					if (isWin==true) InitGameData();
				
				}else{
					//游戏开始时
					//游戏结束
					isFinished();
					//游戏结束处理
					SolveEnd();
				}
			
				if (finisheddraw==true) postInvalidate();
			
			}
			
			try {
				Thread.sleep(20);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
