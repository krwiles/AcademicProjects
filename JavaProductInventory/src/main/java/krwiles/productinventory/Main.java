package krwiles.productinventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the main class that runs the application.
 * <p>
 *     FUTURE ENHANCEMENT a good future enhancement to make for this application would be to address the situation of when a part is modified or deleted from the all parts list but also remains in a product's associated parts list.
 *     Two solutions come to mind: The first being to search all products' associated parts lists and remove/update the part there also.
 *     The second being to prevent parts from being deleted/modified if they are in any associated parts lists.
 *     In both cases, it maybe useful to create a list in the part class to hold references to the products containing that part.
 * </p>
 */
public class Main extends Application {

    /**
     * This is the method used to start the program.
     * @param stage the main stage used by the application
     * @throws IOException if the fxml file is not found
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    // JavaDoc comments can be found in ProjectMain/javadoc

    /**
     * This is the main method of the application. It calls the launch() method.
     * @param args args
     */
    public static void main(String[] args) {
        launch();
    }
}