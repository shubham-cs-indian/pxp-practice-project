package com.cs.core.config.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.auditlog.GetAllAuditLogUserResponseModel;
import com.cs.core.config.interactor.model.auditlog.IGetAllAuditLogUserResponseModel;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogUserRequestModel;
import com.cs.core.rdbms.auditlog.idao.IAuditLogDAO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class GetAllAuditLogUserService extends
    AbstractGetConfigService<IGetGridAuditLogUserRequestModel, IGetAllAuditLogUserResponseModel>
    implements IGetAllAuditLogUserService {
  
  @Override
  protected IGetAllAuditLogUserResponseModel executeInternal(IGetGridAuditLogUserRequestModel model)
      throws Exception
  {
    IAuditLogDAO auditlogDAO = RDBMSUtils.newUserSessionDAO()
        .newAuditLogDAO();
    
    List<IAuditLogDTO> auditLogUserDTOs = auditlogDAO.fetchAllAuditLogUsers(model.getSearchText(),
        model.getSortOrder(), model.getSize(), model.getFrom());
    List<Map<String, Object>> auditLogUserList = new ArrayList<>();
    for (IAuditLogDTO userRecordDTO : auditLogUserDTOs) {
      Map<String, Object> userMap = new HashMap<String, Object>();
      userMap.put(CommonConstants.ID_PROPERTY, userRecordDTO.getUserName());
      userMap.put(CommonConstants.LABEL_PROPERTY, userRecordDTO.getUserName());
      auditLogUserList.add(userMap);
    }
    
    Map<String, Object> auditLogMap = new HashMap<String, Object>();
    auditLogMap.put(CommonConstants.LIST_PROPERTY, auditLogUserList);
    
    Long auditLogUserRowCount = auditlogDAO.getAuditLogUsersCount(model.getSearchText());
    IGetAllAuditLogUserResponseModel response = new GetAllAuditLogUserResponseModel();
    response.setAuditLogUserList(auditLogMap);
    response.setCount(auditLogUserRowCount);
    return response;
  }
  
}
