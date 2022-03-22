package com.cs.core.runtime.elastic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Component
@Scope("prototype")
public class CreateCalculatedPropertyRecordsTask implements Runnable {
  
  private List<Long>               iids;
  private ILocaleCatalogDAO        localeCatalogDAO;
  private List<IPropertyRecordDTO> calculatedPropertyRecords;
  Map<Long, Set<String>>           classifierIIDsVsPropertyCodes = new HashMap<>();
  
  public void setData(List<Long> iids, ILocaleCatalogDAO localeCatalogDAO, List<IPropertyRecordDTO> calculatedPropertyRecords, Map<Long, Set<String>> classifierIIDsVsPropertyCodes)
  {
    this.iids = iids;
    this.localeCatalogDAO = localeCatalogDAO;
    this.calculatedPropertyRecords = new ArrayList<>(calculatedPropertyRecords);
    this.classifierIIDsVsPropertyCodes = classifierIIDsVsPropertyCodes;
  }
  
  @Override
  public void run()
  {
    RDBMSAbstractDriver driver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
    TransactionTemplate transactionTemplate = new TransactionTemplate(driver.getTransactionManager());
    transactionTemplate.execute(new TransactionCallbackWithoutResult()
    {
      
      protected void doInTransactionWithoutResult(TransactionStatus status)
      {
        try {
          execute();
        }
        catch (Exception e) {
          RDBMSLogger.instance().exception(e);
        }
      }
    });
  }
  
  private void execute() throws RDBMSException
  {
    List<String> iidsAsString = iids.stream().map((Object s) -> String.valueOf(s)).collect(Collectors.toList());
    List<IBaseEntityDTO> baseEntities = localeCatalogDAO.getBaseEntitiesByIIDs(iidsAsString);
    for (IBaseEntityDTO baseEntity : baseEntities) {
      try {
        Set<String> applicablePropertyCodes = new HashSet<>();
        Set<IClassifierDTO> otherClassifiers = baseEntity.getOtherClassifiers();
        otherClassifiers.add(baseEntity.getNatureClassifier());
        for (IClassifierDTO classifier : otherClassifiers) {
          Set<String> propertyCodes = classifierIIDsVsPropertyCodes.get(classifier.getClassifierIID());
          if (propertyCodes != null) {
            applicablePropertyCodes.addAll(propertyCodes);
          }
        }
        if (applicablePropertyCodes.isEmpty()) {
          continue;
        }
        IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(baseEntity);
        List<IPropertyRecordDTO> propertyRecordsToCreate = new ArrayList<>();
        for (IPropertyRecordDTO calculatedPropertyRecord : calculatedPropertyRecords) {
          if(applicablePropertyCodes.contains(calculatedPropertyRecord.getProperty().getPropertyCode())) {
            calculatedPropertyRecord.setEntityIID(baseEntity.getBaseEntityIID());
            propertyRecordsToCreate.add(calculatedPropertyRecord);
          }
        }
        baseEntityDAO.createCalulatedPropertyRecord(propertyRecordsToCreate);
      }
      catch (Exception e) {
        RDBMSLogger.instance().exception(e);
      }
    }
    RDBMSLogger.instance().info("Create Calculated Current Batch Complete!!!");
  }
  
}
