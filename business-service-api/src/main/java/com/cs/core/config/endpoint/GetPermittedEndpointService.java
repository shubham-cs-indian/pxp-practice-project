package com.cs.core.config.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEndpointPaginationModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.interactor.model.organization.IReferencedEndpointModel;
import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.permission.IGetPermittedEndpointService;
import com.cs.core.config.strategy.usecase.configdata.IGetConfigDataStrategy;
import com.cs.core.config.strategy.usecase.organization.IGetOrganizationStrategy;
import com.cs.core.config.strategy.usecase.role.IGetRoleByUserStrategy;
import com.cs.core.config.strategy.usecase.role.IGetRoleStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;

@Service
public class GetPermittedEndpointService extends AbstractGetConfigService<IGetConfigDataRequestModel, IGetConfigDataResponseModel>
    implements IGetPermittedEndpointService {
  
  @Autowired
  protected IGetOrganizationStrategy getOrganizationStrategy;
  
  @Autowired
  protected IGetConfigDataStrategy   getConfigDataStrategy;
  
  @Autowired
  protected IGetRoleStrategy         getRoleStrategy;
  
  @Autowired
  protected ISessionContext          context;
  
  @Autowired
  IGetRoleByUserStrategy             getGetRoleByUserStrategy;
  
  @Override
  public IGetConfigDataResponseModel executeInternal(IGetConfigDataRequestModel dataModel) throws Exception
  {
    IGetConfigDataResponseModel response = null;
    List<IConfigEntityInformationModel> filteredEndPonits = new ArrayList<IConfigEntityInformationModel>();
    IGetConfigDataEndpointPaginationModel endpoint = dataModel.getEntities().getEndpoints();
    List<String> epLinkedToOrgList = fetchEndpointsLinkedToOrg(endpoint.getOrganizationId());
    List<String> epLinkedToRoleList = fetchEndpointsLinkedToOrgRole();
    
    // Fetch all endpoint List
    response = getConfigDataStrategy.execute(dataModel);
    List<IConfigEntityInformationModel> allEndPonits = response.getEndpoints().getList();
    
    // get the final list by comparing EP permitted for Role to all EP
    if (!epLinkedToRoleList.isEmpty()) {
      filteredEndPonits = allEndPonits.stream().filter(ep -> epLinkedToRoleList.contains(ep.getCode())).collect(Collectors.toList());
      // Map the filtered List in response
      response.getEndpoints().setList(filteredEndPonits);
    }
    // get the final list by comparing EP permitted for Organization to all EP
    else if (!epLinkedToOrgList.isEmpty()) {
      filteredEndPonits = allEndPonits.stream().filter(ep -> epLinkedToOrgList.contains(ep.getCode())).collect(Collectors.toList());
      // Map the filtered List in response
      response.getEndpoints().setList(filteredEndPonits);
    }
    
    return response;
  }
  
  /**
   * Get the list of EP which is linked to the current organization
   * 
   * @param dataModel
   * @return
   * @throws Exception
   */
  private List<String> fetchEndpointsLinkedToOrg(String id) throws Exception
  {
    List<String> epLinkedToOrgList = new ArrayList<String>();
    IIdParameterModel getEntityModel = new IdParameterModel(id);
    // Fetch EP linked to the organization
    IGetOrganizationModel organizationResponse = getOrganizationStrategy.execute(getEntityModel);
    Map<String, IReferencedEndpointModel> epLinkedToOrg = organizationResponse.getReferencedEndpoints();
    if (epLinkedToOrg != null && !epLinkedToOrg.isEmpty()) {
      epLinkedToOrg.values().forEach(entry -> {
        epLinkedToOrgList.add(entry.getCode());
      });
    }
    return epLinkedToOrgList;
  }
  
  /**
   * Get the list of EP which is linked to the role
   * 
   * @param role
   * @return
   * @throws Exception
   */
  private List<String> fetchEndpointsLinkedToOrgRole() throws Exception
  {
    List<String> epLinkedToRoleList = new ArrayList<String>();
    String role = getRoleLinkedToUser(context.getUserId());
    if (role != null && !role.isEmpty()) {
      IIdParameterModel getEntityModel = new IdParameterModel(role);
      // Fetch EP linked to the roles
      IGetRoleStrategyModel roleResponse = getRoleStrategy.execute(getEntityModel);
      Map<String, IConfigEntityInformationModel> epLinkedToRole = roleResponse.getReferencedEndpoints();
      if (epLinkedToRole != null && !epLinkedToRole.isEmpty()) {
        epLinkedToRole.values().forEach(entry -> {
          epLinkedToRoleList.add(entry.getCode());
        });
      }
    }
    return epLinkedToRoleList;
  }
  
  /**
   * Get Role linked with the userId
   * 
   * @param userId
   * @return
   * @throws Exception
   */
  private String getRoleLinkedToUser(String userId) throws Exception
  {
    IIdParameterModel model = new IdParameterModel();
    model.setId(userId);
    IIdsListParameterModel roles = getGetRoleByUserStrategy.execute(model);
    String role = roles.getIds().isEmpty() ? null : roles.getIds().get(0);
    return role;
  }
}
