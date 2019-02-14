package linearRegressionTest;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.apache.commons.math3.stat.regression.GLSMultipleLinearRegression;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Length;
import io.jenetics.jpx.Route;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.geom.Geoid;

public class MainClass 
{
	public Util util;
	
	public MainClass()
	{
		util = new Util();
	}
	
	public static void main(String[] args) 
	{
		new MainClass().run();

	}
	
	public void run()
	{
		try
		{
			ArrayList<MySegment> allSegments = new ArrayList<MySegment>();
			File dir = new File("input");
			File[] gpxFile = dir.listFiles();
			for (int i = 0; i < gpxFile.length; i++) 
			{
				System.out.println(gpxFile[i].getName()+" ");
				GPX gpx = GPX.reader().read(gpxFile[i]);
				allSegments.addAll(util.getSegmentsFromGPX(gpx));
			}
			
			
			/*
			 * 1st run based on total values
			 */
			System.out.println("\n/*\n*1st run based on total values\n*/");
			{
				double[][]x = util.getX(allSegments.stream());
				double[]y = util.getY(allSegments.stream());
				double[] beta = util.getRegressionVariables(x, y);

				double altitude = 1740;
				double length = 199000;

				double prediction = beta[0] + altitude*beta[1]+length*beta[2];


				System.out.println("nemea prediction:"+util.getHumanTime((long)prediction));

				altitude=3280;
				length = 305445;

				prediction = beta[0] + altitude*beta[1]+length*beta[2];

				System.out.println("perseas prediction:"+util.getHumanTime((long)prediction));
			}
			
			/*
			 * 2nd run based on relative values
			 */
			{
				System.out.println("\n/*\n*2nd run based on relative values\n*/");
				double[][]x = getRelativeX(allSegments);
				double[]y = getRelativeY(allSegments);
				double[] beta = util.getRegressionVariables(x, y);

				double altitude = 1740;
				double length = 199000;

				double prediction = beta[0] + altitude*beta[1]+length*beta[2];


				System.out.println("nemea relative prediction:"+util.getHumanTime((long)prediction));

				altitude=3280;
				length = 305445;

				prediction = beta[0] + altitude*beta[1]+length*beta[2];

				System.out.println("perseas relative prediction:"+util.getHumanTime((long)prediction));
			}
			
			/*
			 * 3rd run based on total values fictional course
			 */
			System.out.println("\n/*\n*3rd run based on total values no intercept \n*/");
			{
				double[][]x = util.getX(allSegments.stream());
				double[]y = util.getY(allSegments.stream());
				double[] beta = util.getRegressionVariables(x, y);

				double altitude = 1800;
				double length = 100000;

				double prediction = beta[0] + altitude*beta[1]+length*beta[2];


				System.out.println("parnitha prediction:"+util.getHumanTime((long)prediction));

			}
			
			/*
			 * 4th run fiction course based on relative values
			 */
			{
				System.out.println("\n/*\n*4th run fiction course based on total values\n*/");
				double[][]x = util.getX(allSegments.stream());
				double[]y = util.getY(allSegments.stream());
				double[] beta = util.getRegressionVariables(x, y);

				double altitude = 470;
				double length = 47000;

				double prediction = beta[0] + altitude*beta[1]+length*beta[2];


				System.out.println("loutsa prediction:"+util.getHumanTime((long)prediction));

			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void regressionTest()
	{
		OLSMultipleLinearRegression olsr = new OLSMultipleLinearRegression();
		
		double[][] x = new double[][]{{4,0,1},{7,1,1},{6,1,0},{2,0,0},{3,0,1}};
		double[] y = new double[]{27,29,23,20,21};
		olsr.newSampleData(y, x);
		
		double[] beta = olsr.estimateRegressionParameters();  
		
		for (int i = 0; i < y.length; i++) 
		{
			double prediction = beta[0] + x[i][0]*beta[1]+x[i][1]*beta[2]+x[i][2]*beta[3];
			
			System.out.println(Arrays.asList(prediction+" - "+y[i]));
		}
		
	}
	
	
	/**
	 * TODO: convert to Stream implementation
	 * @param segments
	 * @return
	 */
	public double[][] getX(ArrayList<MySegment> segments)
	{
		double x[][] = new double[segments.size()][2];
		for (int i=0;i<segments.size();i++) 
		{
			x[i][0] = segments.get(i).getGainedAltitude();
			x[i][1] = segments.get(i).getPathLength();
		}
		return x;
	}
	
	
	
	
	
	public double[][] getRelativeX(ArrayList<MySegment> segments)
	{
		double x[][] = new double[segments.size()][2];
		for (int i=0;i<segments.size();i++) 
		{
			x[i][0] = segments.get(i).getRelativeAltitude();
			x[i][1] = segments.get(i).getRelativeDistance();
		}
		return x;
	}
	
	/**
	 * TODO: convert to Stream implementation
	 * @param segments
	 * @return
	 */
	public double[] getY(ArrayList<MySegment> segments)
	{
		double y[] = new double[segments.size()];
		for (int i=0;i<segments.size();i++) 
		{
			y[i] = segments.get(i).getSegmentTime();
		}
		return y;
	}
	
	
	/**
	 * TODO: convert to Stream implementation
	 * @param segments
	 * @return
	 */
	public double[] getRelativeY(ArrayList<MySegment> segments)
	{
		double y[] = new double[segments.size()];
		for (int i=0;i<segments.size();i++) 
		{
			y[i] = segments.get(i).getRelativeTime();
		}
		return y;
	}
	
	
	
	
	
	

}
