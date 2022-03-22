package com.cs.core.bgprocess.services.dataintegration;

import com.cs.config.dto.ConfigLanguageDTO;
import com.cs.config.idto.IConfigLanguageDTO;
import com.cs.core.bgprocess.dto.BGPLog;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class ImportLanguages extends PXONImporter implements IImportEntity{
  
  private static final String IMPORT_LANGUAGE     = "ImportLanguage";
  private static final Pattern languageCodePattern = Pattern.compile("[a-z][a-z]_[A-Z][A-Z]");
  
  private class LanguageNode
  {
    List<LanguageNode> children = null;
    IConfigLanguageDTO data = null;
    
    void processParentLanguageNode(String currentParentCode, Map<String, LanguageNode> superParents, Map<String, LanguageNode> languageListForOrdering) {
	  LanguageNode parentLanguageNode = languageListForOrdering.get(currentParentCode);
    
      if (parentLanguageNode == null) {
        parentLanguageNode = new LanguageNode();
        superParents.put(currentParentCode, parentLanguageNode);
        languageListForOrdering.put(currentParentCode, parentLanguageNode);
      }
    
      if (parentLanguageNode.children == null) {
        parentLanguageNode.children = new ArrayList<>();
      }
      parentLanguageNode.children.add(this);
    }
    
    void putDataIntoLanguageList(List<IConfigLanguageDTO> languageDTOList) {
      if (this.data != null) {
        languageDTOList.add(this.data);
        if (this.children != null) {
          for (LanguageNode childLanguageNode : this.children) {
            childLanguageNode.putDataIntoLanguageList(languageDTOList);
          }
        }
      }
    }
  }
  
  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> languageBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_LANGUAGE);
    BGPLog log = importer.getJobData().getLog();;
    try (PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigLanguageDTO> languageDTOList = new ArrayList<>();
      
      Map<String, LanguageNode> superParents = new HashMap<>();
      Map<String, LanguageNode> languageListForOrdering = new HashMap<>();
      String defaultLanguageCode = null;
      ConfigLanguageDTO defaultLanguageDTO = null;
      
      for (Entry languageBlock : languageBlocks.entrySet()) {
        try {
          ConfigLanguageDTO configLanguageDTO = new ConfigLanguageDTO();
          configLanguageDTO.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) languageBlock.getValue()));

          String currentLanguageCode = configLanguageDTO.getCode();
          if(!languageCodePattern.matcher(currentLanguageCode).matches()){
            throw new CSFormatException("Incorrect Language Code");
          }
          boolean isDefaultLanguage = configLanguageDTO.isDefaultLanguage();
          if (isDefaultLanguage) {
            defaultLanguageCode = currentLanguageCode;
            if (defaultLanguageDTO != null) {
              defaultLanguageDTO.setIsDefaultLanguage(false);
            }
            defaultLanguageDTO = configLanguageDTO;
          }

          String currentParentCode = configLanguageDTO.getParentCode();
          LanguageNode currentLanguageNode = languageListForOrdering.get(currentLanguageCode);

          if (currentLanguageNode == null) {
            currentLanguageNode = new LanguageNode();
            currentLanguageNode.data = configLanguageDTO;

            languageListForOrdering.put(currentLanguageCode, currentLanguageNode);

            if (currentParentCode.equals("-1")) {
              superParents.put(currentLanguageCode, currentLanguageNode);
            } else {
              currentLanguageNode.processParentLanguageNode(currentParentCode, superParents, languageListForOrdering);
            }

          }
          else {
            if (currentLanguageNode.data == null) {
              currentLanguageNode.data = configLanguageDTO;

              if (!currentParentCode.equals("-1")) {
                currentLanguageNode.processParentLanguageNode(currentParentCode, superParents, languageListForOrdering);
                superParents.remove(currentLanguageCode);
              }
            }
          }
          
          //Create Language into RDBMS
          ILanguageConfigDTO languageConfig = ConfigurationDAO.instance().getLanguageConfig(currentLanguageCode);
          if(languageConfig == null) {
            ConfigurationDAO.instance().createLanguageConfig(currentLanguageCode, currentParentCode);
          }
        }
        catch (Exception e) {
          importer.incrementNumberOfException();
          log.error(e.getMessage());
        }
      }
      
      if (defaultLanguageDTO != null) {
        defaultLanguageDTO.setIsUserInterfaceLanguage(true);
        defaultLanguageDTO.setIsDataLanguage(true);
      }
      
      for (LanguageNode superParentNode : superParents.values()) {
        superParentNode.putDataIntoLanguageList(languageDTOList);
      }
      
      Set<String> languageCodesSet = languageListForOrdering.keySet();
      List<String> languageCodesList = new ArrayList<>(languageCodesSet);
      
      // create or save language in ODB
      Map<String, Object> responseMap = configurationImport(importer.getImportDTO().getLocaleID(), languageDTOList, defaultLanguageCode, languageCodesList.toArray(new String[0]));
      importer.logIds(responseMap);
    } catch(Exception e) {
      importer.incrementNumberOfException();
      log.error(e.getMessage());
    }
  }
  
  public Map<String,Object> configurationImport(String localeID, List<IConfigLanguageDTO> languageList, String defaultLanguageCode, String... languageCodes) throws CSInitializationException, CSFormatException
  {
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, languageList);
    if (defaultLanguageCode != null) {
      requestModel.put("defaultLanguageCode", defaultLanguageCode);
      requestModel.put("languageCodesList", Arrays.asList(languageCodes));
    }
    return CSConfigServer.instance().request(requestModel, IMPORT_LANGUAGE, localeID);
  }
}