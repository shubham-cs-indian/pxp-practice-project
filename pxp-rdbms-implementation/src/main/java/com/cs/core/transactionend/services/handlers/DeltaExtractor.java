package com.cs.core.transactionend.services.handlers;

import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.revision.dto.RevisionTimelineBuilder;
import com.cs.core.rdbms.tracking.dto.ObjectTrackingDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEList;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.transactionend.handlers.dto.DeltaInfoDTO;
import com.cs.core.transactionend.handlers.dto.IDeltaInfoDTO;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import static com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory.*;

public class DeltaExtractor {

  public static Map<Long, IDeltaInfoDTO> getDelta(String transactionId) throws RDBMSException
  {
    Map<Long, IDeltaInfoDTO> deltaInfo = new HashMap<>();
    Map<Long, RevisionTimelineBuilder> changes = extractChanges(transactionId);

    for (Map.Entry<Long, RevisionTimelineBuilder> change : changes.entrySet()) {
      RevisionTimelineBuilder value = change.getValue();
      Map<ITimelineDTO.ChangeCategory, ICSEList> delta = value.getDelta();

      IDeltaInfoDTO deltaInfoDTO = deltaInfo.get(change.getKey());

      if(deltaInfoDTO == null){
        deltaInfoDTO = new DeltaInfoDTO();
        deltaInfo.put(change.getKey(), deltaInfoDTO);
      }

      if (delta.containsKey(CreatedRecord)) {
        for (ICSEElement updatedElement : delta.get(CreatedRecord).getSubElements()) {
          String code = ((ICSEObject) updatedElement).getCode();
          long propertyIID = ConfigurationDAO.instance().getPropertyByCode(code).getPropertyIID();
          deltaInfoDTO.getAddedPropertyIIDs().add(propertyIID);
        }
      }
      if (delta.containsKey(UpdatedRecord)) {
        for (ICSEElement updatedElement : delta.get(UpdatedRecord).getSubElements()) {
          String code = ((ICSEObject) updatedElement).getCode();
          IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(code);
          deltaInfoDTO.getModifiedPropertyIIDs().add(propertyByCode.getPropertyIID());
        }
      }
      if (delta.containsKey(DeletedRecord)) {
        for (ICSEElement updatedElement : delta.get(DeletedRecord).getSubElements()) {
          String code = ((ICSEObject) updatedElement).getCode();
          IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(code);
          deltaInfoDTO.getDeletedPropertyIIDs().add(propertyByCode.getPropertyIID());
        }
      }
      if (delta.containsKey(CouplingSource)) {
        for (ICSEElement updatedElement : delta.get(CouplingSource).getSubElements()) {
          String code = ((ICSEObject) updatedElement).getCode();
          deltaInfoDTO.getAddedCoupledProperties().add(code);
        }
      }
      if (delta.containsKey(RemovedCouplingSource)) {
        for (ICSEElement deletedElement : delta.get(RemovedCouplingSource).getSubElements()) {
          String code = ((ICSEObject) deletedElement).getCode();
          deltaInfoDTO.getRemovedCoupledProperties().add(code);
        }
      }
      if (delta.containsKey(CreatedEntity)) {
        deltaInfoDTO.setCreated(true);
      }
    }
    return deltaInfo;
  }

  private static final String GET_QUERY_FORMAT = "select * from pxp.objectTrackingWithUser where transactionId = '%s' ";

  protected static Map<Long, RevisionTimelineBuilder> extractChanges(String transactionId) throws RDBMSException
  {
    Map<Long, RevisionTimelineBuilder> delta = new HashMap<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement cs = currentConn.prepareStatement(String.format(GET_QUERY_FORMAT, transactionId));
      IResultSetParser resultSet = currentConn.getResultSetParser(cs.executeQuery());

      while (resultSet.next()) {
        ObjectTrackingDTO change = new ObjectTrackingDTO();
        change.set(resultSet);
        long objectIID = change.getObjectIID();

        if (delta.containsKey(objectIID)) {
          delta.get(objectIID).addTrackingTimeline(change);
        }
        else {
          RevisionTimelineBuilder rtBuilder = new RevisionTimelineBuilder();
          rtBuilder.addTrackingTimeline(change);
          delta.put(objectIID, rtBuilder);
        }
      }
    });
    return delta;
  }

  public static void getCouplingDelta(String transactionId)
  {
  }
}
