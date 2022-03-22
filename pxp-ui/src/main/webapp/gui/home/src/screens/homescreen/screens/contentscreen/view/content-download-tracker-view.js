import React from 'react';
import ReactPropTypes from 'prop-types';
import {getTranslations as getTranslation} from '../../../../../commonmodule/store/helper/translation-manager.js';
import {view as DateRangePickerView} from "../../../../../viewlibraries/customdaterangepickerview/custom-date-range-picker-view";
import Moment from "moment";
import ViewLibraryUtils from "../../../../../viewlibraries/utils/view-library-utils";

const oEvents = {};

const oPropTypes = {
  downloadInfoData: ReactPropTypes.object,
};

// @CS.SafeComponent
class ContentDownloadTrackerView extends React.Component {
  static propTypes = oPropTypes;

  getDateInFormat = (sValue) => {
    return ViewLibraryUtils.getDateAttributeInDateTimeFormat(sValue).date;
  };

  getDownloadCountView = (iDownloadCount) => {
    let sDownloadCountLabel = iDownloadCount > 0 ? iDownloadCount : getTranslation().NO;
    return (<div className={"downloadCountSectionWrapper"}>
      <div className={"downloadCount"}>{sDownloadCountLabel}</div>
      <div className={"downloadLabel"}>{getTranslation().DOWNLOADS}</div>
    </div>);
  };

  render () {
    let oDownloadInfoData = this.props.downloadInfoData;
    let oDateTimeFormat = ViewLibraryUtils.getStandardDateTimeFormat();
    let oDownloadRange = oDownloadInfoData.downloadRange;
    let iTotalDownloadCount = oDownloadInfoData.totalDownloadCount ? oDownloadInfoData.totalDownloadCount : 0;
    let iDownloadCountWithinRange = oDownloadInfoData.downloadCountWithinRange ? oDownloadInfoData.downloadCountWithinRange : 0;
    let sStartDate = this.getDateInFormat(oDownloadRange.startTime);
    let sEndDate = this.getDateInFormat(oDownloadRange.endTime);
    let oSelectionRange = {
      'Past 7 Days': [Moment().subtract(6, 'days'), Moment()],
      'Past 30 Days': [Moment().subtract(29, 'days'), Moment()],
    };
    let oDateRangeLocale = {
      customRangeLabel: "Custom Date Range",
      format: oDateTimeFormat.dateFormat + " " + oDateTimeFormat.timeFormat
    };
    let oButtonView = (<div className={"calenderSection"}>
      <div className={"dateRangeSection"}>{sStartDate + " - " + sEndDate}</div>
      <div className={"dateRangeCalenderIcon"}></div>
    </div>);

    let oDateRangePickerView = (
        <DateRangePickerView
            context={oDownloadInfoData.context}
            startDate={oDownloadRange.startTime}
            endDate={oDownloadRange.endTime}
            ranges={oSelectionRange}
            dateRangeLocale={oDateRangeLocale}
            calendarOpenDirection={'left'}
            alwaysShowCalendars={true}
            showDropdowns={true}
            timePicker24Hour={true}
            timePicker={true}
            timePickerSeconds={true}>
          {oButtonView}
        </DateRangePickerView>);

    return (
        <div className={"sectionAssetDownloadInfo"}>
          <div className="sectionTitle">{getTranslation().DOWNLOAD_INFO}</div>
          <div className={"downloadInfoDetails"}>
            <div className={"totalDownloadSection"}>
              <div className={"sectionTitle"}>{getTranslation().TOTAL_DOWNLOADS}</div>
              {this.getDownloadCountView(iTotalDownloadCount)}
            </div>
            <div className={"downloadWithinRangeSection"}>
              <div className={"sectionTitleWrapper"}>
                <div className={"sectionTitle"}>{getTranslation().DOWNLOADS_WITHIN_DATE_RANGE}</div>
                {oDateRangePickerView}
              </div>
              {this.getDownloadCountView(iDownloadCountWithinRange)}
            </div>
          </div>
        </div>
    );
  }
}

export const view = ContentDownloadTrackerView;
export const events = oEvents;
