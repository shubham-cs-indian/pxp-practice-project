import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as ContextualGridView } from '../../../../../viewlibraries/contextualgridview/contextual-grid-view';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as KPIDetailedConfigView } from './kpi-detail-config-view';
import { view as TabLayoutView } from '../../../../../viewlibraries/tablayoutview/tab-layout-view';
import { view as ConfigEntityCreationDialogView } from './config-entity-creation-dialog-view';
import KPITabLayoutData from '../tack/kpi-tab-layout-data';

const oEvents = {
  HANDLE_KPI_CONFIG_DIALOG_BUTTON_CLICKED: "handle kpi config dialog button clicked",
  HANDLE_KPI_CONFIG_TAB_CHANGED: "handle kpi config tab changed"
};

const oPropTypes = {
  gridViewProps: ReactPropTypes.object,
  referencedData: ReactPropTypes.object,
  activeKPI: ReactPropTypes.object,
  activeBlock: ReactPropTypes.object,
  isDialogOpen: ReactPropTypes.bool,
  activeTabId: ReactPropTypes.string,
  ruleList: ReactPropTypes.array,
  aAttributeListForMSS: ReactPropTypes.array,
  oRelationshipListForMSS: ReactPropTypes.object,
  aTagListForMSS: ReactPropTypes.array,
  oDashboardTabsDataForMss: ReactPropTypes.object,
  taxonomyPaginationData: ReactPropTypes.object,
  referencedAttributes: ReactPropTypes.object,
  referencedTags: ReactPropTypes.object,
  physicalCatalogIdsData: ReactPropTypes.array,
  portalIdsData: ReactPropTypes.array,
  lazyMSSReqResInfo: ReactPropTypes.object,
  isKPIDirty: ReactPropTypes.bool,
  isGridViewColumnOrganizerDialogActive: ReactPropTypes.bool
};

// @CS.SafeComponent
class KpiConfigView extends React.Component {
  static propTypes = oPropTypes;

  handleDialogButtonClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.HANDLE_KPI_CONFIG_DIALOG_BUTTON_CLICKED, sButtonId);
  };

  handleKPITabChanged = (sTabId) => {
    EventBus.dispatch(oEvents.HANDLE_KPI_CONFIG_TAB_CHANGED, sTabId);
  };

  getDetailViewDialog = () => {
    let oProps = this.props;

    if (oProps.isDialogOpen) {
      let oBodyStyle = {
        maxWidth: "1200px",
        minWidth: "800px",
        width: "100%",
        height:"80%"
      };
      var aButtonData = [];

      if (this.props.isKPIDirty) {
        aButtonData = [
          {
            id: "cancel",
            label: getTranslation().DISCARD,
            isDisabled: false,
            isFlat: false,
          },
          {
            id: "save",
            label: getTranslation().SAVE,
            isDisabled: false,
            isFlat: false,
          }];
      } else {
        aButtonData = [{
          id: "ok",
          label: getTranslation().OK,
          isFlat: false,
          isDisabled: false
        }];
      }
      let fButtonHandler = this.handleDialogButtonClicked;
      let sContext = "kpiConfigDetail";
      let oKPIDetailViewDOM = (<KPIDetailedConfigView activeKPI={oProps.activeKPI}
                                                referencedData={oProps.referencedData}
                                                context={sContext}
                                                activeTabId={oProps.activeTabId}
                                                aAttributeListForMSS={oProps.aAttributeListForMSS}
                                                oRelationshipListForMSS={oProps.oRelationshipListForMSS}
                                                aTagListForMSS={oProps.aTagListForMSS}
                                                oDashboardTabsDataForMss={oProps.oDashboardTabsDataForMss}
                                                activeBlock={oProps.activeBlock}
                                                taxonomyPaginationData={this.props.taxonomyPaginationData}
                                                referencedAttributes={this.props.referencedAttributes}
                                                referencedTags={this.props.referencedTags}
                                                physicalCatalogIdsData={this.props.physicalCatalogIdsData}
                                                portalIdsData={this.props.portalIdsData}
                                                lazyMSSReqResInfo={this.props.lazyMSSReqResInfo}/>);

      let oTabLayoutViewDOM = (
          <div className="kpiConfigDetailTabViewWrapper">
            <TabLayoutView tabList={KPITabLayoutData()} activeTabId={oProps.activeTabId}
                           onChange={this.handleKPITabChanged}
                           addBorderToBody={true}>{oKPIDetailViewDOM}</TabLayoutView>
          </div>);

      return (<CustomDialogView modal={true} open={true}
                                title={getTranslation().KPI_DETAILS}
                                bodyStyle={oBodyStyle}
                                contentStyle={oBodyStyle}
                                bodyClassName=""
                                contentClassName="kpiConfigDetail"
                                buttonData={aButtonData}
                                onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                                buttonClickHandler={fButtonHandler}
                                children={oTabLayoutViewDOM}>
      </CustomDialogView>);
    } else {
      return null;
    }
  };

  getCreateKpiDialog = (oActiveEntity, sEntityType) => {
    return (
        <ConfigEntityCreationDialogView
            activeEntity={oActiveEntity}
            entityType={sEntityType}
        />
    )
  };

  render() {
    let oDetailViewDialog = this.getDetailViewDialog();
    var oActiveKpi = this.props.activeKPI;
    var oCreateKpiDialog = !CS.isEmpty(oActiveKpi) ? this.getCreateKpiDialog(oActiveKpi,"kpiConfig") : null;
    let oColumnOrganizerData = this.props.columnOrganizerData;

    return (
        <div className="configGridViewContainer" key="kpiConfigGridViewContainer">
          <ContextualGridView {...this.props.gridViewProps}
                    selectedColumns={oColumnOrganizerData.selectedColumns}
                    isColumnOrganizerDialogOpen={oColumnOrganizerData.isColumnOrganizerDialogOpen}/>
          {oDetailViewDialog}
          {oCreateKpiDialog}
        </div>
    );
  }
}

export const view = KpiConfigView;
export const events = oEvents;
