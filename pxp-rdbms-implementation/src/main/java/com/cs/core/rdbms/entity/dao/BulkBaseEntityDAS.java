package com.cs.core.rdbms.entity.dao;

import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.ContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.process.idao.IBulkCatalogDAO.UpdateLinkMode;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vallee
 */
public class BulkBaseEntityDAS extends RDBMSDataAccessService {
  
  private RDBMSAbstractFunction createBaseEntity               = null;
  private RDBMSAbstractFunction updateBaseEntityContextualData = null;
  private RDBMSAbstractFunction updateClassifiers              = null;
  private RDBMSAbstractFunction updateEntity                   = null;
  
  public BulkBaseEntityDAS(RDBMSConnection connection)
  {
    super(connection);
  }
  
  /**
   * @param entity
   *          for batch creation
   * @throws RDBMSException
   * @throws SQLException
   */
  public void createEntity(IBaseEntityDTO entity, long userIID) throws RDBMSException, SQLException
  {
    if (createBaseEntity == null)
      createBaseEntity = driver.getProcedure(currentConnection, "pxp.sp_createBaseEntity");
    createBaseEntity.setInput(ParameterType.IID, entity.getBaseEntityIID())
        .setInput(ParameterType.STRING, entity.getBaseEntityID())
        .setInput(ParameterType.STRING, entity.getContextualObject()
            .getContextCode()
            .isEmpty() ? null
                : entity.getContextualObject()
                    .getContextCode())
        .setInput(ParameterType.STRING, entity.getLocaleCatalog()
            .getCatalogCode())
        .setInput(ParameterType.IID, entity.getNatureClassifier()
            .getIID())
        .setInput(ParameterType.INT, entity.getBaseType()
            .ordinal())
        .setInput(ParameterType.INT, entity.getChildLevel())
        .setInput(ParameterType.STRING, entity.getSourceCatalogCode()
            .isEmpty() ? null : entity.getSourceCatalogCode())
        .setInput(ParameterType.STRING, entity.getLocaleCatalog()
            .getLocaleID())
        .setInput(ParameterType.LONG, entity.getParentIID() == 0 ? null : entity.getParentIID())
        .setInput(ParameterType.LONG,
            entity.getTopParentIID() == 0 ? null : entity.getTopParentIID())
        .setInput(ParameterType.LONG,
            entity.getDefaultImageIID() == 0 ? null : entity.getDefaultImageIID())
        .setInput(ParameterType.LONG,
            entity.getOriginBaseEntityIID() == 0 ? null : entity.getOriginBaseEntityIID())
        .setInput(ParameterType.STRING, entity.getHashCode()
            .isEmpty() ? null : entity.getHashCode())
        .setInput(ParameterType.JSON, entity.getEntityExtension()
            .toString())
        .setInput(ParameterType.LONG, System.currentTimeMillis())
        .setInput(ParameterType.IID, userIID)
        .setInput(ParameterType.STRING, entity.getSourceOrganizationCode())
        .addBatch();
  }
  
  public List<Integer> endCreateEntity() throws SQLException, RDBMSException
  {
    if (createBaseEntity != null)
      return createBaseEntity.executeBatch();
    return new ArrayList<>();
  }
  
  /**
   * @param contextualObject
   *          for batch creation
   * @param contextTagMode
   *          specification add or overwrite for context tags
   * @throws RDBMSException
   * @throws SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void updateBaseEntityContextualData(IContextualDataDTO contextualObject,
      UpdateLinkMode contextTagMode) throws SQLException, RDBMSException, CSFormatException
  {
    if (updateBaseEntityContextualData == null)
      updateBaseEntityContextualData = driver.getProcedure(currentConnection,
          "pxp.sp_updateBaseEntityContextualData");
    if (contextTagMode == UpdateLinkMode.add) {
      ContextualDataDTO existing = (new BaseEntityDAS( currentConnection))
          .loadContextualObject(contextualObject.getContextualObjectIID());
      contextualObject.getContextTagValues()
          .addAll(existing.getContextTagValues());
    }
    updateBaseEntityContextualData
        .setInput(ParameterType.IID, contextualObject.getContextualObjectIID())
        .setInput(ParameterType.LONG, contextualObject.getContextStartTime())
        .setInput(ParameterType.LONG, contextualObject.getContextEndTime())
        .setInput(ParameterType.STRING_ARRAY,
            ((ContextualDataDTO) contextualObject).getContextTagValueCodes())
        .setInput(ParameterType.INT_ARRAY,
            ((ContextualDataDTO) contextualObject).getContextTagRanges())
        .setInput(ParameterType.IID_ARRAY,
            ((ContextualDataDTO) contextualObject).getLinkedBaseEntityIIDs())
        .addBatch();
  }
  
  public List<Integer> endUpdateBaseEntityContextualData() throws SQLException, RDBMSException
  {
    if (updateBaseEntityContextualData != null)
      return updateBaseEntityContextualData.executeBatch();
    return new ArrayList<>();
  }
  
  public void updateClassifiers(BaseEntityDTO entityDto, UpdateLinkMode classifiersMode)
      throws SQLException, RDBMSException
  {
    if (updateClassifiers == null)
      updateClassifiers = driver.getProcedure(currentConnection, "pxp.sp_addClassifiers");
    if (classifiersMode == UpdateLinkMode.overwrite) {
      (new BaseEntityDAS( currentConnection))
          .removeAllOtherClassifiers(entityDto.getBaseEntityIID());
    }
    updateClassifiers.setInput(ParameterType.IID, entityDto.getBaseEntityIID())
        .setInput(ParameterType.IID_ARRAY, entityDto.getOtherClassifierIIDs())
        .addBatch();
  }
  
  public List<Integer> endUpdateClassifiers() throws SQLException, RDBMSException
  {
    if (updateClassifiers != null)
      return updateClassifiers.executeBatch();
    return new ArrayList<>();
  }
  
  public void updateEntity(BaseEntityDTO entityDto, long userIID)
      throws SQLException, RDBMSException
  {
    if (updateEntity == null)
      updateEntity = driver.getProcedure(currentConnection, "pxp.sp_updateBaseEntity");
    updateEntity.setInput(ParameterType.IID, entityDto.getBaseEntityIID())
        .setInput(ParameterType.IID,
            entityDto.getDefaultImageIID() == 0 ? null : entityDto.getDefaultImageIID())
        .setInput(ParameterType.STRING, entityDto.getHashCode()
            .isEmpty() ? null : entityDto.getHashCode())
        .setInput(ParameterType.JSON, entityDto.getHashCode()
            .isEmpty() ? null : entityDto.getHashCode())
        .setInput(ParameterType.JSON, entityDto.getEntityExtension()
            .toString())
        .setInput(ParameterType.LONG, System.currentTimeMillis())
        .setInput(ParameterType.IID, userIID)
        .addBatch();
  }
  
  public List<Integer> endUpdateEntity() throws SQLException, RDBMSException
  {
    if (updateEntity != null)
      return updateEntity.executeBatch();
    return new ArrayList<>();
  }
  
  public void updateChildren(BaseEntityDTO entityDto, UpdateLinkMode updateLinkMode)
  {
    throw new UnsupportedOperationException("Not supported yet."); // To change
                                                                   // body of
                                                                   // generated
                                                                   // methods,
                                                                   // choose
                                                                   // Tools |
                                                                   // Templates.
  }
  
  public void endUpdateChildren()
  {
    throw new UnsupportedOperationException("Not supported yet."); // To change
                                                                   // body of
                                                                   // generated
                                                                   // methods,
                                                                   // choose
                                                                   // Tools |
                                                                   // Templates.
  }
  
}
