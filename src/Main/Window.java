package Main;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window {
	
	JFrame jframe;
	JPanel jpanel;
	Image image;
	
//	InputState input_state;
	InputHandler input_handler;
	
	public Window(){
		this(960,640);
	}
	
	public Window(int x_size, int y_size){
//		this.input_state = input_state;
		input_handler = new InputHandler();
		
		jframe = new JFrame();
		jframe.setResizable(false);
		//i don't know why i need to hack the size a little bigger, but oh well
		jframe.setPreferredSize(new Dimension(x_size+6, y_size+28));
		
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jpanel = new JPanel();
		jpanel.addMouseListener(input_handler);
		jpanel.addMouseMotionListener(input_handler);
		jframe.addKeyListener(input_handler);
		jframe.add(jpanel);
		jframe.pack();
		jpanel.setPreferredSize(new Dimension(x_size, y_size));
		jframe.setLocationRelativeTo(null);
		jframe.setLayout(null);
		jframe.setVisible(true);	
		
		image = jpanel.createVolatileImage(x_size, y_size);
		
	}
	
	public Graphics getGraphics(){
		return image.getGraphics();
	}
	
	public void drawImage(){
		Graphics g = jpanel.getGraphics();
		g.drawImage(image,0,0,null);
		g.dispose();
	}
	
	public InputHandler getInputHandler(){
		return input_handler;
	}

}
