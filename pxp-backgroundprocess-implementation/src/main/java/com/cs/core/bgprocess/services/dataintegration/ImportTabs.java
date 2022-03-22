package com.cs.core.bgprocess.services.dataintegration;

import com.cs.config.dto.*;
import com.cs.config.idto.*;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/** @author vallee */
public class ImportTabs implements IImportEntity  {

  private static final String IMPORT_TABS                     = "ImportTabs";

  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> userBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_TAB);
    try(PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigTabDTO> tabs = new ArrayList<>();
      for (Entry userBlock : userBlocks.entrySet()) {
        IConfigTabDTO tab = new ConfigTabDTO();
        tab.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) userBlock.getValue()));
        // Upsert one by one into RDBMS
        tabs.add(tab);
      }
      // create users in ODB
      Map<String, Object> responseMap = configurationImport( importer.getImportDTO().getLocaleID(), tabs);
      importer.logIds(responseMap);
    } catch (IOException e) {
      importer.incrementNumberOfException();
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }


  public Map<String, Object> configurationImport(String localeID, Collection<IConfigTabDTO> tabs)
      throws CSInitializationException, CSFormatException
  {
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, tabs);

    return CSConfigServer.instance().request(requestModel, IMPORT_TABS, localeID);
  }
}
