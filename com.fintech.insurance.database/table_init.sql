/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/12/13 14:28:49                          */
/*==============================================================*/


drop table if exists insurance_finance.busi_channel;

drop table if exists insurance_finance.busi_contract;

drop table if exists insurance_finance.busi_product;

drop table if exists insurance_finance.busi_product_channel;

drop table if exists busi_product_rate;

drop table if exists insurance_finance.busi_requisition;

drop table if exists insurance_finance.busi_requisition_detail;

drop table if exists insurance_finance.busi_requisition_detail_resource;

drop table if exists insurance_finance.cust_account;

drop table if exists insurance_finance.cust_account_info;

drop table if exists insurance_finance.cust_account_oauth;

drop table if exists insurance_finance.cust_bank_card;

drop table if exists insurance_finance.cust_consultation;

drop table if exists insurance_finance.data_advertisement;

drop table if exists insurance_finance.data_advertisement_position;

drop table if exists insurance_finance.data_app_bank;

drop table if exists insurance_finance.data_config_property;

drop table if exists insurance_finance.data_insurance_branch;

drop table if exists insurance_finance.data_insurance_company;

drop table if exists insurance_finance.data_organization;

drop table if exists insurance_finance.finance_account_voucher;

drop table if exists insurance_finance.finance_enterprise_bank;

drop table if exists insurance_finance.finance_bankcard_verify_record;

drop table if exists insurance_finance.finance_payment_order;

drop table if exists insurance_finance.finance_payment_order_detail;

drop table if exists insurance_finance.finance_repayment_plan;

drop table if exists insurance_finance.finance_repayment_record;

drop table if exists finance_yijifu_log;

drop table if exists oauth_client_details;

drop table if exists oauth_client_token;

drop table if exists insurance_finance.sys_entity_audit_log;

drop table if exists insurance_finance.sys_entity_operation_log;

drop table if exists insurance_finance.sys_permission;

drop table if exists insurance_finance.sys_role;

drop table if exists insurance_finance.sys_role_permission;

drop table if exists insurance_finance.sys_user;

drop table if exists insurance_finance.sys_user_oauth;

drop table if exists insurance_finance.sys_user_role;

drop table if exists insurance_finance.timer_log;

/*==============================================================*/
/* Table: busi_channel                                          */
/*==============================================================*/
create table insurance_finance.busi_channel
(
   id                   int(10) unsigned not null auto_increment,
   channel_code         varchar(32) not null comment '渠道编码，创建时自动生成，生成规则参考需求文档，全局唯一',
   channel_name         varchar(128) not null comment '渠道名称',
   id_number            varchar(32) comment '身份证号码',
   business_licence     varchar(64) comment '营业执照号码',
   organization_id      int(10) unsigned not null comment '该渠道所属的公司id',
   area_code            varchar(12) not null comment '该渠道所属的地区编码',
   is_locked            boolean not null default 0 comment '是否被锁定，被锁定后不能在该渠道展开业务',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   created_by           int(10) unsigned not null comment '创建者',
   updated_by           int(10) unsigned comment '最近更新者',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.busi_channel comment '渠道信息表';

/*==============================================================*/
/* Index: idx_channel_partner_org                               */
/*==============================================================*/
create index idx_channel_partner_org on insurance_finance.busi_channel
(
   organization_id
);

/*==============================================================*/
/* Index: uq_channel_partner_code                               */
/*==============================================================*/
create unique index uq_channel_partner_code on insurance_finance.busi_channel
(
   channel_code
);

/*==============================================================*/
/* Table: busi_contract                                         */
/*==============================================================*/
create table insurance_finance.busi_contract
(
   id                   int(10) unsigned not null auto_increment,
   requisition_id       int(10) unsigned not null,
   channel_id           int(10) unsigned not null,
   customer_id          int(10) unsigned not null comment '申请业务的客户id，关联customer_account表的主键',
   customer_contract_number   varchar(64) not null COMMENT '正式的合同号, 在用户支付服务单成功之后生成',
   contract_status      varchar(32) not null default 'init' comment '合同状态',
   contract_number      varchar(64) COMMENT '用于内部关联的合同号',
   content_file         varchar(32) comment '合同内容文件，转存七牛之后的文件id，格式为图片',
   content_signed_doc   varchar(32) comment '合同存储在七牛的文件id，格式为docx或者pdf',
   bestsign_file        varchar(64) comment '上上签文件id',
   service_content_file varchar(32) comment '服务合同内容文件，转存七牛之后的文件id，格式为图片',
   service_signed_doc   varchar(32) comment '服务合同转存七牛的文件id,格式为docx或者pdf',
   service_bestsign_file varchar(64) comment '服务合同的上上签文件id',
   contract_amount      bigint unsigned not null,
   business_duration    int unsigned not null comment '合同的借款期数',
   start_date           date not null comment '借款日开始日期',
   end_date             date not null comment '借款日终止日期',
   interest_type        varchar(32) not null,
   interest_rate        double unsigned not null,
   loan_ratio           double unsigned not null,
   created_at           datetime not null,
   updated_at           timestamp not null,
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.busi_contract comment '业务合同表';

/*==============================================================*/
/* Index: idx_busi_contract_customer                            */
/*==============================================================*/
create index idx_busi_contract_customer on insurance_finance.busi_contract
(
   customer_id
);

/*==============================================================*/
/* Index: uq_busi_contract_serial                               */
/*==============================================================*/
create unique index uq_busi_contract_serial on insurance_finance.busi_contract
(
   customer_contract_number
);

/*==============================================================*/
/* Index: uq_busi_contract_number                              */
/*==============================================================*/
create index uq_busi_contract_number on insurance_finance.busi_contract
(
   contract_number
);

/*==============================================================*/
/* Table: busi_product                                          */
/*==============================================================*/
create table insurance_finance.busi_product
(
   id                   int(10) unsigned not null auto_increment,
   product_name         varchar(64) not null comment '产品名称',
   product_code         varchar(32) not null comment '产品编码，创建时自动生成，全局唯一',
   product_type         varchar(32) not null comment '产品类型，分期或者抵押',
   product_icon         varchar(32) comment '产品图标',
   product_banner       varchar(32) comment '产品广告图',
   is_onsale            boolean not null default 0 comment '是否已经上架',
   repay_type           varchar(32) not null comment '还款类型',
   repay_day_type       varchar(32) not null comment '还款日类型',
   service_fee_rate     double unsigned not null default 0 comment '服务费率（万倍）',
   other_fee_rate       double unsigned not null default 0 comment '其他费用的费率（万倍）',
   prepayment_penalty_rate double unsigned not null default 0 comment '提前还款罚息率（万倍）',
   prepayment_days      int(10) unsigned comment '提前还款天数',
   overdue_fine_rate    double unsigned not null default 0 comment '逾期罚息率（万倍）',
   max_overdue_days     int unsigned not null default 0 comment '最大逾期天数',
   product_description  text comment '产品描述',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   created_by           int(10) unsigned not null comment '创建者',
   updated_by           int(10) unsigned comment '更新者',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.busi_product comment '产品信息表';

/*==============================================================*/
/* Index: idx_product_repay                                     */
/*==============================================================*/
create index idx_product_repay on insurance_finance.busi_product
(
   repay_type
);

/*==============================================================*/
/* Index: idx_product_repayday_type                             */
/*==============================================================*/
create index idx_product_repayday_type on insurance_finance.busi_product
(
   repay_day_type
);

/*==============================================================*/
/* Index: uq_product_code                                       */
/*==============================================================*/
create unique index uq_product_code on insurance_finance.busi_product
(
   product_code
);

/*==============================================================*/
/* Table: busi_product_channel                                  */
/*==============================================================*/
create table insurance_finance.busi_product_channel
(
   id                   int(10) unsigned not null auto_increment,
   product_id           int(10) unsigned not null comment '产品id，关联business_product表的主键',
   channel_id           int(10) unsigned not null comment '渠道id，关联business_channel表的主键',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.busi_product_channel comment '产品渠道配置表';

/*==============================================================*/
/* Index: uq_product_channel                                    */
/*==============================================================*/
create unique index uq_product_channel on insurance_finance.busi_product_channel
(
   product_id,
   channel_id
);

/*==============================================================*/
/* Table: busi_product_rate                                     */
/*==============================================================*/
create table busi_product_rate
(
   id                   int(10) unsigned not null auto_increment,
   product_id           int(10) unsigned not null,
   business_duration    int not null,
   interest_type        varchar(32) not null comment '计息类型，按月或者按日计息',
   interest_rate        double unsigned not null comment '利率点（百倍）',
   loan_ratio           double unsigned not null default 100 comment '可借比例',
   primary key (id)
)
   engine = InnoDB
charset = UTF8MB4;

alter table busi_product_rate comment '产品利率配置表';

/*==============================================================*/
/* Table: busi_requisition                                      */
/*==============================================================*/
create table insurance_finance.busi_requisition
(
   id                   int(10) unsigned not null auto_increment,
   customer_id          int(10) unsigned not null comment '申请业务的客户id，关联customer_account表的主键',
   customer_account_info_id       int(10) comment '申请业务的客户账户id，关联customer_account_info表的主键',
   channel_id           int(10) unsigned not null comment '渠道id，关联sys_channel表的主键',
   product_id           int(10) unsigned not null comment '产品id，关联busi_product表的主键',
   product_type         varchar(32) not null comment '产品类型',
   is_channel_application boolean not null default 1 comment '是否为渠道人员录单',
   channel_user_id      int(10) unsigned comment '渠道用户id，关联sys_user表中的主键',
   requisition_status   varchar(16) not null,
   requisition_number   varchar(64) not null comment '订单号',
   repay_type           varchar(32) not null,
   repay_day_type       varchar(32) not null,
   service_fee_rate     double not null default 0,
   other_fee_rate       double not null default 0,
   prepayment_penalty_rate double not null default 0,
   prepayment_days      int(10) unsigned not null default 0,
   overdue_fine_rate    double not null default 0,
   max_overdue_days     int(10) unsigned not null default 0,
   payment_order_number varchar(64) comment '支付订单号',
   total_commercial_amount bigint not null comment '商业保险总价值，单位为分',
   total_compulsory_amount bigint not null comment '交强险总价值',
   total_tax_amount     bigint not null comment '车船税总价值',
   total_apply_amount   bigint not null comment '总订单金额，单位为分',
   business_duration    int unsigned not null default 0 comment '客户指定的分期数，0则不指定，自动匹配',
   submission_date      DATETIME NULL DEFAULT NULL comment '提交日期',
   latest_audit_batch   varchar(32) comment '最近提交审核的批次号',
   loan_account_type    varchar(32) not null comment '放款帐户类型',
   loan_account_number  varchar(32),
   loan_account_bank    varchar(16) comment '放款帐户银行编码',
   loan_account_bank_branch varchar(256),
   loan_account_name    varchar(256),
   insurance_company_name  VARCHAR(256) COMMENT '保险公司名称',
   insurance_branch_name   VARCHAR(256) COMMENT '保险公司支部名称',
   loan_time            datetime comment '放款时间',
   audit_success_time   datetime comment '审核通过时间',
   other_resource       text comment '其他材料资源',
   id_card_evidence     varchar(32) comment '手持身份证照片',
   created_at           datetime not null,
   updated_at           timestamp not null,
   created_by           int(10) unsigned not null,
   updated_by           int(10),
   primary key (id)
)
   engine = InnoDB
charset = UTF8MB4;

alter table insurance_finance.busi_requisition comment '业务申请信息表';

/*==============================================================*/
/* Index: uq_busi_requisition_number                            */
/*==============================================================*/
create unique index uq_busi_requisition_number on insurance_finance.busi_requisition
(
   requisition_number
);

/*==============================================================*/
/* Index: idx_busi_requisition_payment                          */
/*==============================================================*/
create index idx_busi_requisition_payment on insurance_finance.busi_requisition
(
   payment_order_number
);

/*==============================================================*/
/* Index: idx_busi_requisition_customer                         */
/*==============================================================*/
create index idx_busi_requisition_customer on insurance_finance.busi_requisition
(
   customer_id
);

/*==============================================================*/
/* Table: busi_requisition_detail                               */
/*==============================================================*/
create table insurance_finance.busi_requisition_detail
(
   id                   int(10) unsigned not null auto_increment,
   requisition_id       int(10) unsigned not null comment '业务申请单id，关联busi_requisition表的主键',
   car_number           varchar(32) comment '车牌号或者合格证号',
   driving_license      varchar(32),
   driving_license_main varchar(32) comment '行驶证正本照片',
   driving_license_attach varchar(32) comment '行驶证副本照片',
   commercial_insurance_number varchar(64) not null comment '商业保险保单号或者投保单号',
   commercial_insurance_amount bigint unsigned not null comment '商业保险金额，单位为分',
   commercial_insurance_start date not null,
   commercial_insurance_end date not null,
   commercial_insurance_value bigint unsigned not null comment '商业保险保单残值',
   max_duration         int not null comment '最大业务期数，按月计算',
   business_duration    int comment '业务开展的期数，按月计算',
   compulsory_insurance_number varchar(64) comment '交强险保单号',
   compulsory_insurance_amount bigint unsigned not null default 0 comment '交强险金额，单位为分',
   compulsory_insurance_start date,
   compulsory_insurance_end date,
   tax_amount           bigint unsigned not null comment '车船税金额，单位为分',
   is_commercial_only   boolean not null default 0 comment '是否只申请商业保险标志位',
   sub_total            bigint unsigned not null default 0 comment '小计金额',
   business_contract_id int(10) unsigned comment '合同id',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.busi_requisition_detail comment '业务申请明细表';

/*==============================================================*/
/* Index: idx_driving_license                                   */
/*==============================================================*/
create index idx_driving_license on insurance_finance.busi_requisition_detail
(
   driving_license
);

/*==============================================================*/
/* Index: idx_commercial_insurance_number                       */
/*==============================================================*/
create index idx_commercial_insurance_number on insurance_finance.busi_requisition_detail
(
   commercial_insurance_number
);

/*==============================================================*/
/* Index: idx_compulsory_insurance_number                       */
/*==============================================================*/
create index idx_compulsory_insurance_number on insurance_finance.busi_requisition_detail
(
   compulsory_insurance_number
);

/*==============================================================*/
/* Table: busi_requisition_detail_resource                      */
/*==============================================================*/
create table insurance_finance.busi_requisition_detail_resource
(
   id                   int(10) not null auto_increment,
   requisition_detail_id int(10) unsigned not null,
   resource_type        varchar(32) not null comment '资源类型',
   resouce_name         varchar(64) not null comment '资源名称，对应为保单号码或者车船税',
   resource_picture     varchar(32) not null comment '资源图片',
   display_sequence     int not null comment '排序号',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.busi_requisition_detail_resource comment '业务申请明细资源表';

/*==============================================================*/
/* Index: idx_requisition_detail_res                            */
/*==============================================================*/
create index idx_requisition_detail_res on insurance_finance.busi_requisition_detail_resource
(
   resource_type
);

/*==============================================================*/
/* Table: cust_account                                          */
/*==============================================================*/
create table insurance_finance.cust_account
(
   id                   int(10) unsigned not null auto_increment,
   id_number            varchar(32) not null comment '身份证号码，全局唯一',
   certification_id     varchar(64) comment '证书id',
   id_front             varchar(32) not null comment '身份证正面照片',
   id_back              varchar(32) not null comment '身份证反面照片',
   is_locked            boolean not null default 0 comment '是否冻结标志位',
   locked_at            timestamp comment '冻结时间',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.cust_account comment '客户账户表';

/*==============================================================*/
/* Index: uq_customer_account                                   */
/*==============================================================*/
create unique index uq_customer_account on insurance_finance.cust_account
(
   id_number
);

/*==============================================================*/
/* Table: cust_account_info                                     */
/*==============================================================*/
create table insurance_finance.cust_account_info
(
   id                   int(10) unsigned not null auto_increment,
   account_id           int(10) unsigned not null comment '关联customer_account表的主键',
   channel_code         varchar(32) not null comment '渠道编码，该客户的来源渠道',
   channel_user_id      int(10) UNSIGNED not null comment '录入客户信息的渠道用户id',
   customer_name        varchar(64) not null comment '客户姓名',
   customer_mobile      varchar(15) not null comment '客户手机号码',
   account_number       varchar(32) not null comment '银行账户号',
   account_bank_name    varchar(128) comment '银行帐户开户行名称',
   account_bank_branch  varchar(256) comment '银行帐户开户行支行',
   bank_card_picture    varchar(32) not null comment '银行卡正面照',
   enterprise_name      varchar(128) comment '企业名称',
   business_licence     varchar(64) comment '营业执照号码',
   business_licence_picture varchar(32) comment '营业执照照片',
   is_enterprise_default boolean not null default 0 comment '是否为企业默认信息',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.cust_account_info comment '客户账户详细信息表';

/*==============================================================*/
/* Index: idx_customer_bank_acct                                 */
/*==============================================================*/
create index idx_customer_bank_acct on insurance_finance.cust_account_info
(
   account_number,
   customer_mobile,
   channel_code
);

/*==============================================================*/
/* Index: idx_customer_channel                                  */
/*==============================================================*/
create index idx_customer_channel on insurance_finance.cust_account_info
(
   channel_code
);

/*==============================================================*/
/* Index: idx_customer_ent                                      */
/*==============================================================*/
create index idx_customer_ent on insurance_finance.cust_account_info
(
   business_licence
);

/*==============================================================*/
/* Table: cust_account_oauth                                    */
/*==============================================================*/
create table insurance_finance.cust_account_oauth
(
   id                   int(10) unsigned not null auto_increment,
   account_id           int(10) unsigned not null comment '客户账户id，关联customer_account表的主键',
   oauth_type           varchar(32) not null comment '授权类型，见用户授权信息表描述',
   oauth_app_id         varchar(64) not null comment '第三方应用id',
   oauth_account        varchar(64) not null comment '第三方帐号id',
   nick_name            varchar(32) comment '第三方帐号信息中的昵称',
   gender               varchar(16) comment '第三方帐号信息中的性别',
   header_image         tinytext comment '第三方帐号信息中的头像',
   wx_unionid           varchar(64) comment '见用户授权信息表描述',
   oauth_content        text comment '完整的第三方授权信息',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.cust_account_oauth comment '客户帐号授权信息表';

/*==============================================================*/
/* Index: uq_customer_account_oauth                             */
/*==============================================================*/
create unique index uq_customer_account_oauth on insurance_finance.cust_account_oauth
(
   account_id,
   oauth_app_id,
   oauth_type
);

/*==============================================================*/
/* Table: cust_bank_card                                        */
/*==============================================================*/
create table insurance_finance.cust_bank_card
(
   id                   int(10) not null auto_increment,
   account_id           int(10) unsigned not null,
   account_bank         varchar(16) not null,
   account_number       varchar(32) not null,
   account_mobile       varchar(16) not null,
   created_at           datetime not null,
   updated_at           timestamp not null,
   disable_flag         boolean not null default 0,
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.cust_bank_card comment '客户银行卡信息表';

/*==============================================================*/
/* Table: cust_consultation                                     */
/*==============================================================*/
create table insurance_finance.cust_consultation
(
   id                   int(10) unsigned not null auto_increment,
   customer_mobile      varchar(15) not null comment '客户手机号码',
   customer_name        varchar(64) not null comment '客户姓名',
   consult_content      text not null comment '咨询内容',
   process_status       varchar(16) not null comment '处理状态',
   process_time         datetime,
   process_remark       text comment '处理的备注',
   oauth_type           varchar(32) not null comment '授权类型',
   oauth_app_id         varchar(64) not null comment '第三方授权的应用id',
   oauth_account        varchar(64) not null comment '第三方账号id',
   wx_unionid           varchar(64) comment '微信授权后获取的unionid',
   created_at           datetime not null,
   updated_at           timestamp not null,
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.cust_consultation comment '客户咨询信息表';

/*==============================================================*/
/* Index: idx_consultation_mobile                               */
/*==============================================================*/
create index idx_consultation_mobile on insurance_finance.cust_consultation
(
   customer_mobile
);

/*==============================================================*/
/* Index: uq_consultation_oauth                                 */
/*==============================================================*/
create index idx_consultation_oauth on insurance_finance.cust_consultation
(
   oauth_app_id,
   oauth_account,
   oauth_type
);

/*==============================================================*/
/* Table: data_advertisement                                    */
/*==============================================================*/
create table insurance_finance.data_advertisement
(
   id                   int(10) unsigned not null auto_increment,
   position_id          int(10) unsigned not null,
   is_display           boolean not null default 1,
   title                varchar(256),
   content              text,
   url                  varchar(1024),
   image                varchar(64),
   start_at             datetime,
   end_at               datetime,
   display_sequence     int(11),
   created_at           datetime not null,
   updated_at           timestamp not null,
   created_by           int(10) unsigned not null,
   updated_by           int(10) unsigned,
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.data_advertisement comment '广告表';

/*==============================================================*/
/* Table: data_advertisement_position                           */
/*==============================================================*/
create table insurance_finance.data_advertisement_position
(
   id                   int(10) unsigned not null auto_increment,
   code                 varchar(64) not null,
   name                 varchar(128),
   display_limit        int(10) unsigned not null default 10,
   description          varchar(256),
   created_at           datetime not null,
   updated_at           timestamp not null,
   created_by           int(10) unsigned not null,
   updated_by           int(10) unsigned,
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.data_advertisement_position comment '广告位表';

/*==============================================================*/
/* Index: uq_ad_position_code                                   */
/*==============================================================*/
create unique index uq_ad_position_code on insurance_finance.data_advertisement_position
(
   code
);

/*==============================================================*/
/* Table: data_app_bank                                         */
/*==============================================================*/
create table insurance_finance.data_app_bank
(
   id                   int(10) unsigned not null auto_increment,
   bank_code            varchar(16) not null,
   bank_name            varchar(128) not null,
   created_at           datetime not null,
   updated_at           timestamp not null,
   created_by           int(10) unsigned not null,
   updated_by           int(10) unsigned,
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.data_app_bank comment '银行信息表';

/*==============================================================*/
/* Index: uq_bank_code                                          */
/*==============================================================*/
create unique index uq_bank_code on insurance_finance.data_app_bank
(
   bank_code
);

/*==============================================================*/
/* Table: data_config_property                                  */
/*==============================================================*/
create table insurance_finance.data_config_property
(
   id                   int(10) unsigned not null auto_increment,
   config_name          varchar(32) not null comment '配置名称',
   config_code          varchar(32) not null comment '配置项编码，全局唯一',
   data_type            varchar(32) not null comment '数据类型，分string,number,date,datetime,rate(利率，一万倍)',
   is_collection        boolean not null default 0 comment '是否为集合，是则值存储为一个ｊｓｏｎ数组',
   config_value         text comment '配置值',
   unit_suffix          varchar(16) comment '单位后缀',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   created_by           int(10) unsigned not null comment '创建者',
   updated_by           int(10) unsigned comment '最近更新者',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.data_config_property comment '常量数据配置表';

/*==============================================================*/
/* Index: uq_config_property_code                               */
/*==============================================================*/
create unique index uq_config_property_code on insurance_finance.data_config_property
(
   config_code
);

/*==============================================================*/
/* Table: data_insurance_branch                                 */
/*==============================================================*/
create table insurance_finance.data_insurance_branch
(
   id                   int(10) unsigned not null auto_increment,
   company_id           int(10) unsigned not null comment '保险公司id，关联data_insurance_company表的主键',
   branch_name          varchar(256) not null comment '分支机构名称',
   branch_address       varchar(256) comment '分支机构地址',
   contact_name         varchar(64) comment '对接人姓名',
   contact_phone        varchar(32) comment '对接人电话号码',
   account_bank         varchar(16) not null comment '保费账户开户行编码',
   account_bank_branch  varchar(256) not null comment '保费账户开户行支行名称',
   account_number       varchar(32) not null comment '保费账户',
   account_name         varchar(256) not null comment '保费账户户名',
   is_disabled          boolean not null default 0 comment '是否被禁用，禁用后不能在该支部开展业务',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   created_by           int(10) unsigned not null comment '创建者',
   updated_by           int(10) unsigned comment '最近更新者',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.data_insurance_branch comment '保险公司支部信息表';

/*==============================================================*/
/* Index: fk_insurance_branch_company                           */
/*==============================================================*/
create index fk_insurance_branch_company on insurance_finance.data_insurance_branch
(
   company_id
);

/*==============================================================*/
/* Table: data_insurance_company                                */
/*==============================================================*/
create table insurance_finance.data_insurance_company
(
   id                   int(10) unsigned not null auto_increment,
   company_name         varchar(256) not null comment '保险公司名称',
   contact_name         varchar(64) comment '保险公司对接人姓名',
   contact_phone        varchar(32) comment '保险公司对接人电话号码',
   account_bank         varchar(16) comment '保险公司保费账户开户行编码',
   account_bank_branch  varchar(256) comment '保险公司保费账户开户行支行',
   account_number       varchar(32) comment '保险公司保费账户',
   account_name         varchar(256) comment '保险公司保费账户户名',
   is_account_company   boolean not null default b'1' comment '是否公司级保费账户，为是时该表的保费账户相关字段都不能为空',
   is_disabled          boolean not null default 0 comment '是否被禁用，被禁用后则不能开展该保险公司的业务',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   created_by           int(10) unsigned not null comment '创建者',
   updated_by           int(10) unsigned comment '最近更新者',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.data_insurance_company comment '保险公司信息表';

/*==============================================================*/
/* Index: idx_insurance_company                                 */
/*==============================================================*/
create index idx_insurance_company on insurance_finance.data_insurance_company
(
   company_name
);

/*==============================================================*/
/* Table: data_organization                                     */
/*==============================================================*/
create table insurance_finance.data_organization
(
   id                   int(10) unsigned not null auto_increment,
   company_name         varchar(256) not null comment '公司名称',
   contact_name         varchar(64) comment '公司联系人姓名',
   contact_phone        varchar(32) comment '公司联系人电话号码',
   parent_id            int(10) unsigned comment '上级公司的id，关联本表的主键',
   root_id              int(10) unsigned comment '所属的母公司id',
   area_code            varchar(6) not null comment '公司所在的地区编码',
   orgnization_sequence varchar(4) not null comment '该公司在所属地区的序号',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   created_by           int(10) unsigned not null comment '创建者',
   updated_by           int(10) unsigned comment '最近更新者',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.data_organization comment '公司组织架构表';

/*==============================================================*/
/* Index: idx_org_area                                          */
/*==============================================================*/
create index idx_org_area on insurance_finance.data_organization
(
   area_code
);

/*==============================================================*/
/* Index: idx_org_root                                          */
/*==============================================================*/
create index idx_org_root on insurance_finance.data_organization
(
   root_id
);

/*==============================================================*/
/* Table: finance_account_voucher                               */
/*==============================================================*/
create table insurance_finance.finance_account_voucher
(
   id                   int(10) not null auto_increment,
   account_type         varchar(32) not null comment '记账类型，放款、还款、服务费',
   repayment_record_id  int(10) unsigned,
   payment_order_id     int(10) unsigned,
   requisition_id       int(10) unsigned,
   transaction_serial   varchar(64) not null,
   user_id              int(10) unsigned not null,
   voucher              text not null,
   account_amount       bigint unsigned not null,
   created_at           datetime not null,
   updated_at           timestamp not null,
   remark               text,
   requisition_code     varchar(64),
   primary key (id)
)
   engine = InnoDB
charset = UTF8MB4;

alter table insurance_finance.finance_account_voucher comment '财务出入账凭证';

/*==============================================================*/
/* Index: idx_account_voucher_serial                            */
/*==============================================================*/
create unique index idx_account_voucher_serial on insurance_finance.finance_account_voucher
(
   transaction_serial
);

/*==============================================================*/
/* Index: idx_account_voucher_requisition                       */
/*==============================================================*/
create index idx_account_voucher_requisition on insurance_finance.finance_account_voucher
(
   requisition_id
);

/*==============================================================*/
/* Table: finance_enterprise_bank                               */
/*==============================================================*/
create table insurance_finance.finance_enterprise_bank
(
   id                   int(10) unsigned not null auto_increment,
   enterprise_code      varchar(64) not null comment '银行服务企业编码',
   application_code     varchar(64) not null comment '应用类型编码: 如代扣、 代付等',
   app_bank_code        varchar(64) not null comment '平台内部银行代码',
   enterprise_bank_code varchar(64) not null comment '企业银行银行代码',
   bank_name            varchar(256),
   remark               varchar(256),
   single_limit         bigint(20) unsigned,
   daily_limit          bigint(20) unsigned,
   disable_flag         boolean not null default 0,
   primary key (id)
)
   engine = InnoDB
charset = UTF8MB4;

alter table insurance_finance.finance_enterprise_bank comment '企业银行信息';

/*==============================================================*/
/* Table: finance_bankcard_verify_record                        */
/*==============================================================*/
CREATE TABLE insurance_finance.finance_bankcard_verify_record (
   id int(10) unsigned NOT NULL AUTO_INCREMENT,
   user_name varchar(64) DEFAULT NULL COMMENT '用户名称',
   id_number varchar(32) DEFAULT NULL COMMENT '身份证号',
   bank_card_number varchar(32) DEFAULT NULL COMMENT '银行卡号',
   reserved_mobile varchar(15) NOT NULL COMMENT '预留手机号',
   bank_code varchar(10) DEFAULT NULL COMMENT '银行code',
   bank_name varchar(64) DEFAULT NULL COMMENT '银行名称',
   platform_order_number varchar(64) DEFAULT NULL COMMENT '验卡平台订单号',
   verification_status varchar(32) DEFAULT NULL COMMENT '验卡状态',
   verification_time datetime DEFAULT NULL COMMENT '验卡时间',
   remarks varchar(128) DEFAULT NULL COMMENT '验卡结果备注',
   PRIMARY KEY (id)
)
   ENGINE=InnoDB
   CHARSET=utf8mb4
   COMMENT='验卡记录表';

/*==============================================================*/
/* Index: idx_card_verify_rec                             */
/*==============================================================*/
create index idx_card_verify_rec on insurance_finance.finance_bankcard_verify_record
(
   bank_card_number
);

/*==============================================================*/
/* Table: finance_payment_order                                 */
/*==============================================================*/
create table insurance_finance.finance_payment_order
(
   id                   int(10) unsigned not null auto_increment,
   customer_id          int(10) unsigned not null,
   payment_status       varchar(16) not null comment '支付状态',
   order_number         varchar(64) not null comment '订单号，唯一',
   order_amount         bigint unsigned not null comment '订单金额，折前金额，单位为分',
   discount_amount      bigint unsigned not null default 0 comment '折扣金额，单位为分',
   total_amount         bigint unsigned not null comment '订单总金额，折前金额减去折扣金额，单位为分',
   payment_method       varchar(16) comment '支付方式',
   payment_time         datetime comment '支付时间',
   payment_amount       bigint unsigned not null default 0 comment '支付金额，单位为分',
   bank_account_number  VARCHAR(32) COMMENT '支付卡号或者账号',
   transactiont_serial  varchar(64) comment '支付流水号',
   customer_voucher     text,
   remark               text,
   is_manual            boolean not null default 0 comment '人工介入操作',
   created_at           datetime not null,
   updated_at           timestamp not null,
   primary key (id)
)
   engine = InnoDB
charset = UTF8MB4;

alter table insurance_finance.finance_payment_order comment '客户支付订单表';

/*==============================================================*/
/* Index: uq_cust_payment_order_num                             */
/*==============================================================*/
create unique index uq_cust_payment_order_num on insurance_finance.finance_payment_order
(
   order_number
);

/*==============================================================*/
/* Table: finance_payment_order_detail                          */
/*==============================================================*/
create table insurance_finance.finance_payment_order_detail
(
   id                   int(10) unsigned not null auto_increment,
   order_id             int(10) unsigned not null,
   item_type            varchar(32) not null comment '类型：服务费，其他费用等',
   price                bigint unsigned not null comment '单价，单位为分，在此应该直接计算为服务费金额',
   quantity             int(10) unsigned not null comment '数量，针对服务则设置为1',
   sub_total            bigint not null comment '小计金额，单位为分',
   formula_text         text comment '计算公式，展示时可以使用',
   remark               text comment '备注',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   primary key (id)
)
   engine = InnoDB
charset = UTF8MB4;

alter table insurance_finance.finance_payment_order_detail comment '客户支付订单明细表';

/*==============================================================*/
/* Index: idx_payment_order_detail_type                         */
/*==============================================================*/
create index idx_payment_order_detail_type on insurance_finance.finance_payment_order_detail
(
   item_type
);

/*==============================================================*/
/* Table: finance_repayment_plan                                */
/*==============================================================*/
create table insurance_finance.finance_repayment_plan
(
   id                   int(10) unsigned not null auto_increment,
   customer_id          int(10) unsigned not null comment '客户id,关联cust_account表的主键',
   contract_number      varchar(64) not null,
   channel_id           int(10) unsigned not null comment '渠道id',
   repay_date           date not null,
   repay_total_amount   bigint unsigned not null comment '分期还款当期总金额，单位为分',
   repay_capital_amount bigint unsigned not null comment '分期还款本金金额',
   repay_interest_amount bigint unsigned not null comment '分期还款利息金额',
   rest_capital_amount  bigint UNSIGNED not null comment '包含当期本金金额的总剩余本金金额',
   current_instalment   int unsigned not null comment '还款记录所在的期数',
   total_instalment     int unsigned not null comment '总期数',
   repay_status         varchar(32) not null comment '还款状态',
   manual_flag          boolean not null default 0,
   created_at           datetime not null,
   updated_at           timestamp not null,
   primary key (id)
)
   engine = InnoDB
charset = UTF8MB4;

alter table insurance_finance.finance_repayment_plan comment '还款计划表';

/*==============================================================*/
/* Index: idx_repayment_plan_contract                           */
/*==============================================================*/
create index idx_repayment_plan_contract on insurance_finance.finance_repayment_plan
(
   contract_number
);

/*==============================================================*/
/* Index: idx_repayment_plan_cust                               */
/*==============================================================*/
create index idx_repayment_plan_cust on insurance_finance.finance_repayment_plan
(
   customer_id
);

/*==============================================================*/
/* Index: idx_repayment_plan_channel                            */
/*==============================================================*/
create index idx_repayment_plan_channel on insurance_finance.finance_repayment_plan
(
   channel_id
);

/*==============================================================*/
/* Index: idx_repayment_plan_date                               */
/*==============================================================*/
create index idx_repayment_plan_date on insurance_finance.finance_repayment_plan
(
   repay_date
);

/*==============================================================*/
/* Table: finance_repayment_record                              */
/*==============================================================*/
create table insurance_finance.finance_repayment_record
(
   id                   int(10) unsigned not null auto_increment,
   repayment_plan_id    int(10) unsigned not null,
   repay_date           date not null,
   repay_time           datetime not null,
   repay_batch_no       varchar(32) comment '合并还款批次号',
   repay_total_amount   bigint unsigned not null comment '还款总金额，单位为分',
   repay_capital_amount bigint unsigned not null comment '还款本金，单位为分',
   repay_interest_amount bigint unsigned not null comment '还款利息金额，单位为分',
   overdue_interest_amount bigint unsigned not null comment '逾期罚息金额，单位为分',
   is_overdue           boolean not null comment '是否逾期',
   is_prepayment        boolean not null comment '是否提前还款',
   prepayment_penalty_amount bigint unsigned not null comment '提前还款惩罚金额，单位为分',
   customer_voucher     text comment '客户上传的凭证照片，以json数组存储',
   transaction_serial   varchar(64) comment '交易流水号',
   confirm_status       varchar(16) not null comment '扣款状态：扣款中、扣款成功、扣款失败、结算成功',
   confirmed_by         int(10),
   confirmed_time       datetime,
   remark               text,
   bank_account_number  varchar(32) comment '扣款银行卡卡号',
   created_at           datetime not null,
   updated_at           timestamp not null,
   primary key (id)
)
   engine = InnoDB
charset = UTF8MB4;

alter table insurance_finance.finance_repayment_record comment '还款记录表';

/*==============================================================*/
/* Index: idx_repayment_rec_date                                */
/*==============================================================*/
create index idx_repayment_rec_date on insurance_finance.finance_repayment_record
(
   repay_date
);

/*==============================================================*/
/* Table: finance_yijifu_log                                    */
/*==============================================================*/
create table finance_yijifu_log
(
   id                   int(10) not null auto_increment,
   type                 varchar(32) not null comment '类型: request, response, notification',
   service_code         varchar(32) comment '易极付服务代码',
   message_id           varchar(64) comment '消息标识',
   is_error             boolean default 0,
   error_message        varchar(256),
   content              text comment '请求内容',
   result_code          varchar(64) comment '请求结果状态代码，只有响应和通知类型有该字段',
   created_at           timestamp not null,
   updated_at           datetime,
   primary key (id)
)
   engine = InnoDB
charset = UTF8MB4;

alter table finance_yijifu_log comment '易极付日志';

/*==============================================================*/
/* Index: idx_czlog_msgtype                                     */
/*==============================================================*/
create index idx_czlog_msgtype on finance_yijifu_log
(
   service_code
);

/*==============================================================*/
/* Index: idx_czlog_msgid                                       */
/*==============================================================*/
create index idx_czlog_msgid on finance_yijifu_log
(
   message_id
);

/*==============================================================*/
/* Table: oauth_client_details                                  */
/*==============================================================*/
create table oauth_client_details
(
   client_id            varchar(128) not null,
   resource_ids         varchar(255),
   client_secret        varchar(255),
   scope                varchar(255),
   authorized_grant_types varchar(255),
   web_server_redirect_uri varchar(255),
   authorities          varchar(255),
   access_token_validity bigint,
   refresh_token_validity bigint,
   additional_information text,
   autoapprove          varchar(255),
   primary key (client_id)
)
   engine = InnoDB
charset = UTF8MB4;

alter table oauth_client_details comment 'Oauth授权客户端表';

/*==============================================================*/
/* Table: oauth_client_token                                    */
/*==============================================================*/
create table oauth_client_token
(
   token_id             varchar(255),
   token                text,
   authentication_id    varchar(128) not null,
   user_name            varchar(255),
   client_id            varchar(128),
   primary key (authentication_id)
)
   engine = InnoDB
charset = UTF8MB4;

alter table oauth_client_token comment '客户端token表';

/*==============================================================*/
/* Index: idx_token_to_client                                   */
/*==============================================================*/
create index idx_token_to_client on oauth_client_token
(
   client_id
);

/*==============================================================*/
/* Table: sys_entity_audit_log                                  */
/*==============================================================*/
create table insurance_finance.sys_entity_audit_log
(
   id                   int(10) unsigned not null auto_increment,
   user_id              int(10) unsigned not null comment '审核用户id，关联sys_user表的主键',
   batch_number         varchar(32) not null comment '批次号，每次提交审核时生成',
   entity_type          varchar(32) not null comment '被审核的实体类型',
   entity_id            int(10) unsigned not null comment '被审核的实体id',
   audit_status         varchar(16) not null comment '审核状态',
   audit_time           datetime comment '审核时间',
   audit_remark         text comment '备注',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   created_by           int(10) unsigned not null,
   updated_by           int(10) unsigned,
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.sys_entity_audit_log comment '审核记录表';

/*==============================================================*/
/* Index: idx_sys_entity_auditor                                */
/*==============================================================*/
create index idx_sys_entity_auditor on insurance_finance.sys_entity_audit_log
(
   user_id
);

/*==============================================================*/
/* Index: idx_sys_entity_audit_target                           */
/*==============================================================*/
create index idx_sys_entity_audit_target on insurance_finance.sys_entity_audit_log
(
   entity_type,
   entity_id
);

/*==============================================================*/
/* Table: sys_entity_operation_log                              */
/*==============================================================*/
create table insurance_finance.sys_entity_operation_log
(
   id                   int(10) not null auto_increment,
   user_id              int(10) unsigned not null,
   entity_type          varchar(32) not null comment '订单类型',
   entity_id            int(10) unsigned not null comment '订单号',
   operation_type       varchar(32) not null comment '操作类型',
   operation_remark     text comment '操作备注',
   attachments          text comment '附件，以json数组方式存储',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   created_by           int(10) unsigned not null comment '创建者',
   updated_by           int(10) unsigned comment '最近更新者',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.sys_entity_operation_log comment '业务操作日志表';

/*==============================================================*/
/* Index: idx_entity_operation_number                           */
/*==============================================================*/
create index idx_entity_operation_number on insurance_finance.sys_entity_operation_log
(
   entity_type,
   entity_id
);

/*==============================================================*/
/* Index: idx_entity_operation_type                             */
/*==============================================================*/
create index idx_entity_operation_type on insurance_finance.sys_entity_operation_log
(
   operation_type
);

/*==============================================================*/
/* Table: sys_permission                                        */
/*==============================================================*/
create table insurance_finance.sys_permission
(
   id                   int(10) unsigned not null auto_increment,
   permission_code      varchar(32) not null comment '权限编码，全局唯一，创建时自动生成，推荐使用拼音首字母拼接',
   permission_name      varchar(64) not null comment '权限名称',
   remark               text comment '备注',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   created_by           int(10) unsigned not null comment '创建者',
   updated_by           int(10) unsigned comment '最近更新者',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.sys_permission comment '权限表';

/*==============================================================*/
/* Index: uq_sys_permission_code                                */
/*==============================================================*/
create unique index uq_sys_permission_code on insurance_finance.sys_permission
(
   permission_code
);

/*==============================================================*/
/* Table: sys_role                                              */
/*==============================================================*/
create table insurance_finance.sys_role
(
   id                   int(10) unsigned not null auto_increment,
   role_code            varchar(32) not null comment '角色编码，创建时自动生成，推荐使用拼音首字母拼接',
   role_name            varchar(64) not null comment '角色名称',
   remark               text comment '备注',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   created_by           int(10) unsigned not null comment '创建者',
   updated_by           int(10) unsigned comment '最近更新者',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.sys_role comment '角色表';

/*==============================================================*/
/* Index: uq_sys_role_code                                      */
/*==============================================================*/
create unique index uq_sys_role_code on insurance_finance.sys_role
(
   role_code
);

/*==============================================================*/
/* Table: sys_role_permission                                   */
/*==============================================================*/
create table insurance_finance.sys_role_permission
(
   id                   int(10) unsigned not null auto_increment,
   role_id              int(10) unsigned not null comment '角色id，关联sys_role表的主键',
   permission_id        int(10) unsigned not null comment '权限id，关联sys_permission表中的主键',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.sys_role_permission comment '角色权限映射表';

/*==============================================================*/
/* Index: uq_role_permission_rel                                */
/*==============================================================*/
create unique index uq_role_permission_rel on insurance_finance.sys_role_permission
(
   role_id,
   permission_id
);

/*==============================================================*/
/* Table: sys_user                                              */
/*==============================================================*/
create table insurance_finance.sys_user
(
   id                   int(10) unsigned not null auto_increment,
   mobile_phone         varchar(15) not null comment '用户手机号码，全局唯一',
   password             varchar(128) comment '用户密码，在后台创建的内部用户不能为空，但对于渠道的自有人员则可以为空',
   user_name            varchar(64) comment '用户的名称',
   is_locked            boolean not null default 0 comment '是否已经被锁定，被锁定后将不能登录系统开展业务',
   user_type            varchar(32) not null comment '用户类型，分为渠道(channel)和员工(staff)',
   channel_code         varchar(32) comment '当用户类型为channel时该字段不为空，表示该用户所属的渠道',
   organization_id      int(10) comment '当用户类型为staff时该字段不为空，表示该用户所在的公司或者分公司',
   is_channel_admin     boolean not null default 0 comment '表示该用户是否为渠道管理员',
   last_login_time      datetime comment '最近登录时间，创建时默认为NULL',
   last_login_ip        varchar(32) comment '最近登录来源IP',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   created_by           int(10) unsigned not null comment '创建者',
   updated_by           int(10) unsigned comment '最近更新者',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.sys_user comment '系统用户表';

/*==============================================================*/
/* Index: idx_sys_user_channel                                  */
/*==============================================================*/
create index idx_sys_user_channel on insurance_finance.sys_user
(
   channel_code
);

/*==============================================================*/
/* Index: idx_sys_user_data_organization                        */
/*==============================================================*/
create index idx_sys_user_data_organization on insurance_finance.sys_user
(
   organization_id
);

/*==============================================================*/
/* Index: uq_sys_user_mobile                                    */
/*==============================================================*/
create unique index uq_sys_user_mobile on insurance_finance.sys_user
(
   mobile_phone
);

/*==============================================================*/
/* Table: sys_user_oauth                                        */
/*==============================================================*/
create table insurance_finance.sys_user_oauth
(
   id                   int(10) unsigned not null auto_increment,
   user_id              int(10) unsigned not null comment '用户id，关联sys_user表的主键',
   oauth_type           varchar(32) not null comment '授权类型，微信接入有三种类型：wechat(微信内授权)、wechat_app(App微信授权)、wechat_pc(微信PC端授权)，根据以后的接入还可以增加weibo(微博授权)等',
   oauth_app_id         varchar(64) not null comment '授权的的第三方应用id',
   oauth_account        varchar(64) not null comment '授权后获取的用户的第三方帐号id',
   nick_name            varchar(32) comment '第三方帐号中的昵称',
   gener                varchar(16) comment '第三方帐号信息中的性别',
   header_image         tinytext comment '第三方帐号信息中的头像',
   wx_unionid           varchar(64) comment '针对微信多公众号关联的同一用户身份识别字段',
   oauth_content        text comment '授权信息完整内容',
   created_at           datetime not null comment '创建时间',
   updated_at           timestamp not null comment '更新时间',
   created_by           int(10) unsigned not null comment '创建者',
   updated_by           int(10) unsigned comment '最近更新者',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.sys_user_oauth comment '系统用户授权信息表';

/*==============================================================*/
/* Index: uq_sys_user_oauth                                     */
/*==============================================================*/
create unique index uq_sys_user_oauth on insurance_finance.sys_user_oauth
(
   user_id,
   oauth_app_id,
   oauth_type
);

/*==============================================================*/
/* Table: sys_user_role                                         */
/*==============================================================*/
create table insurance_finance.sys_user_role
(
   id                   int(10) unsigned not null auto_increment,
   user_id              int(10) unsigned not null comment '用户id，关联sys_user表主键',
   role_id              int(10) unsigned not null comment '角色id，关联sys_role表的主键',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.sys_user_role comment '用户角色映射表';

/*==============================================================*/
/* Index: uq_sys_user_role_rel                                  */
/*==============================================================*/
create unique index uq_sys_user_role_rel on insurance_finance.sys_user_role
(
   user_id,
   role_id
);

/*==============================================================*/
/* Table: timer_log                                             */
/*==============================================================*/
create table insurance_finance.timer_log
(
   id                   int(10) unsigned not null auto_increment,
   name                 varchar(64) not null comment '任务名称',
   status               varchar(32) not null comment '任务状态',
   start_time           datetime comment '任务执行开始时间',
   end_time             datetime comment '任务执行结束时间',
   description          varchar(256) comment '任务描述',
   error                text comment '错误堆栈或者错误信息',
   primary key (id)
)
   engine = InnoDB charset = UTF8MB4;

alter table insurance_finance.timer_log comment '定时器日志';

/*==============================================================*/
/* Index: idx_timer_name                                        */
/*==============================================================*/
create index idx_timer_name on insurance_finance.timer_log
(
   name
);

alter table insurance_finance.busi_contract add constraint fk_busi_contract_channel foreign key (channel_id)
references insurance_finance.busi_channel (id) on delete restrict on update restrict;

alter table insurance_finance.busi_contract add constraint fk_busi_contract_order foreign key (requisition_id)
references insurance_finance.busi_requisition (id) on delete restrict on update restrict;

alter table insurance_finance.busi_product_channel add constraint fk_prod_channel_info foreign key (channel_id)
references insurance_finance.busi_channel (id) on delete restrict on update restrict;

alter table insurance_finance.busi_product_channel add constraint fk_prod_channel_prod_info foreign key (product_id)
references insurance_finance.busi_product (id) on delete restrict on update restrict;

alter table busi_product_rate add constraint FK_fk_rate_to_product foreign key (product_id)
references insurance_finance.busi_product (id) on delete restrict on update restrict;

alter table insurance_finance.busi_requisition add constraint fk_busi_order_to_channel foreign key (channel_id)
references insurance_finance.busi_channel (id) on delete restrict on update restrict;

alter table insurance_finance.busi_requisition add constraint fk_busi_order_to_product foreign key (product_id)
references insurance_finance.busi_product (id) on delete restrict on update restrict;

alter table insurance_finance.busi_requisition_detail add constraint fk_requisition_detail_contract foreign key (business_contract_id)
references insurance_finance.busi_contract (id) on delete restrict on update restrict;

alter table insurance_finance.busi_requisition_detail add constraint FK_fk_requisition_detail_master foreign key (requisition_id)
references insurance_finance.busi_requisition (id) on delete restrict on update restrict;

alter table insurance_finance.busi_requisition_detail_resource add constraint fk_busi_order_detail_resource_to_detail foreign key (requisition_detail_id)
references insurance_finance.busi_requisition_detail (id) on delete cascade;

alter table insurance_finance.cust_account_info add constraint fk_customer_info_acct foreign key (account_id)
references insurance_finance.cust_account (id) on delete cascade on update restrict;

alter table insurance_finance.cust_account_oauth add constraint fk_customer_account_oauth foreign key (account_id)
references insurance_finance.cust_account (id) on delete restrict on update restrict;

alter table insurance_finance.cust_bank_card add constraint fk_bankcard_to_cust foreign key (account_id)
references insurance_finance.cust_account (id) on delete restrict on update restrict;

alter table insurance_finance.data_advertisement add constraint fk_advertisement_to_position foreign key (position_id)
references insurance_finance.data_advertisement_position (id) on delete restrict on update restrict;

alter table insurance_finance.data_insurance_branch add constraint fk_insurance_branch_company foreign key (company_id)
references insurance_finance.data_insurance_company (id) on delete restrict on update restrict;

alter table insurance_finance.data_organization add constraint fk_organization_parent foreign key (parent_id)
references insurance_finance.data_organization (id) on delete restrict on update restrict;

alter table insurance_finance.finance_account_voucher add constraint fk_acct_voucher_payment foreign key (payment_order_id)
references insurance_finance.finance_payment_order (id) on delete restrict on update restrict;

alter table insurance_finance.finance_account_voucher add constraint fk_acct_voucher_repayment foreign key (repayment_record_id)
references insurance_finance.finance_repayment_record (id) on delete restrict on update restrict;

alter table insurance_finance.finance_payment_order_detail add constraint fk_cust_payment_order_detail foreign key (order_id)
references insurance_finance.finance_payment_order (id) on delete cascade on update restrict;

alter table insurance_finance.finance_repayment_record add constraint fk_repayment_record_plan foreign key (repayment_plan_id)
references insurance_finance.finance_repayment_plan (id) on delete restrict on update restrict;

alter table insurance_finance.sys_entity_audit_log add constraint fk_sys_order_auditor foreign key (user_id)
references insurance_finance.sys_user (id) on delete restrict on update restrict;

alter table insurance_finance.sys_entity_operation_log add constraint fk_order_operation_user foreign key (user_id)
references insurance_finance.sys_user (id) on delete restrict on update restrict;

alter table insurance_finance.sys_role_permission add constraint fk_sys_role_permission_perm foreign key (permission_id)
references insurance_finance.sys_permission (id) on delete restrict on update restrict;

alter table insurance_finance.sys_role_permission add constraint fk_sys_role_permission_role foreign key (role_id)
references insurance_finance.sys_role (id) on delete restrict on update restrict;

alter table insurance_finance.sys_user_oauth add constraint fk_sys_user_oauth foreign key (user_id)
references insurance_finance.sys_user (id) on delete restrict on update restrict;

alter table insurance_finance.sys_user_role add constraint fk_relation_to_role foreign key (role_id)
references insurance_finance.sys_role (id) on delete restrict on update restrict;

alter table insurance_finance.sys_user_role add constraint fk_role_to_user foreign key (user_id)
references insurance_finance.sys_user (id) on delete restrict on update restrict;

