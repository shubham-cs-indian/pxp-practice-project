package com.cs.di.config.service;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.di.common.test.DiIntegrationTestConfig;
import com.cs.di.config.interactor.authorization.IBulkSavePartnerAuthorization;
import com.cs.di.config.interactor.authorization.ICreatePartnerAuthorization;
import com.cs.di.config.interactor.authorization.IDeletePartnerAuthorization;
import com.cs.di.config.interactor.authorization.IGetAllPartnerAuthorization;
import com.cs.di.config.interactor.authorization.IGetPartnerAuthorization;
import com.cs.di.config.interactor.authorization.ISavePartnerAuthorization;
import com.cs.di.config.model.authorization.IBulkSavePartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IGetAllPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.ISavePartnerAuthorizationModel;
import com.cs.di.config.model.authorization.PartnerAuthorizationModel;
import com.cs.di.config.model.authorization.SavePartnerAuthorizationModel;

public class PartnerAuthorizationServicesTest extends DiIntegrationTestConfig{
  
  @Autowired
  protected ICreatePartnerAuthorization   createAuthorizationMapping;
  
  @Autowired
  protected ISavePartnerAuthorization     savePartnerAuthorizationService;
  
  @Autowired
  protected IBulkSavePartnerAuthorization bulkSaveAuthorizationMapping;
  
  @Autowired
  protected IGetPartnerAuthorization      getAuthorizationMapping;
  
  @Autowired
  protected IGetAllPartnerAuthorization   getAllAuthorizationMappings;
  
  @Autowired
  protected IDeletePartnerAuthorization   deleteAuthorizationMapping;
  

  /**
   * Test create operation of authorization.
   * 
   * @throws IOException
   */
  @Test
  public void createPartnerAuthorizationTest() throws IOException
  {
    IPartnerAuthorizationModel model = new PartnerAuthorizationModel();
    model.setId("123");
    model.setCode("testmapping");
    model.setLabel("Test Mapping");
   
    IGetPartnerAuthorizationModel response = null;
    try {
      response = createAuthorizationMapping.execute(model);
    }
    catch (Exception e) {
      response = null;
    }
    assertTrue(response != null);
  }
  
  /**
   * Test save operation of authorization.
   * 
   * @throws IOException
   */
  @Test
  public void savePartnerAuthorizationTest() throws IOException
  {
    ISavePartnerAuthorizationModel model = new SavePartnerAuthorizationModel();
    model.setId("testmapping");
    model.setCode("testmapping");
    model.setLabel("123");
    model.setType("com.cs.di.config.model.authorization.PartnerAuthorizationModel");
    model.setAddedAttributeMappings(Arrays.asList("nameattribute"));
    model.setAddedClassMappings(Arrays.asList());
    model.setAddedContextMappings(Arrays.asList());
    model.setAddedRelationshipMappings(Arrays.asList());
    model.setAddedTagMappings(Arrays.asList());
    model.setAddedTaxonomyMappings(Arrays.asList());
    
    model.setDeletedAttributeMappings(Arrays.asList());
    model.setDeletedClassMappings(Arrays.asList());
    model.setDeletedContextMappings(Arrays.asList());
    model.setDeletedRelationshipMappings(Arrays.asList());
    model.setDeletedTagMappings(Arrays.asList());
    model.setDeletedTaxonomyMappings(Arrays.asList());
   
    IGetPartnerAuthorizationModel response = null;
    try {
      response = savePartnerAuthorizationService.execute(model);
    }
    catch (Exception e) {
      response = null;
    }
    assertTrue(response != null);
    
  }
  
  /**
   * Test bulk save operation of authorization.
   * 
   * @throws IOException
   */
  @Test
  public void BulkSavePartnerAuthorizationTest() throws IOException
  {
    IListModel<IIdLabelCodeModel> model = new ListModel<IIdLabelCodeModel>();
    IIdLabelCodeModel idLabelCodeModel= new IdLabelCodeModel();
    idLabelCodeModel.setId("testmapping");
    idLabelCodeModel.setCode("testmapping");
    idLabelCodeModel.setLabel("1234");
   
    model.setList(Arrays.asList(idLabelCodeModel));
   
    IBulkSavePartnerAuthorizationModel response = null;
    try {
      response = bulkSaveAuthorizationMapping.execute(model);
    }
    catch (Exception e) {
      response = null;
    }
    assertTrue(response != null);
  }
  
  /**
   * Test Get operation of authorization.
   * 
   * @throws IOException
   */
  @Test
  public void GetPartnerAuthorizationTest() throws IOException
  {
    IIdParameterModel model = new IdParameterModel("testmapping");
   
    IGetPartnerAuthorizationModel response = null;
    try {
      response = getAuthorizationMapping.execute(model);
    }
    catch (Exception e) {
      response = null;
    }
    assertTrue(response != null);
  }
  
  /**
   * Test get all operation of authorization.
   * 
   * @throws IOException
   */
  @Test
  public void GetAllPartnerAuthorizationTest() throws IOException
  {
    IConfigGetAllRequestModel model = new ConfigGetAllRequestModel();
    model.setSearchColumn("label");
    model.setSearchText("");
    model.setSize(20l);
    model.setSortOrder("asc");
    model.setFrom(0l);
    
    IGetAllPartnerAuthorizationModel response = null;
    try {
      response = getAllAuthorizationMappings.execute(model);
    }
    catch (Exception e) {
      response = null;
    }
    assertTrue(response != null);
  }
  
  /**
   * Test delete operation of authorization.
   * 
   * @throws IOException
   */
  @Test
  public void DeletePartnerAuthorizationTest() throws IOException
  {
    IIdsListParameterModel model = new IdsListParameterModel();
    model.setIds(Arrays.asList("testmapping"));
    
   
    IBulkDeleteReturnModel response = null;
    try {
      response = deleteAuthorizationMapping.execute(model);
    }
    catch (Exception e) {
      response = null;
    }
    assertTrue(response != null);
  }
}
