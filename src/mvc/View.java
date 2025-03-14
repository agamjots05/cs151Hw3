package mvc;
import java.awt.Graphics;
import javax.swing.JPanel;

public class View extends JPanel implements Subscriber{
    protected Model model;

    public View(Model model){
        this.model = model;
        model.subscribe(this);
    }

    public void setView(Model newModel)
    {
        model.unsubscribe(this);
        this.model = model;
        model.subscribe(this);
    }

    public void update() {
        this.repaint();
    }

    public void paintComponent(Graphics gc) {
        super.paintComponent(gc);
    }

}
