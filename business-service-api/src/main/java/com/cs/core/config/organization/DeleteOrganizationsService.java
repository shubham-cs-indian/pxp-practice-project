package com.cs.core.config.organization;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.BGPDeletePartnersDTO;
import com.cs.core.bgprocess.dto.IBGPDeletePartnersDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.config.strategy.usecase.organization.IDeleteOrganizationStrategy;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeleteOrganizationsService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteOrganizationService {

  @Autowired
  protected IDeleteOrganizationStrategy deleteOrganizationStrategy;
  
  @Autowired
  protected IGetEntityConfigurationStrategy      getPartnerEntityConfigurationStrategy;

  @Autowired
  protected RDBMSComponentUtils                  rdbmsComponentUtils;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel templateModel) throws Exception
  {
    List<String> partnerIdsToBeDeleted = templateModel.getIds();
    IGetEntityConfigurationResponseModel getEntityResponse = getPartnerEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(partnerIdsToBeDeleted));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();

    if (!referenceData.keySet().isEmpty()) {
      throw new EntityConfigurationDependencyException();
    }

    IBulkDeleteReturnModel execute = deleteOrganizationStrategy.execute(templateModel);

    List<String> successDeletedIds= (List<String>) execute.getSuccess();
    deleteSupplierInstances(successDeletedIds);
    return execute;
  }

  private void deleteSupplierInstances(List<String> deletedPartnersList) throws Exception
  {
    if(deletedPartnersList.isEmpty()) {
      return;
    }
    ILocaleCatalogDTO localeCatalogDTO = rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO();
    IBGPDeletePartnersDTO deletedPartnersDTO = new BGPDeletePartnersDTO();
    deletedPartnersDTO.setDeletedPartnersList(deletedPartnersList);
    deletedPartnersDTO.setLocaleID(localeCatalogDTO.getLocaleID());
    deletedPartnersDTO.setCatalogCode(localeCatalogDTO.getCatalogCode());

    BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), "DELETE_PARTNERS", "",
        IBGProcessDTO.BGPPriority.LOW, new JSONContent(deletedPartnersDTO.toJSON()));
  }


}
