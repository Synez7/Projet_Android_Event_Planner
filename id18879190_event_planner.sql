-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : jeu. 26 mai 2022 à 20:45
-- Version du serveur :  10.5.12-MariaDB
-- Version de PHP : 7.3.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `id18879190_event_planner`
--

-- --------------------------------------------------------

--
-- Structure de la table `bookings`
--

CREATE TABLE `bookings` (
  `event_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `nameParticipant` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `confirm` tinyint(1) NOT NULL,
  `confirmDate` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Déchargement des données de la table `bookings`
--

INSERT INTO `bookings` (`event_id`, `user_id`, `active`, `nameParticipant`, `confirm`, `confirmDate`) VALUES
(28, 14, 1, 'kdb', 0, '2022-05-24 10:50:11'),
(32, 14, 1, 'kdb', 0, '2022-05-24 10:56:55'),
(39, 27, 1, 'gh', 1, '2022-05-26 22:05:13'),
(39, 28, 1, 'zz', 1, '2022-05-26 22:12:51');

-- --------------------------------------------------------

--
-- Structure de la table `bookmarks`
--

CREATE TABLE `bookmarks` (
  `bookmark_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT current_timestamp(),
  `active` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `bookmarks`
--

INSERT INTO `bookmarks` (`bookmark_id`, `user_id`, `event_id`, `updated`, `active`) VALUES
(4, 1, 70, '2017-04-23 08:50:41', 0),
(5, 5, 70, '2017-04-24 14:27:34', 0),
(14, 1, 79, '2017-04-24 09:03:13', 0),
(19, 1, 39, '2017-04-25 11:53:22', 1),
(20, 1, 116, '2017-04-24 14:52:42', 0),
(22, 1, 125, '2017-04-24 12:52:52', 0),
(23, 5, 39, '2017-04-24 15:07:24', 1),
(28, 1, 132, '2017-04-25 00:40:25', 1),
(33, 7, 116, '2017-04-25 10:19:32', 0),
(34, 7, 74, '2017-04-25 08:30:41', 0),
(35, 7, 39, '2017-04-25 10:19:24', 1),
(36, 5, 170, '2017-04-25 09:21:15', 1),
(37, 7, 131, '2017-04-25 10:35:32', 1),
(38, 21, 175, '2017-04-25 11:14:31', 1),
(39, 1, 178, '2017-04-25 11:21:49', 1),
(40, 7, 132, '2017-04-25 11:25:17', 1),
(41, 1, 131, '2017-04-25 11:32:45', 1),
(44, 29, 39, '2022-05-05 13:48:27', 1),
(48, 30, 212, '2022-05-05 15:04:59', 1),
(49, 30, 217, '2022-05-05 23:19:38', 1),
(50, 14, 217, '2022-05-06 21:16:04', 0),
(52, 15, 301, '2022-05-09 22:55:34', 1),
(53, 15, 298, '2022-05-09 23:00:49', 1),
(54, 15, 289, '2022-05-10 23:08:47', 0),
(57, 16, 28, '2022-05-19 19:40:38', 1),
(58, 16, 36, '2022-05-19 19:41:21', 1),
(59, 14, 28, '2022-05-24 13:20:15', 0),
(62, 17, 39, '2022-05-23 22:51:23', 0),
(63, 14, 39, '2022-05-26 17:11:55', 0),
(64, 14, 32, '2022-05-24 13:14:24', 0),
(65, 14, 30, '2022-05-24 13:23:33', 0),
(66, 14, 45, '2022-05-24 13:23:50', 0),
(68, 22, 28, '2022-05-24 19:20:20', 0),
(69, 14, 52, '2022-05-26 17:11:45', 0),
(71, 24, 39, '2022-05-26 17:38:58', 0),
(72, 25, 39, '2022-05-26 19:57:55', 0),
(74, 26, 51, '2022-05-26 20:00:14', 0),
(75, 27, 51, '2022-05-26 20:04:23', 0),
(76, 28, 51, '2022-05-26 20:11:57', 0);

-- --------------------------------------------------------

--
-- Structure de la table `category`
--

CREATE TABLE `category` (
  `category_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `category`
--

INSERT INTO `category` (`category_id`, `name`) VALUES
(0, 'All'),
(1, 'Cultural'),
(4, 'Seminar'),
(3, 'Sports'),
(2, 'Technical');

-- --------------------------------------------------------

--

--
-- --------------------------------------------------------

--
-- Structure de la table `events3`
--

CREATE TABLE `events3` (
  `event_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT current_timestamp(),
  `category_id` int(11) NOT NULL,
  `venue` varchar(255) NOT NULL,
  `time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `details` varchar(1000) NOT NULL,
  `image` varchar(100) NOT NULL,
  `time_end` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `price` int(11) NOT NULL,
  `attendance` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `events3`
--

INSERT INTO `events3` (`event_id`, `name`, `user_id`, `updated`, `category_id`, `venue`, `time`, `details`, `image`, `time_end`, `price`, `attendance`) VALUES
(28, 'PICEVT,iojioj', 15, '2022-05-11 22:53:01', 1, 'NYBABEooijiojioi', '2022-05-26 00:44:00', 'uhuihih', 'content://media/external/images/media/21', '2022-05-31 00:44:00', 78, 0),
(29, 'PICEVT,iojioj', 15, '2022-05-11 22:53:20', 1, 'brooklyn', '2022-05-26 00:44:00', 'uhuihih', 'content://media/external/images/media/21', '2022-05-31 00:44:00', 78, 0),
(30, 'PICEVTHOL', 15, '2022-05-11 22:53:32', 1, 'brooklyn', '2022-05-26 00:44:00', 'uhuihih', 'content://media/external/images/media/21', '2022-05-31 00:44:00', 78, 0),
(31, 'PICEVTHOL', 15, '2022-05-11 22:53:51', 1, 'brooklyn', '2022-05-26 00:44:00', 'uhuihih', 'content://media/external/images/media/21', '2022-05-31 00:44:00', 78, 0),
(32, 'LASS', 15, '2022-05-11 22:54:41', 1, 'WASHINGTON', '2022-05-20 00:54:00', 'NOTHING.', 'content://media/external/images/media/21', '2022-05-31 00:54:00', 678, 0),
(33, 'GOOD MAN', 16, '2022-05-18 17:18:17', 1, 'NY', '2022-05-19 19:17:00', 'huihuihuih', 'content://media/external/images/media/23', '2022-05-27 19:17:00', 800, 0),
(34, 'EVENT PSG', 16, '2022-05-18 18:05:33', 1, 'PARIS', '2022-05-19 20:04:00', 'PSG GAME VS METZ', 'content://media/external/images/media/39', '2022-05-20 20:04:00', 120, 0),
(35, 'RED EVENT', 16, '2022-05-18 18:49:28', 1, 'LONDON', '2022-05-19 20:48:00', 'hihiohiohiohiohio', 'content://media/external/images/media/23', '2022-05-26 20:48:00', 90, 0),
(36, 'MY MAN', 16, '2022-05-19 10:56:22', 4, 'PARIS', '2022-05-20 12:55:00', 'nothing man...', 'content://media/external/images/media/34', '2022-05-28 12:55:00', 900, 0),
(39, 'VIWINE BROS', 17, '2022-05-19 22:42:55', 1, 'NY', '2022-05-28 00:42:00', 'cdcvrv', 'content://media/external/images/media/36', '2022-05-31 00:42:00', 97, 13),
(45, 'FADA', 17, '2022-05-23 23:31:29', 1, 'Lille', '2022-05-25 00:51:00', 'test', 'content://media/external/images/media/20', '2022-05-26 00:51:00', 90, 1),
(46, 'GRID EVT', 17, '2022-05-24 20:53:28', 1, 'New York', '2022-05-26 22:52:00', 'Nothing', 'content://media/external/images/media/20', '2022-05-27 22:52:00', 99, 2),
(47, 'Test evt', 17, '2022-05-25 17:01:07', 1, 'Mtp', '2022-05-25 19:05:00', 'let me see !!!', 'content://media/external/images/media/20', '2022-05-25 20:00:00', 12, 2),
(48, 'RT', 17, '2022-05-25 17:02:30', 1, 'TZ', '2022-05-26 19:02:00', 'addzfrf', 'content://media/external/images/media/21', '2022-05-27 19:02:00', 12, 12),
(49, 'THATS ME', 17, '2022-05-25 17:04:26', 1, 'ddzd', '2022-05-26 19:03:00', 'dedrfrftv', 'content://media/external/images/media/20', '2022-05-28 19:03:00', 12, 12),
(51, 'test 0', 17, '2022-05-25 19:14:14', 1, 'mtp', '2022-05-26 21:13:00', 'udhuhfuihfui', 'content://media/external/images/media/20', '2022-05-28 21:13:00', 900, 0),
(52, 'gt', 17, '2022-05-26 14:37:54', 1, 'venize', '2022-05-27 16:37:00', 'kkoiojio', 'content://media/external/images/media/22', '2022-05-28 16:37:00', 900, 3),
(54, 'frftgtg', 17, '2022-05-26 17:44:16', 1, 'yuyugyu', '2022-05-19 19:43:00', 'iguihuhiohiohioh', 'content://media/external/images/media/20', '2022-05-25 19:43:00', 12, 12),
(55, 'event demo', 17, '2022-05-26 20:02:33', 3, 'London', '2022-05-27 22:01:00', 'Event demo', 'content://media/external/images/media/433784', '2022-05-29 22:01:00', 123, 5);

-- --------------------------------------------------------

--
-- Structure de la table `fcm`
--

CREATE TABLE `fcm` (
  `fcm_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `token` varchar(400) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `fcm`
--

INSERT INTO `fcm` (`fcm_id`, `user_id`, `token`, `updated`) VALUES
(8, 14, 'dAlfT0mYRGuoI2anV1ZDM3:APA91bERKXESd43te7KkuDMV8goenP9QzLnyLkaib77QwkBLadF0czYSHj5SdC1jNpQVtoU7E1TCPrmT12d6p_ToSj8WTypura2tZdcbFPNV7pi9LX_Epxc0Sp2fUWyebFS--wnj0hRZ', '2022-05-26 19:59:10'),
(21, 17, 'token', '2022-05-26 20:44:30'),
(22, 24, 'dtKA8DwkQHCkeGc1qE_FMY:APA91bFHBByAOh7USp0m0_8S1FRuOmdcupu7O-8WGFui0PBTjXMFyjJNAly-ZJthS2yqZGi6zGbl2vJyfJbLXWwEMIIjoaO2ghQpTk7yxzu5rT266iFSQ-h-8_cBlpQLgPLtibN1vsVN', '2022-05-26 17:37:27'),
(23, 25, 'dAlfT0mYRGuoI2anV1ZDM3:APA91bERKXESd43te7KkuDMV8goenP9QzLnyLkaib77QwkBLadF0czYSHj5SdC1jNpQVtoU7E1TCPrmT12d6p_ToSj8WTypura2tZdcbFPNV7pi9LX_Epxc0Sp2fUWyebFS--wnj0hRZ', '2022-05-26 19:59:24'),
(24, 26, 'dAlfT0mYRGuoI2anV1ZDM3:APA91bERKXESd43te7KkuDMV8goenP9QzLnyLkaib77QwkBLadF0czYSHj5SdC1jNpQVtoU7E1TCPrmT12d6p_ToSj8WTypura2tZdcbFPNV7pi9LX_Epxc0Sp2fUWyebFS--wnj0hRZ', '2022-05-26 19:59:57'),
(25, 27, 'dAlfT0mYRGuoI2anV1ZDM3:APA91bERKXESd43te7KkuDMV8goenP9QzLnyLkaib77QwkBLadF0czYSHj5SdC1jNpQVtoU7E1TCPrmT12d6p_ToSj8WTypura2tZdcbFPNV7pi9LX_Epxc0Sp2fUWyebFS--wnj0hRZ', '2022-05-26 20:04:00'),
(26, 28, 'dAlfT0mYRGuoI2anV1ZDM3:APA91bERKXESd43te7KkuDMV8goenP9QzLnyLkaib77QwkBLadF0czYSHj5SdC1jNpQVtoU7E1TCPrmT12d6p_ToSj8WTypura2tZdcbFPNV7pi9LX_Epxc0Sp2fUWyebFS--wnj0hRZ', '2022-05-26 20:11:45');

-- --------------------------------------------------------

--
-- Structure de la table `Image`
--

CREATE TABLE `Image` (
  `idImage` int(11) NOT NULL,
  `image_path` varchar(100) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Déchargement des données de la table `Image`
--

INSERT INTO `Image` (`idImage`, `image_path`) VALUES
(17, 'shadi/upload/content://media/external/images/media/26.jpg'),
(18, 'shadi/upload/bullsjit.jpg'),
(19, 'shadi/upload/content://media/external/images/media/26.jpg'),
(20, 'shadi/upload/content://media/external/images/media/26.jpg'),
(21, 'upload/content://media/external/images/media/26.jpg'),
(22, 'content://media/external/images/media/26'),
(23, 'content://media/external/images/media/26'),
(24, 'content://media/external/images/media/13'),
(25, 'content://media/external/images/media/26'),
(26, ''),
(27, 'content://media/external/images/media/26'),
(28, ''),
(29, ''),
(30, ''),
(31, 'content://media/external/images/media/26'),
(32, ''),
(33, 'content://media/external/images/media/26'),
(34, 'content://media/external/images/media/26'),
(35, 'content://media/external/images/media/26'),
(36, 'content://media/external/images/media/26'),
(37, 'content://media/external/images/media/26'),
(38, 'content://media/external/images/media/13'),
(39, 'content://media/external/images/media/26');

-- --------------------------------------------------------


-- --------------------------------------------------------

--
-- Structure de la table `UserComplet`
--

CREATE TABLE `UserComplet` (
  `id` int(11) NOT NULL,
  `email` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `pseudo` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `motdepasse` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `usertype_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Déchargement des données de la table `UserComplet`
--

INSERT INTO `UserComplet` (`id`, `email`, `pseudo`, `motdepasse`, `usertype_id`) VALUES
(1, 'd@gmail.com', 'toto', 'a', 0),
(2, 'a@gmail.com', 'user99', 'a', 0),
(12, 'j@gmail.com', 'totouihuihuih', 'a', 0),
(13, 'test@gmail.com', 'boss', 'a', 0),
(14, 'zozo@gmail.com', 'kdb', 'a', 2),
(15, 'az@gmail.com', 'patrick', 'a', 1),
(16, 'ed@hotmail.com', 'bref', 'bref', 1),
(17, 'dr@gmail.com', 'polo', 'polo', 1),
(19, 'er@gmail.com', 'er', 'er', 2),
(20, 'aaa@gmail.com', 'aaa', 'aaa', 2),
(22, 'df@gmail.com', 'df', 'df', 2),
(23, 'a@a.a', 'a', 'a', 1),
(24, 'dozo@gmail.com', 'dozo', 'dozo', 2),
(25, 'aza@gmail.com', 'aza', 'aza', 2),
(26, 'dg@hotmail.com', 'dg', 'dg', 2),
(27, 'gh@gmail.com', 'gh', 'gh', 2),
(28, 'zz@gmail.com', 'zz', 'zza', 2);

-- --------------------------------------------------------

--------------------------------------

--
-- Structure de la table `usertype2`
--

CREATE TABLE `usertype2` (
  `usertype_id` int(11) NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Déchargement des données de la table `usertype2`
--

INSERT INTO `usertype2` (`usertype_id`, `name`) VALUES
(1, 'Provider'),
(2, 'User');

-- --------------------------------------------------------


--



--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`event_id`,`user_id`),
  ADD KEY `FK_USER_BOOK` (`user_id`);

--
-- Index pour la table `bookmarks`
--
ALTER TABLE `bookmarks`
  ADD PRIMARY KEY (`bookmark_id`),
  ADD UNIQUE KEY `bookmark_id` (`bookmark_id`);

--
-- Index pour la table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`category_id`),
  ADD UNIQUE KEY `name` (`name`);


--
--
-- Index pour la table `events3`
--
ALTER TABLE `events3`
  ADD PRIMARY KEY (`event_id`),
  ADD UNIQUE KEY `event_id` (`event_id`);

--
-- Index pour la table `fcm`
--
ALTER TABLE `fcm`
  ADD PRIMARY KEY (`fcm_id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- Index pour la table `Image`
--
ALTER TABLE `Image`
  ADD PRIMARY KEY (`idImage`);


--
-- Index pour la table `UserComplet`
--
ALTER TABLE `UserComplet`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `usertype2`
--
ALTER TABLE `usertype2`
  ADD PRIMARY KEY (`usertype_id`);



--
-- AUTO_INCREMENT pour la table `bookmarks`
--
ALTER TABLE `bookmarks`
  MODIFY `bookmark_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=77;


--
-- AUTO_INCREMENT pour la table `events3`
--
ALTER TABLE `events3`
  MODIFY `event_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=56;

--
-- AUTO_INCREMENT pour la table `fcm`
--
ALTER TABLE `fcm`
  MODIFY `fcm_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT pour la table `Image`
--
ALTER TABLE `Image`
  MODIFY `idImage` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;


--
-- AUTO_INCREMENT pour la table `UserComplet`
--
ALTER TABLE `UserComplet`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;


--
-- AUTO_INCREMENT pour la table `usertype2`
--
ALTER TABLE `usertype2`
  MODIFY `usertype_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;


--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `FK_EVENT_BOOK` FOREIGN KEY (`event_id`) REFERENCES `events3` (`event_id`),
  ADD CONSTRAINT `FK_USER_BOOK` FOREIGN KEY (`user_id`) REFERENCES `UserComplet` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
