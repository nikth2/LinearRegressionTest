package linearRegressionTest;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.api.AthletesApi;
import io.swagger.client.api.StreamsApi;
import io.swagger.client.auth.Authentication;
import io.swagger.client.model.AltitudeStream;
import io.swagger.client.model.DetailedActivity;
import io.swagger.client.model.DetailedAthlete;
import io.swagger.client.model.StreamSet;
import io.swagger.client.model.SummaryActivity;

public class RoutesApiExample 
{
	public static void main(String... args) 
	{
		new RoutesApiExample().run();
	}
	
	public void run ()
	{
		ApiClient client = new ApiClient();
	    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1",3128));
	    client.getHttpClient().setProxy(proxy);
	    //client.setAccessToken("75a26edaf876423bfb7c4a6e2957aaa6573f5a19");//04348c483b3416eacc65c05679f774040d65b947
	    client.setAccessToken("4764f9b5a7f0bb3e707f197b57656bb4dbf24c22");
	    
		//test();
		testStreams(client);
	}
	
	public void test(ApiClient client)
	{
		ActivitiesApi api = new ActivitiesApi(client);
	    //RoutesApi routesApi = new RoutesApi(client);

	    try {
	    	AthletesApi athApi = new AthletesApi(client);
	    	Map<String, Authentication> m = client.getAuthentications();
	    	Authentication auth = m.get("strava_oauth");
	    	//System.out.println(auth.);
	    	DetailedAthlete result = athApi.getLoggedInAthlete();
	    	//System.out.println(result);
			//DetailedActivity da = api.getActivityById(new Long(2136435252),Boolean.TRUE);
	    	List<SummaryActivity> activities = api.getLoggedInAthleteActivities(1549947860, 1541917515, 1, 10);
			
	    	
	    	activities.forEach((activity)->{
				System.out.println(activity.getName()+" "+activity.getId());
				/*
				 * try { DetailedActivity da = api.getActivityById(activity.getId(),
				 * Boolean.TRUE); //List<Split> splits = da.getSplitsMetric();
				 * Optional.ofNullable(da.getSplitsMetric()).ifPresent(splits->
				 * splits.forEach(split->Optional.ofNullable(split).ifPresent(value->
				 * System.out.println(value.toString()))) );
				 * 
				 * 
				 * Optional.ofNullable(da.getSegmentEfforts()).ifPresent(segments->
				 * segments.forEach(segment->System.out.println(segment.toString())) );
				 * 
				 * 
				 * 
				 * 
				 * } catch (ApiException e) { e.printStackTrace(); }
				 */
				
			});
			
			/*
			 * routesApi.getRoutesByAthleteId(result.getId(), 1, 10).forEach(route-> {
			 * System.out.println(route.toString()); } );
			 */
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testStreams(ApiClient client)
	{
		/*
		 * List<String> names = Arrays.asList(new String[] {"nikos","kostas","akis"});
		 * List<String> surnames = Arrays.asList(new String[]
		 * {"theodoropoulos","sourbatis","sinanidis"}); List<Integer> ages =
		 * Arrays.asList(new Integer[] {39,44,34});
		 */
		
		/*
		 * Stream<String> combined =
		 * Stream.of(names,surnames).flatMap(Collection::stream); Collection<String> col
		 * = combined.collect(Collectors.toList()); System.out.println(col);
		 * Stream<List<String>> list = Stream.of(names,surnames);
		 */
		
		/*names.stream().flatMap(name-> surnames.stream().map(surname -> name + surname))
		.collect(Collectors.toList()).stream().forEach(System.out::println);*/
		
		ActivitiesApi api = new ActivitiesApi(client);
		Util util = new Util();
		long[] activityId = new long[] {2136435252,2107079241,2090662526};
		
		ArrayList<MySegment> segmentList = new ArrayList<MySegment>();
		for (int i = 0; i < activityId.length; i++) 
		{
			try 
			{
				DetailedActivity activity = api.getActivityById(activityId[i], Boolean.TRUE);
				System.out.println(activity.getName());
				segmentList.addAll(util.getSegmentsForActivity(client, activityId[i]));
			} 
			catch (ApiException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * 1st run based on total values
		 */
		System.out.println("\n/*\n*1st run based on total values\n*/");
		{
			double[][]x = util.getX(segmentList.stream());
			double[]y = util.getY(segmentList.stream());
			double[] beta = util.getRegressionVariables(x, y);

			double altitude = 1740;
			double length = 199000;

			double prediction = beta[0] + altitude*beta[1]+length*beta[2];


			System.out.println("nemea prediction:"+util.getHumanTime((long)prediction));

			altitude=3280;
			length = 305445;

			prediction = beta[0] + altitude*beta[1]+length*beta[2];

			System.out.println("perseas prediction:"+util.getHumanTime((long)prediction));
		}
		
		/*
		 * 3rd run based on total values fictional course
		 */
		System.out.println("\n/*\n*3rd run based on total values no intercept \n*/");
		{
			double[][]x = util.getX(segmentList.stream());
			double[]y = util.getY(segmentList.stream());
			double[] beta = util.getRegressionVariables(x, y);

			double altitude = 1800;
			double length = 100000;

			double prediction = beta[0] + altitude*beta[1]+length*beta[2];


			System.out.println("parnitha prediction:"+util.getHumanTime((long)prediction));

		}
		
		/*
		 * 4th run fiction course based on relative values
		 */
		{
			System.out.println("\n/*\n*4th run fiction course based on total values\n*/");
			double[][]x = util.getX(segmentList.stream());
			double[]y = util.getY(segmentList.stream());
			double[] beta = util.getRegressionVariables(x, y);

			double altitude = 470;
			double length = 47000;

			double prediction = beta[0] + altitude*beta[1]+length*beta[2];


			System.out.println("loutsa prediction:"+util.getHumanTime((long)prediction));

		}
		
		  
		 
	}
	

}
