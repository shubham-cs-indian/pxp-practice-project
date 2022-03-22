
package com.cs.core.bgprocess.services.dataintegration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import com.cs.config.dto.ConfigPermissionDTO;
import com.cs.config.idto.IConfigPermissionDTO;
import com.cs.core.bgprocess.dto.BGPLog;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class ImportPermissions extends PXONImporter implements IImportEntity{

  private static final String IMPORT_PERMISSIONS = "ImportPermissions";

  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> permissionBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_PERMISSION);
    BGPLog log = importer.getJobData().getLog();
    try(PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigPermissionDTO> permissions = new ArrayList<>();
      for (Entry<ImportBlockIdentifier, ImportBlockInfo> permissionBlock : permissionBlocks.entrySet()) {
        IConfigPermissionDTO permissionDTO = new ConfigPermissionDTO();
        permissionDTO.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) permissionBlock.getValue()));
        
        permissions.add(permissionDTO);
      }
      // create users in ODB
      Map<String, Object> responseMap = configurationImport( importer.getImportDTO().getLocaleID(), permissions);
      importer.logIds(responseMap);
    }
    catch (IOException e) {
      importer.incrementNumberOfException();
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }

  private Map<String, Object> configurationImport(String localeID, List<IConfigPermissionDTO> permissions)
      throws CSFormatException, CSInitializationException
  {
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, permissions);
    
    return CSConfigServer.instance().request(requestModel, IMPORT_PERMISSIONS, localeID);
  }
  
}
