package com.cs.core.bgprocess.dao;

import com.cs.core.bgprocess.dto.BGProcessDTO;
import com.cs.core.bgprocess.idao.IBGPCursor;
import com.cs.core.bgprocess.idao.IBGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.dataintegration.dto.PXONExportPlanDTO;
import com.cs.core.dataintegration.idto.IPXONExportPlanDTO;
import com.cs.core.dataintegration.idto.IPXONExportScopeDTO;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IUserDTO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * PXP Background Processes monitoring services
 *
 * @author vallee
 */
public class BGPDriverDAO implements IBGPDriverDAO {
  
  // Singleton implementation
  private static final BGPDriverDAO INSTANCE = new BGPDriverDAO();
  
  private BGPDriverDAO()
  {
  }
  
  public static BGPDriverDAO instance()
  {
    return INSTANCE;
  }
  
  private BGProcessDAS bgpDAS = new BGProcessDAS();
  
  @Override
  public long submitBGPProcess(String user, String service, String callbackURL,
      IBGProcessDTO.BGPPriority userPriority, IJSONContent entryData) throws RDBMSException
  {
    IUserDTO userDTO = ConfigurationDAO.instance().createUser(user);
    Long jobIID = bgpDAS.postNewPendingTask(userDTO.getUserIID(), service, callbackURL,
        userPriority.ordinal(), entryData.toString());
    return jobIID;
  }
  
  @Override
  public IBGProcessDTO getBGPProcess(long jobIID) throws RDBMSException
  {
    BGProcessDTO bgpProcess = bgpDAS.getBGPProcess(jobIID);
    return bgpProcess;
  }
  
  @Override
  public File loadJobLogFile(long jobIID) throws RDBMSException
  {
    BGProcessDTO bgpProcess = bgpDAS.getBGPProcess(jobIID);
    byte[] log = bgpProcess.getLog().toBytes();
    try {
      String directory = CSProperties.instance().getString("httpServer.downloadDir");
      Path path = FileSystems.getDefault().getPath(directory, "Job" + jobIID + ".log");
      try (FileOutputStream outputStream = new FileOutputStream(path.toFile())) {
        outputStream.write(log);
        outputStream.close();
      }
      return path.toFile();
    }
    catch (IOException | CSInitializationException ex) {
      RDBMSLogger.instance().exception(ex);
      throw new RDBMSException(1010, "WebApplicationException",ex.getMessage());
    }
  }
  
  @Override
  public IPXONExportPlanDTO prepareExport(IPXONExportScopeDTO exportScope) throws RDBMSException
  {
    PXONExportPlanDTO pxonExportPlanDTO = new PXONExportPlanDTO(exportScope);
    // identify entity IIDs to load
    if (!pxonExportPlanDTO.includeAllEntities() && pxonExportPlanDTO.getBaseEntityIIDs().isEmpty()
        && exportScope.getBaseEntityIDs().size() > 0) {
      Set<Long> allBaseEntityIIDsExportPlan = bgpDAS.getAllEntityIIDs(
          exportScope.getBaseEntityIDs());
      pxonExportPlanDTO.getBaseEntityIIDs().addAll(allBaseEntityIIDsExportPlan);
    }
    
    if (!pxonExportPlanDTO.includeAllPropertyEntities() && pxonExportPlanDTO.getEntityPropertyIIDs()
        .isEmpty()) {
      Set<Long> allBaseEntityPropertyIID = bgpDAS
          .getAllBaseEntityPropertyIID(exportScope.getBaseEntityProperties());
      pxonExportPlanDTO.getEntityPropertyIIDs()
          .addAll(allBaseEntityPropertyIID);
    }
    return pxonExportPlanDTO;
  }
  
  @Override
  public IBGPCursor getBGProcesses() throws RDBMSException
  {
    AllBGPCursorProvider cursorProvider = new AllBGPCursorProvider();
    return new AllBGPDataCursor(cursorProvider);
  }
  
  @Override
  public IJSONContent getServiceEntryTemplate(String service) throws RDBMSException
  {
    // Will come with a future improvement, no need here in a first time..
    return new JSONContent(); // currently not implemented in current version
  }
  
  @Override
  public List<String> getServices() throws RDBMSException
  {
     try {
       String services = CSProperties.instance().getString("bgp.scheduler.services");
       return Arrays.asList(services.split("\\s*,\\s*"));
     }
     catch (CSInitializationException e) {
      RDBMSLogger.instance().exception(e);
       throw new RDBMSException(10000, e.getMessage(), "File Redaing Exception");
     }
  }
}
