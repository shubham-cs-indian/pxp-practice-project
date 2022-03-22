package com.cs.core.config.auditlog;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.config.interactor.model.auditlog.AuditLogModel;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.config.interactor.model.auditlog.GetGridAuditLogResponseModel;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogRequestModel;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogResponseModel;
import com.cs.core.rdbms.auditlog.idao.IAuditLogDAO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogFilterDTO;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetGridAuditLogInfoService extends AbstractGetConfigService<IGetGridAuditLogRequestModel, IGetGridAuditLogResponseModel>
    implements IGetGridAuditLogInfoService {
  
  @Override
  protected IGetGridAuditLogResponseModel executeInternal(IGetGridAuditLogRequestModel model) throws Exception
  {
    IAuditLogDAO auditlogDAO = RDBMSUtils.newUserSessionDAO().newAuditLogDAO();
    
    IAuditLogFilterDTO auditLogFilterDTO = auditlogDAO.newAuditLogFilterDTO(model.getElementName(), model.getElementCode(), model.getIpAddress(), model.getactivityId(), model.getUserName(), model.getActivities(), model.getEntityTypes(), model.getElements(), model.getElementTypes(), model.getFromDate(), model.getToDate());
    StringBuilder filterQuery = auditlogDAO.getFilterQuery(auditLogFilterDTO);
    
    String sortBy = model.getSortBy().equals(IAuditLogModel.ACTIVITY_ID) ? IAuditLogDTO.ACTIVITY_IID : model.getSortBy();
    
    List<IAuditLogDTO> listOfAuditLogDTO  = auditlogDAO.fetchAuditLogs(filterQuery, sortBy, model.getSortOrder(), model.getSize(), model.getFrom());
    
    List<IAuditLogModel> auditLogList = new ArrayList<>();
    
    for(IAuditLogDTO auditLogDTO: listOfAuditLogDTO) {
      IAuditLogModel auditLogRecord = new AuditLogModel();
      
      auditLogRecord.setActivity(auditLogDTO.getActivity().toString());
      auditLogRecord.setActivityId(auditLogDTO.getActivityIID());
      auditLogRecord.setDate(auditLogDTO.getDate());
      auditLogRecord.setElement(auditLogDTO.getElement().toString());
      auditLogRecord.setElementCode(auditLogDTO.getElementCode());
      auditLogRecord.setElementName(auditLogDTO.getElementName());
      auditLogRecord.setElementType(auditLogDTO.getElementType());
      auditLogRecord.setEntityType(auditLogDTO.getEntityType().toString());
      auditLogRecord.setIpAddress(auditLogDTO.getIpAddress());
      auditLogRecord.setUserName(auditLogDTO.getUserName());
      
      auditLogList.add(auditLogRecord);
    }
    
    long auditLogRecordsCount = auditlogDAO.getAuditLogCount(filterQuery);
    
    IGetGridAuditLogResponseModel returnModel = new GetGridAuditLogResponseModel();
    returnModel.setAuditLogList(auditLogList);
    returnModel.setCount(auditLogRecordsCount);
    
    return returnModel;
  }
  
}
