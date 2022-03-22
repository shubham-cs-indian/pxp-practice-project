package com.cs.core.csexpress.scope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.cs.core.csexpress.CSEBuilder;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.csexpress.coupling.CSECouplingSource;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.parser.csexpress.csexpressVisitor;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.ICSEElement.ElementType;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag.QualityFlag;
import com.cs.core.technical.icsexpress.calculation.ICSECalculation;
import com.cs.core.technical.icsexpress.calculation.ICSECalculation.Operator;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode.UnaryOperator;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByClassifierFilter.FilterOperator;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByPropertyFilter.PropertyFilter;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;

/**
 * @author vallee
 */
public class CSEEntityFilterBuilder extends CSEBuilder<ICSEEntityFilterNode>
        implements csexpressVisitor<ICSEEntityFilterNode> {

  private final Map<Integer, ICSECalculation.Operator> operatorsMap = new HashMap<>();
  private final Map<Integer, String> notPositionsMap = new HashMap<>();
  private final Map<Integer, csexpressParser.Entity_filterContext> filtersMap = new HashMap<>();
  private ICSEEntityFilterNode currentFilter = null;
  private boolean forwardNot = false;

  CSEEntityFilterBuilder(CSEParser rootParser) {
    super(rootParser);
  }

  /**
   * Split the current parser context into operators and operands
   *
   * @param ctx
   * @return the left most position of the operator with lowest precedence
   */
  private int splitIntoOperandsAndOperators(csexpressParser.Entity_scopeContext ctx) {
    int position = 0;
    int minPrecedenceOp = Integer.MAX_VALUE;
    int minPrecedencePostion = -1;
    String debug = String.format("%s (%d)", ctx.getText(), ctx.getChildCount());
    for (int i = 0; i < ctx.getChildCount(); i++) {
      ParseTree child = ctx.getChild(i);
      if (child instanceof TerminalNode) {
        continue;
      }
      if (child instanceof csexpressParser.Entity_filterContext) {
        filtersMap.put(position++, (csexpressParser.Entity_filterContext) child);
      } else if (child instanceof csexpressParser.Scope_operatorContext) {
        Operator operator = Operator.parseString(child.getText());
        if (operator.getPrecedence() < minPrecedenceOp) {
          minPrecedenceOp = operator.getPrecedence();
          minPrecedencePostion = position;
        }
        operatorsMap.put(position++, operator);
      } else if (child instanceof csexpressParser.Scope_notContext) {
        notPositionsMap.put(position++, UnaryOperator.parseString(child.getText())
                .toString());
      }
    }
    return minPrecedencePostion;
  }

  @Override
  public ICSEEntityFilterNode visitEntity_scope(csexpressParser.Entity_scopeContext ctx) {
    // Collect operands and operators by position
    int splitOperatorPosition = splitIntoOperandsAndOperators(ctx);
    if (operatorsMap.isEmpty()) { // Then the entity scope is reduced to a single operand
      visitChildren(ctx);
      if (forwardNot && currentFilter instanceof CSEEntityByPropertyFilter) {
        raisedExceptions.add("Unary operator not cannot be applied with contains");
      } else if (forwardNot && currentFilter instanceof CSEEntityByClassifierFilter
              && ((CSEEntityByClassifierFilter) currentFilter).containsNatureClass()) {
        raisedExceptions.add("Unary not operator cannot be applied with $nature");
      }
      if (forwardNot) {
        currentFilter.setNot(forwardNot || notPositionsMap.containsKey(0));
        forwardNot = false;
      }
      return currentFilter;
    } // else
    currentFilter = new CSECompoundEntityFilter();
    // left operand is all element below splitOperatorPosition
    StringBuffer leftExpression = new StringBuffer();
    for (int posLeft = 0; posLeft < splitOperatorPosition; posLeft++) {
      leftExpression.append(filtersMap.containsKey(posLeft) ? contextToString(filtersMap.get(posLeft)) + " " : "");
      leftExpression.append(operatorsMap.containsKey(posLeft) ? operatorsMap.get(posLeft).toString() + " " : "");
      leftExpression.append(notPositionsMap.containsKey(posLeft) ? notPositionsMap.get(posLeft) + " " : "");
    }
    // right operand is all element above splitOperatorPosition
    int lastRightPosition = filtersMap.size() + operatorsMap.size() + notPositionsMap.size() + 1;
    StringBuffer rightExpression = new StringBuffer();
    for (int posRight = splitOperatorPosition + 1; posRight < lastRightPosition; posRight++) {
      rightExpression.append(filtersMap.containsKey(posRight) ? contextToString(filtersMap.get(posRight)) + " " : "");
      rightExpression.append(operatorsMap.containsKey(posRight) ? operatorsMap.get(posRight).toString() + " " : "");
      rightExpression.append(notPositionsMap.containsKey(posRight) ? notPositionsMap.get(posRight) + " " : "");
    }
    try {
      ((CSECompoundEntityFilter) currentFilter).setLogicalOperator(operatorsMap.get(splitOperatorPosition));
      ((CSECompoundEntityFilter) currentFilter).setLeft(
              rootParser.newSubParser().parseEntityFilter(leftExpression.toString()));
      ((CSECompoundEntityFilter) currentFilter).setRight(
              rootParser.newSubParser().parseEntityFilter(rightExpression.toString()));
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentFilter;
  }

  @Override
  public ICSEEntityFilterNode visitScope_not(csexpressParser.Scope_notContext ctx) {
    forwardNot = true;
    return currentFilter;
  }

  @Override
  public ICSEEntityFilterNode visitEntity_filter(csexpressParser.Entity_filterContext ctx) {
    if (ctx.evaluated_filter() != null) {
      return visitChildren(ctx); // parse the included entity scope
    } else if (ctx.CONTAINS() != null) // identify a by property filter
    {
      currentFilter = new CSEEntityByPropertyFilter(PropertyFilter.contains);
    } else if (ctx.INVOLVES() != null) // identify a by property filter
    {
      currentFilter = new CSEEntityByPropertyFilter(PropertyFilter.involves);
    } else if (ctx.HAS() != null) // identify a by context filter
    {
      currentFilter = new CSEEntityByContextFilter();
    } else if (ctx.FQUALITY() != null) // identiy a by quality filter
    {
      currentFilter = new CSEEntityByQualityFilter();
    } else if (ctx.BELONGSTO() != null) // identiy a by relationship list filter
    {
      if(ctx.list_through_property() != null){
        currentFilter = new CSEEntityByRelationshipFilter();
      }
      else if(ctx.collection() != null){
        currentFilter = new CSEEntityByCollectionFilter();
      } 
     } else if (ctx.EXISTIN() != null) {
       currentFilter = new CSEEntityByTranslationFilter();
      
    }
    else if(ctx.FEXPIRED() != null){
      currentFilter = new CSEEntityByExpiryFilter();
    }
    else if(ctx.FDUPLICATE() != null){
      currentFilter = new CSEEntityByDuplicateFilter();
    }
    else { // by default a by classifier filter
      currentFilter = new CSEEntityByClassifierFilter();
      if (ctx.IN() != null) {
        ((CSEEntityByClassifierFilter) currentFilter).setOperator(FilterOperator.in);
      } else if (ctx.IS() != null) {
        ((CSEEntityByClassifierFilter) currentFilter).setOperator(FilterOperator.is);
      } else if (ctx.UNDER() != null) {
        ((CSEEntityByClassifierFilter) currentFilter).setOperator(FilterOperator.under);
      }
    }
    return visitChildren(ctx);
  }

  @Override
  public ICSEEntityFilterNode visitTarget_object(csexpressParser.Target_objectContext ctx) {
    ICSECouplingSource source;
    try {
      if (ctx.getChild(0) instanceof csexpressParser.ObjectContext) {
        CSEObject sourceObject = (CSEObject) rootParser.newSubParser().parseDefinition(contextToString(ctx));
        source = new CSECouplingSource(sourceObject);
      } else { // expected to be a predefined object
        source = new CSECouplingSource(ICSECouplingSource.Predefined.valueOf(ctx.getText()));
      }
      ((CSEEntityFilterNode) currentFilter).setObject(source);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    } catch (IllegalArgumentException ex) {
      raisedExceptions.add("Illegal predefined: " + ex.getMessage());
    }
    return currentFilter;
  }

  @Override
  public ICSEEntityFilterNode visitProperty(csexpressParser.PropertyContext ctx) {
    try {
      CSEProperty property = (CSEProperty) rootParser.newSubParser().parseDefinition(ctx.getText());
      ((CSEEntityByPropertyFilter) currentFilter).addProperty(property);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentFilter;
  }

  @Override
  public ICSEEntityFilterNode visitClassifier(csexpressParser.ClassifierContext ctx) {
    ICSECouplingSource classifier;
    try {
      if (ctx.getChild(0) instanceof csexpressParser.ObjectContext) {
        CSEObject classifierObject = (CSEObject) rootParser.newSubParser().parseDefinition(ctx.getText());
        if (classifierObject.getObjectType() != CSEObjectType.Classifier) {
          raisedExceptions.add("Found filter by classifier with non-classifier object: " + ctx.getText());
        }
        classifier = new CSECouplingSource(classifierObject);
      }
      else { // expected to be a predefined object
        String classifierType = ctx.getText();
        if(classifierType.equals("$empty")){
          ICSECouplingSource.Predefined predefined = ICSECouplingSource.Predefined.valueOf(classifierType);
          CSEObject cseObject = new CSEObject(ElementType.OBJECT, CSEObjectType.Classifier);
          cseObject.setCode(predefined.name());
          classifier = new CSECouplingSource(cseObject);

        }
        else{
          classifier = new CSECouplingSource(ICSECouplingSource.Predefined.valueOf(classifierType));
        }
      }
      ((CSEEntityByClassifierFilter) currentFilter).addClassifier(classifier);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentFilter;
  }

  @Override
  public ICSEEntityFilterNode visitList_through_property(csexpressParser.List_through_propertyContext ctx) {
    List<ParserRuleContext> children = getNonTerminalChildren(ctx);
    children.forEach(child -> {
      try {
        if (child instanceof csexpressParser.ObjectContext) {
          CSEObject owner = (CSEObject) rootParser.newSubParser().parseDefinition(contextToString(child));
          CSECouplingSource source = new CSECouplingSource(owner);
          ((CSEEntityByRelationshipFilter) currentFilter).setPropertyOwner(source);
        } else if (child instanceof csexpressParser.PredefinedobjectContext) {
          ICSECouplingSource.Predefined owner = ICSECouplingSource.Predefined.valueOf(contextToString(child));
          CSECouplingSource source = new CSECouplingSource(owner);
          ((CSEEntityByRelationshipFilter) currentFilter).setPropertyOwner(source);
        } else if (child instanceof csexpressParser.PropertyContext) {
          CSEProperty property = (CSEProperty) rootParser.newSubParser().parseDefinition(contextToString(child));
          ((CSEEntityByRelationshipFilter) currentFilter).setProperty(property);
        } else if (child instanceof csexpressParser.Property_fieldContext) {
          ICSERecordOperand.PropertyField field = ICSERecordOperand.PropertyField.valueOf(contextToString(child));
          if (field != ICSERecordOperand.PropertyField.complement) {
            raisedExceptions.add("Improper property field found in filter: " + contextToString(ctx));
          }
          ((CSEEntityByRelationshipFilter) currentFilter).setComplement(true);
        }
      } catch (CSFormatException ex) {
        raisedExceptions.add(ex.getMessage());
      }
    });
    return currentFilter;
  }

  @Override
  public ICSEEntityFilterNode visitContext(csexpressParser.ContextContext ctx) {
    CSEObject context;
    try {
      context = (CSEObject) rootParser.newSubParser().parseDefinition(ctx.getText());
      if (context.getObjectType() != CSEObjectType.Context) {
        raisedExceptions.add("Found filter by context with non-context object: " + ctx.getText());
      }
      ((CSEEntityByContextFilter) currentFilter).addContext(context);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentFilter;
  }

  @Override
  public ICSEEntityFilterNode visitQuality_level(csexpressParser.Quality_levelContext ctx) {
    ((CSEEntityByQualityFilter) currentFilter).addFlag(QualityFlag.valueOf(ctx.getText()));
    return currentFilter;
  }

  @Override
  public ICSEEntityFilterNode visitExpiry(csexpressParser.ExpiryContext ctx) {
    ((CSEEntityByExpiryFilter) currentFilter).setIsExpired(ctx.getText().equals("$true"));
    return currentFilter;
  }

  @Override
  public ICSEEntityFilterNode visitDuplicate(csexpressParser.DuplicateContext ctx) {
    ((CSEEntityByDuplicateFilter) currentFilter).setIsDuplicate(ctx.getText().equals("$true"));
    return currentFilter;
  }
  
  @Override
  public ICSEEntityFilterNode visitCollection(csexpressParser.CollectionContext ctx) {
    CSEObject collection;
    try {
      collection = (CSEObject) rootParser.newSubParser().parseDefinition(ctx.getText());
      if (collection.getObjectType() != CSEObjectType.Collection) {
        raisedExceptions.add("Found filter by collection with non-collection object: " + ctx.getText());
      }
      ((CSEEntityByCollectionFilter) currentFilter).setCollection(collection);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentFilter;
  }
  
  @Override
  public ICSEEntityFilterNode visitTranslation(csexpressParser.TranslationContext ctx) {
    CSEObject translation;
    try {
      translation = (CSEObject) rootParser.newSubParser().parseDefinition(ctx.getText());
      if (translation.getObjectType() != CSEObjectType.Translation) {
        raisedExceptions.add("Found filter by translation with non-translation object: " + ctx.getText());
      }
      ((CSEEntityByTranslationFilter) currentFilter).addTranslation(translation);;
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentFilter;
  }
}
