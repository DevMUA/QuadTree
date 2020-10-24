package sonc.quad;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Trie with 4 sub tries with equal dimensions covering all its area. This class corresponds to the Composite in the Composite design pattern.
 *
 * @param <T> type that extends HasPoint
 */
class NodeTrie <T extends HasPoint> extends Trie <T> {
	protected Map<Trie.Quadrant,Trie<T>> tries;
	protected boolean isDivided;
	
	NodeTrie(double topLeftX, double topLeftY,double bottomRightX, double bottomRightY){
		super(topLeftX,topLeftY,bottomRightX,bottomRightY);
		tries= new HashMap<Trie.Quadrant,Trie<T>>();
		this.isDivided=false;
		}
	
	/**
	 * Divides a Nodetrie into 4 subsections/leaftries
	 */
	protected void subdivide(){
		double w = this.bottomRightX-this.topLeftX;
		double h = this.topLeftY-this.bottomRightY;
		tries.put(Quadrant.NW,new LeafTrie<T>(this.topLeftX,this.topLeftY,this.topLeftX+(w/2),this.topLeftY-(h/2)));
		tries.put(Quadrant.NE,new LeafTrie<T>(this.topLeftX+(w/2),this.topLeftY,this.bottomRightX,this.bottomRightY+(h/2)));
		tries.put(Quadrant.SW,new LeafTrie<T>(this.topLeftX,this.topLeftY-(h/2),this.bottomRightX-(w/2),this.bottomRightY));
		tries.put(Quadrant.SE,new LeafTrie<T>(this.topLeftX+(w/2),this.topLeftY-(h/2),this.bottomRightX,this.bottomRightY));
		this.isDivided=true;
	}
	/**
	 * Collect all points in this node and its descendants in given set
	 * @param points set of HasPoint for collecting points
	 */
	protected void collectAll(Set<T> points){
		for(Map.Entry<Trie.Quadrant,Trie<T>> entry : tries.entrySet()){
			entry.getValue().collectAll(points);
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
		for(Map.Entry<Trie.Quadrant,Trie<T>> entry : tries.entrySet()){
			entry.getValue().collectNear(x, y, radius, nodes);
			}
		
	}
	/**
	 * Delete given point
	 * @param point to delete
	 */
	protected void delete(T point){
		Set<T> nodes = new HashSet<T>();
		for(Map.Entry<Trie.Quadrant,Trie<T>> entry : tries.entrySet()){
			entry.getValue().delete(point);
		}
		// assures that if a node trie has all leafnodes empty then it undivides itself
		this.collectAll(nodes);
		if(nodes.isEmpty()){
			tries.clear();
			isDivided=false;
		}
		
		
	}
	/**
	 * Find a recorded point with the same coordinates of given point
	 * @param point with requested coordinates
	 * @return recorded point, if found; null otherwise
	 */
	protected T find(T point){
		T tmp = null;
		for(Map.Entry<Trie.Quadrant,Trie<T>> entry : tries.entrySet()){
			tmp = entry.getValue().find(point);
			if(tmp!=null)
				break;
		}
		return tmp;
	}

	/**
	 * Insert given point, replacing existing points in same location
	 * @param point to be inserted
	 * @return changed parent node
	 */
	protected Trie<T> insert(T point){
		//variable to assure that one point in the border of two or more 
		//rectangles is only placed once on one of them
		boolean alreadyPlaced = false;
		if(this.isInsideRectangle(point)){
			// limit exceed on this leaf so subdivide
			if(isDivided==false)
				subdivide();
			//insert point
			for(Map.Entry<Trie.Quadrant,Trie<T>> entry : tries.entrySet()){
				if(entry.getValue().isInsideRectangle(point) && alreadyPlaced==false){
					alreadyPlaced = true;
					entry.getValue().insert(point);
				}
			}
			return this;
		}
		else{
			return this;
		}
	}
	/**
	 * Insert given point, replacing existing points in same location
	 * @param point to be inserted
	 * @return changed parent node
	 */
	protected Trie<T> insertReplace(T point){
		for(Map.Entry<Trie.Quadrant,Trie<T>> entry : tries.entrySet()){
			if(this.isInsideRectangle(point))
				entry.getValue().insertReplace(point);
		}
		return this;
	}
	
	protected Trie.Quadrant quadrantOf(T point){
		//NW
		if(tries.get("NW").isInsideRectangle(point))
			return Quadrant.NW;
		//NE
		else if(tries.get("NE").isInsideRectangle(point))
			return Quadrant.NE;
		//SW
		else if(tries.get("SW").isInsideRectangle(point))
			return Quadrant.SW;
		//SE
		else 
			return Quadrant.SE;
	}
	
	public String toString(){
		return "isDivided" + isDivided + System.lineSeparator() +
				 "tries size: " + tries.size() + System.lineSeparator() +
				 "bottomRightX: " +bottomRightX + System.lineSeparator() +
					 "bottomRightY: " +bottomRightY + System.lineSeparator() +
					 "topLeftX: " +topLeftX + System.lineSeparator() +
					 "topLeftY: " +topLeftY + System.lineSeparator() +
					 "capacity: " + getCapacity() + System.lineSeparator();
	}




}