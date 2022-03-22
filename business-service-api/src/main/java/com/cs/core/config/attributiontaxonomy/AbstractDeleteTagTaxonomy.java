package com.cs.core.config.attributiontaxonomy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.config.standard.IConfigMap;
import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder.RootFilter;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.exception.delete.InstanceExistsForTaxonomyException;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

public abstract class AbstractDeleteTagTaxonomy<P extends IModel, R extends IConfigResponseWithAuditLogModel>
    extends AbstractDeleteConfigService<P, R> {
  
  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;
  
  protected abstract IBulkDeleteReturnModel executeDeleteAttributionTaxonomy(
      IIdsListParameterModel p) throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    List<String> taxonomyCodes = new ArrayList<String>();
    taxonomyCodes.addAll(((IIdsListParameterModel) model).getIds());
    List<Long> classifierIIDs = new ArrayList<>();
    for(String taxonomy :taxonomyCodes) {
      classifierIIDs.add( ConfigurationDAO.instance().getClassifierByCode(taxonomy).getClassifierIID());
    }
    
    if (!classifierIIDs.isEmpty()) {
      List<Long> baseentityiid = RDBMSUtils.getBaseentityIIDsForTaxonomies(classifierIIDs);
      
      ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
      ISearchDTOBuilder searchDTOBuilder = localeCatalogDao.getSearchDTOBuilder(IConfigMap.getAllBaseTypes(), RootFilter.BOTH, true);
      searchDTOBuilder.addTaxonomyFilters(taxonomyCodes.toArray(new String[] {}));
      ISearchDTO searchDTO = searchDTOBuilder.build();
      ISearchDAO searchDAO = localeCatalogDao.openSearchDAO(searchDTO);
      Long totalCountFromArchive = searchDAO.getCountForEntityUsage();
      
      if (!baseentityiid.isEmpty() || totalCountFromArchive > 0) {
        throw new InstanceExistsForTaxonomyException();
      }
    }
    
    IBulkDeleteReturnModel response = executeDeleteAttributionTaxonomy(
        (IIdsListParameterModel) model);

    List<String>  successIds = (List<String>) response.getSuccess();
    for(String id : successIds){
      ConfigurationDAO.instance().deleteClassifier(id);
    }
    IExceptionModel failure = response.getFailure();
    // TODO: Need to get this from rdbms call in future
    /*taxonomyIdsHavingInstances
    .forEach(typeId -> ExceptionUtil.addFailureDetailsToFailureObject(failure,
        new InstanceExistsForTaxonomyException(), typeId, null));*/
    
    return (R) response;
  }
}
