package cn.Arthur.ZZ.PopStar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Main extends Activity {
    /** Called when the activity is first created. */
    
    GameView gameview;
    MainView mainview;
    private MediaPlayer mPlayer = null;
    boolean isMainView;
    private SharedPreferences sp;
    int highscore;
    boolean isResume;
    int level;
    int score;
    int targescore;
    boolean music=true;
    private long clickTime = 0; //记录第一次点击的时间 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐去标题栏（应用程序的名字）  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐去状态栏部分(电池等图标和一切修饰部分)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sp = this.getSharedPreferences("SaveGameData", this.MODE_PRIVATE);
        highscore=sp.getInt("High",0);
        isResume=sp.getBoolean("Resume", false);
        level=sp.getInt("Level",0);
        score=sp.getInt("Score",0);
        targescore=sp.getInt("Targe",0);
        music=sp.getBoolean("Music", true);
        gameview=new GameView(this);
        gameview.Stopped();
        PlaySound(3);
        mainview=new MainView(this);
        mainview.set(highscore, isResume,music);
        isMainView=true;
        setContentView(mainview);
    }
    
    public void onPause(){  
    	gameview.Stopped();
    	mainview.Stopped();
    	super.onPause();  
    }  
    
    public void onStop(){  
    	gameview.Stopped();
    	mainview.Stopped();
    	PauseMusic();
    	super.onStop();  
    }  
    
    public void onResume(){
    	if (isMainView==true){
    		mainview.Gone();
    	}else{
    		gameview.Gone();
    	}
    	super.onResume();  
    }  
    
    public void onRestart(){
    	ResumeMusic();
    	if (isMainView==true){
    		mainview.Gone();
    	}else{
    		gameview.Gone();
    	}
    	super.onRestart();  
    }  
    
    public void onDestroy(){
    	mainview.Stopped();
    	gameview.Stopped();
    	EndMusic();
    	super.onDestroy();   
    }  
    
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		switch(keyCode)
		{
			case KeyEvent.KEYCODE_BACK:
				if (isMainView==true){
					 if ((System.currentTimeMillis() - clickTime) > 2000) {  
				            Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",Toast.LENGTH_SHORT).show();  
				            clickTime = System.currentTimeMillis();  
					 }else{
						 this.finish();
					 }
				}else{
					gameview.KeyBack();
				}
				return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void GoToMainView(){
		isMainView=true;
		PlaySound(3);
		gameview.Stopped();
		mainview.Gone();
		mainview.set(highscore, isResume,music);
		setContentView(mainview);
	}
	
	public void NewGame(){
		isMainView=false;
		PlaySound(2147483647);
		mainview.Stopped();
		gameview.Gone();
		gameview.InitNewGame(0, 0, 0,highscore);
		setContentView(gameview);
	}
	
	public void ResumeGame(){
		isMainView=false;
		PlaySound(2147483647);
		mainview.Stopped();
		gameview.Gone();
		gameview.InitNewGame(score,level, targescore,highscore);
		setContentView(gameview);
	}
	
	public void SaveGame(int s,int l,int ts,int h){
		if (h>highscore) highscore=h;
		isResume=true;
		score=s;
		level=l;
		targescore=ts;
		save();
	}
	
	public void NoSaveGame(int h){
		if (h>highscore) highscore=h;
		isResume=false;
		save();
	}
	
	public void setMusic(){
		music=!music;
		if (mPlayer!=null){
			if (music==false){
				mPlayer.pause();
			}else{
				mPlayer.start();
			}
		}
		save();
	}
	public void save(){
		sp.edit().putBoolean("Music", music).commit();
		sp.edit().putInt("Level", level).commit();
		sp.edit().putInt("Score",score).commit();
		sp.edit().putInt("Targe",targescore).commit();
		sp.edit().putInt("High",highscore).commit();
		sp.edit().putBoolean("Resume", isResume).commit();
	}
	
	
	public void PauseMusic(){
		if (mPlayer!=null){
			mPlayer.pause();
		}
	}
	
	public void ResumeMusic(){
		if (mPlayer!=null){
			mPlayer.start();
		}
	}
	
	public void EndMusic(){
		if (mPlayer!=null){
			mPlayer.stop();
			mPlayer.release();
		}
	}
	
	/**
	 * 播放声音
	 */
	public void PlaySound(int index){
		if (mPlayer!=null){
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
		switch(index){
		case 1:
			mPlayer = MediaPlayer.create(this, R.raw.select);
			mPlayer.setLooping(false);
			if (music) mPlayer.start();
			break;
		case 2:
			mPlayer = MediaPlayer.create(this, R.raw.pop_star);
			mPlayer.setLooping(false);
			if (music) mPlayer.start();
			break;
		case 3:
			mPlayer = MediaPlayer.create(this, R.raw.ib);
			mPlayer.setLooping(true);
			if (music) mPlayer.start();
			break;
		case 4:
			mPlayer = MediaPlayer.create(this, R.raw.victory);
			mPlayer.setLooping(false);
			if (music) mPlayer.start();
			break;
		case 5:
			mPlayer = MediaPlayer.create(this, R.raw.defeat);
			mPlayer.setLooping(false);
			if (music) mPlayer.start();
			break;
		}
	}
	
}