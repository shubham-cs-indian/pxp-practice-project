package com.cs.core.bgprocess.services.dataintegration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import com.cs.config.dto.ConfigTagDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.idto.IConfigTagDTO;
import com.cs.config.idto.IConfigTagValueDTO;
import com.cs.constants.SystemLevelIds;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/** @author vallee */
public class ImportTags implements IImportEntity {

  private static final String UPSERT_TAGS                     = "UpsertTags";
  private static final String UNIT_TAG                                       = "tag_type_unit";

  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> tagBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_TAG);
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    try(PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigTagDTO> tagDTOList = new ArrayList<>();

      for (Entry tagEntityMeta : tagBlocks.entrySet()) {
        ConfigTagDTO tagDTO = new ConfigTagDTO();
        tagDTO.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) tagEntityMeta.getValue()));
        if(tagDTO.getTagType().equals(UNIT_TAG)){
          continue;
        }
        //Creating tag in RDBMS
        IPropertyDTO.PropertyType propertyType = PropertyType.valueOf(tagDTO.getType());
        String code = tagDTO.getCode();
        IPropertyDTO propertyDTO = configurationDAO.createProperty(code, propertyType);
        long propertyIID = propertyDTO.getPropertyIID();
        tagDTO.setPropertyIID(propertyIID);
        
        List<IConfigTagValueDTO> children = tagDTO.getChildren();
        for (IConfigTagValueDTO tagValueDTO : children) {
          ITagValueDTO iTagValueDTO = configurationDAO.createTagValue(tagValueDTO.getCode(), propertyIID);
          tagValueDTO.setPropertyIID(iTagValueDTO.getPropertyIID());
        }
        tagDTOList.add(tagDTO);
      }
      //Creating Tags in ODB
      Map<String, Object> responseMap = configurationImport(
          importer.getImportDTO().getLocaleID(), tagDTOList.toArray(new IConfigTagDTO[0]));
      List<Map<String, Object>> booleanTags = (List<Map<String, Object>>) responseMap.get(SystemLevelIds.BOOLEAN_TAG_TYPE_ID);
      for (Map<String, Object> booleanTag : booleanTags) {
        List<Map<String, Object>> childern = (List<Map<String, Object>>) booleanTag.get(ConfigTag.children.toString());
        for (Map<String, Object> childTag : childern) {
          RDBMSUtils.createTagValue((String) childTag.get(ConfigTag.code.toString()), (String) booleanTag.get(ConfigTag.code.toString()));
        }
      }
      importer.logIds(responseMap);
    } catch (Exception e) {
      importer.incrementNumberOfException();
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }

  public Map<String,Object> configurationImport(String localeID, IConfigTagDTO... tags)
      throws CSInitializationException, CSFormatException {
    List<IConfigTagDTO> tagList =  Arrays.asList(tags);
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, tagList);

    return CSConfigServer.instance()
        .request(requestModel, UPSERT_TAGS, localeID);
  }
}
