import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.uwm.cs351.RasterSeq;
import edu.uwm.cs351.Raster;
import edu.uwm.cs351.Pixel;

public class VideoDemo extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private Raster[] frames = new Raster[17];
	private RasterSeq video = new RasterSeq();
	private static final int FRAMERATE = 25;
	private static final int PIXELWIDTH = 50;
	private static final int X = 850;
	private static final int Y = 500;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				VideoDemo x = new VideoDemo();
				x.setSize(X, Y);
				x.setLocationRelativeTo(null);
				x.setVisible(true);
				x.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				
				x.createFrames();
				x.buildVideo();
				x.playVideo();
				x.sloMo(4);
				x.playVideo();
			}
		});
		
	}
	
	@SuppressWarnings("serial")
	public VideoDemo() {
		this.setContentPane(new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				int width = PIXELWIDTH;
				if(video.hasCurrent())
					video.getCurrent().draw(g, width);
			}
		}
		);
	}
	
	/*
	 * 
	 * Create a video that is a RasterSeq
	 * The video should consist of 5 copies of frames[0]
	 * followed by 1 copy each of frames[1] through frames[16]
	 * And finally 5 more copies of frames[0]
	 * You should use loops so that you don't have too many lines of code
	 */
	public void buildVideo() {
		video = new RasterSeq();
		//TODO: Implement this
	}
	
	/*
	 * Slow down a video by a factor
	 * You should accomplish this by adding factor - 1 copies of each frame
	 * For example, if the factor is 2, you are doubling each frame
	 * Note: for these purposes, a copy can be a reference to the same Raster
	 * Also note: this operation on a dynamic array is not that efficient. Think about it.
	 */
	public void sloMo(int factor) {
		if(factor < 1)
			throw new IllegalArgumentException();
		//TODO: Implement this
	}
	
	public void playVideo() {
		video.start();
		while(video.hasCurrent()) {
			this.getContentPane().paint(this.getGraphics());
			try {
	            Thread.sleep(1000/FRAMERATE);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
			video.advance();
		}
	}
	
	public void createFrames() {
		for(int i=0; i<17; i++)
			frames[i] = new Raster(16, 9);
		for(int i=1; i<17; i++)
			frames[i].addPixel(new Pixel(i-1,8,Color.BLACK));
	}
}
