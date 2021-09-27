import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.uwm.cs351.Pixel;
import edu.uwm.cs351.Raster;





/**
 * Render some pixels on the screen.
 */
public class Demo extends JFrame {
	/**
	 * Eclipse wants this
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Demo x = new Demo();
				x.setSize(500, 500);
				x.setVisible(true);
				x.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			}
		});
	}

	@SuppressWarnings("serial")
	public Demo() {
		this.setContentPane(new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				int width = 25;
				
				
				Raster r = new Raster(10,10);
				r.addPixel(new Pixel(8, 8, Color.BLACK));
				r.getPixel(9, 9).invert();
				r.draw(g, width);
	
				Pixel test1 = new Pixel(new Point(0,0), Color.CYAN );
				Pixel test2 = new Pixel(new Point(1,0), Color.RED );
				Pixel test3 = new Pixel(new Point(2,0), Color.BLUE );
				Pixel test4 = new Pixel(new Point(0,1), new Color(230, 230, 170) );
				Pixel test5 = new Pixel(new Point(0,2), Color.GREEN );
				Pixel test6 = new Pixel(new Point(1,1), Color.BLACK );
				Pixel test7 = new Pixel(new Point(2,1), Color.GRAY );
				Pixel test8 = new Pixel(new Point(1,2), Color.YELLOW );
				Pixel test9 = new Pixel(new Point(2,2), Color.MAGENTA );
				test3.invert();
				test1.draw(g,width);
				test2.draw(g,width);
				test3.draw(g,width);
				test4.draw(g,width);
				test5.draw(g,width);
				test6.draw(g,width);
				test7.draw(g,width);
				test8.draw(g,width);
				test9.draw(g,width);

			}
		});
	}
}
