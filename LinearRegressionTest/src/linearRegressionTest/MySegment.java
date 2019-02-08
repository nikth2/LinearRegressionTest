package linearRegressionTest;

public class MySegment 
{
	private long gainedAltitude;
	private long segmentTime;
	private long pathLength;
	
	
	
	public MySegment(long gainedAltitude, long segmentTime, long pathLength) {
		super();
		this.gainedAltitude = gainedAltitude;
		this.segmentTime = segmentTime;
		this.pathLength = pathLength;
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
	
	public String getHumanTime()
	{
		long hours = this.segmentTime /(1000*60*60);
		long minutes = this.segmentTime /(1000*60) - hours*60;
		
		return hours+"h "+minutes+"min";
	}
	



	@Override
	public String toString() {
		return "MySegment [gainedAltitude=" + gainedAltitude + ", segmentTime=" + getHumanTime() + ", pathLength="
				+ pathLength + "]";
	}
	
	
}
