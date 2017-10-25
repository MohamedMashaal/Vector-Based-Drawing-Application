package eg.edu.alexu.csd.oop.draw.cs60.view;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

import javax.swing.*;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs60.model.DrawEngineImp;
import eg.edu.alexu.csd.oop.draw.cs60.model.shapes.Line;
import eg.edu.alexu.csd.oop.draw.cs60.model.shapes.Rectangle;

public class MainView2 extends JPanel implements MouseMotionListener, MouseListener{
	DrawingEngine engine = new DrawEngineImp();
	
	Point p = null;
	Point p2 = null;
	Random rand = new Random(255);
	
	public MainView2(){
		super();
		this.setBackground(Color.WHITE);
		addMouseListener(this);
		addMouseMotionListener(this);
    }
	
	@Override
	public void paint(Graphics g) {
		addMouseListener(this);
        addMouseMotionListener(this);
        System.out.println(p + "23");
		Shape rect = new Rectangle();
		if(p != null && p2 != null) {
			rect.setPosition(p);
			//((Rectangle) rect).setPosition2(p2);
			rect.setColor(new Color(rand.nextInt()));
			engine.addShape(rect);
			engine.refresh(g);
		}
	}
    
    public static void main(String[] args) {
 
        JFrame frame = new JFrame("Paint Try out");
        frame.setBackground(Color.WHITE);
        frame.getContentPane().add(new MainView2());
		frame.setSize(1000, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		p = getMousePosition();
		System.out.println(p + " 54");
		//repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		p2 = getMousePosition();
		System.out.println(p2 + " 81");
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}