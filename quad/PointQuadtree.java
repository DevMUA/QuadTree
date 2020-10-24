package sonc.quad;

import java.util.HashSet;
import java.util.Set;

/**
 * This class follows the Facade design pattern and presents a presents a single access point to manage quad trees. It provides methods for inserting, deleting and finding elements implementing HasPoint. This class corresponds to the Client in the Composite design pattern used in this package.
 *
 * @param <T> a type extending HasPoint
 */
public class PointQuadtree <T extends HasPoint> {
	protected Trie<T> root;
	protected Set<T> points;
	
	public PointQuadtree(double topLeftX,double topLeftY,
						double bottomRightX,double bottomRightY){
		root = new LeafTrie<T>(topLeftX,topLeftY,bottomRightX,bottomRightY);
		points = new HashSet<T>();
	}
	
	/**
	 * Delete given point from QuadTree, if it exists there
	 * @param point to be deleted
	 */
	public void delete(T point){
		root.delete(point);
	}
	
	/**
	 * @param point with requested coordinates
	 * @return recorded point, if found; null otherwise
	 */
	public T find(T point){
		return root.find(point);
	}
	
	/**
	 * Insert given point in the QuadTree
	 * @param point to be inserted
	 */
	public void insert(T point){
		//try{
			if(!(root.isInsideRectangle(point))){
				throw new PointOutOfBoundException();
			}
			root.insert(point);
		//}
		//catch(PointOutOfBoundException ex){
			//ex.getMessage();
		//}
		
	}
	
	/**
	 * Insert point, replacing existing point in the same position
	 * @param point point to be inserted
	 */
	public void insertReplace(T point){
		root.insertReplace(point);
		//insert(point);
	}
	
	/**
	 * A set with all points in the QuadTree
	 * @return set of instances of type HasPoint
	 */
	public Set<T> getAll(){
		points.clear();
		root.collectAll(points);
		return points;
	}
	
	/**
	 * Returns a set of points at a distance smaller or equal to radius from point with given coordinates.
	 * @param x coordinate of point
	 * @param y coordinate of point
	 * @param radius from given point
	 * @return set of instances of type HasPoint
	 */
	protected Set<T> findNear(double x, double y, double radius){
		points.clear();
		root.collectNear(x, y, radius, points);
		return points;
	}
}