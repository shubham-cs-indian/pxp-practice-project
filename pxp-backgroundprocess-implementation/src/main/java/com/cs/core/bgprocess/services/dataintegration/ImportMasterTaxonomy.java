package com.cs.core.bgprocess.services.dataintegration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import com.cs.config.dto.ConfigClassifierDTO;
import com.cs.config.idto.IConfigClassifierDTO;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.config.strategy.usecase.klass.IGetTaxonomyHierarchyIdsStrategy;
import com.cs.core.config.strategy.usecase.taxonomy.GetTaxonomyHierarchyIdsStrategy;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIDsListModel;
import com.cs.core.runtime.interactor.model.configuration.IIIDsListModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.usecase.defaultinstance.GetConfigDetailsForDefaultInstanceStrategy;
import com.cs.core.runtime.strategy.usecase.defaultinstance.IGetConfigDetailsForDefaultInstanceStrategy;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;

/** @author vallee */
@SuppressWarnings("unchecked")
public class ImportMasterTaxonomy extends AbstractHierarchyTree implements IImportEntity {

  private static final String                           UPSERT_MASTER_TAXONOMY = "UpsertMasterTaxonomy";
  static               IGetTaxonomyHierarchyIdsStrategy              getHierarchy    = BGProcessApplication.getApplicationContext().getBean(
      GetTaxonomyHierarchyIdsStrategy.class);
  
  static ConfigUtil configUtil = BGProcessApplication.getApplicationContext().getBean(ConfigUtil.class);

  static IGetConfigDetailsForDefaultInstanceStrategy getConfigDetailsForTaxonomyDefaultInstanceStrategy = BGProcessApplication.getApplicationContext().getBean(
      GetConfigDetailsForDefaultInstanceStrategy.class);
  
  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> taxonomyBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_MASTER_TAXONOMY);
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    try(PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigClassifierDTO> taxonomyDTOList = new ArrayList<>();
      Node<String> root = new Node<>(ROOT);
      Map<String, ConfigClassifierDTO> taxonomies = new HashMap<>();
      
      List<String> types = new ArrayList<String>();
      
      IMulticlassificationRequestModel multiclassificationRequestModel = configUtil.getConfigRequestModelForGivenTypesTaxonomies(types,
          new ArrayList<String>());
      multiclassificationRequestModel.setUserId(CommonConstants.ADMIN_USER_ID);
      multiclassificationRequestModel.setOrganizationId(IStandardConfig.STANDARD_ORGANIZATION_CODE);
      IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsForTaxonomyDefaultInstanceStrategy
          .execute(multiclassificationRequestModel);
      
      for (Entry taxonomyBlock : taxonomyBlocks.entrySet()) {
        ConfigClassifierDTO configTaxonomyDTO = new ConfigClassifierDTO();
        
        configTaxonomyDTO.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) taxonomyBlock.getValue()));
        types.add(configTaxonomyDTO.getClassifierDTO().getCode());

        // Upsert one by one into RDBMS
        IClassifierDTO classifier = configTaxonomyDTO.getClassifierDTO();
        IClassifierDTO taxonomyDTO = configurationDAO.createClassifier(
            classifier.getClassifierCode(), classifier.getClassifierType());
        configTaxonomyDTO.setClassifierIID(taxonomyDTO.getIID());
        List<String> levelCodes = configTaxonomyDTO.getLevelCodes();
        List<String> levelCodeWithIID = new ArrayList<>();
        for(String levelCode : levelCodes) {
          IPropertyDTO tagDTO = configurationDAO.createProperty(levelCode, PropertyType.TAG);
          String codeWithIID = levelCode + ":" + tagDTO.getPropertyIID();
          levelCodeWithIID.add(codeWithIID);
        }
        configTaxonomyDTO.getLevelCodes().clear();
        configTaxonomyDTO.getLevelCodes().addAll(levelCodeWithIID);
        taxonomies.put(classifier.getClassifierCode(), configTaxonomyDTO);
        prepareHierarchyTree(root, configTaxonomyDTO, taxonomyDTO);
      
      }
      prepareList(root, taxonomyDTOList, taxonomies);
      // Upserting classifiers in ODB
      Map<String, Object> responseMap = configurationImport( importer.getImportDTO().getLocaleID(), taxonomyDTOList.toArray(new IConfigClassifierDTO[0]));
      updateHierarchyIIDs(taxonomyDTOList);
      importer.logIds(responseMap);
      
      
      for (String type : types) {
        LocaleCatalogDAO localeCatlogDAO = (LocaleCatalogDAO) RDBMSUtils.getDefaultLocaleCatalogDAO();
        IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByCode(type);
        IBaseEntityDTO baseEntityDTO = localeCatlogDAO.getEntityByID(type);
        if (baseEntityDTO == null)
          baseEntityDTO = localeCatlogDAO.newBaseEntityDTOBuilder(type, BaseType.UNDEFINED, classifierDTO).build();
        IBaseEntityDAO baseEntityDAO = localeCatlogDAO.openBaseEntity(baseEntityDTO);
        IPropertyRecordDTO[] propertyRecords = createPropertyRecordInstance(baseEntityDAO, localeCatlogDAO, configDetails);
        try {
          baseEntityDAO.createPropertyRecords(propertyRecords);
        }
        catch (Exception e) {
          //In case taxonomy already exists
          RDBMSLogger.instance().exception(e);
        }
      }
    }
    catch (Exception e) {
      importer.incrementNumberOfException();
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }

  private void updateHierarchyIIDs(List<IConfigClassifierDTO> taxonomyDTOList) throws Exception
  {
    for(IConfigClassifierDTO taxonomy : taxonomyDTOList){
      String parentCode = taxonomy.getParentCode();
      IIIDsListModel execute;
      if (!parentCode.equals("-1")) {
        execute = getHierarchy.execute(new IdLabelCodeModel(parentCode));
      }
      else {
        execute = new IIDsListModel();
        ArrayList<Long> iids = new ArrayList<>();
        iids.add(-1L);
        execute.setIids(iids);
      }
      execute.getIids().add(taxonomy.getClassifierDTO().getClassifierIID());
      ConfigurationDAO.instance().updateHierarchyIIDs(taxonomy.getClassifierDTO().getClassifierIID(), execute.getIids());
    }
  }

  public Map<String,Object> configurationImport(String localeID, IConfigClassifierDTO... taxonomies)
      throws CSInitializationException, CSFormatException {
    List<IConfigClassifierDTO> taxonomiesList =  Arrays.asList(taxonomies);
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, taxonomiesList);

    return CSConfigServer.instance().request(requestModel, UPSERT_MASTER_TAXONOMY, localeID);
  }
  
  protected IPropertyRecordDTO[] createPropertyRecordInstance(IBaseEntityDAO baseEntityDAO,ILocaleCatalogDAO localeCatalogDAO,
      IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO, configDetails,
        PropertyRecordType.DEFAULT_COUPLED, localeCatalogDAO);
    // Create Attribute
    List<IPropertyRecordDTO> propertyRecords = this.createAttributePropertyRecordInstance(propertyRecordBuilder, configDetails);
    // Create tag
    propertyRecords.addAll(this.createTagPropertyRecordInstance(propertyRecordBuilder, configDetails));
    return propertyRecords.toArray(new IPropertyRecordDTO[propertyRecords.size()]);
  }
  
  protected List<IPropertyRecordDTO> createAttributePropertyRecordInstance(PropertyRecordBuilder propertyRecordBuilder,
      IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    List<IPropertyRecordDTO> attributePropertyRecords = new ArrayList<>();
    configDetails.getReferencedAttributes().values().forEach(referencedAttribute -> {
      try {
        IPropertyRecordDTO dto = propertyRecordBuilder.createValueRecord(referencedAttribute);
        if (dto != null) {
          attributePropertyRecords.add(dto);
        }
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    return attributePropertyRecords;
  }
  
  protected List<IPropertyRecordDTO> createTagPropertyRecordInstance(PropertyRecordBuilder propertyRecordBuilder,
      IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    List<IPropertyRecordDTO> tagPropertyRecords = new ArrayList<>();
    configDetails.getReferencedTags().values().forEach(referencedTag -> {
      try {
        IPropertyRecordDTO dto = propertyRecordBuilder.createTagsRecordForDefaultInstance(referencedTag);
        if (dto != null) {
          tagPropertyRecords.add(dto);
        }
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    return tagPropertyRecords;
  }
  
}
