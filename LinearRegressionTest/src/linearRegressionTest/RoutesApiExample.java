package linearRegressionTest;

import java.util.List;
import java.util.Observable;

import io.swagger.client.ApiClient;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.api.AthletesApi;
import io.swagger.client.api.RoutesApi;
import io.swagger.client.model.DetailedActivity;
import io.swagger.client.model.DetailedAthlete;
import io.swagger.client.model.SummaryActivity;

public class RoutesApiExample 
{
	public static void main(String... args) 
	{
	    ApiClient client = new ApiClient();
	    client.setAccessToken("75a26edaf876423bfb7c4a6e2957aaa6573f5a19");
	    //client.addDefaultHeader("Authorization: Bearer", "40bfd5dc809dcbbe4465a79bf3ea73ec6f0ae3e0");
	    //client.addDefaultHeader("token_type","Bearer");
	    //client.addDefaultHeader("access_token", "40bfd5dc809dcbbe4465a79bf3ea73ec6f0ae3e0");
	    ActivitiesApi api = new ActivitiesApi(client);
	    try {
	    	AthletesApi athApi = new AthletesApi(client);
	    	DetailedAthlete result = athApi.getLoggedInAthlete();
	    	System.out.println(result);
			//DetailedActivity da = api.getActivityById(new Long(2136435252),Boolean.TRUE);
	    	List<SummaryActivity> activities = api.getLoggedInAthleteActivities(1549947860, 1541917515, 1, 1);
	    	api.get
			System.out.println(activities);
			//da.getSegmentEfforts().stream();
		} catch (Exception e) {
			e.printStackTrace();
		}

	    
	  }

}
