package linearRegressionTest;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import io.swagger.client.ApiClient;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.api.AthletesApi;
import io.swagger.client.api.RoutesApi;
import io.swagger.client.auth.Authentication;
import io.swagger.client.model.DetailedActivity;
import io.swagger.client.model.DetailedAthlete;
import io.swagger.client.model.SummaryActivity;

public class RoutesApiExample 
{
	public static void main(String... args) 
	{
	    ApiClient client = new ApiClient();
	    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1",3128));
	    client.getHttpClient().setProxy(proxy);
	    //client.setAccessToken("75a26edaf876423bfb7c4a6e2957aaa6573f5a19");//04348c483b3416eacc65c05679f774040d65b947
	    client.setAccessToken("4764f9b5a7f0bb3e707f197b57656bb4dbf24c22");
	    ActivitiesApi api = new ActivitiesApi(client);
	    try {
	    	AthletesApi athApi = new AthletesApi(client);
	    	Map<String, Authentication> m = client.getAuthentications();
	    	Authentication auth = m.get("strava_oauth");
	    	//System.out.println(auth.);
	    	DetailedAthlete result = athApi.getLoggedInAthlete();
	    	System.out.println(result);
			//DetailedActivity da = api.getActivityById(new Long(2136435252),Boolean.TRUE);
	    	List<SummaryActivity> activities = api.getLoggedInAthleteActivities(1549947860, 1541917515, 10, 1);
	    	//api.get
			System.out.println(activities);
			//da.getSegmentEfforts().stream();
		} catch (Exception e) {
			e.printStackTrace();
		}

	    
	  }

}
