package com.example.push.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.annotations.Push;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@Push
@SpringUI(path = "")
public class MyUI extends UI {

	Label label = new Label("Now : ");

	//VerticalLayout layout = new VerticalLayout();

	@Override
	protected void init(VaadinRequest request) {
		// Put a widget on this UI. In real work we would use a Layout.
		setContent(label);

		// Start the data feed thread
		new FeederThread().start();

		//getUI().access(() -> layout.addComponent(new Label("Hello!")));
	}

	/*
	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false) 
	public static class MyUIServlet extends VaadinServlet { }
	 */

	public void tellTime() {
		label.setValue("Now : " + new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss").format(new Date()));
	}

	class FeederThread extends Thread {
		int count = 0;

		@Override
		public void run() {
			try {
				// Update the data for a while
				while (count < 60) {
					Thread.sleep(1000);

					// Calling special 'access' method on UI object, for inter-thread communication.
					access(() -> {
						count++;
						tellTime();
					});
				}

				// Inform that we have stopped running
				// Calling special 'access' method on UI object, for inter-thread communication.
				access(new Runnable() {
					@Override
					public void run() {
						label.setValue("Done.");
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
