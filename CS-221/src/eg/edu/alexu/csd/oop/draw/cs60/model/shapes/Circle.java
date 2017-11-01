package eg.edu.alexu.csd.oop.draw.cs60.model.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.cs60.model.MainShape;

public class Circle extends MainShape{
	private float STROKE = 2;
	private double radius ;
	
	public Circle() {
		super();
	}
	public Circle(Point position , double radius) {
		super();
		setPosition(position);
		this.radius = radius ;
		getProperties().put("x", getPosition().getX());
		getProperties().put("y", getPosition().getY());
		getProperties().put("radius", new Double(radius));
		getProperties().put("color", Color.BLACK.getRGB()*1.0);
		getProperties().put("fill_color", Color.RED.getRGB()*1.0);
	}
	public Circle(Point position , int radius, Color color, Color fillColor) {
		super();
		setPosition(position);
		this.radius = radius ;
		getProperties().put("x", getPosition().getX());
		getProperties().put("y", getPosition().getY());
		getProperties().put("radius", new Double(radius));
		getProperties().put("color", color.getRGB()*1.0);
		getProperties().put("fill_color", fillColor.getRGB()*1.0);
	}
	
	@Override
	public void draw(Graphics canvas) {
		// TODO Auto-generated method stub
		Graphics2D g = (Graphics2D)canvas ;
		g.setColor(new Color(getProperties().get("color").intValue()));
		g.fillOval(getProperties().get("x").intValue()-getProperties().get("radius").intValue(), getProperties().get("y").intValue()-getProperties().get("radius").intValue(), 
				getProperties().get("radius").intValue()*2, getProperties().get("radius").intValue()*2);
		g.setStroke( new BasicStroke(STROKE));
		g.drawOval(getProperties().get("x").intValue()-getProperties().get("radius").intValue(), getProperties().get("y").intValue()-getProperties().get("radius").intValue(),
				getProperties().get("radius").intValue()*2, getProperties().get("radius").intValue()*2);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Shape clone = new Circle(new Point(getProperties().get("x").intValue(),getProperties().get("x").intValue()) ,
								 getProperties().get("radius").intValue(), new Color(getProperties().get("color").intValue()), 
								 new Color(getProperties().get("fill_color").intValue())); // needs some adjustment
		Map<String,Double> clone_prop = new HashMap<>();
		clone_prop.putAll(this.getProperties());
		clone.setProperties(clone_prop);
		return clone;
	}
}
