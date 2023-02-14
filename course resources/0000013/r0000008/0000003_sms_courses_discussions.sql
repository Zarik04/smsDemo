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
-- База данных: `sms_courses_discussions`
--

-- --------------------------------------------------------

--
-- Структура таблицы `c0000012`
--

CREATE TABLE `c0000012` (
  `ID` int(11) UNSIGNED NOT NULL,
  `date` varchar(80) DEFAULT NULL,
  `sender` varchar(10) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Дамп данных таблицы `c0000012`
--

INSERT INTO `c0000012` (`ID`, `date`, `sender`, `message`) VALUES
(1, '07/02/2023', 'S0000020', 'Awesome !'),
(2, '07/02/2023', 'S0000006', 'Can you share slides?'),
(3, '06/02/2023', 'T0000001', 'Yes of course!');

-- --------------------------------------------------------

--
-- Структура таблицы `c0000013`
--

CREATE TABLE `c0000013` (
  `ID` int(11) UNSIGNED NOT NULL,
  `date` varchar(80) DEFAULT NULL,
  `sender` varchar(10) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Дамп данных таблицы `c0000013`
--

INSERT INTO `c0000013` (`ID`, `date`, `sender`, `message`) VALUES
(4, '10/02/2023', 'T0000001', 'Hello students!'),
(5, '10/02/2023', 'T0000001', 'Welcome to the course of Software Engineering!');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `c0000012`
--
ALTER TABLE `c0000012`
  ADD PRIMARY KEY (`ID`);

--
-- Индексы таблицы `c0000013`
--
ALTER TABLE `c0000013`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `c0000012`
--
ALTER TABLE `c0000012`
  MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT для таблицы `c0000013`
--
ALTER TABLE `c0000013`
  MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
