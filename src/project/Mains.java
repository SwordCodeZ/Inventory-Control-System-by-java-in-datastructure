package project;

public class Mains {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                InventoryStore.load();        // Load inventory from file
                new LoadingScreen();          // Show splash and then menu
            }
        });
    }
}
