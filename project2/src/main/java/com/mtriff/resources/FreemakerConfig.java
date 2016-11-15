package com.mtriff.resources;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

public class FreemakerConfig {

	private static Configuration configuration;
	
	public static Configuration getConfig() throws IOException {
		if (configuration == null) {
			// Create your Configuration instance, and specify if up to what FreeMarker
			// version (here 2.3.25) do you want to apply the fixes that are not 100%
			// backward-compatible. See the Configuration JavaDoc for details.
			configuration = new Configuration(Configuration.VERSION_2_3_25);

			// Specify the source where the template files come from. Here I set a
			// plain directory for it, but non-file-system sources are possible too:
			File templateDir = new File(System.getProperty("user.dir") + "/src/main/java/com/mtriff/html");
			configuration.setDirectoryForTemplateLoading(templateDir);

			// Set the preferred charset template files are stored in. UTF-8 is
			// a good choice in most applications:
			configuration.setDefaultEncoding("UTF-8");

			// Sets how errors will appear.
			// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
			configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

			// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
			configuration.setLogTemplateExceptions(false);
		}
		return configuration;
	}
	
	public static String getRenderedTemplate(String templateName, Map<String, Object> model) {
		try {
			model.put("RenderBody", "../home/" + templateName + ".ftlh");
			Template template = getConfig().getTemplate("views/shared/_Layout.ftlh");
			StringWriter writer = new StringWriter();
			template.process(model, writer);
			return writer.toString();
		} catch (TemplateNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
