import CS from '../../libraries/cs';
import React from 'react';
import { diffChars } from 'diff/lib/diff/character';
import { view as CustomPopoverView } from '../../viewlibraries/customPopoverView/custom-popover-view';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as ContentAttributeElementView } from './../attributeelementview/content-attribute-element-view';
import ContentAttributeElementViewModel from './../attributeelementview/model/content-attribute-element-view-model';
import ViewLibraryUtils from '../utils/view-library-utils';
import { view as ImageSimpleView } from './../imagesimpleview/image-simple-view';

const oEvents = {
  MATCH_MERGE_CELL_VIEW_CELL_CLICKED: "match_merge_cell_view_cell_clicked"
};

const oPropTypes = {
  property: ReactPropTypes.object,
  columnWidth: ReactPropTypes.number,
  comparisonProperty: ReactPropTypes.object,
  rendererType: ReactPropTypes.string,
  rowId: ReactPropTypes.string,
  tableId: ReactPropTypes.string,
  tableGroupName: ReactPropTypes.string,
  masterAttribute: ReactPropTypes.object,
  isRowDisabled: ReactPropTypes.bool,
  referencedAssetsData: ReactPropTypes.object,
  isInstancesComparisonView: ReactPropTypes.bool,
  columnId: ReactPropTypes.number
};
/**
 * @class MatchAndMergeAttributeCellView - Use to display attribute view of comparision table in product view.
 * @memberOf Views
 * @property {object} [property] - Pass properties like isDisabled, value, visibleValues.
 * @property {number} [columnWidth] - Pass 250 width default for column.
 * @property {object} [comparisonProperty] - Pass comparison properties
 * @property {string} [rendererType] - Pass type of matchandmerge view.
 * @property {string} [rowId] - Pass Id of table row.
 * @property {string} [tableId] -  Pass table Id.
 * @property {string} [tableGroupName] -  Pass table group name.
 * @property {object} [masterAttribute] - Pass object contain master attribute like id, label,etc.
 * @property {bool} [isRowDisabled] -  Pass boolean for rowdisabled if true else enable.
 * @property {object} [referencedAssetsData] - Not in use.
 * @property {bool} [isInstancesComparisonView] - Pass boolean for if true for instance comparison else false.
 * @property {number} [columnId] - Pass column Id of table column.
 */

// @CS.SafeComponent
class MatchAndMergeAttributeCellView extends React.Component {

  constructor(props) {
    super(props);

    this.oWrapperDivStyle = {
      overflow: "hidden",
      border: "3px solid #1976D2"
    };

    this.state = {
      showPopover: false,
    }
  }

  openPopover =(oEvent)=> {
    let sRendererType = this.props.rendererType;
    let property, isRowDisabled;
    ({ property, isRowDisabled } = this.props);

    if(property.isEditable && !property.isDisabled && !isRowDisabled && sRendererType !== "coverflow") {
      let oDOM = oEvent.currentTarget;
      this.oWrapperDivStyle.width = oDOM.clientWidth + "px";
      this.oWrapperDivStyle.minHeight = oDOM.clientHeight + "px";

      this.setState({
        showPopover: true,
        anchorElement: oDOM
      });
    }
  }

  closePopover =()=> {
    this.setState({
      showPopover: false
    });
    // this.handleValueChanged();
  }

  handleCellValueClicked =()=> {
    let oComparisonProperty = this.props.comparisonProperty;
    let sRowId = this.props.rowId;
    let oProperty = this.props.property;
    let sTableId = this.props.tableId;
    let sTableGroupName = this.props.tableGroupName;

    if(!CS.isEmpty(oComparisonProperty) && !this.props.isRowDisabled && !this.props.isInstancesComparisonView){
      if(oComparisonProperty.value != oProperty.value) {
        EventBus.dispatch(oEvents.MATCH_MERGE_CELL_VIEW_CELL_CLICKED, sRowId, oProperty, sTableId, sTableGroupName, this.props.isGoldenRecordComparison, this.props.columnId);
      }
    }
  }

  handleAttributeEditValueChanged =(oData)=> {
    let sRowId = this.props.rowId;
    let sValue = /*oData.valueAsHtml || */oData.value;
    let sTableId = this.props.tableId;
    let sTableGroupName = this.props.tableGroupName;
    let oProp = this.props.property;
    let oNameAttribute = null;

    if(oProp.nameAttribute) {
      oNameAttribute = oProp.nameAttribute;
      oNameAttribute.label = sValue;
    }

    let oProperty = {
      value: oData.value,
      valueAsHtml: oData.valueAsHtml,
      nameAttribute: oNameAttribute
    };

    EventBus.dispatch(oEvents.MATCH_MERGE_CELL_VIEW_CELL_CLICKED, sRowId, oProperty, sTableId, sTableGroupName, this.props.isGoldenRecordComparison, this.props.columnId);
  };

  formatDate = (sDate) => {
    sDate = sDate ? +sDate ? new Date(+sDate) : null : null;
    if (CS.isNaN(Date.parse(sDate))) {
      sDate = null;
    }
    return sDate ? ViewLibraryUtils.getDateAttributeInTimeFormat(sDate) : null;
  };

  getHTMLInnerText =(sHTML)=>{
    let div = document.createElement("div");
    div.innerHTML = sHTML;

    return div.textContent || div.innerText || "";
  };

  getAttributeElementView =(sValue)=> {
    let sAttributeId = this.props.rowId;
    let oMasterAttribute = this.props.masterAttribute;

    let oAttributeElementModel = new ContentAttributeElementViewModel(
        sAttributeId,
        "",
        sValue || "",
        false, //TODO: 'bIsDisabled' is hardcoded for now, later pass it as props
        "",
        "",
        oMasterAttribute,
        {}
    );

    return (<ContentAttributeElementView key={sAttributeId}
                                         data-id={sAttributeId}
                                         model={oAttributeElementModel}
                                         onBlurHandler={this.handleAttributeEditValueChanged}
                                         />);

  }

  getImageView =(sURL)=> {
    return (<span className="matchAndMergeImage">
      <ImageSimpleView imageSrc={sURL} classLabel="mnmrIcon"/>
    </span>);
  }

  getValueForComparison =()=> {
    let oComparisonProperty = this.props.comparisonProperty;
    let oProperty = this.props.property;
    let sValue = oProperty.value;
    let sOriginalValue = oProperty.originalValue;
    let sRendererType = this.props.rendererType;
    let sComparator = "";
    //normal date, createdOn and Due date
    if (oComparisonProperty) {

      let sValueToCompareWith = oComparisonProperty.value;
      let sOriginalValueToCompareWith = oComparisonProperty.originalValue;

      if (sRendererType == "date") {
        let sValA = this.formatDate(sValue);
        let sValB = this.formatDate(sValueToCompareWith);

        if (sValue == sValueToCompareWith) {
          sComparator = "eq";
        } else if (+sValue > +sValueToCompareWith) {
          sComparator = "gt";
        } else {
          sComparator = "lt";
        }

        return this.getComparisonAttributeValue(sValA, sValB, sRendererType, sComparator);
      }
      else if (sRendererType == "html") {
        let sFilteredValText = this.getHTMLInnerText(sValue);
        let sFilteredCompText = this.getHTMLInnerText(sValueToCompareWith);

        return this.getComparisonAttributeValue(sFilteredValText, sFilteredCompText, sRendererType);
      }
      else if (sRendererType == "number" || sRendererType == "measurementMetrics" || sRendererType === "calculated") {
        let nOriginalValue = Number(sOriginalValue);
        let nOriginalValueToCompareWith = Number(sOriginalValueToCompareWith);
        if (nOriginalValue == nOriginalValueToCompareWith || sValue == sValueToCompareWith) {
          sComparator = "eq";
        } else if (+nOriginalValue > +nOriginalValueToCompareWith || +sValue > +sValueToCompareWith) {
          sComparator = "gt";
        } else if (nOriginalValue < nOriginalValueToCompareWith || +sValue < +sValueToCompareWith) {
          sComparator = "lt";
        }

        return this.getComparisonAttributeValue(sValue, sValueToCompareWith, sRendererType, sComparator);
      }
      else if(sRendererType == "coverflow") {
        if (sValue == sValueToCompareWith) {
          sComparator = "eq";
        }
        return this.getComparisonAttributeValue(sValue, sValueToCompareWith, sRendererType, sComparator);
      }

      return this.getComparisonAttributeValue(sValue, sValueToCompareWith, sRendererType, "");
    }
    else {
      return this.getComparisonAttributeValue(sValue, "", sRendererType, "");
    }
  }

  getComparisonAttributeValue =(sValue, sValueToCompareWith, sType, sComparator)=> {
    let oView = null;
    let aSpans = [];
    let sClassName = "";
    let oComparisonProperty = this.props.comparisonProperty;

    if (!CS.isEmpty(oComparisonProperty)) {
      if (CS.isEmpty(sValue)) {

        oView = (<span className={sClassName}></span>);

      } else {
        if (sType == "date" || sType == "number" || sType == "measurementMetrics" || sType === "calculated") {

          var sComparisonClassname = "";
          var sTitle = "";

          if (sComparator == "eq") {
            sComparisonClassname = "comparisonEqual";
            sTitle = getTranslation().EQUAL;
          } else if (sComparator == "lt") {
            sComparisonClassname = "comparisonLesser";
            sTitle = getTranslation().LESS_THAN;
          } else if (sComparator == "gt") {
            sComparisonClassname = "comparisonGreater";
            sTitle = getTranslation().GREATER;
          }

          oView = (<span className={sComparisonClassname}>
            <span className="compVal">{sValue}</span>
            <span className="compIcon" title={sTitle}></span>
          </span>);

        } else if(sType === "coverflow") {
          var oProperty = this.props.property;
          var oReferencedAssetsData = oProperty.referencedAssetsData;
          var sURL = oReferencedAssetsData.thumbKeySrc;

          let sComparisonClassName = "";
          let sTitle = "";

          if (sComparator === "eq") {
            sComparisonClassName = "comparisonIcon";
            sTitle = getTranslation().EQUAL;
          }

          return (<span className="matchAndMergerImageCell">
            <span className={sComparisonClassName} title={sTitle}></span>
            {this.getImageView(sURL)}
          </span>);
        } else {
          let aDiffTexts = diffChars(sValueToCompareWith, sValue);
          let aSpans = [];
          let sClassName = "";

          if (sValue == sValueToCompareWith) {
            sClassName = "comparisonEqual";
            aSpans.push(<span key={"compIcon" } className="compIcon" title={getTranslation().EQUAL}></span>);
          }

          CS.forEach(aDiffTexts, function (oDiff, iIndex) {
            if (oDiff.added) {
              aSpans.push(<span key={iIndex} className="comparisonGreen">{oDiff.value}</span>);
            } else if (oDiff.removed) {
              aSpans.push(<span key={iIndex} className="comparisonRed">{oDiff.value}</span>);
            } else {
              aSpans.push(<span key={iIndex}>{oDiff.value}</span>);
            }
          });

          oView = (<span className={sClassName}>{aSpans}</span>);

        }
      }
    }
    else {

      if(sType == "html"){
        sValue = this.getHTMLInnerText(sValue);
      }else if(sType == "date"){
        sValue = this.formatDate(sValue);
      }
      else if(sType == "coverflow") {
        var oProperty = this.props.property;
        var oReferencedAssetsData = oProperty.referencedAssetsData;
        var sURL = oReferencedAssetsData.thumbKeySrc;
        return (<span className="matchAndMergerImageCell">{this.getImageView(sURL)}</span>);
      }

      aSpans.push(<span key={sValue}>{sValue}</span>);
      oView = (<span className={sClassName}>
        {aSpans}
      </span>);
    }

    return oView;
  }

  render() {

    let oPopoverStyle = {
      "width": this.props.columnWidth + "px"
    };

    // let oWrapperDivStyle = {
    //   "width": this.props.columnWidth + "px",
    //   "border": "2px solid blue"
    // };

    let oProperty = this.props.property;
    let sValue = oProperty.value;
    let oComparisonProperty = this.props.comparisonProperty;
    let sCellTextClassName = "readOnlyText";

    if(!CS.isEmpty(oComparisonProperty) && !this.props.isInstancesComparisonView && !this.props.isRowDisabled){
      sCellTextClassName = sCellTextClassName + " mnmClickableText";
    }

    let fOnCellClick = this.handleCellValueClicked;

    if ((this.props.isGoldenRecordComparison && oProperty.isDisabled) || this.props.isRowDisabled) {
      fOnCellClick = null;
      sCellTextClassName += " isDisabled";
    }

    let oWrapperDivStyle = CS.cloneDeep(this.oWrapperDivStyle);

    return (
        <div className="matchAndMergeCellView" onDoubleClick={this.openPopover}>
          <div className={sCellTextClassName} onClick={fOnCellClick}>{this.getValueForComparison()}</div>
          <CustomPopoverView className="popover-root mnmDatePickerEmptyButtonStyle"
                   open={this.state.showPopover}
                   animated={false}
                   style={oPopoverStyle}
                   anchorEl={this.state.anchorElement}
                   anchorOrigin={{horizontal: 'left', vertical: 'top'}}
                   transformOrigin={{horizontal: 'left', vertical: 'top'}}
                   onClose={this.closePopover}>
            <div style={oWrapperDivStyle}>
              {this.getAttributeElementView(sValue)}
            </div>
          </CustomPopoverView>
        </div>
    );
  }

}

MatchAndMergeAttributeCellView.propTypes = oPropTypes;

export const view = MatchAndMergeAttributeCellView;
export const events = oEvents;
