package finalProject;

import acm.graphics.GCompound;
import acm.graphics.GImage;

public abstract class Armament extends GCompound {

	//can move, can be destroyed
	private boolean terminated;

	public abstract void move();
	
	public boolean isTerminated() {
		return terminated;
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}

	public void checkOutOfBound() {
		if (this.getX()>640)
			this.setTerminated(true);
		if (this.getX()<-this.getWidth())
			this.setTerminated(true);
		if (this.getY()<-this.getHeight())
			this.setTerminated(true);
	}
	
	
}
