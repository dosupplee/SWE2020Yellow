package ui;

import droneSim.Meal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CustomMealListCell extends ListCell<Meal> {
	private VBox content;
    private Label name;
    private Label weight;
    private Label probability;
    private Label mealContents;
    
    private final int SMALL_FONT_SIZE = 12;
    private final int LARGE_FONT_SIZE = 14;

    public CustomMealListCell() {
        super();
        name = new Label();
       // name.setMinWidth(150);
        //name.setMaxWidth(150);
        name.setWrapText(true);
        name.setFont(Font.font("Arial", FontWeight.BOLD, LARGE_FONT_SIZE));
        
        weight = new Label();
        probability = new Label();
        mealContents = new Label();
        
        weight.setFont(Font.font("Arial",SMALL_FONT_SIZE));
        probability.setFont(Font.font("Arial",SMALL_FONT_SIZE));
        mealContents.setFont(Font.font("Arial",SMALL_FONT_SIZE));
        
        VBox vBox = new VBox(weight, probability, mealContents);
        double spacing = 2;
        double leftSpacing = 25;
        Insets insets = new Insets(spacing, spacing, spacing, leftSpacing);
        VBox.setMargin(weight, insets);
        VBox.setMargin(probability, insets);
        VBox.setMargin(mealContents, insets);
        
        content = new VBox(name, vBox);
        content.setSpacing(5);
        content.setAlignment(Pos.CENTER_LEFT);
        
        /*
         * Setup of cell
         * 
         * Vbox{
         * 	name
         * 	Vbox{
         * 		weight
         * 		probability
         * 		contents
         * 	}
         * }
         * 
         */
    }

    /**
     * Populate the list view cell
     */
    @Override
    protected void updateItem(Meal meal, boolean empty) {
        super.updateItem(meal, empty);
        if (meal != null && !empty) { // <== test for null item and empty parameter
            name.setText(meal.getName());
            
            weight.setText(String.format("%-11s %d (oz)", "Weight:",meal.getWeight()));
            probability.setText(String.format("%-11s  %.2f -> %.2f%%", "Probibilty:", meal.getRawProbability(), (meal.getScaledProbability()*100)));
            mealContents.setText(String.format("%-11s %s", "Contents:", meal.getFoodNames()));

            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}
