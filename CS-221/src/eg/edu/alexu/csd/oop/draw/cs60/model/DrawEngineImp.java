package eg.edu.alexu.csd.oop.draw.cs60.model;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;

import javax.management.RuntimeErrorException;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Observer;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.Subject;
import eg.edu.alexu.csd.oop.draw.cs60.model.shapes.Circle;
import eg.edu.alexu.csd.oop.draw.cs60.model.shapes.Ellipse;
import eg.edu.alexu.csd.oop.draw.cs60.model.shapes.Line;
import eg.edu.alexu.csd.oop.draw.cs60.model.shapes.Rectangle;
import eg.edu.alexu.csd.oop.draw.cs60.model.shapes.Square;
import eg.edu.alexu.csd.oop.draw.cs60.model.shapes.Triangle;

public class DrawEngineImp implements DrawingEngine , Subject {
	private List<Observer> observers ;
	private List<Class<? extends Shape>> supportedShapes ;
	private Stack<ArrayList<Shape>> shapes ;
	private Stack<ArrayList<Shape>> redoShapes ;
	private static DrawEngineImp uniqueInstance = new DrawEngineImp() ;
	private JoeSONParser JSONParser = new JoeSONParser();
	private ShapesFactory shapesFactory = new ShapesFactory();

	private DrawEngineImp() {
		shapes = new Stack<ArrayList<Shape>>();
		shapes.push(new ArrayList<Shape>());
		redoShapes = new Stack<ArrayList<Shape>>();
		observers = new ArrayList<Observer>();
		initSupportedShapes();
	}
	
	private void clear() {
		shapes = new Stack<ArrayList<Shape>>();
		shapes.push(new ArrayList<Shape>());
		redoShapes = new Stack<ArrayList<Shape>>();
		initSupportedShapes();
	}
	
	private void initSupportedShapes() {
		supportedShapes = new ArrayList<>();
		supportedShapes.add(Line.class);
		supportedShapes.add(Rectangle.class);
		supportedShapes.add(Square.class);
		supportedShapes.add(Circle.class);
		supportedShapes.add(Ellipse.class);
		supportedShapes.add(Triangle.class);
	}

	public static DrawEngineImp getUniqueInstance() {
		return uniqueInstance;
	}
	
	@Override
	public void refresh(Graphics canvas) {
		// TODO Auto-generated method stub
		for(Shape x : shapes.peek()) {
			x.draw(canvas);
			/*if(x.isSelected()) {
				x.drawBonds(canvas);
			}*/
			
		}
		drawFullBonds(canvas);
	}
	
	private void drawFullBonds(Graphics canvas) {
		Point p1 = null;
		Point p2 = null;
		for(Shape x : getShapes()) {
			//if(x.isSelected()) {
			if(x.getProperties().get("selected") != null && x.getProperties().get("selected").intValue() == 1) {
				Point [] bonds = new Point[] {new Point(x.getProperties().get("bond_1_x").intValue(),x.getProperties().get("bond_1_y").intValue()) ,new Point(x.getProperties().get("bond_2_x").intValue(),x.getProperties().get("bond_2_y").intValue())};
				if(p1 == null ) {
				p1 = bonds[0];
				p2= bonds [1];
				}
				else {
					p1.x = Math.min(p1.x, bonds[0].x);
					p1.y = Math.min(p1.y, bonds[0].y);
					p2.x = Math.max(p2.x, bonds[1].x);
					p2.y = Math.max(p2.y, bonds[1].y);
				}
			}
		}
		int margin = 10 ;
		if(p1 != null) {
			Graphics2D g = (Graphics2D)canvas ;
			g.setStroke( new BasicStroke(2));
			g.drawRect(p1.x - margin, p1.y - margin ,p2.x - p1.x + 2 *margin, p2.y - p1.y + 2 * margin);
		}
		}

	public void setSelected() {
		for(Shape x : getShapes()) {
			//x.setSelected(false);
			x.getProperties().put("selected", 0.0);
		}
	}
	
	public void setSelected(int [] indices) {
		Shape [] shapes = getShapes();
		for(Integer x : indices) {
			//shapes[x].setSelected(true);
			shapes[x].getProperties().put("selected", 1.0);
		}
		notifyObserversSelection();
	}
	
	@Override
	public void addShape(Shape shape) {
		// TODO Auto-generated method stub
		redoShapes = new Stack<>();
		if(shapes.size() <= 20)
			shapes.push(new ArrayList<Shape>(shapes.peek()));
		else {
			shapes.remove(0);
			shapes.push(new ArrayList<Shape>(shapes.peek()));
		}
		shapes.peek().add(shape);
		notifyObservers();
	}

	@Override
	public void removeShape(Shape shape) {
		// TODO Auto-generated method stub
		int index = shapes.peek().indexOf(shape);
		if(index >= 0) {
			redoShapes = new Stack<>();
			if(shapes.size() <= 20) {
				shapes.push(new ArrayList<Shape>(shapes.peek()));
			}
			else {
				shapes.remove(0);
				shapes.push(new ArrayList<Shape>(shapes.peek()));
			}	
			shapes.peek().remove(index);
		}
		notifyObservers();
	}

	public void removeShapes(int [] indices) {
		// TODO Auto-generated method stub
		Arrays.sort(indices);
		if(indices.length > 0) {
			redoShapes = new Stack<>();
			if(shapes.size() <= 20) {
				shapes.push(new ArrayList<Shape>(shapes.peek()));
			}
			else {
				shapes.remove(0);
				shapes.push(new ArrayList<Shape>(shapes.peek()));
			}
			for(int i = indices.length-1 ; i >=0 ; i --) {
			shapes.peek().remove(getShapes()[indices[i]]);}
		}
		notifyObservers();
	}
	
	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		// TODO Auto-generated method stub
		int index = shapes.peek().indexOf(oldShape);
		if(index >= 0){
			redoShapes = new Stack<>();
			if(shapes.size() <= 20)
				shapes.push(new ArrayList<Shape>(shapes.peek()));
			else {
				shapes.remove(0);
				shapes.push(new ArrayList<Shape>(shapes.peek()));
			}	
			shapes.peek().set(index, newShape);
		}
		notifyObservers();
	}
	
	public void addShapeDrag(Shape shape) {
		shapes.peek().add(shape);
	}
	
	public void removeShapeDrag(Shape shape) {
		int index = shapes.peek().indexOf(shape);
		shapes.peek().remove(index);
	}
	
	public void dragDrawShape(Shape oldShape, Shape newShape) {
		// TODO Auto-generated method stub
		int index = shapes.peek().indexOf(oldShape);
		if(index >= 0){
			shapes.peek().set(index, newShape);
		}
	}
	
	@Override
	public Shape[] getShapes() {
		// TODO Auto-generated method stub
		return shapes.peek().toArray(new Shape[0]);
	}
	
	@Override
	public List<Class<? extends Shape>> getSupportedShapes() {
		//Reflections reflections = new Reflections();
		//List<Class<? extends Shape>> list = new LinkedList<>(reflections.getSubTypesOf(MainShape.class));
		//return list ;
		return supportedShapes;
	}

	@Override
	public void undo() {
		if(shapes.size() > 1)
			redoShapes.push(shapes.pop());
		notifyObservers();
	}

	@Override
	public void redo() {
		if(!redoShapes.empty())
			shapes.push(redoShapes.pop());
		notifyObservers();
	}

	@Override
	public void save(String path) {
		//throw new RuntimeException(path);
		if(path.substring(path.length()-3).equalsIgnoreCase("xml")){
			saveXML(path);
		}
		else if (path.substring(path.length()-4).equalsIgnoreCase("json")){
			saveJSON(path);
		}
		else {
			throw new RuntimeException(path);
		}
	}

	@Override
	public void load(String path) {
		//throw new RuntimeException(path);
		if(path.substring(path.length()-3).equalsIgnoreCase("xml")){
			loadXML(path);
		}
		else if (path.substring(path.length()-4).equalsIgnoreCase("json")){
			loadJSON(path);
		}
		else {
			throw new RuntimeException(path);
		}
	}
	
	private void saveXML(String path){
		/*if(shapes.peek().isEmpty())
			return;*/
        String objToString = objectToString(shapes);
        File outputXML = new File(path);
        try {
			FileWriter pw = new FileWriter(outputXML);
			pw.write(objToString);
			pw.close();
		}
        catch (Exception e) {
        	throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadXML(String path){
		File inputXML = new File(path);
		StringBuilder shapesXMLContent = new StringBuilder();
		Scanner in;
		try {
			in = new Scanner(inputXML);
			while(in.hasNextLine()) {
				shapesXMLContent.append(in.nextLine());
			}
			//in.close();
	        Stack<ArrayList<Shape>> parsedObj = (Stack<ArrayList<Shape>>) stringToObject(shapesXMLContent.toString());
	        shapes.push(parsedObj.peek());
			notifyObservers();
	        }
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void saveJSON(String path){
		//throw new RuntimeException(path);
		ArrayList<Map<String, String>> arrayListofShapeMap = new ArrayList<>();
		Map<String, Integer> freqOfShapes = new HashMap<>();
		for(Shape shape : shapes.peek()){
			// calculation of frequency (for indexing purpose)
			String shapeName = shape.getClass().getSimpleName();
			try{
				freqOfShapes.put(shapeName,freqOfShapes.get(shapeName)+1);
			}
			catch (Exception e){
				freqOfShapes.put(shapeName,1);
			}

			Map<String, String> newMap = new HashMap<String, String>();
			for(Map.Entry
					entry
					: shape
					.getProperties()
					.entrySet()){
				newMap.put(entry.getKey().toString(), entry.getValue().toString());
			}
			newMap.put("id", shapeName + freqOfShapes.get(shapeName));
			arrayListofShapeMap.add(newMap);
		}

		String parsedObject = JSONParser.parseArrayOfMapsIntoJSON(arrayListofShapeMap);
		System.out.println(parsedObject + "\n---------------------------\n");

		File outputXML = new File(path);
		try {
			FileWriter pw = new FileWriter(outputXML);
			pw.write(parsedObject);
			pw.close();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}

		JSONParser.parseJSONIntoArrayOfMaps(parsedObject);
	}
	
	private void loadJSON(String path){
		System.out.println("--line 327");
		File inputJSON = new File(path);
		StringBuilder shapesJSONContent = new StringBuilder();
		//try {
		Scanner in = null;
		try {
			in = new Scanner(inputJSON);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(in.hasNextLine()) {
				shapesJSONContent.append(in.nextLine() + "\n");
			}
			System.out.println(shapesJSONContent);
			ArrayList<Map<String,String>> parsedObj = JSONParser.parseJSONIntoArrayOfMaps(shapesJSONContent.toString());
			//shapes.push(parsedObj.peek());
			ArrayList<Shape> loadedShapes = new ArrayList<>();

			for(Map<String,String> map : parsedObj){
				Map<String,Double> tempMap = new HashMap<>();
				for(Map.Entry entry : map.entrySet()){
					if(entry.getKey().toString().equals("id"))
						continue;
					tempMap.put(entry.getKey().toString(), Double.parseDouble(entry.getValue().toString()));
					System.out.println(entry.getKey().toString() + " " + Double.parseDouble(entry.getValue().toString()));
				}
				String tempShapeName = map.get("id");
				String shapeName = "";
				System.out.println("Here: " + shapeName);
				for(int i=0; i<tempShapeName.length(); i++){
					if(tempShapeName.charAt(i) >= '0' && tempShapeName.charAt(i) <= '9')
						break;
					shapeName += tempShapeName.charAt(i);
				}

				System.out.println("Here: " + shapeName);
				Shape loadedShape = shapesFactory.CreateShape(shapeName);
				loadedShape.setProperties(tempMap);
				loadedShapes.add(loadedShape);
				System.out.println("--line 352");
			}
			shapes = new Stack<>();
			shapes.push(loadedShapes);
			notifyObservers();
			System.out.println("--line 357");
		//}
		/*catch(Exception e) {
			throw new RuntimeException(e);
		}*/
	}
	
	private String objectToString(Object hashMap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLEncoder xmlEncoder = new XMLEncoder(bos);
        xmlEncoder.writeObject(hashMap);
        xmlEncoder.close();
        return bos.toString();
    }

    private Object stringToObject(String string) {
        @SuppressWarnings("resource")
		XMLDecoder xmlDecoder = new XMLDecoder(new ByteArrayInputStream(string.getBytes()));
        return xmlDecoder.readObject();
    }


	@Override
	public void notifyObservers() {
		for(Observer x : observers) {
			x.update();
		}
	}

	@Override
	public void addObserver(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		int index = observers.indexOf(observer);
		if(index >= 0) {
			observers.remove(observer);
		}
	}

	@Override
	public void notifyObserversSelection() {
		for(Observer x : observers) {
			x.updateSelected();
		}
	}

}
