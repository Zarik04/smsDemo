-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1
-- Время создания: Фев 13 2023 г., 20:01
-- Версия сервера: 10.4.24-MariaDB
-- Версия PHP: 8.1.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `sms_courses`
--

-- --------------------------------------------------------

--
-- Структура таблицы `course_list`
--

CREATE TABLE `course_list` (
  `ID` int(11) NOT NULL,
  `course_name` varchar(150) NOT NULL,
  `course_logo` varchar(150) NOT NULL,
  `course_teacher` varchar(10) NOT NULL,
  `start_date` varchar(50) NOT NULL,
  `end_date` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Дамп данных таблицы `course_list`
--

INSERT INTO `course_list` (`ID`, `course_name`, `course_logo`, `course_teacher`, `start_date`, `end_date`) VALUES
(12, 'Math', 'CALCULATOR', 'T0000003', '10/02/2023', '10/07/2023'),
(13, 'Software Engineering', 'CODE', 'T0000001', '10/01/2023', '15/06/2023');

-- --------------------------------------------------------

--
-- Структура таблицы `s0000006`
--

CREATE TABLE `s0000006` (
  `ID` int(11) NOT NULL,
  `course_name` varchar(150) NOT NULL,
  `course_id` varchar(8) NOT NULL,
  `course_logo` varchar(150) NOT NULL,
  `start_date` varchar(50) NOT NULL,
  `enrolled_date` varchar(50) NOT NULL,
  `end_date` varchar(50) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `teacher` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Дамп данных таблицы `s0000006`
--

INSERT INTO `s0000006` (`ID`, `course_name`, `course_id`, `course_logo`, `start_date`, `enrolled_date`, `end_date`, `status`, `teacher`) VALUES
(18, 'Math', 'C0000012', 'CALCULATOR', '10/02/2023', '07/02/2023', '10/07/2023', 1, 'T0000003'),
(19, 'Software Engineering', 'C0000013', 'CODE', '10/01/2023', '07/02/2023', '15/06/2023', 1, 'T0000004');

-- --------------------------------------------------------

--
-- Структура таблицы `s0000017`
--

CREATE TABLE `s0000017` (
  `ID` int(11) UNSIGNED NOT NULL,
  `course_name` varchar(150) DEFAULT NULL,
  `course_id` varchar(8) DEFAULT NULL,
  `course_logo` varchar(150) DEFAULT NULL,
  `start_date` varchar(50) DEFAULT NULL,
  `enrolled_date` varchar(50) DEFAULT NULL,
  `end_date` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `teacher` varchar(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Структура таблицы `s0000018`
--

CREATE TABLE `s0000018` (
  `ID` int(11) UNSIGNED NOT NULL,
  `course_name` varchar(150) DEFAULT NULL,
  `course_id` varchar(8) DEFAULT NULL,
  `course_logo` varchar(150) DEFAULT NULL,
  `start_date` varchar(50) DEFAULT NULL,
  `enrolled_date` varchar(50) DEFAULT NULL,
  `end_date` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `teacher` varchar(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Структура таблицы `s0000019`
--

CREATE TABLE `s0000019` (
  `ID` int(11) UNSIGNED NOT NULL,
  `course_name` varchar(150) DEFAULT NULL,
  `course_id` varchar(8) DEFAULT NULL,
  `course_logo` varchar(150) DEFAULT NULL,
  `start_date` varchar(50) DEFAULT NULL,
  `enrolled_date` varchar(50) DEFAULT NULL,
  `end_date` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `teacher` varchar(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Структура таблицы `s0000020`
--

CREATE TABLE `s0000020` (
  `ID` int(11) UNSIGNED NOT NULL,
  `course_name` varchar(150) DEFAULT NULL,
  `course_id` varchar(8) DEFAULT NULL,
  `course_logo` varchar(150) DEFAULT NULL,
  `start_date` varchar(50) DEFAULT NULL,
  `enrolled_date` varchar(50) DEFAULT NULL,
  `end_date` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `teacher` varchar(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Дамп данных таблицы `s0000020`
--

INSERT INTO `s0000020` (`ID`, `course_name`, `course_id`, `course_logo`, `start_date`, `enrolled_date`, `end_date`, `status`, `teacher`) VALUES
(1, 'Math', 'C0000012', 'CALCULATOR', '10/02/2023', '07/02/2023', '10/07/2023', 1, 'T0000003'),
(2, 'Software Engineering', 'C0000013', 'CODE', '10/01/2023', '07/02/2023', '15/06/2023', 1, 'T0000004');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `course_list`
--
ALTER TABLE `course_list`
  ADD PRIMARY KEY (`ID`);

--
-- Индексы таблицы `s0000006`
--
ALTER TABLE `s0000006`
  ADD PRIMARY KEY (`ID`);

--
-- Индексы таблицы `s0000017`
--
ALTER TABLE `s0000017`
  ADD PRIMARY KEY (`ID`);

--
-- Индексы таблицы `s0000018`
--
ALTER TABLE `s0000018`
  ADD PRIMARY KEY (`ID`);

--
-- Индексы таблицы `s0000019`
--
ALTER TABLE `s0000019`
  ADD PRIMARY KEY (`ID`);

--
-- Индексы таблицы `s0000020`
--
ALTER TABLE `s0000020`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `course_list`
--
ALTER TABLE `course_list`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT для таблицы `s0000006`
--
ALTER TABLE `s0000006`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT для таблицы `s0000017`
--
ALTER TABLE `s0000017`
  MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT для таблицы `s0000018`
--
ALTER TABLE `s0000018`
  MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT для таблицы `s0000019`
--
ALTER TABLE `s0000019`
  MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT для таблицы `s0000020`
--
ALTER TABLE `s0000020`
  MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
