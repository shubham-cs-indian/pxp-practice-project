import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as ListView } from '../../../../../viewlibraries/listview/list-view.js';
import { view as BlackListEditableView } from './black-list-editable-view';
import { view as ConfigEntityCreationDialogView } from './config-entity-creation-dialog-view';
import { view as ConfigHeaderView } from '../../../../../viewlibraries/configheaderview/config-header-view';
import SectionLayout from '../tack/rule-list-config-layout-data';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';
import {view as NothingFoundView} from "../../../../../viewlibraries/nothingfoundview/nothing-found-view";
import ManageEntityConfigProps from "../store/model/manage-entity-config-props";
import {view as ManageEntityDialogView} from "./entity-usage-dialog-view";

const oEvents = {
  RULE_LIST_LABEL_CHANGED: "rule_list_label_changed",
};

const oPropTypes = {
  actionItemModel: ReactPropTypes.object.isRequired,
  listOfRuleListModel: ReactPropTypes.array.isRequired,
  activeRuleList: ReactPropTypes.object.isRequired,
  bRuleListCreated: ReactPropTypes.bool,
  isRuleListDirty: ReactPropTypes.bool,
  showLoadMore: ReactPropTypes.bool,
};

// @CS.SafeComponent
class RuleListConfigView extends React.Component {
  static propTypes = oPropTypes;

  handleRuleListLabelChanged = (oEvent) => {
    var __props = this.props;
    var sNewValue = CS.trim(oEvent.target.value);
    if(__props.activeRuleList.label != sNewValue) {
      EventBus.dispatch(oEvents.RULE_LIST_LABEL_CHANGED, null, sNewValue);
    }
  };

  getRuleListView = () => {
    var aListViewModels = this.props.listOfRuleListModel;
    if(CS.isEmpty(aListViewModels)){
      return <NothingFoundView message={getTranslation().NOTHING_TO_DISPLAY}/>;
    }
    return (<ListView model={aListViewModels} bListItemCreated={this.props.bRuleListCreated} bEnableLoadMore={this.props.showLoadMore}
                      context={"ruleList"} hideSearchBar={true}/>);
  };

  getRuleListDetailModel =()=> {
    let oActiveRuleList = this.props.activeRuleList;
    let oBlacListEditableViewDOM = this.getBlackListEditableView();
    return {
      label: CS.getLabelOrCode(oActiveRuleList),
      code: oActiveRuleList.code,
      blackListedWords: oBlacListEditableViewDOM,
    }
  };

  getBlackListEditableView = () => {
    var oActiveRuleList = this.props.activeRuleList;
    let sContext = "ruleList";
    return (
        <BlackListEditableView
            list={oActiveRuleList.list}
            context={sContext}
        />);
  };

  getRuleListDetailView = () => {
    var __props = this.props;
    if(CS.isEmpty(__props.activeRuleList)) {
      return null;
    }else {
      let oRuleListModel = this.getRuleListDetailModel();
      let aRuleListSectionLayout = SectionLayout().ruleListConfigData;
      let aDisabledFields = ["code"];
      let sContext = "ruleList";
      return(
          <div className="ruleListDetailWrapper">
            <CommonConfigSectionView context={sContext} data={oRuleListModel}
                                     sectionLayout={aRuleListSectionLayout}
                                     disabledFields={aDisabledFields}/>

          </div>
      );
    }
  };

  createMappingDialog = (oActiveRuleList, sEntityType) => {
    return (
        <ConfigEntityCreationDialogView
            activeEntity={oActiveRuleList}
            entityType={sEntityType}
        />
    )
  };

  getEntityDialog = () => {
    let oEntityDatList = ManageEntityConfigProps.getDataForDialog();
    let bIsDelete = ManageEntityConfigProps.getIsDelete();
    return (
        <ManageEntityDialogView
            oEntityDatList = {oEntityDatList}
            bIsDelete={bIsDelete}
        />
    );
  };

  render() {
    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;
    var aRuleListActionGroup = this.props.actionItemModel.RuleListView;
    var aRuleListView = this.getRuleListView();
    var oRuleListDetailView = this.getRuleListDetailView();
    var oActiveRuleList = this.props.activeRuleList;
    var oCreateMappingDialog = null;
    if (!CS.isEmpty(oActiveRuleList)) {
      oCreateMappingDialog = this.createMappingDialog(oActiveRuleList, 'ruleList');
    }
    let oSearchBarProps = {searchText: this.props.searchText};

    return (
        <div className="ruleListConfigViewContainer">
          {oManageEntityDialog}
          <ConfigHeaderView actionButtonList={aRuleListActionGroup}
                            showSaveDiscardButtons={this.props.isRuleListDirty}
                            searchBarProps={oSearchBarProps}
                            context={"ruleList"}/>
          <div className="listOfRuleListViewContainer">
            {aRuleListView}
          </div>
          <div className="ruleListDetailedViewContainer">
            {oRuleListDetailView}
            {oCreateMappingDialog}
          </div>
        </div>
    );
  }
}

export const view = RuleListConfigView;
export const events = oEvents;
