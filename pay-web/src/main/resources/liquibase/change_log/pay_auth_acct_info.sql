CREATE TABLE pay_auth_acct_info (
  auth_id int(11) NOT NULL AUTO_INCREMENT,
  call_flow varchar(150) NOT NULL COMMENT '唯一支付指令流水号',

  -- 确定支付管道
  scene_id int(11) NOT NULL COMMENT '场景ID',
  product_id int(11) NOT NULL COMMENT '产品ID',
  funds_id int(11) NOT NULL COMMENT '信托项目ID',
  provider_id int(11) NOT NULL COMMENT '资金来源ID',
  payment_mode tinyint(4) DEFAULT NULL COMMENT '支付方式。0:信息查询，1:代付，2:代收',

  -- 绑卡通道
  channel_id int(11) NOT NULL COMMENT '支付通道ID',

  -- 用户卡信息
  cert_type varchar(3) DEFAULT '01' COMMENT '证件类型',
  cert_no varchar(25) DEFAULT NULL COMMENT '证件号码',
  user_name varchar(20) DEFAULT NULL COMMENT '客户姓名',
  mobile varchar(15) DEFAULT NULL COMMENT '客户手机',
  bank_account varchar(50) NOT NULL COMMENT '银行卡号',
  bank_name varchar(20) DEFAULT NULL COMMENT '银行名称',
  bank_code varchar(10) DEFAULT NULL COMMENT '开户行编码',

  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  status tinyint(4) DEFAULT NULL COMMENT '状态：1 成功，0 失败',
  PRIMARY KEY (auth_id),
  UNIQUE KEY authUnique (channel_id, bank_account) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='绑卡信息表';