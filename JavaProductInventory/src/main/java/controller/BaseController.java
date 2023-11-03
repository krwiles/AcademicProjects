package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * An abstract class to hold common methods used by multiple controllers.
 * @author Kurtis
 */
public abstract class BaseController {

    /** Holds the main stage of the application */
    protected Stage stage;
    /** Holds any new scene */
    protected Parent scene;

    /** This emum represents whether the form will be adding or modifying. */
    protected enum FormMode {
        /** This state indicates the form is going to add a part or product */
        ADD,
        /** This state indicates the form is going to modify a part or product */
        MODIFY
    }


    /**
     * Loads a new scene in the current window of a button.
     * @param event ActionEvent from the button's action listener
     * @param location path to the resource to be loaded
     * @throws IOException if the loader fails to find the resource
     */
    protected void loadViewFromButtonPress(ActionEvent event, String location) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(location));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Loads a new scene in the current window of a button.
     * @param event ActionEvent from the button action listener
     * @param loader FXMLLoader with the desired location already set and loaded
     */
    protected void loadViewFromButtonPress(ActionEvent event, FXMLLoader loader) {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Creates a dialog box indicating that no item has been selected.
     */
    protected void alertUnselectedItem() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Please select an item first.");
        alert.showAndWait();
    }

    /**
     * Creates a dialog box asking for confirmation to return to the main menu.
     * @return true if the user presses OK
     */
    protected boolean alertCancelConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Any unsaved changes will be lost!");
        alert.setContentText("Press OK to exit to the main menu.");

        Optional<ButtonType> answer = alert.showAndWait();
        return (answer.isPresent() && answer.get() == ButtonType.OK);
    }

    /**
     * Creates a dialog box asking for confirmation about a deletion.
     * @param verb the verb to be said (e.g. "Delete" or "Remove")
     * @param item the name of the item to be deleted/removed
     * @return true if the user presses OK
     */
    protected boolean alertDeleteConfirmation(String verb, String item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(verb + " " + item + "?");
        alert.setContentText("Press OK to " + verb + ".");

        Optional<ButtonType> answer = alert.showAndWait();
        return (answer.isPresent() && answer.get() == ButtonType.OK);
    }

    /**
     * Creates a dialog box indicating that a deletion has failed.
     * @param item the name of the item being deleted
     */
    protected void alertDeleteFailed(String item) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("An error has occurred!");
        alert.setContentText("Deletion of " + item + " has failed.");
        alert.showAndWait();
    }

    /**
     * Creates a dialog box indicating an input error.
     * @param content the content of the message to be shown
     */
    protected void alertInputError(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Input error!");
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Creates a generic dialog box indicating an input error.
     */
    protected void alertInputError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Input error!");
        alert.setContentText("Please enter a valid value for each text field.\n(Ensure only numbers are present in fields that expect numbers)");
        alert.showAndWait();
    }

    /**
     * Validates a set of given min, max, and inventory values.
     * @param min the min to be checked
     * @param max the max to be checked
     * @param inv the inventory value to be checked
     * @throws Exception if the test is failed, with a message detailing the failure
     */
    protected void validateMinMaxInv(int min, int max, int inv) throws Exception {
        if (min > max) {
            throw new Exception("Min value greater than Max.");
        }
        if (inv < min || inv > max) {
            throw new Exception("Inventory value outside of Min/Max.");
        }
    }

}
