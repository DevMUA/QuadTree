package sonc.quad;

import java.util.Set;
/**
 * 
 * Abstract class common to all classes implementing the trie structure. Defines methods required by those classes and provides general methods for checking overlaps and computing distances. This class corresponds to the Component in the Composite design pattern.
 *
 * @param <T> Type that extends interface HasPoint
 */
public abstract class Trie <T extends HasPoint> {
	protected double bottomRightX;
	protected double bottomRightY;
	protected double topLeftX;
	protected double topLeftY;
	private static int capacity;
	protected Quadrant q;

	protected Trie(double topLeftX,double topLeftY,double bottomRightX,double bottomRightY){
		this.bottomRightX=bottomRightX;
		this.bottomRightY=bottomRightY;
		this.topLeftX=topLeftX;
		this.topLeftY=topLeftY;
	}
	
	
	/**
	 * Collect all points in this node and its descendants in given set
	 * @param points set of HasPoint for collecting points
	 */
	abstract void collectAll(Set<T> points);
	
	/**
	 * Collect points at a distance smaller or equal to radius from (x,y) and place them in given list
	 * @param x coordinate of point
	 * @param y coordinate of point
	 * @param radius from given point
	 * @param points set for collecting points
	 */
	abstract void collectNear(double x, double y, double radius,Set<T> points);
	
	/**
	 * Insert given point
	 * @param point to be inserted
	 * @return changed parent node
	 */
	abstract Trie<T> insert(T point);
	
	/**
	 * Insert given point, replacing existing points in same location
	 * @param point to be inserted
	 * @return changed parent node
	 */
	abstract Trie<T> insertReplace(T point);
	
	/**
	 * Delete given point
	 * @param point to delete
	 */
	abstract void delete(T point);
	
	/**
	 * Find a recorded point with the same coordinates of given point
	 * @param point with requested coordinates
	 * @return recorded point, if found; null otherwise
	 */
	abstract T find(T point);
	
	/**
	 * Check if overlaps with given circle
	 * @param x coordinate of circle
	 * @param y coordinate of circle
	 * @param radius of circle
	 * @return true if overlaps and false otherwise
	 */
	boolean overlaps(double x,double y,double radius){
		double rectCenterX = ((bottomRightX-topLeftX)/2)+topLeftX;
		double rectCenterY = ((topLeftY-bottomRightY)/2)+bottomRightY;
		double rectWidth = bottomRightX-topLeftX;
		double rectHeight = topLeftY-bottomRightY;
		double circleDistanceX= Math.abs(x-rectCenterX);
		double circleDistanceY= Math.abs(y-rectCenterY);
		
		if(circleDistanceX > (rectWidth/2 + radius))
			return false;
		if(circleDistanceX > (rectHeight/2 + radius))
			return false;
		
		if(circleDistanceX <= (rectWidth/2))
			return true;
		if(circleDistanceX <= (rectHeight/2))
			return true;
		
		double cornerDistance = ((circleDistanceX - rectWidth/2)*(circleDistanceX - rectWidth/2) +
				(circleDistanceY - rectHeight/2)*(circleDistanceY - rectHeight/2));
		
		return (cornerDistance <= (radius*radius));
		
	}
	
	/**
	 * Get capacity of a bucket
	 * @return capacity
	 */
	public static int getCapacity() {
		return capacity;
	}
	
	/**
	 * Set capacity of a bucket
	 * @param capacity of bucket
	 */
	public static void setCapacity(int capacity) {
		Trie.capacity = capacity;
	}

	/**
	 * Euclidean distance between two pair of coordinates of two points
	 * @param x1 x coordinate of first point
	 * @param y1 y coordinate of first point
	 * @param x2 x coordinate of second point
	 * @param y2 y coordinate of second point
	 * @return distance between given points
	 */
	public static double getDistance(double x1,double y1,double x2, double y2){
		return Math.sqrt(((x1-x2)*(x1-x2)) + ((y1-y2)*(y1-y2)));
	}
	
	/**
	 * Tests if point is inside rectangle.
	 * @param point to be tested
	 * @return true if inside, false if not.
	 */
	protected boolean isInsideRectangle(T point){
		if(((point.getX()>=topLeftX && point.getX() <= bottomRightX) &&
				(point.getY() <= topLeftY && point.getY() >= bottomRightY))){
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Tests if point is inside rectangle
	 * @param x x coordinate of point
	 * @param y y coordinate of point 
	 * @return true if inside, false if not
	 */
	protected boolean isInsideRectangle(double x,double y){
		if(((x>=topLeftX && x <= bottomRightX) &&
				(y <= topLeftY && y >= bottomRightY))){
			return true;
		}
		else
			return false;
	}
	
	public String toString(){
		return "bottomRightX: " +bottomRightX + System.lineSeparator() +
			"bottomRightY: " +bottomRightY + System.lineSeparator() +
			"topLeftX: " +topLeftX + System.lineSeparator() +
			"topLeftY: " +topLeftY + System.lineSeparator() +
			"capacity: " +capacity + System.lineSeparator();
	}



	static enum Quadrant {
		NW,NE,SE,SW;
	}
}