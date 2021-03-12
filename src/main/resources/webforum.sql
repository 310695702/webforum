/*
 Navicat Premium Data Transfer

 Source Server         : localhost3306
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : webforum

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 02/02/2021 12:21:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for android_version
-- ----------------------------
DROP TABLE IF EXISTS `android_version`;
CREATE TABLE `android_version`  (
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `version_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`version`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of android_version
-- ----------------------------
INSERT INTO `android_version` VALUES ('2', '1.3');

-- ----------------------------
-- Table structure for forum_category
-- ----------------------------
DROP TABLE IF EXISTS `forum_category`;
CREATE TABLE `forum_category`  (
  `category_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `category_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类图片',
  `is_recommend` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否为推荐分类0-不是 1-是',
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_category
-- ----------------------------
INSERT INTO `forum_category` VALUES (1, 'DNF', 'http://localhost:8088/categoryImages/3a2771cd-006c-4d92-b61a-66c127b2a52f.jpg', 0);
INSERT INTO `forum_category` VALUES (2, '原神', 'http://localhost:8088/categoryImages/5a929357-7d89-42eb-9b67-f1004b73fa83.jpg', 0);

-- ----------------------------
-- Table structure for forum_comment
-- ----------------------------
DROP TABLE IF EXISTS `forum_comment`;
CREATE TABLE `forum_comment`  (
  `comment_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '评论的id',
  `user_id` bigint(0) NOT NULL COMMENT '评论的人的id',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `parent_id` bigint(0) NOT NULL COMMENT '评论的帖子的id',
  `comment_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '评论时间',
  `visibility` int(0) NOT NULL DEFAULT 1 COMMENT '评论是否可见 1-可见 2-不可见',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_comment
-- ----------------------------
INSERT INTO `forum_comment` VALUES (1, 4, '1', 1, '2020-12-31 16:58:05', 1);
INSERT INTO `forum_comment` VALUES (2, 4, '你好呀。。', 28, '2021-02-02 10:56:10', 1);
INSERT INTO `forum_comment` VALUES (3, 4, '哈喽。。。', 28, '2021-02-02 10:59:29', 1);
INSERT INTO `forum_comment` VALUES (4, 4, '你在干嘛呢', 28, '2021-02-02 10:59:52', 1);
INSERT INTO `forum_comment` VALUES (5, 4, '你好呀。。', 28, '2021-02-02 11:00:07', 1);
INSERT INTO `forum_comment` VALUES (6, 4, '你好呀。。', 1, '2021-02-02 12:13:25', 1);

-- ----------------------------
-- Table structure for forum_comment_images
-- ----------------------------
DROP TABLE IF EXISTS `forum_comment_images`;
CREATE TABLE `forum_comment_images`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `comment_id` bigint(0) NOT NULL COMMENT '评论id',
  `comment_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_comment_images
-- ----------------------------

-- ----------------------------
-- Table structure for forum_msg_state
-- ----------------------------
DROP TABLE IF EXISTS `forum_msg_state`;
CREATE TABLE `forum_msg_state`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT ' 状态id',
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  `post_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '帖子id',
  `msg_state` int(0) NOT NULL COMMENT '消息状态0-删除 1-已读 2-未读',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_msg_state
-- ----------------------------

-- ----------------------------
-- Table structure for forum_post
-- ----------------------------
DROP TABLE IF EXISTS `forum_post`;
CREATE TABLE `forum_post`  (
  `post_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '帖子id',
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '帖子标题',
  `content` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '帖子内容',
  `category_id` bigint(0) NOT NULL COMMENT '帖子分类',
  `user_id` bigint(0) NOT NULL COMMENT '发帖人',
  `visibility` int(0) NOT NULL DEFAULT 1 COMMENT '帖子是否可见 1-可见 2-不可见',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '帖子创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '帖子更新时间',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_post
-- ----------------------------
INSERT INTO `forum_post` VALUES (1, '测试', '测试发帖', 1, 1, 1, '2020-12-26 22:02:22', '2021-01-09 16:37:07');
INSERT INTO `forum_post` VALUES (2, '测试2', '测试发帖', 1, 1, 1, '2020-12-26 22:02:53', '2020-12-26 22:02:53');
INSERT INTO `forum_post` VALUES (3, '测试3', '测试内容3', 1, 3, 1, '2020-12-26 22:31:40', '2020-12-26 22:31:39');
INSERT INTO `forum_post` VALUES (5, 'DNF', '今天也是充满希望的一天', 1, 1, 1, '2021-01-09 11:06:21', '2021-01-09 11:06:20');
INSERT INTO `forum_post` VALUES (6, '测试01', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:32:01', '2021-01-09 14:32:01');
INSERT INTO `forum_post` VALUES (7, '测试02', 'asjkdnasda', 1, 4, 2, '2021-01-09 14:32:06', '2021-01-09 14:32:06');
INSERT INTO `forum_post` VALUES (8, '测试03', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:32:15', '2021-01-09 14:32:14');
INSERT INTO `forum_post` VALUES (9, '测试04', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:32:22', '2021-01-09 14:32:21');
INSERT INTO `forum_post` VALUES (10, '测试05', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:32:25', '2021-01-09 14:32:25');
INSERT INTO `forum_post` VALUES (11, '测试06', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:32:29', '2021-01-09 14:32:28');
INSERT INTO `forum_post` VALUES (12, '测试07', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:32:33', '2021-01-09 14:32:32');
INSERT INTO `forum_post` VALUES (13, '测试08', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:32:38', '2021-01-09 14:32:37');
INSERT INTO `forum_post` VALUES (14, '测试09', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:32:57', '2021-01-09 14:32:56');
INSERT INTO `forum_post` VALUES (15, '测试10', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:01', '2021-01-09 14:33:00');
INSERT INTO `forum_post` VALUES (16, '测试11', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:04', '2021-01-09 14:33:04');
INSERT INTO `forum_post` VALUES (17, '测试12', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:07', '2021-01-09 14:33:07');
INSERT INTO `forum_post` VALUES (18, '测试13', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:12', '2021-01-09 14:33:11');
INSERT INTO `forum_post` VALUES (19, '测试14', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:15', '2021-01-09 14:33:14');
INSERT INTO `forum_post` VALUES (20, '测试15', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:18', '2021-01-09 14:33:18');
INSERT INTO `forum_post` VALUES (21, '测试16', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:21', '2021-01-09 14:33:20');
INSERT INTO `forum_post` VALUES (22, '测试17', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:24', '2021-01-09 14:33:23');
INSERT INTO `forum_post` VALUES (23, '测试18', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:27', '2021-01-09 14:33:27');
INSERT INTO `forum_post` VALUES (24, '测试19', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:31', '2021-01-09 14:33:30');
INSERT INTO `forum_post` VALUES (25, '测试20', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:36', '2021-01-09 14:33:35');
INSERT INTO `forum_post` VALUES (26, '发一个贴', '测试内容。', 1, 4, 1, '2021-01-09 23:44:11', '2021-01-09 23:44:11');
INSERT INTO `forum_post` VALUES (27, '再发一个帖', '测试内容2', 1, 4, 1, '2021-01-09 23:45:16', '2021-01-09 23:45:15');
INSERT INTO `forum_post` VALUES (28, '关注测试', '123', 1, 1, 1, '2021-01-11 20:13:46', '2021-01-11 20:13:45');
INSERT INTO `forum_post` VALUES (29, '测试图片上传', '123123', 1, 4, 1, '2021-01-28 12:27:03', '2021-01-28 12:27:02');
INSERT INTO `forum_post` VALUES (31, 'l2lkl', 'kkl', 2, 4, 1, '2021-01-28 13:06:52', '2021-01-28 13:06:52');
INSERT INTO `forum_post` VALUES (32, '发图片看看', '测试图片', 2, 4, 1, '2021-01-29 12:08:33', '2021-01-29 12:08:33');

-- ----------------------------
-- Table structure for forum_post_images
-- ----------------------------
DROP TABLE IF EXISTS `forum_post_images`;
CREATE TABLE `forum_post_images`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `post_id` bigint(0) NOT NULL COMMENT '帖子id',
  `post_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '帖子图片',
  `identifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片标识符',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_post_images
-- ----------------------------
INSERT INTO `forum_post_images` VALUES (1, 31, 'http://localhost:8088/images/85ebef79-7c6d-458d-bf9d-681e45a822e7.png', '');
INSERT INTO `forum_post_images` VALUES (2, 31, 'http://localhost:8088/images/cfb8a8a6-9960-4c55-942d-cccd38b5d433.jpg', '');
INSERT INTO `forum_post_images` VALUES (3, 32, 'http://localhost:8088/PostImages/e010c1fe-89bb-49c2-9ccd-68d9af84eb5c.png', '');
INSERT INTO `forum_post_images` VALUES (4, 32, 'http://localhost:8088/PostImages/00c2fa5e-3f91-4b9f-b126-692bde0a59df.jpg', '');

-- ----------------------------
-- Table structure for forum_subscribe
-- ----------------------------
DROP TABLE IF EXISTS `forum_subscribe`;
CREATE TABLE `forum_subscribe`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '关注表id',
  `be_subscribe` bigint(0) NOT NULL COMMENT '被关注者id',
  `subscribe` bigint(0) NOT NULL COMMENT '关注者id',
  `is_del` tinyint(0) NOT NULL DEFAULT 0 COMMENT '0-存在关注关系 1-取消关注',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后变更时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_subscribe
-- ----------------------------
INSERT INTO `forum_subscribe` VALUES (1, 1, 4, 0, '2021-01-22 23:13:48');
INSERT INTO `forum_subscribe` VALUES (2, 2, 4, 0, '2021-01-22 23:15:16');
INSERT INTO `forum_subscribe` VALUES (3, 4, 2, 0, '2021-01-08 09:02:58');
INSERT INTO `forum_subscribe` VALUES (4, 1, 2, 1, '2021-01-22 23:54:06');

-- ----------------------------
-- Table structure for forum_user
-- ----------------------------
DROP TABLE IF EXISTS `forum_user`;
CREATE TABLE `forum_user`  (
  `user_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码,MD5加密',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `personalized_signature` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '个性签名',
  `role` int(0) NOT NULL DEFAULT 1 COMMENT '角色，1-普通用户，2-管理员',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `head_sculpture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `is_ban` int(0) NULL DEFAULT 0 COMMENT '是否在封号中 0-否  1-是',
  `start_time` timestamp(0) NULL DEFAULT NULL COMMENT '开始封号时间',
  `end_time` timestamp(0) NULL DEFAULT NULL COMMENT '结束封号时间',
  `ban_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '封号理由',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_user
-- ----------------------------
INSERT INTO `forum_user` VALUES (1, '测试', 'eiuGicLv7Rvzv50Hk33xyw==', '', '1', 1, '2020-12-26 20:43:58', '2021-02-02 10:55:47', 'E://webforumimg/7bcdf02d-2160-44ce-8b0b-4d8a2b26accc.jpg', 0, NULL, NULL, '');
INSERT INTO `forum_user` VALUES (2, 'admin', 'eiuGicLv7Rvzv50Hk33xyw==', '', '', 2, '2020-12-26 20:47:00', '2020-12-26 20:47:20', 'E://webforumimg/7bcdf02d-2160-44ce-8b0b-4d8a2b26accc.jpg', 0, NULL, NULL, '');
INSERT INTO `forum_user` VALUES (4, 'hyx', '6zNMw2KXXZFDnloG3nou0Q==', '310695702@qq.com', '123', 1, '2020-12-31 10:22:44', '2021-02-02 10:55:48', 'E://webforumimg/7bcdf02d-2160-44ce-8b0b-4d8a2b26accc.jpg', 0, '2021-01-27 18:32:53', '2021-01-27 18:34:33', '无');

-- ----------------------------
-- Table structure for forum_user_category_subscribe
-- ----------------------------
DROP TABLE IF EXISTS `forum_user_category_subscribe`;
CREATE TABLE `forum_user_category_subscribe`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '关注表id',
  `category_id` bigint(0) NOT NULL COMMENT '被关注的板块id',
  `user_id` bigint(0) NOT NULL COMMENT '关注者id',
  `is_del` tinyint(0) NOT NULL DEFAULT 0 COMMENT '0-存在关注关系 1-取消关注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_user_category_subscribe
-- ----------------------------
INSERT INTO `forum_user_category_subscribe` VALUES (1, 3, 4, 0);
INSERT INTO `forum_user_category_subscribe` VALUES (2, 1, 2, 0);
INSERT INTO `forum_user_category_subscribe` VALUES (3, 1, 4, 0);
INSERT INTO `forum_user_category_subscribe` VALUES (4, 2, 4, 0);

SET FOREIGN_KEY_CHECKS = 1;
