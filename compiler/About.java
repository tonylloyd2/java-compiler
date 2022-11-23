/**
  * Subject :Programming Languages
  * Code : TCS 2073
  * Lecturer: Mr. Awad Mohed
  * Description : About info - This piece of source code is taken from internet
  * Written by : BALA SINGAM
  * Student ID : B00349
  * Email : bala@singam.net
  * URL : www.singam.net/java
  * Date : 5th September 2004
*/




//import the packages for using the classes in them into this class
import java.awt.*;
import javax.swing.*;

/**
 *A CLASS FOR CREATING ABOUT PANEL
 */
public class About extends JPanel{
	public About(){
		//Create a Label & an image icon in it
		JLabel label1 = new JLabel(new ImageIcon("icons/B00349.jpg"));
		//adding label1 to the JPanel
	  	this.add(label1);
	  	//Create a Label & put a HTML script
		
                JLabel label2 = new JLabel("<html><li>Simple Compiler™</li><li><p>Ver 1.0</li>"
		+"<li><p>Developed by: BALA SINGAM</li><li><p>SICT, KUTPM</li><li>"
                +"<p>TCS 2073 - Programming Languages</li>"
		+"<li><p>Lecture : MR.Awad Mohed</li>"
		+"<li><p>CopyRight© 2004 www.singam.net</li></html>");
		
                //adding label2 to the JPanel
		this.add(label2);
	}
}