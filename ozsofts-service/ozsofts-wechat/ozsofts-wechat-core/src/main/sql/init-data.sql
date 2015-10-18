insert into t_wx_accounts 
(system_id, name, app_id, secret, token, encoding_aes_key, encrypt_type, account_type, is_auth, is_init, is_enabled)
values
('nyymh',    '南粤牡丹爱影会', 'wx0395a0935a06dd22', '4d998637034377aee375731b2dce2fac', 'nyymh',       'yC7ud7ATy8AyxUr1cQdFawKsICcffic3AFCL3F4Swnj', 'aes', 'DY', 1, 0, 0),
('ikdy',     '迈络爱影网',    'wx0855361da61e2483', 'abbb4165a583f4682d9868497e0188db', 'mobiritikdy', 'R8QRwuFqQ3AKaIHDwSwukIR8ZlDL6kFWckDWJSzHPP8', 'raw', 'DY', 0, 0, 0),
('ikdyserv', '我的爱影网',    'wx17b5c19e50d8519b', 'ae0f1795e2224e8f49b2189f4ebe8af1', 'serviceikdy', '9cHqvBXVPZXg0JfWMZNQjIztSu0j0jWHVQMNp7Jq3kw', 'aes', 'FW', 1, 0, 1);



insert into t_wx_resp_messages
(id, msg_type, content, account_id)
values
(1, 'text', '', 3)

create table t_wx_resp_messages (
	id bigint not null auto_increment,
	
	msg_type varchar(32) comment '消息类型', 

	content varchar(1024) default '' comment '针对文本信息,文本消息内容',

	media_id varchar(64) default '' comment '针对图片、视频以及语音消息媒体id',
	
	title varchar(512) default '' comment '针对音乐和视频消息,消息标题',
	description varchar(255) default '' comment '针对音乐和视频消息,消息描述',
	thumb_media_id varchar(64) default '' comment '针对音乐和视频消息,图片、语音消息媒体id',

	music_url varchar(255) default '' comment '针对音乐消息,消息链接',
	hq_music_url varchar(255) default '' comment '针对音乐消息,消息链接',
	
	account_id bigint not null comment '回复消息所属公众号标识', 
	primary key (id)
) ENGINE=InnoDB comment '微信公众号回复信息表';



insert into t_wx_rules
(id, rule_name, rule_type, rule_value, match_type, resp_messages_id, ref_type, ref_id, account_id)
values
(1, '我的爱影网关注回复', 'default', '', '', 1, '', null);


CREATE TABLE t_wx_rules (
	id bigint not null auto_increment,

	rule_name varchar(64) not null comment '规则名称，便于管理人员识别',
	
	rule_type varchar(32) not null comment '规则类型，default: 关注时回复；auto: 自动回复；keyword: 关键字回复; 其它类型可以由具体的引用而定', 
	rule_value varchar(128) default '' comment '活动规则定义，关键字',
	match_type varchar(32) default '' comment '匹配类型，regex: 正则；startsWith: 以关键字开头；endsWidth: 以关键字结束；contains: 包含关键字',

	resp_message_id bigint not null comment '具体的消息定义在回复消息表中',
	
	ref_type varchar(32) default '' comment '消息的关联引用，视后续扩展而定，系统级的保持空即可',
	ref_id bigint comment '当ref_type有数据时，这里表示实际引用的对象',

	account_id bigint not null comment '消息规则所属公众号标识', 
  	primary key (id)
) ENGINE=InnoDB comment '消息规则表，根据上行消息进行匹配然后形成返回的消息';


insert into t_wx_actevents 
(name, code, start_date, end_date, status, account_id)
values
('测试微信活动', '签到', '20151008', '20151231', 0, 3);
