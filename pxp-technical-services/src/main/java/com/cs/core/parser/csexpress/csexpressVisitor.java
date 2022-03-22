// Generated from csexpress\csexpress.g4 by ANTLR 4.7

    package com.cs.core.parser.csexpress;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link csexpressParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface csexpressVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link csexpressParser#search_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearch_expression(csexpressParser.Search_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#rule_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRule_expression(csexpressParser.Rule_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#calculation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalculation(csexpressParser.CalculationContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScope(csexpressParser.ScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#basetype_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasetype_scope(csexpressParser.Basetype_scopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#entity_basetypes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntity_basetypes(csexpressParser.Entity_basetypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#basetype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasetype(csexpressParser.BasetypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#catalog_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatalog_scope(csexpressParser.Catalog_scopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#catalogs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatalogs(csexpressParser.CatalogsContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#organization_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrganization_scope(csexpressParser.Organization_scopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#organizations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrganizations(csexpressParser.OrganizationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#organization_code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrganization_code(csexpressParser.Organization_codeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#catalog_code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatalog_code(csexpressParser.Catalog_codeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#locale_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocale_scope(csexpressParser.Locale_scopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#locales}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocales(csexpressParser.LocalesContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#locale_code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocale_code(csexpressParser.Locale_codeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#iid_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIid_scope(csexpressParser.Iid_scopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#entity_iids}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntity_iids(csexpressParser.Entity_iidsContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#entity_iid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntity_iid(csexpressParser.Entity_iidContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#endpoint_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndpoint_scope(csexpressParser.Endpoint_scopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#endpoints}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndpoints(csexpressParser.EndpointsContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#endpoint_code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndpoint_code(csexpressParser.Endpoint_codeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#entity_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntity_scope(csexpressParser.Entity_scopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#entity_filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntity_filter(csexpressParser.Entity_filterContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#scope_not}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScope_not(csexpressParser.Scope_notContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#scope_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScope_operator(csexpressParser.Scope_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#list_through_property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_through_property(csexpressParser.List_through_propertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#evaluated_filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvaluated_filter(csexpressParser.Evaluated_filterContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#context}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContext(csexpressParser.ContextContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#collection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollection(csexpressParser.CollectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#expiry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpiry(csexpressParser.ExpiryContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#duplicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDuplicate(csexpressParser.DuplicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#translation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTranslation(csexpressParser.TranslationContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#action_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction_list(csexpressParser.Action_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction(csexpressParser.ActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#evaluated_action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvaluated_action(csexpressParser.Evaluated_actionContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#target_property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTarget_property(csexpressParser.Target_propertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#target_object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTarget_object(csexpressParser.Target_objectContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#classifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassifier(csexpressParser.ClassifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#predefinedclassifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredefinedclassifier(csexpressParser.PredefinedclassifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#quality_level}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuality_level(csexpressParser.Quality_levelContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#evaluation_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvaluation_expression(csexpressParser.Evaluation_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#operand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperand(csexpressParser.OperandContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#evaluated_operand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvaluated_operand(csexpressParser.Evaluated_operandContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#unary_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_operator(csexpressParser.Unary_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#function_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_operator(csexpressParser.Function_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#function_parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_parameter(csexpressParser.Function_parameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(csexpressParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#math_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMath_operator(csexpressParser.Math_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#text_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitText_operator(csexpressParser.Text_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#conditional_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditional_operator(csexpressParser.Conditional_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#property_operand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty_operand(csexpressParser.Property_operandContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#property_field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty_field(csexpressParser.Property_fieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#literal_operand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral_operand(csexpressParser.Literal_operandContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#tag_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTag_literal(csexpressParser.Tag_literalContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#tag_code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTag_code(csexpressParser.Tag_codeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(csexpressParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#range_literal_operand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange_literal_operand(csexpressParser.Range_literal_operandContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#rangeLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeLiteral(csexpressParser.RangeLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#coupling}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoupling(csexpressParser.CouplingContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#relationcoupling}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationcoupling(csexpressParser.RelationcouplingContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#objectcoupling}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectcoupling(csexpressParser.ObjectcouplingContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#predefinedobject}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredefinedobject(csexpressParser.PredefinedobjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement(csexpressParser.ElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(csexpressParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty(csexpressParser.PropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#propertytype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertytype(csexpressParser.PropertytypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#tag_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTag_value(csexpressParser.Tag_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#tagvaluetype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagvaluetype(csexpressParser.TagvaluetypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject(csexpressParser.ObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#objecttype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjecttype(csexpressParser.ObjecttypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#meta}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeta(csexpressParser.MetaContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#identifierelt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierelt(csexpressParser.IdentifiereltContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#iidspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIidspec(csexpressParser.IidspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#catalogspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatalogspec(csexpressParser.CatalogspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#organizationspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrganizationspec(csexpressParser.OrganizationspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#localespec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalespec(csexpressParser.LocalespecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#contextspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContextspec(csexpressParser.ContextspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#propertyspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyspec(csexpressParser.PropertyspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#typespec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypespec(csexpressParser.TypespecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#sidespec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSidespec(csexpressParser.SidespecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#startspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStartspec(csexpressParser.StartspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#endspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndspec(csexpressParser.EndspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#tagspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagspec(csexpressParser.TagspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#tagcode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagcode(csexpressParser.TagcodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#rangespec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangespec(csexpressParser.RangespecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#rangeofspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeofspec(csexpressParser.RangeofspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#datespec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatespec(csexpressParser.DatespecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#isversionablespec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsversionablespec(csexpressParser.IsversionablespecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#targets_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargets_spec(csexpressParser.Targets_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#endpointspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndpointspec(csexpressParser.EndpointspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link csexpressParser#primaryidentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryidentifier(csexpressParser.PrimaryidentifierContext ctx);
}