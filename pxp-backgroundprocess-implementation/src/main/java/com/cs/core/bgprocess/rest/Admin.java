package com.cs.core.bgprocess.rest;

import com.cs.core.data.ISODateTime;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Administrator services
 *
 * @author vallee
 */
@Path("/admin")
public class Admin {
  
  /**
   * [server URL]/REST/admin/ping
   *
   * @param request
   *          current HttpRequest
   * @return server name and address
   * @throws java.net.UnknownHostException
   */
  @GET
  @Path("ping")
  @Produces(MediaType.TEXT_PLAIN)
  public String props(@Context HttpServletRequest request) throws UnknownHostException
  {
    RDBMSLogger.instance()
        .info("Starting REST service admin/ping");
    InetAddress serverAddr = InetAddress.getLocalHost();
    return String.format("BGP server alive at IP %s [%s], server time %s", serverAddr.getHostName(),
        serverAddr, ISODateTime.toString(System.currentTimeMillis()));
  }
  
  /**
   * [server URL]/REST/admin/props
   *
   * @return the list of defined properties
   * @throws com.cs.core.technical.exception.CSInitializationException
   */
  @GET
  @Path("props")
  @Produces(MediaType.TEXT_PLAIN)
  public String props() throws CSInitializationException
  {
    RDBMSLogger.instance()
        .info("Starting REST service admin/props");
    StringBuilder propListing = new StringBuilder("BGP current properties:\n");
    Set<String> props = CSProperties.instance()
        .getPropertyNames();
    for (String prop : props) {
      propListing.append("\t")
          .append(prop)
          .append("=")
          .append(CSProperties.instance().getString(prop))
          .append("\n");
    }
    return propListing.toString();
  }
  
  /**
   * [server
   * URL]/REST/admin/callback?job=jobIID&status=ERROR/EXCEPTION/SUCCESS
   *
   * @param request current HttpRequest
   * @param jobIID the job IID to acknowledge
   * @param status the final job status
   * @return an acknowledge message
   * @throws com.cs.core.technical.exception.CSInitializationException
   */
  @PUT
  @Path("callback")
  @Produces(MediaType.APPLICATION_JSON)
  public String callback(@Context HttpServletRequest request, @QueryParam("job") Long jobIID,
      @QueryParam("status") String status) throws CSInitializationException
  {
    RDBMSLogger.instance()
        .info("Received callback return of job %d with status %s", jobIID, status);
    return String.format("End of job %d received with status %s", jobIID, status);
  }
}
