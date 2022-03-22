import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations } from '../../../../../commonmodule/store/helper/translation-manager';

import { view as RolePermissionContainerView } from './role-permission-container-view';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';
import { view as SystemsSelectionView } from './systems-selection-view';
import { view as TabLayoutView } from '../../../../../viewlibraries/tablayoutview/tab-layout-view';
import { view as SmallTaxonomyView } from '../../../../../viewlibraries/small-taxonomy-view/small-taxonomy-view';
import SmallTaxonomyViewModel from './../../../../../viewlibraries/small-taxonomy-view/model/small-taxonomy-view-model';
import ViewUtils from './utils/view-utils';
import SectionLayout from '../tack/organisation-config-layout-data';
import OrganisationConfigTabData from '../tack/organisation-config-tab-layout';
import MockDataForOrganisationConfigTypes from './../tack/mock/mock-data-for-organisation-config-types';
import MockDataForOrganisationConfigPhysicalCatalogAndPortal from '../../../../../commonmodule/tack/mock-data-physical-catalog-and-portal-types';
import Constants from '../../../../../commonmodule/tack/constants';
import PhysicalCatalogDictionary from '../../../../../commonmodule/tack/physical-catalog-dictionary';
const oTranslations = getTranslations('SettingScreenTranslations');

const oEvents = {
  HANDLE_ORGANISATION_TAB_CHANGED: "handle_organisation_tab_changed"
};

const oPropTypes = {
  activeOrganisation: ReactPropTypes.object.isRequired,
  organisationBasicInformationData: ReactPropTypes.object,
  ruleConfigView: ReactPropTypes.object,
  context: ReactPropTypes.string,
  activeTabId: ReactPropTypes.string,
  isPermissionVisible: ReactPropTypes.bool,
  systemList: ReactPropTypes.array,
  hideRuleConfigPermissionToggle: ReactPropTypes.bool,
  systemsSelectionViewModel: ReactPropTypes.object,
  hideSystemsSelectionView: ReactPropTypes.bool,
  ssoSettingView: ReactPropTypes.object,
  iconLibraryData: ReactPropTypes.object,
};

// @CS.SafeComponent
class OrganisationConfigDetailView extends React.Component{
  static propTypes = oPropTypes;

  handleOrganisationTabChanged =(sTabId)=> {
    EventBus.dispatch(oEvents.HANDLE_ORGANISATION_TAB_CHANGED, sTabId);
  }

  getMSSModel =(aItems, aSelectedItems, sContext, sInnerContext, bIsMultiSelect, bIsDisabled, bCannotRemove)=> {
    let sSplitter = ViewUtils.getSplitter();
    let sMssContext = `${sContext}${sSplitter}${sInnerContext}`;
    return {
      label: "",
      items: aItems,
      selectedItems: aSelectedItems,
      isMultiSelect: bIsMultiSelect,
      context: sMssContext,
      disabled: bIsDisabled,
      cannotRemove: bCannotRemove
    }
  }

  getSelectionToggleModel =(aItems, aSelectedItems, sContext, sInnerContext, bIsSingleSelect, aDisabledItems)=> {
    return {
      selectedItems: aSelectedItems,
      items: aItems,
      context: sContext,
      contextKey: sInnerContext,
      singleSelect: bIsSingleSelect,
      disabledItems: aDisabledItems || []
    }
  }

  getSmallTaxonomyViewModel =(oMultiTaxonomyData)=> {
    var oSelectedMultiTaxonomyList = oMultiTaxonomyData.selected;
    var oMultiTaxonomyListToAdd = oMultiTaxonomyData.notSelected;
    var sRootTaxonomyId = ViewUtils.getTreeRootId();
    var oProperties = {};
    oProperties.headerPermission = {};
    return new SmallTaxonomyViewModel('', oSelectedMultiTaxonomyList, oMultiTaxonomyListToAdd, oTranslations.ADD_TAXONOMY, 'forMultiTaxonomy', sRootTaxonomyId, oProperties)
  }


  getGetAvailabilityView =()=> {
    let oReferencedData = this.props.organisationBasicInformationData;

    let oMultiTaxonomyData = oReferencedData.multiTaxonomyData;
    let oAllowedTaxonomyHierarchyList = oMultiTaxonomyData.allowedTaxonomyHierarchyList;
    var oSmallTaxonomyViewModel = this.getSmallTaxonomyViewModel(oReferencedData.multiTaxonomyData);
    let oTaxonomyPaginationData = oReferencedData.taxonomyPaginationData;

    return (<SmallTaxonomyView model={oSmallTaxonomyViewModel}
                               allowedTaxonomyHierarchyList={oAllowedTaxonomyHierarchyList}
                               showCustomIcon={true}
                               isDisabled={false}
                               context={this.props.context}
                               showAllComponents={true}
                               paginationData={oTaxonomyPaginationData}/>);
  }

  getEndpointSelectionView =()=> {
    let oProps = this.props;
    let oSystemsSelectionViewModel = oProps.systemsSelectionViewModel;
    if (oProps.hideSystemsSelectionView) {
      return null;
    }

    return (
        <SystemsSelectionView
            contextMenuViewModel={oSystemsSelectionViewModel.contextMenuViewModel}
            endpointsSelectionViewModels={oSystemsSelectionViewModel.endpointsSelectionViewModels}
            context={oSystemsSelectionViewModel.context}
            selectedItems={oSystemsSelectionViewModel.selectedItems}/>
    );
  };

  getIconModel = (oActiveOrganisation) => {
      return ({
        icon: oActiveOrganisation.icon,
        context: "organisation",
        iconKey: oActiveOrganisation.iconKey,
      });
  };

  getBasicInformationModel =()=> {
    let oProps = this.props;
    let oActiveOrganisation = oProps.activeOrganisation;
    let sViewContext = oProps.context;

    let aSelectedCoreOrganisationItems = CS.isEmpty(oActiveOrganisation.type) ? [] : [oActiveOrganisation.type];
    let aSelectedAvailabilityItemsForPhysicalCatalog = CS.isEmpty(oActiveOrganisation.physicalCatalogs) ? [] : oActiveOrganisation.physicalCatalogs;
    let aSelectedAvailabilityItemsForPortals = CS.isEmpty(oActiveOrganisation.portals) ? [] : oActiveOrganisation.portals;


    return {
      label: oActiveOrganisation.label,
      code: oActiveOrganisation.code,
      icon: this.getIconModel(oActiveOrganisation),
      type: this.getMSSModel(MockDataForOrganisationConfigTypes(), aSelectedCoreOrganisationItems, sViewContext, "type", false, true, true),
      availability: this.getGetAvailabilityView(),
      targetClasses: this.props.organisationBasicInformationData.multiKlassData,
      physicalCatalogs: this.getSelectionToggleModel(MockDataForOrganisationConfigPhysicalCatalogAndPortal.physicalCatalogs(), aSelectedAvailabilityItemsForPhysicalCatalog, sViewContext, "physicalCatalogs", false, [PhysicalCatalogDictionary.PIM]),
      portals: this.getSelectionToggleModel(MockDataForOrganisationConfigPhysicalCatalogAndPortal.portals(), aSelectedAvailabilityItemsForPortals, sViewContext, "portals", false),
      showSelectIconDialog: oActiveOrganisation.showSelectIconDialog,
      selectIconData: this.props.iconLibraryData,
    }
  }


  getBasicInformationView =()=> {
    let aBasicInformationLayout = SectionLayout().organisationConfigBasicInformation;
    let oBasicInformationModel = this.getBasicInformationModel();
    let oProps = this.props;
    let oEndpointSelectionView = this.getEndpointSelectionView();
    var aDisabledFields = ["code"];

    return (
        <div className="organizationBasicInformationViewContainer">
          <CommonConfigSectionView context={oProps.context} data={oBasicInformationModel}
                                   sectionLayout={aBasicInformationLayout}
                                   disabledFields={aDisabledFields}/>
          {oEndpointSelectionView}
        </div>
        );
  }

  getRolesView =()=> {
    return (
        <div className={"roles"}>
          <RolePermissionContainerView
              ruleConfigView={this.props.ruleConfigView}
              isPermissionVisible={this.props.isPermissionVisible}
              hideRuleConfigPermissionToggle={this.props.hideRuleConfigPermissionToggle}
          />
        </div>
    );
  }

  getSSOSettingView = () => {
    return (
      <div className={"ssoSettings"}>
        {this.props.ssoSettingView}
      </div>
    );
  };

  getView =()=> {
    let sTabId = this.props.activeTabId;
    switch (sTabId) {
      case Constants.ORGANISATION_CONFIG_INFORMATION:
        return (this.getBasicInformationView());
      case Constants.ORGANISATION_CONFIG_ROLES:
        return (this.getRolesView());
      case Constants.ORGANISATION_CONFIG_SSO_SETTING:
        return (this.getSSOSettingView())
    }
  }

  render(){
    return (<div className={"organisationConfigDetailView"}>
      <TabLayoutView
          activeTabId={this.props.activeTabId}
          onChange={this.handleOrganisationTabChanged}
          tabList={new OrganisationConfigTabData()}
          addBorderToBody={true}>
        {this.getView()}
      </TabLayoutView>
    </div> )
  }

}

export const view = OrganisationConfigDetailView;
export const events = oEvents;
