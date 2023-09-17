package vendingMachineSystem.view;

import java.awt.Component;

import javax.swing.*;

public class Window {

	private static Window window;
	
	JFrame frame;
	
	public static Window getInstance() {
		if (window == null)
			window = new Window();
		return window;
	}
	
	private Window() {
       frame = new JFrame("Vending Machine");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setSize(660,300);
	}
	
	public void updateWindow(Component component) {
		frame.getContentPane().removeAll();
		frame.add(component);
		frame.setVisible(true);
	}

}
