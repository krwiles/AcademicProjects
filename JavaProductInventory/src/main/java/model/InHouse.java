package model;

/**
 * An object to represent a part created in-house.
 * @author Kurtis
 */
public class InHouse extends Part{

    /** The ih-house part machine id */
    private int machineId;


    /**
     * Constructs a new in-house part.
     * All the part members are set with this constructor, including the in-house machineId member.
     * @param id the part id
     * @param name the part name
     * @param price the part price
     * @param stock the part stock/inventory
     * @param min the part min stock/inventory
     * @param max the part max stock/inventory
     * @param machineId the id of the machine that makes this part
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Gets the id of the machine that makes this part.
     * @return the machineId
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * Sets the id of the machine that makes this part.
     * @param machineId the machineId to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

}
