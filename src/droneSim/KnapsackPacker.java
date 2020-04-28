/*
 * Theoretical Implentation Adapted from Fabian Terh
 * https://medium.com/@fabianterh/how-to-solve-the-knapsack-problem-with-dynamic-programming-eb88c706d3cf
 */


package droneSim;

import java.util.ArrayList;

public class KnapsackPacker 
{
	
	public ArrayList<Order> skippedOrders; 
	private CurrentSetup currentSetup;
	private int numSkipped;
	
	public KnapsackPacker(CurrentSetup currentSetup)
	{
		this.currentSetup = currentSetup;
		skippedOrders = new ArrayList<Order>();
		numSkipped = 0;
	}
	
	
	/*
	 * Uses Knapsack packing algorithm to find most efficient packing of orderBacklog
	 * Any skipped orders from last iteration are automatically packed
	 * Takes in orderBacklog to take orders from
	 * Returns an ArrayList of packed orders
	 */
	public ArrayList<Order> pack(ArrayList<Order> orderBacklog) 
	{	
		 int orderCountAdjust = 0; //Place on the backlog to start looking at new orders
		
		 //If we already skipped orders last iteration we need to assume those are already in the solution
		 if(skippedOrders!=null)
	        	orderCountAdjust = skippedOrders.size();
	
		//finds the number of orders that we are going to solve the packing algorithm with
		//findLastValidItem will give us the index of the last valid order than we can check
		 //Then we subtract by the skipped order size so that we only iterate through the
		 //valid non-skipped orders.  We find the most effecient packing based on these valid
		 //non-skipped orders.
		int numValidNonSkippedOrders = findLastValidItem(orderCountAdjust,orderBacklog) - skippedOrders.size();
		 
		//Need to account for the skipped orders that are fast-tracked
        int droneWeight = currentSetup.getDroneWeight() - getAllWeight(skippedOrders);

        // Create 2d array of possible solutions
        //Each possible weight has a row
        //Each possible item has a column
        ArrayList<Order>[][] mat = new ArrayList[numValidNonSkippedOrders + 1][droneWeight + 1];
        

        // Main logic
        //For each order
        for (int order = 1; order <= numValidNonSkippedOrders; order++) 
        {
        	//for each capacity
            for (int capacity = 1; capacity <= droneWeight; capacity++) 
            {
            	ArrayList<Order> currentOrder = new ArrayList<Order>();
            	ArrayList<Order> previousOrder = new ArrayList<Order>();
        
            	//previous order is best combination before this item was allowed
                previousOrder = mat[order -1][capacity];
                
                //weight of current order being added
                int weightOfCurr = orderBacklog.get(order+ orderCountAdjust-1).getOrderWeight();
               
                //if can even fit current item
                if (capacity >= weightOfCurr) 
                {
                	currentOrder.add(orderBacklog.get(order+ orderCountAdjust-1));
                	
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
	        
	       

 
        ArrayList<Order> ans = mat[numValidNonSkippedOrders][droneWeight];
        
        //Add previously skipped orders to solution
        if(skippedOrders!=null && ans!=null)
        	ans.addAll(skippedOrders);
        

        
        //Reset Skipped Orders
        skippedOrders.clear();
       
	       
        ArrayList<Order> ordersToDelete = new ArrayList<>();
        
        //orders overlooked, but not sure if skipped or just at the end of the backlog
        ArrayList<Order> purgatoryOrders = new ArrayList<>();
        
        
        //for each order that could have been chosen
        for(int i=0;i<numValidNonSkippedOrders + orderCountAdjust;i++)
        {
        	 boolean isInSolution = false;
	    	 
	    	 //find it packed in solution
        	 if(ans!=null && ans.size()>0)
	    	 for(int j=0;j<ans.size();j++)
	    	 {
	    		 if(ans.get(j) == orderBacklog.get(i))
	    		 {
	    			 isInSolution = true;
	    		 }
	    	 }
	    	 
	    	 if(isInSolution==true)
	    	 {
	    		 ordersToDelete.add(orderBacklog.get(i));
	    		 
	    		 //signify that the orders that previously were overlooked 
    			 //were actually skipped rather than just at the end of the backlog
    			 skippedOrders.addAll(purgatoryOrders);
    			 
    			 //clear out waiting overlooked orders
    			 purgatoryOrders.clear();
    			
	    	 }
	    	 else
	    	 {
	    			 //purgatory orders are orders that are overlooked (not present in solution), 
	    			 //but unsure if actually skipped or at the end of the backlog
	    			 purgatoryOrders.add(orderBacklog.get(i));

	    	 }
        }
        //clear out overlooked orders
        purgatoryOrders.clear();
        
        //remove packed orders from backlog
       	orderBacklog.removeAll(ordersToDelete);
        
      
         
         if(ans==null)
        	 ans = new ArrayList<Order>();
	        
         
         numSkipped += skippedOrders.size();
         
         
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
		double maxThisCycle = ((double) currentSetup.getDroneWeight() * 1.5) - (double)getAllWeight(skippedOrders);
		int sum = 0;
		
		for(int i=numStart;i<arrayList.size();i++)
		{
			if(sum+arrayList.get(i).getOrderWeight()>maxThisCycle)
			{
				return i;
			}
			else
			{
				sum +=arrayList.get(i).getOrderWeight();
			}
		}
		return arrayList.size();
	}
	
	public void printNumSkipped()
	{
		System.out.println("Number of skipped meals: " + numSkipped);
		
	}
}


