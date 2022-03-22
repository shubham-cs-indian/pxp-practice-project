package com.cs.core.bgprocess.rest;

import com.cs.core.bgprocess.BGProcessDispatcher;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dao.BGProcessDAS;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.dataintegration.dto.PXONExportScopeDTO;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.dataintegration.idto.IPXONExportPlanDTO;
import com.cs.core.dataintegration.idto.IPXONExportScopeDTO;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 * BGP Jobs management services
 *
 * @author vallee
 */
@Path("/bgp")
public class Bgp {
  
  private final BGProcessDAS bgpProcessDAS = new BGProcessDAS();
  
  /**
   * [server URL]/REST/bgp/man service=SERVICE
   *
   * @param request
   * @param service
   * @return
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  @GET
  @Path("man")
  @Produces(MediaType.APPLICATION_JSON)
  public String man(@Context HttpServletRequest request, @QueryParam("service") String service)
      throws RDBMSException, CSFormatException
  {
    RDBMSLogger.instance()
        .info("Starting REST service bgp/man " + service);
    JSONContent content = new JSONContent(
        "{\"message\":\"service not implemented in current version...\"}");
    return content.toString();
  }
  
  /**
   * [server URL]/REST/bgp/submit service=SERVICE user=USER
   * userPriority=LOW|MEDIUM|HIGH
   *
   * @param request
   * @param service
   * @param userName
   * @param priority
   * @param jsonEntry
   *          entry parameters in the query body
   * @return job IID
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  @POST
  @Path("submit")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public String submit(@Context HttpServletRequest request, @QueryParam("service") String service,
      @QueryParam("user") String userName, @QueryParam("priority") String priority,
      String jsonEntry) throws RDBMSException, CSFormatException
  {
    
    RDBMSLogger.instance()
        .info("Starting REST service bgp/submit " + service);
    JSONContent entryParameters = new JSONContent(jsonEntry);
    String callBackURI = entryParameters.getInitField( IPXON.PXONTag.callback.toTag(), "");
    BGPPriority userPriority = BGPPriority.valueOf(priority);
    long jobIID = BGPDriverDAO.instance()
        .submitBGPProcess(userName, service, callBackURI, userPriority, entryParameters);
    return String.format("%d", jobIID);
  }
  
  /**
   * [server URL]/REST/bgp/info job=IID
   *
   * @param request
   * @param jobIID
   * @return
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  @GET
  @Path("info")
  @Produces(MediaType.APPLICATION_JSON)
  public String info(@Context HttpServletRequest request, @QueryParam("job") String jobIID)
      throws RDBMSException, CSFormatException
  {
    
    RDBMSLogger.instance()
        .info("Starting REST service bgp/info " + jobIID);
    long iid = Long.parseLong(jobIID);
    IBGProcessDTO process = BGPDriverDAO.instance().getBGPProcess(iid);
    return process.toPXON();
  }
  
  /**
   * [server URL]/REST/bgp/log job=IID
   *
   * @param request
   * @param jobIID
   * @return
   * @throws RDBMSException
   */
  @GET
  @Path("log")
  public Response log(@Context HttpServletRequest request, @QueryParam("job") String jobIID)
      throws RDBMSException
  {
    
    RDBMSLogger.instance()
        .info("Starting REST service bgp/log " + jobIID);
    long iid = Long.parseLong(jobIID);
    File logFile = BGPDriverDAO.instance().loadJobLogFile(iid);
    StreamingOutput fileStream = (output) -> {
      try {
        java.nio.file.Path path = Paths.get(logFile.toURI());
        byte[] data = Files.readAllBytes(path);
        output.write(data);
        output.flush();
      }
      catch (IOException ex) {
        RDBMSLogger.instance().exception(ex);
        throw new WebApplicationException(ex.getMessage());
      }
    };
    return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
        .header("content-disposition", "attachment; filename = " + logFile.getName())
        .build();
  }
  
  /**
   * [server URL]/REST/bgp/exportplan
   *
   * @param request
   * @param jsonExportScope
   *          entry parameters in the query body
   * @return an export plan as JSON in the response body
   * @throws RDBMSException
   * @throws CSFormatException
   */
  @POST
  @Path("exportplan")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String exportplan(@Context HttpServletRequest request, String jsonExportScope)
      throws RDBMSException, CSFormatException
  {
    IPXONExportScopeDTO exportScope = new PXONExportScopeDTO();
    exportScope.fromJSON(jsonExportScope);
    IPXONExportPlanDTO exportPlan = BGPDriverDAO.instance()
        .prepareExport(exportScope);
    return exportPlan.toJSON();
  }
  
  /**
   * [server URL]/REST/bgp/pause job=IID
   *
   * @param request
   * @param jobIID
   * @return
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSInitializationException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  @PUT
  @Path("pause")
  @Produces(MediaType.TEXT_PLAIN)
  public String pause(@Context HttpServletRequest request, @QueryParam("job") String jobIID)
      throws RDBMSException, CSInitializationException, CSFormatException
  {
    bgpProcessDAS.updateStatus(Long.parseLong(jobIID), BGPStatus.PAUSED.ordinal());
    // also manage alive job
    BGProcessDispatcher.instance()
        .manageJobWithStopStatus(Long.parseLong(jobIID), BGPStatus.PAUSED);
    return jobIID;
  }
  
  /**
   * [server URL]/REST/bgp/cancel job=IID
   *
   * @param request
   * @param jobIID
   * @return
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSInitializationException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  @PUT
  @Path("cancel")
  @Produces(MediaType.TEXT_PLAIN)
  public String cancel(@Context HttpServletRequest request, @QueryParam("job") String jobIID)
      throws RDBMSException, CSInitializationException, CSFormatException
  {
    bgpProcessDAS.updateStatus(Long.parseLong(jobIID), IBGProcessDTO.BGPStatus.CANCELED.ordinal());
    // also cancel the job
    BGProcessDispatcher.instance()
        .manageJobWithStopStatus(Long.parseLong(jobIID), BGPStatus.CANCELED);
    return jobIID;
  }
  
  /**
   * [server URL]/REST/bgp/resume job=IID
   *
   * @param request
   * @param jobIID
   * @return
   * @throws RDBMSException
   */
  @PUT
  @Path("resume")
  @Produces(MediaType.TEXT_PLAIN)
  public String resume(@Context HttpServletRequest request, @QueryParam("job") String jobIID)
      throws RDBMSException
  {
    bgpProcessDAS.updateStatus(Long.parseLong(jobIID), IBGProcessDTO.BGPStatus.PENDING.ordinal());
    return jobIID;
  }
}
