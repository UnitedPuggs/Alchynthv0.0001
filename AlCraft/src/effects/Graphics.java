package effects; 
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.ImageIOImageData;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import base.Game;

public class Graphics {
	public static int windowWidth = 1920, windowHeight = 1080;
	public static void Start() throws IOException {
		Display.setTitle("Alchynth");
		try {
			Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
			Display.setIcon(new ByteBuffer[] {
					//Setting the first and second boolean to true turns into the taskbar icon (but upside down?). Setting just the second boolean to true just does window icon.
					new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("resources/alchynth1window.png")), false, true, null),
					new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("resources/alchynth1task.png")), true, true, null)
			});
			Display.setVSyncEnabled(true);
			Display.create();
			
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		//left - right - bottom - top - zNear - zFar
		//-windowWidth because left. Divide by 12.8 on width and 9.6 on height because that's a 4:3 ratio
		glOrtho(-windowWidth / 12.8, windowWidth / 3, windowHeight / 3, -windowHeight / 9.6, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void DrawObjectTexture(Texture texture, float x, float y, float width, float height, float xo, float yo) {
		texture.bind();
		float texHeight = texture.getImageHeight();
		float texWidth = texture.getImageWidth();
		glTranslatef(x - xOffset(), y - yOffset(), 0);
		//This is such a useful method, you don't even know man
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glBegin(GL_QUADS);
		//You're trying to divide by 1/64 the total size of the image. yeet. I disregarded this fact instantly and made a mistake. 
		/*
		 * I'll try to explain the math real quick.
		 * There are only 2 parameters for the coordinates of the texture. X and Y.
		 * The first part (0+xo or 0+yo) is the offset. As in, where in the sheet your whatever is. 
		 * It's divided by either the width or height times 0.015625f(loat) or 1/64. 
		 * This perfectly aligns with the 64x64 textures that we are using in our game.
		 * But every texture MUST be 64x64 pixels and the total sheet width and height MUST be divisible by 64.
		 */
		glTexCoord2f((0+xo) / (texHeight * 0.015625f), (0+yo) / (texWidth * 0.015625f));
		glVertex2f(0, 0);
		glTexCoord2f((1+xo) / (texHeight * 0.015625f), (0+yo) / (texWidth * 0.015625f));
		glVertex2f(width, 0);
		glTexCoord2f((1+xo) / (texHeight * 0.015625f), (1+yo) / (texWidth * 0.015625f));
		glVertex2f(width, height);
		glTexCoord2f((0+xo) / (texHeight * 0.015625f), (1+yo) / (texWidth * 0.015625f));
		glVertex2f(0, height);
		glEnd();
		glLoadIdentity();
	}
	
	public static float xOffset()  {
		//Horizontal offset for wherever I want the world to be translated to
		//Divide the window width by times 2 whatever I have the zoom above
		float xOffset = Game.getPlayerX() - windowWidth / 8 + Game.getPlayerWidth() / 2;
		return xOffset;
	}
	
	public static float yOffset() {
		//Just the vertical offset for where the world is translated to
		//Same thing. Divide the height by the same amount as the height
		float yOffset = Game.getPlayerY() - windowHeight / 8 + Game.getPlayerWidth() / 2;
		return yOffset;
	}
	
	public static Texture loadTexture(String path, String fileType) {
		Texture tex = null;
		InputStream in = ResourceLoader.getResourceAsStream(path);
		try {
			tex = TextureLoader.getTexture(fileType, in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tex;
	}
	
	public static Texture shortcut(String name) {
		Texture tex = null;
		tex = loadTexture("resources/" + name + ".png", "PNG");
		return tex;
	}
	
	public int getWindowWidth() {
		return windowWidth;
	}
	
	public int getWindowHeight() { 
		return windowHeight;
	}
	
	public void setWindowWidth(int x) {
		windowWidth = x;
	}
	
	public void setWindowHeight(int y) {
		windowHeight = y;
	}
}