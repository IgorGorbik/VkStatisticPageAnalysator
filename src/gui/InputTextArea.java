package gui;

import java.util.Vector;
import javax.swing.JTextArea;
import observer.Observable1;
import observer.Observer1;

/**
 *
 * @author Игорь
 */
public class InputTextArea extends JTextArea implements Observable1 {

    private boolean changed = false;
    private Vector<Observer1> obs;

    public InputTextArea() {
        obs = new Vector<>();
    }

    public void run() {
        setChanged();
        notifyObservers(getText());
    }

    @Override
    public void addObserver(Observer1 o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }

    @Override
    public void deleteObserver(Observer1 o) {
        obs.removeElement(o);
    }

    @Override
    public void notifyObservers() {
        notifyObservers(null);
    }

    @Override
    public void notifyObservers(Object arg) {
        Object[] arrLocal;
        synchronized (this) {
            if (!changed) {
                return;
            }
            arrLocal = obs.toArray();
            clearChanged();
        }
        for (int i = arrLocal.length - 1; i >= 0; i--) {
            ((Observer1) arrLocal[i]).update(this, arg);
        }
    }

    @Override
    public synchronized void deleteObservers() {
        obs.removeAllElements();
    }

    @Override
    public void setChanged() {
        changed = true;
    }

    @Override
    public void clearChanged() {
        changed = false;
    }

    @Override
    public boolean hasChanged() {
        return changed;
    }

    @Override
    public int countObservers() {
        return obs.size();
    }

}
