const oEndpointTypes = {
  INBOUND: "onboardingendpoint",
  OUTBOUND: "offboardingendpoint"
}

const oEndpointTileModes = {
  UPLOAD_SUMMARY: "uploadSummary",
  ALL_UPLOADS: "allUploads",
  LAST_UPLOAD: "lastUpload",
  DOWNLOAD_SUMMARY: "downloadSummary",
  ALL_DOWNLOADS: "allDownloads",
  LAST_DOWNLOAD: "lastDownload"
}

const oKpiTileButton = {
  PREVIOUS_BUTTON: "previous",
    NEXT_BUTTON: "next"
}

const oKpiTileView = {
  NORMAL_VIEW: "normalView",
    DIALOG_VIEW: "dialogView"
}

export default {
  endpointTypes : oEndpointTypes,
  endpointTileModes : oEndpointTileModes,
  kpiTileButton : oKpiTileButton,
  kpiTileView : oKpiTileView
}