/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3306
 Source Schema         : drop

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 31/05/2024 12:47:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
                             `id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '部门id',
                             `pid` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '上级部门id',
                             `dept_code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '部门编码',
                             `dept_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '部门名称',
                             `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
                             `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                             `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人id',
                             `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                             `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '系统部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
                             `id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键id',
                             `type` bit(1) NULL DEFAULT b'1' COMMENT '0-字典项/1-字典',
                             `pid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '字典项的父字典id',
                             `dict_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '字典名称',
                             `dict_code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '字典编码，字典项没有',
                             `item_value` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '字典项值',
                             `item_text` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '字典项文字',
                             `sort` int NULL DEFAULT 0 COMMENT '排序',
                             `dict_desc` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '字典描述',
                             `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                             `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人id',
                             `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                             `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `idx_uk_pid_dictNo`(`pid` ASC, `item_value` ASC) USING BTREE,
                             UNIQUE INDEX `idx_uk_dictCode`(`dict_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
                                   `id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限id',
                                   `pid` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '父权限id',
                                   `type` tinyint(1) NULL DEFAULT NULL COMMENT '资源类型,0-按钮，1-菜单',
                                   `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '资源名称，展示在页面上',
                                   `code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '资源编码，区分唯一的资源',
                                   `router_path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '组件路由地址，type=1该字段才有意义',
                                   `router_component` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '该路由对应的文件路径，type=1该字段才有意义',
                                   `visible` bit(1) NULL DEFAULT b'1' COMMENT '是否可见',
                                   `icon` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '组件图标',
                                   `redirect` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '重定向地址',
                                   `sort` int NULL DEFAULT 0 COMMENT '排序',
                                   `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
                                   `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                   `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人id',
                                   `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                                   `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1', '0', 1, 'RABC', 'RABC', '/rabc', '/layout/index.vue', b'1', 'Lock', NULL, 0, '基于用户-角色-权限的一系列管理', NULL, NULL, '2023-06-22 22:04:38', '测试更新人');
INSERT INTO `sys_permission` VALUES ('1795733840586289154', '5', 1, '缓存管理', 'CACHE', '/tools/cache', '/views/tools/cache/index.vue', b'1', 'Tools', NULL, 1, '233', '2024-05-29 16:28:05', '测试添加人', '2024-05-29 16:28:05', '测试更新人');
INSERT INTO `sys_permission` VALUES ('2', '1', 1, '用户管理', 'User', '/rabc/user', '/view/rabc/user/index.vue', b'1', 'User', NULL, 0, NULL, NULL, NULL, '2024-05-29 10:58:26', '测试更新人');
INSERT INTO `sys_permission` VALUES ('3', '1', 1, '角色管理', 'Role', '/rabc/role', '/view/rabc/role/index.vue', b'1', 'UserFilled', NULL, 0, NULL, NULL, NULL, '2024-05-29 10:58:39', '测试更新人');
INSERT INTO `sys_permission` VALUES ('4', '1', 1, '资源管理', 'Permission', '/rabc/permission', '/view/rabc/permission/index.vue', b'1', 'School', NULL, 0, NULL, NULL, NULL, '2024-05-29 10:58:45', '测试更新人');
INSERT INTO `sys_permission` VALUES ('5', '0', 1, '工具', 'Tools', '/tools', '/layout/index.vue', b'1', 'Tools', NULL, 0, NULL, NULL, NULL, '2023-06-22 22:39:03', '测试更新人');
INSERT INTO `sys_permission` VALUES ('6', '5', 1, '数据字典', 'Dict', '/tools/dict', '/view/tool/dict/index.vue', b'1', 'Tools', NULL, 2, NULL, NULL, NULL, '2023-06-05 20:48:13', '测试更新人');
INSERT INTO `sys_permission` VALUES ('7', '5', 1, '数据库管理', 'Db', '/tools/db', '/view/tool/db/index.vue', b'1', 'Tools', NULL, 3, '数据库在线管理', '2023-10-21 14:52:24', '测试添加人', '2023-06-05 20:48:13', '测试更新人');
INSERT INTO `sys_permission` VALUES ('8', '5', 1, '接口文档', 'Doc', '/tools/doc', '/view/tool/doc/index.vue', b'1', 'Tools', NULL, 4, NULL, '2023-10-21 14:52:24', '测试添加人', '2023-06-05 20:48:13', '测试更新人');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色id',
                             `role_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色名称',
                             `role_code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色编码',
                             `role_status` bit(1) NULL DEFAULT b'1' COMMENT '角色状态',
                             `role_remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色备注',
                             `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                             `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人id',
                             `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                             `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1659801087510994946', '管理员', 'admin', b'1', '一个小小的超级管理员，理论上拥有所有权限', '2023-05-20 13:59:52', '测试添加人', '2023-05-20 14:06:09', '测试更新人');
INSERT INTO `sys_role` VALUES ('1712856568404701186', '测试员', 'cs', b'0', '测试角色\n', '2023-10-13 23:43:24', '测试添加人', '2023-10-13 23:43:24', '测试更新人');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
                                        `id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键id',
                                        `role_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色id',
                                        `permission_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '权限id',
                                        `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                        `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人id',
                                        `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                                        `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色权限关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('1795733886228705282', '1659801087510994946', '1', '2024-05-29 16:28:16', '测试添加人', '2024-05-29 16:28:16', '测试更新人');
INSERT INTO `sys_role_permission` VALUES ('1795733886228705283', '1659801087510994946', '2', '2024-05-29 16:28:16', '测试添加人', '2024-05-29 16:28:16', '测试更新人');
INSERT INTO `sys_role_permission` VALUES ('1795733886295814145', '1659801087510994946', '3', '2024-05-29 16:28:16', '测试添加人', '2024-05-29 16:28:16', '测试更新人');
INSERT INTO `sys_role_permission` VALUES ('1795733886295814146', '1659801087510994946', '4', '2024-05-29 16:28:16', '测试添加人', '2024-05-29 16:28:16', '测试更新人');
INSERT INTO `sys_role_permission` VALUES ('1795733886295814147', '1659801087510994946', '5', '2024-05-29 16:28:16', '测试添加人', '2024-05-29 16:28:16', '测试更新人');
INSERT INTO `sys_role_permission` VALUES ('1795733886358728705', '1659801087510994946', '1795733840586289154', '2024-05-29 16:28:16', '测试添加人', '2024-05-29 16:28:16', '测试更新人');
INSERT INTO `sys_role_permission` VALUES ('1795733886358728706', '1659801087510994946', '6', '2024-05-29 16:28:16', '测试添加人', '2024-05-29 16:28:16', '测试更新人');
INSERT INTO `sys_role_permission` VALUES ('1795733886425837570', '1659801087510994946', '7', '2024-05-29 16:28:16', '测试添加人', '2024-05-29 16:28:16', '测试更新人');
INSERT INTO `sys_role_permission` VALUES ('1795733886883016705', '1659801087510994946', '8', '2024-05-29 16:28:16', '测试添加人', '2024-05-29 16:28:16', '测试更新人');

-- ----------------------------
-- Table structure for sys_table
-- ----------------------------
DROP TABLE IF EXISTS `sys_table`;
CREATE TABLE `sys_table`  (
                              `id` int NOT NULL COMMENT '表格id',
                              `table_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '表格名称',
                              `table_code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '表格所属资源',
                              `table_item_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'text' COMMENT '表格列类型',
                              `table_item_title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '表格列标题',
                              `table_item_prop` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '表格列绑定字段',
                              `table_item_width` double NULL DEFAULT NULL COMMENT '表格列宽度',
                              `table_item_overflow` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '当表格列内容过长被隐藏时是否显示tooltip',
                              `table_item_callback` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '处理当前列数据的回调函数',
                              `table_item_slot_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '插槽名称',
                              `table_item_fixed` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '表格列fixed属性',
                              `table_item_tag_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'tag类型',
                              `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                              `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人id',
                              `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                              `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '系统table表格结构' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_table
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                             `id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户id',
                             `user_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
                             `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户密码',
                             `real_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
                             `avatar` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '用户头像',
                             `phone` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电话',
                             `email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                             `status` tinyint NULL DEFAULT NULL COMMENT '状态',
                             `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
                             `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                             `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人id',
                             `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                             `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '系统用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '1', '杨桐', '\r\nhttp://localhost:8888/raindrop/sys/file/download?fileId=2024-04-13%234k%20%E9%95%BF%E8%BE%AB%E5%AD%90%E5%A5%B3%E5%AD%A9%20%E8%80%B3%E7%8E%AF%20%E4%BE%A7%E8%84%B8%20%E5%94%AF%E7%BE%8E%20%E6%A2%85%E8%8A%B1%20%E7%BA%A2%E8%89%B2%E8%83%8C%E5%BD%B1%20%E5%8E%9A%E6%B6%82%E5%8A%A8%E6%BC%AB%20%E9%AB%98%E6%B8%85%E5%A3%81%E7%BA%B8_%E5%BD%BC%E5%B2%B8%E5%9B%BE%E7%BD%91.jpg', '17784722008', 'yt220600@gmail.com', 1, '这是一个小小的管理员!!!', '2023-05-23 22:14:21', '测试添加人', '2023-10-22 14:33:08', '测试更新人');

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept`  (
                                  `id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键id',
                                  `user_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户id',
                                  `dept_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '部门id',
                                  `leader` bit(1) NULL DEFAULT NULL COMMENT '是否部门负责人',
                                  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人id',
                                  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                                  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '系统用户-部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
                                  `id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键id',
                                  `user_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户id',
                                  `role_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色id',
                                  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人id',
                                  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                                  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户角色关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1665631294444744706', '1', '1659801087510994946', '2023-06-05 16:07:01', '测试添加人', '2023-06-05 16:07:01', '测试更新人');

SET FOREIGN_KEY_CHECKS = 1;
