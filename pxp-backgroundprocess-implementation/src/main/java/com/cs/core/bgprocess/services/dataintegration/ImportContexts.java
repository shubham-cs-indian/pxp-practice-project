package com.cs.core.bgprocess.services.dataintegration;

import com.cs.config.dto.ConfigContextDTO;
import com.cs.config.idto.IConfigContextDTO;
import com.cs.config.standard.IConfigMap;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/** @author vallee */
public class ImportContexts implements IImportEntity {

  private static final String UPSERT_CONTEXTS                 = "UpsertContexts";

  //TODO: Remaining Update
  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> contextBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_CONTEXT);
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    try(PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigContextDTO> contextDTOList = new ArrayList<>();

      for (Entry pcEntityMeta : contextBlocks.entrySet()) {
        ConfigContextDTO configContextDTO = new ConfigContextDTO();
        configContextDTO.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) pcEntityMeta.getValue()));

        //Create into RDBMS
        IContextDTO.ContextType contextType = IConfigMap.getContextType(configContextDTO.getType());
        configurationDAO.createContext(configContextDTO.getCode(), contextType);

        contextDTOList.add(configContextDTO);
      }

      //Creating Context in ODB
      Map<String, Object> responseMap = configurationImport(
          importer.getImportDTO().getLocaleID(), contextDTOList.toArray(new IConfigContextDTO[0]));
      importer.logIds(responseMap);
    } catch (IOException e) {
      importer.incrementNumberOfException();
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }

  public Map<String, Object> configurationImport(String localeID, IConfigContextDTO... contexts)
      throws CSInitializationException, CSFormatException
  {
    List<IConfigContextDTO> contextList = Arrays.asList(contexts);
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, contextList);

    return CSConfigServer.instance().request(requestModel, UPSERT_CONTEXTS, localeID);
  }
}
