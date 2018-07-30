/**
 * Date: 04.07.14
 * Time: 23:57
 */
public interface Subject {

    public void registerObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObservers();
}
