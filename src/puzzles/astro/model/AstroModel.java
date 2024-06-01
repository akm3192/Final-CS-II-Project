package puzzles.astro.model;

import puzzles.common.Observer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AstroModel {
    /** the collection of observers of this model */
    private final List<Observer<AstroModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private AstroConfig currentConfig;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<AstroModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void notifyObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    public AstroModel(String filename) throws IOException {
        try {
            this.currentConfig = new AstroConfig(filename);
        }
        catch (IOException e) {}
    }
}
