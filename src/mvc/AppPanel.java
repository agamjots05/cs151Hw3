package mvc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// AppPanel is the MVC controller
public class AppPanel extends JPanel implements Subscriber, ActionListener  {

    protected Model model;
    protected AppFactory factory;
    protected View view;
    protected JPanel controlPanel;
    private JFrame frame;
    public static int FRAME_WIDTH = 500;
    public static int FRAME_HEIGHT = 300;

    public AppPanel(AppFactory factory) {

        // initialize fields here
        this.factory = factory;
        this.controlPanel = new ControlPanel();

        this.model = factory.makeModel();
        this.view = factory.makeView(this.model);

        this.add(this.controlPanel);
        this.add(this.view);

        // Layout
        this.setLayout((new GridLayout(1, 2)));

        frame = new SafeFrame();
        Container cp = frame.getContentPane();
        cp.add(this);
        frame.setJMenuBar(createMenuBar());
        frame.setTitle(factory.getTitle());
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    public void display() { frame.setVisible(true); }

    public void update() {  /* override in extensions if needed */ }

    public Model getModel() { return model; }

    // called by file/open and file/new
    public void setModel(Model newModel) {
        this.model.unsubscribe(this);
        this.model = newModel;
        this.model.subscribe(this);
        // view must also unsubscribe then resubscribe:
        view.setView(this.model);
        model.changed();
    }

    protected JMenuBar createMenuBar() {
        JMenuBar result = new JMenuBar();
        // add file, edit, and help menus
        JMenu fileMenu =
                Utilities.makeMenu("File", new String[] {"New",  "Save", "SaveAs", "Open", "Quit"}, this);
        result.add(fileMenu);

        JMenu editMenu =
                Utilities.makeMenu("Edit", factory.getEditCommands(), this);
        result.add(editMenu);

        JMenu helpMenu =
                Utilities.makeMenu("Help", new String[] {"About", "Help"}, this);
        result.add(helpMenu);

        return result;
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String cmmd = ae.getActionCommand();

            if (cmmd.equals("Save")) {
                Utilities.save(model, false);
            } else if (cmmd.equals("SaveAs")) {
                Utilities.save(model, true);
            } else if (cmmd.equals("Open")) {
                Model newModel = Utilities.open( (Model) model);
                if (newModel != null) setModel(newModel);
            } else if (cmmd.equals("New")) {
                Utilities.saveChanges(model);
                setModel(factory.makeModel());
                // needed cuz setModel sets to true:
                model.setUnsavedChanges(false);
            } else if (cmmd.equals("Quit")) {
                Utilities.saveChanges(model);
                System.exit(0);
            } else if (cmmd.equals("About")) {
                Utilities.inform(factory.about());
            } else if (cmmd.equals("Help")) {
                Utilities.inform(factory.getHelp());
            } else { // must be from Edit menu
                executeCommand(cmmd);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }
    protected class ControlPanel extends JPanel implements ActionListener {

        @Override
        public Component add(Component comp) {
            ((JButton) comp ).addActionListener(this);
            super.add(comp);
            return comp;
        }
    
		@Override
		public void actionPerformed(ActionEvent ae) {
            String cmmd = ae.getActionCommand();
			try {
                executeCommand(cmmd);
            } catch (Exception e) {
                handleException(e);
            }
		}
    }

    private void executeCommand(String cmmd) {
        for (String editCmd : factory.getEditCommands()) {
            if (cmmd.equals(editCmd)) {
                Command cmd = factory.makeEditCommand(model, editCmd, null);
                cmd.execute();
            }
        }
    }

    protected void handleException(Exception e) {
        Utilities.error(e);
    }
}
