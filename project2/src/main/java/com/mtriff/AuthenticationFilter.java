package com.mtriff;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import com.mtriff.services.DatabaseAccessObject;
 
/**
 * This filter verify the access permissions for a user
 * based on username and passowrd provided in request
 * Modified from: http://howtodoinjava.com/jersey/jersey-rest-security/
 * */
@Provider
public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter
{
     
    @Context
    private ResourceInfo resourceInfo;
     
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
                                                        .entity("{ \"result\": \"UNAUTHORIZED\" }").build();
    private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN)
                                                        .entity("{ \"result\": \"FORBIDDEN\" } ").build();
      
    public static String[] getAuthorization(ContainerRequestContext requestContext) {
        //Get request headers
        final MultivaluedMap<String, String> headers = requestContext.getHeaders();
          
        //Fetch authorization header
        final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
          
        //If no authorization information present; block access
        if(authorization == null || authorization.isEmpty())
        {
            requestContext.abortWith(ACCESS_DENIED);
            return null;
        }
    	
    	//Get encoded username and password
        final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
          
        //Decode username and password
        String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;

        //Split username and password tokens
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        
        return new String[] { username, password };
    }
    
    @Override
    public void filter(ContainerRequestContext requestContext)
    {
        Method method = resourceInfo.getResourceMethod();
        //Access allowed for all
        if( ! method.isAnnotationPresent(PermitAll.class))
        {
            //Access denied for all
            if(method.isAnnotationPresent(DenyAll.class))
            {
                requestContext.abortWith(ACCESS_FORBIDDEN);
                return;
            }

            //Verify user access
            if(method.isAnnotationPresent(RolesAllowed.class))
            {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

                String[] authValues = getAuthorization(requestContext);
                
                //Is user valid?
                if( ! isUserAllowed(authValues[0], authValues[1], rolesSet))
                {
                    requestContext.abortWith(ACCESS_DENIED);
                    return;
                }
            }
        }
    }
    private boolean isUserAllowed(final String email, final String password, final Set<String> rolesSet)
    {
        return DatabaseAccessObject.getDAO().verifyCredentials(email, password);
    }
}