package linearRegressionTest;

import java.util.ArrayList;

public class StravaCollector 
{
	private Point previousPoint;
	private float distance;
	private float distanceStep = 10000;
	private long time;
	private float elevation;
	private ArrayList<MySegment> segments;
	
	public StravaCollector()
	{
		segments = new ArrayList<MySegment>();
	}
	
	
	public void createSegment(Point point)
	{
		add(point);
		//System.out.println(distanceStep+" "+_length.doubleValue()+ " "+_length.doubleValue()%distranceStep);
		if(((long)distance)%distanceStep < 10 && ((long)distance)/distanceStep>0)
		{
			segments.add(new MySegment((long)elevation, time,(long) distance));
		}
	}
	
	public void add(Point point)
	{
		if(this.previousPoint==null)
		{
			previousPoint = point;
			return;
		}
		distance = point.getDistance();
		time = point.getTime()*1000;
		
		if(previousPoint.getAltitude()<point.getAltitude())
			elevation+= point.getAltitude()-previousPoint.getAltitude();
		
		previousPoint = point;
	}
	
	public StravaCollector combine(final StravaCollector other) {
		throw new UnsupportedOperationException();
	}
	
	public ArrayList<MySegment> getCustomSegments()
	{
		//add last segment
		segments.add(new MySegment((long)elevation, time,(long) distance));
		return this.segments;
	}
	
}
