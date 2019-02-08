package linearRegressionTest;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
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
			GPX gpx = readGPXFile();
			//testRoutes(getRoutes(gpx));
			//testTracks(getTracks(gpx));
			//getPathLength(gpx);
			testStream(gpx);
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
	
	
	public GPX readGPXFile() throws IOException
	{
		return GPX.reader().read("input/Brevet_300.gpx");
	}
	
	public List<Track> getTracks(GPX input)
	{
		return input.getTracks();
	}
	
	public List<Route> getRoutes(GPX input)
	{
		return input.getRoutes();
	}
	
	public void testTracks(List<Track> trackList)
	{
		for (Iterator<Track> it = trackList.iterator(); it.hasNext();) {
			Track track = (Track) it.next();
			System.out.println("MainClass.testTracks "+ track);
			testSegments(track.getSegments());
		}
	}
	
	public void testSegments(List<TrackSegment> segments)
	{
		for (TrackSegment segment : segments)
		{ 
			System.out.println("MainClass.testSegments :"+segment);
			testWayPoints(segment.getPoints());
		}
	}
	
	public void testWayPoints(List<WayPoint> points)
	{
		for (WayPoint wayPoint : points) 
		{
			System.out.println("MainClass.testWayPoints :"+wayPoint);
		}
		/*final Length distance = Geoid.WGS84.distance(points.get(0), points.get(points.size()-1));
		System.out.println(distance);*/
	}
	
	public void testRoutes(List<Route> routeList)
	{
		for (Route route : routeList) 
		{
			System.out.println("MainClass.testRoutes " + route.toString());
		}
	}
	
	public Length getPathLength(GPX gpx)
	{
		Length length = gpx.tracks().flatMap(Track::segments)
				.findFirst().map(TrackSegment::points).orElse(Stream.empty())
				.collect(Geoid.WGS84.toPathLength());
		
		System.out.println("MainClass.getPathLength: \t\t"+length);
		
		return length;
	}
	
	public void testStream(GPX gpx)
	{
		Stream<Track> track = gpx.tracks();
		Stream<TrackSegment> segment = track.flatMap(Track::segments);
		Optional<TrackSegment> optional = segment.findFirst();
		
		TrackSegment tr = optional.get();
		Stream<WayPoint> points = tr.points();
		//Length length = points.collect(Geoid.WGS84.toTourLength());
		
		Stream<Optional<ZonedDateTime>> t = points.map(WayPoint::getTime);
		//System.out.println("MainClass.testStream:"+ t.findFirst().get());
		
		Stream<Instant> instantStream = t.map(Optional::get).map(ZonedDateTime::toInstant);
		long total = 0;
		
		/*
		 * calculation with collector
		 */
		total = instantStream.collect(getTotalTime());
		
		
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
	

}
