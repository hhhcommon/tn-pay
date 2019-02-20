CREATE TABLE pay_wait_payment_record (
  payment_id int(11) NOT NULL AUTO_INCREMENT,

  -- 数据流标志
  call_flow varchar(150) NOT NULL COMMENT '唯一支付指令流水号',
  out_order_no varchar(50) NOT NULL COMMENT '外部进单号',
  apply_id int(11) NOT NULL COMMENT '内部申请单号',
  trans_no varchar(150) NOT NULL COMMENT '商户订单号_宝付',

  -- 确定支付管道
  scene_id int(11) NOT NULL COMMENT '场景ID',
  product_id int(11) NOT NULL COMMENT '产品ID',
  funds_id int(11) NOT NULL COMMENT '信托项目ID',
  provider_id int(11) NOT NULL COMMENT '资金来源ID',
  payment_mode tinyint(4) NOT NULL COMMENT '支付方式。0:信息查询，1:代付，2:代收，3:绑卡 协议支付',

  -- 支付管道
  channel_id int(11) NOT NULL COMMENT '支付通道ID',

  -- 用户信息
  account_name varchar(20) NOT NULL COMMENT '客户开户名称',
  cert_type varchar(3) DEFAULT '01' COMMENT '客户证件类型: 01身份证',
  cert_no varchar(25) DEFAULT NULL COMMENT '客户证件号码',
  mobile varchar(15) DEFAULT NULL COMMENT '客户手机号',
  cust_account varchar(50) NOT NULL COMMENT '客户银行卡号',
  bank_code varchar(10) DEFAULT NULL COMMENT '开户行编码',
  bank_name varchar(20) DEFAULT NULL COMMENT '银行名称',

  -- 宝付协议支付特性
  protocol_no varchar(50) DEFAULT NULL COMMENT '签约协议号',
  user_id varchar(50) DEFAULT NULL COMMENT '客户唯一标识',
  -- 兴业特性
  pub_orpri_flag tinyint(4) DEFAULT '1' COMMENT '1：对私 2：对公  兴业银行：0：对公 1：对私',

  -- 交易信息
  payment_amount decimal(9,2) DEFAULT '0.00' COMMENT '支付金额',
  payment_status tinyint(4) NOT NULL DEFAULT '0' COMMENT '支付状态。0 等待处理；1支付中；2支付成功；3支付失败',
  payment_msg varchar(255) DEFAULT NULL COMMENT '支付返回信息',

  -- 交易时间戳
  order_date datetime DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  payment_time datetime DEFAULT NULL COMMENT '支付成功时间',

  status tinyint(4) NOT NULL DEFAULT '1' COMMENT '作废标志 0作废 1正常',

  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (payment_id),
  UNIQUE KEY callFlow (call_flow) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='待支付记录表';