import Moment from "moment";

let oDateFilters = {
  today: {
    id: "today",
    label: "TODAY",
    value: [Moment(), Moment()],
  },
  yesterday: {
    id: "yesterday",
    label: "YESTERDAY",
    value: [Moment().subtract(1, 'days'), Moment().subtract(1, 'days')],
  },
  last7Days: {
    id: "last7Days",
    label: "LAST_SEVEN_DAYS",
    value: [Moment().subtract(6, 'days'), Moment()],
  },
  last30Days: {
    id: "last30Days",
    label: "LAST_THIRTY_DAYS",
    value: [Moment().subtract(29, 'days'), Moment()],
  },
  thisMonth: {
    id: "thisMonth",
    label: "THIS_MONTH",
    value: [Moment().startOf('month'), Moment().endOf('month')],
  },
};

export default oDateFilters;