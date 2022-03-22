package com.cs.core.config.auditlog;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.auditlog.IHouseKeepingRequestModel;
import com.cs.core.rdbms.auditlog.idao.IAuditLogExportDAO;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service public class AuditLogExportHouseKeepingService extends AbstractGetConfigService<IHouseKeepingRequestModel, IModel>
    implements IAuditLogExportHouseKeepingService {
  
  @Autowired IDeleteAuditLogExportService deleteAuditLogExportService;

  @Override
  protected IModel executeInternal(IHouseKeepingRequestModel model) throws Exception
  {
    IAuditLogExportDAO auditLogExportDAO = RDBMSUtils.newUserSessionDAO().newAuditLogExportDAO();
    List<String> assetIds = auditLogExportDAO
        .getAuditLogExportTrackerAssetIdsForHouseKeeping(getHouseKeepingTime(new Date(), -model.getOffset()));    
    
    if (!assetIds.isEmpty()) {
      IIdsListParameterModel requestModel = new IdsListParameterModel();
      requestModel.setIds(assetIds);
      deleteAuditLogExportService.execute(requestModel);
    }
    return new VoidModel();
  }
  
  private Long getHouseKeepingTime(Date date, Integer days)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, days);
    return cal.getTime().getTime();
  }
  
}
