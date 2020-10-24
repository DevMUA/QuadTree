package sonc.quad;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A Trie that has no descendants. This class corresponds to the Leaf in the Composite design pattern.
 *
 * @param <T> Type that extends HasPoint
 */
class LeafTrie <T extends HasPoint> extends Trie<T> {
	List<T> listPoints;
	
	LeafTrie(double topLeftX, double topLeftY,double bottomRightX, double bottomRightY){
		super(topLeftX,topLeftY,bottomRightX,bottomRightY);
		listPoints = new ArrayList<T>();
		}
	
	/**
	 * Insert given point, replacing existing points in same location
	 * @param point to be inserted
	 * @return changed parent node
	 */
	protected Trie<T> insert(T point){
		if(listPoints.size()==Trie.getCapacity()){
			// limit exceed so create NodeTrie and return it with added points
			NodeTrie<T> node = new NodeTrie<T>(this.topLeftX,this.topLeftY,this.bottomRightX,this.bottomRightY);
			listPoints.add(point);
			for(T tmp : listPoints){
				node.insert(tmp);
			}
			return node;
			
		}
		else{
			listPoints.add(point);
			return this;
		}
	}
	/**
	 * Delete given point
	 * @param point to delete
	 */
	protected void delete(T point){
		Iterator<T> it = listPoints.iterator();
		//iterate through list to avoid deletion errors
		while(it.hasNext()){
			T tmp = it.next();
			if(tmp.equals(point))
				it.remove();
		}
		
	}
	/**
	 * Collect all poi	nts in this node and its descendants in given set
	 * @param points set of HasPoint for collecting points
	 */
	protected void collectAll(Set<T> points){
		for(T tmp : listPoints){
			points.add(tmp);
		}
	}
	/**
	 * Collect points at a distance smaller or equal to radius from (x,y) and place them in given list
	 * @param x coordinate of point
	 * @param y coordinate of point
	 * @param radius from given point
	 * @param nodes set for collecting points
	 */
	protected void collectNear(double x, double y,double radius,Set<T> nodes){
		for(T tmp : listPoints){
			if(getDistance(tmp.getX(),tmp.getY(),x,y)<=radius){
				nodes.add(tmp);
			}
		}
	}
	/**
	 * Find a recorded point with the same coordinates of given point
	 * @param point with requested coordinates
	 * @return recorded point, if found; null otherwise
	 */
	protected T find(T point){
		for(T tmp : listPoints){
			if(/*tmp.equals(point)*/ tmp.getX()==point.getX() && tmp.getY()==point.getY()){
				return tmp;
			}
		}
		return null;
	}
	/**
	 * Insert given point, replacing existing points in same location
	 * @param point to be inserted
	 * @return changed parent node
	 */
	protected Trie<T> insertReplace(T point){
		if(this.isInsideRectangle(point))
			listPoints.clear();
			listPoints.add(point);
		return this;
	}
	
	public String toString(){
		return "listPoints size: " + listPoints.size() + System.lineSeparator() +
				 	"bottomRightX: " +bottomRightX + System.lineSeparator() +
					 "bottomRightY: " +bottomRightY + System.lineSeparator() +
					 "topLeftX: " +topLeftX + System.lineSeparator() +
					 "topLeftY: " +topLeftY + System.lineSeparator() +
					 "capacity: " + getCapacity() + System.lineSeparator();
	}
}