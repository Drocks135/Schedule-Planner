import GUI.GUI_Launcher;
import projEvents.EventStorage;

public class Main {
    public static void main(String[] args) {
        GUI_Launcher gui = new GUI_Launcher();
        EventStorage eventStorage = EventStorage.getInstance();

        gui.launchGUI(args);


    }
}
