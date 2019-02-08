package linearRegressionTest;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.atomic.DoubleAdder;

import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.geom.Geoid;

public class CustomCollector 

{
	private Instant previousInstant;
	private long totalTime;
	private long distanceStep;
	private DoubleAdder totalElevation;
	private WayPoint previousPoint;
	private final DoubleAdder _length = new DoubleAdder();
	private ArrayList<MySegment> segments;
	private TimeCollector timeCollector;
	private final Geoid _geoid;
	
	public CustomCollector()
	{
		this(10000);
	}
	
	public CustomCollector(long distanceStep)
	{
		this.distanceStep = distanceStep;
		totalElevation = new DoubleAdder();
		segments = new ArrayList<MySegment>();
		timeCollector = new TimeCollector();
		_geoid = Geoid.WGS84;
	}
	
	public void createSegment(WayPoint point)
	{
		add(point);
		//System.out.println(distanceStep+" "+_length.doubleValue()+ " "+_length.doubleValue()%distranceStep);
		if(((long)_length.doubleValue())%distanceStep < 10)
		{
			segments.add(new MySegment((long)getTotalElevation(), timeCollector.getTotalTime(),(long) _length.doubleValue()));
		}
	}
	
	public void add(WayPoint point)
	{
		if(this.previousPoint==null)
		{
			previousPoint = point;
			return;
		}
		addDistance(point);
		addElevation(point);
		timeCollector.add(point.getTime().get().toInstant());
		previousPoint = point;
	}
	
	public void addDistance(WayPoint point) 
	{
		_length.add(_geoid.distance(point, this.previousPoint).doubleValue());
	}
	
	public void addElevation(WayPoint point)
	{
		if(previousPoint.getElevation().get().compareTo(point.getElevation().get())<0)
		{
			totalElevation.add(point.getElevation().get().doubleValue());
			totalElevation.add(- previousPoint.getElevation().get().doubleValue());
		}
	}
	
	public CustomCollector combine(final CustomCollector other) {
		throw new UnsupportedOperationException();
	}
	
	public long getTotalTime()
	{
		return this.totalTime;
	}
	
	public double getTotalElevation()
	{
		return this.totalElevation.doubleValue();
	}
	
	public ArrayList<MySegment> getCustomSegments()
	{
		//add last segment
		segments.add(new MySegment((long)getTotalElevation(), timeCollector.getTotalTime(),(long) _length.doubleValue()));
		return this.segments;
	}

}
