# SystemDesignAlgorithms



B-Tree Implementation

Detailed Steps for Inserting a Key:
Traverse to the Appropriate Node:
Start at the root and move down the tree to find the correct leaf node where the key should be inserted.
Insert into a Non-Full Leaf Node:
If the leaf node has space (less than 2 * t - 1 keys), insert the key in the correct sorted position.
Shift other keys to the right if needed to make space for the new key.
Split if Necessary:
If a node is full (i.e., it has 2 * t - 1 keys) when trying to insert, split the node:
Create a new node to store the right half of the keys.
Move the middle key up to the parent node.
If the parent is also full, split the parent recursively.
Promote Keys to the Parent:
The middle key of the full node is promoted to the parent to maintain the B-tree properties.
This may cause the parent to become full, triggering further splits up the tree.
Example of Insertion:
Suppose we have a B-tree with t = 3 (minimum degree of 3). The maximum number of keys in each node is 2 * 3 - 1 = 5.

Inserting keys 10, 20, 5, 6, 12, 30, 7, 17:

Insert 10: The tree is empty, so 10 becomes the root key.
Insert 20: Insert 20 into the root node. The node now has [10, 20].
Insert 5: Insert 5 into the root node. The node now has [5, 10, 20] in sorted order.
Insert 6: Insert 6 into the root node. The node now has [5, 6, 10, 20].
Insert 12: Insert 12 into the root node. The node now has [5, 6, 10, 12, 20].
Insert 30: The root is now full. Split the root node:
The middle key (10) is promoted to a new root.
The root splits into two nodes: [5, 6] and [12, 20, 30], with 10 as the new root.
Insert 7: Insert 7 into the left child [5, 6] as it has space. It becomes [5, 6, 7].
Insert 17: Insert 17 into the right child [12, 20, 30] as it has space. It becomes [12, 17, 20, 30].
Resulting B-Tree:
        [10]
       /    \
  [5, 6, 7]  [12, 17, 20, 30]
Summary:
Insertion ensures balance by splitting nodes when full and promoting keys to maintain B-tree properties.
Recursion is used to traverse down the tree and handle insertions into non-leaf nodes.
Efficiency is maintained as all operations (search, insert, delete) are 
O
(
log
⁡
n
)
O(logn), making B-trees suitable for databases and file systems.
