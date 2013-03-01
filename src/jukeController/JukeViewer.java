package jukeController;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import uk.co.caprica.vlcj.player.*;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;

public class JukeViewer {
	private EmbeddedMediaPlayer mediaPlayer;

	//buttons
	private static JButton pause;
	private static JButton open;
	private static JButton stop;
	private static JButton next;
	private static JButton back;
	private static JButton exit;
	//private static JButton loop;
	private static JukeListener listener;
	private String currentDir;
	private Frame f;
	private JScrollPane jsp;
	private JList playlist;
	private Controller c;
	private boolean showPlaylist;
	private Font theFont;
	private Color bgColor;
	private Color buttonColor;
	private FullScreenStrategy fullScreenStrategy;
	private Canvas vs;
	private Font jlistFont;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public JukeViewer(Controller c) {
		
		//initialize variables
		File AirStreamLoc = new File(System.getProperty("custom.font.path")+"\\AIRSTREA.TTF");
		File FabulousLoc = new File(System.getProperty("custom.font.path")+"\\FABULOUS.TTF");
		this.c = c;
		f = new Frame("JukeManager");
		f.setUndecorated(true);

		listener = new JukeListener(c);
		showPlaylist = true;
		playlist = new JList(formatJList(c.getPlaylist()));
		playlist.addMouseListener(mouseListener);
		jsp = new JScrollPane(playlist);
		jsp.setPreferredSize(new Dimension(200, jsp.getHeight()));
		jsp.setHorizontalScrollBar(jsp.createHorizontalScrollBar());
		jsp.setVerticalScrollBar(jsp.createVerticalScrollBar());
		JPanel controls = new JPanel();
		JPanel menus = new JPanel();
		menus.setLayout(new BorderLayout());
		buttonColor = new Color(255, 60, 60);
	    UIManager.put("Button.background", buttonColor);
	    UIManager.put("Scrollpane.border", 12);
//	    UIManager.put("Panel.background", Color.blue);
//	    UIManager.put("Frame.background", Color.blue);
	    
	    f.setBackground(new Color(50, 200, 200));
	   
	    menus.setBackground(new Color(50, 200, 200));
	    controls.setBackground(new Color(50, 200, 200));
	    
	    try {
			theFont = Font.createFont(Font.TRUETYPE_FONT, AirStreamLoc);
			theFont = theFont.deriveFont((float)(20));
			jlistFont = Font.createFont(Font.TRUETYPE_FONT, FabulousLoc);
			jlistFont = jlistFont.deriveFont((float)(20));
		} catch (FontFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		bgColor = Color.BLUE;
		
	    UIManager.put("Button.font", theFont);
	    UIManager.put("JPanel.foreground", bgColor);
		menus.setLayout(new FlowLayout());
		currentDir = null;

		f.setSize(800, 600);
		f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		f.addKeyListener(new JukeKeyListener(c));
		//f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(new BorderLayout());
		vs = new Canvas();
		f.add((vs), BorderLayout.CENTER);
		controls.setLayout(new FlowLayout());
		f.add(jsp, BorderLayout.EAST);		
		back = new JButton("Back");
		back.addActionListener(listener);
		controls.add(back);
		next = new JButton("Next");
		next.addActionListener(listener);
		controls.add(next);
		pause = new JButton("Play/Pause");
		pause.addActionListener(listener);
		controls.add(pause);
		stop = new JButton("Stop");
		stop.addActionListener(listener);
		controls.add(stop);
	/*	loop = new JButton("Loop All");
		loop.addActionListener(listener);
		controls.add(loop); */
		open = new JButton("Open...");
		open.addActionListener(listener);
		menus.add(open, BorderLayout.WEST);
		exit = new JButton("Exit");
		exit.addActionListener(listener);
		menus.add(exit, BorderLayout.EAST);
		f.add(menus, BorderLayout.NORTH);
		f.add(controls, BorderLayout.SOUTH);
		
		  fullScreenStrategy = new DefaultFullScreenStrategy(f);
		  fullScreenStrategy.enterFullScreenMode();

		f.setVisible(true);
		focusFrame();
		MediaPlayerFactory factory = new MediaPlayerFactory(new String[] {});
		mediaPlayer = factory.newMediaPlayer(fullScreenStrategy);
		mediaPlayer.setVideoSurface(vs);
		//mediaPlayer.setStandardMediaOptions(":sout=#duplicate{dst=std{access=http,mux=ts,dst=127.0.0.1:5555}}");
		factory.release();
	}

	public void setSelected(int index){
		playlist.setSelectedIndex(index);
	}
	
	public EmbeddedMediaPlayer getMediaPlayer(){
		return mediaPlayer;
	}
	
	public void togglePlaylist(){
		showPlaylist = !showPlaylist;
		if(showPlaylist){
			
		}
	}
	
	public void updatePlaylist(){
		//f.setVisible(false);
		f.remove(jsp);
		playlist = new JList(formatJList(c.getPlaylist()));
		playlist.setFont(jlistFont);
		playlist.addMouseListener(mouseListener);
		jsp = new JScrollPane(playlist);
		jsp.setPreferredSize(new Dimension(200, jsp.getHeight()));
		jsp.setHorizontalScrollBar(jsp.createHorizontalScrollBar());
		jsp.setVerticalScrollBar(jsp.getVerticalScrollBar());
		f.add(jsp, BorderLayout.EAST);
		//f.setVisible(true);
		f.validate();
	}

	public void openMenu(){
		JFileChooser fc = new JFileChooser(currentDir, FileSystemView.getFileSystemView());
		fc.setMultiSelectionEnabled(true);
		//int ret = fc.showDialog(null, "Open...");
		int ret = fc.showDialog(f, "Open...");
		if(ret == JFileChooser.APPROVE_OPTION){
			c.queueMedia(fc.getSelectedFiles());
			//mediaPlayer.playMedia(fc.getSelectedFile().getAbsolutePath());
			//currentDir = fc.getCurrentDirectory().getAbsolutePath();
		}else if(ret == JFileChooser.CANCEL_OPTION){
		}
		
	}
	
	public void focusFrame(){
		f.requestFocus();
	}

	public String[] formatJList(String[] playlist){
		String[] newList = playlist;
		for(int i=0; i<newList.length; i++){
			newList[i] = "<html><em><font style=\"color:rgb(100,100,255); font-weight:bold;\">" + 
						playlist[i] +"</font></em></html>";
		}
		return newList;
	}
	
	public class JukeListener implements ActionListener{
		private Controller c;
		public JukeListener(Controller c){
			this.c = c;
		}
		public void actionPerformed(ActionEvent e){
			if (e.getSource()==JukeViewer.pause){
				c.pause();
			}else if (e.getSource()==JukeViewer.open){
				c.openMenu();
			}else if(e.getSource() == JukeViewer.stop){
				c.stop();
			}else if(e.getSource() == JukeViewer.next){
				c.next();
			}else if(e.getSource() == JukeViewer.back){
				c.back();
			}else if(e.getSource() == JukeViewer.exit){
				System.exit(0);
			}/*else if(e.getSource() == JukeViewer.loop){
				if(c.isLoop()){
					c.unsetLoop();
					System.out.println("No Looping");
				}else{
					c.setLoop();
					System.out.println("Looping");
				}
			}*/
			c.focusFrame();
		}
		
	}
	public class JukeKeyListener implements KeyListener{
		private Controller c;
		public JukeKeyListener(Controller c){
			this.c = c;
		}
		@Override
		public void keyPressed(KeyEvent k) {
			// TODO Auto-generated method stub
			if(k.getKeyChar() == ' '){
				c.pause();
			}else if((k.getKeyChar() == 'n')){
				c.next();
			}else if(k.getKeyChar() == 'b'){
				c.back();
			}else if(k.getKeyChar() == 'm'){
				c.menuActivate();
			}else if(k.getKeyChar() == 'f'){
				if(fullScreenStrategy.isFullScreenMode()){
					f.dispose();
					fullScreenStrategy.exitFullScreenMode();
					f.setUndecorated(false);
					f.setVisible(true);
					c.resizeFixVideo(vs);

				}else{
					f.dispose();
					f.setUndecorated(true);
					fullScreenStrategy.enterFullScreenMode();
					f.setVisible(true);
					c.resizeFixVideo(vs);
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent k) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent k) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	MouseListener mouseListener = new MouseAdapter(){
		public void mouseClicked(MouseEvent e){
			//if(e.getClickCount()==2){
				int index = playlist.locationToIndex(e.getPoint());
				if(index >= 0){
					c.playNow(index);
				}
				System.out.println(playlist.locationToIndex(e.getPoint()));
			//}
		}
	};
}
