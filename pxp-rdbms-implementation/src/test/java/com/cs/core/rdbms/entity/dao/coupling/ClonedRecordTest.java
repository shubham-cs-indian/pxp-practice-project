package com.cs.core.rdbms.entity.dao.coupling;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.standard.IStandardConfig.StandardCatalog;
import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.core.bgprocess.dto.TransferPlanDTO;
import com.cs.core.bgprocess.idto.ITransferPlanDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;

import static com.cs.core.rdbms.testutil.DataTestUtils.*;

public class ClonedRecordTest extends AbstractRDBMSDriverTests {
  
  @Test
  public void cloneEntityWithDirectRecords() throws RDBMSException, CSFormatException, CSInitializationException
  {
    printTestTitle("cloneEntityWithDirectRecords");
    BaseEntityDTO originEntityDTO = DataTestUtils.newBaseEntity("Origin", true);
    IBaseEntityDAO originEntityDAO = localeCatalogDao.openBaseEntity(originEntityDTO);
    originEntityDAO.createPropertyRecords(createRandomTagRecord(originEntityDAO),
        createRandomValueRecordText(originEntityDAO), createRelationsSetRecord(originEntityDAO));
    Collection<IPropertyDTO> properties = localeCatalogDao.getAllEntityProperties(originEntityDTO.getBaseEntityIID());
    IBaseEntityDTO cloneEntityDTO = localeCatalogDao.cloneEntityByIID(
        originEntityDTO.getBaseEntityIID(), new HashSet<IClassifierDTO>(), properties.toArray(new IPropertyDTO[0]));
    printJSON("Origin Entity DTO", originEntityDAO.getBaseEntityDTO());
    printJSON("Clone Entity DTO", cloneEntityDTO);
    assert (cloneEntityDTO.getPropertyRecords().size() == 4);
    assert (cloneEntityDTO.getPropertyRecord(StandardProperty.nameattribute.getIID()) != null);
    assert (cloneEntityDTO.getBaseEntityID()
        .matches(String.format("%s[#][0-9]+$", originEntityDTO.getBaseEntityID())));
  }
  
  @Test
  public void cloneOfCloneEntityWithDirectRecords() throws RDBMSException, CSFormatException, CSInitializationException
  {
    printTestTitle("cloneOfCloneEntityWithDirectRecords");
    BaseEntityDTO originEntityDTO = DataTestUtils.newBaseEntity("Origin", true);
    IBaseEntityDAO originEntityDAO = localeCatalogDao.openBaseEntity(originEntityDTO);
    originEntityDAO.createPropertyRecords(createRandomTagRecord(originEntityDAO),
        createRandomValueRecordText(originEntityDAO), createRelationsSetRecord(originEntityDAO));
    Collection<IPropertyDTO> properties = localeCatalogDao
        .getAllEntityProperties(originEntityDTO.getBaseEntityIID());
    IBaseEntityDTO cloneEntityDTO = localeCatalogDao.cloneEntityByIID(
        originEntityDTO.getBaseEntityIID(),new HashSet<IClassifierDTO>(), properties.toArray(new IPropertyDTO[0]));
    printJSON("Origin Entity DTO", originEntityDAO.getBaseEntityDTO());
    printJSON("Clone Entity DTO", cloneEntityDTO);
    assert (cloneEntityDTO.getPropertyRecords().size() == 4);
    assert (cloneEntityDTO.getPropertyRecord(StandardProperty.nameattribute.getIID()) != null);
    assert (cloneEntityDTO.getBaseEntityID()
        .matches(String.format("%s[#][0-9]+$", originEntityDTO.getBaseEntityID())));
    
    IBaseEntityDTO cloneOfClonedEntityDTO = localeCatalogDao.cloneEntityByIID(
        cloneEntityDTO.getBaseEntityIID(),new HashSet<IClassifierDTO>(), properties.toArray(new IPropertyDTO[0]));
    printJSON("Clone Entity DTO", cloneEntityDTO);
    printJSON("Clone of cloned entity DTO", cloneOfClonedEntityDTO);
    assert (cloneOfClonedEntityDTO.getPropertyRecords().size() == 4);
    assert (cloneOfClonedEntityDTO
        .getPropertyRecord(StandardProperty.nameattribute.getIID()) != null);
  }
  
  @Test
  public void updateCloneOfCloneEntityWithDirectRecords() throws RDBMSException, CSFormatException, CSInitializationException
  {
    printTestTitle("updateCloneOfCloneEntityWithDirectRecords");
    BaseEntityDTO originEntityDTO = DataTestUtils.newBaseEntity("Origin", true);
    IBaseEntityDAO originEntityDAO = localeCatalogDao.openBaseEntity(originEntityDTO);
    IValueRecordDTO valueRecordDTO = createRandomValueRecordText(originEntityDAO);
    originEntityDAO.createPropertyRecords(createRandomTagRecord(originEntityDAO), valueRecordDTO,
        createRelationsSetRecord(originEntityDAO));
    Collection<IPropertyDTO> properties = localeCatalogDao
        .getAllEntityProperties(originEntityDTO.getBaseEntityIID());
    IBaseEntityDTO cloneEntityDTO = localeCatalogDao.cloneEntityByIID(
        originEntityDTO.getBaseEntityIID(),new HashSet<IClassifierDTO>(), properties.toArray(new IPropertyDTO[0]));
    printJSON("Origin Entity DTO", originEntityDAO.getBaseEntityDTO());
    printJSON("Clone Entity DTO", cloneEntityDTO);
    assert (cloneEntityDTO.getPropertyRecords().size() == 4);
    assert (cloneEntityDTO.getPropertyRecord(StandardProperty.nameattribute.getIID()) != null);
    assert (cloneEntityDTO.getBaseEntityID()
        .matches(String.format("%s[#][0-9]+$", originEntityDTO.getBaseEntityID())));
    
    IBaseEntityDTO cloneOfCloneEntityDTO = localeCatalogDao.cloneEntityByIID(
        cloneEntityDTO.getBaseEntityIID(), new HashSet<IClassifierDTO>(), properties.toArray(new IPropertyDTO[0]));
    assert (cloneOfCloneEntityDTO.getPropertyRecords().size() == 4);
    assert (cloneOfCloneEntityDTO
        .getPropertyRecord(StandardProperty.nameattribute.getIID()) != null);
    // Update cloned value
    IValueRecordDTO valueRecord = (IValueRecordDTO) cloneOfCloneEntityDTO
        .getPropertyRecord(valueRecordDTO.getProperty().getIID());
    valueRecord.setValue("New Value");
    IBaseEntityDAO cloneOfClonedEntityDAO = localeCatalogDao.openBaseEntity(cloneOfCloneEntityDTO);
    cloneOfClonedEntityDAO.updatePropertyRecords(valueRecord);
    Collection<IPropertyDTO> entityProperties = localeCatalogDao
        .getAllEntityProperties(cloneEntityDTO.getBaseEntityIID());
    cloneOfClonedEntityDAO.loadPropertyRecords(entityProperties.toArray(new IPropertyDTO[0]));
    printJSON("Updated clone of cloned entity DTO", cloneOfClonedEntityDAO.getBaseEntityDTO());
    valueRecord = (IValueRecordDTO) cloneOfCloneEntityDTO
        .getPropertyRecord(valueRecordDTO.getProperty().getIID());
    assert (valueRecord.getValue().equals("New Value"));
    assert (valueRecord.getRecordStatus() == RecordStatus.DIRECT);
    assert (valueRecord.getMasterEntityIID() == 0L);
    assert (valueRecord.getMasterPropertyIID() == 0L);
    assert (valueRecord.getMasterNodeID() == null || valueRecord.getMasterNodeID().isEmpty());
    assert (valueRecord.getCouplingBehavior() == CouplingBehavior.NONE);
  }

  @Test
  public void cloneOfCoupledEnity() throws RDBMSException, CSFormatException, CSInitializationException
  {
    printTestTitle("cloneOfCoupledEnity");
    BaseEntityDTO originEntityDTO = DataTestUtils.newBaseEntity("Origin", true);
    IBaseEntityDAO originEntityDAO = localeCatalogDao.openBaseEntity(originEntityDTO);
    IValueRecordDTO valueRecord = createRandomValueRecordText(originEntityDAO);
    ITagsRecordDTO tagsRecord = createRandomTagRecord(originEntityDAO);
    IRelationsSetDTO relationsSetRecord = createRelationsSetRecord(originEntityDAO);
    originEntityDAO.createPropertyRecords(valueRecord, tagsRecord, relationsSetRecord);
    printJSON("Origin Entity", originEntityDTO);
    Collection<IPropertyDTO> properties = localeCatalogDao
        .getAllEntityProperties(originEntityDTO.getBaseEntityIID());
    
    IBaseEntityDTO cloneEntityDTO = localeCatalogDao.cloneEntityByIID(
        originEntityDTO.getBaseEntityIID(),new HashSet<IClassifierDTO>(), properties.toArray(new IPropertyDTO[0]));
    printJSON("Cloned Entity", cloneEntityDTO);
    assert (cloneEntityDTO.getPropertyRecords()
        .size() == 4);
  }
  
  @Test
  public void transferEntity() throws RDBMSException, CSFormatException
  {
    printTestTitle("transferEntity");
    BaseEntityDTO originEntityDTO = DataTestUtils.newBaseEntity("Origin", true);
    IBaseEntityDAO originEntityDAO = localeCatalogDao.openBaseEntity(originEntityDTO);
    originEntityDAO.createPropertyRecords(createRandomTagRecord(originEntityDAO),
        createRandomValueRecordText(originEntityDAO), createRelationsSetRecord(originEntityDAO));
    Collection<IPropertyDTO> properties = localeCatalogDao
        .getAllEntityProperties(originEntityDTO.getBaseEntityIID());
    ITransferPlanDTO dto = new TransferPlanDTO();
    dto.setTargetCatalogCode(StandardCatalog.offboarding.toString());
    dto.setOrganizationCode(IStandardConfig.STANDARD_ORGANIZATION_CODE);
    IBaseEntityDTO cloneEntityDTO = localeCatalogDao.transferEntityByIID(
        originEntityDTO.getBaseEntityIID(), dto,
        properties.toArray(new IPropertyDTO[0]));
    printJSON("Origin Entity DTO", originEntityDAO.getBaseEntityDTO());
    printJSON("Transferred Entity DTO", cloneEntityDTO);
    assert (cloneEntityDTO.getPropertyRecords().size() == 4);
    assert (cloneEntityDTO.getPropertyRecord(StandardProperty.nameattribute.getIID()) != null);
    assert (cloneEntityDTO.getBaseEntityID().equals(originEntityDTO.getBaseEntityID()));
  }
}
