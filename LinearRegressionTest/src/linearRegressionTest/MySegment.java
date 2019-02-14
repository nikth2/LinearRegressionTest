package linearRegressionTest;

public class MySegment 
{
	private long gainedAltitude;
	private long segmentTime;
	private long pathLength;
	private long relativeTime;
	private long relativeAltitude;
	private long relativeDistance;
	
	
	public MySegment(long gainedAltitude, long segmentTime, long pathLength) {
		super();
		this.gainedAltitude = gainedAltitude;
		this.segmentTime = segmentTime;
		this.pathLength = pathLength;
	}
	

	public MySegment(long gainedAltitude, long segmentTime, long pathLength, long relativeTime, long relativeAltitude,
			long relativeDistance) {
		super();
		this.gainedAltitude = gainedAltitude;
		this.segmentTime = segmentTime;
		this.pathLength = pathLength;
		this.relativeTime = relativeTime;
		this.relativeAltitude = relativeAltitude;
		this.relativeDistance = relativeDistance;
	}


	public long getGainedAltitude() {
		return gainedAltitude;
	}



	public void setGainedAltitude(long gainedAltitude) {
		this.gainedAltitude = gainedAltitude;
	}



	public long getSegmentTime() {
		return segmentTime;
	}



	public void setSegmentTime(long segmentTime) {
		this.segmentTime = segmentTime;
	}



	public long getPathLength() {
		return pathLength;
	}


	public void setPathLength(long pathLength) {
		this.pathLength = pathLength;
	}
	
	public String getHumanTime(long time)
	{
		long hours = time /(1000*60*60);
		long minutes = time /(1000*60) - hours*60;
		
		return hours+"h "+minutes+"min";
	}
	

	public long getRelativeTime() {
		return relativeTime;
	}


	public void setRelativeTime(long relativeTime) {
		this.relativeTime = relativeTime;
	}


	public long getRelativeAltitude() {
		return relativeAltitude;
	}


	public void setRelativeAltitude(long relativeAltitude) {
		this.relativeAltitude = relativeAltitude;
	}


	public long getRelativeDistance() {
		return relativeDistance;
	}


	public void setRelativeDistance(long relativeDistance) {
		this.relativeDistance = relativeDistance;
	}


	@Override
	public String toString() {
		return "MySegment [gainedAltitude=" + gainedAltitude + ", segmentTime Human=" + getHumanTime(segmentTime) + ", pathLength="
				+ pathLength + ", segmentTime=" + segmentTime + ", relativeAltitude=" + relativeAltitude
				+ ", relativeDistance=" + relativeDistance + "]";
	}
	

	
	
	
}
