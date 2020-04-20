package ui;

import java.util.ArrayList;

import droneSim.Meal;
import javafx.scene.control.ListView;

public class CustomMealListView {

	// private CurrentSetup currentSetup;
	private ListView<Meal> listView;

	public CustomMealListView() {
		listView = new ListView<>();
		listView.setCellFactory(e -> new CustomMealListCell());
		listView.getSelectionModel().selectedItemProperty().addListener(e -> {
			//System.out.println(getSelected().toString());
		});
	}

	/**
	 * Add a single meal to list view
	 * 
	 * @param meal
	 */
	public void addMeal(Meal meal) {
		listView.getItems().add(meal); // add an item
	}

	/**
	 * Add multiple meals to list view
	 * 
	 * @param meals
	 */
	public void addMeal(ArrayList<Meal> meals) {
		listView.getItems().addAll(meals); // add an item
	}

	/**
	 * - get the selecte meal - returns null if nothing selected
	 * 
	 * @return
	 */
	public Meal getSelected() {
		
		int selectedIndex = listView.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) {
			return listView.getItems().get(selectedIndex);

		}
		return null;
	}

	/**
	 * remove selected meal (if not available, return null)
	 */
	public Meal removeSelected() {
		Meal itemToRemove = null;
		int selectedIndex = listView.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) {
			itemToRemove = getSelected();

			// what should be selected after the removal
			final int newSelectedIdx = (selectedIndex == listView.getItems().size() - 1) 
					? selectedIndex - 1
					: selectedIndex;

			listView.getItems().remove(selectedIndex);
			listView.getSelectionModel().select(newSelectedIdx); // select the next item
		}
		return itemToRemove;
	}
	
	public void clearListView() {
		listView.getItems().clear();
	}

	/**
	 * return the list view
	 * 
	 * @return
	 */
	public ListView<Meal> getListView() {
		return listView;
	}

}
