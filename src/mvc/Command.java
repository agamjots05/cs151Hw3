package mvc;

public abstract class Command {
    Model model;
    public Command(Model model) {
        this.model = model;
    }
    public abstract void execute();
}
