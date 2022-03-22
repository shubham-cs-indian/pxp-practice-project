package com.cs.core.bgprocess.services.dataintegration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import com.cs.config.dto.ConfigClassifierDTO;
import com.cs.config.dto.ConfigNatureRelationshipDTO;
import com.cs.config.idto.IConfigClassifierDTO;
import com.cs.config.idto.IConfigNatureRelationshipDTO;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/** @author vallee */
public class ImportClassifier extends AbstractHierarchyTree implements IImportEntity {

  private static final String UPSERT_CLASSES = "UpsertClasses";

  
  private static final List<String> natureRelationshipClass      = Arrays.asList(Constants.FIXED_BUNDLE, Constants.SET_OF_PRODUCTS);
  private static final String       UNIT                         = "unit";
  private static final String       PROMOTIONAL_COLLECTION       = "promotionalCollection";
  private static final String       THRESHOLD_BUNDLE             = "looseBundle";
  

  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> classBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_CLASSIFIER);
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    try(PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigClassifierDTO> classDTOList = new ArrayList<>();
      Node<String> root = new Node<>(ROOT);
      Map<String, ConfigClassifierDTO> classes = new HashMap<>();
      for (Entry classBlock : classBlocks.entrySet()) {
        ConfigClassifierDTO configClassDTO = new ConfigClassifierDTO();
        try {
          configClassDTO.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) classBlock.getValue()));
          String natureType = configClassDTO.getNatureType();
          if (natureType != null  && (natureType.equals(UNIT) || natureType.equals(PROMOTIONAL_COLLECTION) || natureType.equals(THRESHOLD_BUNDLE))) {
            continue;
          }
           // Upsert one by one into RDBMS
          IClassifierDTO classifier = configClassDTO.getClassifierDTO();         
          IClassifierDTO classifierDTO = configurationDAO.createClassifier(
              classifier.getCode(), classifier.getClassifierType());
          configClassDTO.setClassifierIID(classifierDTO.getIID());
          List<String> relationshipType = new ArrayList<>();
          setRelationshipIID(configurationDAO, configClassDTO, relationshipType);
          prepaeNatureRelationship(configurationDAO, configClassDTO, classifierDTO, relationshipType);
          configClassDTO.toJSONBuffer();
          classes.put(classifier.getCode(), configClassDTO);
          prepareHierarchyTree(root, configClassDTO, classifierDTO);
        }catch (Exception e) {
          RDBMSLogger.instance().exception(e);
          importer.incrementNumberOfException();
        }
        
      }
      prepareList(root, classDTOList, classes);
      
      // Upserting classifiers in ODB
      Map<String, Object> responseMap = configurationImport( importer.getImportDTO().getLocaleID(), classDTOList.toArray(new IConfigClassifierDTO[0]));
      importer.logIds(responseMap);
    } catch (IOException e) {
      importer.incrementNumberOfException();
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }

  private void prepaeNatureRelationship(ConfigurationDAO configurationDAO, ConfigClassifierDTO configClassDTO, IClassifierDTO classifierDTO,
      List<String> relationshipType) throws RDBMSException, CSFormatException
  {
    String natureType = configClassDTO.getNatureType();
    if(StringUtils.isNotEmpty(natureType) && natureRelationshipClass.contains(natureType)) {
      String natureRelationshipType = getNatureRelationshipType(natureType);
      if(StringUtils.isNotEmpty(natureRelationshipType) && !relationshipType.contains(natureRelationshipType)) {
        IConfigNatureRelationshipDTO relationship = new ConfigNatureRelationshipDTO();
        String newUniqueID = RDBMSAppDriverManager.getDriver()
            .newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP_SIDE.getPrefix());
        IPropertyDTO createProperty = configurationDAO.createProperty(newUniqueID, PropertyType.NATURE_RELATIONSHIP);
        relationship.setPropertyDTO(createProperty.getCode(), createProperty.getPropertyType());
        relationship.setPropertyIID(createProperty.getPropertyIID());
        relationship.setRelationshipType(natureRelationshipType);
        relationship.setIsNature(true);
        relationship.setLabel(createProperty.getCode());
        relationship.getSide1().setCode(classifierDTO.getCode());
        relationship.getSide1().setCouplings(new ArrayList<>());
        relationship.getSide2().setCode(classifierDTO.getCode());
        relationship.getSide2().setCouplings(new ArrayList<>());
        configClassDTO.getRelationships().add(relationship);
      }
    }
  }

  private void setRelationshipIID(ConfigurationDAO configurationDAO, ConfigClassifierDTO configClassDTO, List<String> relationshipType) throws RDBMSException, CSFormatException
  {
    Collection<IConfigNatureRelationshipDTO> relationships = configClassDTO.getRelationships();
    for(IConfigNatureRelationshipDTO relationship : relationships) {
      IPropertyDTO propertyDTO = relationship.getPropertyDTO();
      IPropertyDTO relationshipProperty = configurationDAO.createProperty(propertyDTO.getCode(), propertyDTO.getPropertyType());
      relationship.setPropertyIID(relationshipProperty.getPropertyIID());
      relationshipType.add(relationship.getRelationshipType());
    }
  }
  
  public Map<String,Object> configurationImport(String localeID, IConfigClassifierDTO... classifiers)
      throws CSInitializationException, CSFormatException {
    List<IConfigClassifierDTO> classifierList =  Arrays.asList(classifiers);
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, classifierList);

    return CSConfigServer.instance()
        .request(requestModel, UPSERT_CLASSES, localeID);
  }
  
  private String getNatureRelationshipType(String natureType)
  {
    switch (natureType) {
      case Constants.FIXED_BUNDLE:
        return CommonConstants.FIXED_BUNDLE;
      case Constants.SET_OF_PRODUCTS:
        return CommonConstants.SET_OF_PRODUCTS;
    }
    return null;
  }
}
