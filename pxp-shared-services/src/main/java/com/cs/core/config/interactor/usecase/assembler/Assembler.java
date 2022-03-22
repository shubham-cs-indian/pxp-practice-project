package com.cs.core.config.interactor.usecase.assembler;

import com.cs.core.data.Text;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.calculation.ICSECalculation.Operator;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByClassifierFilter.FilterOperator;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class Assembler {
  
  public String getElementExpression(CSEObjectType objectType, String code)
  {
    return String.format("[%s>%s]", objectType.letter(), code);
  }

  protected String getEntityExpression(FilterOperator filter, String codes, boolean negateOperation)
  {
    if(negateOperation) {
      return String.format(" not %s %s %s ", Keyword.$entity, filter.name(), codes);
    }
    return String.format(" %s %s %s ", Keyword.$entity, filter.name(), codes);
  }
  
  public String scopeForLocale(List<String> languages)
  {
    String localeScope = Text.join(" | ", languages);
    return !localeScope.isEmpty() ? String.format(" %s %s %s ", Keyword.$locale, Operator.Equals.getSymbol(), localeScope) : "";
  }

  public String scopeForCatalog(List<String> physicalCatalogIds)
  {
    String catalogScope = Text.join(" | ", physicalCatalogIds);
    return String.format(" %s %s %s ", Keyword.$ctlg, Operator.Equals.getSymbol(), catalogScope);
  }
  
  public String fillClassifierExpressions(Collection<String> types, FilterOperator filterOperator) 
  {
    return fillClassifierExpressions(types, filterOperator, false);
  }

  public String fillClassifierExpressions(Collection<String> types, FilterOperator filterOperator, boolean negateOperation)
  {
    String typeScope = Text.join(" | ", types, getElementExpression(CSEObjectType.Classifier, "%s")); 
      return typeScope.isEmpty() ? "" : getEntityExpression(filterOperator, typeScope, negateOperation);
  }
  
  protected String expressionForRange(String entityExpression, String type, String from, String to)
  { 
    StringBuilder expressionForValues =  new StringBuilder();
    switch(type) {
        
      case "range":
    	  expressionForValues.append(String.format(" (%s >= %s and %s <= %s) ", entityExpression, from, entityExpression, to));  
        break;
      // String
      case "length_range":
    	expressionForValues.append(String.format(" (%s.length > %s and %s.length < %s) ", entityExpression, from, entityExpression, to));  
    	break;
      
      default:
        break;
    }
    return expressionForValues.toString();
  }

  public String nonValueExpressions(String entityExpression, String type)
  {
    switch(type) {
      case "empty":
    	return String.format(" %s = $null ", entityExpression);
      case "notempty":
        return String.format(" %s <> $null", entityExpression);
      case "isunique":
        return String.format(" unique (%s) ", entityExpression);
      default: 
        return "";
    }
  }

protected String attributeConditionalExpression(String value, String type, String entityExpression) {
	StringBuilder expressionForValues = new StringBuilder();
	switch (type) {
	  
	  case "exact":
	    
		if(value.isBlank() || value.isEmpty()) {
			expressionForValues.append(String.format(" %s = $null ", entityExpression));
		}else {
			expressionForValues.append(String.format(" %s = %s ", entityExpression, value));
		}
	    break;
	  
	  // number, StringentityId
	  case "contains":
		if(value.isBlank() || value.isEmpty()) {
			expressionForValues.append(String.format(" %s = $null ", entityExpression));
		}else {
			expressionForValues.append(String.format(" %s contains %s ", entityExpression,  value ));
		}
		break;
	  
	  case "start":
		
		if(value.isBlank() || value.isEmpty()) {
			expressionForValues.append(String.format(" %s = $null ", entityExpression));
		}else {
			expressionForValues.append(String.format(" %s like %s ", entityExpression, String.format("'%s%%'", value.substring(1, (value.length()-1)))));
		}

	  	break;
	  
	  case "end":
		  
		if (value.isBlank() || value.isEmpty()) {
			expressionForValues.append(String.format(" %s = $null ", entityExpression));
		} else {
			expressionForValues.append(String.format(" %s like %s ", entityExpression, String.format("'%%%s'", value.substring(1, (value.length() - 1)))));
		}
		break;
	  
	  // Number, Date:
	  case "lt":
	    expressionForValues.append(String.format(" %s < %s ", entityExpression, value.isBlank() || value.isEmpty() ? 0 : value));
	    break;
	  
	  case "gt":
	    expressionForValues.append(String.format(" %s > %s ", entityExpression, value.isBlank() || value.isEmpty() ? 0 : value));
	    break;
	
      case "lte":
		expressionForValues.append(String.format(" %s <= %s ", entityExpression, value.isBlank() || value.isEmpty() ? 0 : value));
		break;

	  case "gte":
		expressionForValues.append(String.format(" %s >= %s ", entityExpression, value.isBlank() || value.isEmpty() ? 0 : value));
		break;

      case "length_lt":
		expressionForValues.append(String.format(" (%s.length < %s) ", entityExpression, value.isBlank() || value.isEmpty() ? 0 : value));
		break;

	  case "length_gt":
		expressionForValues.append(String.format(" (%s.length > %s) ", entityExpression, value.isBlank() || value.isEmpty() ? 0 : value));  
	    break;
	  
	  // check switch case for this usecase
	  case "length_equal":
		  expressionForValues.append(String.format("(%s.length = %s)", entityExpression, value.isBlank() || value.isEmpty() ? 0 : value));  
	    break;
	  case "regex":
	    if(value.isBlank() || value.isEmpty()) {
	      expressionForValues.append(String.format(" %s regex $null ", entityExpression));
	    }else {
	      expressionForValues.append(String.format(" %s regex %s ", entityExpression, value));
	    }
      break;
	  default:
		  break;
	}
	return expressionForValues.toString();
}
  
  protected String getTagConditionExpression(String entityExpression, String type, String tagValuesExpression)
  {
    StringBuilder expressionForValues = new StringBuilder();
    switch (type) {
      case "empty":
        expressionForValues.append(String.format(" %s = $null ", entityExpression));
        break;
      case "notempty":
        expressionForValues.append(String.format(" %s <> $null ", entityExpression));
        break; 
      case "contains":
    	  if(tagValuesExpression.isEmpty() || tagValuesExpression.equals("{}")) {
    		  expressionForValues.append(String.format(" %s = $null ", entityExpression));
    	  }else {
    		  expressionForValues.append(String.format(" %s contains %s ", entityExpression, tagValuesExpression));  
    	  }
      	break;
      case "exact":
    	  if(tagValuesExpression.isEmpty() || tagValuesExpression.equals("{}")) {
    		  expressionForValues.append(String.format(" %s = $null ", entityExpression));
    	  }else {
    		  expressionForValues.append(String.format("%s = %s", entityExpression, tagValuesExpression));
    	  }
        break;
      case "notcontains":
        if(tagValuesExpression.isEmpty() || tagValuesExpression.equals("{}")) {
          expressionForValues.append(String.format(" %s = $null ", entityExpression));
        }else {
          expressionForValues.append(String.format(" %s notcontains %s ", entityExpression, tagValuesExpression));  
        }
        break;
    }
    return expressionForValues.toString();
  }

	public String scopeForOrganizations(List<String> organizations)
	{
	  if(organizations.isEmpty()){
	    return "";
	  }
		if(organizations.contains("-1")){
			organizations.remove("-1");
			organizations.add("$stdo");
		}
		String organizationScope = Text.join(" | ", organizations);
		return String.format(" %s %s %s ", Keyword.$org, Operator.Equals.getSymbol(), organizationScope);
	}
}


