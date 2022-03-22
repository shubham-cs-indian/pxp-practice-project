package com.cs.core.config.interactor.usecase.migration;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.process.idao.IMigrationDAO;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service public class MigrateCoupledRecords extends AbstractRuntimeService<IVoidModel, IVoidModel> implements IMigrateCoupledRecords {

  @Autowired protected RDBMSComponentUtils rdbmsComponentUtils;

  @Override
  public IVoidModel executeInternal(IVoidModel dataModel) throws RDBMSException, CSFormatException
  {
    //FOR TIGHT COUPLING
    IMigrationDAO localeCatalogDAO = rdbmsComponentUtils.getMigrationDAO();
    List<Map<String, Object>> tightlyCoupledRecords = localeCatalogDAO.getAllTightlyCoupledRecord();

    for (Map<String, Object> coupledRecord : tightlyCoupledRecords) {
      long entityIID = (long) coupledRecord.get("entityIID");
      long propertyIID = (long) coupledRecord.get("propertyIID");

      long masterEntityIID = (long) coupledRecord.get("masterEntityIID");
      long localeIID = (long) coupledRecord.get("localeIID");

      IPropertyDTO property = ConfigurationDAO.instance().getPropertyByIID(propertyIID);

      Optional<IPropertyRecordDTO> recordOpt = localeCatalogDAO.getSourceRecord(masterEntityIID, property, localeIID);
      IPropertyRecordDTO record = recordOpt.get();
      record.setEntityIID(entityIID);
      localeCatalogDAO.createRecord(record);
      localeCatalogDAO.deleteRecord(entityIID, property);
     }

    //FOR DYNAMIC COUPLING
    Map<String, List<Map<String, Object>>> conflictingValues = localeCatalogDAO.getDynamicConflictingValues();

    for (Map.Entry<String, List<Map<String, Object>>> conflictingValuesEntry : conflictingValues.entrySet()) {
      String[] keys = conflictingValuesEntry.getKey().split("__");
      List<Map<String, Object>> values = conflictingValuesEntry.getValue();

      long targetEntityIID = Long.parseLong(keys[0]);
      long propertyIID = Long.parseLong(keys[1]);

      List<Long> sourceEntityIIDs =  values.stream().map(x -> (Long) x.get("sourceEntityIID")).collect(Collectors.toList());
      Long localeIID = values.stream().map(x -> (Long)x.get("localeIID")).findFirst().orElse(0L);

      IPropertyDTO property = ConfigurationDAO.instance().getPropertyByIID(propertyIID);
      Optional<IPropertyRecordDTO> record = localeCatalogDAO.getSourceRecord(targetEntityIID, property, localeIID);

      Long similarEntityIID = 0L;
      if (record.isPresent()) {
        localeCatalogDAO.deleteRecord(targetEntityIID, property);
        IPropertyRecordDTO value = record.get();
        similarEntityIID = localeCatalogDAO.findSimilarValuedSourceEntity(value, sourceEntityIIDs);
      }
      long entityToCouple =  similarEntityIID.equals(0L) ? sourceEntityIIDs.get(0) : similarEntityIID;
      sourceEntityIIDs.remove(entityToCouple);
      CouplingType couplingType = values.stream().filter(x -> x.get("sourceEntityIID").equals(entityToCouple))
          .map(x -> CouplingType.valueOf((int)x.get("couplingSourceType"))).findFirst().orElse(CouplingType.NONE);
      localeCatalogDAO.migrateToDynamicCoupling(entityToCouple, property, targetEntityIID, sourceEntityIIDs, localeIID, couplingType);
    }
    return null;
  }

}
