package logic;

public class Pipe {
	public double width;
	public double x;
	public double vy;
	public double openingY;
	public double openingSize;
	
	public Pipe(double width, double x, double openingY, double openingSize, double vy) {
		this.width = width;
		this.x = x;
		this.openingY = openingY;
		this.openingSize = openingSize;
		this.vy = vy;
	}
	
	public boolean collision(Bird bird) {
		if(bird.x > (this.x-this.width/2-bird.r) && bird.x < (this.x+this.width/2+bird.r)) {
			if(bird.y < (this.openingY-this.openingSize/2+bird.r)) {
				return true;
			}
			
			if(bird.y > (this.openingY+this.openingSize/2-bird.r)) {
				return true;
			}
		}
		
		return false;
	}
}
