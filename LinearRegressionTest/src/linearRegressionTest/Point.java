package linearRegressionTest;

public class Point 
{
	float altitude;
	int time;
	float distance;
	
	
	
	public Point(float altitude, int time, float distance) {
		this.altitude = altitude;
		this.time = time;
		this.distance = distance;
	}
	
	


	public float getAltitude() {
		return altitude;
	}




	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}




	public int getTime() {
		return time;
	}




	public void setTime(int time) {
		this.time = time;
	}




	public float getDistance() {
		return distance;
	}




	public void setDistance(float distance) {
		this.distance = distance;
	}




	@Override
	public String toString() {
		return "Point [altitude=" + altitude + ", time=" + time + ", distance=" + distance + "]";
	}
	
	
}
