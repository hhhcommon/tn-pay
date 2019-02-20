CREATE TABLE pay_bind_card_info (
  bind_id int(11) NOT NULL AUTO_INCREMENT,

  -- 确定支付管道
  scene_id int(11) NOT NULL COMMENT '场景ID',
  product_id int(11) NOT NULL COMMENT '产品ID',
  funds_id int(11) NOT NULL COMMENT '信托项目ID',
  provider_id int(11) NOT NULL COMMENT '资金来源ID',

  -- 绑卡通道
  channel_id int(11) NOT NULL COMMENT '支付通道ID',

  -- 用户卡信息
  user_id varchar(50) NOT NULL COMMENT '用户在系统中唯一id',
  bank_account varchar(50) NOT NULL COMMENT '银行卡号',
  user_name varchar(20) DEFAULT NULL COMMENT '客户姓名',
  cert_no varchar(25) DEFAULT NULL COMMENT '证件号码',
  mobile varchar(15) DEFAULT NULL COMMENT '客户手机',
  bank_code varchar(10) DEFAULT NULL COMMENT '开户行编码',
  bank_name varchar(20) DEFAULT NULL COMMENT '银行名称',

  cert_type varchar(3) DEFAULT '01' COMMENT '证件类型',
  bank_card_type varchar(3) DEFAULT '101' COMMENT '卡类型 101 借记卡|102 信用卡',
  bank_security_code varchar(3) DEFAULT NULL COMMENT '银行卡安全码 000[信用卡]',
  bank_valid_thru varchar(4) DEFAULT NULL COMMENT '银行卡有效期 yyMM[信用卡]',
  cert_valid_thru varchar(10) DEFAULT NULL COMMENT '证件有效期',

  -- 返回码
  unique_code varchar(255) DEFAULT NULL COMMENT '预签约唯一码 ',
  protocol_code varchar(255) DEFAULT NULL COMMENT '签约协议号',

  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  status tinyint(4) DEFAULT NULL COMMENT '状态：1 预绑卡中，2 绑卡成功，3 确认绑卡中 0 作废',
  PRIMARY KEY (bind_id),
  UNIQUE KEY bindUnique (channel_id, bank_account) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='绑卡信息表';