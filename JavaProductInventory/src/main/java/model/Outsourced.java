package model;

/**
 * An object to represent a part that is outsourced.
 * @author Kurtis
 */
public class Outsourced extends Part {

    /** The outsourced part company name */
    private String companyName;


    /**
     * Constructs a new outsourced part.
     * All the part members are set with this constructor, including the outsourced company name member.
     * @param id the part id.
     * @param name the part name.
     * @param price the part price.
     * @param stock the part stock/inventory.
     * @param min the part min stock/inventory.
     * @param max the part max stock/inventory.
     * @param companyName the name of the company that the part is sourced from.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Gets the company name.
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the company name.
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
