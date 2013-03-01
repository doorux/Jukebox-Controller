package jukeController;
import java.awt.Canvas;
import java.io.File;
import java.util.ArrayList;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class Controller {
	private static EmbeddedMediaPlayer mediaPlayer;
	private static JukeViewer view;
	private static QueueThing playerQueue;
	//private boolean loop;
	
	public Controller(){
		//loop = false;
		playerQueue = new QueueThing();
		view = new JukeViewer(this);
		mediaPlayer = view.getMediaPlayer();

		
		mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter(){
			public void finished(MediaPlayer mediaPlayer){
				//if(loop){
					next();				
				//}
			}
		});
		
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.release();		
	}
	
	public void play(){
		mediaPlayer.play();
	}
	
	public void playNow(int index){
		mediaPlayer.playMedia(playerQueue.getFileLocation(index));
	}
	
	public void pause(){
		mediaPlayer.pause();
	}
	
	public void stop(){
		if(mediaPlayer.isPlaying()){
			mediaPlayer.stop();
			//mediaPlayer.release();
		}
	}
	
	public void resizeFixVideo(Canvas vs){
		float posit = mediaPlayer.getPosition();
		mediaPlayer.stop();
		mediaPlayer.setVideoSurface(vs);
		mediaPlayer.play();
		mediaPlayer.setPosition(posit);
	}
	
	public void playMedia(String m){
		mediaPlayer.playMedia(m);
	}
	
	public void menuActivate(){
		mediaPlayer.menuActivate();
	}
	
	public void togglePlaylist(){
		view.togglePlaylist();
	}
	public void queueMedia(File[] m){
		
		playerQueue.push(m);
		updatePlaylist();
		if(!mediaPlayer.isPlaying()){
			playMedia(playerQueue.pop());
		}
	}
	
	public void next(){
		stop();
		if(!playerQueue.isEmpty()){
			mediaPlayer.playMedia(playerQueue.next());
		}
		
	}
	public void back(){
		stop();
		if(!playerQueue.isEmpty()){
			mediaPlayer.playMedia(playerQueue.previous());
		}
	}
	/*
	public void setLoop(){
		loop = true;
	}
	
	public void unsetLoop(){
		loop = false;
	}
	
	public boolean isLoop(){
		return loop;
	}
	*/
	public void openMenu(){
		view.openMenu();
	}
	
	public void focusFrame(){
		view.focusFrame();
	}
	
	public void updatePlaylist(){
		view.updatePlaylist();
	}
	
	public String[] getPlaylist(){
		return playerQueue.getPlaylist();
	}
	
}
