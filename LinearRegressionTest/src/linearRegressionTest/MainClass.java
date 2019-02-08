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
import java.util.stream.Stream;

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

	public MainClass()
	{
		
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
				System.out.print(gpxFile[i].getName()+" ");
				GPX gpx = GPX.reader().read(gpxFile[i]);
				allSegments.addAll(getSegmentsFromGPX(gpx));
			}
			
			double[][]x = getX(allSegments);
			double[]y = getY(allSegments);
			double[] beta = getRegressionVariables(x, y);
			
			double altitude = 1740;
			double length = 199000;
			
			double prediction = beta[0] + altitude*beta[1]+length*beta[2];
			
			
			System.out.println("nemea prediction:"+getHumanTime((long)prediction));
			
			altitude=3280;
			length = 305445;
			
			prediction = beta[0] + altitude*beta[1]+length*beta[2];
			
			System.out.println("perseas prediction:"+getHumanTime((long)prediction));
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
	
	public double[] getRegressionVariables(double[][]x, double[] y)
	{
		OLSMultipleLinearRegression olsr = new OLSMultipleLinearRegression();
		
		
		olsr.newSampleData(y, x);
		
		double[] beta = olsr.estimateRegressionParameters(); 
		
		return beta;
		
	}
	
	public String getHumanTime(long time)
	{
		long hours = time /(1000*60*60);
		long minutes = time /(1000*60) - hours*60;
		
		return hours+"h "+minutes+"min";
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
	
	
	public GPX readGPXFile() throws IOException
	{
		return GPX.reader().read("input/Brevet_300.gpx");
	}
	
	
	public Length getPathLength(GPX gpx)
	{
		Length length = gpx.tracks().flatMap(Track::segments)
				.findFirst().map(TrackSegment::points).orElse(Stream.empty())
				.collect(Geoid.WGS84.toPathLength());
		
		System.out.println("MainClass.getPathLength: \t\t"+length);
		
		return length;
	}
	
	
	public ArrayList<MySegment> getSegmentsFromGPX(GPX gpx)
	{
		Stream<Track> track = gpx.tracks();
		Stream<TrackSegment> segment = track.flatMap(Track::segments);
		Optional<TrackSegment> optional = segment.findFirst();
		
		TrackSegment tr = optional.get();
		Stream<WayPoint> points = tr.points();
		
		
		ArrayList<MySegment> mySegments = points.collect(getCustomSegments());
		System.out.println("mySegments:"+mySegments.size()+" last:"+mySegments.get(mySegments.size()-1));
		
		return mySegments;
	}
	
	
	public void testStream(GPX gpx)
	{
		Stream<Track> track = gpx.tracks();
		Stream<TrackSegment> segment = track.flatMap(Track::segments);
		Optional<TrackSegment> optional = segment.findFirst();
		
		TrackSegment tr = optional.get();
		Stream<WayPoint> points = tr.points();
		//Length length = points.collect(Geoid.WGS84.toTourLength());
		
		double totalElevation = points.collect(getTotalElevation());
		System.out.println("totalElevation:"+totalElevation);
		
		points = tr.points();
		
		Stream<Optional<ZonedDateTime>> t = points.map(WayPoint::getTime);
		//System.out.println("MainClass.testStream:"+ t.findFirst().get());
		
		Stream<Instant> instantStream = t.map(Optional::get).map(ZonedDateTime::toInstant);
		long total = 0;
		
		/*
		 * calculation with collector
		 */
		total = instantStream.collect(getTotalTime());
		
		points = tr.points();
		ArrayList<MySegment> mySegments = points.collect(getCustomSegments());
		System.out.println("mySegments:"+mySegments.size()+" last:"+mySegments.get(mySegments.size()-1));
		
		double[][]x = getX(mySegments);
		double[]y = getY(mySegments);
		getRegressionVariables(x, y);
		
		/*
		 * calculation with iteration
		 */
		/*Iterator<Instant> it = instantStream.iterator();
		Instant previousInstant = null;
		while (it.hasNext()) 
		{
			Instant ins = (Instant) it.next();
			if(previousInstant==null)
			{
				previousInstant = ins;
				continue;
			}
			total += Duration.between(previousInstant, ins).toMillis();
			previousInstant = ins;
		}
		*/
		
		
		long hours = total/(1000*60*60);
		long minutes = total/(1000*60) - hours*60;
		
		System.out.println("total time:"+hours+"h "+minutes+"min");
		
		//Stream<WayPoint> points = segment.findFirst().map(TrackSegment::points);
		
        /*Stream<Optional<ZonedDateTime>> t = 
				points.map(wp->wp.toBuilder().time());
		t.findFirst().get();*/
		
		//System.out.println("MainClass.testStream \t\t"+length);
	}
	
	//total time:14h 51min
	
	public Collector<Instant, ? ,Long> getTotalTime()
	{
		return Collector.of(
				() -> new TimeCollector(), 
				TimeCollector::add,
				TimeCollector::combine, 
				TimeCollector::getTotalTime
				);
	}
	
	public Collector<WayPoint, ? ,Double> getTotalElevation()
	{
		return Collector.of(
				() -> new CustomCollector(), 
				CustomCollector::add,
				CustomCollector::combine, 
				CustomCollector::getTotalElevation
				);
	}
	
	
	public Collector<WayPoint, ? ,ArrayList<MySegment>> getCustomSegments()
	{
		return Collector.of(
				() -> new CustomCollector(), 
				CustomCollector::createSegment,
				CustomCollector::combine, 
				CustomCollector::getCustomSegments
				);
	}
	

}
