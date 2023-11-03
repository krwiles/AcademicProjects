package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A class used to store and keep track of part and product objects.
 * @author Kurtis
 */
public class Inventory {

    /** The list that holds all parts */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    /** The list that holds all products */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /**
     * Adds a part to the list of all parts.
     * <p>
     *     RUNTIME ERROR allParts was null. It needed to be initialized by FXCollections.observableArrayList()
     * </p>
     * @param newPart the part to be added
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a product to the list of all products.
     * @param newProduct the product to be added
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Searches for a part with a given id in the list of all parts.
     * @param partId the id of the part to look up
     * @return the part with a matching id, or null if no part is found
     */
    public static Part lookupPart(int partId) {
        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     * Searches for a product with a given id in the list of all products.
     * @param productId the id of the product to look up
     * @return the product with a matching id, or null if no product is found
     */
    public static Product lookupProduct(int productId) {
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Searches for parts with a given name in the list of all parts.
     * Parts are also included on partial matches, and an empty list will be returned if no match is found.
     * @param partName the name to search
     * @return an observablelist of parts with matching names
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();

        for (Part part : allParts) {
            if (part.getName().contains(partName)) {
                filteredParts.add(part);
            }
        }
        return filteredParts;
    }

    /**
     * Searches for products with a given name in the list of all products.
     * Products are also included on partial matches, and an empty list will be returned if no match is found.
     * @param productName the name to search
     * @return an observablelist of parts with matching names
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();

        for (Product product : allProducts) {
            if (product.getName().contains(productName)) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    /**
     * Updates a part in the list of all parts.
     * @param index the index of the part in the list of all parts
     * @param part the new part to replace the old part
     */
    public static void updatePart(int index, Part part) {
        allParts.set(index, part);
    }

    /**
     * Updates a product in the list of all products.
     * @param index the index of the product in the list of all products
     * @param product the new product to replace the old product
     */
    public static void updateProduct(int index, Product product) {
        allProducts.set(index, product);
    }

    /**
     * Deletes a part from the list of all parts.
     * @param selectedPart the part to delete
     * @return true if the deletion was successful
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Deletes a product from the list of all products.
     * @param selectedProduct the product to delete
     * @return true if the deletion was successful
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Gets the list of all parts.
     * @return the observablelist of all parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Gets the list of all products.
     * @return the observablelist of all products.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
