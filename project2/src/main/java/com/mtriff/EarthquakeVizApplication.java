package com.mtriff;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;

import com.mtriff.resources.ChartDataResource;
import com.mtriff.resources.IndexResource;
import com.mtriff.resources.MagnitudeResource;
import com.mtriff.resources.QuakeLocationResource;
import com.mtriff.resources.TsunamiCountResource;
import com.mtriff.resources.TsunamiLocationResource;
import com.mtriff.resources.UserResource;
import com.mtriff.services.DatabaseAccessObject;

public class EarthquakeVizApplication extends ResourceConfig {
	
	public EarthquakeVizApplication() {
		super();
		register(org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature.class);
		property(FreemarkerMvcFeature.TEMPLATE_BASE_PATH, "views");
		register(LoggingFeature.class);
		register(FreemarkerMvcFeature.class);
		
		register(IndexResource.class);
		register(MagnitudeResource.class);
		register(QuakeLocationResource.class);
		register(TsunamiCountResource.class);
		register(TsunamiLocationResource.class);
		
		register(ChartDataResource.class);
		register(UserResource.class);
		register(AuthenticationFilter.class);
		DatabaseAccessObject.getDAO();
	}
	
}
