/*
 * Theoretical Implentation Adapted from Fabian Terh
 * https://medium.com/@fabianterh/how-to-solve-the-knapsack-problem-with-dynamic-programming-eb88c706d3cf
 */


package droneSim;

import java.util.ArrayList;

public class KnapsackPacker 
{
	
	public ArrayList<Order> skippedOrders; 
	
	public KnapsackPacker()
	{
		skippedOrders = new ArrayList<Order>();
	}
	
	
	/*
	 * Uses Knapsack packing algorithm to find most efficient packing of orderBacklog
	 * Any skipped orders from last iteration are automatically packed
	 * Takes in orderBacklog to take orders from
	 * Returns an ArrayList of packed orders
	 */
	public ArrayList<Order> pack(ArrayList<Order> orderBacklog) 
	{	
		 int orderCountAdjust = -1; //Place on the backlog to start looking at new orders
		
		 //If we already skipped orders last iteration we need to assume those are already in the solution
		 if(skippedOrders!=null)
	        	orderCountAdjust = skippedOrders.size()-1;
	
		//finds the number of orders that we are going to solve the packing algorithm with
		int numOrders = findLastValidItem(orderCountAdjust+1,orderBacklog);
		 
		//Need to account for the skipped orders that are fast-tracked
        int droneWeight = Main.getCurrentSetup().getDroneWeight() - getAllWeight(skippedOrders);     

        // Create 2d array of possible solutions
        //Each possible weight has a row
        //Each possible item has a column
        ArrayList<Order>[][] mat = new ArrayList[numOrders + 1][droneWeight + 1];
        

        // Main logic
        //For each order
        for (int order = 1; order <= numOrders; order++) 
        {
        	//for each capacity
            for (int capacity = 1; capacity <= droneWeight; capacity++) 
            {
            	ArrayList<Order> currentOrder = new ArrayList<Order>();
            	ArrayList<Order> previousOrder = new ArrayList<Order>();
        
            	//previous order is best combination before this item was allowed
                previousOrder = mat[order -1][capacity];
                
                //weight of current order being added
                int weightOfCurr = orderBacklog.get(order+ orderCountAdjust).getOrderWeight();
               
                //if can even fit current item
                if (capacity >= weightOfCurr) 
                {
                	currentOrder.add(orderBacklog.get(order+ orderCountAdjust));
                	
                	//find how much space left
                	int remainingCapacity = capacity - weightOfCurr;
                	
                	//if we came up with a solution before which used the remaining capacity, add that to existing order
                	ArrayList<Order> remainingCapacityOrder = mat[order-1][remainingCapacity];
                	if(remainingCapacityOrder != null)
                	{
                		currentOrder.addAll(remainingCapacityOrder);
                	}
    
                }
               
                //Here we choose if we want to put the current order (this iteration's order + any well fitting orders)
                //Or the previous order (what last iteration we decided was our best combination)
                
                if(previousOrder == null) //if we had nothing last time
                {
                	mat[order][capacity] = currentOrder;
                }
                else if(currentOrder == null) //if we have nothing this
                {
                	mat[order][capacity] = previousOrder;
                }
                //if current is objectively better (either more orders, or the same number and weighing less)
                else if((currentOrder.size() == previousOrder.size() && getAllWeight(currentOrder)<getAllWeight(previousOrder))
                		|| currentOrder.size()  > previousOrder.size())
                {
                	mat[order][capacity] = currentOrder;
                }
                else
                {
                	mat[order][capacity] = previousOrder;
                }
            }
        }
	        
	       
	       /* 
	        // Print out array for debugging
	        System.out.print("\n");
	        for(int i=0;i<numOrders+1;i++)
	        {
	        	System.out.print("\n");
	        	for(int j=0;j<droneWeight+1;j++)
	        	{
	        		if(mat[i][j]!=null)
	        		{
	        			System.out.print(mat[i][j].size() + " , ");
	        		}
	        		else
	        		{
	        			System.out.print("N" + " , ");
	        		}
	        		
	        	}
	        }
	        
	        */
	        
        ArrayList<Order> ans = mat[numOrders][droneWeight];
        
        //Add previously skipped orders to solution
        if(skippedOrders!=null && ans!=null)
        	ans.addAll(skippedOrders);
        
        /*
        //Remove skipped Orders from orderBacklog
        if(skippedOrders!=null)
        for(int i=0;i<skippedOrders.size();i++)
        {
        	 for(int j=0;j<skippedOrders.size();j++)
        	 {
        		 if(skippedOrders.get(i)==orderBacklog.get(j))
        		 {
        			 orderBacklog.remove(j);
        			 j --;
        		 }
        	 }
        }
        */
        //Reset Skipped Orders
        skippedOrders.clear();
        
        /*
        if(ans!=null)
        for(int i=0;i<ans.size();i++)
        {
        	System.out.println(ans.get(i).getMeal().getName());
        }
        */
	        
	     //for each order that could have been chosen
        
         if(ans!=null)
	     for(int i=0;i<numOrders;i++)
	     {
	    	 boolean isInSolution = false;
	    	 //find it packed in solution
	    	 for(int j=0;j<ans.size();j++)
	    	 {
	    		 if(ans.get(j) == orderBacklog.get(i))
	    		 {
	    			 isInSolution = true;
	    		 }
	    	 }
	    	 
	    	 //if packed remove it from main
	    	 if(isInSolution == true)
    		 {
    			 orderBacklog.remove(i);
    			 i --;
    			 numOrders --;
    		 }
    		 else //add it to skipped list
    		 {
    			 skippedOrders.add(orderBacklog.get(i));
    		 }
	     }
         
         if(ans==null)
        	 ans = new ArrayList<Order>();
	        
	     return ans;
	}
	
	
	/*
	 * Returns Weight of all orders in ArrayList
	 * Takes in arraylist
	 * Returns weight of all orders in that ArrayList
	 */
	public static int getAllWeight(ArrayList<Order> arrayList)
	{
		
		
		int sum = 0;
		for(int i=0;i<arrayList.size();i++)
		{
			sum += arrayList.get(i).getOrderWeight();
		}
		return sum;
	}
	
	
	/*
	 * Iterates through orders until it finds enough to fill two drones
	 * This will give all valid orders while also allowing skipped orders to be filled next cycle
	 * Looks for an arrayList to iterate through (orderBacklog)
	 * Returns index of final valid order
	 */
	public int findLastValidItem(int numStart,ArrayList<Order> arrayList)
	{
		//MaxThisCycle is the space that can be filled this cycle while still allowing the next cycle to fulfill any skipped orders
		int maxThisCycle = (Main.getCurrentSetup().getDroneWeight() * 2) - getAllWeight(skippedOrders);
		int sum = 0;
		
		for(int i=numStart;i<arrayList.size();i++)
		{
			if(sum+arrayList.get(i).getOrderWeight()>maxThisCycle)
			{
				return i - 1;
			}
			else
			{
				sum +=arrayList.get(i).getOrderWeight();
			}
		}
		return arrayList.size();
	}
}


