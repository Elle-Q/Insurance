# insert init super admin account with user/password: admin/welcome1
insert into insurance_finance.sys_user(mobile_phone, password, user_name, is_locked, user_type, channel_code, organization_id, is_channel_admin, last_login_time, last_login_ip, created_at, updated_at, created_by, updated_by)
    values('admin', '632fb2985a1e2b764764b60d497bb036', '超级管理员', 0, 'staff', null, 1, 0, null, null, now(), current_timestamp(), 1, null);

# insert super admin role
insert into insurance_finance.sys_role(role_code, role_name, remark, created_at, updated_at, created_by, updated_by) values ('admin', '超级管理员', '超级系统管理员，拥有所有操作的权限', now(), current_timestamp(), 1, null);
# insert other roles
insert into insurance_finance.sys_role(role_code, role_name, remark, created_at, updated_at, created_by, updated_by) values ('operator', '运营', '查看所有界面的权限', now(), current_timestamp(), 1, null);
insert into insurance_finance.sys_role(role_code, role_name, remark, created_at, updated_at, created_by, updated_by) values ('risker', '风控', '查看所有界面的权限', now(), current_timestamp(), 1, null);
insert into insurance_finance.sys_role(role_code, role_name, remark, created_at, updated_at, created_by, updated_by) values ('leader', '领导', '查看所有界面的权限', now(), current_timestamp(), 1, null);
insert into insurance_finance.sys_role(role_code, role_name, remark, created_at, updated_at, created_by, updated_by) values ('treasurer', '财务', '查看所有界面权限(拥有操作权限：取消、确定已放款，确定已退保，确定已还款，确认已支付、扣款)', now(), current_timestamp(), 1, null);
insert into insurance_finance.sys_role(role_code, role_name, remark, created_at, updated_at, created_by, updated_by) values ('auditor', '审核员', '查看所有界面的权限(拥有操作权限：审核)', now(), current_timestamp(), 1, null);

# insert super admin with role relation
insert into insurance_finance.sys_user_role(user_id, role_id) values (1, 1);

# insert nomi organization data
insert into insurance_finance.data_organization(company_name, contact_name, contact_phone, parent_id, root_id, area_code, orgnization_sequence, created_at, updated_at, created_by, updated_by)
  values ('诺米金融服务有限公司', '', '', null, 1, '4403', '001', now(), current_timestamp(), 1, null);
insert into insurance_finance.data_organization(company_name, contact_name, contact_phone, parent_id, root_id, area_code, orgnization_sequence, created_at, updated_at, created_by, updated_by)
  values ('诺米金融服务有限公司北京分公司', '', '', 1, 1, '', '001', now(), current_timestamp(), 1, null);

# insert bank code

# insert oauth client details
insert into oauth_client_details(client_id, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES
  ('gateway-server', 'X33OgMZLJhiu70ZFZMIn7GYBjElMVQYf', 'all', 'password,client_credentials,refresh_token', null, null, 3600 * 24 * 7, 30 * 24 * 3600, null, true);

# insert config property
insert into data_config_property(config_name, config_code, data_type, config_value, unit_suffix, created_at, updated_at, created_by, is_collection) VALUES ('最大逾期天数', 'maxOverdueDays', 'number', '5', '天', now(), current_timestamp(), 1, 0);
insert into data_config_property(config_name, config_code, data_type, config_value, unit_suffix, created_at, updated_at, created_by, is_collection) VALUES ('还款提醒提前天数', 'aheadRemindDays', 'number', '3', '天', now(), current_timestamp(), 1, 0);
insert into data_config_property(config_name, config_code, data_type, config_value, unit_suffix, created_at, updated_at, created_by, is_collection) VALUES ('还款提前天数（自然日）', 'aheadRefundDays', 'number', '3', '天', now(), current_timestamp(), 1, 0);
insert into data_config_property(config_name, config_code, data_type, config_value, unit_suffix, created_at, updated_at, created_by, is_collection) VALUES ('可借比例', 'borrowRate', 'rate', '100', '%', now(), current_timestamp(), 1, 0);

# insert ad position data
insert into data_advertisement_position(code, name, display_limit, description, created_at, updated_at, created_by)
    VALUES ('CUST_WECHAT_BANNER', '微信客户端Banner位', 3, '微信客户端Banner位', now(), now(), 1);
insert into data_advertisement_position(code, name, display_limit, description, created_at, updated_at, created_by)
    VALUES ('CHANNEL_WECHAT_BANNER', '微信渠道端Banner位', 3, '微信渠道端Banner位', now(), now(), 1);

# 易极付支持银行代扣数据
INSERT INTO finance_enterprise_bank (enterprise_code, application_code, app_bank_code, enterprise_bank_code, bank_name, single_limit, daily_limit, disable_flag)
VALUES
  ("YiJiFu", "DEBT", "CBC", "CBC", "中国建设银行", 500000, 500000, 0),
  ("YiJiFu", "DEBT", "ABC", "ABC", "中国农业银行", 500000, 1000000, 0),
  ("YiJiFu", "DEBT", "CEB", "CEB", "中国光大银行", 5000000, 10000000, 0),
#  ("YiJiFu", "DEBT", "CMSB", "CMSB", "中国民生银行", 500000, 1000000, 0),
  ("YiJiFu", "DEBT", "CCB", "CITIC", "中信银行", 5000000, null, 0),
  ("YiJiFu", "DEBT", "RCB", "CQRCB", "重庆农商银行", 500000, 500000, 0),
  ("YiJiFu", "DEBT", "CIB", "CIB", "兴业银行", 5000000, 15000000, 0),
  ("YiJiFu", "DEBT", "ICBC", "ICBC", "中国工商银行", 1000000, 5000000, 0),
  ("YiJiFu", "DEBT", "PSBC", "PSBC", "中国邮政储蓄银行", 500000, 500000, 0),
  ("YiJiFu", "DEBT", "PAB", "PINGANBK", "平安银行", 5000000, 50000000, 0),
  ("YiJiFu", "DEBT", "BOC", "BOC", "中国银行", null, null, 0),
  ("YiJiFu", "DEBT", "COMM", "COMM", "交通银行", 1000000, 1000000, 0),
  ("YiJiFu", "DEBT", "CGB", "CGB", "广发银行", 100000000, null, 0),
  ("YiJiFu", "DEBT", "CMB", "CMB", "招商银行", 100000, null, 0),
  ("YiJiFu", "DEBT", "SPDB", "SPDB", "浦发银行", 5000000, 5000000, 0);

# 易极付支持银行验卡数据
INSERT INTO finance_enterprise_bank (enterprise_code, application_code, app_bank_code, enterprise_bank_code, bank_name, single_limit, daily_limit, disable_flag)
VALUES
  ("YiJiFu", "VERIFY", "CBC", "CBC", "中国建设银行", NULL, NULL, 0),
  ("YiJiFu", "VERIFY", "ABC", "ABC", "中国农业银行", NULL, NULL, 0),
  ("YiJiFu", "VERIFY", "CEB", "CEB", "中国光大银行", NULL, NULL, 0),
  ("YiJiFu", "VERIFY", "CMSB", "CMSB", "中国民生银行", NULL, NULL, 0),
  ("YiJiFu", "VERIFY", "CCB", "CCB", "中信银行", NULL, NULL, 0),
  ("YiJiFu", "VERIFY", "RCB", "CQRCB", "重庆农商银行", NULL, NULL, 0),
  ("YiJiFu", "VERIFY", "CIB", "CIB", "兴业银行", NULL, NULL, 0),
  ("YiJiFu", "VERIFY", "ICBC", "ICBC", "中国工商银行", NULL, NULL, 0),
  ("YiJiFu", "VERIFY", "PSBC", "PSBC", "中国邮政储蓄银行", NULL, NULL, 0),
  ("YiJiFu", "VERIFY", "PAB", "PINGANBK", "平安银行", NULL, NULL, 0),
  ("YiJiFu", "VERIFY", "BOC", "BOC", "中国银行", NULL, NULL, 0),
  ("YiJiFu", "VERIFY", "COMM", "COMM", "交通银行", NULL, NULL, 0),
  ("YiJiFu", "VERIFY", "CGB", "CGB", "广发银行", NULL, NULL, 0),
  ("YiJiFu", "VERIFY", "CMB", "CMB", "招商银行", NULL, NULL, 0);

# 插入财务权限项
insert into sys_permission(permission_code, permission_name, remark, created_at, updated_at, created_by) VALUES
  ('requisition.debit', '服务费扣款', '服务费扣款', now(), current_timestamp(), 1),
  ('requisition.cancel', '取消申请单', '取消申请单', now(), current_timestamp(), 1),
  ('requisition.loaned', '确认放款', '确定已放款', now(), current_timestamp(), 1),
  ('contract.withhold', '确认退保', '确认已退保', now(), current_timestamp(), 1),
  ('repayment.confirm_refund', '确认还款', '确认已还款', now(), current_timestamp(), 1),
  ('requisition.paid', '确认服务费已支付', '确认服务费已支付', now(), current_timestamp(), 1),
  ('repayment.refund_withhold', '扣款', '扣款', now(), current_timestamp(), 1);

# 插入财务的权限关系
INSERT INTO sys_role_permission(role_id, permission_id)
  SELECT sr.id AS role_id, sp.id AS permission_id
  FROM sys_permission sp, sys_role sr WHERE sp.permission_code IN ('requisition.cancel', 'requisition.loaned', 'contract.withhold', 'repayment.confirm_refund', 'requisition.paid', 'repayment.refund_withhold', 'requisition.debit')
  AND sr.role_code = 'treasurer' AND NOT EXISTS (SELECT 'x' FROM sys_role_permission srp WHERE srp.permission_id = sp.id AND srp.role_id = sr.id);

# 插入审核者的权限项
insert into sys_permission(permission_code, permission_name, remark, created_at, updated_at, created_by) VALUES
  ('requisition.audit', '申请单审核', '申请单审核', now(), current_timestamp(), 1);

# 插入审核者的权限关系
INSERT INTO sys_role_permission(role_id, permission_id)
  SELECT sr.id AS role_id, sp.id AS permission_id
  FROM sys_permission sp, sys_role sr WHERE sp.permission_code IN ('requisition.audit') AND sr.role_code = 'auditor' AND NOT EXISTS (SELECT 'x' FROM sys_role_permission srp WHERE srp.permission_id = sp.id AND srp.role_id = sr.id);

# 插入超级管理员的权限关系
INSERT INTO sys_role_permission(role_id, permission_id)
  SELECT sr.id AS role_id, sp.id AS permission_id
  FROM sys_permission sp, sys_role sr WHERE sp.permission_code IN ('requisition.audit', 'requisition.debit', 'requisition.cancel', 'requisition.loaned', 'contract.withhold', 'repayment.confirm_refund', 'requisition.paid', 'repayment.refund_withhold')
  AND sr.role_code = 'admin' AND NOT EXISTS (SELECT 'x' FROM sys_role_permission srp WHERE srp.permission_id = sp.id AND srp.role_id = sr.id);
