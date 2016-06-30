package gui;

public class LessonPanel {

	private int x;
	private int y;
	private int width;
	private int height;
	private String info;
	
	public LessonPanel(int x, int y, int width, int height, String info) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.info = "<html>";
		for (String line : info.split("\n")) {
			this.info += line + "<br>";
		}
		this.info += "</html>";
	}
	
	public boolean isIn(int x, int y) {
		if (x < this.x || x > this.x + width)
			return false;
		if (y < this.y || y > this.y + height)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return info;
	}
	
	public boolean collision(LessonPanel pnlLesson) {
		if (isIn(pnlLesson.x, pnlLesson.y))
			return true;
		if (isIn(pnlLesson.x + pnlLesson.width, pnlLesson.y))
			return true;
		if (isIn(pnlLesson.x, pnlLesson.y + pnlLesson.height))
			return true;
		if (isIn(pnlLesson.x + pnlLesson.width, pnlLesson.y + pnlLesson.height))
			return true;
		return false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	protected void increaseY(int height) {
		this.y += height;
	}
}
