package com.cs.base.strategy.postgres.constants;

public class AuditLogConstant {
  
  public static final String SQL_BASE_DIR                        = "sql";
  
  public static final String AUDIT_LOG_DDL                       = SQL_BASE_DIR     + "/auditLog-DDL.sql";
  
  public static final String AUDIT_LOG                           = "audit_log";
  public static final String AUDIT_LOG_SQUENCE_NO                = "audit_log_sequence";
  
  public static final String AUDIT_LOG_EXPORT_TRACKER_TABLE      = "audit_log_export_tracker";
  public static final String AUDIT_LOG_EXPORT_TRACKER_SQUENCE_NO = "audit_log_export_tracker_sequence";
  
}
