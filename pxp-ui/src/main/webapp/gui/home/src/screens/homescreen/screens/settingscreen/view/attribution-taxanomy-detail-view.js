import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as ClassConfigSectionsView } from './class-config-sections-view';
import { view as ClassConfigDataTransferView } from './class-config-data-transfer-view';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view';
import ViewUtils from './utils/view-utils';
import SectionLayout from '../tack/attibution-taxanomy-layout-data';

const oEvents = {};

const oPropTypes = {
  activeTaxonomy: ReactPropTypes.object,
  taxonomyDetailedViewData: ReactPropTypes.object,
  isActiveTaxonomyDirty: ReactPropTypes.bool,
  iconLibraryData: ReactPropTypes.object
};

// @CS.SafeComponent
class AttributionTaxonomyDetailView extends React.Component {
  static propTypes = oPropTypes;

  getSectionsView = () => {
    var __props = this.props;
    var oActiveTaxonomy = __props.activeTaxonomy;
    let oTaxonomyDetailedViewData = __props.taxonomyDetailedViewData;
    return (
        <ClassConfigSectionsView
            sections={oActiveTaxonomy.sections}
            propertyCollectionModels={oTaxonomyDetailedViewData.propertyCollectionModels}
            referencedContexts={oTaxonomyDetailedViewData.referencedContexts}
            masterEntitiesForSection={oTaxonomyDetailedViewData.masterEntitiesForSection} context={"attributionDetail"}
        />
    );
  };

  getTaxonomyIconModel = () => {
    var oActiveTaxonomy = this.props.activeTaxonomy;
    return {
      icon: oActiveTaxonomy.icon,
      context: "attributionTaxonomyDetail",
      iconKey: oActiveTaxonomy.iconKey,
    }
  };

  getLazyMSSModel = (aItems, aSelectedIds, sContext, bIsSingleSelect, aDisabledItems, oReferencedData, oRegResponseInfo, isMultiselect) => {
    aSelectedIds = CS.isEmpty(aSelectedIds) ? [] : aSelectedIds;
    return {
      disabledItems: aDisabledItems || [],
      items: aItems,
      selectedItems: aSelectedIds,
      context: sContext,
      singleSelect: !!bIsSingleSelect,
      referencedData: oReferencedData,
      requestResponseInfo: oRegResponseInfo,
      isMultiSelect: isMultiselect
    }
  };

  getLazyMSSRequestResponseInfo = (sEntity) => {
    return {
      requestType: "configData",
      entityName: sEntity,
    }
  }

  getTaxonomyDetailModel = () => {
    let oActiveTaxonomy = this.props.activeTaxonomy;
    let oTaxonomyDetailedViewData = this.props.taxonomyDetailedViewData;
    let sContext = "attributionTaxonomyDetail";
    let sSplitter = ViewUtils.getSplitter();
    let sDataRuleContext = sContext + sSplitter + "dataRules";
    let sTasksContext = sContext + sSplitter + "tasks";

    return {
      id: oActiveTaxonomy.id,
      label: oActiveTaxonomy.label,
      code: oActiveTaxonomy.code || "",
      icon: this.getTaxonomyIconModel(),
      dataRules: this.getLazyMSSModel(oTaxonomyDetailedViewData.ruleList, oActiveTaxonomy.dataRules, sDataRuleContext, false, [],
          oTaxonomyDetailedViewData.referencedDataRules, this.getLazyMSSRequestResponseInfo("dataRules"), true),
      tasks: this.getLazyMSSModel(oTaxonomyDetailedViewData.taskList, oActiveTaxonomy.tasks, sTasksContext, false, [],
          oTaxonomyDetailedViewData.referencedTasks, this.getLazyMSSRequestResponseInfo("tasks"), true),
      embeddedKlass: this.getDataTransferView(),
      showSelectIconDialog: this.props.activeTaxonomy.showSelectIconDialog,
      selectIconData: this.props.iconLibraryData,
    }
  };

  getDataTransferView = () => {
    let oActiveTaxonomy = this.props.activeTaxonomy;
    let oTaxonomyDetailedViewData = this.props.taxonomyDetailedViewData;
    let sContext = "attributionTaxonomyDetail";
    let sSplitter = ViewUtils.getSplitter();
    let sEmbeddedKlassContext = sContext + sSplitter + "embeddedKlassIds";
    let oModel = {
      embeddedKlassDropdown: this.getLazyMSSModel(oTaxonomyDetailedViewData.embeddedKlassList, oActiveTaxonomy.embeddedKlassIds, sEmbeddedKlassContext, false, [],
          oTaxonomyDetailedViewData.referencedKlasses, oTaxonomyDetailedViewData.requestResponseObjectForEmbedded, true)
    };
    let oReferencedData = {
      referencedKlasses: oTaxonomyDetailedViewData.referencedKlasses,
      referencedTags: oTaxonomyDetailedViewData.referencedTags,
      referencedAttributes: oTaxonomyDetailedViewData.referencedAttributes
    };

    return (
        <ClassConfigDataTransferView activeClass={oActiveTaxonomy}
                                     propertyMSSModel={oModel}
                                     property={"embeddedKlassIds"}
                                     isDeleteKlassEnabled={true}
                                     isAddKlassEnabled={true}
                                     referencedData={oReferencedData}
                                     section="embeddedKlass"
                                     context="attributionTaxonomy"
        />
    );
  };

  render() {
    let oSectionsView = this.getSectionsView();
    let sAttributionTaxonomyDetailViewClass = "attributionTaxonomyDetailView";
    let aDisabledFields = ["code"];
    if (this.props.isActiveTaxonomyDirty) {
      sAttributionTaxonomyDetailViewClass += " dirty";
    }

    let aSectionLayout = new SectionLayout();
    return (<div className={sAttributionTaxonomyDetailViewClass}>
      <div className={"attributionTaxonomyViewContainer"}>
        <CommonConfigSectionView context="attributionTaxonomyDetail" sectionLayout={aSectionLayout}
                                 disabledFields={aDisabledFields}
                                 data={this.getTaxonomyDetailModel()}/>
      </div>
      {oSectionsView}
    </div> );
  }
}

export const view = AttributionTaxonomyDetailView;
export const events = oEvents;
