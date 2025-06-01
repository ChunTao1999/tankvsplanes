package finalProject;

import java.awt.Color;
import java.io.File;

import acm.graphics.GCanvas;
import acm.graphics.GCompound;
import acm.graphics.GImage;
import acm.graphics.GOval;
import acm.graphics.GRect;

public class Bullet extends Armament {

	private final double velocity=8;
	private double tan;
	private double velocityX;
	private double velocityY;
	private double angle;
	private int shellNum;
	private GImage[] bulletArray=new GImage[21];
	
	public Bullet(double tan){

		for(int i=0;i<bulletArray.length;i++){		
			bulletArray[i]=new GImage("shell"+i+".png",0,0);
		}	
		
		this.tan=tan;
		this.velocityY=-Math.sqrt(velocity*velocity/(tan*tan+1));
		this.velocityX=-this.velocityY*tan;
		this.setAngle();
		this.add(this.bulletArray[shellNum]);
		
	}

	public void move(){
		this.setLocation(this.getX()+velocityX,this.getY()+velocityY);
		super.checkOutOfBound();//can this override polymorphism?
		this.checkOutOfBound();
	}
	
	public void setAngle(){
		this.angle=Math.toDegrees(Math.atan(this.tan));
		double shellnumber=(this.angle+90.0)/(180.0/this.bulletArray.length);
		//this.shellNum=(int)Math.floor(shellnumber);
		this.shellNum=(int)shellnumber;
		if (this.shellNum==21)
			shellNum=20;
	}

	public void checkOutOfBound() {
		if (this.getY()<-this.getHeight())
			this.setTerminated(true);
	}
	
	
	
}
