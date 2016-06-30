package data;

import handlers.InferenceHandler;

public abstract class Action {

	public abstract void undoAction(InferenceHandler handler);
}
