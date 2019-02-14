package linearRegressionTest;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Length;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.geom.Geoid;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.StreamsApi;
import io.swagger.client.model.AltitudeStream;
import io.swagger.client.model.StreamSet;

public class Util 
{
	
	public Util()
	{
		
	}
	
	public Collector<WayPoint, CustomCollector ,ArrayList<MySegment>> getCustomSegments()
	{
		return Collector.of(
				() -> new CustomCollector(), 
				CustomCollector::createSegment,
				CustomCollector::combine, 
				CustomCollector::getCustomSegments
				);
	}
	
	public Collector<Point, StravaCollector ,ArrayList<MySegment>> getCustomSegmentsFromPoints()
	{
		return Collector.of(
				() -> new StravaCollector(), 
				StravaCollector::createSegment,
				StravaCollector::combine, 
				StravaCollector::getCustomSegments
				);
	}
	
	public Collector<MySegment, ? ,double[][]> getIndependentVariables()
	{
		return Collector.of(
				() -> new CustomCollector(), 
				CustomCollector::addIndpendentVariable,
				CustomCollector::combine, 
				CustomCollector::getIndependentVariables
				);
	}
	
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
	
	private ArrayList<MySegment> getSegmentsFromPoints(List<Point> points)
	{
		
		ArrayList<MySegment> mySegments = points.stream().collect(getCustomSegmentsFromPoints());
		System.out.println("mySegments:"+mySegments.size()+" last:"+mySegments.get(mySegments.size()-1));
		return mySegments;
	}
	
	public ArrayList<MySegment> getSegmentsForActivity(ApiClient client ,long activityId)throws ApiException
	{
		StreamsApi streamsApi = new StreamsApi(client);
		  
		List<String> keys = Arrays.asList(new String[] {"time", "distance", "latlng", "altitude",
				"velocity_smooth", "moving", "grade_smooth", "temp"}); 
		StreamSet stream = streamsApi.getActivityStreams(activityId, keys, Boolean.TRUE); 
		AltitudeStream  altitudeStream = stream.getAltitude();

		List<Float> altitudeData = altitudeStream.getData(); 
		List<Integer> timeData = stream.getTime().getData(); 
		List<Float> distanceData = stream.getDistance().getData();

		List<Point> points = new ArrayList<Point>();
		for(int i=0;i<distanceData.size();i++)
		{
			points.add(new Point(altitudeData.get(i), timeData.get(i), distanceData.get(i)));
		}

		ArrayList<MySegment> segments = getSegmentsFromPoints(points);

		//System.out.printf("%d %d %d \n", altitudeData.size(),timeData.size(),distanceData.size()); 
		
		return segments;
	}
	
	public String getHumanTime(long time)
	{
		long hours = time /(1000*60*60);
		long minutes = time /(1000*60) - hours*60;
		
		return hours+"h "+minutes+"min";
	}
	
	public double[][] getX(Stream<MySegment> sgStream)
	{
		return sgStream.collect(getIndependentVariables());
	}
	
	public double[] getY(Stream<MySegment> sgStream)
	{
		return sgStream.mapToDouble(MySegment::getSegmentTime).toArray();
	}
	
	
	public double[] getRegressionVariables(double[][]x, double[] y)
	{
		OLSMultipleLinearRegression olsr = new OLSMultipleLinearRegression();
		
		olsr.newSampleData(y, x);
		
		double[] beta = olsr.estimateRegressionParameters(); 
		
		return beta;
		
	}
	
}
