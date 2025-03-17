package mvc;
import java.awt.Graphics;
import javax.swing.JPanel;

public class View extends JPanel implements Subscriber{
    protected Model model;

    public View(Model model){
        this.model = model;
        model.subscribe(this);
    }

    public void setModel(Model newModel) {
        this.model.unsubscribe(this);
        this.model = newModel;
        model.subscribe(this);
    }

    public void update() {
        this.repaint();
    }

    public void paintComponent(Graphics gc) {
        super.paintComponent(gc);
    }

}
