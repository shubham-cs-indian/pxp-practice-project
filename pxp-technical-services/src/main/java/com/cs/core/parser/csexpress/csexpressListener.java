// Generated from csexpress\csexpress.g4 by ANTLR 4.7

    package com.cs.core.parser.csexpress;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link csexpressParser}.
 */
public interface csexpressListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link csexpressParser#search_expression}.
	 * @param ctx the parse tree
	 */
	void enterSearch_expression(csexpressParser.Search_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#search_expression}.
	 * @param ctx the parse tree
	 */
	void exitSearch_expression(csexpressParser.Search_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#rule_expression}.
	 * @param ctx the parse tree
	 */
	void enterRule_expression(csexpressParser.Rule_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#rule_expression}.
	 * @param ctx the parse tree
	 */
	void exitRule_expression(csexpressParser.Rule_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#calculation}.
	 * @param ctx the parse tree
	 */
	void enterCalculation(csexpressParser.CalculationContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#calculation}.
	 * @param ctx the parse tree
	 */
	void exitCalculation(csexpressParser.CalculationContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#scope}.
	 * @param ctx the parse tree
	 */
	void enterScope(csexpressParser.ScopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#scope}.
	 * @param ctx the parse tree
	 */
	void exitScope(csexpressParser.ScopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#basetype_scope}.
	 * @param ctx the parse tree
	 */
	void enterBasetype_scope(csexpressParser.Basetype_scopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#basetype_scope}.
	 * @param ctx the parse tree
	 */
	void exitBasetype_scope(csexpressParser.Basetype_scopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#entity_basetypes}.
	 * @param ctx the parse tree
	 */
	void enterEntity_basetypes(csexpressParser.Entity_basetypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#entity_basetypes}.
	 * @param ctx the parse tree
	 */
	void exitEntity_basetypes(csexpressParser.Entity_basetypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#basetype}.
	 * @param ctx the parse tree
	 */
	void enterBasetype(csexpressParser.BasetypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#basetype}.
	 * @param ctx the parse tree
	 */
	void exitBasetype(csexpressParser.BasetypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#catalog_scope}.
	 * @param ctx the parse tree
	 */
	void enterCatalog_scope(csexpressParser.Catalog_scopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#catalog_scope}.
	 * @param ctx the parse tree
	 */
	void exitCatalog_scope(csexpressParser.Catalog_scopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#catalogs}.
	 * @param ctx the parse tree
	 */
	void enterCatalogs(csexpressParser.CatalogsContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#catalogs}.
	 * @param ctx the parse tree
	 */
	void exitCatalogs(csexpressParser.CatalogsContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#organization_scope}.
	 * @param ctx the parse tree
	 */
	void enterOrganization_scope(csexpressParser.Organization_scopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#organization_scope}.
	 * @param ctx the parse tree
	 */
	void exitOrganization_scope(csexpressParser.Organization_scopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#organizations}.
	 * @param ctx the parse tree
	 */
	void enterOrganizations(csexpressParser.OrganizationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#organizations}.
	 * @param ctx the parse tree
	 */
	void exitOrganizations(csexpressParser.OrganizationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#organization_code}.
	 * @param ctx the parse tree
	 */
	void enterOrganization_code(csexpressParser.Organization_codeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#organization_code}.
	 * @param ctx the parse tree
	 */
	void exitOrganization_code(csexpressParser.Organization_codeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#catalog_code}.
	 * @param ctx the parse tree
	 */
	void enterCatalog_code(csexpressParser.Catalog_codeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#catalog_code}.
	 * @param ctx the parse tree
	 */
	void exitCatalog_code(csexpressParser.Catalog_codeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#locale_scope}.
	 * @param ctx the parse tree
	 */
	void enterLocale_scope(csexpressParser.Locale_scopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#locale_scope}.
	 * @param ctx the parse tree
	 */
	void exitLocale_scope(csexpressParser.Locale_scopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#locales}.
	 * @param ctx the parse tree
	 */
	void enterLocales(csexpressParser.LocalesContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#locales}.
	 * @param ctx the parse tree
	 */
	void exitLocales(csexpressParser.LocalesContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#locale_code}.
	 * @param ctx the parse tree
	 */
	void enterLocale_code(csexpressParser.Locale_codeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#locale_code}.
	 * @param ctx the parse tree
	 */
	void exitLocale_code(csexpressParser.Locale_codeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#iid_scope}.
	 * @param ctx the parse tree
	 */
	void enterIid_scope(csexpressParser.Iid_scopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#iid_scope}.
	 * @param ctx the parse tree
	 */
	void exitIid_scope(csexpressParser.Iid_scopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#entity_iids}.
	 * @param ctx the parse tree
	 */
	void enterEntity_iids(csexpressParser.Entity_iidsContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#entity_iids}.
	 * @param ctx the parse tree
	 */
	void exitEntity_iids(csexpressParser.Entity_iidsContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#entity_iid}.
	 * @param ctx the parse tree
	 */
	void enterEntity_iid(csexpressParser.Entity_iidContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#entity_iid}.
	 * @param ctx the parse tree
	 */
	void exitEntity_iid(csexpressParser.Entity_iidContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#endpoint_scope}.
	 * @param ctx the parse tree
	 */
	void enterEndpoint_scope(csexpressParser.Endpoint_scopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#endpoint_scope}.
	 * @param ctx the parse tree
	 */
	void exitEndpoint_scope(csexpressParser.Endpoint_scopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#endpoints}.
	 * @param ctx the parse tree
	 */
	void enterEndpoints(csexpressParser.EndpointsContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#endpoints}.
	 * @param ctx the parse tree
	 */
	void exitEndpoints(csexpressParser.EndpointsContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#endpoint_code}.
	 * @param ctx the parse tree
	 */
	void enterEndpoint_code(csexpressParser.Endpoint_codeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#endpoint_code}.
	 * @param ctx the parse tree
	 */
	void exitEndpoint_code(csexpressParser.Endpoint_codeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#entity_scope}.
	 * @param ctx the parse tree
	 */
	void enterEntity_scope(csexpressParser.Entity_scopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#entity_scope}.
	 * @param ctx the parse tree
	 */
	void exitEntity_scope(csexpressParser.Entity_scopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#entity_filter}.
	 * @param ctx the parse tree
	 */
	void enterEntity_filter(csexpressParser.Entity_filterContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#entity_filter}.
	 * @param ctx the parse tree
	 */
	void exitEntity_filter(csexpressParser.Entity_filterContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#scope_not}.
	 * @param ctx the parse tree
	 */
	void enterScope_not(csexpressParser.Scope_notContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#scope_not}.
	 * @param ctx the parse tree
	 */
	void exitScope_not(csexpressParser.Scope_notContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#scope_operator}.
	 * @param ctx the parse tree
	 */
	void enterScope_operator(csexpressParser.Scope_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#scope_operator}.
	 * @param ctx the parse tree
	 */
	void exitScope_operator(csexpressParser.Scope_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#list_through_property}.
	 * @param ctx the parse tree
	 */
	void enterList_through_property(csexpressParser.List_through_propertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#list_through_property}.
	 * @param ctx the parse tree
	 */
	void exitList_through_property(csexpressParser.List_through_propertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#evaluated_filter}.
	 * @param ctx the parse tree
	 */
	void enterEvaluated_filter(csexpressParser.Evaluated_filterContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#evaluated_filter}.
	 * @param ctx the parse tree
	 */
	void exitEvaluated_filter(csexpressParser.Evaluated_filterContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#context}.
	 * @param ctx the parse tree
	 */
	void enterContext(csexpressParser.ContextContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#context}.
	 * @param ctx the parse tree
	 */
	void exitContext(csexpressParser.ContextContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#collection}.
	 * @param ctx the parse tree
	 */
	void enterCollection(csexpressParser.CollectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#collection}.
	 * @param ctx the parse tree
	 */
	void exitCollection(csexpressParser.CollectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#expiry}.
	 * @param ctx the parse tree
	 */
	void enterExpiry(csexpressParser.ExpiryContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#expiry}.
	 * @param ctx the parse tree
	 */
	void exitExpiry(csexpressParser.ExpiryContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#duplicate}.
	 * @param ctx the parse tree
	 */
	void enterDuplicate(csexpressParser.DuplicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#duplicate}.
	 * @param ctx the parse tree
	 */
	void exitDuplicate(csexpressParser.DuplicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#translation}.
	 * @param ctx the parse tree
	 */
	void enterTranslation(csexpressParser.TranslationContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#translation}.
	 * @param ctx the parse tree
	 */
	void exitTranslation(csexpressParser.TranslationContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#action_list}.
	 * @param ctx the parse tree
	 */
	void enterAction_list(csexpressParser.Action_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#action_list}.
	 * @param ctx the parse tree
	 */
	void exitAction_list(csexpressParser.Action_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#action}.
	 * @param ctx the parse tree
	 */
	void enterAction(csexpressParser.ActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#action}.
	 * @param ctx the parse tree
	 */
	void exitAction(csexpressParser.ActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#evaluated_action}.
	 * @param ctx the parse tree
	 */
	void enterEvaluated_action(csexpressParser.Evaluated_actionContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#evaluated_action}.
	 * @param ctx the parse tree
	 */
	void exitEvaluated_action(csexpressParser.Evaluated_actionContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#target_property}.
	 * @param ctx the parse tree
	 */
	void enterTarget_property(csexpressParser.Target_propertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#target_property}.
	 * @param ctx the parse tree
	 */
	void exitTarget_property(csexpressParser.Target_propertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#target_object}.
	 * @param ctx the parse tree
	 */
	void enterTarget_object(csexpressParser.Target_objectContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#target_object}.
	 * @param ctx the parse tree
	 */
	void exitTarget_object(csexpressParser.Target_objectContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#classifier}.
	 * @param ctx the parse tree
	 */
	void enterClassifier(csexpressParser.ClassifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#classifier}.
	 * @param ctx the parse tree
	 */
	void exitClassifier(csexpressParser.ClassifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#predefinedclassifier}.
	 * @param ctx the parse tree
	 */
	void enterPredefinedclassifier(csexpressParser.PredefinedclassifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#predefinedclassifier}.
	 * @param ctx the parse tree
	 */
	void exitPredefinedclassifier(csexpressParser.PredefinedclassifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#quality_level}.
	 * @param ctx the parse tree
	 */
	void enterQuality_level(csexpressParser.Quality_levelContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#quality_level}.
	 * @param ctx the parse tree
	 */
	void exitQuality_level(csexpressParser.Quality_levelContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#evaluation_expression}.
	 * @param ctx the parse tree
	 */
	void enterEvaluation_expression(csexpressParser.Evaluation_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#evaluation_expression}.
	 * @param ctx the parse tree
	 */
	void exitEvaluation_expression(csexpressParser.Evaluation_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#operand}.
	 * @param ctx the parse tree
	 */
	void enterOperand(csexpressParser.OperandContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#operand}.
	 * @param ctx the parse tree
	 */
	void exitOperand(csexpressParser.OperandContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#evaluated_operand}.
	 * @param ctx the parse tree
	 */
	void enterEvaluated_operand(csexpressParser.Evaluated_operandContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#evaluated_operand}.
	 * @param ctx the parse tree
	 */
	void exitEvaluated_operand(csexpressParser.Evaluated_operandContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#unary_operator}.
	 * @param ctx the parse tree
	 */
	void enterUnary_operator(csexpressParser.Unary_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#unary_operator}.
	 * @param ctx the parse tree
	 */
	void exitUnary_operator(csexpressParser.Unary_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#function_operator}.
	 * @param ctx the parse tree
	 */
	void enterFunction_operator(csexpressParser.Function_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#function_operator}.
	 * @param ctx the parse tree
	 */
	void exitFunction_operator(csexpressParser.Function_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#function_parameter}.
	 * @param ctx the parse tree
	 */
	void enterFunction_parameter(csexpressParser.Function_parameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#function_parameter}.
	 * @param ctx the parse tree
	 */
	void exitFunction_parameter(csexpressParser.Function_parameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(csexpressParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(csexpressParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#math_operator}.
	 * @param ctx the parse tree
	 */
	void enterMath_operator(csexpressParser.Math_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#math_operator}.
	 * @param ctx the parse tree
	 */
	void exitMath_operator(csexpressParser.Math_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#text_operator}.
	 * @param ctx the parse tree
	 */
	void enterText_operator(csexpressParser.Text_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#text_operator}.
	 * @param ctx the parse tree
	 */
	void exitText_operator(csexpressParser.Text_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#conditional_operator}.
	 * @param ctx the parse tree
	 */
	void enterConditional_operator(csexpressParser.Conditional_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#conditional_operator}.
	 * @param ctx the parse tree
	 */
	void exitConditional_operator(csexpressParser.Conditional_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#property_operand}.
	 * @param ctx the parse tree
	 */
	void enterProperty_operand(csexpressParser.Property_operandContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#property_operand}.
	 * @param ctx the parse tree
	 */
	void exitProperty_operand(csexpressParser.Property_operandContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#property_field}.
	 * @param ctx the parse tree
	 */
	void enterProperty_field(csexpressParser.Property_fieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#property_field}.
	 * @param ctx the parse tree
	 */
	void exitProperty_field(csexpressParser.Property_fieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#literal_operand}.
	 * @param ctx the parse tree
	 */
	void enterLiteral_operand(csexpressParser.Literal_operandContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#literal_operand}.
	 * @param ctx the parse tree
	 */
	void exitLiteral_operand(csexpressParser.Literal_operandContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#tag_literal}.
	 * @param ctx the parse tree
	 */
	void enterTag_literal(csexpressParser.Tag_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#tag_literal}.
	 * @param ctx the parse tree
	 */
	void exitTag_literal(csexpressParser.Tag_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#tag_code}.
	 * @param ctx the parse tree
	 */
	void enterTag_code(csexpressParser.Tag_codeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#tag_code}.
	 * @param ctx the parse tree
	 */
	void exitTag_code(csexpressParser.Tag_codeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(csexpressParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(csexpressParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#range_literal_operand}.
	 * @param ctx the parse tree
	 */
	void enterRange_literal_operand(csexpressParser.Range_literal_operandContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#range_literal_operand}.
	 * @param ctx the parse tree
	 */
	void exitRange_literal_operand(csexpressParser.Range_literal_operandContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#rangeLiteral}.
	 * @param ctx the parse tree
	 */
	void enterRangeLiteral(csexpressParser.RangeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#rangeLiteral}.
	 * @param ctx the parse tree
	 */
	void exitRangeLiteral(csexpressParser.RangeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#coupling}.
	 * @param ctx the parse tree
	 */
	void enterCoupling(csexpressParser.CouplingContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#coupling}.
	 * @param ctx the parse tree
	 */
	void exitCoupling(csexpressParser.CouplingContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#relationcoupling}.
	 * @param ctx the parse tree
	 */
	void enterRelationcoupling(csexpressParser.RelationcouplingContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#relationcoupling}.
	 * @param ctx the parse tree
	 */
	void exitRelationcoupling(csexpressParser.RelationcouplingContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#objectcoupling}.
	 * @param ctx the parse tree
	 */
	void enterObjectcoupling(csexpressParser.ObjectcouplingContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#objectcoupling}.
	 * @param ctx the parse tree
	 */
	void exitObjectcoupling(csexpressParser.ObjectcouplingContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#predefinedobject}.
	 * @param ctx the parse tree
	 */
	void enterPredefinedobject(csexpressParser.PredefinedobjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#predefinedobject}.
	 * @param ctx the parse tree
	 */
	void exitPredefinedobject(csexpressParser.PredefinedobjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(csexpressParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(csexpressParser.ElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(csexpressParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(csexpressParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#property}.
	 * @param ctx the parse tree
	 */
	void enterProperty(csexpressParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#property}.
	 * @param ctx the parse tree
	 */
	void exitProperty(csexpressParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#propertytype}.
	 * @param ctx the parse tree
	 */
	void enterPropertytype(csexpressParser.PropertytypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#propertytype}.
	 * @param ctx the parse tree
	 */
	void exitPropertytype(csexpressParser.PropertytypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#tag_value}.
	 * @param ctx the parse tree
	 */
	void enterTag_value(csexpressParser.Tag_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#tag_value}.
	 * @param ctx the parse tree
	 */
	void exitTag_value(csexpressParser.Tag_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#tagvaluetype}.
	 * @param ctx the parse tree
	 */
	void enterTagvaluetype(csexpressParser.TagvaluetypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#tagvaluetype}.
	 * @param ctx the parse tree
	 */
	void exitTagvaluetype(csexpressParser.TagvaluetypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#object}.
	 * @param ctx the parse tree
	 */
	void enterObject(csexpressParser.ObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#object}.
	 * @param ctx the parse tree
	 */
	void exitObject(csexpressParser.ObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#objecttype}.
	 * @param ctx the parse tree
	 */
	void enterObjecttype(csexpressParser.ObjecttypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#objecttype}.
	 * @param ctx the parse tree
	 */
	void exitObjecttype(csexpressParser.ObjecttypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#meta}.
	 * @param ctx the parse tree
	 */
	void enterMeta(csexpressParser.MetaContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#meta}.
	 * @param ctx the parse tree
	 */
	void exitMeta(csexpressParser.MetaContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#identifierelt}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierelt(csexpressParser.IdentifiereltContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#identifierelt}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierelt(csexpressParser.IdentifiereltContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#iidspec}.
	 * @param ctx the parse tree
	 */
	void enterIidspec(csexpressParser.IidspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#iidspec}.
	 * @param ctx the parse tree
	 */
	void exitIidspec(csexpressParser.IidspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#catalogspec}.
	 * @param ctx the parse tree
	 */
	void enterCatalogspec(csexpressParser.CatalogspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#catalogspec}.
	 * @param ctx the parse tree
	 */
	void exitCatalogspec(csexpressParser.CatalogspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#organizationspec}.
	 * @param ctx the parse tree
	 */
	void enterOrganizationspec(csexpressParser.OrganizationspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#organizationspec}.
	 * @param ctx the parse tree
	 */
	void exitOrganizationspec(csexpressParser.OrganizationspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#localespec}.
	 * @param ctx the parse tree
	 */
	void enterLocalespec(csexpressParser.LocalespecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#localespec}.
	 * @param ctx the parse tree
	 */
	void exitLocalespec(csexpressParser.LocalespecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#contextspec}.
	 * @param ctx the parse tree
	 */
	void enterContextspec(csexpressParser.ContextspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#contextspec}.
	 * @param ctx the parse tree
	 */
	void exitContextspec(csexpressParser.ContextspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#propertyspec}.
	 * @param ctx the parse tree
	 */
	void enterPropertyspec(csexpressParser.PropertyspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#propertyspec}.
	 * @param ctx the parse tree
	 */
	void exitPropertyspec(csexpressParser.PropertyspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#typespec}.
	 * @param ctx the parse tree
	 */
	void enterTypespec(csexpressParser.TypespecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#typespec}.
	 * @param ctx the parse tree
	 */
	void exitTypespec(csexpressParser.TypespecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#sidespec}.
	 * @param ctx the parse tree
	 */
	void enterSidespec(csexpressParser.SidespecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#sidespec}.
	 * @param ctx the parse tree
	 */
	void exitSidespec(csexpressParser.SidespecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#startspec}.
	 * @param ctx the parse tree
	 */
	void enterStartspec(csexpressParser.StartspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#startspec}.
	 * @param ctx the parse tree
	 */
	void exitStartspec(csexpressParser.StartspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#endspec}.
	 * @param ctx the parse tree
	 */
	void enterEndspec(csexpressParser.EndspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#endspec}.
	 * @param ctx the parse tree
	 */
	void exitEndspec(csexpressParser.EndspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#tagspec}.
	 * @param ctx the parse tree
	 */
	void enterTagspec(csexpressParser.TagspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#tagspec}.
	 * @param ctx the parse tree
	 */
	void exitTagspec(csexpressParser.TagspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#tagcode}.
	 * @param ctx the parse tree
	 */
	void enterTagcode(csexpressParser.TagcodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#tagcode}.
	 * @param ctx the parse tree
	 */
	void exitTagcode(csexpressParser.TagcodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#rangespec}.
	 * @param ctx the parse tree
	 */
	void enterRangespec(csexpressParser.RangespecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#rangespec}.
	 * @param ctx the parse tree
	 */
	void exitRangespec(csexpressParser.RangespecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#rangeofspec}.
	 * @param ctx the parse tree
	 */
	void enterRangeofspec(csexpressParser.RangeofspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#rangeofspec}.
	 * @param ctx the parse tree
	 */
	void exitRangeofspec(csexpressParser.RangeofspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#datespec}.
	 * @param ctx the parse tree
	 */
	void enterDatespec(csexpressParser.DatespecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#datespec}.
	 * @param ctx the parse tree
	 */
	void exitDatespec(csexpressParser.DatespecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#isversionablespec}.
	 * @param ctx the parse tree
	 */
	void enterIsversionablespec(csexpressParser.IsversionablespecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#isversionablespec}.
	 * @param ctx the parse tree
	 */
	void exitIsversionablespec(csexpressParser.IsversionablespecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#targets_spec}.
	 * @param ctx the parse tree
	 */
	void enterTargets_spec(csexpressParser.Targets_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#targets_spec}.
	 * @param ctx the parse tree
	 */
	void exitTargets_spec(csexpressParser.Targets_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#endpointspec}.
	 * @param ctx the parse tree
	 */
	void enterEndpointspec(csexpressParser.EndpointspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#endpointspec}.
	 * @param ctx the parse tree
	 */
	void exitEndpointspec(csexpressParser.EndpointspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link csexpressParser#primaryidentifier}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryidentifier(csexpressParser.PrimaryidentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link csexpressParser#primaryidentifier}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryidentifier(csexpressParser.PrimaryidentifierContext ctx);
}