package mvc;

public interface Subscriber {
    void notifySubscribers(Publisher publisher);

    public void update();
}
