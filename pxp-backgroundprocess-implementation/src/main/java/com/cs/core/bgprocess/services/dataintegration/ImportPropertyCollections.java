package com.cs.core.bgprocess.services.dataintegration;

import com.cs.config.dto.ConfigPropertyCollectionDTO;
import com.cs.config.idto.IConfigPropertyCollectionDTO;
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
public class ImportPropertyCollections implements IImportEntity {

  private static final String UPSERT_PROPERTY_COLLECTIONS     = "UpsertPropertyCollections";

  //TODO: Remaining Update
  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> propertyCollectionBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_PROPERTY_COLLECTION);
    try(PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigPropertyCollectionDTO> pcDTOList = new ArrayList<>();

      for (Entry pcEntityMeta : propertyCollectionBlocks.entrySet()) {
        ConfigPropertyCollectionDTO pcDTO = new ConfigPropertyCollectionDTO();
        pcDTO.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) pcEntityMeta.getValue()));
        pcDTOList.add(pcDTO);
      }

      //Creating Relationships in ODB
      Map<String, Object> responseMap = configurationImport(
          importer.getImportDTO().getLocaleID(), pcDTOList.toArray(new IConfigPropertyCollectionDTO[0]));
      importer.logIds(responseMap);

    } catch (IOException e) {
      importer.incrementNumberOfException();
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }


  public Map<String,Object> configurationImport(String localeID,
      IConfigPropertyCollectionDTO... collections)
      throws CSInitializationException, CSFormatException {
    List<IConfigPropertyCollectionDTO> pcList =  Arrays.asList(collections);
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, pcList);

    return CSConfigServer.instance()
        .request(requestModel, UPSERT_PROPERTY_COLLECTIONS, localeID);
  }
}
