package com.cs.core.rdbms.process.idao;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IMigrationDAO {


  public Optional<IPropertyRecordDTO> getSourceRecord(long masterEntityIID, IPropertyDTO propertyDTO, long localeIID)
      throws RDBMSException;

  void createRecord(IPropertyRecordDTO record) throws RDBMSException;

  void deleteRecord(long entityIID, IPropertyDTO property) throws RDBMSException;

  List<Map<String, Object>> getAllTightlyCoupledRecord() throws RDBMSException;

  public Map<String,List<Map<String, Object>>> getDynamicConflictingValues() throws RDBMSException;

  Long findSimilarValuedSourceEntity(IPropertyRecordDTO value, List<Long> sourceEntityIIDs) throws RDBMSException;

  void migrateToDynamicCoupling(long entityToCouple, IPropertyDTO property, long targetEntityIID, List<Long> sourceEntityIIDs,long localeIID, ICSECoupling.CouplingType couplingType)
      throws RDBMSException;
}
