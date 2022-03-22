package com.cs.core.config.interactor.model.smartdocument.preset;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPreset;
import com.cs.core.config.interactor.entity.smartdocument.preset.SmartDocumentPreset;

public class SmartDocumentPresetModel extends SmartDocumentPreset
    implements ISmartDocumentPresetModel {
  
  private static final long      serialVersionUID    = 1L;
  
  protected ISmartDocumentPreset smartDocumentPreset   = new SmartDocumentPreset();
  protected List<Long>           klassClassifierIds    = new ArrayList<>();
  protected List<Long>           taxonomyClassifierIds = new ArrayList<>();
  
  @Override
  public IEntity getEntity()
  {
    return smartDocumentPreset;
  }

  @Override
  public List<Long> getKlassClassifierIds()
  {
    return klassClassifierIds;
  }

  @Override
  public void setKlassClassifierIds(List<Long> klassClassifierIds)
  {
    this.klassClassifierIds = klassClassifierIds;
  }

  @Override
  public List<Long> getTaxonomyClassifierIds()
  {
    return taxonomyClassifierIds;
  }

  @Override
  public void setTaxonomyClassifierIds(List<Long> taxonomyClassifierIds)
  {
    this.taxonomyClassifierIds = taxonomyClassifierIds;
  }
}
