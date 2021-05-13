-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 22. Mrz 2021 um 09:43
-- Server-Version: 10.1.21-MariaDB
-- PHP-Version: 7.1.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `wheeloffortune`
--
CREATE DATABASE IF NOT EXISTS `wheeloffortune` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `wheeloffortune`;

--
-- User: gamehost
--

CREATE USER 'gamehost'@'localhost' IDENTIFIED BY 'wheeloffortune';
GRANT ALL PRIVILEGES ON wheeloffortune.* TO 'testhost'@'localhost';

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `category`
--

CREATE TABLE `category` (
  `categoryID` int(11) NOT NULL,
  `category` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `category`
--

INSERT INTO `category` (`categoryID`, `category`) VALUES
(2, 'Politics'),
(3, 'Events'),
(4, 'Celebrities'),
(5, 'Technology'),
(6, 'Finance'),
(7, 'Cooking'),
(8, 'Economics'),
(9, 'Psychology'),
(10, 'Cars'),
(11, 'Practices'),
(13, 'General');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `question`
--

CREATE TABLE `question` (
  `questionID` int(64) NOT NULL,
  `question` varchar(255) NOT NULL,
  `rightAnswer` varchar(255) NOT NULL,
  `wrongAnswer` varchar(255) NOT NULL,
  `categoryID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `question`
--

INSERT INTO `question` (`questionID`, `question`, `rightAnswer`, `wrongAnswer`, `categoryID`) VALUES
(5, 'Where does Yoga originate from?', 'India', 'Greece', 11),
(7, 'What does a band do at an event?', 'Perform', 'Act', 3),
(8, 'Who published the song Never Say Never', 'Justin Bieber', 'Kim Kardashian', 4),
(9, 'Who invented the iPhone?', 'Steve Jobs', 'Tim Cook', 5),
(10, 'What is the most popular food in the world?', 'Pizza', 'Spaghetti', 7),
(11, 'Who is the Chairman of the Federal Reserve?', 'Jerome Powell', 'Janet Yellen', 8),
(12, 'What is the largest car producer in the world?', 'Volkswagen', 'Toyota', 10),
(13, 'Who is the 46th President of the United States?', 'Joe Biden', 'Donald Trump', 2),
(14, 'Name the largest planet in the universe', 'Jupiter', 'Mercury', 13),
(15, 'What is the most valuable company in the world?', 'Saudi Aramco', 'Amazon', 6),
(16, 'What is the most used ingredient in cooking?', 'Salt', 'Pepper', 7),
(17, 'How are budget deficits financed?', 'Credit', 'Revenues', 8);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `score`
--

CREATE TABLE `score` (
  `scoreID` int(11) NOT NULL,
  `rank` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `playedOn` datetime NOT NULL,
  `moneyAmount` decimal(10,0) NOT NULL,
  `roundsAmount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `score`
--

INSERT INTO `score` (`scoreID`, `rank`, `username`, `playedOn`, `moneyAmount`, `roundsAmount`) VALUES
(1, 0, 'admin', '2021-03-10 19:18:13', '1200', 5),
(2, 0, 'admin', '2021-03-10 19:19:34', '1100', 2),
(3, 0, 'admin', '2021-03-10 19:20:49', '600', 9),
(4, 0, 'admin', '2021-03-10 20:27:09', '50', 2),
(5, 0, 'admin', '2021-03-11 07:29:33', '200', 12),
(6, 0, 'lewin', '2021-03-11 07:30:35', '1000', 3),
(7, 0, 'lewin', '2021-03-11 07:32:10', '500', 7),
(8, 0, 'lewin', '2021-03-11 08:06:56', '550', 3),
(9, 0, 'lewin', '2021-03-11 08:04:37', '50', 1),
(10, 0, 'test', '2021-03-13 08:34:12', '1900', 4),
(11, 0, 'lewin', '2021-03-13 21:38:54', '250', 4),
(12, 0, 'lewin', '2021-03-13 21:40:36', '0', 0),
(13, 0, 'lewin', '2021-03-13 21:41:52', '0', 0),
(14, 0, 'lewin', '2021-03-15 21:48:29', '0', 0),
(15, 0, 'lewin', '2021-03-15 22:05:16', '1060', 4),
(16, 0, 'Test', '2021-03-15 23:42:34', '200', 4),
(17, 0, 'Test', '2021-03-19 11:45:22', '875', 11),
(18, 0, 'Test', '2021-03-19 13:04:07', '550', 1),
(19, 0, 'Test', '2021-03-19 17:14:58', '1400', 4),
(20, 0, 'Lewin', '2021-03-21 22:59:08', '0', 8),
(21, 0, 'Lewin', '2021-03-21 23:33:12', '50', 2);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user`
--

CREATE TABLE `user` (
  `userID` int(64) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  `isAdmin` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `user`
--

INSERT INTO `user` (`userID`, `username`, `password`, `salt`, `isAdmin`) VALUES
(1, 'admin', 'eVGVxxSgWifCgcknPmTh6JnVIzD2nhcKXb4mCM3mGFdfcyOBX2emQNyM5nKQWse5DxiCq1VjKraPHA/z7JNSqw==', 'w1O1MGrH3wKO9EdSOY2ULK1a/X/3MMq/WuoH7hU1D8bxX4wzfSvYYpD/pWY+2kYF4bh18rXDBV/xFdfkcetLLg==', 1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `word`
--

CREATE TABLE `word` (
  `wordID` int(11) NOT NULL,
  `word` varchar(255) NOT NULL,
  `categoryID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `word`
--

INSERT INTO `word` (`wordID`, `word`, `categoryID`) VALUES
(10, 'Government', 2),
(11, 'Presidency', 2),
(12, 'Concert', 3),
(13, 'Birthday', 3),
(14, 'Windows', 5),
(15, 'Stock Market', 6),
(16, 'Pan', 7),
(17, 'Sigmund Freud', 9),
(18, 'Ferrari', 10),
(19, 'Rims', 10),
(20, 'Yoga', 11),
(21, 'Meditation', 11),
(22, 'Moon', 13),
(23, 'Space', 13);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`categoryID`);

--
-- Indizes für die Tabelle `question`
--
ALTER TABLE `question`
  ADD PRIMARY KEY (`questionID`),
  ADD KEY `categoryID` (`categoryID`);

--
-- Indizes für die Tabelle `score`
--
ALTER TABLE `score`
  ADD PRIMARY KEY (`scoreID`);

--
-- Indizes für die Tabelle `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userID`);

--
-- Indizes für die Tabelle `word`
--
ALTER TABLE `word`
  ADD PRIMARY KEY (`wordID`),
  ADD KEY `categoryID` (`categoryID`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `category`
--
ALTER TABLE `category`
  MODIFY `categoryID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT für Tabelle `question`
--
ALTER TABLE `question`
  MODIFY `questionID` int(64) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
--
-- AUTO_INCREMENT für Tabelle `score`
--
ALTER TABLE `score`
  MODIFY `scoreID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;
--
-- AUTO_INCREMENT für Tabelle `user`
--
ALTER TABLE `user`
  MODIFY `userID` int(64) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `word`
--
ALTER TABLE `word`
  MODIFY `wordID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `question`
--
ALTER TABLE `question`
  ADD CONSTRAINT `question_ibfk_1` FOREIGN KEY (`categoryID`) REFERENCES `category` (`categoryID`);

--
-- Constraints der Tabelle `word`
--
ALTER TABLE `word`
  ADD CONSTRAINT `word_ibfk_1` FOREIGN KEY (`categoryID`) REFERENCES `category` (`categoryID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
