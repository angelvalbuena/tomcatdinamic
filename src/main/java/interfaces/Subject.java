package interfaces;

public interface Subject {

	public void AddObserver(Observer e);
	public void RemoveObserver(Observer e);
	public void NotifyObservers();
	
}
