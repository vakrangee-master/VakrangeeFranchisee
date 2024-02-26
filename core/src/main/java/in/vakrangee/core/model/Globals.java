package in.vakrangee.core.model;

/**
 * Created by Nileshd on 2/21/2017.
 */

public class Globals {

    private static Globals instance = new Globals();

    // Getter-Setters
    public static Globals getInstance() {
        return instance;
    }

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    private int notification_index;


    private Globals() {

    }

    public int getValue() {
        return notification_index;
    }

    public void setValue(int notification_index) {
        this.notification_index = notification_index;
    }

}
