package com.cs.core.bgprocess.services.propertycollection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.ListUtils;
import org.json.simple.JSONObject;

import com.cs.core.bgprocess.dto.PropagatePCOnPropertyDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IPropagatePCOnPropertyDeleteDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.datarule.DataRulesHelperModel;
import com.cs.core.config.interactor.model.datarule.IDataRulesHelperModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForSaveConcatenatedAttributeModel;
import com.cs.core.config.strategy.usecase.concatenatedAttribute.IGetConfigDetailsForSaveConcatenatedAttributeStrategy;
import com.cs.core.data.Text;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class PropagateOnModificationPC extends AbstractBGProcessJob implements IBGProcessJob {
  
  private int                     currentBatchNo                 = 0;
  private int                     nbBatches                      = 1;
  IPropagatePCOnPropertyDeleteDTO propagatePCOnPropertyDeleteDTO = new PropagatePCOnPropertyDeleteDTO();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    
    propagatePCOnPropertyDeleteDTO.fromJSON(jobData.getEntryData().toString());
    
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
  @Override
  public BGPStatus runBatch() throws Exception
  {
    this.propagateOnModificationOfPC();
    setCurrentBatchNo(++currentBatchNo);
    
    IBGProcessDTO.BGPStatus status = null;
    
    jobData.getProgress().setPercentageCompletion(currentBatchNo * 100 / nbBatches);
    
    if (jobData.getProgress().getPercentageCompletion() == 100) {
      status = IBGProcessDTO.BGPStatus.ENDED_SUCCESS;
    }
    else {
      status = IBGProcessDTO.BGPStatus.ENDED_ERRORS;
    }
    return status;
  }
  
  protected void propagateOnModificationOfPC()
      throws Exception
  {
    //// Handling when Property is been removed from PC
    List<Long> deletedPropertyIIDs = getpropertyIdsByPropertyCode(
        propagatePCOnPropertyDeleteDTO.getDeletedPropertyCodes());
    List<Long> classifierIIds = getclassifierIIDByclassifierCode(
        propagatePCOnPropertyDeleteDTO.getClassifierCodes());
    RuleHandler ruleHandler = new RuleHandler();
    Set<Long> baseentityIids = getBaseEntityByClassifierIID(classifierIIds);
    
    if (baseentityIids.isEmpty()) {
      return;
    }
    
    if (!deletedPropertyIIDs.isEmpty()) {
      deleteRuleViolation(deletedPropertyIIDs, baseentityIids);
      deleteUniquenessViolation(deletedPropertyIIDs, classifierIIds);
      deleteContexualObject(deletedPropertyIIDs, baseentityIids);
      
      for (Long baseentiyiid : baseentityIids) {
        reEvaluateMandatoryViolations(baseentiyiid, ruleHandler);
      }
    }
    
    //// Handling when Property is been added to PC
    handlingOfPropertyAdditionInPC(baseentityIids);
  }
  
  /*private static final String DELETE_TAGS_INSTANCE = " delete from pxp.tagsrecord where entityiid in ( %s ) and propertyiid in ( %s )";
  
  private void deletepropertyFromTagsRecord(List<Long> propertyIIDs, Set<Long> baseentityIids) throws RDBMSException, SQLException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(DELETE_TAGS_INSTANCE, Text.join(",", baseentityIids), Text.join(",", propertyIIDs));
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.execute();
    });
  }
  
  private static final String DELETE_VALUE_INSTANCE = " delete from pxp.valuerecord where entityiid in ( %s ) and propertyiid in ( %s )";
  
  private void deletePropertyFromValueRecord(List<Long> propertyIIDs, Set<Long> baseentityIids) throws RDBMSException, SQLException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(DELETE_VALUE_INSTANCE, Text.join(",", baseentityIids), Text.join(",", propertyIIDs));
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.execute();
    });
  }*/
  
  private void reEvaluateMandatoryViolations(Long baseentiyiid, RuleHandler ruleHandler)
      throws Exception
  {
    ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, baseentiyiid);
    
    IBaseEntityDTO entity = catalogDao.getEntityByIID(baseentiyiid);
    BaseEntityDAO entityDao = (BaseEntityDAO) catalogDao.openBaseEntity(entity);
    
    List<String> baseEntityClassifiers = new ArrayList<>();
    List<String> baseEntityTaxonomies = new ArrayList<>();
    List<String> removedClassifiers = new ArrayList<>();
    
    ruleHandler.getBaseEntityClassifiersAndTaxonomies(catalogDao, entityDao, baseEntityClassifiers, baseEntityTaxonomies);
    
    Map<String, Object> requestModel = new HashMap<>();
    requestModel.put("classifiers", ListUtils.sum(baseEntityClassifiers, baseEntityTaxonomies));
    requestModel.put("removedClassifiers", removedClassifiers);
    
    JSONObject detailsFromODB = CSConfigServer.instance().request(requestModel, "GetProductIdentifiersForClassifiers",
        catalogDao.getLocaleCatalogDTO().getLocaleID());
    
    IDataRulesHelperModel dataRules = ObjectMapperUtil.readValue(ObjectMapperUtil.writeValueAsString(detailsFromODB), DataRulesHelperModel.class);   
    
    ruleHandler.evaluateDQMustShould(baseentiyiid, dataRules, entityDao, catalogDao);
  }
  
  private static final String GET_CONTEXUAL_OBJECT = " select contextualobjectiid from pxp.valuerecord where entityiid in ( %s ) and propertyiid in ( %s )";
  private static final String DELETE_CONTEXT       = " delete from contextualobject where contextualobjectiid in ( %s )";
  
  private void deleteContexualObject(List<Long> propertyIIDs, Set<Long> baseentityIids) throws RDBMSException, SQLException
  {
    List<Long> contexualObjectIIds = new ArrayList<Long>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(GET_CONTEXUAL_OBJECT, Text.join(",", baseentityIids), Text.join(",", propertyIIDs));
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.execute();
      IResultSetParser ruleResult = currentConn.getResultSetParser(stmt.getResultSet());
      while (ruleResult.next()) {
        contexualObjectIIds.add(ruleResult.getLong("contextualobjectiid"));
      }
      if (!contexualObjectIIds.isEmpty()) {
        String query1 = String.format(DELETE_CONTEXT, Text.join(",", contexualObjectIIds));
        PreparedStatement stmt1 = currentConn.prepareStatement(query1.toString());
        stmt1.execute();
      }
    });
  }
  
  private static final String GET_ENTITY = " select baseentityiid from pxp.baseentity where catalogcode != 'diff-pim' and classifieriid in( %s ) union select baseentityiid from pxp.baseentityclassifierlink where otherclassifieriid in( %s )";
  
  private Set<Long> getBaseEntityByClassifierIID(List<Long> classifierIIds) throws SQLException, RDBMSException
  {
    Set<Long> baseEntityIIds = new TreeSet<Long>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(GET_ENTITY, Text.join(",", classifierIIds), Text.join(",", classifierIIds));
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.execute();
      IResultSetParser ruleResult = currentConn.getResultSetParser(stmt.getResultSet());
      while (ruleResult.next()) {
        baseEntityIIds.add(ruleResult.getLong("baseentityiid"));
      }
    });
    return baseEntityIIds;
  }
  
  private static final String DELETE_VIOLATION = " delete from pxp.baseentityqualityrulelink where ruleexpressioniid = -1 and baseentityiid in ( %s ) and propertyiid in ( %s )";
  
  private void deleteRuleViolation(List<Long> propertyIIDs, Set<Long> baseentityIids) throws RDBMSException, SQLException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(DELETE_VIOLATION, Text.join(",", baseentityIids), Text.join(",", propertyIIDs));
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.execute();
    });
  }
  
  private static final String DELETE_UNIQUENESS_VIOLATION = " delete from pxp.uniquenessviolation where propertyiid in ( %s ) and classifieriid in ( %s )";
  
  private void deleteUniquenessViolation(List<Long> propertyIIDs, List<Long> classifierIIds) throws RDBMSException, SQLException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(DELETE_UNIQUENESS_VIOLATION, Text.join(",", propertyIIDs), Text.join(",", classifierIIds));
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.execute();
    });
  }
  
  private static final String PROPERTY_IIDS = " select propertyiid from pxp.propertyconfig where propertycode in ( %s )";
  
  public List<Long> getpropertyIdsByPropertyCode(List<String> propertyCodes) throws SQLException, RDBMSException
  {
    List<Long> propertyIIDs = new ArrayList<Long>();
    if (propertyCodes.isEmpty()) {
      return propertyIIDs;
    }
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(PROPERTY_IIDS, Text.join(",", propertyCodes, "'%s'"));
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.execute();
      IResultSetParser ruleResult = currentConn.getResultSetParser(stmt.getResultSet());
      while (ruleResult.next()) {
        propertyIIDs.add(ruleResult.getLong("propertyiid"));
      }
    });
    return propertyIIDs;
    
  }
  
  private static final String CLASSIFIER_IIDS = " select classifieriid from pxp.classifierconfig where classifiercode in ( %s )";
  
  public List<Long> getclassifierIIDByclassifierCode(Set<String> classifierCodes) throws SQLException, RDBMSException
  {
    List<Long> classifierIIDs = new ArrayList<Long>();
    if (classifierCodes.isEmpty()) {
      return classifierIIDs;
    }
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(CLASSIFIER_IIDS, Text.join(",", classifierCodes, "'%s'"));
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.execute();
      IResultSetParser ruleResult = currentConn.getResultSetParser(stmt.getResultSet());
      while (ruleResult.next()) {
        classifierIIDs.add(ruleResult.getLong("classifieriid"));
      }
    });
    return classifierIIDs;
  }
  
  private void handlingOfPropertyAdditionInPC(Set<Long> baseentityIids) throws Exception
  {
    // Only handling for Calculated / Concatenated attributes addition in PC
    Map<String, String> calculatedAttrPropertyIIDsAndCodes = getCalculatedAndConcatenatedPropertyIIDsAndCodes();
    
    if (!calculatedAttrPropertyIIDsAndCodes.isEmpty()) {
      for (Long baseentiyiid : baseentityIids) {
        createCalculatedConcatenatedAttributeInstance(baseentiyiid, calculatedAttrPropertyIIDsAndCodes);
      }
    }
  }
  
  private static final String CAL_CONCAT_PROPERTY_IIDS_CODES = " select propertyiid, propertycode from pxp.propertyconfig where propertycode in ( %s ) and propertytype in ( 2 , 3 )";
  
  private Map<String, String> getCalculatedAndConcatenatedPropertyIIDsAndCodes()
      throws RDBMSException
  {
    Set<String> addedPropertyCodes = propagatePCOnPropertyDeleteDTO.getAddedPropertyCodes();
    
    Map<String, String> calConcatPropertyCodes = new HashMap<String, String>();
    if (addedPropertyCodes.isEmpty()) {
      return calConcatPropertyCodes;
    }
    
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          String query = String.format(CAL_CONCAT_PROPERTY_IIDS_CODES,
              Text.join(",", addedPropertyCodes, "'%s'"));
          PreparedStatement stmt = currentConn.prepareStatement(query.toString());
          stmt.execute();
          IResultSetParser ruleResult = currentConn.getResultSetParser(stmt.getResultSet());
          while (ruleResult.next()) {
            calConcatPropertyCodes.put(ruleResult.getString("propertyiid"),
                ruleResult.getString("propertycode"));
          }
        });
    
    return calConcatPropertyCodes;
  }
  
  private void createCalculatedConcatenatedAttributeInstance(Long baseEntityIID,
      Map<String, String> calConcatAttrPropIIdsAndCodes) throws Exception
  {
    ILocaleCatalogDAO localCatalogDAO = openUserSession().openLocaleCatalog(userSession,
        baseEntityIID);
    IBaseEntityDTO baseEntityDTO = localCatalogDAO.getEntityByIID(baseEntityIID);
    BaseEntityDAO baseEntityDAO = (BaseEntityDAO) localCatalogDAO.openBaseEntity(baseEntityDTO);
    
    List<String> existingCalConcatAttrPropIIDs = getExistingValueRecordsPropIIDS(
        baseEntityIID, calConcatAttrPropIIdsAndCodes);
    
    List<String> calConactAttrPropIIDs = new ArrayList<String>(calConcatAttrPropIIdsAndCodes.keySet());
    calConactAttrPropIIDs.removeAll(existingCalConcatAttrPropIIDs);
    
    if (calConactAttrPropIIDs.isEmpty()) {
      return;
    }
    
    IConfigDetailsForSaveConcatenatedAttributeModel configDetails = getConfigDetails(baseEntityDTO);
    
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails
        .getReferencedElements();
    
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO,
        referencedAttributes, configDetails.getReferencedTags(), referencedElements,
        PropertyRecordType.DEFAULT, localCatalogDAO);
    
    List<IPropertyRecordDTO> addedPropertyRecords = new ArrayList<IPropertyRecordDTO>();
    for (String calConcatAttrPropertyIID : calConactAttrPropIIDs) {
      String calConcatAttrCode = calConcatAttrPropIIdsAndCodes.get(calConcatAttrPropertyIID);
      
      IPropertyRecordDTO propertyRecordDTO = propertyRecordBuilder
          .createValueRecord(referencedAttributes.get(calConcatAttrCode));
      
      if (propertyRecordDTO != null) {
        addedPropertyRecords.add(propertyRecordDTO);
      }
    }
    
    if (!addedPropertyRecords.isEmpty()) {
      baseEntityDAO.createPropertyRecords(
          addedPropertyRecords.toArray(new IPropertyRecordDTO[addedPropertyRecords.size()]));
      localCatalogDAO.postUsecaseUpdate(baseEntityIID, IEventDTO.EventType.ELASTIC_UPDATE);
    }
  }

  private static final String SELECT_VALUE_RECORDS = " select propertyiid from pxp.valuerecord where entityiid in ( %s ) and propertyiid in ( %s )";
  
  private List<String> getExistingValueRecordsPropIIDS(Long baseEntityIID,
      Map<String, String> calConcatAttrPropIIdsAndCodes) throws RDBMSException
  {
    List<String> calConcatAttrPropIIDs = new ArrayList<String>();
    
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          String query = String.format(SELECT_VALUE_RECORDS, Text.join(",", baseEntityIID),
              Text.join(",", calConcatAttrPropIIdsAndCodes.keySet()));
          PreparedStatement stmt = currentConn.prepareStatement(query.toString());
          stmt.execute();
          IResultSetParser ruleResult = currentConn.getResultSetParser(stmt.getResultSet());
          while (ruleResult.next()) {
            calConcatAttrPropIIDs.add(ruleResult.getString("propertyiid"));
          }
        });
    
    return calConcatAttrPropIIDs;
  }
  
  private IConfigDetailsForSaveConcatenatedAttributeModel getConfigDetails(
      IBaseEntityDTO baseEntity) throws Exception
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    
    List<String> nonNatureClassifierCodes = new ArrayList<>();
    nonNatureClassifierCodes.add(baseEntity.getNatureClassifier().getCode());
    List<String> selectedTaxonomyCodes = new ArrayList<>();
    baseEntity.getOtherClassifiers()
        .forEach(classifier -> {
          ClassifierType classifierType = classifier.getClassifierType();
          if (classifierType.equals(ClassifierType.CLASS)) {
            nonNatureClassifierCodes.add(classifier.getCode());
          }
          else {
            selectedTaxonomyCodes.add(classifier.getCode());
          }
        });
    
    multiclassificationRequestModel.setKlassIds(nonNatureClassifierCodes);
    multiclassificationRequestModel.setSelectedTaxonomyIds(selectedTaxonomyCodes);
    
    IGetConfigDetailsForSaveConcatenatedAttributeStrategy getConfigDetailsForSaveConcatenatedAttributeStrategy = getBean(
        IGetConfigDetailsForSaveConcatenatedAttributeStrategy.class);
    
    IConfigDetailsForSaveConcatenatedAttributeModel configDetails = getConfigDetailsForSaveConcatenatedAttributeStrategy
        .execute(multiclassificationRequestModel);
    
    return configDetails;
  }
}
