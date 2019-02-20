CREATE TABLE pay_http_log (
  log_id int(11) NOT NULL AUTO_INCREMENT,

  payment_id int(11) NOT NULL COMMENT '支付指令id payment_id',
  channel_id int(11) NOT NULL COMMENT '支付通道ID',
  pay_action tinyint(4) NOT NULL COMMENT '支付步骤：1 支付，2 查询',

  request_message text COMMENT '请求报文',
  request_status tinyint(4) DEFAULT '1' COMMENT '请求状态。1请求中；2请求返回',
  response_message text COMMENT '应答报文',

  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (log_id),
  UNIQUE KEY httpUnique (payment_id, channel_id, pay_action) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='http请求流水表';