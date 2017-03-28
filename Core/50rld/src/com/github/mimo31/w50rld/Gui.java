package com.github.mimo31.w50rld;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.awt.Point;
import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

/**
 * Handles low level user interaction. Usually prepares event listeners and then calls methods in Main when the event is triggered.
 * @author mimo31
 *
 */
public class Gui {

	// identifier of the game window
	public static long window;
	
	// sizes of the window's content pane - automatically changed when the window resizes
	public static int width;
	public static int height;
	
	// whether the current window is decorated
	public static boolean decorated = true;
	
	/**
	 * Initializes the window and the OpenGL drawing. Should be called just once when initializing the game.
	 */
	public static void initializeGui()
	{
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit())
		{
			throw new RuntimeException("Failed to intialize GLFW.");
		}
		
		window = createWindow(true);
		
		ResourceHandler.loadTextures();
		TextDraw.loadFont();
		
	}
	
	/**
	 * Crates the window.
	 * @param decorated whether the window should be decorated
	 * @return the identifier of the window
	 */
	private static long createWindow(boolean decorated)
	{
		// set the hints
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GL_TRUE);
		glfwWindowHint(GLFW_DECORATED, decorated ? GL_TRUE : GL_FALSE);
		
		// create the window
		long win = glfwCreateWindow(600, 480, "50rld", NULL, NULL);
		if (win == 0)
		{
			throw new RuntimeException("Failed to create a GLFW window.");
		}
		
		// set the proper size
		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(win, (videoMode.width() - 600) / 2, (videoMode.height() - 480) / 2);
		
		// set the callbacks
		glfwSetKeyCallback(win, new KeyHandler());
		glfwSetMouseButtonCallback(win, new MouseHandler());
		
		glfwSetWindowSizeCallback(win, (long window, int w, int h) ->
	    	{
	        	glViewport(0, 0, w, h);
	        	width = w;
	        	height = h;
	    	}
		);
		
		// make context out of the window
		glfwMakeContextCurrent(win);
		
		// show the window
		glfwShowWindow(win);
		
		// setup GL
		GL.createCapabilities();

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glClearColor(1f, 1f, 1f, 1f);
		
		return win;
	}
	
	/**
	 * Terminates GLFW.
	 */
	public static void terminateGui()
	{
		glfwTerminate();
	}

	private static class KeyHandler extends GLFWKeyCallback
	{

		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if (action == GLFW_PRESS)
			{
				// toggle between fullscreen and normal mode - doesn't work very well currently
				if (key == GLFW_KEY_F11)
				{
					// destroy the old window
					glfwDestroyWindow(window);
					
					// create the new window with switched decorations
					createWindow(!decorated);
					
					decorated = !decorated;
				}
				Main.keyPressed(key);
			}
			else if (action == GLFW_RELEASE)
			{
				Main.keyReleased(key);
			}
		}
		
	}
	
	private static class MouseHandler extends GLFWMouseButtonCallback
	{

		@Override
		public void invoke(long window, int button, int action, int mods) {
			if (action == GLFW_PRESS)
			{
				Main.mouseClicked(getMousePosition());
			}
		}
		
	}
	
	/**
	 * @return the position of the mouse cursor in the window coordinates
	 */
	public static Point getMousePosition()
	{
		DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(window, xBuffer, yBuffer);
		return new Point((int)xBuffer.get(0), (int)yBuffer.get(0));
	}
}
