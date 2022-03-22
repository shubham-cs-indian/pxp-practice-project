import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import TooltipView from '../tooltipview/tooltip-view';
import { view as ImageFitToContainerView } from '../imagefittocontainerview/image-fit-to-container-view';
import ContextMenuViewModel from '../contextmenuwithsearchview/model/context-menu-view-model';
import ViewUtils from '../utils/view-library-utils';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import SmallTaxonomyViewModel from './model/small-taxonomy-view-model';
import { view as AddTaxonomyPopoverView } from '../addtaxonomypopoverview/add-taxonomy-popover-view';
import TaxonomyBaseTypeDictionary from '../../commonmodule/tack/mock-data-for-taxonomy-base-types-dictionary'
import ClassNameFromBaseTypeDictionary from '../../commonmodule/tack/class-name-base-types-dictionary';

const oEvents = {
  MULTI_TAXONOMY_CROSS_ICON_CLICKED: 'multi_taxonomy_cross_icon_clicked'
};

const oPropTypes = {
  model: ReactPropTypes.instanceOf(SmallTaxonomyViewModel).isRequired,
  context: ReactPropTypes.string,
  isDisabled: ReactPropTypes.bool,
  paginationData: ReactPropTypes.object,
  showAllComponents: ReactPropTypes.bool,
  localSearch: ReactPropTypes.bool,
  hideAddTaxonomyButton: ReactPropTypes.bool,
  allowedTaxonomyHierarchyList: ReactPropTypes.object,
  nothingSelectedMessage: ReactPropTypes.string
};
/**
 * @class SmallTaxonomyView - use for Add Taxonomy in overview tab.
 * @memberOf Views
 * @property {custom} model - pass model name.
 * @property {string} [context] - pass context name.
 * @property {bool} [isDisabled] -  boolean which is used for isDisabled or not.
 * @property {object} [paginationData] -  an object which contain paginationData.
 * @property {bool} [showAllComponents] -  boolean which is used for showAllComponents or not.
 * @property {bool} [localSearch] -  boolean which is used for localSearch or not.
 * @property {bool} [hideAddTaxonomyButton] -  boolean which is used for hideAddTaxonomyButton or multi.
 */

// @CS.SafeComponent
class SmallTaxonomyView extends React.Component{

  constructor(props) {
    super(props);

    this.state = {
      showItemHandlerPopover: false,
      showFurtherClassificationPopover: false
    }
  }

  getItemIconClassName = (sBaseType) => {
    let sMasterTaxonomyClassName = ClassNameFromBaseTypeDictionary[TaxonomyBaseTypeDictionary.masterTaxonomy];
    return CS.isNotEmpty(sBaseType) ? ClassNameFromBaseTypeDictionary[sBaseType] : sMasterTaxonomyClassName;
  };

  getContextItemModelList = (aItemList, oAllowedTaxonomyHierarchyList) => {
    let _this = this;
    var aItemModels = [];
    let sViewContext = this.props.context;
    CS.forEach(aItemList, function (oItem) {
      let sBaseType = oItem.baseType;
      let oProperties = {
        context: 'Taxonomy',
        viewContext: sViewContext,
        code: oItem.code,
        customIconClassName : _this.getItemIconClassName(sBaseType)
      };

      if(!CS.isEmpty(oAllowedTaxonomyHierarchyList)) {
        oProperties.taxonomyHierarchy = oAllowedTaxonomyHierarchyList[oItem.id];
      }

      aItemModels.push(new ContextMenuViewModel(
          oItem.id,
          CS.getLabelOrCode(oItem),
          false,
          oItem.iconKey,
          oProperties
      ));
    });
    return aItemModels;
  }

  handleRemoveTaxonomyClicked =(oTaxonomy, sParentTaxonomyId)=> {
    let sTaxonomyType = "majorTaxonomy";
    if (this.props.context === "property_collection_taxonomy") {
      sTaxonomyType = "minorTaxonomy";
    }
    EventBus.dispatch(oEvents.MULTI_TAXONOMY_CROSS_ICON_CLICKED, this, oTaxonomy, sParentTaxonomyId, this.props.context, sTaxonomyType);
  }

  getFurtherClassificationView =(aFurtherClassificationList, sTaxonomyId, oAllowedTaxonomyHierarchyList)=> {
    var aFurtherClassificationModelList = this.getContextItemModelList(aFurtherClassificationList, oAllowedTaxonomyHierarchyList);
    var sHandlerRef = sTaxonomyId;
    var sHandlerText = getTranslation().CLASSIFY_FURTHER;
    var oModel = this.props.model;
    var oProperties = {};
    oProperties.context = "ClassifyFurther";
    var oTaxonomyPopoverViewModel = this.getAddTaxonomyPopoverViewModel('', sHandlerText, aFurtherClassificationModelList, true, sHandlerRef, oProperties);
    return <AddTaxonomyPopoverView context={this.props.context} taxonomyId={sTaxonomyId} model={oTaxonomyPopoverViewModel} paginationData={this.props.paginationData} localSearch={this.props.localSearch} showCustomIcon={this.props.showCustomIcon}/>;
  }

  getTaxonomyItemView =(oTaxonomyItem, sParentTaxonomyId)=> {
    var sTaxonomyItemClassName = "taxonomySelectionItem ";
    var aTaxonomyItemView = [];
    var oTaxonomyIconView;
    var oTaxonomyLabelView;
    var oModel = this.props.model;
    let bShowAllComponents = this.props.showAllComponents;
    var oHeaderPermission = oModel.properties["headerPermission"];
    var bCanDeleteTaxonomy = !this.props.isDisabled;
    let sBaseType = oTaxonomyItem.baseType;
    if(!CS.isEmpty(oHeaderPermission)) {
      bCanDeleteTaxonomy = oHeaderPermission.canDeleteTaxonomy;
    }

    if (oTaxonomyItem.iconKey) {
      var sSrc = ViewUtils.getIconUrl(oTaxonomyItem.iconKey);
      oTaxonomyIconView = <div className="taxonomyIcon" key="taxonomyIcon"><ImageFitToContainerView imageSrc={sSrc}/></div>;
    } else {
      let sIconClassName = this.getItemIconClassName(sBaseType);
      oTaxonomyIconView = <div className={"taxonomyIcon " + sIconClassName} key="taxonomyIcon"></div>;
    }
    aTaxonomyItemView.push(oTaxonomyIconView);

    var sLabel = CS.getLabelOrCode(oTaxonomyItem);
    oTaxonomyLabelView = <div className="taxonomyLabel" key="taxonomyLabel">{sLabel}</div>;
    aTaxonomyItemView.push(oTaxonomyLabelView);

    if(oTaxonomyItem.selected){
      let oTaxonomySelectedView = <div className="taxonomySelected" key="taxonomySelected"/>;
      aTaxonomyItemView.push(oTaxonomySelectedView);
    }
    if((bShowAllComponents || !aTaxonomyItemView.selected) && bCanDeleteTaxonomy && !this.props.isDisabled) {
      sTaxonomyItemClassName += "withRemoveIcon ";
      let oRemoveClassView = <div className="removeClass" key="removeClass" onClick={this.handleRemoveTaxonomyClicked.bind(this, oTaxonomyItem, sParentTaxonomyId)}/>;
      aTaxonomyItemView.push(oRemoveClassView);
    }

    return (
        <TooltipView placement="bottom" label={CS.getLabelOrCode(oTaxonomyItem)} key={oTaxonomyItem.id}>
          <div className={sTaxonomyItemClassName}>
            {aTaxonomyItemView}
          </div>
        </TooltipView>
    );
  }

  getAddedClassificationList =(aAddedClassificationList, sTaxonomyId)=> {
    var _this = this;
    return CS.map(aAddedClassificationList, function (oItem) {
      return _this.getTaxonomyItemView(oItem, sTaxonomyId);
    });
  }

  getSelectedTaxonomy = (oModel, oAllowedTaxonomyHierarchyList) => {
    var aSelectedTaxonomy = oModel.selectedTaxonomyList;
    if (CS.isEmpty(aSelectedTaxonomy) && CS.isNotEmpty(this.props.nothingSelectedMessage)) {
      return (
          <div className={"noTaxonomySelectedView"}>
            {this.props.nothingSelectedMessage}
          </div>
      );
    }

    var _this = this;
    let bShowAllComponents = _this.props.showAllComponents;
    var oHeaderPermission = oModel.properties["headerPermission"];
    var bCanAddTaxonomy = oHeaderPermission.canAddTaxonomy;
    return CS.map(aSelectedTaxonomy, function (oTaxonomy) {
      var aAddedClassificationList = oTaxonomy.addedClassificationList;
      var aFurtherClassificationList = oTaxonomy.furtherClassificationList;
      var sTaxonomyId = oTaxonomy.id;
      var aAddedClassificationView = _this.getAddedClassificationList(aAddedClassificationList, sTaxonomyId);
      var oFurtherClassificationView = (bShowAllComponents || bCanAddTaxonomy && !_this.props.isDisabled) ? _this.getFurtherClassificationView(aFurtherClassificationList, sTaxonomyId, oAllowedTaxonomyHierarchyList) : [];
      return (<div className="selectedTaxonomyWrapper" key={sTaxonomyId}>
        <div className="addedClassificationListWrapper">{aAddedClassificationView}</div>
        <div className="furtherClassificationListWrapper">{oFurtherClassificationView}</div>
      </div>)
    });
  }

  getAddTaxonomyPopoverViewModel = (sId, sLabel, aModelList, bCaretVisibilty, sContext, oProperties) => {
    return ({
      id: sId,
      label: sLabel,
      contextMenuModelList: aModelList,
      caretVisibility: bCaretVisibilty,
      context: sContext,
      properties: oProperties
    });
  };

  render() {
    let oProps = this.props;
    var isDisabled = oProps.isDisabled;
    var oModel = oProps.model;
    var oAllowedTaxonomyHierarchyList = oProps.allowedTaxonomyHierarchyList;
    var aContextMenuModelList = this.getContextItemModelList(oModel.contextMenuModelList, oAllowedTaxonomyHierarchyList);
    var aSelectedTaxonomyView = this.getSelectedTaxonomy(oModel, oAllowedTaxonomyHierarchyList);
    var sHandlerText = oModel.handlerText;
    var sContext = oModel.context;

    let bShowAllComponents = oProps.showAllComponents;
    let bHideAddTaxonomyButton = oProps.hideAddTaxonomyButton;
    let bShowAddTaxonomyButton = (bShowAllComponents && !bHideAddTaxonomyButton);
    var oHeaderPermission = oModel.properties["headerPermission"];
    //var bShowTaxonomy = true;
    var bShowAddTaxonomy = oHeaderPermission.canAddTaxonomy;
    var sHandlerRef = 'addItemHandler' + sContext;
    var oProperties = {};
    oProperties.context = "Taxonomy";
    var oTaxonomyPopoverViewModel = this.getAddTaxonomyPopoverViewModel('', sHandlerText, aContextMenuModelList, false, sHandlerRef, oProperties);
    return (<div className="smallTaxonomyViewContainer">
      <div className="taxonomyDataWrapper">{aSelectedTaxonomyView}</div>
      {(bShowAddTaxonomyButton && !isDisabled) ?
       <div className="taxonomyHandler"><AddTaxonomyPopoverView context={this.props.context}
                                                                taxonomyId={oModel.rootTaxonomyId}
                                                                model={oTaxonomyPopoverViewModel}
                                                                paginationData={this.props.paginationData}
                                                                localSearch={this.props.localSearch}
                                                                showCustomIcon={this.props.showCustomIcon}/></div>
          : null}
    </div>);
  }

}

SmallTaxonomyView.propTypes = oPropTypes;

export const view = SmallTaxonomyView;
export const events = oEvents;
