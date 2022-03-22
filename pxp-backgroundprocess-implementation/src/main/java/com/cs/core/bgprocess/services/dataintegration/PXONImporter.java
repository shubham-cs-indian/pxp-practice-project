package com.cs.core.bgprocess.services.dataintegration;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.bgprocess.dto.BGProcessDTO;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import com.cs.core.bgprocess.dto.BGPLog;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.data.Text;
import com.cs.core.dataintegration.dto.PXONImporterPlanDTO;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/** @author vallee */
public class PXONImporter extends AbstractBGProcessJob implements IBGProcessJob {

  private final Set<ImportSteps>                   APPLICABLE_STEPS = new HashSet<>();
  private       Path                               path             = null;
  private final PXONImporterBlocksMap<ImportSteps> importBlocks     = new PXONImporterBlocksMap<>();
  private       PXONImporterDAS                    pxonImporterDAS  = null;
  private final PXONImporterPlanDTO                importerPlanDTO  = new PXONImporterPlanDTO();
  private       long                               numberOfExceptions;

  //constants to be used
  protected static final String                       FAILURE_IDs      = "failedIds";
  protected static final String                       SUCCESS          = "success";
  protected static final String                       FAILURE          = "failure";

  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO defaultUserSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, defaultUserSession);
    pxonImporterDAS = new PXONImporterDAS(userSession);
    String directory = CSProperties.instance().getString("nfs.file.path")+"/";

    importerPlanDTO.fromJSON(jobData.getEntryData().toString());
    path = FileSystems.getDefault()
        .getPath( directory + importerPlanDTO.getRelativeImportFile());
    numberOfExceptions = 0;
    // Prepare steps based on DTO
    prepareStepsToImport();

    jobData.getProgress().initSteps(ImportSteps.getLabels().toArray(new String[0]));
    jobData.getLog().progress(log() + "Job started with iid=%d", jobData.getJobIID());
    // Prepare steps based on DTO
    updateProcessData();
  }

  @Override
  public IBGProcessDTO.BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException
  {
    if (jobData.getProgress().getPercentageCompletion() == 100) {
      jobData.setStatus(BGPStatus.ENDED_SUCCESS);
      return jobData.getStatus();
    }
    int currentStepNo = jobData.getProgress().getCurrentStepNo();
    ImportSteps currentStep = ImportSteps.valueOf(currentStepNo - 1);
    if(APPLICABLE_STEPS.contains(currentStep)) {
      IImportEntity importer = ImportSteps.getImporter(currentStep);
      importer.importEntity(this);
    }
    jobData.getProgress().incrStepNo();

    if (jobData.getProgress().getPercentageCompletion() == 100) {
      if(numberOfExceptions >0)
        jobData.setStatus(BGPStatus.ENDED_EXCEPTION);
      else
        jobData.setStatus(BGPStatus.ENDED_SUCCESS);
      moveImportFile();
    }
    else
      jobData.setStatus(BGPStatus.RUNNING);

    updateProcessData();
    return jobData.getStatus();
  }

  private void moveImportFile() throws CSInitializationException, RDBMSException
  {
    // TODO : need to confirm path
    String nsfPath = CSProperties.instance()
        .getString("nfs.file.path") + "/";
   
    File sourceFile = path.toFile();
    File targetFile = null;
    if(importerPlanDTO.getRelativeAfterImportFile().startsWith(nsfPath)) {
      targetFile = new File(""+importerPlanDTO.getRelativeAfterImportFile());
    }else {
      targetFile = new File(nsfPath + importerPlanDTO.getRelativeAfterImportFile());
    }
    if (targetFile.exists()) {
      targetFile.delete();
    }
    try {
      if(sourceFile.exists()) {
        FileUtils.moveFile(sourceFile, targetFile);
      }else {
        jobData.getLog().error("source file not found");
        throw new RDBMSException(10000, "IOException", "source file not found "+sourceFile.getPath());
      }
    }
    catch (IOException e) {
      jobData.getLog().error(e.getMessage());
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }
  
  /**
   * Register an identified import block
   * @param pxonObject the current parsed object
   * @return a new empty block corresponding to the imported object
   * @throws CSInitializationException
   * @throws CSFormatException 
   */
  private ImportBlockInfo newImportBlockInfo(JSONObject pxonObject, PXONFileParser.PXONBlock parsedBlock) 
          throws CSInitializationException, CSFormatException
  {
    ICSEElement cse = (new CSEParser()).parseDefinition((String) pxonObject.get(IPXON.PXONTag.csid.toTag()));
    ICSEObject cseObject = (ICSEObject) cse;

    String specificationType = cseObject.getSpecification(ICSEElement.Keyword.$type);
    ICSEElement.CSEObjectType cseObjectType = cseObject.getObjectType();
    String language = (String) pxonObject.get("$baselocale");
    String identifier = cseObject.getCode();
    ImportBlockIdentifier importBlockIdentifier = new ImportBlockIdentifier();
    importBlockIdentifier.setIdentifier(identifier);
    importBlockIdentifier.setLanguage(language);
    ImportSteps currentStep = ImportSteps.getStepFromPXON(pxonObject, specificationType, cseObjectType);
    return importBlocks.registerBlock(currentStep, importBlockIdentifier, parsedBlock);
  }

  private void prepareStepsToImport() throws CSInitializationException, CSFormatException, RDBMSException {
	  try(PXONFileParser pxonFileParser = new PXONFileParser(path.toString())){
    PXONFileParser.PXONBlock blockInfo = null;
    while ((blockInfo = pxonFileParser.getNextBlock()) != null) {
      try {
        JSONObject pxonObject = JSONContent.StringToJSON(blockInfo.getData());
        newImportBlockInfo(pxonObject, blockInfo);
      }
      catch (Exception e) {
        incrementNumberOfException();
        jobData.getLog().error(e.getMessage());
      }
    }
    APPLICABLE_STEPS.addAll( importBlocks.getSteps());
    String declaredSteps = Text.join( ",", importBlocks.reportSteps());
    RDBMSLogger.instance().info( "RDBMSImporter parsed import steps: " + declaredSteps);
    jobData.getLog().info(log() + "Identified sections: " + declaredSteps);
	  }catch (IOException | CSFormatException e) {
	    incrementNumberOfException();
      jobData.getLog().error(e.getMessage());
      RDBMSLogger.instance().warn(e.getMessage());
      throw new RDBMSException(10000, e.getClass().getSimpleName(), e.getMessage());
    }
  }

  /**
   * this method will write the header for log file.
   */
  @Override
  protected void writeLogHeader()
  {
    jobData.getLog().info("Time|BGP Service|Job iid|Session Id|User Name|Comment" );
    jobData.getLog().progress(log() + "Starting from status %s\n", jobData.getStatus());
  }


  /**
   * log info into bgp log
   * @param responseMap
   */
  protected void logIds(Map<String, Object> responseMap)
  {
    BGPLog log = jobData.getLog();
    log.info("Successfully created list"+responseMap.get(SUCCESS).toString());
    log.error(responseMap.get(FAILURE).toString());
    List<Map<String,Object>> failureIdMap = (List<Map<String, Object>>) responseMap.get(FAILURE_IDs);
    if(failureIdMap == null)
      failureIdMap = new ArrayList<>(); 
    numberOfExceptions += failureIdMap.size();
      log.error(failureIdMap.toString());
  }

  protected void incrementNumberOfException()
  {
    numberOfExceptions++;
  }
  protected void incrementNumberOfException(Integer numberToIncrement)
  {
    numberOfExceptions+= numberToIncrement;
  }
  protected BGProcessDTO getJobData()
  {
    return jobData;
  }

  protected PXONImporterBlocksMap<ImportSteps> getBlocks()
  {
    return importBlocks;
  }

  protected PXONImporterDAS getDAS()
  {
    return pxonImporterDAS;
  }

  protected Path getPath()
  {
    return path;
  }

  protected PXONImporterPlanDTO getImportDTO()
  {
    return importerPlanDTO;
  }
  
  @Override
  protected String getCallbackData()
  {
    Map<String, Object> callBackData = new HashMap<>();
    callBackData.put(SUCCESS_IIDS, jobData.getSuccessIds());
    callBackData.put(FAILED_IIDS, jobData.getFailedIds());
    return new JSONObject(callBackData).toString();
  }
}
