package com.mtriff;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.mtriff.resources");
        rc.register(AuthenticationFilter.class);
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        StaticHttpHandler imageHandler = new StaticHttpHandler("src/main/java/com/mtriff/html/images");
        StaticHttpHandler jsHandler = new StaticHttpHandler("src/main/java/com/mtriff/html/js");
        StaticHttpHandler cssHandler = new StaticHttpHandler("src/main/java/com/mtriff/html/css");
        StaticHttpHandler dataHandler = new StaticHttpHandler("src/main/java/com/mtriff/html/data");
        server.getServerConfiguration().addHttpHandler(imageHandler, "/images");
        server.getServerConfiguration().addHttpHandler(jsHandler, "/js");
        server.getServerConfiguration().addHttpHandler(cssHandler, "/css");
        server.getServerConfiguration().addHttpHandler(dataHandler, "/data");
        return server;
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}

