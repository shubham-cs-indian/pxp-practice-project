package com.cs.core.bgprocess.services.dataintegration;

import com.cs.config.dto.ConfigRelationshipDTO;
import com.cs.config.idto.IConfigRelationshipDTO;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/** @author vallee */
public class ImportRelationships implements IImportEntity {

  private static final String UPSERT_RELATIONSHIPS            = "UpsertRelationships";

  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> relationshipBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_RELATIONSHIP);
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    try(PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigRelationshipDTO> relationshipDTOList = new ArrayList<>();

      for (Entry relationshipEntityMeta : relationshipBlocks.entrySet()) {
        ConfigRelationshipDTO relationshipDTO = new ConfigRelationshipDTO();
        relationshipDTO.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) relationshipEntityMeta.getValue()));

        IPropertyDTO relationshipProperty = configurationDAO.createProperty(relationshipDTO.getCode(),
            IPropertyDTO.PropertyType.RELATIONSHIP);
        relationshipDTO.setPropertyIID(relationshipProperty.getIID());

        relationshipDTOList.add(relationshipDTO);
      }

      //Creating Tags in ODB
      Map<String, Object> responseMap = configurationImport(
          importer.getImportDTO().getLocaleID(), relationshipDTOList.toArray(new IConfigRelationshipDTO[0]));
      importer.logIds(responseMap);
    } catch (IOException e) {
      importer.incrementNumberOfException();
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }


  public Map<String,Object> configurationImport(String localeID, IConfigRelationshipDTO... relationships)
      throws CSInitializationException, CSFormatException {
    List<IConfigRelationshipDTO> relationshipList =  Arrays.asList(relationships);
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, relationshipList);

    return CSConfigServer.instance()
        .request(requestModel, UPSERT_RELATIONSHIPS, localeID);
  }
}
