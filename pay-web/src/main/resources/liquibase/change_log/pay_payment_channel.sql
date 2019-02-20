CREATE TABLE pay_payment_channel (
  channel_id int(11) NOT NULL AUTO_INCREMENT,

  -- 管道索引
  scene_id int(11) NOT NULL COMMENT '场景ID',
  product_id int(11) NOT NULL COMMENT '产品ID',
  funds_id int(11) NOT NULL COMMENT '信托项目ID',
  provider_id int(11) NOT NULL COMMENT '资金来源ID',
  payment_mode tinyint(4) NOT NULL COMMENT '支付方式。0:信息查询，1:代付，2:代收，3:绑卡 协议支付',

  -- 管道
  pay_company_id int(11) NOT NULL COMMENT '第三方支付公司ID',
  pay_company_name varchar(50) NOT NULL COMMENT '第三方支付公司名称',

  -- 管道信息
  member_no varchar(50) NOT NULL COMMENT '商户号',
  member_key varchar(50) DEFAULT NULL COMMENT '商户注册key，余额功能',
  terminal_no varchar(50) NOT NULL COMMENT '终端号',
  pub_key_path varchar(255) DEFAULT NULL COMMENT '公钥路径',
  pri_key_path varchar(255) DEFAULT NULL COMMENT '私钥路径',
  pri_key_password varchar(50) DEFAULT NULL COMMENT '私钥密码',

  -- 管道地址版本
  data_type varchar(5) DEFAULT 'json' COMMENT '数据请求类型 json/xml',
  version varchar(10) NOT NULL COMMENT '接口版本',
  request_url varchar(255) NOT NULL COMMENT '接口地址',

  status tinyint(4) DEFAULT NULL COMMENT '状态，0废弃，1线上，2测试',
  PRIMARY KEY (channel_id),
  UNIQUE KEY channelUnique (scene_id,product_id,funds_id,provider_id,payment_mode,status) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付通道表';
