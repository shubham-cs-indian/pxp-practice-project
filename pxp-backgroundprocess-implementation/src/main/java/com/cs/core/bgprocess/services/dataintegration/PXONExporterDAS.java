package com.cs.core.bgprocess.services.dataintegration;

import com.cs.config.standard.IConfigMap;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.csexpress.CSEList;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.data.Text;
import com.cs.core.dataintegration.dto.PXONExportPlanDTO;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
@SuppressWarnings("unchecked")
public class PXONExporterDAS {
  
  private static final String PROPERTY_RECORDS   = PXONTag.record.toJSONArrayTag();
  private static final String HASH_CODE          = PXONTag.hash.toCSETag();
  private static final String NATURE             = PXONTag.nature.toReadOnlyCSETag();
  private static final String CONTEXTUAL_OBJECT  = PXONTag.cxtual.toJSONContentTag();
  private static final String PROPERT_RECORDS    = PXONTag.record.toJSONArrayTag();
  private static final String CHILDREN           = PXONTag.embd.toCSEListTag();
  private static final String OTHER_CLASSIFIERS  = PXONTag.classifier.toCSEListTag();
  private static final String ENTITY_EXTENSION   = PXONTag.ext.toJSONContentTag();
  private static final String CREATED            = PXONTag.created.toJSONContentTag();
  private static final String LAST_MODIFIED      = PXONTag.modified.toJSONContentTag();
  private static final String EMBEDDED_TYPE      = PXONTag.embdtype.toReadOnlyTag();
  private static final String PARENT             = PXONTag.parent.toReadOnlyCSETag();
  private static final String TOP_PARENT         = PXONTag.top.toReadOnlyCSETag();
  private static final String BASE_LOCALE        = PXONTag.baselocale.toReadOnlyTag();
  private static final String CHILD_LEVEL        = PXONTag.level.toPrivateTag();
  private static final String DEFAULT_IMAGE      = PXONTag.img.toCSETag();
  private static final String HTML               = PXONTag.html.toTag();
  private static final String NUMBER             = PXONTag.number.toTag();
  private static final String UNIT_SYMBOL        = PXONTag.unit.toTag();
  private static final String VALUE              = PXONTag.value.toTag();
  public static final String  RELATIONS          = PXONTag.link.toJSONArrayTag();
  public static final String  EXTENSION_CLASS    = PXONTag.extclass.toReadOnlyCSETag();
  public static final String  TAG_RECORD_IDS     = PXONTag.tag.toJSONArrayTag();
  private static final String COUPLING_TYPE      = PXONTag.cpl.toPrivateTag();
  private static final String RECORD_STATUS      = PXONTag.status.toPrivateTag();
  private static final String ORIGIN_ENTITY      = PXONTag.origin.toReadOnlyCSETag();
  private static final String SIDE_BASEENTITY    = PXONTag.entity.toReadOnlyCSETag();
  private static final String ENTITY_IID         = "baseEntityIID";
  private static final String CSID               = PXONTag.csid.toTag();
  private static final String WHEN               = PXONTag.when.toReadOnlyTag();
  private static final String WHO                = PXONTag.user.toReadOnlyTag();
  private static final String IS_EXPIRED         = PXONTag.isexpired.toJSONContentTag();
  private static final String GET_ALL_LOCALE_IDS = "select localeid from pxp.baseentitylocaleidlink where baseentityiid = ";
  public static final String GET_VARIANT_CONTEXT = "GetVariantContext";
  
  private Map<String, Map<String, Object>> variants            = new HashMap<>();
  
  public void  getAllBaseEntittiesByIID(List<Long> baseEntityIIDs,
      Map<Long, Map<String, Object>> exportBaseEntities, String localeID) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          RDBMSLogger.instance().info("BaseEntities export started for %S",baseEntityIIDs);
          IResultSetParser baseEntityResultSet = currentConn
              .getFunction( RDBMSAbstractFunction.ResultType.CURSOR, "pxp.fn_exportbaseentitypxon")
              .setInput(ParameterType.IID_ARRAY, baseEntityIIDs)
              .execute();
          while (baseEntityResultSet.next()) {
            prepareBaseEntity(exportBaseEntities, baseEntityResultSet, localeID);
          }
        });
  }
  
  private void prepareBaseEntity(Map<Long, Map<String, Object>> exportBaseEntities,
      IResultSetParser executeQuery, String localeID) throws SQLException, CSFormatException, RDBMSException
  {
    Map<String, Object> object = new HashMap<>();
    String csid = executeQuery.getString("csid");
    RDBMSLogger.instance().info("exported BaseEntity %S",csid);
    object.put(CSID, csid);
    setLocaleID(executeQuery, localeID, object); 
    object.put(NATURE, executeQuery.getString("Nature"));
    object.put(CHILD_LEVEL, executeQuery.getInt("childlevel"));
    object.put(EMBEDDED_TYPE,
        EmbeddedType.valueOf(executeQuery.getInt("embeddedtype"))
            .name());
    String img = executeQuery.getString("img");
    if (StringUtils.isNotEmpty(img)) 
      object.put(DEFAULT_IMAGE, img);
    
    String hash = executeQuery.getString("hashCode");
    if(StringUtils.isNotEmpty(hash)) {
      object.put(HASH_CODE, hash);
    }
    
    String imageExtension = executeQuery.getString("entityExtension");
    JSONContent imageExtensionJSON = new JSONContent(imageExtension);
    if(StringUtils.isNotEmpty(imageExtension))
      object.put(ENTITY_EXTENSION, imageExtensionJSON);
    
      
    object.put(PROPERT_RECORDS, new ArrayList<Map<String, Object>>());
    object.put(CHILDREN, "");
    object.put(OTHER_CLASSIFIERS, "");
    
    String parent = executeQuery.getString("parent");
    String top = executeQuery.getString("top");
    if (StringUtils.isNotEmpty(parent) && StringUtils.isNotEmpty(top)) {
      object.put(TOP_PARENT, top);
      object.put(PARENT, parent);
    }
    
    String origin = executeQuery.getString("origin");
    if (StringUtils.isNotEmpty(origin))
      object.put(ORIGIN_ENTITY, origin);
    
    String cxt = executeQuery.getString("cxt");
    
    if (StringUtils.isNotEmpty(cxt)) {
      String cxtObj = prepareVariants(localeID, cxt);
      object.put(CONTEXTUAL_OBJECT, cxtObj);
    }
    
    Map<String, Object> created = new HashMap<>();
    created.put(WHO, executeQuery.getString("createdusername"));
    created.put(WHEN, executeQuery.getLong("createtime"));
    object.put(CREATED, created);
    
    Map<String, Object> modified = new HashMap<>();
    modified.put(WHO, executeQuery.getString("lastusername"));
    modified.put(WHEN, executeQuery.getLong("lastModifiedTime"));
    object.put(LAST_MODIFIED, modified);
    
    object.put(IS_EXPIRED, executeQuery.getBoolean("isExpired"));
    long entityIID = executeQuery.getLong(ENTITY_IID);
    exportBaseEntities.put(entityIID, object);
  }

  private String prepareVariants(String localeID, String cxt) throws CSFormatException
  {
    ICSEElement cxtCSE = (new CSEParser()).parseDefinition(cxt);
    CSEObject cxtObj = (CSEObject) cxtCSE;
    String cxtCode = cxtObj.getCode();
    getVariant(localeID, cxtCode, variants, GET_VARIANT_CONTEXT);
    Map<String, Object> context = variants.get(cxtCode);
    Boolean isTimeEnabled = (Boolean) context.get(PXONTag.isTimeEnabled.toString());
    if(!isTimeEnabled) {
      String defaultTime = String.valueOf(0l);
      cxtObj.setSpecification(Keyword.$start, defaultTime);
      cxtObj.setSpecification(Keyword.$end, defaultTime);
    }
    return cxtObj.toString();
  }

  private void getVariant(String localeID, String code, Map<String, Map<String, Object>> config, String klassCode)
  {
    try {
      Map<String, Object> configDetails = config.get(code);
      if (configDetails == null) {
        JSONObject requestModel = new JSONObject();
        requestModel.put(PXONTag.id.toString(), code);
        Map<String, Object> variant = CSConfigServer.instance().request(requestModel, klassCode, localeID);
        config.put(code, variant);
      }
    }
    catch (CSFormatException | CSInitializationException e) {
      RDBMSLogger.instance().exception(e);
    }
  }

  /**
   * Check whether the entity present into current exported language else set creation language
   * @param executeQuery
   * @param localeID translation localeId
   * @param object
   * @throws RDBMSException
   * @throws SQLException
   */
  private void setLocaleID(IResultSetParser executeQuery, String localeID, Map<String, Object> object) throws RDBMSException, SQLException
  {
    List<String> localeIds = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement query = currentConn.prepareStatement(GET_ALL_LOCALE_IDS + executeQuery.getLong(ENTITY_IID));
      IResultSetParser result = currentConn.getResultSetParser(query.executeQuery());
      while (result.next()) {
        localeIds.add(result.getString("localeid"));
      }
    });
    if (localeIds.contains(localeID)) {
      object.put(BASE_LOCALE, localeID);
    }
    else {
      object.put(BASE_LOCALE, executeQuery.getString("baselocale"));
    }
  }
  
  public Collection<Long> getAllEntitiesIID(PXONExportPlanDTO pxonExportPlanDTO) throws RDBMSException
  {
    Collection<Long> baseEntityIIDs = new TreeSet<>();
    List<String> selectedBaseTypes = (List<String>) pxonExportPlanDTO.getSearchCriteria().get("selectedBaseTypes");
    List<Integer> basetypes = new ArrayList<>();
    selectedBaseTypes.forEach(moduleId -> basetypes.add(IBaseEntityIDDTO.BaseType.valueOf(moduleId.toString()).ordinal()));
    String query = String.format("select baseEntityIID from pxp.baseEntity where catalogCode = ? and basetype in (%s) and "
        + "baselocaleid = ? and organizationcode = ? and childlevel = 1 and ismerged != true", Text.join(",", basetypes));
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement prepareStmt = currentConn.prepareStatement(query);
      prepareStmt.setString(1, pxonExportPlanDTO.getCatalogCode());
      prepareStmt.setString(2, pxonExportPlanDTO.getLocaleID());
      prepareStmt.setString(3, pxonExportPlanDTO.getOrganizationCode());
      ResultSet executeQuery = prepareStmt.executeQuery();
      while (executeQuery.next()) {
        baseEntityIIDs.add(executeQuery.getLong(ENTITY_IID));
      }
    });
    return baseEntityIIDs;
  }
  
  public void exportAllValueRecord(List<Long> entityIIDs, String localeID,
      Map<Long, Map<String, Object>> exportBaseEntities) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          RDBMSLogger.instance().info("ValueRecord exporting for entity %S",entityIIDs);
          IResultSetParser baseEntityResultSet =
              currentConn.getFunction( RDBMSAbstractFunction.ResultType.CURSOR, "pxp.fn_exportValueRecordPXON")
              .setInput(ParameterType.IID_ARRAY, entityIIDs)
              .setInput(ParameterType.STRING, localeID)
              .execute();
          while (baseEntityResultSet.next()) {
            Map<String, Object> object = new HashMap<>();
            String csid = baseEntityResultSet.getString("csid");
            RDBMSLogger.instance().info("exporting ValueRecord %S",csid);
            object.put(CSID, csid);
            checkNotNone(COUPLING_TYPE, baseEntityResultSet.getString("cpl"), object);
            checkNotNone(RECORD_STATUS, baseEntityResultSet.getString("status"), object);
            object.put(VALUE, baseEntityResultSet.getString("value"));
            long asNumber = baseEntityResultSet.getLong("asnumber");
            if(asNumber != 0)
              object.put(NUMBER, asNumber);
            String unitSymbol = baseEntityResultSet.getString("unit");
            if(StringUtils.isNotEmpty(unitSymbol))
              object.put(UNIT_SYMBOL, unitSymbol);
            String html = baseEntityResultSet.getString("ashtml");
            if(StringUtils.isNotEmpty(html))
              object.put(HTML, html);
            
            String cxt = baseEntityResultSet.getString("cxt");
            if (StringUtils.isNotEmpty(cxt)) {
              String cxtObj = prepareVariants(localeID, cxt);
              object.put(CONTEXTUAL_OBJECT, cxtObj);
            }
            List<Map<String, Object>> valueRecord = (List<Map<String, Object>>) exportBaseEntities
                .get(baseEntityResultSet.getLong("entityIID"))
                .get(PROPERTY_RECORDS);
            valueRecord.add(object);
          }
        });
  }
  
  private void checkNotNone(String key, String name, Map<String, Object> entityMap)
  {
    // Resolve To bypass data issue - PXPFDEV-21237 
    if (!name.contains("NONE") && !name.contains("$name"))
      entityMap.put(key, name);
  }
  
  public void exportAllTagRecord(List<Long> entityIIDs,
      Map<Long, Map<String, Object>> exportBaseEntities) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          RDBMSLogger.instance().info("TagRecord exporting for entities %S",entityIIDs);
          IResultSetParser baseEntityResultSet = 
              currentConn.getFunction( RDBMSAbstractFunction.ResultType.CURSOR,"pxp.fn_exportTagsRecordPXON")
              .setInput(ParameterType.IID_ARRAY, entityIIDs)
              .execute();
          while (baseEntityResultSet.next()) {
            Map<String, Object> object = new HashMap<>();
            String csid = baseEntityResultSet.getString("csid");
            RDBMSLogger.instance().info("exporting TagRecord %S",csid);
            object.put(CSID, csid);
            checkNotNone(COUPLING_TYPE, baseEntityResultSet.getString("cpl"), object);
            checkNotNone(RECORD_STATUS, baseEntityResultSet.getString("status"), object);
            String ltag = baseEntityResultSet.getString("ltag");
            StringBuilder listTag = new StringBuilder(ltag);
            listTag.insert(0, "[");
            listTag.insert(listTag.length(), "]");
            object.put(TAG_RECORD_IDS, listTag);
            List<Map<String, Object>> record = (List<Map<String, Object>>) exportBaseEntities
                .get(baseEntityResultSet.getLong("entityIID"))
                .get(PROPERTY_RECORDS);
            record.add(object);
          }
        });
  }
  
  public void exportAllRelationRecord(List<Long> entityIIDs,
      Map<Long, Map<String, Object>> exportBaseEntities)
      throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          RDBMSLogger.instance().info("RelationRecord exporting for entities %S",entityIIDs);
          IResultSetParser baseEntityResultSet = currentConn
              .getFunction( RDBMSAbstractFunction.ResultType.CURSOR, "pxp.fn_exportRelationSide1PXON")
              .setInput(ParameterType.IID_ARRAY, entityIIDs)
              .execute();
          // side1Record
          prepareRelationRecord(baseEntityResultSet, "side1EntityIID", exportBaseEntities);
          
          baseEntityResultSet = currentConn
              .getFunction( RDBMSAbstractFunction.ResultType.CURSOR, "pxp.fn_exportRelationSide2PXON")
              .setInput(ParameterType.IID_ARRAY, entityIIDs)
              .execute();
          // side2Record
          prepareRelationRecord(baseEntityResultSet, "side2EntityIID", exportBaseEntities);
        });
  }
  
  private void prepareRelationRecord(IResultSetParser baseEntityResultSet, String side1EntityIID,
      Map<Long, Map<String, Object>> exportBaseEntities)
      throws SQLException, RDBMSException, CSFormatException
  {
    Map<Long, Map<String, Map<String, Object>>> relationshipExport = new HashMap<>();
    while (baseEntityResultSet.next()) {
      long side1IID = baseEntityResultSet.getLong(side1EntityIID);
     Map<String, Map<String, Object>> relationRecord = relationshipExport.get(side1IID);
      if (relationRecord == null) {
        relationRecord = new HashMap<>();
        relationshipExport.put(side1IID, relationRecord);
        getRelationRecord(baseEntityResultSet, relationRecord);
      }
      else {
        getRelationRecord(baseEntityResultSet, relationRecord);
      }
    }
    Set<Long> keySet = relationshipExport.keySet();
    for (Long entityIID : keySet) {
      List<Map<String, Object>> record = (List<Map<String, Object>>) exportBaseEntities
          .get(entityIID)
          .get(PROPERTY_RECORDS);
      record.addAll(relationshipExport.get(entityIID)
          .values());
    }
  }
  
  private void getRelationRecord(IResultSetParser baseEntityResultSet,
      Map<String, Map<String, Object>> relationRecord) throws SQLException, CSFormatException
  {
    String csid = baseEntityResultSet.getString("csid");
    String entitylink = baseEntityResultSet.getString("entitylink");
    String cxt = baseEntityResultSet.getString("cxt");
    String cpl = baseEntityResultSet.getString("cpl");
    String status = baseEntityResultSet.getString("status");
    Map<String, Object> entityMap = relationRecord.get(csid);
    if (entityMap == null) {
      prepareRelationLink(csid, entitylink, cxt, relationRecord, cpl, status);
    }
    else {
      List<HashMap<String, Object>> link = (List<HashMap<String, Object>>) entityMap.get(RELATIONS);
      prepareRelationLink(entitylink, cxt, link);
    }
  }
  
  private void prepareRelationLink(String entitylink, String cxt, List<HashMap<String, Object>> link) throws CSFormatException
  {
    HashMap<String, Object> linkMap = new HashMap<>();
    linkMap.put(SIDE_BASEENTITY, entitylink);
    if (StringUtils.isNotEmpty(cxt)) {
      String cxtObj = prepareVariants("en_US", cxt);
      linkMap.put(CONTEXTUAL_OBJECT, cxtObj);
    }
    link.add(linkMap);
  }
  
  private void prepareRelationLink(String csid, String entitylink, String cxt,
        Map<String, Map<String, Object>> map, String cpl, String status) throws CSFormatException
  {
    Map<String, Object> entityMap;
    entityMap = new HashMap<>();
    map.put(csid, entityMap);
    entityMap.put(CSID, csid);
    checkNotNone(COUPLING_TYPE, cpl, entityMap);
    checkNotNone(RECORD_STATUS, status, entityMap);
    List<HashMap<String, Object>> link = new ArrayList<>();
    entityMap.put(RELATIONS, link);
    prepareRelationLink(entitylink, cxt, link);
  }
  
  public String getWhereQuery(String queryBaseEntityIIDsIn, String query)
  {
    return query.replace("?", queryBaseEntityIIDsIn);
  }
  
  public void getAllEmbdEntities(List<Long> entityIIDs,
      Map<Long, Map<String, Object>> exportBaseEntities, String localeID)
      throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          RDBMSLogger.instance().info("EmbdEntities exporting for entities %S",entityIIDs);
          IResultSetParser baseEntityResultSet = currentConn
              .getFunction( RDBMSAbstractFunction.ResultType.CURSOR, "pxp.fn_exportEmbdEntitiesPXON")
              .setInput(ParameterType.IID_ARRAY, entityIIDs)
              .execute();
          Set<Long> childEntityIID = new HashSet<>();
          while (baseEntityResultSet.next()) {
            childEntityIID.add(baseEntityResultSet.getLong("baseEntityIID"));
            prepareBaseEntity(exportBaseEntities, (IResultSetParser) baseEntityResultSet, localeID);
            String csid = baseEntityResultSet.getString("csid");
            ICSEElement parseEmb = (new CSEParser()).parseDefinition(csid);
            int embededType = baseEntityResultSet.getInt("embeddedtype");
            CSEObject emb = (CSEObject) parseEmb;
            emb.setSpecification(Keyword.$type, EmbeddedType.valueOf(embededType));
            long parentIID = baseEntityResultSet.getLong("parentIID");
            Map<String, Object> parentEntity = exportBaseEntities.get(parentIID);
            prepareCSEList(emb.toString(), parentEntity, CHILDREN);
          }
          if (!childEntityIID.isEmpty()) {
            List<Long> chilEntityIIDs = childEntityIID.stream().collect( Collectors.toCollection(ArrayList::new));
            RDBMSLogger.instance().info("exporting EmbdEntity %S", chilEntityIIDs);
            exportAllValueRecord(chilEntityIIDs, localeID, exportBaseEntities);
            exportAllTagRecord(chilEntityIIDs, exportBaseEntities);
            exportAllRelationRecord(chilEntityIIDs, exportBaseEntities);
            getAllEmbdEntities(chilEntityIIDs, exportBaseEntities, localeID);
            getOtherClassifier(chilEntityIIDs, exportBaseEntities);
          }
        });
  }
  
  private void prepareCSEList(String csid, Map<String, Object> baseEntity, String cseList)
      throws CSFormatException
  {
    new CSEParser().parseDefinition(csid);
    String children = (String) baseEntity.get(cseList);
    if (children.isEmpty()) {
      CSEList list = new CSEList();
      ICSEElement element = (new CSEParser())
          .parseDefinition(csid);
      list.getSubElements()
          .add(element);
      baseEntity.put(cseList, list.toString());
    }
    else {
      CSEList list = (CSEList) (new CSEParser())
          .parseDefinition(children);
      ICSEElement element = (new CSEParser())
          .parseDefinition(csid);
      list.getSubElements()
          .add(element);
      baseEntity.put(cseList, list.toString());
    }
  }
  
  public void getOtherClassifier(List<Long> entityIIDs,
      Map<Long, Map<String, Object>> exportBaseEntities) throws RDBMSException
  {
    
    RDBMSConnectionManager.instance()
        .runTransaction(( RDBMSConnection currentConn) -> {
           IResultSetParser baseEntityResultSet = currentConn
              .getFunction( RDBMSAbstractFunction.ResultType.CURSOR, "pxp.fn_exportOtherClassifierPXON")
              .setInput(ParameterType.IID_ARRAY, entityIIDs)
              .execute();
          while (baseEntityResultSet.next()) {
            String csid = baseEntityResultSet.getString(CSID);
            long baseEntityIID = baseEntityResultSet.getLong(ENTITY_IID);
            RDBMSLogger.instance()
                .info("OtherClassifier exporting for baseEntity %d for otherClass csid %S",
                    baseEntityIID, csid);
            Map<String, Object> baseEntity = exportBaseEntities.get(baseEntityIID);
            prepareCSEList(csid, baseEntity, OTHER_CLASSIFIERS);
          }
        });
  }

  public void getEmptyRecords(List<Long> iids, Map<Long, Map<String, Object>> exportBaseEntities, PXONExportPlanDTO pxonExportPlanDTO)
      throws CSFormatException, CSInitializationException, RDBMSException
  {
    for(Long iid : iids){

      Map<String, Object> exports = exportBaseEntities.get(iid);
      if (exports == null) {
        RDBMSLogger.instance().warn("Bookmark contains iid:" + iid + " which does not exist.");
        continue;
      }
      CSEParser cseParser = new CSEParser();
      List<Map<String, Object>> properties = (List<Map<String, Object>>) exports.get(PROPERTY_RECORDS);

      List<String> alreadyPresentProperties = new ArrayList<>();
      for (Map<String, Object> property : properties) {
        String csid = (String) property.get(CSID);
        CSEObject cseObject = (CSEObject) cseParser.parseDefinition(csid);
        String code = cseObject.getCode();
        alreadyPresentProperties.add(code);
      }

      JSONObject configResponse = getConfigDetailsForEmptyRecords(pxonExportPlanDTO, exports, cseParser);

      Map<String, Object> referencedElements = (Map<String, Object>) configResponse.get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);

      for (String referencedElementKey : referencedElements.keySet()) {
        Map<String, Object> referencedElement = (Map<String, Object>) referencedElements.get(referencedElementKey);
        String propertyId = (String) referencedElement.get(IReferencedSectionElementModel.PROPERTY_ID);
        String type = (String) referencedElement.get(IReferencedSectionElementModel.TYPE);

        if (alreadyPresentProperties.contains(propertyId)) {
          continue;
        }

        if (type.equals(CommonConstants.ATTRIBUTE)) {
          IValueRecordDTO valueRecord = new ValueRecordDTO.ValueRecordDTOBuilder(iid,
              ConfigurationDAO.instance().getPropertyByCode(propertyId), "").build();
          Map<String, Object> property = new HashMap<>();
          property.put(CSID, valueRecord.toCSExpressID().toString());
          properties.add(property);
        }
        else if (type.equals(CommonConstants.TAG)) {
          ITagsRecordDTO tags = new TagsRecordDTO.TagsRecordDTOBuilder(iid,
              ConfigurationDAO.instance().getPropertyByCode(propertyId)).build();
          Map<String, Object> property = new HashMap<>();
          property.put(CSID, tags.toCSExpressID().toString());
          properties.add(property);
        }
      }
    }
  }

  private JSONObject getConfigDetailsForEmptyRecords(PXONExportPlanDTO pxonExportPlanDTO, Map<String, Object> exports, CSEParser cseParser)
      throws CSFormatException, CSInitializationException
  {
    CSEObject cseObject = (CSEObject) cseParser.parseDefinition((String) exports.get(CSID));
    String baseType = cseObject.getSpecification(Keyword.$type);

    List<String> types = new ArrayList<>();
    List<String> taxonomies = new ArrayList<>();

    String natureClassPXON = (String) exports.get(NATURE);
    ICSEObject natureClass = (CSEObject) cseParser.parseDefinition(natureClassPXON);
    types.add(natureClass.getCode());

    String otherClassPXON = (String) exports.get(OTHER_CLASSIFIERS);
    if(StringUtils.isNotEmpty(otherClassPXON)) {
      CSEList otherClasses = (CSEList) cseParser.parseDefinition(otherClassPXON);
      for(ICSEElement element : otherClasses.getSubElements()){
        CSEObject object = (CSEObject)element;
        if(object.getSpecification(Keyword.$type).equals("CLASS")){
          types.add(object.getCode());
        }
        else{
          taxonomies.add(object.getCode());
        }
      }
    }

    IMulticlassificationRequestModel multiClassificationRequestModel = new MulticlassificationRequestModel();

    multiClassificationRequestModel.setPhysicalCatalogId(pxonExportPlanDTO.getCatalogCode());
    multiClassificationRequestModel.setOrganizationId(pxonExportPlanDTO.getOrganizationCode());
    multiClassificationRequestModel
        .setLanguageCodes(Arrays.asList(pxonExportPlanDTO.getLocaleID()));
    multiClassificationRequestModel.setUserId("admin");
    multiClassificationRequestModel.getKlassIds().addAll(types);
    multiClassificationRequestModel.getSelectedTaxonomyIds().addAll(taxonomies);
    IBaseEntityIDDTO.BaseType baseTypeByName = IBaseEntityIDDTO.BaseType.getBaseTypeByName(baseType);
    multiClassificationRequestModel.setBaseType(IConfigMap.getBaseTypeClassPath(baseTypeByName));
    multiClassificationRequestModel.setShouldUseTagIdTagValueIdsMap(false);

    return CSConfigServer.instance()
        .request(ObjectMapperUtil.convertValue(multiClassificationRequestModel, Map.class), "GetConfigDetailsWithoutPermissions",
            pxonExportPlanDTO.getLocaleID());
  }
}
