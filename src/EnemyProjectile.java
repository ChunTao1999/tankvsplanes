package finalProject;

import acm.graphics.GImage;
import acm.graphics.GRect;

public class EnemyProjectile extends Armament {

	private double velocityX;
	private final double velocityY=5;
	private GImage[] bombArray=new GImage[10];
	private int timeCount;
	private int bombNum;
	private int type;
	private int direction;
	
	public EnemyProjectile(EnemyAircraft enemyAircraft){
		
		this.type=enemyAircraft.getType();
		this.direction=enemyAircraft.getDirection();
		this.velocityX=enemyAircraft.getXVelocity();
		String name="";

		switch(this.type){
		case 0: name+="dumbbomb";break;
		case 1: name+="dumbbomb";break;
		case 2: name+="dumbbomb";
		}
		
		if(direction==1)
			name+="R";
		else
			name+="L";
		
		for(int i=0;i<bombArray.length;i++){		
			bombArray[i]=new GImage(name+i+".png",0,0);
		}

	}

	public void move(){
		this.setLocation(this.getX()+velocityX,this.getY()+velocityY);
		if (bombNum<bombArray.length){
			if (timeCount%2==1)
				this.phase();
			timeCount++;
		}
		this.checkOutOfBound();
		super.checkOutOfBound();

	}
	
	public void phase(){
		if(bombNum>0)
			this.remove(bombArray[bombNum-1]);
		this.add(bombArray[bombNum]);
		bombNum++;
	}
	
	public void checkOutOfBound() {
		if (this.getY()>480)
			this.setTerminated(true);
	}
	
	
	
}
