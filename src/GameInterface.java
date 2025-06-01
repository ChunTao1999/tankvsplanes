package finalProject;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import acm.graphics.GCanvas;
import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

public class GameInterface extends GraphicsProgram {
	
	public final int WINDOW_WIDTH=640;
	public final int WINDOW_HEIGHT=480+100;
	private final GImage sky=new GImage("sky.jpg");
	private final GImage bg1=new GImage("bg1.png",0,180);
	private final GImage bg2=new GImage("bg2.png",0,180);
	private final GImage ground=new GImage("ground.png",0,420);
	private final GImage track=new GImage("tracks.png",0,458);
	private final GImage pause=new GImage("pause.png");
	private final GImage gameStarts=new GImage("survivalmode.png");
	
	private Tank tank=new Tank();
	private Timer frameControl;	
	private Timer nukeControl;
	private JTextArea textArea;
	private int nukeAnimationCount;
	private JButton playBGM;
	private boolean soundLoaded;
	private Clip clip;
	private int gameOverCount;
	
	private int frameCount;
	private double tan;
	private int mouseX;
	private int mouseY;
	private boolean running=true;
	private int nukeCount;
	
	
	GImage[] nukeArray = new GImage[9];

	public void setTan(){
		this.tan=(this.mouseX-this.tank.getX()-this.tank.getWidth()/2)/(this.WINDOW_HEIGHT-this.mouseY-45);
	}
	
	public void tankFire(){
		Bullet bullet=new Bullet(tan);
		GameInterface.this.add(bullet);
		double yOffset=Math.sqrt((39*39)/(this.tan*this.tan+1));
		double xOffset=yOffset*this.tan;
		bullet.setLocation(tank.getX()+tank.getWidth()/2-bullet.getWidth()/2+xOffset, 435-bullet.getHeight()/2-yOffset);
		Sound tankFiring = new Sound("cached_tankfire2.wav");
		tankFiring.play();
	}
	
	public void backgroundMove(){
		this.bg1.setLocation(this.bg1.getX()-1,this.bg1.getY());
		this.bg2.setLocation(this.bg2.getX()-2,this.bg2.getY());
		this.ground.setLocation(this.ground.getX()-4,this.ground.getY());
		if(this.frameCount%960==0)
			this.bg1.setLocation(0,this.bg1.getY());
		if(this.frameCount%480==0)
			this.bg2.setLocation(0,this.bg2.getY());
		if(this.frameCount%160==0)
			this.ground.setLocation(0,this.ground.getY());
	}
	
	public void pause(){
		if (this.running){				
			this.frameControl.stop();					
			this.running=false;					
			this.add(pause);				
		}				
		else{				
			this.frameControl.start();				
			this.running=true;				
			this.remove(pause);
		}
	}
	
	public void nuke() {
		GameInterface.this.add(nukeArray[nukeAnimationCount]);
		nukeAnimationCount++;
		if (nukeAnimationCount > 8) {
			for (int i = 0; i < 9; i++) {
				GameInterface.this.remove(nukeArray[i]);
			}
			nukeControl.stop();
			nukeAnimationCount = 0;
		}
	}
	
	
	public void detectCollision(){
		
		ArrayList<Bullet> bulletArray=new ArrayList<Bullet>();
		ArrayList<EnemyAircraft> enemyAircraftArray=new ArrayList<EnemyAircraft>();
		ArrayList<EnemyProjectile> enemyProjectileArray=new ArrayList<EnemyProjectile>();
		int numElement=GameInterface.this.getElementCount();

		for(int i=0;i<numElement;i++){
			GObject element=GameInterface.this.getElement(i);
			if(element instanceof Bullet)
				bulletArray.add((Bullet)element);
			if(element instanceof EnemyAircraft)
				enemyAircraftArray.add((EnemyAircraft)element);
			if(element instanceof EnemyProjectile)
				enemyProjectileArray.add((EnemyProjectile)element);
		}
		
		for(int i=0;i<enemyAircraftArray.size();i++){
			for (int j=0;j<bulletArray.size();j++){
				if ((enemyAircraftArray.get(i)).getBounds().intersects((bulletArray.get(j)).getBounds())){							
					(enemyAircraftArray.get(i)).isHit();
					bulletArray.get(j).setTerminated(true);									
				}
			}
		}
		
		for(int i=0;i<enemyProjectileArray.size();i++){
			for (int j=0;j<bulletArray.size();j++){
				if ((enemyProjectileArray.get(i)).getBounds().intersects((bulletArray.get(j)).getBounds())){							
					enemyProjectileArray.get(i).setTerminated(true);
					bulletArray.get(j).setTerminated(true);
					Sound cancel = new Sound("cached_tractorbeam.wav");
					cancel.play();
				}
			}
		}
		
		
		for(int i=0;i<enemyProjectileArray.size();i++){
			if ((enemyProjectileArray.get(i)).getBounds().intersects(GameInterface.this.tank.getBounds())){							
				enemyProjectileArray.get(i).setTerminated(true);
				this.tank.isHit();
			}
		}
		
		
	}
	
	public void deployEnemyAircraft(){
		EnemyAircraft enemyAircraft=new EnemyAircraft();
		this.add(enemyAircraft);
		enemyAircraft.setLocation(enemyAircraft.getDefaultX(), enemyAircraft.getDefaultY());
	}
	
	public void enemyReady(){
		int numElement=GameInterface.this.getElementCount();
		for(int i=0;i<numElement;i++){
			GObject element=GameInterface.this.getElement(i);
			if(element instanceof EnemyAircraft)
				if(GameInterface.this.frameCount%50==0)
					if(((EnemyAircraft)element).ableToFire())
						GameInterface.this.enemyFire(((EnemyAircraft)element));
		}
	}
	
	public void enemyFire(EnemyAircraft enemy){
		EnemyProjectile dumbBomb=new EnemyProjectile(enemy);
		this.add(dumbBomb);
		dumbBomb.setLocation(enemy.getX()+enemy.getWidth()/2,enemy.getY()+enemy.getHeight());
		Sound bombFall = new Sound ("bombfall.wav");
		bombFall.play();
	}
		
	public void recycle(){
		for(int i=0;i<GameInterface.this.getElementCount();i++){
			GObject element=GameInterface.this.getElement(i);
			if (element instanceof Armament){
				if (((Armament)element).isTerminated()){
					GameInterface.this.remove(element);
					i--;
				}
			}
		}
	}
	
	public void moveElement(){
		int numElement=GameInterface.this.getElementCount();
		for(int i=0;i<numElement;i++){
			GObject element=GameInterface.this.getElement(i);
			if(element instanceof Armament)
				((Armament)element).move();						
		}
	}
	
	public void gameOver(){
		if (this.gameOverCount==50){
			GImage gameOver=new GImage("gameover.png");
			this.add(gameOver);
			Sound gameover = new Sound ("gameover.wav");
			gameover.play();
		}
		gameOverCount++;
	}
	
	public void gameStarts(){
		if(this.frameCount==0)
			this.add(gameStarts);
		if(this.frameCount==100)
			this.remove(gameStarts);
	}
	
	
	public void init(){ 
		
		this.setSize(this.WINDOW_WIDTH, this.WINDOW_HEIGHT);
		this.add(sky);
		this.add(bg1);
		this.add(bg2);
		this.add(ground);
		this.add(track);
		this.add(tank);
		tank.setLocation(-80,420);
		
		for(int i=0;i<nukeArray.length;i++){		
			nukeArray[i]=new GImage("nuke"+i+".jpg",0,0);
		}
		
		this.textArea = new JTextArea(4, 35);
		this.textArea.setText("Click or hold the LEFT mouse button to fire.\n"+"The tank will follow the cursor.\n"
		+ "Press SPACE to pause the game. Press again to continue.\n"+ "Press C to unleash a nuclear bomb! Remember, you can only use the nuclear bomb once!");
		this.textArea.setLineWrap(true);
		this.textArea.setWrapStyleWord(true);
		this.add(textArea, NORTH);
		
				
		//Keyboard listener - pause & nuke
		this.addKeyListeners(new KeyListener(){
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode()==32)									
					GameInterface.this.pause();
				else if (e.getKeyCode()==67) {
					nukeCount++;
					Sound nuke = new Sound("cached_nukeblast.wav");
					nuke.play();
					int numElement=GameInterface.this.getElementCount();
					for(int i=0;i<numElement;i++){
						GObject element=GameInterface.this.getElement(i);
						if((element instanceof EnemyProjectile)||(element instanceof EnemyAircraft)) {
							((Armament)element).removeAll();
							if((element instanceof EnemyAircraft))
								((EnemyAircraft)element).setTerminated(true);
						}
					}
					nukeControl = new Timer(50, new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							GameInterface.this.nuke();
					}});
					nukeControl.start();
				}
			}
			public void keyReleased(KeyEvent e){}
			public void keyTyped(KeyEvent e){}
		});
		this.getGCanvas().requestFocus();
		
		//MouseListener - check firing status
		this.getGCanvas().addMouseListener(new MouseListener(){
			public void mousePressed(MouseEvent e) {
				tank.ready(true);
			}
			public void mouseReleased(MouseEvent e){
				tank.ready(false);
				tank.intervalReset();
			}
			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
		});
		
		//MouseMotionListener - get mouse location
		this.getGCanvas().addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				mouseX=e.getX();
				mouseY=e.getY();
				if(mouseY>435)
					mouseY=435;
			}		
			public void mouseMoved(MouseEvent e) {	
				mouseX=e.getX();
				mouseY=e.getY();
				if(mouseY>434)
					mouseY=434;
			}
		});
		
		//mater control
		frameControl=new Timer(25,new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				GameInterface.this.gameStarts();
				GameInterface.this.backgroundMove();
				GameInterface.this.setTan();
				GameInterface.this.tank.setVelocity(GameInterface.this.tan*8);
				
				if((tank.ableToFire())&&(nukeCount<=1)){
					tank.continousFire();	
					if (tank.isReady())
						GameInterface.this.tankFire();	
					if (GameInterface.this.frameCount%150==149)
						GameInterface.this.deployEnemyAircraft();		
					GameInterface.this.enemyReady();
					GameInterface.this.detectCollision();

				}else 
					GameInterface.this.gameOver();
				
				GameInterface.this.moveElement();
				GameInterface.this.recycle();
				
				GameInterface.this.frameCount++;
				
			}
		});
		frameControl.start();
		
		
		try {
	         File file = new File("bgm.wav");
	         AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
	         clip = AudioSystem.getClip();
	         clip.open(audioIn);
	         soundLoaded = true;
	      } 
	      catch (UnsupportedAudioFileException e) {
	         soundLoaded = false; 
	         e.printStackTrace();
	      } 
	      catch (IOException e) {
	         soundLoaded = false; 
	         e.printStackTrace();
	      } 
	      catch (LineUnavailableException e) {
	         soundLoaded = false; 
	         e.printStackTrace();
	      }
		playBGM = new JButton("Play or Stop BGM!");
		clip.loop(Clip.LOOP_CONTINUOUSLY);  // repeat forever;
		playBGM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (soundLoaded) {
               	if (clip.isRunning())
                       clip.stop();   // Stop the player if it is still running
               	else {
               		clip.setFramePosition(0); // rewind to the beginning
               		clip.loop(Clip.LOOP_CONTINUOUSLY);  // repeat forever;		
               	}
				}
			}});
		this.getRegionPanel(NORTH).add(playBGM);
		
		Sound prepare = new Sound("prepare.wav");
		prepare.play();
		
	}
	
	public class Tank extends Armament implements Vehicle {

		private double velocity=100;
		private boolean ready=false;
		private int firingCount=0;
		private int explosionAnimationCount;
		private boolean tankIsHit;	
		private GImage shadow=new GImage("tankshadow.png",0,39);
		private GImage[] bodyArray=new GImage[10];
		private GImage[] gunArray=new GImage[21];
		private GImage[] explosionArray=new GImage[20];
		private boolean soundPlayed;
		
		public Tank(){
			this.add(shadow);
			for(int i=0;i<gunArray.length;i++){			
				gunArray[i]=new GImage("gun"+i+".png",0,-25);
				this.add(this.gunArray[i]);
			}	
			for(int i=0;i<bodyArray.length;i++){
				bodyArray[i]=new GImage("tank"+i+".png",0,0);
				this.add(this.bodyArray[i]);	
			}
			
			for(int i=0;i<explosionArray.length;i++){		
				explosionArray[i]=new GImage("explosion"+i+".png");
			}
		}

		public boolean ableToFire() {
			if (tankIsHit)
				return false;
			return true;
		}

		public void setVelocity(double velocity){
			if (velocity>8)
				this.velocity=8;
			else
				this.velocity=velocity;
		}
		
		public void move(){
			if(this.tankIsHit){
				this.explode();
			}
			else{
				this.phase();
				this.setLocation(this.getX()+velocity,this.getY());
				this.checkOutOfBound();
			}
		}
		
		public void ready(boolean ready){
			this.ready=ready;
		}
		
		public void continousFire(){
			firingCount++;
		}
		
		public boolean isReady(){
			if ((firingCount%9==1)&&this.ready)
				return this.ready;
			return false;
		}

		public void isHit(){
			this.tankIsHit=true;
		}
		
		public void intervalReset(){
			this.firingCount=0;
		}
		
		public void explode(){
			this.removeAll();
			Sound tankExplode = new Sound("cached_tankexplode.wav");
			if (!soundPlayed){
				tankExplode.play();
				this.soundPlayed=true;
			}
			this.add(explosionArray[explosionAnimationCount]);
			explosionArray[explosionAnimationCount].setLocation(-40,-50);
			explosionAnimationCount++;
			if (explosionAnimationCount>explosionArray.length-1)
				this.setTerminated(true);
		}
		
		public void phase(){
			int status=(GameInterface.this.frameCount)%10;
			for(int i=0;i<bodyArray.length;i++){
				if(i==status)
					bodyArray[i].setVisible(true);
				else
					bodyArray[i].setVisible(false);
			}
			
			double fireAngle=Math.toDegrees(Math.atan(GameInterface.this.tan));
			double gunnumber=(fireAngle+90.0)/(180.0/this.gunArray.length);
			int gunNum=(int)gunnumber;

			for(int i=0;i<gunArray.length;i++){
				if (i==gunNum)
					gunArray[i].setVisible(true);
				else
					gunArray[i].setVisible(false);
			}
				
		}

		public void checkOutOfBound() {
			if ((this.getX()>=640-this.getWidth()))
				this.setLocation(this.getX()-this.velocity,this.getY());
		}
			
	}


}
