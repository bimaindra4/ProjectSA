/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy.algo.ta;

import java.util.Scanner;

/**
 *
 * @author Raynaldi Susanto
 */

public class StrategyAlgoTA {

       public void solve(int n, String start, String auxiliary, String end) {
           if (n == 1) {
               System.out.println("move disk : " + n + " from " +start + " to " + end);
           } else {
               solve(n - 1, start, end, auxiliary);
               System.out.println("move disk : " + n + " from " +start + " to " + end);
               solve(n - 1, auxiliary, start, end);
           }
       }

       public static void main(String[] args) {
           StrategyAlgoTA towersOfHanoi = new StrategyAlgoTA();
           System.out.print("Enter number of discs: ");
           Scanner scanner = new Scanner(System.in);
           int discs = scanner.nextInt();
           towersOfHanoi.solve(discs, "A", "B", "C");
       }
}
