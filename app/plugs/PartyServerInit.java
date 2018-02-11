package plugs;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import task.NotifyUser;
import task.WxAccessTokenReflush;

public class PartyServerInit implements jws.Init{

	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(10); 
	
	@Override
	public void init() {
		service.scheduleAtFixedRate(new WxAccessTokenReflush(), 0, 1, TimeUnit.MINUTES);
		service.scheduleAtFixedRate(new NotifyUser(), 0, 1, TimeUnit.MINUTES);
	}

}
