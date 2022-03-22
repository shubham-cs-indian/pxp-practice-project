package com.cs.core.bgprocess.services.dataintegration;

import com.cs.config.dto.ConfigTaskDTO;
import com.cs.config.idto.IConfigTaskDTO;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.ITaskDTO;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/** @author vallee */
public class ImportTasks implements IImportEntity  {

  private static final String IMPORT_TASKS                    = "ImportTasks";

  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> taskBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_TASK);
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    try(PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigTaskDTO> taskDTOList = new ArrayList<>();
      for (Entry taskBlock : taskBlocks.entrySet()) {
        IConfigTaskDTO configTaskDTO = new ConfigTaskDTO();
        configTaskDTO.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) taskBlock.getValue()));
        // Upsert one by one into RDBMS
        ITaskDTO.TaskType taskType = ITaskDTO.TaskType.valueOf(configTaskDTO.getType());
        configurationDAO.createTask(configTaskDTO.getCode(), taskType);
        taskDTOList.add(configTaskDTO);
      }
      // create tasks in ODB
      Map<String, Object> responseMap = configurationImport( importer.getImportDTO().getLocaleID(), taskDTOList.toArray(new IConfigTaskDTO[0]));
      importer.logIds(responseMap);
    } catch (IOException e) {
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }

  public Map<String,Object> configurationImport(String localeID, IConfigTaskDTO... tasks) throws CSInitializationException, CSFormatException
  {
    List<IConfigTaskDTO> tasksList =  Arrays.asList(tasks);
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, tasksList);

    return CSConfigServer.instance()
        .request(requestModel, IMPORT_TASKS, localeID);
  }
}
