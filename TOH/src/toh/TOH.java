/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
 
/**
 *
 * @author Raynaldi Susanto
 */
public class TOH{
 
  public static void main(String args[]) {
    Scanner sc = new Scanner(System.in);
		
    System.out.print("Masukkan jumlah disk : ");
    int disk = sc.nextInt(); // jumlah disk
		
    System.out.print("Masukkan jumlah tiang : ");
    int tiang = sc.nextInt(); // tiang
		
    // Konfigurasi tiang awal
    Node source = readPegsConfiguration(tiang, disk, 1); // timpukkan disk dari tiang 1
		
    // Konfigurasi tiang target
    Node target = readPegsConfiguration(tiang, disk, tiang); // tumpukkan disk tiang terakhir
		
    // Proses BFS Minimal Move Target
    Set visited = new HashSet();
    try {
      minMovesToTarget(source, target, visited);
    } catch (Exception ex) {
      System.out.println("Exception = " + ex);
    }
  }
 
  private static void minMovesToTarget(Node source, Node target, Set visited) throws CloneNotSupportedException   {
    // BFS queue
    // tambahkan node source ke queue
    
    Queue q = new LinkedList();
    
    q.add(source);
    Node current = source;
    while (!q.isEmpty()) {
      current = (Node) q.poll();
      if (current.equals(target)) { // Node target
        break; // Jika target ketemu, maka berhenti
      }
			
      List neighbors = current.neighbors();
      if (neighbors.size() > 0) {
        for (Object n : neighbors) {
          if (!visited.contains(n)) {// jika blm pernah dikunjungi maka taruh di queue
            q.offer(n);
            visited.add(n);
          }
        }
      }
    }
		
    //Cetak jika tumpukkan disk sesuai dengan node target pada tiang terakhir
    if (current.equals(target)) {
      printOutput(current);
    }
  }
 
  private static Node readPegsConfiguration(int tiang, int disk, int sc) {
    Stack[] initialState = new Stack[tiang];
    for (int i = 0; i < tiang; i++) {
      initialState[i] = new Stack();
    }
		
    // Karena perulangan dimulai dari kecil ke besar, maka dilakukan Collection.reverseOrder untuk membalik value
    TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>(Collections.reverseOrder());
    for (int i = 0; i < disk; i++) {
      map.put(i, sc);
    }
		
    //prepare towers
    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
      initialState[entry.getValue() - 1].push(entry.getKey());
    }
		
    return new Node(initialState);
  }
 
  static void printOutput(Node target) {
		// Print hasil dari BFS
    Stack stack = new Stack<>();
    while (target.parent != null) {
      stack.add(target.move);
      target = target.parent;
    }
    
    System.out.println("Jumlah move yang dibutuhkan : " + stack.size() + "\n");
    System.out.println("Solve : ");
    
    while (!stack.isEmpty()) {
      System.out.println(stack.pop());
    }
  }
 
  static class Node implements Cloneable {
    // tiang
    Stack[]state = null;
    Node parent = null;  //untuk backtracking
    Move move = null; // untuk bergerak ke tiang lainnya
   
    public Node(Stack[] st) {
      state = st;
    }
 
    @Override
    protected Node clone() throws CloneNotSupportedException  {
      Stack[] cloneStacks = new Stack[state.length];
      for (int i = 0; i < state.length; i++) {
        cloneStacks[i] = (Stack) state[i].clone();
      }
      Node clone = new Node(cloneStacks);
      return clone;
    }
 
    //returns the neghboring configurations.
    //What all configurations we can get based on current config.
    public List neighbors() throws CloneNotSupportedException  {
      List neighbors = new ArrayList<>();
      int k = state.length;
      for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
          if (i != j && !state[i].isEmpty()) {
            //Need to clone to avoid change the parent node.
            Node child = this.clone();
            //make a move
            if (canWeMove(child.state[i], child.state[j])) {
              child.state[j].push(child.state[i].pop());
              //this is required to backtrack the trail once we find the target config
              child.parent = this;
              //Menunjukkan perpindahan disk
              child.move = new Move(i, j);
              neighbors.add(child);
            }
          }
        }
      }
      return neighbors;
    }
 
    public boolean canWeMove(Stack fromTower, Stack toTower) { // peraturan tower of hanoi
      boolean answer = false;
      if (toTower.isEmpty()) {// jika tiang tujuan kosong maka kita bisa menambahkan semabarang disk ke tiang tersebut
        return true;
      }
      int toDisc = (int) toTower.peek();
          int fromDisc = (int) fromTower.peek();
      if (fromDisc < toDisc) { //hanya dapat meletakkan disk kecil di atas disk yang besar
        answer = true;
      }
      return answer;
    }
 
    @Override
    public int hashCode() {
      int hash = 7;
      return hash;
    } 

  
    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final Node other = (Node) obj;
      if (!Arrays.deepEquals(this.state, other.state)) {
        return false;
      }
      return true;
    }
 
    class Move {
      int towerFrom, towerTo;
      public Move(int f, int t) {
        towerFrom = f + 1;
        towerTo = t + 1;
      }
     @Override
      public String toString() {
        return "move Disk from " + towerFrom + " to " + towerTo;
      }
    }
  }
}
