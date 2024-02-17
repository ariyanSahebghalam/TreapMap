import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.Random;

// Imports just for the KeySet method
import java.util.AbstractSet;
import java.util.Iterator;


/**
 * A treap-based partial Java implementation of the Map interface.
 * The implementation will be randomized, which may offer better performance
 * in some situations. The heap priority property ensures is based on min-heap.
 *
 * @author Ariyan Sahebghalam, Eyosyas Andarge, Sadiq Azmi
 * @param <K> The type of keys held in this tree
 * @param <V> The type of values held in this tree
 */
public class TreapMap<K, V> implements Map<K,V> {

	int size = 0;
	Node<K,V> root;
	private static Random rand;     // For randomizing the priority of the nodes

	// Class for implementation of a singular node of the treap
	private static final class Node <K,V>{
		V value;
		K key;
		int priority;
		Node left;
		Node right;
		Node parent;

		//empty constructor to create a node
		public Node() {
		}
	}
	/**
	 * Constructor to construct an object of type TreapMap
	 */
	public TreapMap() {
	}



	/**
	 * put method that inserts a new node into the treap given key and value and a randomized priority
	 * done in O(log(n)) time.
	 * @param key the value used to locate the location the node must be inserted in the treap
	 * @param value the value attached to the key
	 * @return The old key value if a node is being replaced, otherwise null if inserted without replacement
	 */
	@Override
	public V put(K key, V value) {

		Node<K,V> node = new Node<>();
		node.key = key;
		node.value = value;
		node.priority = new Random().nextInt();

		// case where we are added the root
		if (size == 0) {
			root = node;
			size++;
			return null;
		}
		size++;

		Node<K, V> current = root;
		V val = null;

		int found = 0;
		//a loop to go through the map, it will exit once the key is found. or if it does not exist.
		while (current != null && found == 0) {

			int a =  ((Comparable) key).compareTo((Comparable)current.key);

			if ( a < 0) {
				if (current.left == null) {
					found = 1;
					break;
				}
				current = current.left;
			}
			else if ( a > 0) {
				if (current.right == null) {
					found = 2;
					break;
				}
				current = current.right;
			}
			else if ( a == 0) {
				// key is already there
				val = current.value;
				current.value = value;
				current.priority = node.priority;
				size--;
				break;
			}
			else {
				break;
			}
		}

		//setting the node either left or right depending on the BST search
		if (found == 1 ) {
			current.left = node;
			node.parent = current;
		}
		else if (found ==2) {
			current.right = node;
			node.parent = current;
		}


		Node<K,V> temp;
		//if heap property is violated, rotate right or left, respectively
		while (current != null) {
			//rotate right
			if (current.left != null && current.left.priority < current.priority) {

				rotateRight(current);

			}

			//rotate left
			else if (current.right != null && current.right.priority < current.priority) {

				rotateLeft(current);

			}

			current = current.parent;
		}


		return val;
	}


	/**
	 * remove method that removes the node from this treap if present, done in O(log(n)).
	 * @param key the specific node with that key to remove from the treap
	 * @return the value associated with the removed node, or null if the key didn't exist in the treap
	 *
	 * Inspired by this link on the java implementation of TreeMap https://developer.classpath.org/doc/java/util/TreeMap-source.html
	 */
	@Override
	public V remove(Object key) {
		Node<K, V> current = root;
		V val = null;
		boolean keyFound = false;

		//a loop to go through the map, it will exit once the key is found. or if it does not exist.
		while (current != null ) {

			int a =  ((Comparable) key).compareTo((Comparable)current.key);

			if ( a < 0) {
				current = current.left;
			}
			else if ( a > 0) {
				current = current.right;
			}
			else if ( a == 0) {
				val = current.value;
				size--;
				current.priority = (int)Float.POSITIVE_INFINITY;
				keyFound = true;
				break;
			}
		}

		// If key is not found, just return null
		if (!keyFound) {
			return null;
		}

		Node<K,V> temp;
		//rotations
		while (current.left != null || current.right != null) {
			//rotate right
			if ( current.left != null && current.left.priority <= current.priority) {
				rotateRight(current);
			}
			//rotate left
			else if ( current.right != null && current.priority > current.right.priority) {
				rotateLeft(current);
			}
		}

		//now to remove the node
		//special case where we are removing only one node
		if (size == 0) {
			root = null;
			return val;
		}
		//check if its at left side
		else if (current.parent.left == current) {
			current.parent.left = null;
		}
		//if not its at the right side
		else {
			current.parent.right = null;
		}

		current.parent = null;
		//the node is not accessible and therefore is removed and now return the value

		return val;
	}




	/**
	 * Get function to obtain an associated value with a given key, done in O(log(n)) time.
	 * @param key the specific key to obtain the associated value with it.
	 * @return Returns the value associated with the given key
	 */
	@Override
	public V get(Object key) {
		Node<K, V> temp = getKeyNode((K) key);

		// In case the key wasn't found
		if (temp == null) {
			return null;
		}

		// Else return the associated value with that key
		return temp.value;
	}


	/**
	 * helper method used to make the recursive process easier
	 * @param key the specific key to obtain the associated value with it late
	 * @return Returns the node associated with the given key
	 */
	private Node<K, V> getKeyNode(K key) {
		Node<K, V> cur = root;

		// Why the operator < > don't work?
		while (cur != null) {

			int result = ((Comparable) key).compareTo((Comparable) cur.key);

			if (result < 0) {
				cur = cur.left;
			}
			else if (result > 0) {
				cur = cur.right;
			}
			else {
				return cur;
			}
		}

		// or null basically
		return cur;
	}





	/**
	 * A function to see the current size of the treap
	 * @return Returns the number of nodes in the TreapMap
	 */
	@Override
	public int size() {
		return this.size;
	}




	/**
	 * Removes the entire nodes from the TreapMap, and sets the size to 0.
	 */
	@Override
	public void clear() {
		this.size = 0;
		this.root = null;
	}



	/**
	 * Gives a set equivalence of the TreapMap's key nodes
	 * @return Returns the set of keys for the TreapMap
	 *
	 * Inspired by the code from the page https://github.com/eagle518/jdk-source-code/blob/master/jdk5.0_src/j2se/src/share/classes/java/util/TreeMap.java
	 */
	@Override
	public Set<K> keySet() {
		return new AbstractSet<K>() {
			@Override
			public Iterator<K> iterator() {
				return new Iterator<K>() {
					private Node<K, V> currentNode = getSmallestNode(root);

					@Override
					public boolean hasNext() {
						return currentNode != null;
					}

					@Override
					public K next() {
						Node<K, V> prevNode = currentNode;
						currentNode = getNextNode(currentNode);
						return prevNode.key;
					}
				};
			}

			@Override
			public int size() {
				return TreapMap.this.size();
			}

			@Override
			public boolean add(K key) {
				throw new UnsupportedOperationException("Adding keys is not supported");
			}
		};
	}

	private Node<K, V> getSmallestNode(Node<K, V> node) {
		if (node == null) {
			return null;
		}
		while (node.left != null) {
			node = node.left;
		}
		return node;
	}

	private Node<K, V> getNextNode(Node<K, V> node) {
		if (node.right != null) {
			return getSmallestNode(node.right);
		}
		while (node.parent != null && node == node.parent.right) {
			node = node.parent;
		}
		return node.parent;
	}




	/**
	 * toString function which acts identical to AbstractMap's default implementation in that it will
	 * print the keys and values as key-value pairs like {key1=value1, key2=value2, ...}. Done in linear time.
	 * @return Returns the key-value pair representation of the TreapMap as a string
	 */
	@Override
	public String toString() {
		String result = "{" + helperToString(root);

		// Remove the last comma and space if they exist
		int length = result.length();
		if (length >= 2) {
			result = result.substring(0, length - 2);
		}

		return result + "}";
	}

	/**
	 * Helper function used to ease the process of recursively going through the TreapMap nodes
	 * and adding the key-value pairs to a string variable.
	 * @param root the node we start the inorder traversal from
	 * @return String representation of the key-value pairs for the TreapMap
	 */
	private String helperToString(Node root) {
		String str = "";
		if (root != null) {
			str += helperToString(root.left) + "" + root.key + "=" + root.value + ", " + helperToString(root.right);
		}
		return str;
	}


	/**
	 * Performs a left rotation on the given 'current' node in the TreapMap.
	 * @param current The node to be rotated left.
	 */
	private void rotateLeft(Node<K,V> current) {
		Node<K, V> temp;
		//temp nodes
		temp = current.right;

		if (current == root) {
			root = temp;
		}
		temp.parent = current.parent;
		current.right = temp.left;
		current.parent = temp;



		if ( temp.left != null) {
			temp.left.parent = current;
		}
		temp.left = current;

		//case only if we aren't handling the root
		if (temp.parent != null) {
			// now final step to set the parent of the upper node to point to the new node
			// use if statement to check if its left side or right side
			if (temp.parent.right == current) {
				temp.parent.right = temp;
			}
			else if (temp.parent.left == current){
				temp.parent.left = temp;
			}
		}
	}


	/**
	 * Performs a right rotation on the given 'current' node in the TreapMap.
	 * @param current The node to be rotated right.
	 */
	private void rotateRight(Node<K,V> current) {

		Node<K,V> temp;
		//temp nodes
		temp = current.left;

		//special case where we rotate the top node
		if (current == root) {
			root = temp;
		}

		temp.parent = current.parent;
		current.left = temp.right;
		current.parent = temp;

		if ( temp.right != null) {
			temp.right.parent = current;
		}
		temp.right = current;


		//case only if we aren't handling the root
		if (temp.parent != null) {
			// now final step to set the parent of the upper node to point to the new node
			// use if statement to check if its left side or right side
			if (temp.parent.left == current) {
				temp.parent.left = temp;
			}
			else if (temp.parent.right == current){
				temp.parent.right = temp;
			}
		}
	}






	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getOrDefault(Object key, Object defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void forEach(BiConsumer action) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void replaceAll(BiFunction function) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object putIfAbsent(Object key, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object key, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean replace(Object key, Object oldValue, Object newValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object replace(Object key, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object computeIfAbsent(Object key, Function mappingFunction) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object computeIfPresent(Object key, BiFunction remappingFunction) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object compute(Object key, BiFunction remappingFunction) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object merge(Object key, Object value, BiFunction remappingFunction) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsKey(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}
}
