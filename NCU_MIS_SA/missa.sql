-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 2022-01-05 17:49:53
-- 伺服器版本： 10.4.14-MariaDB
-- PHP 版本： 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `missa`
--

-- --------------------------------------------------------

--
-- 資料表結構 `tb_manager`
--

CREATE TABLE `tb_manager` (
  `manager_id` int(11) NOT NULL,
  `root_manager` int(11) NOT NULL DEFAULT 0,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `tb_manager`
--

INSERT INTO `tb_manager` (`manager_id`, `root_manager`, `password`) VALUES
(1, 1, 'root'),
(4, 1, 'SA'),
(5, 0, 'no');

-- --------------------------------------------------------

--
-- 資料表結構 `tb_member`
--

CREATE TABLE `tb_member` (
  `user_id` int(11) NOT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 資料表結構 `tb_reserve`
--

CREATE TABLE `tb_reserve` (
  `reserve_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone_Number` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `number_of_People` int(11) NOT NULL,
  `reserve_datetime_id` int(11) NOT NULL,
  `checkin` int(11) NOT NULL DEFAULT 0,
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rate` int(11) NOT NULL DEFAULT 0,
  `create_time` datetime NOT NULL DEFAULT current_timestamp(),
  `modified_time` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 資料表結構 `tb_reservedatetime`
--

CREATE TABLE `tb_reservedatetime` (
  `reserve_datetime_id` int(11) NOT NULL,
  `available_datetime` datetime NOT NULL,
  `available_seat_number` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `tb_manager`
--
ALTER TABLE `tb_manager`
  ADD PRIMARY KEY (`manager_id`);

--
-- 資料表索引 `tb_member`
--
ALTER TABLE `tb_member`
  ADD PRIMARY KEY (`user_id`);

--
-- 資料表索引 `tb_reserve`
--
ALTER TABLE `tb_reserve`
  ADD PRIMARY KEY (`reserve_id`);

--
-- 資料表索引 `tb_reservedatetime`
--
ALTER TABLE `tb_reservedatetime`
  ADD PRIMARY KEY (`reserve_datetime_id`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `tb_manager`
--
ALTER TABLE `tb_manager`
  MODIFY `manager_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `tb_member`
--
ALTER TABLE `tb_member`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `tb_reserve`
--
ALTER TABLE `tb_reserve`
  MODIFY `reserve_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `tb_reservedatetime`
--
ALTER TABLE `tb_reservedatetime`
  MODIFY `reserve_datetime_id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
