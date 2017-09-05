import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Model model = new Model();
                MainView mainView = new MainView(model);
                model.addObserver(mainView);
            }
        });
    }
}
