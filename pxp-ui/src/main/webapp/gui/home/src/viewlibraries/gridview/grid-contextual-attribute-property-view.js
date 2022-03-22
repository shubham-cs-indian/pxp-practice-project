import CS from '../../libraries/cs';
import MasterTagListContext from '../../commonmodule/HOC/master-tag-list-context';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomPopoverView } from '../customPopoverView/custom-popover-view';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

const oEvents = {

};

const oPropTypes = {
  attributeTags: ReactPropTypes.array,
  onChange: ReactPropTypes.func,
  masterTagListFromContext: ReactPropTypes.object
};
/**
 * @class GridContextualAttributePropertyView
 * @memberOf Views
 * @property {array} [attributeTags]
 * @property {func} [onChange]
 */

// @MasterTagListContext
// @CS.SafeComponent
class GridContextualAttributePropertyView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      showPopover: false,
      attributeTags: this.props.attributeTags
    }

    this.gridContextualAttributePropertyView = React.createRef();
  }

  openPopover =()=> {
    var oDOM = this.gridContextualAttributePropertyView.current;
    this.setState({
      showPopover: true,
      anchorElement: oDOM
    });
  }

  closePopover =()=> {
    this.setState({
      showPopover: false
    });
    this.handleValueChanged();
  }

  handleValueChanged =()=> {
    this.props.onChange(this.state.attributeTags);
  }

  handleAddButtonClicked =(oAttributeTag, oEvent)=> {
    // EventBus.dispatch(oEvents.ATTRIBUTE_TAG_GROUP_ITEM_ADD_ICON_CLICKED, this, oAttributeTag);
    var aAttributeTags = CS.clone(this.state.attributeTags);
    aAttributeTags.push(oAttributeTag);
    this.setState({
      attributeTags: aAttributeTags
    });
  }

  handleRemoveButtonClicked =(sId, oEvent)=> {
    // EventBus.dispatch(oEvents.ATTRIBUTE_TAG_GROUP_ITEM_REMOVE_ICON_CLICKED, sId);
    var aAttributeTags = CS.clone(this.state.attributeTags);
    CS.remove(aAttributeTags, {tagId: sId});
    this.setState({
      attributeTags: aAttributeTags
    });
  }

  setContextTagCheckAllStatus =(aTagValues, bStatus)=> {
    CS.forEach(aTagValues, function (oTag) {
      if(!CS.isEmpty(oTag)) {
        oTag.isSelected = bStatus;
      }
    });
  }

  handleElementTagCheckAllChanged =(sAttributeTagId, oEvent)=> {
    // EventBus.dispatch(oEvents.HANDLE_ATTRIBUTE_TAG_CHECK_ALL_CHANGED, this, sAttributeTagId);
    var aAttributeTags = CS.cloneDeep(this.state.attributeTags);
    var oContextTag = CS.find(aAttributeTags, {tagId: sAttributeTagId});
    if (!CS.isEmpty(oContextTag)) {
      var aTagValues = oContextTag.tagValues;
      var aSelectedTags = CS.filter(aTagValues, {isSelected: true});
      var iSelectedCout = aSelectedTags.length;
      if (iSelectedCout == 0) {
        this.setContextTagCheckAllStatus(aTagValues, true);
      } else if (iSelectedCout != aTagValues.length) {
        this.setContextTagCheckAllStatus(aTagValues, true);
      } else {
        this.setContextTagCheckAllStatus(aTagValues, false);
      }
    }
    this.setState({
      attributeTags: aAttributeTags
    });
  }

  handleAttributeTagSelectionChanged =(sAttributeTagId, sTagValueId, oEvent)=> {
    // EventBus.dispatch(oEvents.HANDLE_ATTRIBUTE_TAG_SELECTION_CHANGED, this, sAttributeTagId, sTagValueId);
    var aAttributeTags = CS.cloneDeep(this.state.attributeTags);
    var oContextTag = CS.find(aAttributeTags, {tagId: sAttributeTagId});
    if (!CS.isEmpty(oContextTag)) {
      var aTagValues = oContextTag.tagValues;
      var oTag = CS.find(aTagValues, {tagValueId: sTagValueId});
      if (!CS.isEmpty(oTag)) {
        oTag.isSelected = !oTag.isSelected;
      }
    }
    this.setState({
      attributeTags: aAttributeTags
    });
  }

  getAttributeTagValues =(oTag)=> {
    var aTagValues = [];
    CS.forEach(oTag.children, function (oChildTag) {
      var oMasterTagValue = {};
      oMasterTagValue.tagValueId = oChildTag.id;
      oMasterTagValue.label = CS.getLabelOrCode(oChildTag);
      oMasterTagValue.isSelected = false;
      oMasterTagValue.color = oChildTag.color;
      aTagValues.push(oMasterTagValue);
    });
    return aTagValues;
  }

  getTagListDomForElement =(oAttributeTag)=> {
    var that = this;
    var aTagList = !CS.isEmpty(oAttributeTag) && oAttributeTag.tagValues;
    var sAttributeTagId = oAttributeTag.tagId;
    var aTagElements = [];

    CS.forEach(aTagList, function (oTag) {
      var sClassName = "tagElement ";

      if (oTag.isSelected) {
        sClassName += "tagElementSelected ";
      }
      var sTagValueId = oTag.tagValueId;
      aTagElements.push(
          <div className={sClassName} title={CS.getLabelOrCode(oTag)} key={sTagValueId}
               onClick={that.handleAttributeTagSelectionChanged.bind(that,  sAttributeTagId, sTagValueId)}>
            {CS.getLabelOrCode(oTag)}
          </div>);
    });

    return aTagElements.length ? (
            <div className="attribute-tag-list-container">
              <div className="tagListBody">
                {aTagElements}
              </div>
            </div>) : null;
  }

  getAttributeTagDetails =()=> {
    var _this = this;
    // var oAttribute = _this.props.selectedAttributeDetailedModel;
    // if (CS.isEmpty(oAttribute)) {
    //   return null;
    // } else if (!oAttribute.canBeContextual) {
    //   return null;
    // }
    var aAttributeTags = this.state.attributeTags;
    var aAttributeTagMap = this.props.masterTagListFromContext;
    var aContextListView = [];
    var aAttributeTagGroup = !CS.isEmpty(aAttributeTagMap) && aAttributeTagMap[0].children;
    CS.forEach(aAttributeTagGroup, function (oTag) {
      var oAttributeTag = CS.find(aAttributeTags, {tagId: oTag.id});
      if (CS.isEmpty(oAttributeTag)) {
        var oAttributeTags = {};
        oAttributeTags.tagId = oTag.id;
        oAttributeTags.label = CS.getLabelOrCode(oTag);
        oAttributeTags.tagValues = _this.getAttributeTagValues(oTag);
        aContextListView.push(
            <div className="attributeContainer">
              {CS.getLabelOrCode(oTag)}
              <div className="attributeTagButton addButton"
                   onClick={_this.handleAddButtonClicked.bind(_this, oAttributeTags)}></div>
            </div>
        );
      }
    });


    var aChildAttributeTagListView = [];
    CS.forEach(aAttributeTags, function (oAttributeTag) {
      var aTagValues = oAttributeTag.tagValues;
      var aSelectedTagValues = CS.filter(aTagValues, {isSelected: true});
      var iSelectedCount = aSelectedTagValues.length;
      var sCheckboxClassName = "";
      if(iSelectedCount == aTagValues.length) {
        sCheckboxClassName = "checked";
      } else if(iSelectedCount > 0) {
        sCheckboxClassName = "partialChecked";
      }
      aChildAttributeTagListView.push(
          <div className="attributeTagChildContainer">
            {CS.getLabelOrCode(oAttributeTag)}
            <div className={"tagListSelectAll "+sCheckboxClassName}
                 onClick={_this.handleElementTagCheckAllChanged.bind(_this, oAttributeTag.tagId)}>
            </div>
            <div className="attributeTagButton removeButton"
                 onClick={_this.handleRemoveButtonClicked.bind(_this, oAttributeTag.tagId)}></div>
            {_this.getTagListDomForElement(oAttributeTag)}
          </div>
      );
    });

    return (
        <div className="attributeTagListsContainer">
          <div className="allAttributeTagList">
            <div className="listTitle">{getTranslation().TAG_GROUP}</div>
            <div className="listBody">
              {aContextListView}
            </div>
          </div>
          <div className="childAttributeTagsList">
            <div className="listTitle">{getTranslation().CONTEXT_SELECTED_TAG_GROUP}</div>
            <div className="listBody">
              {aChildAttributeTagListView}
            </div>
          </div>
        </div>
    );
  }

  render() {

    var oPopoverStyle = {
      width : "800px",
      padding: "20px",
      overflow: "auto"
    };

    var iNumberOfTags = this.props.attributeTags.length;
    var sSelectedTags = (iNumberOfTags == 1) ? (iNumberOfTags + " " + getTranslation().TAG): (iNumberOfTags + " " + getTranslation().TAGS);

    return (
        <div className="gridContextualAttributePropertyView" ref={this.gridContextualAttributePropertyView} onClick={this.openPopover}>
          <div className="selectedTagsCount">{sSelectedTags}</div>
          <CustomPopoverView className="popover-root"
                   open={this.state.showPopover}
                   style={ oPopoverStyle}
                   anchorEl={this.state.anchorElement}
                   anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
                   transformOrigin={{horizontal: 'right', vertical: 'top'}}
                   onClose={this.closePopover}>
          {this.getAttributeTagDetails()}
          </CustomPopoverView>
        </div>
    );
  }

}

GridContextualAttributePropertyView.propTypes = oPropTypes;

export const view = MasterTagListContext(GridContextualAttributePropertyView);
export const events = oEvents;
