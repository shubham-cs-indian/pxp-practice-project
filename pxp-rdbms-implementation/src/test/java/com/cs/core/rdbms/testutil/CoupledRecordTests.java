package com.cs.core.rdbms.testutil;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public abstract class CoupledRecordTests extends AbstractRDBMSDriverTests {
  
  protected void assertNotNull(Object object)
  {
    assert (object != null);
  }
  
  protected void assertNull(Object object)
  {
    assert (object == null);
  }
  
  @SuppressWarnings("unchecked")
  protected <T extends IPropertyRecordDTO> T getProperty(IBaseEntityDAO entityDAO,
      IPropertyDTO property)
  {
    return (T) entityDAO.getBaseEntityDTO()
        .getPropertyRecord(property.getIID());
  }
  
  @SuppressWarnings("unchecked")
  protected <T extends IPropertyRecordDTO> T loadProperty(IBaseEntityDAO entityDAO,
      IPropertyDTO property) throws RDBMSException, CSFormatException
  {
    return (T) entityDAO.loadPropertyRecords(property)
        .getPropertyRecord(property.getIID());
  }
  
  protected void assertDynamicCoupling(IPropertyRecordDTO propertyRecord, CouplingType couplingType)
      throws CSFormatException
  {
    assert (RecordStatus.COUPLED == propertyRecord.getRecordStatus());
    assert (CouplingBehavior.DYNAMIC == propertyRecord.getCouplingBehavior());
    assert (couplingType == propertyRecord.getCouplingType());
    assert (!propertyRecord.getCouplingExpressions()
        .isEmpty());
    assert (!propertyRecord.getMasterNodeID()
        .isEmpty());
  }
  
  protected void assertTightCoupling(IPropertyRecordDTO propertyRecord, CouplingType couplingType)
      throws CSFormatException
  {
    assert (RecordStatus.COUPLED == propertyRecord.getRecordStatus());
    assert (CouplingBehavior.TIGHTLY == propertyRecord.getCouplingBehavior());
    assert (couplingType == propertyRecord.getCouplingType());
    assert (!propertyRecord.getCouplingExpressions()
        .isEmpty());
    assert (!propertyRecord.getMasterNodeID()
        .isEmpty());
  }
  
  protected void assertUpdatedTightCoupling(IPropertyRecordDTO propertyRecord,
      CouplingType couplingType, IBaseEntityDAO targetEntityDAO)
      throws CSFormatException, RDBMSException
  {
    assert (RecordStatus.FORKED == propertyRecord.getRecordStatus());
    assert (CouplingBehavior.TIGHTLY == propertyRecord.getCouplingBehavior());
    assert (couplingType == propertyRecord.getCouplingType());
    assert (!propertyRecord.getCouplingExpressions()
        .isEmpty());
    assert (!propertyRecord.getMasterNodeID()
        .isEmpty());
    targetEntityDAO.loadCouplingNotifications(propertyRecord.getProperty());
    assert (propertyRecord.isNotifiedByCoupling());
  }
  
  protected void assertDirectRecord(IPropertyRecordDTO propertyRecord) throws CSFormatException
  {
    assert (RecordStatus.DIRECT == propertyRecord.getRecordStatus());
    assert (propertyRecord.getMasterNodeID() == null || propertyRecord.getMasterNodeID()
        .isEmpty());
    assert (propertyRecord.getMasterNodeID() == null || propertyRecord.getCouplingExpressions()
        .isEmpty());
  }
  
  public abstract void updateDynamiclyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void updateDynamiclyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void updateTightlyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void updateTightlyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void updateDynamiclyCoupledDefaultValue()
      throws RDBMSException, CSFormatException;
  
  public abstract void updateTightlyCoupedDefaultValue() throws RDBMSException, CSFormatException;
  
  public abstract void updateSourceOfDynamiclyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void updateSourceOfDynamiclyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void updateSourceOfTightlyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void updateSourceOfTightlyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void deleteTightlyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void deleteTightlyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void deleteDynamiclyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void deleteDynamiclyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void deleteDynamiclyCoupledDefaultValue()
      throws RDBMSException, CSFormatException;
  
  public abstract void deleteTightlyCoupledDefaultValue() throws RDBMSException, CSFormatException;
  
  public abstract void deleteSourceOfTightlyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void deleteSourceOfTightlyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void deleteSourceOfDynamiclyCoupledByRelationshipRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void deleteSourceOfDynamiclyCoupledByInheritanceRecord()
      throws RDBMSException, CSFormatException;
  
  public abstract void resolveAsDirectRecord() throws RDBMSException, CSFormatException;
  
  public abstract void resolveAsCoupledRecord() throws RDBMSException, CSFormatException;
}
