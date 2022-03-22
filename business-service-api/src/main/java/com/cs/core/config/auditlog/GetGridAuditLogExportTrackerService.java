package com.cs.core.config.auditlog;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.auditlog.*;
import com.cs.core.rdbms.auditlog.idao.IAuditLogExportDAO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogExportDTO;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetGridAuditLogExportTrackerService extends AbstractGetConfigService<IGetGridAuditLogExportRequestModel, IGetGridAuditLogExportResponseModel>
    implements IGetGridAuditLogExportTrackerService  {
  
  @Override
  protected IGetGridAuditLogExportResponseModel executeInternal(
      IGetGridAuditLogExportRequestModel model) throws Exception
  {
    String userName = model.getUserName();
    List<IAuditLogExportEntryModel> auditLogExportModel = new ArrayList<>();
    IAuditLogExportDAO auditLogExportDAO = RDBMSUtils.newUserSessionDAO().newAuditLogExportDAO();
    List<IAuditLogExportDTO> auditLogExportDTOs = auditLogExportDAO.getAllAuditLogExportTrackers(userName, model.getSortOrder(), model.getSize(), model.getFrom());
    long count = auditLogExportDAO.getCount(userName);
    
    auditLogExportDTOs.forEach(auditLogExportDTO -> {
      prepareModelFromDTO(auditLogExportModel, auditLogExportDTO);
    });
    
    GetGridAuditLogExportResponseModel responseModel = new GetGridAuditLogExportResponseModel();
    responseModel.setAuditLogExportList(auditLogExportModel);
    responseModel.setCount(count);
    return responseModel;
  }
  
  private void prepareModelFromDTO(List<IAuditLogExportEntryModel> auditLogExportModel,
      IAuditLogExportDTO auditLogExportDTO)
  {
    IAuditLogExportEntryModel auditLogExportEntryModel = new AuditLogExportEntryModel();
    auditLogExportEntryModel.setAssetId(auditLogExportDTO.getAssetId());
    auditLogExportEntryModel.setStartTime(auditLogExportDTO.getStartTime());
    auditLogExportEntryModel.setEndTime(auditLogExportDTO.getEndTime());
    auditLogExportEntryModel.setUserName(auditLogExportDTO.getUserName());
    auditLogExportEntryModel.setFileName(auditLogExportDTO.getFileName());
    auditLogExportEntryModel.setStatus(auditLogExportDTO.getStatus());
    
    auditLogExportModel.add(auditLogExportEntryModel);
  }
}
