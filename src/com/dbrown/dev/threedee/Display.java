package com.dbrown.dev.threedee;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.dbrown.dev.threedee.graphics.Render;
import com.dbrown.dev.threedee.graphics.Screen;

public class Display extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 1024;
	public static final int HEIGHT = WIDTH / 16 * 9;
	public static final String TITLE = "THREEDEE Engine Pre-Alpha 0.01";

	private Thread thread;
	private Screen screen;
	private BufferedImage img;
	private boolean running = false;
	public int updates = 0;
	public double delta = 0;
	private int[] pixels;
	private Render render;

	JFrame frame;

	public Display(JFrame f) {
		this.frame = f;
		screen = new Screen(WIDTH, HEIGHT);
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		screen.render();
	}

	private void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();

	}

	private void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	public void run() {		
		
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		delta = 0;
		int frames = 0;
		updates = 0;
		requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				updates++;
				delta--;

			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;

				frame.setTitle(TITLE + " | " + updates + " ups " + frames + " fps");
				updates = 0;
				frames = 0;
				if (timer > 0) {
					timer--;
				}
			}
		}
		stop();

	}

	private void tick() {

	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		for (int i = 0; i < WIDTH * HEIGHT; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(img,0,0,WIDTH,HEIGHT,null);
		g.dispose();
		bs.show();
		

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Display game = new Display(frame);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setTitle(TITLE);

		game.start();

	}

}
