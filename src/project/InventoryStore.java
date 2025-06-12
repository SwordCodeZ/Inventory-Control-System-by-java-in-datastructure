package project;

public class InventoryStore {
    public static BinaryTree inventoryTree = new BinaryTree();
    public static final String DATA_FILE = "inventory_data.txt";

    public static void load() {
        inventoryTree.loadFromFile(DATA_FILE);
    }

    public static void save() {
        inventoryTree.saveToFile(DATA_FILE);
    }
}
