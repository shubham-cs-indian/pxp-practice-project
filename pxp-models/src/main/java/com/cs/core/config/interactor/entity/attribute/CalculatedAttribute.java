package com.cs.core.config.interactor.entity.attribute;

import com.cs.core.config.interactor.entity.datarule.AttributeOperator;
import com.cs.core.config.interactor.entity.datarule.IAttributeOperator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class CalculatedAttribute extends AbstractAttribute implements ICalculatedAttribute {

	private static final long serialVersionUID = 1L;

	protected Integer precision;

	protected List<IAttributeOperator> attributeOperatorList;

	protected String calculatedAttributeType;

	protected String calculatedAttributeUnit;

	protected String calculatedAttributeUnitAsHTML;

	protected Boolean hideSeparator = false;

	@Override
	public String getCalculatedAttributeUnitAsHTML() {
		return calculatedAttributeUnitAsHTML;
	}

	@Override
	public void setCalculatedAttributeUnitAsHTML(String calculatedAttributeUnitAsHTML) {
		this.calculatedAttributeUnitAsHTML = calculatedAttributeUnitAsHTML;
	}

	@Override
	public String getCalculatedAttributeType() {
		return calculatedAttributeType;
	}

	@Override
	public void setCalculatedAttributeType(String calculatedAttributeType) {
		this.calculatedAttributeType = calculatedAttributeType;
	}

	@Override
	public String getCalculatedAttributeUnit() {
		return calculatedAttributeUnit;
	}

	@Override
	public void setCalculatedAttributeUnit(String calculatedAttributeUnit) {
		this.calculatedAttributeUnit = calculatedAttributeUnit;
	}

	@Override
	public List<IAttributeOperator> getAttributeOperatorList() {
		if (attributeOperatorList == null) {
			attributeOperatorList = new ArrayList<>();
		}
		return attributeOperatorList;
	}

	@JsonDeserialize(contentAs = AttributeOperator.class)
	@Override
	public void setAttributeOperatorList(List<IAttributeOperator> attributeOperatorList) {
		this.attributeOperatorList = attributeOperatorList;
	}

	@Override
	public String getRendererType() {
		return Renderer.CALCULATED.name();
	}

	@Override
	public Integer getPrecision() {
		return precision;
	}

	@Override
	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

  @Override
  public Boolean getHideSeparator()
  {
    return this.hideSeparator;
  }
  
  @Override
  public void setHideSeparator(Boolean hideSeparator)
  {
    this.hideSeparator = hideSeparator;
  }
}
