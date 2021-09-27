package edu.uwm.cs351;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import edu.uwm.cs351.util.Queue;

public class BlockQueue {
	private final JFrame frame;
	private final MyPanel panel = new MyPanel();
	private static Queue<Block> pending;
	private static Queue<Block> random;
	private static Queue<Block> fixed;
	private int supplyType;
	
	private static Block getNextBlock(Queue<Block> q) {
		Block thing = q.dequeue();
		
		q.enqueue(thing);
		return thing;
		//implement this method to get the next block from the selected queue
		//Make sure you put the block back at the end of the queue so we don't run out
	}

	public static void main(String[] args) {
		//initialize the random queue with the pieces from the RBG table
		random = new Queue<Block>();
		for(Block b : Block.RBG_TABLE)
			random.enqueue(b);
		// seed the random queue based on system time, so it doesn't always start the same
		long seed = System.currentTimeMillis() % random.size();
		// advance (?enqueue)? the random queue a number of times equal to seed
		int number = (int)seed;
		for (int i = 0; i < number; i++) {
			Block thinger = random.dequeue();
			random.enqueue(thinger);
		}
		//initialize the fixed queue with one of each block
		fixed = new Queue<Block>();
		Block[] values = Block.values();
		for (int i = 0; i < values.length; i++) {
			fixed.enqueue(values[i]);
		}
		// Add one of each block to fixed in a fixed order.
		//You can use Block.values().
		
		//initialize the pending queue with two blocks from the random queue
		//this is for the initial display
		pending = new Queue<Block>();
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				new BlockQueue();
			}
		});
	}
	
	private class MyPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		Block current = pending.dequeue();
		Block next = pending.front();
		JLabel print = new JLabel("Pending: " + pending.toString());
		
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			current.draw(g, current, 40, 20, 40);
			next.draw(g, next, 20, 30, 160);
			print.setText("Pending: " + pending.toString());
		}
	}
/**
 * Renders a Frame which shows a current and
 * upcoming Block within a queue
 * 
 * Supply types:
 * 
 * 0- We see blocks in the same repeating sequence: Z,S,L,J,O,I,T.
 * 
 * 1- Random piece generation. We supply pieces drawn uniformly at random from the set of blocks.
 * Any amount of time may pass before seeing the same piece
 * (this is standard in most classic Tetris games)
 * 
 * 2- 7 Seq randomizer. Blocks are supplied as a series of permutations of the 7 blocks.
 * This means that we see one of each block in some order before we get a new set
 * of the same blocks in a different order.
 * No more than a 12 pieces will pass before the same piece is seen again.
 * This would happen if that block is the first block in one permutation,
 * and then the last block in the next permutation.
 * (this is standard in most modern Tetris games)
 * 
 * 3- 14 Seq randomizer. We instead see two of each block before a new set begins.
 * This means we are supplying permutations of a collection of 14 blocks (2 of each).
 * Longer times between repeat pieces is possible.
 */
	public BlockQueue() {
		supplyType = 1; // fully random by default
		frame = new JFrame("Block Queue");		
		frame.setSize(500, 340);

		JButton advance = new JButton("Advance");
		advance.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(pending.size() <= 1) {  //if there is only one left in the pending queue
											// we need to supply more blocks to the pending queue
					switch(supplyType) {
					case 0: // set supply, giving one of each block in a fixed order
						// Copy the fixed queue to the pending queue
						for (int i = 0 ; i < 7; i++) {
							Block thing = fixed.dequeue();
							pending.enqueue(thing);
							fixed.enqueue(thing);
						}
						// Make sure the fixed queue ends up in the same state
					break;
					case 1:// fully random
						Block thing = random.dequeue();
						pending.enqueue(thing);
						random.enqueue(thing);
						// Add the next block from the random queue to the pending queue.
						// You can use your private getNextBlock() method.
					break;
					case 2:// 7-supply
						int unique = 0;
						Block[] added = new Block[7];
						int nextIndex = 0;
						while (unique != 7) {
							Block blocky = random.dequeue();
							boolean found = false;
							
							for (int i = 0; i < added.length; i++) {
								if (added[i] != null && added[i].equals(blocky)) {
									found = true;
								}
							}
							if (!found) {
								pending.enqueue(blocky);
								added[nextIndex] = blocky;
								nextIndex++;
								unique++;
							}
							random.enqueue(blocky);
						}
						// Get blocks from the random queue to add to pending
						// Each of the 7 unique blocks should be added once
						// If you get a block that has already been added, skip it
					break;
					case 3:// 14-supply
						int count = 0;
						Block[] stuff = new Block[14];
						int nextInde = 0;
						while (count != 14) {
							Block blocky = random.dequeue();
							int found = 0;
							
							for (int i = 0; i < stuff.length; i++) {
								if (stuff[i] != null && stuff[i].equals(blocky)) {
									found++;
								}
							}
							if (found < 2) {
								pending.enqueue(blocky);
								stuff[nextInde] = blocky;
								nextInde++;
								count++;
							}
							random.enqueue(blocky);
						}
						// Get blocks from the random queue to add to pending
						// Each of the 7 unique blocks should be added twice
						// If you get a block that has already been added twice, skip it
					break;
					}
				}
				Block thing = pending.dequeue();
				panel.current = thing;
				// Remove a block from pending and store it in panel.current,
				// and panel.next should become the front-most block in pending.
				panel.repaint();
			}
		});
		JButton inOrder = new JButton("Ordered");
		inOrder.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				supplyType = 0;
			}
		});
		JButton random = new JButton("Random");
		random.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				supplyType = 1;
			}
		});
		JButton sup_7 = new JButton("7 Sup");
		sup_7.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				supplyType = 2;
			}
		});
		JButton sup_14 = new JButton("14 Sup");
		sup_14.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				supplyType = 3;
			}
		});
		
		JLabel current = new JLabel("Current");
		JLabel next = new JLabel("Next");
		panel.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		
		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(random);
		buttonPanel.add(inOrder);
		buttonPanel.add(sup_7);
		buttonPanel.add(sup_14);
		
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(panel.print, BorderLayout.NORTH);
		southPanel.add(advance, BorderLayout.SOUTH);
		
		panel.add(buttonPanel, BorderLayout.EAST);
		panel.add(southPanel, BorderLayout.SOUTH);
		panel.add(current, BorderLayout.NORTH);
		panel.add(next, BorderLayout.CENTER);

		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}




}



