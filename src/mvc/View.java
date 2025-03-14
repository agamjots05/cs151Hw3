package mvc;
import javax.swing.JFrame;

public class View extends JFrame implements Subscriber{
    protected Model model;

    public View(Model model){
        this.model = model;
        model.subscribe(this);
    }

    public void update() {
        this.repaint();
    }

}
