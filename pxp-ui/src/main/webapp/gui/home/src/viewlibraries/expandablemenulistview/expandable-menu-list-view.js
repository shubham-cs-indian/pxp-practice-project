import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ViewLibraryUtils from '../utils/view-library-utils';
import { view as ImageFitToContainerView } from './../imagefittocontainerview/image-fit-to-container-view';
import TooltipView from './../tooltipview/tooltip-view';

const oEvents = {};

const oPropTypes = {
  linkItemsData: ReactPropTypes.arrayOf(
      ReactPropTypes.shape({
        id: ReactPropTypes.string,
        label: ReactPropTypes.string,
        icon: ReactPropTypes.string
      })
  ),
  selectedItemId: ReactPropTypes.string,
  onItemClick: ReactPropTypes.func,
  selectDefaultItem: ReactPropTypes.func
};
/**
 * @class ExpandableMenuListView - use for display Sidebar of mainSection.
 * @memberOf Views
 * @property {array} [linkItemsData] -  an array which contain link items data in sidebar.
 * @property {string} [selectedItemId] -  a string of selected item id.
 * @property {func} [onItemClick] -  function which used onItemClick event.
 * @property {func} [selectDefaultItem] -  function which used selectDefaultItem event.
 */

// @CS.SafeComponent
class ExpandableMenuListView extends React.Component {

  constructor (props) {
    super(props);

    this.state = {
      isExpanded: true
    };
    this.scrollbar = React.createRef();

    this.setRef =( sContext, element) => {
      this[sContext] = element;
    };

    this.toggleButtonClicked = this.toggleButtonClicked.bind(this);
  }

  componentDidMount () {
    this.selectDefaultItem();
  }

  componentDidUpdate () {
    this.selectDefaultItem();
    this.setSelectedSection();
  }

  toggleButtonClicked () {
    let bIsExpanded = this.state.isExpanded;
    this.setState({
      isExpanded: !bIsExpanded
    });
  }

  handleItemClicked (sId) {
    if (CS.isFunction(this.props.onItemClick)) {
      this.props.onItemClick(sId);
    }
  }

  setSelectedSection = () => {
    // TODO: Identify how to get DOM from ref on component and then fix below code: Vidit
    /*let sSectionId = this.props.selectedItemId;
    var scrollbar = this.scrollbar.current;

    if (scrollbar) {
      const scrollbarInnerSection = scrollbar.firstChild;
      const oLinkedSection = this["linkedSection_" + sSectionId] || {};
      ViewLibraryUtils.scrollTo(scrollbarInnerSection, {scrollTop: oLinkedSection.offsetTop}, "swing");
    }*/
  };

  selectDefaultItem () {
    let __props = this.props;
    if (CS.isEmpty(__props.selectedItemId) && CS.isFunction(__props.selectDefaultItem)) {
      let oFirstItem = __props.linkItemsData[0];
      if (!CS.isEmpty(oFirstItem)) {
        __props.selectDefaultItem(oFirstItem.id);
      }
    }
  }

  getIconView (oItem) {
    let sIconId = oItem.iconKey;
    if (sIconId) {
      let sIconURL = ViewLibraryUtils.getIconUrl(sIconId);
      return (
          <div className="iconContainer">
            <ImageFitToContainerView imageSrc={sIconURL}/>
          </div>
      )
    } else {
      //show first 2 letters
      let sLabel = CS.getLabelOrCode(oItem);
      return (
          <div className="itemInitials">{sLabel[0] + (sLabel[1] || "")}</div>
      )
    }
  }

  render () {

    let _this = this;
    let aListItemViews = [];
    let aListItems = this.props.linkItemsData;
    let sSelectedItemId = this.props.selectedItemId;

    CS.forEach(aListItems, function (oItem) {
      let sId = oItem.id;
      let sLabel = CS.getLabelOrCode(oItem);

      let sListItemClass = "listItem ";
      if (sId === sSelectedItemId) {
        sListItemClass += "selected ";
      }

      let oIconView = _this.getIconView(oItem);

      if (oItem.view) {
        aListItemViews.push(oItem.view);
      } else {
        aListItemViews.push(
            <TooltipView label={sLabel} key={sId}>
              <div className={sListItemClass} key={sId} data-id={sId}
                   ref={_this.setRef.bind(_this, "linkedSection_" + sId)}
                   onClick={_this.handleItemClicked.bind(_this, sId)}>
                <div className="iconSection">
                  {oIconView}
                </div>
                <div className="labelSection">{sLabel}</div>
              </div>
            </TooltipView>
        );
      }
    });

    let sViewClass = "expandableMenuListView ";
    if (this.state.isExpanded) {
      sViewClass += "expanded ";
    } else {
      sViewClass += "collapsed ";
    }

    return (
        <div className={sViewClass} ref={this.props.forwardedRef}>
          <div className="listItemsSection">
              {aListItemViews}
          </div>
          <div className="toggleButtonSection" onClick={this.toggleButtonClicked}>
            <div className="toggleButtonSectionIcon"></div>
          </div>

        </div>
    );
  }
}

ExpandableMenuListView.propTypes = oPropTypes;

export const view = ExpandableMenuListView;
export const events = oEvents;
