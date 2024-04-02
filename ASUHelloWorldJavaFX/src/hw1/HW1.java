package hw1;    
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class HW1 extends Application {

    private ArrayList<CheckBox> eatCheckBoxes = new ArrayList<>();
    private ToggleGroup drinkToggleGroup = new ToggleGroup();
    private TextArea billTextArea = new TextArea();

    // Makes the prices of the food absolute
    private final double EGG_SANDWICH_PRICE = 7.99;
    private final double CHICKEN_SANDWICH_PRICE = 9.99;
    private final double BAGEL_PRICE = 2.50;
    private final double POTATO_SALAD_PRICE = 4.49;

    // Makes the prices of the drinks absolute
    private final double COFFEE_PRICE = 1.99;
    private final double GREEN_TEA_PRICE = 0.99;
    private final double BLACK_TEA_PRICE = 1.25;
    private final double ORANGE_JUICE_PRICE = 2.25;

    @Override
    public void start(Stage primaryStage) {
    	primaryStage.setTitle("Joe's Deli Register");
        // Sets the title
        Label headerLabel = new Label("Welcome to Joe's Deli!");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(headerLabel, createGridPane());

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Eat column list
        VBox eatVBox = new VBox(5);
        eatVBox.setAlignment(Pos.TOP_LEFT);
        Label eatLabel = new Label("Eat:");
        eatVBox.getChildren().add(eatLabel);
        CheckBox eggSandwichCheckBox = new CheckBox("Egg Sandwich - $" + EGG_SANDWICH_PRICE);
        CheckBox chickenSandwichCheckBox = new CheckBox("Chicken Sandwich - $" + CHICKEN_SANDWICH_PRICE);
        CheckBox bagelCheckBox = new CheckBox("Bagel - $" + BAGEL_PRICE + "0");
        CheckBox potatoSaladCheckBox = new CheckBox("Potato Salad - $" + POTATO_SALAD_PRICE);
        eatCheckBoxes.add(eggSandwichCheckBox);
        eatCheckBoxes.add(chickenSandwichCheckBox);
        eatCheckBoxes.add(bagelCheckBox);
        eatCheckBoxes.add(potatoSaladCheckBox);
        eatVBox.getChildren().addAll(eggSandwichCheckBox, chickenSandwichCheckBox, bagelCheckBox, potatoSaladCheckBox);
        Button orderButton = new Button("Order");
        orderButton.setOnAction(e -> calculateBill());
        eatVBox.getChildren().add(orderButton);
        gridPane.add(eatVBox, 0, 0);

        // Drink column list
        VBox drinkVBox = new VBox(5);
        drinkVBox.setAlignment(Pos.TOP_LEFT);
        Label drinkLabel = new Label("Drink:");
        drinkVBox.getChildren().add(drinkLabel);
        RadioButton coffeeRadioButton = new RadioButton("Coffee - $" + COFFEE_PRICE);
        coffeeRadioButton.setToggleGroup(drinkToggleGroup);
        RadioButton greenTeaRadioButton = new RadioButton("Green Tea - $" + GREEN_TEA_PRICE);
        greenTeaRadioButton.setToggleGroup(drinkToggleGroup);
        RadioButton blackTeaRadioButton = new RadioButton("Black Tea - $" + BLACK_TEA_PRICE);
        blackTeaRadioButton.setToggleGroup(drinkToggleGroup);
        RadioButton orangeJuiceRadioButton = new RadioButton("Orange Juice - $" + ORANGE_JUICE_PRICE);
        orangeJuiceRadioButton.setToggleGroup(drinkToggleGroup);
        drinkVBox.getChildren().addAll(coffeeRadioButton, greenTeaRadioButton, blackTeaRadioButton, orangeJuiceRadioButton);
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> clearSelections());
        drinkVBox.getChildren().add(cancelButton);
        gridPane.add(drinkVBox, 1, 0);
        

        // Bill column
        VBox billVBox = new VBox(5);
        billVBox.setAlignment(Pos.TOP_LEFT);
        Label billLabel = new Label("Bill:");
        billTextArea.setEditable(false);
        billVBox.getChildren().addAll(billLabel, billTextArea);
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            double total = calculateTotal();
            DecimalFormat df = new DecimalFormat("#.00");
            billTextArea.setText("Total: $" + df.format(total) + "\nHave a nice Day!");
            billTextArea.setStyle("-fx-font-weight: bold;");
       
        });
        //adjusts the TextArea box to look more proportional
        billTextArea.setPrefHeight(85);
        billTextArea.setPrefWidth(200);
        
        billVBox.getChildren().addAll(confirmButton);
        gridPane.add(billVBox, 2, 0);

        return gridPane;
    }

    private double calculateTotal() {
        double total = 0.00;

        // Calculate eat items cost
        for (CheckBox checkBox : eatCheckBoxes) {
            if (checkBox.isSelected()) {
                String[] parts = checkBox.getText().split("\\$");
                double price = Double.parseDouble(parts[1]);
                total += price;
            }
        }

        // Calculate drink cost
        RadioButton selectedDrink = (RadioButton) drinkToggleGroup.getSelectedToggle();
        if (selectedDrink != null) {
            String[] parts = selectedDrink.getText().split("\\$");
            double price = Double.parseDouble(parts[1]);
            total += price;
        }

        // Calculate 7% sales tax
        total *= 1.07; // 

        return total;
    }

    private void calculateBill() {
        double total = calculateTotal();
        StringBuilder billText = new StringBuilder();

        // Append selected eat items to billText
        for (CheckBox checkBox : eatCheckBoxes) {
            if (checkBox.isSelected()) {
                billText.append(checkBox.getText()).append("\n");
            }
        }

        // Append selected drink to billText
        RadioButton selectedDrink = (RadioButton) drinkToggleGroup.getSelectedToggle();
        if (selectedDrink != null) {
            billText.append(selectedDrink.getText()).append("\n");
        }

        // Display total in billTextArea
        DecimalFormat df = new DecimalFormat("#.00");
        billText.append("Total: $").append(df.format(total));
        billTextArea.setText(billText.toString());
    }
    

    private void clearSelections() {
        for (CheckBox checkBox : eatCheckBoxes) {
            checkBox.setSelected(false);
        }
        drinkToggleGroup.selectToggle(null);
        billTextArea.clear();
        billTextArea.setStyle("-fx-font-weight: normal;");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
