package com.cs.core.runtime.strategy.usecase.loadbalancer.ribbon;

import com.netflix.client.AbstractLoadBalancerAwareClient;
import com.netflix.client.ClientException;
import com.netflix.client.ClientRequest;
import com.netflix.client.RequestSpecificRetryHandler;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import com.netflix.loadbalancer.reactive.ServerOperation;
import com.netflix.niws.client.http.HttpClientLoadBalancerErrorHandler;
import com.sun.jersey.api.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import rx.Observable;

import java.net.ConnectException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map.Entry;

/**
 * CSLoadBalancerAwareClient extends abstract class
 * AbstractLoadBalancerAwareClient that provides the integration of client with
 * load balancers. CSRestTemplate eventually calls
 * org.springframework.web.client RestTemplate
 */
public class CSLoadBalancerAwareClient
    extends AbstractLoadBalancerAwareClient<HttpRequest, HttpResponse> {
  
  /*@Autowired
  	TestSpring testSpring;
  */
  private String         restClientName;
  
  private IClientConfig  clientConfig;
  
  @Autowired
  private CSRestTemplate restTemplate;
  
  // private HashMap<String, List<Server>> map;
  
  public CSLoadBalancerAwareClient()
  {
    super(null);
  }
  
  public CSLoadBalancerAwareClient(ILoadBalancer lb)
  {
    super(lb);
    setRestClientName("default");
  }
  
  public CSLoadBalancerAwareClient(ILoadBalancer lb, IClientConfig ncc)
  {
    super(lb, ncc);
    initWithNiwsConfig(ncc);
  }
  
  public CSLoadBalancerAwareClient(IClientConfig ncc)
  {
    super(null, ncc);
    initWithNiwsConfig(ncc);
  }
  
  public CSLoadBalancerAwareClient(ILoadBalancer lb, Client jerseyClient)
  {
    super(lb);
    this.setRetryHandler(new HttpClientLoadBalancerErrorHandler());
  }
  
  /*public HashMap<String, List<Server>> getHashList() {
  		if (restClientName.contains("elastic")) {
  			map = ElasticLoadBalancer.getElasticMap();
  		} else {
  			map = OrientDBLoadBalancer.getOrientMap();
  		}
  
  		return map;
  	}
  */
  @Bean
  public CSRestTemplate getRestTemplate()
  {
    return new CSRestTemplate();
  }
  
  @Override
  public void initWithNiwsConfig(IClientConfig clientConfig)
  {
    super.initWithNiwsConfig(clientConfig);
    this.clientConfig = clientConfig;
    this.setRestClientName(clientConfig.getClientName());
    // getHashList();
  }
  
  /**
   * execute method calls CSRestTemplate to execute request
   */
  @Override
  public HttpResponse execute(HttpRequest request, IClientConfig requestConfig) throws Exception
  {
    HttpEntity<String> requestEntity = null;
    
    restTemplate = getRestTemplate();
    restTemplate.getMessageConverters()
        .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    
    Object entity = request.getEntity();
    URI requestedURI = request.getUri();
    MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<String, String>();
    List<Entry<String, String>> header = request.getHttpHeaders()
        .getAllHeaders();
    for (Entry<String, String> entry : header) {
      headerMap.add(entry.getKey(), entry.getValue());
    }
    headerMap.add("Accept", MediaType.APPLICATION_JSON_VALUE);
    
    if (entity != null) {
      
      requestEntity = new HttpEntity<String>(entity.toString(), headerMap);
    }
    else {
      requestEntity = new HttpEntity<>(headerMap);
    }
    try {
      ResponseEntity<String> response = restTemplate.exchange(requestedURI,
          getHttpMethod(request.getVerb()
              .verb()),
          requestEntity, String.class);
      
      CSHttpResponse httpResponse = new CSHttpResponse(response, requestedURI);
      return httpResponse;
    }
    catch (Exception e) {
      Throwable t = e;
      throw new Exception(t);
    }
  }
  
  public IClientConfig getClientConfig()
  {
    return clientConfig;
  }
  
  public void setClientConfig(IClientConfig clientConfig)
  {
    this.clientConfig = clientConfig;
  }
  
  /**
   * This method
   * {@link #executeWithLoadBalancer(ClientRequest, com.netflix.client.config.IClientConfig)}
   * is overriden.It is eventually called to dispatch the request to a server
   * which will be chosen by the load balancer. It calculates the final URI by
   * calling
   * {@link #reconstructURIWithServer(com.netflix.loadbalancer.Server, java.net.URI)}
   * and then calls {@link #execute(HttpRequest, IClientConfig)} with an
   * observable.
   *
   * @param HttpRequest
   *          request to be dispatched to a server with partial URI.
   * @param IClientConfig
   *          requestConfig of the named client
   * @throws ClientException
   */
  @Override
  public HttpResponse executeWithLoadBalancer(HttpRequest request, IClientConfig requestConfig)
      throws ClientException
  {
    RequestSpecificRetryHandler handler = getRequestSpecificRetryHandler(request, requestConfig);
    LoadBalancerCommand<HttpResponse> command = LoadBalancerCommand.<HttpResponse> builder()
        .withLoadBalancerContext(this)
        .withRetryHandler(handler)
        .withLoadBalancerURI(request.getUri())
        .build();
    try {
      return command.submit(new ServerOperation<HttpResponse>()
      {
        
        @Override
        public Observable<HttpResponse> call(Server server)
        {
          /*if (map.containsKey(request.getUri().toString())) {
          	List<Server> serverList = CSLoadBalancerAwareClient.this.map.get(request.getUri().toString());
          	serverList.add(server);
          
          } else {
          	ArrayList<Server> serverList = new ArrayList<>();
          	serverList.add(server);
          	CSLoadBalancerAwareClient.this.map.put(request.getUri().toString(), serverList);
          }*/
          
          URI finalUri = reconstructURIWithServer(server, request.getUri());
          HttpRequest requestForServer = (HttpRequest) request.replaceUri(finalUri);
          try {
            Observable<HttpResponse> resp = Observable
                .just(CSLoadBalancerAwareClient.this.execute(requestForServer, requestConfig));
            // System.out.println("\nRequest for : " + finalUri + " success\n");
            
            return resp;
          }
          catch (Exception e) {
            // System.out.println("\nRequest for : " + finalUri + " failed\n");
            if (e.getMessage()
                .contains("Connection refused")) {
              e = new ConnectException(e.getLocalizedMessage());
            }
            
            return Observable.error(e);
          }
        }
      })
          .toBlocking()
          .single();
    }
    catch (Exception e) {
      // Throwable t = e.getCause();
      // if (t instanceof ClientException) {
      // throw (ClientException) t;
      // } else {
      throw new ClientException(e.getMessage());
      // }
      // System.out.println(e.getMessage());
    }
  }
  
  @Override
  public HttpResponse executeWithLoadBalancer(HttpRequest request) throws ClientException
  {
    
    return executeWithLoadBalancer(request, getClientConfig());
  }
  
  @Override
  public RequestSpecificRetryHandler getRequestSpecificRetryHandler(HttpRequest request,
      IClientConfig requestConfig)
  {
    
    if (!request.isRetriable()) {
      return new RequestSpecificRetryHandler(false, false, this.getRetryHandler(), requestConfig);
    }
    if (this.clientConfig.get(CommonClientConfigKey.OkToRetryOnAllOperations, false)) {
      return new RequestSpecificRetryHandler(true, true, this.getRetryHandler(), requestConfig);
    }
    if (request.getVerb() != HttpRequest.Verb.GET) {
      return new RequestSpecificRetryHandler(true, false, this.getRetryHandler(), requestConfig);
    }
    else {
      return new RequestSpecificRetryHandler(true, true, this.getRetryHandler(), requestConfig);
    }
  }
  
  private HttpMethod getHttpMethod(String verb)
  {
    switch (verb) {
      case "GET":
        return HttpMethod.GET;
      case "POST":
        return HttpMethod.POST;
      case "DELETE":
        return HttpMethod.DELETE;
      case "PUT":
        return HttpMethod.PUT;
    }
    return null;
  }
  
  public String getRestClientName()
  {
    return restClientName;
  }
  
  public void setRestClientName(String restClientName)
  {
    this.restClientName = restClientName;
  }
}
