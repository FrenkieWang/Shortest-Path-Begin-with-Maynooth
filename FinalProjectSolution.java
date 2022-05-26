package cs211FinalProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

//Draw
import java.awt.*;
import javax.swing.*;

public class FinalProjectSolution{ 

	//this can be changed by all methods in this class
	private static double[][] ajacencyMatrix;
	private static boolean[] trip;
	private static ArrayList<Integer> canvasLatitude = new ArrayList<Integer>();
	private static ArrayList<Integer> canvasLongitude = new ArrayList<Integer>();

	public static void main(String[] args) {
		
        // creating object of JFrame(Window popup)
        JFrame window = new JFrame();
  
        // setting closing operation
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  
        // setting size of the pop window
        window.setBounds(10, 10, 1100, 900);
		
		// Read the file line by line
		ReadFiledata read = new ReadFiledata();		
		
		// Please change the address
		File file = new File("C://Users/pc/Desktop/CS211[A] ¡ª Algorithms & Data Structures 2 (2021-22Semester 2)/120schools.txt");
		String []line = read.txt2String(file).split("\n");
		
		// Store city number and their latitude and longitude
//		String []num = new String [line.length];
		String [][]coordinate = new String [line.length][2];
		int [][]CanvasCoordinate = new int [line.length][2];
		for (int i=0; i<line.length; i++) {
//			num[i] = line[i].split(",")[0].trim(); //city number
			coordinate[i][0] = line[i].split(",")[1].trim(); //latitude
			coordinate[i][1] = line[i].split(",")[2].trim(); //longitude
			CanvasCoordinate [i][0] = (int)((55.5-Double.parseDouble(coordinate[i][0]))*200);
			CanvasCoordinate [i][1] = (int)((Double.parseDouble(coordinate[i][1])+10.5)*200);
		}
		
		// "Greedy Algorithm" to find the nearest path from start to end.

		// City amount
		int n = line.length;
		// Calculate the adjacency matrix, earth distance between city i and city j
		ajacencyMatrix = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				ajacencyMatrix[i][j] = getDistance(coordinate[i][0],coordinate[i][1],coordinate[j][0],coordinate[j][1]);
//				System.out.print(ajacencyMatrix[i][j]+"   ");
			} 
//			System.out.println();
		}

		// The record of the city traveled
		int currentCity = 0;
		int nextCity;
		double distance = 0.0;
		int[] path = new int[n + 1];

		// Initialize the trip
		trip = new boolean[n];
		for (int j = 0; j < n; j++) {
			trip[j] = false;
		}
		
		// Get the shortest path
		int index = 0; //index of path
		while (true) {
			//find the nearest city
			nextCity = nearestCity(currentCity);

			// If all cities are passed by
			if (nextCity == -1) {
				distance += ajacencyMatrix[currentCity][0];
//				System.out.println(ajacencyMatrix[currentCity][0]);
				canvasLatitude.add(CanvasCoordinate [currentCity][0]);
				canvasLongitude.add(CanvasCoordinate [currentCity][1]);
				path[index] = currentCity;
				canvasLatitude.add(CanvasCoordinate [0][0]);
				canvasLongitude.add(CanvasCoordinate [0][1]);
				path[index + 1] = 0; //last index is city 0
				break;
			}

			// Travel
			distance += ajacencyMatrix[currentCity][nextCity];
//			System.out.println(ajacencyMatrix[currentCity][nextCity]);
			canvasLatitude.add(CanvasCoordinate [currentCity][0]);
			canvasLongitude.add(CanvasCoordinate [currentCity][1]);
			path[index] = currentCity;
			trip[currentCity] = true;
			currentCity = nextCity;
			index++;
		}

		System.out.print("The shortest path is: ");
		System.out.println();
		for (int i = 0; i <= n; i++) {
			System.out.print(path[i]);
			if((i+1)%25==0) {
				System.out.println();
			}
			if(i==n) {
				continue;
			}
			System.out.print(",");
		}
		System.out.println("");
		System.out.println("Total length: " + distance + " km");
		
        // setting canvas for draw
		
        window.getContentPane().add(new MyCanvas(canvasLatitude,canvasLongitude));
		
        // set visibility
        window.setVisible(true);
	}
	
	
	

	// Get the next nearest city
	public static int nearestCity(int current) {
		double distance = Integer.MAX_VALUE;
		int city = -1;
		for (int i = 0; i < trip.length; i++) {
			if (i != current && !trip[i])
				if (ajacencyMatrix[current][i] < distance) {
					distance = ajacencyMatrix[current][i];
					city = i;
				}
		}
		return city;
	}
	
	//Get the distance between two coordinate on the earth.
	public static double getDistance(String lt1, String ln1, String lt2, String ln2) {
		final int R = 6371; // Radius of the earth
		Double lat1 = Double.parseDouble(lt1);
		Double lon1 = Double.parseDouble(ln1);
		Double lat2 = Double.parseDouble(lt2);
		Double lon2 = Double.parseDouble(ln2);
		Double latDistance = toRad(lat2-lat1);
		Double lonDistance = toRad(lon2-lon1);
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
				Math.cos(toRad(lat1)) * Math.cos(toRad(lat2))*
				Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		Double distance = R * c;
		return distance;
	}
	
	//Converting angle into radian.
	public static Double toRad(Double value) {
		return value * Math.PI / 180;
	}
}

// This class used to convert .txt file to the String type.
class ReadFiledata {
    public String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//Create a BufferedReader Class to read file
            String s = null;
            while((s = br.readLine())!=null){//Use readLine method to read line by line
                result.append(System.lineSeparator()+s);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }        
        return result.toString().trim(); //remove the space
    }
}

class MyCanvas extends JComponent {
	
	public ArrayList<Integer> canvasLatitude;
	public ArrayList<Integer> canvasLongitude;

	public MyCanvas(ArrayList<Integer> canvasLatitude, ArrayList<Integer> canvasLongitude) {
		this.canvasLatitude=canvasLatitude;
		this.canvasLongitude=canvasLongitude;
	}
	
	// Please change the address
	Image img = Toolkit.getDefaultToolkit().getImage("C://Users/pc/Desktop/CS211[A] ¡ª Algorithms & Data Structures 2 (2021-22Semester 2)/Ireland Map.png");
    public void paint(Graphics g)
    {
    	int length = canvasLatitude.size();
       	g.drawImage(img, 0, 0, 1030, 830, null);
    	Graphics2D g2 = (Graphics2D) g;
        // draw and display the line 
    	g2.setPaint(Color.red);
    	g2.fillOval(canvasLongitude.get(0), canvasLatitude.get(0), 10, 10);
    	for (int i=1; i<length; i++) {
        	g2.setPaint(Color.black);
            g2.drawLine(canvasLongitude.get(i-1), canvasLatitude.get(i-1), canvasLongitude.get(i), canvasLatitude.get(i));
        	g2.fillOval(canvasLongitude.get(i), canvasLatitude.get(i), 5, 5);
    	}
    }
}





//JFrame
//Graphics2D
//    DrawLine
//    
//Point Class -50,+5
//
//fillOver
