package observer;

import java.util.Observable;
import java.util.Observer;

public class GroupObserver extends Observable implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers(arg);
	}
}
