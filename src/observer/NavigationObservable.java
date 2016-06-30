package observer;

import java.util.Observable;

public class NavigationObservable extends Observable {

	public void change() {
		setChanged();
		notifyObservers();
	}
}
