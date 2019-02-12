package linearRegressionTest;

import java.time.Duration;
import java.time.Instant;

public class TimeCollector 
{
	private Instant previousInstant;
	private long totalTime;
	private long relativeTime;
	
	public TimeCollector()
	{
		
	}
	
	public void add(Instant inst)
	{
		if(previousInstant==null)
		{
			previousInstant = inst;
			return;
		}
		totalTime += Duration.between(previousInstant, inst).toMillis();
		relativeTime += Duration.between(previousInstant, inst).toMillis();
		previousInstant = inst;
	}
	
	public TimeCollector combine(final TimeCollector other) {
		throw new UnsupportedOperationException();
	}
	
	public long getTotalTime()
	{
		return this.totalTime;
	}
	

	public long getRelativeTime() {
		return relativeTime;
	}


	public void reset()
	{
		this.relativeTime=0;
	}

}
