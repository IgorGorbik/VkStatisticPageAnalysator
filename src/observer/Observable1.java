package observer;


import observer.Observer1;


/**
 *
 * @author Игорь
 */
public interface Observable1 {

    void addObserver(Observer1 o);

    void deleteObserver(Observer1 o);

    void notifyObservers();

    void notifyObservers(Object arg);

    void deleteObservers();

    void setChanged();

    void clearChanged();

    boolean hasChanged();

    int countObservers();

}
