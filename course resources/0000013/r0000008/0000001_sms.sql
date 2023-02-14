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
-- База данных: `sms`
--

-- --------------------------------------------------------

--
-- Структура таблицы `admin`
--

CREATE TABLE `admin` (
  `ID` int(11) NOT NULL,
  `f_name` varchar(100) NOT NULL,
  `s_name` varchar(100) NOT NULL,
  `m_name` varchar(100) NOT NULL,
  `gender` char(1) NOT NULL,
  `birth_date` varchar(50) NOT NULL,
  `user_type` char(1) NOT NULL,
  `email` varchar(150) NOT NULL,
  `phone` varchar(12) NOT NULL,
  `password` varchar(100) NOT NULL,
  `reg_status` tinyint(1) NOT NULL,
  `log_status` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Дамп данных таблицы `admin`
--

INSERT INTO `admin` (`ID`, `f_name`, `s_name`, `m_name`, `gender`, `birth_date`, `user_type`, `email`, `phone`, `password`, `reg_status`, `log_status`) VALUES
(1, 'Zarshedjon', 'Nasimov', 'Utkurovich', 'm', '29/06/2004', 'A', 'zarshednasimov@gmail.com', '998944709937', 'Zarshed@2004', 1, 0);

-- --------------------------------------------------------

--
-- Структура таблицы `groups`
--

CREATE TABLE `groups` (
  `ID` int(11) NOT NULL,
  `timing` varchar(50) NOT NULL,
  `date_created` varchar(50) NOT NULL,
  `students_number` int(3) NOT NULL,
  `max_students` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Дамп данных таблицы `groups`
--

INSERT INTO `groups` (`ID`, `timing`, `date_created`, `students_number`, `max_students`) VALUES
(2, 'morning', '09/02/2023', 0, 25),
(3, 'afternoon', '10/03/2023', 0, 25);

-- --------------------------------------------------------

--
-- Структура таблицы `student`
--

CREATE TABLE `student` (
  `ID` int(11) NOT NULL,
  `f_name` varchar(100) NOT NULL,
  `s_name` varchar(100) NOT NULL,
  `m_name` varchar(100) NOT NULL,
  `group_id` varchar(10) DEFAULT NULL,
  `gender` char(1) NOT NULL,
  `birth_date` varchar(50) NOT NULL,
  `user_type` char(1) NOT NULL,
  `email` varchar(150) NOT NULL,
  `phone` varchar(12) NOT NULL,
  `password` varchar(100) NOT NULL,
  `reg_status` tinyint(1) NOT NULL,
  `log_status` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Дамп данных таблицы `student`
--

INSERT INTO `student` (`ID`, `f_name`, `s_name`, `m_name`, `group_id`, `gender`, `birth_date`, `user_type`, `email`, `phone`, `password`, `reg_status`, `log_status`) VALUES
(6, 'Zarshedjon', 'Nasimov', 'Utkurovich', 'G0000003', 'm', '29/06/2004', 'S', 'zarshednasimov@gmail.com', '998944709937', 'Zarshed@2004', 1, 0),
(17, 'Rustam', 'Kodirov', 'Anvarovich', NULL, 'm', '21/09/2020', 'S', 'rustam@gmail.com', '998911234567', 'Rustam@2003', 0, 0),
(18, 'Ganisher', 'Boboyorev', 'Sarvarovich', 'G0000003', 'm', '27/08/2004', 'S', 'ganisher@gmail.com', '998951234567', 'Ganisher@2004', 1, 0),
(19, 'Lucy', 'Johnson', 'Georgy', 'G0000002', 'f', '20/02/2002', 'S', 'lucy@gmail.com', '998991234567', 'Lucy@2002', 1, 0),
(20, 'Sarvarbek', 'Azimjonov', 'Dilmurodovich', 'G0000002', 'm', '21/03/2004', 'S', 'sarvar@gmail.com', '998951234567', 'Sarvar@2004', 1, 0);

-- --------------------------------------------------------

--
-- Структура таблицы `teacher`
--

CREATE TABLE `teacher` (
  `ID` int(11) NOT NULL,
  `f_name` varchar(100) NOT NULL,
  `s_name` varchar(100) NOT NULL,
  `m_name` varchar(100) NOT NULL,
  `gender` char(1) NOT NULL,
  `birth_date` varchar(50) NOT NULL,
  `user_type` char(1) NOT NULL,
  `email` varchar(150) NOT NULL,
  `phone` varchar(12) NOT NULL,
  `password` varchar(100) NOT NULL,
  `reg_status` tinyint(1) NOT NULL,
  `log_status` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Дамп данных таблицы `teacher`
--

INSERT INTO `teacher` (`ID`, `f_name`, `s_name`, `m_name`, `gender`, `birth_date`, `user_type`, `email`, `phone`, `password`, `reg_status`, `log_status`) VALUES
(1, 'Shohijahon', 'Nasimov', 'Uktamovich', 'm', '09/06/2010', 'T', 'shohijahon@gmail.com', '998991234567', 'Shohijahon@2010', 1, 0),
(2, 'Amira', 'Muminova', 'Azamatovna', 'f', '12/01/2000', 'T', 'amira@gmail.com', '998941234567', 'Amira@2000', 0, 0),
(3, 'Zarshedjon', 'Nasimov', 'Utkurovich', 'm', '20/06/1995', 'T', 'zarshed@mail.ru', '998951234567', 'Zarshed@2004', 0, 0),
(4, 'Faridun', 'Nasimov', 'Utkurovich', 'm', '12/06/1995', 'T', 'faridun@gmail.com', '998951234567', 'Faridun@1997', 0, 0);

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`ID`);

--
-- Индексы таблицы `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`ID`);

--
-- Индексы таблицы `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`ID`);

--
-- Индексы таблицы `teacher`
--
ALTER TABLE `teacher`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `admin`
--
ALTER TABLE `admin`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT для таблицы `groups`
--
ALTER TABLE `groups`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT для таблицы `student`
--
ALTER TABLE `student`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT для таблицы `teacher`
--
ALTER TABLE `teacher`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
