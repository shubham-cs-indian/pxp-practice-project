package com.cs.core.initialize;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.base.interactor.model.portal.IPortalModel;
import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipModel;
import com.cs.core.config.interactor.model.relationship.RelationshipModel;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.relationship.IBulkCreateRelationshipsStrategy;
import com.cs.core.config.strategy.usecase.translations.ICreateOrSaveRelationshipTranslationsStrategy;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class InitializeRelationshipsService implements IInitializeRelationshipsService {
  
  @Autowired
  protected IBulkCreateRelationshipsStrategy              bulkCreateRelationshipsStrategy;
  
  @Autowired
  protected ICreateOrSaveRelationshipTranslationsStrategy saveRelationshipTranslationsStrategy;
  
  @Autowired
  protected List<IPortalModel>                            portalModels;
  
  @Override
  public void execute() throws Exception
  {
    initializeRelationships();
    initializeRelationshipTranslations();
  }
  
  private void initializeRelationshipTranslations() throws Exception
  {
    ISaveRelationshipTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveRelationshipAndReferenceTranslationsRequestModel(
            InitializeDataConstants.RELATIONSHIP_TRANSLATIONS_JSON, CommonConstants.RELATIONSHIP);
    
    saveRelationshipTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void initializeRelationships() throws Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.RELATIONSHIPS_JSON);
    List<IRelationshipModel> relationships = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<RelationshipModel>>()
        {
        });
    stream.close();
    
    List<String> allowedEntities = new ArrayList<>();
    for (IPortalModel iPortalModel : portalModels) {
      allowedEntities.addAll(iPortalModel.getAllowedEntities());
    }
    
    InputStream klassIdStream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.ENTITY_KLASS_MAPPING_JSON);
    Map<String, String> klassIdMap = ObjectMapperUtil.readValue(klassIdStream,
        new TypeReference<Map<String, String>>()
        {
        });
    
    List<IRelationshipModel> relationshipsToInitialize = new ArrayList<>();
    // fetch configuration DAO
    IConfigurationDAO configurationDAO = RDBMSUtils.newConfigurationDAO();
    for (IRelationshipModel relationship : relationships) {
      IRelationshipSide side1 = relationship.getSide1();
      IRelationshipSide side2 = relationship.getSide2();
      
      String side1Entity = klassIdMap.get(side1.getKlassId());
      String side2Entity = klassIdMap.get(side2.getKlassId());
      ValidationUtils.validateEntityIdMapping(side1Entity, side2Entity);
      if (allowedEntities.contains(side1Entity) && allowedEntities.contains(side2Entity)) {
        relationshipsToInitialize.add(relationship);
        ValidationUtils.validateRelationship(relationship);
        
      }
      configurationDAO.createStandardProperty(relationship.getPropertyIID(), relationship.getCode(),
          PropertyType.RELATIONSHIP);
    }
    
    IListModel<IRelationshipModel> relationshipsListModel = new ListModel<>(
        relationshipsToInitialize);
    bulkCreateRelationshipsStrategy.execute(relationshipsListModel);
  }
}
