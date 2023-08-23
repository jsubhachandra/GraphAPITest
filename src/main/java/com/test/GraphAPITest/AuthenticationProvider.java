package com.test.GraphAPITest;

import java.util.Arrays;
import java.util.List;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;

import okhttp3.Request;

public class AuthenticationProvider {

    private static AuthenticationProvider authenticationProvider = null;


    public AuthenticationProvider(){ }

    public static AuthenticationProvider getInstance(){
        if (authenticationProvider == null) {
            authenticationProvider = new AuthenticationProvider();
        }
        return authenticationProvider;
    }

    public GraphServiceClient getAuthProvider() throws Exception {

//        UsernamePasswordProvider authProvider = new UsernamePasswordProvider(
//                ApplicationProperties.getClientId(),
//                ApplicationProperties.getScopeList(),
//                ApplicationProperties.getUsername(),
//                ApplicationProperties.getPassword(),
//                NationalCloud.Global,
//                ApplicationProperties.getTenantId(),
//                ApplicationProperties.getClientSecret());

//    	com.microsoft.graph.authentication.TokenCredentialAuthProvider authProvider1 = new TokenCredentialAuthProvider(
//        	    scopes, credential);
//        
//        IGraphServiceClient graphClient =
//                GraphServiceClient
//                        .builder()
//                        .authenticationProvider(authProvider)
//                        .buildClient();
    	
    	final String clientId = "44d90de1-a6bf-480e-a23e-34b98d440828";
    	final String tenantId = "821fbcdb-84c3-4570-a2f1-96d43f588da5";
    	final String clientSecret = "~ho8Q~IaWTlljeFfsUBSJBiRVFK.69jvyBjBNadJ";

    	// The client credentials flow requires that you request the
    	// /.default scope, and pre-configure your permissions on the
    	// app registration in Azure. An administrator must grant consent
    	// to those permissions beforehand.
    	final List<String> scopes = Arrays.asList("https://graph.microsoft.com/.default");

    	final ClientSecretCredential credential = new ClientSecretCredentialBuilder()
    	    .clientId(clientId).tenantId(tenantId).clientSecret(clientSecret).build();

    	if (null == scopes || null == credential) {
    	    throw new Exception("Unexpected error");
    	}
    	final TokenCredentialAuthProvider authProvider = new TokenCredentialAuthProvider(
    	    scopes, credential);

    	final GraphServiceClient<Request> graphClient = GraphServiceClient.builder()
    	    .authenticationProvider(authProvider).buildClient();
    

        return graphClient;
    }
}