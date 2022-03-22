package problem;

import java.util.Scanner;

public class BinaryTreeTest
{
  
  public int findDistance(Node root, int n1, int n2)
  {
    
    int leftNodeToRootNode = Pathlength(root, n1, "L") - 2; 
    int rightNodeToRootNode = Pathlength(root, n2, "R") - 2;
    if (leftNodeToRootNode != rightNodeToRootNode) {
      System.out.println("nods are not on same level.");
      System.exit(0);
    }
    int lcaData = findLCA(root, n1, n2).data; // LCA->Lowest Common Ancestor
    int lcaDistance = Pathlength(root, lcaData, "lcaDistance") - 1;
    return (leftNodeToRootNode + rightNodeToRootNode) - 2 * lcaDistance;
    
  }
  
  public int Pathlength(Node root, int n1, String callingFrom)
  {
    
    if (root != null) {
      
      int x = 0;
      
      if ("R" == callingFrom) {
        if (root.left == null || root.right == null) {
          System.out.println("counting empty child of : " + root.data);
        }
        if ((root.data == n1) || (x = Pathlength(root.left, n1, "R")) > 0
            || (x = Pathlength(root.right, n1, "R")) > 0) {
          return x + 1;
        }
      }
      if ("R" != callingFrom) {
        
        if ((root.data == n1) || (x = Pathlength(root.left, n1, "L")) > 0
            || (x = Pathlength(root.right, n1, "L")) > 0) {
          return x + 1;
        }
      }
      
      return 0;
    }
    return 0;
  }
  
  public Node findLCA(Node root, int n1, int n2)
  {
    
    if (root != null) {
      
      if (root.data == n1 || root.data == n2) {
        return root;
      }
      Node left = findLCA(root.left, n1, n2);
      Node right = findLCA(root.right, n1, n2);
      
      if (left != null && right != null) {
        return root;
      }
      if (left != null) {
        return left;
      }
      if (right != null) {
        return right;
      }
    }
    return null;
  }
  
  @SuppressWarnings("resource")
  public static void main(String[] args) throws java.lang.Exception
  {
    
    Node root = new Node(5);
    root.left = new Node(2);
    root.right = new Node(3);
    root.left.left = new Node(7);
    root.left.left.left = new Node(9);
    
    root.right.right = new Node(1);
    root.right.right.left = new Node(4);
    root.right.right.right = new Node(6);
    
    BinaryTreeTest binaryTreeTest = new BinaryTreeTest();
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter the From and To nodes: ");
    int from = sc.nextInt();
    int to = sc.nextInt();
    System.out.println(
        "Distance between From and To is : " + binaryTreeTest.findDistance(root, from, to));
  }
  
}


