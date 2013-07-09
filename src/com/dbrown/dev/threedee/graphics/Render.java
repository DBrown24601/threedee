package com.dbrown.dev.threedee.graphics;

public class Render {
	public final int width;
	public final int height;
	public final int[] pixels;

	public Render(int w, int h) {
		this.width = w;
		this.height = h;
		pixels = new int[width * height];

	}

	public void draw(Render render, int xOffset, int yOffset) {
		for (int y = 0; y < render.height; y++) {
			int yPix = y + yOffset;
			for (int x = 0; x < render.width; x++) {
				int xPix = x + xOffset;
				pixels[xPix + yPix * width] = render.pixels[x + y * render.width];
				
			}
		}

	}

}
