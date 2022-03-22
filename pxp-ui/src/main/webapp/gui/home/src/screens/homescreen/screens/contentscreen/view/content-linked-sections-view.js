import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactDOM from 'react-dom';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ExceptionLogger from '../../../../../libraries/exceptionhandling/exception-logger';
import { view as ExpandableMenuListView } from '../../../../../viewlibraries/expandablemenulistview/expandable-menu-list-view';
import ViewUtils from '../../../../../viewlibraries/utils/view-library-utils';
import { view as ImageFitToContainerView } from '../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view';
import CommonUtils from '../../../../../commonmodule/util/common-utils';

const oEvents = {
  CONTENT_LINKED_SECTION_VIEW_LINK_ITEM_CLICKED: "content_linked_section_view_link_item_clicked"
};

const oPropTypes = {
  selectedElementInformation: ReactPropTypes.object, //todo: make it shape
  linkItemsData: ReactPropTypes.array,
  sectionComponents: ReactPropTypes.array,
  isHelperLanguageSelected: ReactPropTypes.bool,
  helperLanguages: ReactPropTypes.array,
  showExpandedMenuList: ReactPropTypes.bool,
  sectionHeaderData: ReactPropTypes.object,
  linkedItemClicked: ReactPropTypes.func,
};

const oDefaultProps = {
};

// @CS.SafeComponent
class ContentLinkedSectionsView extends React.Component {

  constructor (props) {
    super(props);

    this.toggleButtonClicked = this.toggleButtonClicked.bind(this);
    this.setSelectedSection = this.setSelectedSection.bind(this);
    this.linkItemClicked = this.linkItemClicked.bind(this);
    this.setPreventScroll = this.setPreventScroll.bind(this);
    this.preventScroll = false;
    this.selectedSectionId = "";
    this.timer = null;

    this.setRef = (sContext, element) => {
      if (!CS.isEmpty(element)) {
        this[sContext] = element;
      }
    };

    this.state = {
      selectedSectionId: "",
      changeSection: false
    }
  }

  componentDidUpdate = () => {
    this.checkAndSelectFirstSection();
    this.makeSelectionOfActiveSection();
  };

  componentDidMount = () => {
    this.checkAndSelectFirstSection();
    this.setPreventScroll(true);
    this.setSelectedSection(this.state.selectedSectionId);
  };

  static getDerivedStateFromProps = (oNextProps, oState) => {
    let oSelectedElementInformation = oNextProps.selectedElementInformation;
    if(oState.changeSection){
      return {changeSection: false};
    }else {
      return {selectedSectionId: oSelectedElementInformation.sectionId};
    }
  };

  makeSelectionOfActiveSection = () => {
    let sCurrentSectionId = this.state.selectedSectionId;
    let oExpandableComponent = this["expandableMenu"];
    try {
      if(oExpandableComponent) {
        let oExpandableDOM = ReactDOM.findDOMNode(oExpandableComponent);
        if(oExpandableDOM && !CS.isEmpty(sCurrentSectionId)) {
          let oSelectedItem = oExpandableDOM.querySelectorAll(".listItem[data-id='" + sCurrentSectionId + "']");
          if (oSelectedItem.length) {
            let aSelectedDOM = oExpandableDOM.querySelectorAll(".listItem.selected");
            if (aSelectedDOM.length) {
              CS.forEach(aSelectedDOM, function (oDom) {
                oDom.classList.remove("selected");
              })
            }
            oSelectedItem[0].classList.add("selected");
          }
        }
      }
    }
    catch (oException) {
      ExceptionLogger.error("Problem in applying style: ");
    }
  };

  setPreventScroll (_bValue) {
    this.preventScroll = _bValue;
  }

  setSelectedSection (sSectionId) {
    var scrollbar =  ReactDOM.findDOMNode(this["scrollbar"]);
    if (scrollbar) {
      const oLinkedSection = this["linkedSection_" + sSectionId] || {};
      CommonUtils.scrollTo(scrollbar, {scrollTop: oLinkedSection.offsetTop}, "ease", this.setPreventScroll.bind(this, false));
    } else {
      //remove scroll lock as ref to scroll bar is not available
      this.setPreventScroll(false);
    }
  }

  handleScrollLinkedSection = () => {
    if(this.timer != null) {
      this.timer = null;
    }
    this.timer = setTimeout(this.handleLinkedSectionsScroll.bind(this, true), 300);
  };

  handleLinkedSectionsScroll = (bTriggerForcefully) => {
    let _this = this;
    let __props = _this.props;
    var scrollbar =  ReactDOM.findDOMNode(this["scrollbar"]);
    let sSelectedSectionId = this.state.selectedSectionId;

    if (CS.isEmpty(scrollbar) || this.preventScroll) {
      return;
    }

    // const scrollbarInnerSection = scrollbar.firstChild;
    let iParentScrollTop = scrollbar.scrollTop;
    let aSectionComponents = __props.sectionComponents;
    CS.forEach(aSectionComponents, (oSection) => {
      let sCurrentSectionId = oSection.sectionId;
      let oNode = _this["linkedSection_" + sCurrentSectionId] || {};
      let iChildOffsetTop = oNode.offsetTop;
      let iChildHeight = oNode.offsetHeight;
      let iChildPosition = iChildHeight + iChildOffsetTop;
      if ((iChildPosition - iParentScrollTop) > 1) {
        if (sCurrentSectionId !== sSelectedSectionId || bTriggerForcefully) {
          //Below check is added for DTP specific use-case where we want to dispatch our own event when a section is
          // clicked. If any one is using content-linked-section-view to render their own views, then they can also
          // added their own function implementation with same name.
          if(this.props.linkItemClicked){
            this.props.linkItemClicked(sCurrentSectionId, !bTriggerForcefully);
          } else {
            EventBus.dispatch(oEvents.CONTENT_LINKED_SECTION_VIEW_LINK_ITEM_CLICKED, sCurrentSectionId, !bTriggerForcefully);
          }
          _this.setState({
            selectedSectionId: sCurrentSectionId,
            changeSection: true
          });
          this.makeSelectionOfActiveSection();
        }
        return false;
      }
    });

  };

  linkItemClicked (sSectionId) {
    if(this.state.selectedSectionId !== sSectionId) {
      this.setPreventScroll(true);
      this.setSelectedSection(sSectionId);
      //Below check is added for DTP specific use-case where we want to dispatch our own event when a section is
      // clicked. If any one is using content-linked-section-view to render their own views, then they can also
      // added their own function implementation with same name.
      if (this.props.linkItemClicked) {
        this.props.linkItemClicked(sSectionId, false);
      } else {
        EventBus.dispatch(oEvents.CONTENT_LINKED_SECTION_VIEW_LINK_ITEM_CLICKED, sSectionId, false);
      }
    }
  }

  checkAndSelectFirstSection () {
    //will select first section by default when none is selected
    let __props = this.props;
    let sSelectedSectionId = __props.selectedElementInformation.sectionId;
    let aSectionComponents = __props.sectionComponents;
    if(CS.isEmpty(sSelectedSectionId)){
      let oFirstSection = aSectionComponents[0];
      if (!CS.isEmpty(oFirstSection)) {
        this.linkItemClicked(oFirstSection.sectionId);
      }
    }
  }

  toggleButtonClicked () {
    let bIsExpanded = this.state.isExpanded;
    this.setState({
      isExpanded: !bIsExpanded
    });
  }

  getLinkedSections () {
    let _this = this;
    let __props = this.props;
    let aLinkedSectionViews = [];
    let aSectionComponents = this.props.sectionComponents;
    let sSelectedSectionId = __props.selectedElementInformation.sectionId;
    CS.forEach(aSectionComponents, function (oSectionComponent) {
      let sSectionId = oSectionComponent.sectionId;
      let oSectionComponentDOM = null;

      if (oSectionComponent.isSkipped) {
        return;
      }

      if (!oSectionComponent.isPropertyCollectionSection) {
        let oUnSelectedDOM = null;
        let sSectionWrapperClass = "sectionWrapper ";
        if (sSelectedSectionId == sSectionId) {
          sSectionWrapperClass += "selected";
        } else {
          oUnSelectedDOM = (
              <div className={"unSelectedMask"}>
              </div>
          );
        }

        let oSectionLabelDOM = oSectionComponent.label ? <div className="sectionHeader">
          <div className="contentSectionNewLabel">{oSectionComponent.label}</div>
          {oSectionComponent.relationshipConflictView}
        </div> : null;

        oSectionComponentDOM = (
            <div>
              {oSectionLabelDOM}
              <div className={sSectionWrapperClass} onClick={_this.linkItemClicked.bind(_this, sSectionId)}>
                {oUnSelectedDOM}
                {oSectionComponent.view}
              </div>
            </div>)

      } else {
        oSectionComponentDOM = oSectionComponent.view;
      }

      if(CS.isNotEmpty(oSectionComponentDOM)) {
        aLinkedSectionViews.push(
            <div key={sSectionId} className="linkedSection"
                 ref={_this.setRef.bind(_this, "linkedSection_" + sSectionId)}>
              {oSectionComponentDOM}
            </div>
        )
      }
    });

    return aLinkedSectionViews;
  }

  getHeaderColumnView (bIsAttributeComparisonViewSelected, aHelperLanguages) {
    let aColumnsView = [];
    let sHeaderClassName = "columnHeader";

    if (bIsAttributeComparisonViewSelected) {
      CS.forEach(aHelperLanguages, function (oLanguage, iIndex) {
        let sLabel = CS.getLabelOrCode(oLanguage);
        let oLanguageIconDom = null;
        if (oLanguage.iconKey) {
          let sUILanguageIcon = ViewUtils.getIconUrl(oLanguage.iconKey);
          oLanguageIconDom = (
              <div className="selectedItemIcon">
                <ImageFitToContainerView imageSrc={sUILanguageIcon}/>
              </div>
          );
        }
        aColumnsView.push(
            <div className={sHeaderClassName} key={sHeaderClassName + iIndex}>
              {oLanguageIconDom}
              <div className={"columnHeaderLabel"}>{sLabel}</div>
            </div>
        )
      });
      if (CS.isNotEmpty(aColumnsView)) {
        return (<div className="linkedSectionColumnHeader">{aColumnsView}</div>)
      }
    }
    return null;
  }

  getHeaderView (oHeaderViewData) {
    if (oHeaderViewData && oHeaderViewData.showHeaderView) {
      let aHeaderButtonsData = oHeaderViewData.headerButtonsData;
      let oSectionLabel = oHeaderViewData.sectionLabel;
      return (
          <div className="linkedSectionHeader">
            {oSectionLabel}
            {aHeaderButtonsData}
          </div>
      );
    }
    return null;
  }

  render () {

    let _this = this;
    let oSelectedElementInformation = this.props.selectedElementInformation || {};
    let sSelectedSectionId = oSelectedElementInformation.sectionId;
    let fOnLinkItemClicked = this.linkItemClicked.bind(this);
    let aLinkedSectionViews = this.getLinkedSections();
    let oExpandableMenuListView = null;
    let sLinkedSectionsContainer = "linkedSectionsContainer";
    if(this.props.showExpandedMenuList) {
      sLinkedSectionsContainer += " withBorder";
      oExpandableMenuListView =(<div className="linksContainer">
        <ExpandableMenuListView
            ref={this.setRef.bind(this,"expandableMenu")}
            linkItemsData={this.props.linkItemsData}
            selectedItemId={sSelectedSectionId}
            onItemClick={fOnLinkItemClicked}
        />
      </div>);
    }

    let oStyle = {};
    let oHeaderColumnView = this.getHeaderColumnView(this.props.isHelperLanguageSelected, this.props.helperLanguages);
    let oHeaderView = this.getHeaderView(this.props.sectionHeaderData);
    if (CS.isNotEmpty(oHeaderView) && CS.isNotEmpty(oHeaderColumnView)) {
      oStyle.height = "calc(100% - 90px)";
    } else if (CS.isNotEmpty(oHeaderView) || CS.isNotEmpty(oHeaderColumnView)) {
      oStyle.height = "calc(100% - 45px)";
    } else {
      oStyle.height = "100%";
    }
    return (
        <div className="contentLinkedSectionsView">
          {oExpandableMenuListView}
          <div className={sLinkedSectionsContainer}>
            {oHeaderColumnView}
            {oHeaderView}
            <div className={"scrollContentSectionWrapper"}
                style={oStyle}
                onScroll={_this.handleScrollLinkedSection}
                ref={_this.setRef.bind(_this,"scrollbar")}
            >
              {aLinkedSectionViews}
            </div>
          </div>

        </div>
    );
  }
}

ContentLinkedSectionsView.propTypes = oPropTypes;
ContentLinkedSectionsView.defaultProps = oDefaultProps;

export const view = ContentLinkedSectionsView;
export const events = oEvents;
