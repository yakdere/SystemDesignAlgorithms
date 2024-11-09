import java.util.TreeMap;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.UUID;
public class ConsistentHashing {
    private final NavigableMap<Integer, String> ring = new TreeMap<>();
    private final int numberOfReplicas;
    
    public ConsistentHashing(int numberOfReplicas) {
        this.numberOfReplicas = numberOfReplicas;
    }

    private int hash(String key) {
        return key.hashCode() & 0x7fffffff; // Ensure non-negative hash
    }

    public void addNode(String node) { // ring.put() complexity O(logN)
        for (int i = 0; i < numberOfReplicas; i++) {
            int nodeHash = hash(node + i);
            ring.put(nodeHash, node);
            System.out.println("Added node replica: " + node + " with hash: " + nodeHash);
        }
    }

    public void removeNode(String node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            int nodeHash = hash(node + i);
            ring.remove(nodeHash);
            System.out.println("Removed node replica: " + node + " with hash: " + nodeHash);
        }
    }

    public String getNode(String key) { // ring.tailMap(keyHash): This operation is O(log n) TreeMap Search
        if (ring.isEmpty()) {
            return null;
        }
        int keyHash = hash(key);
        // tailMap() Returns a view of the portion of this map whose keys are greater than or equal to fromKey.
        SortedMap<Integer, String> tailMap = ring.tailMap(keyHash);
        // if it is Empty return the lowest key in the map
        // if it is not empty return the lowest key from the tail (greater value from the Key point)
        int nodeHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
        // now we found the key aka exact hash value in the ring, we can retrieve it in instantly with get()
        return ring.get(nodeHash);
    }
  
  public static void main(String args[]) {
     
     ConsistentHashing consistentHashing = new ConsistentHashing(3); // Using 3 replicas per node

        // Add nodes
        consistentHashing.addNode("Node1");
        consistentHashing.addNode("Node2");
        consistentHashing.addNode("Node3");

        // Get node for a key
        String key = UUID.randomUUID().toString();// UUID > hashCode() function will return the hash
        System.out.println("Key: " + key + " is mapped to Node: " + consistentHashing.getNode(key));

        // Remove a node
        consistentHashing.removeNode("Node2");
        System.out.println("After removing Node2, key: " + key + " is mapped to Node: " + consistentHashing.getNode(key));
   
  }
}