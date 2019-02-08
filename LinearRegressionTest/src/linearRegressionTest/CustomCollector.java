package linearRegressionTest;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.DoubleAdder;

import io.jenetics.jpx.Length;
import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.geom.Geoid;

public class CustomCollector 

{
	private Instant previousInstant;
	private long totalTime;
	private long distanceStep;
	private DoubleAdder totalElevation;
	private WayPoint previousPoint;
	
	private final Geoid _geoid;
	
	public CustomCollector()
	{
		this(10000);
		totalElevation = new DoubleAdder();
		
	}
	
	public CustomCollector(long distanceStep)
	{
		this.distanceStep = distanceStep;
		_geoid = Geoid.WGS84;
	}
	
	public void add(Instant inst)
	{
		if(previousInstant==null)
		{
			previousInstant = inst;
			return;
		}
		totalTime += Duration.between(previousInstant, inst).toMillis();
		previousInstant = inst;
	}
	
	public void addElevation(WayPoint point)
	{
		if(this.previousPoint==null)
		{
			previousPoint = point;
			return;
		}
		if(previousPoint.getElevation().get().compareTo(point.getElevation().get())<0)
		{
			totalElevation.add(point.getElevation().get().doubleValue());
			totalElevation.add(- previousPoint.getElevation().get().doubleValue());
		}
		
		previousPoint = point;
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

}
