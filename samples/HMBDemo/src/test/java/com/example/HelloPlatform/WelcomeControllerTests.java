package com.example.HelloPlatform;

import org.testng.annotations.Test;

import com.sforce.ws.ConnectionException;
import com.vmforce.hmbdemo.WelcomeController;

public class WelcomeControllerTests {
	
	private WelcomeController controller = new WelcomeController();
	
	@Test
	public void welcome() {
		try {
			controller.welcome();
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
