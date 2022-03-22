package com.cs.core.bgprocess.services.datatransfer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections4.map.HashedMap;

import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.coupling.dto.RelationshipDataTransferDTO;
import com.cs.core.rdbms.coupling.idto.IBGPCouplingDTO;
import com.cs.core.rdbms.coupling.idto.IRelationshipDataTransferDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.configdetails.GetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipSidePropertiesModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class RelationshipDataTransfer extends AbstractBGProcessJob implements IBGProcessJob {

  IRelationshipDataTransferDTO relationshipDataTransferDTO = new RelationshipDataTransferDTO();
  protected int nbBatches = 1;
  protected int batchSize;
  int currentBatchNo = 0;

  @Override public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    batchSize = CSProperties.instance().getInt(propName("batchSize"));

    relationshipDataTransferDTO.fromJSON(jobData.getEntryData().toString());

    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }

  @Override public BGPStatus runBatch() throws Exception
  {
    for (IBGPCouplingDTO couplingDTO : relationshipDataTransferDTO.getBGPCouplingDTOs()) {
      processRelationship(couplingDTO);
    }

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

  public void processRelationship(IBGPCouplingDTO couplingDTO) throws Exception
  {
    long relationshipIID = 0;
    List<String> relationshipIds = new ArrayList<>();
    List<String> natureRelationshipIds = new ArrayList<>();
    String relationshipId;
    if (couplingDTO.getRelationshipId() != null && !couplingDTO.getRelationshipId().isEmpty()) {
      relationshipId = couplingDTO.getRelationshipId();
      relationshipIds.add(relationshipId);
    }
    else {
      relationshipId = couplingDTO.getNatureRelationishipId();
      natureRelationshipIds.add(relationshipId);
    }

    for (long sourceEntityIID : couplingDTO.getSourceBaseEntityIIDs()) {
      Map<String, Object> configRequestModel = new HashedMap<>();
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(sourceEntityIID);

      List<String> types = new ArrayList<>();
      List<String> taxonomyIds = new ArrayList<>();
      fillTypesAndTaxonomies(configRequestModel, baseEntityDAO, types, taxonomyIds);
      Collection<IPropertyDTO> allEntityProperties = rdbmsComponentUtils.getLocaleCatlogDAO().getAllEntityProperties(sourceEntityIID);
      baseEntityDAO.loadAllPropertyRecords(allEntityProperties.toArray(new IPropertyDTO[0]));
      IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails = getConfigDetails(relationshipIds, natureRelationshipIds,
          configRequestModel, types, taxonomyIds);

      IRelationship relationshipConfig = configDetails.getReferencedRelationships().get(relationshipId);
      if (relationshipConfig == null) {
        relationshipConfig = configDetails.getReferencedNatureRelationships().get(relationshipId);
      }

      RelationSide relationSide;

      if (couplingDTO.getSideId() == null || couplingDTO.getSideId().isEmpty()) {
        relationshipIID = ConfigurationDAO.instance().getPropertyByCode(couplingDTO.getRelationshipId()).getPropertyIID();
        int side = getSide(relationshipIID, sourceEntityIID);
        relationSide = side == 1 ? RelationSide.SIDE_1 : RelationSide.SIDE_2;
      }
      else {
        relationSide = DataTransferUtil.getRelationshipSide(couplingDTO.getSideId(), relationshipConfig);
      }
      createCouplingPropertyRecords(baseEntityDAO, relationshipId, configDetails, couplingDTO.getAddedEntityIIDs(),
          couplingDTO.getDeletedEntityIIDs(), relationSide, relationshipConfig);

      rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(sourceEntityIID, IEventDTO.EventType.END_OF_TRANSACTION);
    }
  }

  private IGetConfigDetailsForSaveRelationshipInstancesResponseModel getConfigDetails(List<String> relationshipIds,
      List<String> natureRelationshipIds, Map<String, Object> configRequestModel, List<String> types, List<String> taxonomyIds)
      throws Exception
  {
    configRequestModel.put("klassIds", types);
    configRequestModel.put("taxonomyIds", taxonomyIds);
    configRequestModel.put("relationshipIds", relationshipIds);
    configRequestModel.put("natureRelationshipIds", natureRelationshipIds);

    Map<String, Object> configDetailsMap = CSConfigServer.instance()
        .request(configRequestModel, "GetConfigDetailsForSaveRelationshipInstance", "en_US");
    IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails = ObjectMapperUtil.readValue(
        ObjectMapperUtil.writeValueAsString(configDetailsMap), GetConfigDetailsForSaveRelationshipInstancesResponseModel.class);
    return configDetails;
  }

  private void fillTypesAndTaxonomies(Map<String, Object> configRequestModel, IBaseEntityDAO baseEntityDAO, List<String> types,
      List<String> taxonomyIds) throws RDBMSException
  {
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();

    types.add(baseEntityDTO.getNatureClassifier().getCode());
    List<IClassifierDTO> classifiers = baseEntityDAO.getClassifiers();
    for (IClassifierDTO classifier : classifiers) {
      if (classifier.getClassifierType().equals(ClassifierType.CLASS)) {
        types.add(classifier.getCode());
      }
      else {
        taxonomyIds.add(classifier.getCode());
      }
    }
  }

  protected void createCouplingPropertyRecords(IBaseEntityDAO baseEntityDAO, String relationshipId,
      IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails, List<Long> addedTargetBaseEntityIIDs,
      List<Long> deletedTargetBaseEntityIIDs, RelationSide relationshipSide, IRelationship relationshipConfig) throws Exception
  {

    IPropertyDTO relationshipPropertyDTO = RDBMSUtils.newPropertyDTO(relationshipConfig.getPropertyIID(), relationshipConfig.getId(),
        relationshipConfig.getCode(), PropertyType.RELATIONSHIP);
    relationshipPropertyDTO.setRelationSide(relationshipSide);

    IReferencedRelationshipPropertiesModel referencedRelationshipProperties = configDetails.getReferencedRelationshipProperties()
        .get(relationshipId);

    IRelationshipSidePropertiesModel holderSideProperties = null;
    IRelationshipSidePropertiesModel oppositeSideProperties = null;
    if (relationshipSide == RelationSide.SIDE_1) {
      holderSideProperties = referencedRelationshipProperties.getSide1();
      oppositeSideProperties = referencedRelationshipProperties.getSide2();
    }
    else if (relationshipSide == RelationSide.SIDE_2) {
      holderSideProperties = referencedRelationshipProperties.getSide2();
      oppositeSideProperties = referencedRelationshipProperties.getSide1();
    }

    DataTransferUtil.handleHolderSideCouplingProperties(baseEntityDAO, holderSideProperties, relationshipPropertyDTO,
        addedTargetBaseEntityIIDs, deletedTargetBaseEntityIIDs);
    DataTransferUtil.handleOppositeSideCouplingProperties(baseEntityDAO, oppositeSideProperties, relationshipPropertyDTO,
        deletedTargetBaseEntityIIDs, addedTargetBaseEntityIIDs);
  }

  private static final String GET_SIDE_FROM_RELATION = " select count(*) from pxp.relation where propertyiid = ? and side1entityiid = ?";

  public int getSide(Long relationshipIID, Long sourceEntityIID) throws SQLException, RDBMSException
  {
    AtomicInteger side = new AtomicInteger();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      StringBuilder query = new StringBuilder(GET_SIDE_FROM_RELATION);
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.setLong(1, relationshipIID);
      stmt.setLong(2, sourceEntityIID);
      stmt.execute();
      int count = 0;
      IResultSetParser ruleResult = currentConn.getResultSetParser(stmt.getResultSet());
      while (ruleResult.next()) {
        count = ruleResult.getInt("count");
      }
      if (count > 0) {
        side.set(1);
      }
      else {
        side.set(2);
      }
    });
    return side.intValue();
  }
}
