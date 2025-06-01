package finalProject;

import java.util.Random;

import acm.graphics.GImage;
import acm.graphics.GRect;

public class EnemyAircraft extends Armament {

	
	private GImage body;
	private Random random;
	private int direction;//-1=left, 1=right
	private double velocity;
	private double defaultX;
	private double defaultY;
	private int health=4;
	private int type;
	private int explosionAnimationCount;
	GImage[] explosionArray=new GImage[20];
	private boolean soundPlayed;

	public EnemyAircraft(){
		random=new Random();
		this.type=random.nextInt(3);
		this.direction=(int)Math.pow(-1, random.nextInt(2));//potential problem?

		String name="";
		switch(this.type){
		case 0: name+="bomber";break;
		case 1: name+="bigbomber";break;
		case 2: name+="jetfighter";
		}
		
		if(direction==1){
			body=new GImage(name+"R.png");
			defaultX=-body.getWidth();
		}else{
			body=new GImage(name+"L.png");
			defaultX=640;
		}
		
		this.add(body);
		defaultY=random.nextInt(220)+30;
		velocity=(random.nextInt(3)+2)*direction;
		
		for(int i=0;i<explosionArray.length;i++){		
			explosionArray[i]=new GImage("explosion"+i+".png",0,0);
		}
	}
	
	public void move() {
		if (health<=0){
			this.explode();
		}else{
			this.setLocation(this.getX()+velocity,this.getY());	
			super.checkOutOfBound();
		}
	}

	public double getDefaultX(){
		return defaultX;
	}
	
	public double getDefaultY(){
		return defaultY;
	}
	
	public int getType(){
		return this.type;
	}
	
	public int getDirection(){
		return this.direction;
	}
	
	public boolean ableToFire(){
		if (health>0)
			return true;
		return false;
	}
	
	public void isHit(){
		this.health--;
	}

	public void explode(){
		this.removeAll();
		if (!soundPlayed){
			switch(this.getType()) {
			case 0: 
				Sound middleExplode = new Sound("middleexplode.wav");
				middleExplode.play();
				break;
			case 1:
				Sound bigExplode = new Sound("bigexplode.wav");
				bigExplode.play();
				break;
			case 2:
				Sound smallExplode = new Sound ("smallExplode.wav");
				smallExplode.play();
				break;
			}
			this.soundPlayed=true;
		}
		this.add(explosionArray[explosionAnimationCount]);
		explosionArray[explosionAnimationCount].setLocation(this.body.getX()-30,this.body.getY()-50);
		explosionAnimationCount++;
		if (explosionAnimationCount>explosionArray.length-1)
			this.setTerminated(true);
	}
	
	public double getXVelocity() {
		return this.velocity;
	}
}
