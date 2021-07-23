--system_config
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (1, 1, 'FTP_PLT', 'IP', '10.1.89.72');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (2, 1, 'FTP_PLT', 'PORT', '21');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (3, 1, 'FTP_PLT', 'PATH', '/PMS_LOG/');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (4, 1, 'FTP_PLT', 'PATH_BACKUP', '/PMS_LOG_BACKUP/');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (5, 1, 'FTP_PLT', 'ACCOUNT', 'burn');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (6, 1, 'FTP_PLT', 'PASSWORD', 'burn');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (7, 2, 'E_MAIL_PROXY', '帳號', '123MES@gmail.com');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (8, 2, 'E_MAIL_PROXY', '密碼', '123MES');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (9, 2, 'E_MAIL_PROXY', '協定', 'POST');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (10, 3, 'FTP_DATA_BKUP', 'IP', '10.1.90.10');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (11, 3, 'FTP_DATA_BKUP', 'FTP_PORT', '21');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (12, 3, 'FTP_DATA_BKUP', 'ACCOUNT', 'pm_bom_server');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (13, 3, 'FTP_DATA_BKUP', 'PASSWORD', '2fIlHs');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (14, 3, 'FTP_DATA_BKUP', 'PATH', '/PM_BOM_DBBackup/');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (15, 4, 'DATA_BKUP', 'FOLDER_NAME', '\WebAppBackupDatabase\');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (16, 4, 'DATA_BKUP', 'FILE_NAME', 'dtrimes_backup');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (17, 4, 'DATA_BKUP', 'PG_DUMP', 'C:\Program Files\PostgreSQL\10\bin\pg_dump.exe');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (18, 4, 'DATA_BKUP', 'DB_NAME', 'postgres://dbadmin:12345@localhost/dtrimes');
INSERT INTO system_config(sc_id, sc_g_id, sc_g_name, sc_name, sc_value)VALUES (19, 4, 'DATA_BKUP', 'DB_PORT', '5432');


SELECT setval('public.system_config_seq', 19, true);
DROP sequence IF EXISTS SYSTEM_CONFIG_G_SEQ CASCADE;
create sequence SYSTEM_CONFIG_G_SEQ start with 5 increment by 1;

--system_permission
--無權限
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control,sys_header)VALUES (1, 0,0, '無', '0000000000', 0, 'Title_Group', '',true);
--系統管理
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sys_status, sp_name, sp_control)VALUES (2, 1,1, '系統管理', '0001001111', 1001, 2, '設定-參數設定', 'system_config.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sys_status, sp_name, sp_control)VALUES (3, 1,1, '系統管理', '0001001111', 1002, 2, '設定-單元管理', 'system_permission.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (4, 1,1, '系統管理', '0001001111', 1003, '設定-群組管理', 'system_group.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (5, 1,1, '系統管理', '0001001111', 1004, '設定-帳號管理', 'system_user.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (6, 1,0, '系統管理', '0001001111', 1005, '通用-人員待命', 'system_standby.basil');
--個人功能
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (7, 2,1, '個人設定', '0001001101', 1101, '設定-帳號參數', 'own_config.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (8, 2,0, '個人設定', '0001001101', 1102, '通用-帳號資料', 'own_user.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (9, 2,0, '個人設定', '0001000001', 1103, '通用-首頁', 'index.basil');
--產品製程
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (10, 3,1, '產品製程', '0001001101', 1201, '設定-產品SN-規則', 'production_sn.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (11, 3,1, '產品製程', '0001001101', 1202, '設定-料件SN-類型', 'production_config.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (12, 3,0, '產品製程', '0001001101', 1203, '通用-料件SN-關聯紀錄', 'production_body.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (13, 3,0, '產品製程', '0001001101', 1204, '通用-製令單', 'production_header.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (14, 3,0, '產品製程', '0001001101', 1205, '通用-製令單-規格', 'production_records.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (15, 3,0, '產品製程', '0001001111', 1206, '通用-製令單-即時進度', 'production_schedule.basil');
--工作站
--遺棄--INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (17, 4,1, '工作站', '0001001101', 1301, '設定-料件SN類型', 'workstation_item.basil');

INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (16, 4,1, '工作站', '0001001101', 1302, '設定-工作站-名稱設定', 'workstation_config.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (18, 4,1, '工作站', '0001001101', 1303, '設定-工作站-綁定-SN', 'workstation.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (19, 4,1, '工作站', '0001001101', 1304, '設定-工作站-流程管理', 'workstation_program.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (20, 4,0, '工作站', '0001001101', 1305, '通用-工作站', 'workstation_work.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (21, 4,0, '工作站', '0001001101', 1306, '通用-SN補單', 'workstation_snadd.basil');
--工時績效
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (23, 5,1, '工作紀錄', '0001001101', 1401, '設定-作業類型', 'work_type.basil');
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (22, 5,0, '工作紀錄', '0001001101', 1402, '通用-工時登記', 'work_hours.basil');


--維修區
INSERT INTO system_permission(sp_id, sp_g_id, sp_type, sp_g_name, sp_permission, sys_sort, sp_name, sp_control)VALUES (24, 6,1, '產品維修', '0001001101', 1501, '設定-不良代碼', 'maintain_code.basil');

SELECT setval('public.system_permission_seq', 24, true);
DROP sequence IF EXISTS SYSTEM_PERMISSION_G_SEQ CASCADE;
create sequence SYSTEM_PERMISSION_G_SEQ start with 7 increment by 1;

--system_group(sg_permission[特殊3(512),特殊2(256),特殊1(128),訪問(64),下載(32),上傳(16),新增(8),修改(4),刪除(2),查詢(1)])
----admin
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort,sys_header) VALUES (1,1, '系統管理者', '0000000000', 1,0,true);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (2,1, '系統管理者', '1111111111', 2,1001);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (3,1, '系統管理者', '1111111111', 3,1002);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (4,1, '系統管理者', '0001111111', 4,1003);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (5,1, '系統管理者', '1111111111', 5,1004);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (6,1, '系統管理者', '1111111111', 6,1005);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (7,1, '系統管理者', '1111111111', 7,1006);

INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (8,1, '系統管理者', '1111111111', 8,1101);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (9,1, '系統管理者', '1111111111', 9,1102);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (10,1, '系統管理者', '1111111111', 10,1103);

INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (11,1, '系統管理者', '1111111111', 11,1201);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (12,1, '系統管理者', '1111111111', 12,1202);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (13,1, '系統管理者', '1111111111', 13,1203);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (14,1, '系統管理者', '1111111111', 14,1204);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (15,1, '系統管理者', '1111111111', 15,1205);

INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (16,1, '系統管理者', '1111111111', 16,1301);
--INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (17,1, '系統管理者', '1111111111', 17,1302);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (18,1, '系統管理者', '1111111111', 18,1303);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (19,1, '系統管理者', '1111111111', 19,1304);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (20,1, '系統管理者', '1111111111', 20,1305);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (21,1, '系統管理者', '1111111111', 21,1306);

INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (22,1, '系統管理者', '1111111111', 22,1401);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (23,1, '系統管理者', '1111111111', 23,1402);

INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (24,1, '系統管理者', '1111111111', 24,1501);

----user
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort,sys_header) VALUES (25,2, '一般使用者', '0000000000', 1,0,true);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (26,2, '一般使用者', '0001000001', 8,1101);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (27,2, '一般使用者', '0001000001', 9,1102);
INSERT INTO system_group(sg_id, sg_g_id, sg_name, sg_permission, sg_sp_id,sys_sort) VALUES (28,2, '一般使用者', '0001000001', 10,1103);
SELECT setval('public.system_group_seq', 28, true);
DROP sequence IF EXISTS SYSTEM_GROUP_G_SEQ CASCADE;
create sequence SYSTEM_GROUP_G_SEQ start with 3 increment by 1;

--system_user
INSERT INTO system_user(su_id,su_account, su_e_name, su_email, su_name, su_password, su_position,su_sg_g_id) VALUES (1,'admin','Admin_en', 'admin@dtr.com', 'Admin', '$2a$10$2tt8kwMSweSbTY/Jx1T9HuWcmrHzy50fZuOJWm/XORjJEOtpIoUdy', '超級管理者',1 );
INSERT INTO system_user(su_id,su_account, su_e_name, su_email, su_name, su_password, su_position,su_sg_g_id) VALUES (2,'user','User_en', 'user@dtr.com', 'User', '$2a$10$2tt8kwMSweSbTY/Jx1T9HuWcmrHzy50fZuOJWm/XORjJEOtpIoUdy', '一般使用者',2);
SELECT setval('public.system_user_seq', 2, true);

----production_records

INSERT INTO production_records(pr_id, pr_bom_id, pr_c_from,pr_c_name,pr_s_sn, pr_e_sn, pr_order_id, pr_p_model,pr_p_quantity,pr_p_ok_quantity,pr_b_item, pr_s_item) VALUES ('A44654-A654',  '91-363-G100001', '測試-生產注意事項','MAYA(Isreal)', 'no_sn0000n000','no_sn0000n000','測試-訂單編號(OP-2021042001)', 'DT363GL',3,0, '{"CPU":i600,"RAM":"4G"}','{"M/B 版本":"R5.6.P","ECN":"D6B"}');
INSERT INTO production_records(pr_id, pr_bom_id, pr_c_from,pr_c_name,pr_s_sn, pr_e_sn, pr_order_id, pr_p_model,pr_p_quantity,pr_p_ok_quantity,pr_b_item, pr_s_item) VALUES ('A511-210204004',  '92-363-G100001', '測試-生產注意事項','MAYA(Isreal)', 'no_sn0000n000','no_sn0000n000','測試-訂單編號(OP-2021042002)', 'DT363GL',7,0, '{"CPU":i700,"RAM":"4G"}','{"M/B 版本":"R5.7.P","ECN":"D6B"}');
INSERT INTO production_records(pr_id, pr_bom_id, pr_c_from,pr_c_name,pr_s_sn, pr_e_sn, pr_order_id, pr_p_model,pr_p_quantity,pr_p_ok_quantity,pr_b_item, pr_s_item) VALUES ('A513-123456799',  '93-363-G100001', '測試-生產注意事項','MAYA(Isreal)', 'A13W1CC140300','A13W1CC140306','測試-訂單編號(OP-2021042002)', 'DT363GL',7,0, '{"CPU":i800,"RAM":"4G"}','{"M/B 版本":"R5.8.P","ECN":"D6B"}');

--production_header
INSERT INTO production_header(ph_id,ph_pb_g_id,ph_type, ph_pr_id, ph_schedule, ph_wp_id, sys_header, sys_ver) VALUES (1,1,'A511_no_sn', 'A44654-A654', '', 1, true, 0);
INSERT INTO production_header(ph_id,ph_pb_g_id,ph_type, ph_pr_id, ph_schedule, ph_wp_id, sys_header, sys_ver) VALUES (2,1,'A511_no_sn', 'A511-210204004', '', 2, true, 0);
INSERT INTO production_header(ph_id,ph_pb_g_id,ph_type, ph_pr_id, ph_schedule, ph_wp_id, sys_header, sys_ver) VALUES (3,3,'A511_has_sn', 'A513-123456799', '', 1, true, 0);
SELECT setval('public.production_header_seq', 3, true);
DROP sequence IF EXISTS PRODUCTION_HEADER_G_SEQ CASCADE;
create sequence PRODUCTION_HEADER_G_SEQ start with 4 increment by 1;

----production_body
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_value01, pb_value02 ,pb_value03 ,pb_value04 ,pb_value05 ,pb_value06 ,pb_value07,pb_value08,pb_value09,pb_sn, sys_header,pb_w_name01,pb_w_name02,pb_w_name03,pb_w_name04,pb_w_name05,pb_w_name06,pb_w_name07,pb_w_name08) VALUES ( 0,0, 0,'MB(UUID)' ,'LAN1 MAC' ,'LAN2 MAC' ,'WIFI MAC' ,'NVRAM MAC','IMEI','ECN','EC','BIOS','', true,'PCB_processing','PCB_burnin','PCB_function_test','Assembly','Burnin','T1','T2','Package');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule) VALUES ( 1,1, 0, 'no_sn' , false, '');

INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 5,3, 0, 'A13W1CC140300' , false, '{"1":{"name":"PCB_processing","type":"N"},"5":{"name":"PCB_function_test","type":"N"}}','主版號21');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 6,3, 0, 'A13W1CC140301' , false, '{"1":{"name":"PCB_processing","type":"N"},"5":{"name":"PCB_function_test","type":"N"}}','主版號22');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01,pb_l_text) VALUES ( 7,3, 0, 'A13W1CC140302' , false, '{"1":{"name":"PCB_processing","type":"N"},"5":{"name":"PCB_function_test","type":"N"}}','主版號23','999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 8,3, 0, 'A13W1CC140303' , false, '{"1":{"name":"PCB_processing","type":"N"},"5":{"name":"PCB_function_test","type":"N"}}','主版號24');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 9,3, 0, 'A13W1CC140304' , false, '{"1":{"name":"PCB_processing","type":"N"},"5":{"name":"PCB_function_test","type":"N"}}','主版號25');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 10,3, 0, 'A13W1CC140305' , false, '{"1":{"name":"PCB_processing","type":"N"},"5":{"name":"PCB_function_test","type":"N"}}','主版號26');
INSERT INTO production_body(pb_id,pb_g_id, sys_ver, pb_sn, sys_header, pb_schedule,pb_value01) VALUES ( 11,3, 0, 'A13W1CC140306' , false, '{"1":{"name":"PCB_processing","type":"N"},"5":{"name":"PCB_function_test","type":"N"}}','主版號27');

SELECT setval('public.production_body_seq', 11, true);
DROP sequence IF EXISTS PRODUCTION_BODY_G_SEQ CASCADE;
create sequence PRODUCTION_BODY_G_SEQ start with 4 increment by 1;

--workstation_item 'MB(UUID)' ,'LAN1 MAC' ,'LAN2 MAC'
INSERT INTO workstation_item(wi_id,sys_header, wi_pb_cell, wi_pb_value)VALUES (0, true,'Group' , 'Group');
INSERT INTO workstation_item(wi_id,sys_header, wi_pb_cell, wi_pb_value)VALUES (1, false,'pb_value01' , 'MB(UUID)');
INSERT INTO workstation_item(wi_id,sys_header, wi_pb_cell, wi_pb_value)VALUES (2, false,'pb_value02' , 'LAN1 MAC');
INSERT INTO workstation_item(wi_id,sys_header, wi_pb_cell, wi_pb_value)VALUES (3, false,'pb_value03' , 'LAN2 MAC');
INSERT INTO workstation_item(wi_id,sys_header, wi_pb_cell, wi_pb_value)VALUES (4, false,'pb_value04' , 'WIFI MAC');
INSERT INTO workstation_item(wi_id,sys_header, wi_pb_cell, wi_pb_value)VALUES (5, false,'pb_value05' , 'NVRAM MAC');
INSERT INTO workstation_item(wi_id,sys_header, wi_pb_cell, wi_pb_value)VALUES (6, false,'pb_value06' , 'IMEI');
INSERT INTO workstation_item(wi_id,sys_header, wi_pb_cell, wi_pb_value)VALUES (7, false,'pb_value07' , 'ECN');
INSERT INTO workstation_item(wi_id,sys_header, wi_pb_cell, wi_pb_value)VALUES (8, false,'pb_value08' , 'EC');
INSERT INTO workstation_item(wi_id,sys_header, wi_pb_cell, wi_pb_value)VALUES (9, false,'pb_value09' , 'BIOS');
SELECT setval('public.workstation_item_seq', 9, true);

--workstation
INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (0, true, 'Group', 0, 'Group','','', 0, 0);

INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (1, true, 'BC088', 1, 'PCB_processing','pb_w_name01','系統管理者', 1, 0);
INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (2, false, 'BC088', 1, 'PCB_processing','pb_w_name01','系統管理者', 1, 1);
INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (3, false, 'BC088', 1, 'PCB_processing','pb_w_name01','系統管理者', 1, 2);
INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (4, false, 'BC088', 1, 'PCB_processing','pb_w_name01','系統管理者', 1, 3);

INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (5, true, 'BC099', 2, 'PCB_function_test','pb_w_name03','系統管理者', 1, 0);
INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (6, false, 'BC099', 2, 'PCB_function_test','pb_w_name03','系統管理者', 1, 1);
INSERT INTO workstation(w_id, sys_header, w_c_name, w_g_id, w_pb_name,w_pb_cell, w_sg_name,w_sg_id, w_i_id)VALUES (7, false, 'BC099', 2, 'PCB_function_test','pb_w_name03','系統管理者', 1, 2);
SELECT setval('public.workstation_seq', 7, true);
DROP sequence IF EXISTS WORKSTATION_G_SEQ CASCADE;
create sequence WORKSTATION_G_SEQ start with 3 increment by 1;

--workstation_program
INSERT INTO workstation_program(wp_id,wp_g_id,wp_w_g_id, wp_c_name, wp_name, sys_sort,sys_header) VALUES (1,1,0,'WP999', '測試-PCB+TEST站程序', 0,true);
INSERT INTO workstation_program(wp_id,wp_g_id,wp_w_g_id, wp_c_name, wp_name, sys_sort,sys_header) VALUES (2,1,1,'WP999', '測試-PCB+TEST站程序', 1,false);
INSERT INTO workstation_program(wp_id,wp_g_id,wp_w_g_id, wp_c_name, wp_name, sys_sort,sys_header) VALUES (3,1,2,'WP999', '測試-PCB+TEST站程序', 2,false);

INSERT INTO workstation_program(wp_id,wp_g_id,wp_w_g_id, wp_c_name, wp_name, sys_sort,sys_header) VALUES (4,2,0,'WP888', '測試-TEST站程序', 0,true);
INSERT INTO workstation_program(wp_id,wp_g_id,wp_w_g_id, wp_c_name , wp_name, sys_sort,sys_header) VALUES (5,2,1,'WP888', '測試-TEST站程序', 1,false);
SELECT setval('public.workstation_program_seq', 5, true);
DROP sequence IF EXISTS WORKSTATION_PROGRAM_G_SEQ CASCADE;
create sequence WORKSTATION_PROGRAM_G_SEQ start with 3 increment by 1;

--production_sn
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (1, 1, '機種別', '', '',true);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (2, 1, '機種別', 'NO(SN)', 'no_',false);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (3, 1, '機種別', '136BU', 'AAA',false);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (4, 2, '生產廠別', '', '',true);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (5, 2, '生產廠別', 'NO(SN)', 's',false);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (6, 2, '生產廠別', 'Beijing', 'B',false);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (7, 3, '保固期限', '', '',true);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (8, 3, '保固期限', 'NO(SN)', 'n',false);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (9, 3, '保固期限', '1_year', '1',false);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (10, 4, '生產年周', '', '',true);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (11, 4, '生產年周', '[YYWW]', '2118',false);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (12, 5, 'Panel廠商(面板)', '', '',true);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (13, 5, 'Panel廠商(面板)','NO(SN)', 'n',false);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (14, 5, 'Panel廠商(面板)','AGL', 'A',false);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (15, 6, '流水號', '', '',true);
INSERT INTO production_sn(ps_id, ps_g_id, ps_g_name, ps_name, ps_value,sys_header)VALUES (16, 6, '流水號', '[000]', '346',false);
SELECT setval('public.production_sn_seq', 16, true);
DROP sequence IF EXISTS PRODUCTION_SN_G_SEQ  CASCADE;
create sequence PRODUCTION_SN_G_SEQ start with 7 increment by 1;

--維修code
INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (1, 1, '電池', '', 'EC',true);
INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (2, 1, '電池', '冒煙', 'EC001',false);
INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (3, 1, '電池', '浸水', 'EC002',false);
INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (4, 1, '電池', '長香菇', 'EC003',false);

INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (5, 2, '螢幕', '', 'EA',true);
INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (6, 2, '螢幕', '不亮', 'EA001',false);
INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (7, 2, '螢幕', '很亮', 'EA002',false);
INSERT INTO maintain_code(mc_id, mc_g_id, mc_g_name, mc_name, mc_value,sys_header)VALUES (8, 2, '螢幕', '一下亮一下不亮', 'EA003',false);
SELECT setval('public.maintain_code_seq', 8, true);
DROP sequence IF EXISTS MAINTAIN_CODE_G_SEQ  CASCADE;
create sequence MAINTAIN_CODE_G_SEQ start with 3 increment by 1;

--工作類型
INSERT INTO work_type(wt_id,wt_name)VALUES (0,'Title_Group');
INSERT INTO work_type(wt_id,wt_name)VALUES (1,'前板組裝');
INSERT INTO work_type(wt_id,wt_name)VALUES (2,'後板組裝');
INSERT INTO work_type(wt_id,wt_name)VALUES (3,'其他');
INSERT INTO work_type(wt_id,wt_name)VALUES (4,'電池組裝');
SELECT setval('public.work_type_seq', 4, true);

--工時登記
INSERT INTO public.work_hours(wh_id, wh_account, wh_do, wh_nb, wh_pr_id, wh_wt_id,sys_header,sys_status)VALUES (1, '', '', 0, 'A44654-A654', 0,true,2);
INSERT INTO public.work_hours(wh_id, wh_account, wh_do, wh_nb, wh_pr_id, wh_wt_id,sys_status)VALUES (2, 'admin', '測1試做事', 1, 'A44654-A654', 1,2);
INSERT INTO public.work_hours(wh_id, wh_account, wh_do, wh_nb, wh_pr_id, wh_wt_id,sys_status)VALUES (3, 'admin', '測2試做事', 2, 'A44654-A654', 2,2);
INSERT INTO public.work_hours(wh_id, wh_account, wh_do, wh_nb, wh_pr_id, wh_wt_id,sys_status)VALUES (4, 'admin', '測3試做事', 2, 'A44654-A654', 3,2);

INSERT INTO public.work_hours(wh_id, wh_account, wh_do, wh_nb, wh_pr_id, wh_wt_id,sys_header,sys_status)VALUES (5, '', '', 0, 'A511-210204004', 0,true,2);
INSERT INTO public.work_hours(wh_id, wh_account, wh_do, wh_nb, wh_pr_id, wh_wt_id,sys_status)VALUES (6, 'admin', '測1試做事', 1, 'A511-210204004', 1,2);
INSERT INTO public.work_hours(wh_id, wh_account, wh_do, wh_nb, wh_pr_id, wh_wt_id,sys_status)VALUES (7, 'admin', '測2試做事', 2, 'A511-210204004', 2,2);
INSERT INTO public.work_hours(wh_id, wh_account, wh_do, wh_nb, wh_pr_id, wh_wt_id,sys_status)VALUES (8, 'admin', '測3試做事', 2, 'A511-210204004', 3,2);
SELECT setval('public.work_hours_seq', 8, true);


