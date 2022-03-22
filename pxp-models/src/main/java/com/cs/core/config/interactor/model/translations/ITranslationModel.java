package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Arrays;
import java.util.List;

public interface ITranslationModel extends IModel {
  
  public static final String       LABEL              = "label";
  public static final String       PLACEHOLDER        = "placeholder";
  public static final String       DESCRIPTION        = "description";
  public static final String       TOOLTIP            = "tooltip";
  
  public static final List<String> TRNASLATION_FIELDS = Arrays.asList(LABEL, PLACEHOLDER,
      DESCRIPTION, TOOLTIP);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getPlaceholder();
  
  public void setPlaceholder(String placeholder);
  
  public String getDescription();
  
  public void setDescription(String description);
  
  public String getTooltip();
  
  public void setTooltip(String tooltip);
}
