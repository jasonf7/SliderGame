import java.awt.event.*; // import statements
import java.text.NumberFormat;

/**
 * This class deals with my timer.
 * @author Jason Fang
 * @since November 30, 2011
 */
public class TimeClass implements ActionListener
{
	private static double counter; // counter variable
	
	/**
	 * This constructor takes the number of seconds the game starts out with
	 * @param seconds: The number of seconds that the user has
	 */
	public TimeClass(double seconds)
	{
		this.counter = seconds;
	} // end constructor
	
	/**
	 * This mutator method sets the counter variable to a new number.
	 * @param seconds: the new amount of seconds
	 */
	public void setCounter(double seconds)
	{
		counter = seconds;
	} // end setCounter method
	
	/**
	 * This accessor method returns the counter variable.
	 * @return the counter variable as a string to 2 decimal places
	 */
	public String getCounter()
	{
		return NumberFormat.getInstance().format(counter);
	}
	
	/**
	 * This method is called whenever the timer class is called (once ever 0.01s / 10ms)
	 * @param tc: The actionEvent variable
	 */
	public void actionPerformed(ActionEvent tc) 
	{
		// takes off a hundreth of a second away from the time left
		counter = counter + 0.01;
		GameGUI.timeLabel.setText("Time Elapsed: " + NumberFormat.getInstance().format(counter) + "s");
	} // end actionPerformed method
} // end TimeClass class
