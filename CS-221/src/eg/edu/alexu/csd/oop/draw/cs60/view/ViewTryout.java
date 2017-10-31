package eg.edu.alexu.csd.oop.draw.cs60.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs60.model.DrawEngineImp;
import eg.edu.alexu.csd.oop.draw.cs60.model.shapes.Rectangle;

public class ViewTryout extends JPanel implements MouseMotionListener, MouseListener{
	//DrawingEngine engine = DrawEngineImp.getUniqueInstance();
	DrawingEngine engine = new DrawEngineImp();
	Point p = null;
	Point p2 = null;
	Random rand = new Random(255);
	
	boolean save = false;
	boolean load = false;
	
	public ViewTryout(){
		super();
		this.setBackground(Color.WHITE);
		addMouseListener(this);
		addMouseMotionListener(this);
    }
	
	@Override
	public void paint(Graphics g) {
		if(!load){
			engine.load("0.6798829240056644_testDraw.XmL");
			engine.refresh(g);
			load = true;
		}
		addMouseListener(this);
        addMouseMotionListener(this);
        System.out.println(p + "23");
		Shape rect;
		if(p != null && p2 != null) {
			if(p.x > p2.x || p.y > p2.y) { // TODO needs more handling
				Point temp = new Point(p);
				p = new Point(p2);
				p2 = temp;
			}
			rect = new Rectangle(p,Math.abs(p2.x-p.x),Math.abs(p2.y-p.y));
			rect.setColor(new Color(rand.nextInt()));
			engine.addShape(rect);
			engine.refresh(g);
		}
		//if(!save) {
			//engine.save("0.6798829240056644_testDraw.XmL");
		//}
	}
    
    public static void main(String[] args) {
 
        JFrame frame = new JFrame("Paint Try out");
        frame.setBackground(Color.WHITE);
        frame.getContentPane().add(new ViewTryout());
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