export default {

  taskplanned: {
    "accountable": ["taskready"]
  },
  taskready: {
    "responsible": ["taskinprogress", "taskdeclined"]
  },
  taskinprogress: {
    "responsible": ["taskdone"]
  },
  taskdeclined: {
    "accountable": ["taskplanned"]
  },
  taskdone: {
    "verify": ["taskinprogress", "taskverified"]
  },
  taskverified: {
    "signoff": ["taskinprogress", "tasksignedoff"]
  },
  tasksignedoff: {}
};