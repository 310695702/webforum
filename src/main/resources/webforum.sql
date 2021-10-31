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

 Date: 19/04/2021 22:42:17
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
INSERT INTO `android_version` VALUES ('3', '2.5');

-- ----------------------------
-- Table structure for forum_banner
-- ----------------------------
DROP TABLE IF EXISTS `forum_banner`;
CREATE TABLE `forum_banner`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '轮播id',
  `post_id` bigint(0) NOT NULL COMMENT '轮播帖子id',
  `recommend_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '轮播图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_banner
-- ----------------------------

-- ----------------------------
-- Table structure for forum_category
-- ----------------------------
DROP TABLE IF EXISTS `forum_category`;
CREATE TABLE `forum_category`  (
  `category_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `category_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类图片',
  `is_recommend` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否为推荐分类0-不是 1-是',
  `visibility` int(0) NOT NULL DEFAULT 0 COMMENT '是否可见 0-可见 1-不可见',
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_category
-- ----------------------------
INSERT INTO `forum_category` VALUES (1, 'DNFF', 'http://localhost:8088/categoryImages/cbea7ab8-f3ee-41bd-bd13-b45cf8057cd0.png', 0, 0);
INSERT INTO `forum_category` VALUES (2, '原神', 'http://localhost:8088/categoryImages/5a929357-7d89-42eb-9b67-f1004b73fa83.jpg', 0, 0);
INSERT INTO `forum_category` VALUES (3, '2333', 'http://localhost:8088/categoryImages/19c3a28f-ee0c-48b3-a895-21afa45c7da6.png', 0, 0);
INSERT INTO `forum_category` VALUES (4, '测试2', 'http://localhost:8088/categoryImages/c6796f51-60cc-4506-b98e-6b669694e110.jpg', 0, 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_comment
-- ----------------------------
INSERT INTO `forum_comment` VALUES (1, 4, '1', 1, '2020-12-31 16:58:05', 1);
INSERT INTO `forum_comment` VALUES (2, 4, '你好呀。。', 28, '2021-02-02 10:56:10', 1);
INSERT INTO `forum_comment` VALUES (3, 4, '哈喽。。。', 28, '2021-02-02 10:59:29', 1);
INSERT INTO `forum_comment` VALUES (4, 4, '你在干嘛呢', 28, '2021-02-02 10:59:52', 1);
INSERT INTO `forum_comment` VALUES (5, 4, '你好呀。。', 28, '2021-02-02 11:00:07', 1);
INSERT INTO `forum_comment` VALUES (6, 4, '你好呀。。', 1, '2021-02-02 12:13:25', 1);
INSERT INTO `forum_comment` VALUES (12, 4, '哈喽哈喽。', 1, '2021-02-02 12:58:15', 1);
INSERT INTO `forum_comment` VALUES (13, 4, '哈喽哈喽，', 28, '2021-02-02 12:58:39', 1);
INSERT INTO `forum_comment` VALUES (14, 4, '哈喽哈喽。', 28, '2021-02-02 14:05:37', 1);
INSERT INTO `forum_comment` VALUES (15, 4, '12222', 3, '2021-02-08 21:38:52', 1);
INSERT INTO `forum_comment` VALUES (16, 4, '12222', 3, '2021-02-08 21:44:14', 1);
INSERT INTO `forum_comment` VALUES (17, 2, '12311', 32, '2021-03-19 20:53:10', 1);
INSERT INTO `forum_comment` VALUES (18, 2, '12311', 32, '2021-03-19 20:53:12', 1);
INSERT INTO `forum_comment` VALUES (19, 2, '12311', 32, '2021-03-19 20:53:14', 1);
INSERT INTO `forum_comment` VALUES (20, 2, '12311', 32, '2021-03-19 20:53:14', 1);
INSERT INTO `forum_comment` VALUES (22, 4, '评论图片1', 32, '2021-04-16 21:16:16', 1);
INSERT INTO `forum_comment` VALUES (24, 4, '123123', 10, '2021-04-18 01:07:00', 1);
INSERT INTO `forum_comment` VALUES (25, 4, '123123', 10, '2021-04-18 01:16:59', 1);

-- ----------------------------
-- Table structure for forum_comment_images
-- ----------------------------
DROP TABLE IF EXISTS `forum_comment_images`;
CREATE TABLE `forum_comment_images`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `comment_id` bigint(0) NOT NULL COMMENT '评论id',
  `comment_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_comment_images
-- ----------------------------
INSERT INTO `forum_comment_images` VALUES (1, 22, 'http://localhost/CommentImages/a6ce365a-8340-428b-a90b-6abe92da41d6.png');
INSERT INTO `forum_comment_images` VALUES (2, 22, 'http://localhost/CommentImages/cc0d48a7-b816-445d-b818-7dc6e1083145.jpg');
INSERT INTO `forum_comment_images` VALUES (3, 5, 'http://localhost/CommentImages/db7ec590-9cb8-4420-a256-2e06afd8dec7.jpg');
INSERT INTO `forum_comment_images` VALUES (4, 5, 'http://localhost/CommentImages/46495434-510e-475e-bb40-b7cab933fa75.png');
INSERT INTO `forum_comment_images` VALUES (5, 25, 'http://localhost/CommentImages/db7ec590-9cb8-4420-a256-2e06afd8dec7.jpg');
INSERT INTO `forum_comment_images` VALUES (6, 25, 'http://localhost/CommentImages/46495434-510e-475e-bb40-b7cab933fa75.png');

-- ----------------------------
-- Table structure for forum_good
-- ----------------------------
DROP TABLE IF EXISTS `forum_good`;
CREATE TABLE `forum_good`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `post_id` bigint(0) NOT NULL COMMENT '帖子id',
  `users` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '点赞的信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_good
-- ----------------------------

-- ----------------------------
-- Table structure for forum_msg_state
-- ----------------------------
DROP TABLE IF EXISTS `forum_msg_state`;
CREATE TABLE `forum_msg_state`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT ' 状态id',
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  `post_id` bigint(0) NOT NULL COMMENT '帖子id',
  `msg_state` int(0) NOT NULL DEFAULT 0 COMMENT '消息状态0-未读 1-已读',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_msg_state
-- ----------------------------
INSERT INTO `forum_msg_state` VALUES (1, 1, 1, 0);
INSERT INTO `forum_msg_state` VALUES (2, 1, 28, 1);
INSERT INTO `forum_msg_state` VALUES (3, 3, 3, 1);
INSERT INTO `forum_msg_state` VALUES (4, 4, 32, 1);
INSERT INTO `forum_msg_state` VALUES (5, 4, 10, 1);

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
  `is_essences` int(0) NOT NULL DEFAULT 0 COMMENT '是否为精华帖 1-是 0-否',
  `view_num` int(0) NOT NULL DEFAULT 0 COMMENT '观看次数',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_post
-- ----------------------------
INSERT INTO `forum_post` VALUES (1, '测试', '测试发帖', 1, 1, 2, '2020-12-26 22:02:22', '2021-03-08 22:00:46', 0, 0);
INSERT INTO `forum_post` VALUES (2, '测试2', '测试发帖', 1, 1, 2, '2020-12-26 22:02:53', '2021-03-08 22:00:46', 0, 0);
INSERT INTO `forum_post` VALUES (3, '测试3', '测试内容3', 1, 3, 2, '2020-12-26 22:31:40', '2021-03-08 22:00:46', 0, 0);
INSERT INTO `forum_post` VALUES (5, 'DNF', '今天也是充满希望的一天', 1, 1, 2, '2021-01-09 11:06:21', '2021-03-08 22:00:46', 0, 0);
INSERT INTO `forum_post` VALUES (6, '测试01', 'asjkdnasda', 1, 4, 2, '2021-01-09 14:32:01', '2021-03-08 22:00:46', 1, 0);
INSERT INTO `forum_post` VALUES (7, '测试02', 'asjkdnasda', 1, 4, 2, '2021-01-09 14:32:06', '2021-01-09 14:32:06', 0, 0);
INSERT INTO `forum_post` VALUES (8, '测试03', 'asjkdnasda', 1, 4, 2, '2021-01-09 14:32:15', '2021-03-08 22:00:46', 0, 0);
INSERT INTO `forum_post` VALUES (9, '测试04', 'asjkdnasda', 1, 4, 2, '2021-01-09 14:32:22', '2021-03-08 22:00:46', 0, 0);
INSERT INTO `forum_post` VALUES (10, '测试05', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:32:25', '2021-04-18 01:16:59', 0, 0);
INSERT INTO `forum_post` VALUES (11, '测试06', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:32:29', '2021-04-16 22:32:32', 0, 0);
INSERT INTO `forum_post` VALUES (12, '测试07', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:32:33', '2021-04-16 22:32:33', 0, 0);
INSERT INTO `forum_post` VALUES (13, '测试08', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:32:38', '2021-04-16 22:32:34', 0, 0);
INSERT INTO `forum_post` VALUES (14, '测试09', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:32:57', '2021-04-16 22:32:35', 0, 0);
INSERT INTO `forum_post` VALUES (15, '测试10', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:01', '2021-04-16 22:32:36', 0, 0);
INSERT INTO `forum_post` VALUES (16, '测试11', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:04', '2021-04-16 22:32:37', 0, 0);
INSERT INTO `forum_post` VALUES (17, '测试12', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:07', '2021-04-16 22:32:38', 0, 0);
INSERT INTO `forum_post` VALUES (18, '测试13', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:12', '2021-04-16 22:32:39', 0, 0);
INSERT INTO `forum_post` VALUES (19, '测试14', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:15', '2021-04-16 22:32:39', 0, 0);
INSERT INTO `forum_post` VALUES (20, '测试15', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:18', '2021-04-16 22:32:39', 0, 0);
INSERT INTO `forum_post` VALUES (21, '测试16', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:21', '2021-04-16 22:32:40', 0, 0);
INSERT INTO `forum_post` VALUES (22, '测试17', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:24', '2021-04-16 22:32:40', 0, 0);
INSERT INTO `forum_post` VALUES (23, '测试18', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:27', '2021-04-16 22:32:41', 0, 0);
INSERT INTO `forum_post` VALUES (24, '测试19', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:31', '2021-04-16 22:32:41', 0, 0);
INSERT INTO `forum_post` VALUES (25, '测试20', 'asjkdnasda', 1, 4, 1, '2021-01-09 14:33:36', '2021-04-16 22:32:42', 0, 0);
INSERT INTO `forum_post` VALUES (26, '发一个贴', '测试内容。', 1, 4, 1, '2021-01-09 23:44:11', '2021-04-16 22:32:42', 0, 0);
INSERT INTO `forum_post` VALUES (27, '再发一个帖', '测试内容2', 1, 4, 1, '2021-01-09 23:45:16', '2021-04-16 22:32:42', 0, 0);
INSERT INTO `forum_post` VALUES (28, '关注测试', '123', 1, 1, 1, '2021-01-11 20:13:46', '2021-04-16 22:32:43', 0, 0);
INSERT INTO `forum_post` VALUES (29, '测试图片上传', '123123', 1, 4, 1, '2021-01-28 12:27:03', '2021-04-16 22:32:44', 0, 0);
INSERT INTO `forum_post` VALUES (31, 'l2lkl', 'kkl', 2, 4, 1, '2021-01-28 13:06:52', '2021-01-28 13:06:52', 0, 0);
INSERT INTO `forum_post` VALUES (32, '发图片看看', '测试图片', 2, 4, 1, '2021-01-29 12:08:33', '2021-04-16 21:16:16', 0, 5);
INSERT INTO `forum_post` VALUES (33, '123123', '哈哈哈哈', 1, 4, 2, '2021-03-06 00:00:48', '2021-03-08 22:00:46', 0, 0);

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
-- Table structure for forum_reply
-- ----------------------------
DROP TABLE IF EXISTS `forum_reply`;
CREATE TABLE `forum_reply`  (
  `reply_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '回复id',
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  `comment_id` bigint(0) NOT NULL COMMENT '评论id',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回复内容',
  `reply_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '回复时间',
  PRIMARY KEY (`reply_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_reply
-- ----------------------------
INSERT INTO `forum_reply` VALUES (2, 4, 1, '2333', '2021-03-19 20:25:54');
INSERT INTO `forum_reply` VALUES (3, 2, 20, '12312', '2021-03-19 20:53:35');
INSERT INTO `forum_reply` VALUES (4, 2, 20, '11', '2021-03-19 20:53:42');
INSERT INTO `forum_reply` VALUES (5, 2, 20, '2', '2021-03-19 20:53:47');
INSERT INTO `forum_reply` VALUES (6, 2, 20, '33', '2021-03-19 20:53:48');
INSERT INTO `forum_reply` VALUES (7, 2, 20, '444', '2021-03-19 20:53:50');

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
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_subscribe
-- ----------------------------
INSERT INTO `forum_subscribe` VALUES (1, 1, 4, 0, '2021-03-05 20:14:43');
INSERT INTO `forum_subscribe` VALUES (2, 2, 4, 0, '2021-01-22 23:15:16');
INSERT INTO `forum_subscribe` VALUES (3, 4, 2, 1, '2021-03-05 20:14:51');
INSERT INTO `forum_subscribe` VALUES (4, 1, 2, 1, '2021-01-22 23:54:06');
INSERT INTO `forum_subscribe` VALUES (10, 4, 1, 0, '2021-03-04 22:23:17');

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
  `ban_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封号理由',
  `school` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学校',
  `sex` int(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0 COMMENT '性别 0-保密 1-男 2-女',
  `personal_website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个人网站',
  `wechat` varchar(21) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信号',
  `qq` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'qq号',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_user
-- ----------------------------
INSERT INTO `forum_user` VALUES (1, '测试', 'eiuGicLv7Rvzv50Hk33xyw==', '', '1', 1, '2020-12-26 20:43:58', '2021-03-16 12:31:03', 'E://webforumimg/7bcdf02d-2160-44ce-8b0b-4d8a2b26accc.jpg', 0, NULL, NULL, '', '123', 2, '123', '123', '123');
INSERT INTO `forum_user` VALUES (2, 'admin', 'eiuGicLv7Rvzv50Hk33xyw==', '', '', 2, '2020-12-26 20:47:00', '2021-03-16 12:31:03', 'E://webforumimg/7bcdf02d-2160-44ce-8b0b-4d8a2b26accc.jpg', 0, NULL, NULL, '', '123', 2, '123', '123', '123');
INSERT INTO `forum_user` VALUES (4, 'hyx', '6zNMw2KXXZFDnloG3nou0Q==', '', '123', 1, '2020-12-31 10:22:44', '2021-03-16 12:34:44', 'E://webforumimg/7bcdf02d-2160-44ce-8b0b-4d8a2b26accc.jpg', 0, '2021-01-27 18:32:53', '2021-01-27 18:34:33', '无', NULL, 0, NULL, NULL, '123');
INSERT INTO `forum_user` VALUES (6, '12333', 'eiuGicLv7Rvzv50Hk33xyw==', '', '', 1, '2021-03-03 00:17:01', '2021-03-16 12:31:39', NULL, 0, NULL, NULL, NULL, '123', 1, '123', '123', '1232');
INSERT INTO `forum_user` VALUES (7, '4444', 'eiuGicLv7Rvzv50Hk33xyw==', '310695702@qq.com', '', 1, '2021-03-08 13:01:40', '2021-03-16 12:31:03', NULL, 0, NULL, NULL, NULL, '123', 0, '123', '123', '123');

-- ----------------------------
-- Table structure for forum_user_category_subscribe
-- ----------------------------
DROP TABLE IF EXISTS `forum_user_category_subscribe`;
CREATE TABLE `forum_user_category_subscribe`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '关注表id',
  `category_id` bigint(0) NOT NULL COMMENT '被关注的板块id',
  `user_id` bigint(0) NOT NULL COMMENT '关注者id',
  `is_del` tinyint(0) NOT NULL DEFAULT 0 COMMENT '0-存在关注关系 1-取消关注',
  `is_sign` int(0) NOT NULL DEFAULT 0 COMMENT '今日是否签到0-否 1-是',
  `exp` int(0) NOT NULL DEFAULT 0 COMMENT '用户该分类经验',
  `sign_days` int(0) NOT NULL DEFAULT 0 COMMENT '用户连续签到天数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum_user_category_subscribe
-- ----------------------------
INSERT INTO `forum_user_category_subscribe` VALUES (1, 3, 4, 0, 0, 0, 0);
INSERT INTO `forum_user_category_subscribe` VALUES (2, 1, 2, 0, 0, 40, 0);
INSERT INTO `forum_user_category_subscribe` VALUES (3, 1, 4, 0, 0, 36, 0);
INSERT INTO `forum_user_category_subscribe` VALUES (4, 2, 4, 0, 0, 11, 0);

-- ----------------------------
-- Procedure structure for forum_subscribe
-- ----------------------------
DROP PROCEDURE IF EXISTS `forum_subscribe`;
delimiter ;;
CREATE PROCEDURE `forum_subscribe`()
begin
    declare i int;                      #申明变量
		declare j int;  
    set i = 0;                          #变量赋值
		set j = 1; 
    while i < 10 do                     #结束循环的条件: 当i大于10时跳出while循环
        insert into forum_subscribe(be_subscribe,subscribe,is_del) VALUES(4,(select user_id from forum_user WHERE user_id != 4 LIMIT i,j),0) ;    #往test表添加数据
        set i = i + 1;                  #循环一次,i加一
				 set j = j + 1; 
    end while;                          #结束while循环
    select * from forum_subscribe;                 #查看test表数据
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for test
-- ----------------------------
DROP PROCEDURE IF EXISTS `test`;
delimiter ;;
CREATE PROCEDURE `test`()
begin
    declare i int;                      #申明变量
    set i = 0;                          #变量赋值
    while i < 10 do                     #结束循环的条件: 当i大于10时跳出while循环
        insert into test values (i);    #往test表添加数据
        set i = i + 1;                  #循环一次,i加一
    end while;                          #结束while循环
    select * from test;                 #查看test表数据
end
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
