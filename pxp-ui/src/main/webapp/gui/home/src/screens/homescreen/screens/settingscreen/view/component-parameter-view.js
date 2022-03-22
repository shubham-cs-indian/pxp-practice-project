import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as CustomTextFieldView } from '../../../../../viewlibraries/customtextfieldview/custom-text-field-view';
import { view as MultiSelectSearchView } from './../../../../../viewlibraries/multiselectsearchviewnew/multiselect-search-view';
import { view as LazyMSSView } from './../../../../../viewlibraries/lazy-mss-view/lazy-mss-view';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import ProcessComponentParameterLayoutData from './../tack/process-component-parameter-layout-data';
import ProcessComponentParameterPropertiesLayoutData from './../tack/process-component-parameter-properties-layout-data';

/**************
 * DEV-NOTE:::View Currently not used, oEvents triggered by BPMNCustomComponentsPropertiesView Remove if not
 *  needed in future
 ***************/

const oEvents = {
  COMPONENT_PARAMETER_DATA_SOURCE_VALUE_CHANGED: "component_parameter_data_source_value_changed",
  COMPONENT_PARAMETER_DATA_SOURCE_CLASS_VALUE_CHANGED: "component_parameter_data_source_class_value_changed",
  COMPONENT_TAXONOMY_DATA_SOURCE_CLASS_VALUE_CHANGED: "component_taxonomy_data_source_class_value_changed",
  COMPONENT_ADD_TAXONOMY_IN_CLASS_VALUE: "component_add_taxonomy_in_class_value",
  COMPONENT_REMOVE_TAXONOMY_IN_CLASS_VALUE: "component_remove_taxonomy_in_class_value"
};

const oPropTypes = {
  selectedComponent: ReactPropTypes.object,
  ruleList: ReactPropTypes.array,
  relationshipList: ReactPropTypes.object,
  contextList: ReactPropTypes.array,
  attributeList: ReactPropTypes.array,
  orgConfigListModel: ReactPropTypes.array,
  destinationCatalogModel: ReactPropTypes.array,
  allTalendJobList: ReactPropTypes.array,
  lazyMSSReqResInfo: ReactPropTypes.object,
  referencedData: ReactPropTypes.object
};

// @CS.SafeComponent
class ComponentParameterView extends React.Component {
  static propTypes = oPropTypes;

  handleDataChanged = (sKey, sType, sValue) => {
    if (sType === "dataSource") {
      EventBus.dispatch(oEvents.COMPONENT_PARAMETER_DATA_SOURCE_VALUE_CHANGED, sKey, sValue);
    }
    else if (sType === "classInfo") {
      EventBus.dispatch(oEvents.COMPONENT_PARAMETER_DATA_SOURCE_CLASS_VALUE_CHANGED, sKey, sValue);
    }
  };

  handleDataSourceStringValueChanged = (sKey, oEvent) => {
    var sValue = oEvent.target.value;
    EventBus.dispatch(oEvents.COMPONENT_PARAMETER_DATA_SOURCE_VALUE_CHANGED, sKey, sValue);
  };

  handleClassInfoSelectChanged = (sKey, oEvent) => {
    var sValue = oEvent.target.value;
    EventBus.dispatch(oEvents.COMPONENT_PARAMETER_DATA_SOURCE_CLASS_VALUE_CHANGED, sKey, sValue);
  };

  handleTaxonomyInfoValueChanged = (sKey, sNestedObjectId, sNestedObjectKey, sValue) => {
     EventBus.dispatch(oEvents.COMPONENT_TAXONOMY_DATA_SOURCE_CLASS_VALUE_CHANGED, sKey, sNestedObjectId, sNestedObjectKey, sValue);
  };

  handleAddTaxonomyClicked = () => {
     EventBus.dispatch(oEvents.COMPONENT_ADD_TAXONOMY_IN_CLASS_VALUE);
  };

  handleRemoveTaxonomyClicked = (sTaxonomyId) => {
     EventBus.dispatch(oEvents.COMPONENT_REMOVE_TAXONOMY_IN_CLASS_VALUE, sTaxonomyId);
  };

  handleMultiClassSelectionChanged = (sKey, aValues) => {
    if (sKey === "attributeColumn" || sKey === "talendExecutable"|| sKey === "contextId") {
      //this req single string so extracting it here.
      aValues = aValues.length ? aValues[0] : "";
    }
    EventBus.dispatch(oEvents.COMPONENT_PARAMETER_DATA_SOURCE_CLASS_VALUE_CHANGED, sKey, aValues);
  };

  handleCheckboxToggled = (sKey, bValue) => {
    var sValue = !bValue;
    EventBus.dispatch(oEvents.COMPONENT_PARAMETER_DATA_SOURCE_CLASS_VALUE_CHANGED, sKey, sValue);
  };

  handleClassTypeDropdownChanged = (sKey, aSelectedItems, oReferencedData) => {
    EventBus.dispatch(oEvents.COMPONENT_PARAMETER_DATA_SOURCE_CLASS_VALUE_CHANGED, sKey, aSelectedItems[0], oReferencedData);
  };

  getCheckBoxView = (sKey, sLabel, bIsChecked) => {
    return (
        <div className="parameterRow">
          <div className="parameterLabel">
            {sLabel}
            <input
                className="paramterCheckbox"
                type="checkbox"
                checked={bIsChecked}
                onChange={this.handleCheckboxToggled.bind(this, sKey, bIsChecked)}
            />
          </div>
        </div>
    );
  };

  getTextInputView = (sKey, sLabel, sLabelToShow) => {
    let oSelectedComponent = this.props.selectedComponent;
    let oDataSource = oSelectedComponent.dataSources[0];
    let oClassInfo = oDataSource.classInfo;
    let sType = oClassInfo.hasOwnProperty(sKey) ? "classInfo" : "dataSource";
    return (
        <div className="parameterRow">
          <div className="parameterLabel">{sLabelToShow || sLabel}</div>
          <div className="parameterValue">
            <CustomTextFieldView
                value={oClassInfo[sKey] || oDataSource[sKey]}
                hintText={sLabelToShow || sLabel}
                onBlur={this.handleDataChanged.bind(this, sKey, sType)}/>
          </div>
        </div>
    );
  };

  getDropDownView = (sKey, sLabelToShow, aItems, aSelectedItems, bIsSingleSelect) => {
    let oView = this.getMultiSelectView(aItems, aSelectedItems, sKey, "", bIsSingleSelect);

    return (
        <div className="parameterRow">
          <div className="parameterLabel">{sLabelToShow}</div>
          <div className="parameterValue">
            {oView}
          </div>
        </div>);
  };

  getMultiSelectView = (aItems, aSelectedItems, sKey, sLabel, bIsSingleSelect) => {
    return (
        <MultiSelectSearchView
            items={aItems}
            selectedItems={aSelectedItems}
            onChange={this.handleMultiClassSelectionChanged.bind(this, sKey)}
            label={sLabel}
            singleSelect={!!bIsSingleSelect}
            checkbox={true}
        />
    );
  };

  getLazyMSSView = (aSelectedItems, oReferencedData, oReqResInfo, fOnApply, bIsMultiSelect, sLabelToShow) => {
    let oSelectedComponent = this.props.selectedComponent;
    let bIsMultiSelectTemp = bIsMultiSelect || false;

    return (
        <div className="parameterRow">
          <div className="parameterLabel">{sLabelToShow}</div>
          <div className="parameterValue">
            <LazyMSSView
                isMultiSelect={bIsMultiSelectTemp}
                selectedItems={aSelectedItems}
                context={oSelectedComponent.componentId}
                referencedData={oReferencedData}
                requestResponseInfo={oReqResInfo}
                onApply={fOnApply}
            />
          </div>
        </div>
    );
  };

  getSelectView = (sKey, aOptions, sClassName, sLabelToShow) => {
    let oSelectedComponent = this.props.selectedComponent;
    let oDataSource = oSelectedComponent.dataSources[0];
    let oClassInfo = oDataSource.classInfo;
    let oLabelDom = CS.isEmpty(sLabelToShow) ? null : (<div className="parameterLabel">{sLabelToShow}</div>);
    let onChangeFunction = this.handleClassInfoSelectChanged.bind(this, sKey);
    if(sKey === "fileSource"){
      onChangeFunction = this.handleDataSourceStringValueChanged.bind(this, sKey);
    }
    return (
        <div className="parameterRow">
          {oLabelDom}
          <div className="parameterValue">
            <select className={sClassName} value={oClassInfo[sKey] || oDataSource[sKey]} onChange={onChangeFunction}>
              {aOptions}
            </select>
          </div>
        </div>);

  };

  getContextTypeColumnView = (oProperty) => {
    var oSelectedComponent = this.props.selectedComponent;
    var aContextList = this.props.contextList;
    var oDataSource = oSelectedComponent.dataSources[0];
    var oClassInfo = oDataSource.classInfo;

    var aContextsToShow = [];
    if(oClassInfo.isAttributeVariant){
        aContextsToShow = CS.filter(aContextList, {type: "attributeVariantContext"});
    }else {
      aContextsToShow = aContextsToShow.concat(CS.filter(aContextList, {type: "contextualVariant"}));
      aContextsToShow = aContextsToShow.concat(CS.filter(aContextList, {type: "gtinVariant"}));
    }

    let aSelectedItems = !CS.isEmpty(oClassInfo[oProperty.key]) ? [oClassInfo[oProperty.key]] : [];
    return this.getDropDownView(oProperty.key, oProperty.label, aContextsToShow, aSelectedItems, true);
  };

  getTaxonomyView = (oProperty) => {
    var _this = this;
    var oSelectedComponent = this.props.selectedComponent;
    var oDataSource = oSelectedComponent.dataSources[0];
    var oClassInfo = oDataSource.classInfo;
    var aViews = [];

    CS.forEach(oClassInfo[oProperty.key], function (oTaxonomy) {
      var oColumnNameView = (<CustomTextFieldView
          value={oTaxonomy.taxonomyColumn}
          hintText={getTranslation().TAXONOMY_COLUMN_NAME}
          onBlur={_this.handleTaxonomyInfoValueChanged.bind(_this, oProperty.key, oTaxonomy.taxonomyId, 'taxonomyColumn')}/>);
      aViews.push(
          <div className="parameterRow">
            <div className="parameterValue">
              {oColumnNameView}
            </div>
            <TooltipView label={getTranslation().REMOVE_TAXONOMY}>
              <div className="removeTaxonomy"
                   onClick={_this.handleRemoveTaxonomyClicked.bind(_this, oTaxonomy.taxonomyId)}>
              </div>
            </TooltipView>
          </div>
      );
    });

    aViews.push(
        <TooltipView label={getTranslation().ADD_TAXONOMY}>
          <div className="addTaxonomyButton" onClick={_this.handleAddTaxonomyClicked}>
          </div>
        </TooltipView>
    );

    return (
        <div className="parameterRow taxonomyWrapper">
          <div className="taxonomyLabel">{oProperty.label}</div>
          <div className="taxonomyBody">{aViews}</div>
        </div>
    );
  };

  getRelationshipView = (bForceColumn, oProperty) => {
    var oSelectedComponent = this.props.selectedComponent;
    var aRelationships = this.props.relationshipList;
    var oDataSource = oSelectedComponent.dataSources[0];
    var oClassInfo = oDataSource.classInfo;

    var oView = null;

    var sLabelToShow = oProperty.label;
    if (bForceColumn || oClassInfo.relationshipType === "relationshipColumn") {
      sLabelToShow = getTranslation().RELATIONSHIP_COLUMN;
      oView = this.getTextInputView("relationshipColumnName", getTranslation().ENTER_RELATIONSHIP_COLUMN_NAME, sLabelToShow);
    }
    else if(oClassInfo.relationshipType === "singleRelationship") {
      var aOptions = [];
      aOptions.push(<option value="">{getTranslation().NONE}</option>);
      CS.forEach(aRelationships, function (oRelationship) {
        aOptions.push(<option value={oRelationship.id}>{oRelationship.label}</option>);
      });
      oView = (<div className="parameterRow">
            <div className="parameterLabel">{sLabelToShow}</div>
            <div className="parameterValue">
               <select className="relationshipSelector" value={oClassInfo[oProperty.key]} onChange={this.handleClassInfoSelectChanged.bind(this, 'relationshipId')}>
            {aOptions}
          </select>
            </div>
          </div>
      )
    }
    else {
      return null;
    }

    return oView;
  };

  getDestinationCatalogView = (oProperty) => {
    let bIsSingleSelect = true;
    let aItems = this.props.destinationCatalogModel;
    let oSelectedComponent = this.props.selectedComponent;
    let oDataSource = oSelectedComponent.dataSources[0];
    let oClassInfo = oDataSource.classInfo;
    let aSelectedItems = oClassInfo[oProperty.key] || [];
    if(!CS.isArray(oClassInfo[oProperty.key])){
      aSelectedItems = [oClassInfo[oProperty.key]];
    }
    return(this.getDropDownView(oProperty.key, oProperty.label, aItems, aSelectedItems, bIsSingleSelect));
  };

  getDestinationOrganisationId = (oProperty) => {
    let aOrgConfigListModel = this.props.orgConfigListModel;
    let aItems = [];
    let bIsSingleSelect = true;
    CS.forEach(aOrgConfigListModel,function (oOrgConfigListModel) {
      aItems.push({ id: oOrgConfigListModel.id,
      label:  oOrgConfigListModel.label}
      );
    });
    let oSelectedComponent = this.props.selectedComponent;
    let oDataSource = oSelectedComponent.dataSources[0];
    let oClassInfo = oDataSource.classInfo;
    let aSelectedItems = oClassInfo[oProperty.key] || [];
    if(!CS.isArray(oClassInfo[oProperty.key])){
      aSelectedItems = [oClassInfo[oProperty.key]];
    }
    return(this.getDropDownView(oProperty.key, oProperty.label, aItems, aSelectedItems, bIsSingleSelect));
  };

  getTalendJobsView = (oProperty) => {
    var aAllTalendJobListModel = this.props.allTalendJobList;
    let bIsSingleSelect = true;
    let oSelectedComponent = this.props.selectedComponent;
    let oDataSource = oSelectedComponent.dataSources[0];
    let oClassInfo = oDataSource.classInfo;
    let aSelectedItems = oClassInfo[oProperty.key] && [oClassInfo[oProperty.key]] || [];
    return (this.getDropDownView(oProperty.key, oProperty.label, aAllTalendJobListModel, aSelectedItems, bIsSingleSelect));
  };

  getDependentFieldSingleClassView = (oProperty) => {
    let oSelectedComponent = this.props.selectedComponent;
    let oDataSource = oSelectedComponent.dataSources[0];
    let oClassInfo = oDataSource.classInfo;
    let _this = this;

    let oParametersViews = [];
    let oDependentProperty = {};
    if (oClassInfo.type === "singleClass") {
      oDependentProperty = oProperty.singleClass;
      let oReqResInfo = _this.props.lazyMSSReqResInfo;
      let oReferencedData = _this.props.referencedData || {};
      oParametersViews = (_this.getLazyMSSView([oClassInfo[oDependentProperty.key]], oReferencedData.referencedKlasses, oReqResInfo.natureKlassType, _this.handleClassTypeDropdownChanged.bind(this, oDependentProperty.key), false, oDependentProperty.label));
    }
    else if (oClassInfo.type === "klassColumn" || oClassInfo.secondaryClassType === "klassColumn") {
      oDependentProperty = oProperty.klassColumn;
      oParametersViews = (_this.getTextInputView(oDependentProperty.key, oDependentProperty.hintText, oDependentProperty.label));
    }

    return oParametersViews;
  };

  getVariantTypeView = (oProperty) => {
    let oSelectedComponent = this.props.selectedComponent;
    let oDataSource = oSelectedComponent.dataSources[0];
    let oClassInfo = oDataSource.classInfo;
    let aParametersViews = [];
    let aOptionList = oProperty.options;
    let aOption = [];
    CS.forEach(aOptionList, function (oOptions) {
      aOption.push(<option value={oOptions.id}>{oOptions.label}</option>);
    });
    aParametersViews.push(this.getSelectView(oProperty.key, aOption, "typeContainer", oProperty.label));
    if (oClassInfo[oProperty.key] === "nestedVariant") {
      let aContextsToShow = this.props.contextList;
      let aOptions = [];
      CS.forEach(aContextsToShow, function (oContext) {
        aOptions.push(<option value={oContext.id}>{oContext.label}</option>);
      });
      let oProcessComponentParameterPropertiesLayoutData = new ProcessComponentParameterPropertiesLayoutData();
      let oComponentProperties = oProcessComponentParameterPropertiesLayoutData.component_properties;
      let oParentVariantColumnName = oComponentProperties[oProperty.dependentFields[1]];
      aParametersViews.push(this.getTextInputView(oParentVariantColumnName.key, oParentVariantColumnName.label, oParentVariantColumnName.label));
    }
    return aParametersViews;
  };

  getAttributeVariantView = (oProperty) => {
    let oSelectedComponent = this.props.selectedComponent;
    let oDataSource = oSelectedComponent.dataSources[0];
    let oClassInfo = oDataSource.classInfo;
    let aParametersViews = [];
    aParametersViews.push(this.getCheckBoxView(oProperty.key, oProperty.label, oClassInfo[oProperty.key]));
    if (oClassInfo.isAttributeVariant) {
      let aAttributeList = this.props.attributeList;
      let aDependentField = oProperty.dependentFields;
      let oProcessComponentParameterPropertiesLayoutData = new ProcessComponentParameterPropertiesLayoutData();
      let oComponentProperties = oProcessComponentParameterPropertiesLayoutData.component_properties;
      let oAttributeColumn = oComponentProperties[aDependentField[0]];
      let oAttributeValueColumn = oComponentProperties[aDependentField[1]];
      let aSelectedItems = !CS.isEmpty(oClassInfo[oAttributeColumn.key]) ? [oClassInfo[oAttributeColumn.key]] : [];
      aParametersViews.push(this.getDropDownView(oAttributeColumn.key, oAttributeColumn.label, aAttributeList, aSelectedItems, true));
      aParametersViews.push(this.getTextInputView(oAttributeValueColumn.key, oAttributeValueColumn.label));
    }
    return aParametersViews;
  };

  getMulticlassificationView = (oProperty) => {
    let aOptionsList = oProperty.options;
    let aOptions = [];
    CS.forEach(aOptionsList, function (oOptions) {
      aOptions.push(<option value={oOptions.id}>{oOptions.label}</option>);
    });
    return (this.getSelectView(oProperty.key, aOptions, "typeContainer", oProperty.label));
  };

  getMultiKlassInfoView = (oProperty) => {
    let oSelectedComponent = this.props.selectedComponent;
    let oDataSource = oSelectedComponent.dataSources[0];
    let oClassInfo = oDataSource.classInfo;
    let oDependentProperty = {};
    let oParametersViews = null;
    if (oClassInfo.secondaryClassType === "multiClass") {
      oDependentProperty = oProperty.multiClass;
      let oReqResInfo = this.props.lazyMSSReqResInfo;
      let oReferencedData = this.props.referencedData || {};
      oParametersViews = (this.getLazyMSSView(oClassInfo[oDependentProperty.key], oReferencedData.referencedKlasses, oReqResInfo.nonNatureKlassType, this.handleMultiClassSelectionChanged.bind(this, oDependentProperty.key), true, oDependentProperty.label));
    }
    else if (oClassInfo.secondaryClassType === "klassColumn") {
      oDependentProperty = oProperty.klassColumn;
      oParametersViews = (this.getTextInputView(oDependentProperty.key, oDependentProperty.hintText, oDependentProperty.label));
    }
    return oParametersViews;
  };

  getCustomMulticlassificationView = (oProperty) => {
    let oSelectedComponent = this.props.selectedComponent;
    let oDataSource = oSelectedComponent.dataSources[0];
    let oClassInfo = oDataSource.classInfo;
    let aParametersViews = [];
    aParametersViews.push(this.getCheckBoxView(oProperty.key, oProperty.label, oClassInfo[oProperty.key]));
    if (oClassInfo.isMultiClassificationEnabled) {
      let oProcessComponentParameterPropertiesLayoutData = new ProcessComponentParameterPropertiesLayoutData();
      let oComponentProperties = oProcessComponentParameterPropertiesLayoutData.component_properties;
      aParametersViews.push(this.getMulticlassificationView(oComponentProperties[oProperty.dependentFields[0]]));
      aParametersViews.push(this.getMultiKlassInfoView(oComponentProperties[oProperty.dependentFields[1]]));
    }
    return aParametersViews;
  };

  getView = () => {
    var aParametersViews = [];
    var oSelectedComponent = this.props.selectedComponent;
    var oDataSource = oSelectedComponent.dataSources[0];
    var oClassInfo = oDataSource.classInfo;
    let _this = this;
    let oProcessComponentParameterLayoutData = new ProcessComponentParameterLayoutData();
    let oComponents = oProcessComponentParameterLayoutData.components;
    let aComponentProperties = oComponents[oSelectedComponent.componentId] || oComponents.customComponent;

    CS.forEach(aComponentProperties, function (oProperty) {
        switch(oProperty.type){
          case "singleText" :
            aParametersViews.push(_this.getTextInputView(oProperty.key, oProperty.hintText, oProperty.label));
            break;

          case "select" :
            let aOptionsList = oProperty.options;
            let aOptions = [];
            CS.forEach(aOptionsList, function (oOptions) {
              aOptions.push(<option value={oOptions.id}>{oOptions.label}</option>);
            });
            aParametersViews.push(_this.getSelectView(oProperty.key, aOptions, "typeContainer", oProperty.label));

            break;

          case "dependentSelect" :
            let aNativeOptionsLists = oProperty.options;
            let aNativeOptions = [];
            CS.forEach(aNativeOptionsLists, function (oOptions) {
              aNativeOptions.push(<option value={oOptions.id}>{oOptions.label}</option>);
            });
            aParametersViews.push(_this.getSelectView(oProperty.key, aNativeOptions, "typeContainer", oProperty.label));
            if (oDataSource.fileSource === "fromFolder") {
              let oProcessComponentParameterPropertiesLayoutData = new ProcessComponentParameterPropertiesLayoutData();
              let oComponentProperties = oProcessComponentParameterPropertiesLayoutData.component_properties;
              let oDependentField = oComponentProperties[oProperty.dependentFields[0]];
              aParametersViews.push(_this.getTextInputView(oDependentField.key, oDependentField.hintText, oDependentField.label));
            }
            break;

          case "checkbox" :
            aParametersViews.push(_this.getCheckBoxView(oProperty.key, oProperty.label, oClassInfo[oProperty.key]));
            break;

          case "mss":
            aParametersViews.push(_this.getDropDownView(oProperty.key, oProperty.label, oProperty.list, oProperty.selectedItem, oProperty.isSingleSelect));
            break;

          case "dependentFieldSingleClass" :
            aParametersViews.push(_this.getDependentFieldSingleClassView(oProperty));
            break;

          case "dependentFieldMultipleClass" :
            aParametersViews.push(_this.getMultiKlassInfoView(oProperty));
            break;

          case "customMulticlassificationView" :
            aParametersViews.push(_this.getCustomMulticlassificationView(oProperty));

            break;

          case "customAttributeVariantView" :
            aParametersViews.push(_this.getAttributeVariantView(oProperty));
            break;

          case "customTaxonomyView" :
            aParametersViews.push(_this.getTaxonomyView(oProperty));
            break;

          case "customContextTypeColumnView" :
            aParametersViews.push(_this.getContextTypeColumnView(oProperty));
            break;

          case "customRelationshipView" :
            if(oSelectedComponent.componentId === "Nature_Relationships_Import"){
              aParametersViews.push(_this.getRelationshipView(true,oProperty));
            }
            else{
              aParametersViews.push(_this.getRelationshipView(false,oProperty));
            }
            break;

          case "customDestinationCatalogView" :
            aParametersViews.push(_this.getDestinationCatalogView(oProperty));
            break;

          case "customDestinationOrganisationView" :
            aParametersViews.push(_this.getDestinationOrganisationId(oProperty))
            break;

          case "customTalendJobView":
            aParametersViews.push(_this.getTalendJobsView(oProperty));
            break;

          case "customVariantTypeView" :
            aParametersViews.push(_this.getVariantTypeView(oProperty));
            break;
        }
    });

    return aParametersViews;
  };

  render() {

    return (
        <div className="componentParameterViewContainer">
          {this.getView()}
        </div>
    );
  }
}

export const view = ComponentParameterView;
export const events = oEvents;
