import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as GridView } from './../../../../../viewlibraries/gridview/grid-view';
import ContextMenuViewModel from '../../../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';
import { view as ContextMenuViewNew } from '../../../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import SettingScreenModuleDictionary from './../../../../../commonmodule/tack/setting-screen-module-dictionary';

const oEvents = {
  TRANSLATION_PROPERTY_CHANGED: "translation_property_changed",
  SELECTED_LANGUAGES_CHANGED: "SELECTED_LANGUAGES_CHANGED",
  HANDLE_SELECTED_CLASS_MODULE_CHANGED: "handle_selected_class_module_changed"
};

const oPropTypes = {
  moduleList: ReactPropTypes.array,
  selectedModule: ReactPropTypes.string,
  gridData: ReactPropTypes.object,
  classModuleList: ReactPropTypes.array,
  selectedClassModule: ReactPropTypes.string,
  defaultLanguage: ReactPropTypes.string,
  userInterfaceLanguages : ReactPropTypes.array,
  };

// @CS.SafeComponent
class TranslationsConfigView extends React.Component {
  static propTypes = oPropTypes;

  handlePropertyChanged = (oModel) => {
    var oGridData = this.props.gridData;
    EventBus.dispatch(oEvents.TRANSLATION_PROPERTY_CHANGED, oModel.id, oGridData.gridViewContext);
  };

  applySelectedLanguages = (aLanguages) => {
    EventBus.dispatch(oEvents.SELECTED_LANGUAGES_CHANGED, aLanguages)
  };

  getPropertyButtonView = (oPropertyData) => {
    var oButtonView = this.getButtonView(oPropertyData);
    return (oButtonView);
  };

  getContextMenuModel = (sContext, aList, sSelectedProperty) => {
    var aMasterListModels = [];
    var sDefaultLanguage = this.props.defaultLanguage;
    let oLanguages = CS.keyBy(this.props.userInterfaceLanguages, 'code');

    CS.forEach(aList, function (sItemLabel) {
      if (sItemLabel == sDefaultLanguage || (sSelectedProperty && sItemLabel == sSelectedProperty)) {
        return;
      }
      let sLabel = (sContext != 'language') ? sItemLabel.toUpperCase() :  CS.getLabelOrCode(oLanguages[sItemLabel]) ;

      aMasterListModels.push(new ContextMenuViewModel(
          sItemLabel,
          sLabel,
          false,
          "",
          {context: ""}
      ));
    });
    return aMasterListModels;
  };

  getButtonView = (oButtonData, sLabel) => {
    if (!sLabel) {
      sLabel = oButtonData.selectedProperty;
    }
    let aContextMenuModel = this.getContextMenuModel(sLabel, oButtonData.list, oButtonData.selectedProperty);
    let oCountView = oButtonData.isMultiSelect ?
        <div className="selectedColumnCount">{oButtonData.selectedList.length - 1}</div> : null;
    var bIsMovable = oButtonData.isMultiSelect;
    let oButtonIconView = oButtonData.disabled ? null : <div className="buttonIcon"></div>;

    let oButtonView = (<ContextMenuViewNew
        onApplyHandler={oButtonData.onApplyHandler}
        onClickHandler={oButtonData.onClickHandler}
        isMultiselect={oButtonData.isMultiSelect}
        contextMenuViewModel={aContextMenuModel}
        selectedItems={oButtonData.selectedList}
        showSelectedItems={oButtonData.showSelectedItems}
        isMovable={bIsMovable}
        disabled={oButtonData.disabled}>
      <div className="columnOrganiserButton">
        <div className="buttonLabel">{getTranslation()[sLabel.toUpperCase()]}</div>
        {oCountView}
        {oButtonIconView}
      </div>
    </ContextMenuViewNew>);
    return oButtonView;
  };

  getPropertyLabel = () => {
    let sLabel = getTranslation().PROPERTY + ':';
    return (<div className="propertiesLabel">{sLabel}</div>)
  };

  getActionItems = () => {
    let __props = this.props;
    let oActionItemData = __props.gridData.actionItemData;
    let oPropertyData = oActionItemData.propertyData;
    let oLanguageData = oActionItemData.languageData;
    oPropertyData.onClickHandler = this.handlePropertyChanged;
    oLanguageData.onApplyHandler = this.applySelectedLanguages;
    return (
        <div className="translationScreenActionButtonWrapper">
          {__props.selectedModule == SettingScreenModuleDictionary.STATIC_TRANSLATION ? null : this.getPropertyLabel()}
          {__props.selectedModule == SettingScreenModuleDictionary.STATIC_TRANSLATION ? null : this.getPropertyButtonView(oPropertyData)}
        </div>)
  };

  getTranslationsGridView = () => {
    var oGridData = this.props.gridData;
    var oActionItems = this.getActionItems();
    let oColumnOrganizerData = this.props.columnOrganizerData;

    return (
        <div className="configGridViewContainer" key="translationsConfigGridViewContainer">
          <GridView context={oGridData.gridViewContext}
                    skeleton={oGridData.gridViewSkeleton}
                    resizedColumnId={oGridData.resizedColumnId}
                    data={oGridData.gridViewData}
                    visualData={oGridData.gridViewVisualData}
                    hierarchical={oGridData.hierarchical}
                    paginationData={oGridData.gridViewPaginationData}
                    totalItems={oGridData.gridViewTotalItems}
                    totalNestedItems={oGridData.gridViewTotalNestedItems}
                    searchText={oGridData.gridViewSearchText}
                    sortBy={oGridData.gridViewSortBy}
                    sortOrder={oGridData.gridViewSortOrder}
                    isGridDataDirty={oGridData.isGridDataDirty}
                    showCheckboxColumn={oGridData.showCheckboxColumn}
                    disableDeleteButton={oGridData.disableDeleteButton}
                    disableCreate={oGridData.disableCreate}
                    enableImportExportButton={oGridData.enableImportExportButton}
                    customActionView={oActionItems}
                    selectedColumns={oColumnOrganizerData.selectedColumns}
                    isColumnOrganizerDialogOpen={oColumnOrganizerData.isColumnOrganizerDialogOpen}
                    gridViewSearchBarPlaceHolder={oGridData.gridViewSearchBarPlaceHolder}/>
        </div>
    );
  };

  handleSelectedClassModuleChanged = (sSelectedClassModuleId) => {
    EventBus.dispatch(oEvents.HANDLE_SELECTED_CLASS_MODULE_CHANGED, sSelectedClassModuleId);
  };

  getClassTranslationsView = () => {
    var oGridView = this.getTranslationsGridView();

    return (
        <div className="configTranslationsTabViewContainer">
          {oGridView}
        </div>
    );
  };

  render() {
    let aClassModuleIdList = [
      SettingScreenModuleDictionary.CLASS_ARTICLE,SettingScreenModuleDictionary.CLASS_ASSET,
      SettingScreenModuleDictionary.CLASS_SUPPLIER,SettingScreenModuleDictionary.CLASS_TARGET,
      SettingScreenModuleDictionary.CLASS_TEXTASSET
    ];
    let sSelectedClassModule = this.props.selectedClassModule;

    return (
        <div className="translationsGridView">
          {CS.includes(aClassModuleIdList,sSelectedClassModule) ? this.getClassTranslationsView() : this.getTranslationsGridView()}
        </div>
      );
  }
}

export const view = TranslationsConfigView;
export const events = oEvents;
