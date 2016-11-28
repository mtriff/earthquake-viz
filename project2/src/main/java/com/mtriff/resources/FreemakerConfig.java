package com.mtriff.resources;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

public class FreemakerConfig {

	private static Configuration configuration;

	public static Configuration getConfig(ServletContext servletContext) throws IOException {
		if (configuration == null) {
			// Create your Configuration instance, and specify if up to what FreeMarker
			// version (here 2.3.25) do you want to apply the fixes that are not 100%
			// backward-compatible. See the Configuration JavaDoc for details.
			configuration = new Configuration(Configuration.VERSION_2_3_25);

			// Set the preferred charset template files are stored in. UTF-8 is
			// a good choice in most applications:
			configuration.setDefaultEncoding("UTF-8");

			// Sets how errors will appear.
			// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
			configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

			// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
			configuration.setLogTemplateExceptions(false);
			
			StringTemplateLoader stringLoader = new StringTemplateLoader();
			   stringLoader.putTemplate("Index", IOUtils.toString(servletContext.getResourceAsStream("/views/Index.ftlh"), "UTF-8"));
			   stringLoader.putTemplate("Magnitude", IOUtils.toString(servletContext.getResourceAsStream("/views/Magnitude.ftlh"), "UTF-8"));
			   stringLoader.putTemplate("QuakeLocation", IOUtils.toString(servletContext.getResourceAsStream("/views/QuakeLocation.ftlh"), "UTF-8"));
			   stringLoader.putTemplate("TsunamiCount", IOUtils.toString(servletContext.getResourceAsStream("/views/TsunamiCount.ftlh"), "UTF-8"));
			   stringLoader.putTemplate("TsunamiLocation", IOUtils.toString(servletContext.getResourceAsStream("/views/TsunamiLocation.ftlh"), "UTF-8"));
			   stringLoader.putTemplate("layout", IOUtils.toString(servletContext.getResourceAsStream("/views/_Layout.ftlh"), "UTF-8"));
			configuration.setTemplateLoader(stringLoader);
		}
		return configuration;
	}
	
	public static String getRenderedTemplate(ServletContext servletContext, String templateName, Map<String, Object> model) {
		try {
			Logger.getAnonymousLogger().info(System.getProperty("user.dir"));
			model.put("RenderBody", templateName);
			StringWriter writer = new StringWriter();
			Configuration config = getConfig(servletContext);
			Template template = config.getTemplate("layout");
			template.process(model, writer);
			return writer.toString();
		} catch (TemplateNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
