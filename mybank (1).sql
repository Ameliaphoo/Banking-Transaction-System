-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 07, 2024 at 11:25 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mybank`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
  `account_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `account_number` varchar(100) NOT NULL,
  `account_type` varchar(100) NOT NULL,
  `balance` int(11) NOT NULL,
  `pin` int(11) NOT NULL,
  `status` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`account_id`, `user_id`, `account_number`, `account_type`, `balance`, `pin`, `status`) VALUES
(1, 1, '1234 4567 7890 1234', 'Deposit Account', 92866, 123456, 'Active'),
(2, 1, '0987 1234 6789 1222', 'Saving Account', 500000, 987265, 'Active'),
(3, 3, '1111 2222 3333 4444 ', 'Saving Account', 120000, 123456, 'suspended'),
(4, 3, '1234 5678 9012 1234', 'Saving Account', 120000, 987651, 'suspended'),
(5, 4, '1234 5678 9012 5555', 'Deposit Account', 80000, 987652, 'Active'),
(6, 8, '4567 8888 1212 3456', 'Deposit Account', 12300, 545454, 'Active'),
(7, 14, '3333 5678 9212 4449', 'Saving Account', 200000, 45321, 'Active'),
(8, 9, '7892 1124 6754 1345', 'Deposit Account', 400000, 676745, 'suspended'),
(9, 5, '4321 5643 9087 2222', 'Saving Account', 90800, 342121, 'Active'),
(10, 20, '0978 4187 9995 5138', 'Saving Account', 400000, 676745, 'Active'),
(11, 22, '4536 7892 1234 9849 ', 'Saving Account', 700000, 987265, 'Active'),
(12, 18, '3547 2674 9074 1111', 'Deposit account', 700000, 987265, 'Active'),
(13, 12, '0974 2245 1231 6565', 'Saving Account', 130000, 952323, 'Active'),
(14, 16, '8742 1234 6789 1256', 'Saving Account', 42000, 452311, 'Active'),
(15, 13, '7645 3210 3782 1155', 'Saving Account', 76000, 674222, 'Active'),
(16, 15, '6788 1111 8787 0009', 'Deposit Account', 78000, 123456, 'suspended'),
(17, 18, '7892 3334 8976 1456 ', 'Saving Account', 78000, 897654, 'Active'),
(18, 13, '4321 9998 6564 1341', 'Deposit Account', 60000, 676742, 'Active'),
(19, 16, '1462 7893 5673 1234', 'Deposit Account', 200000, 145678, 'Active'),
(20, 20, '9872 1283 4629 9383', 'Deposit Account', 67000, 145234, 'Active'),
(21, 4, '2635 1111 9387 7536', 'Deposit Account', 40000, 444444, 'Active'),
(22, 5, '3743 5678 1234 0773', 'Deposit Account', 900000, 444444, 'Active');

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `tran_id` int(11) NOT NULL,
  `account_id` int(11) DEFAULT NULL,
  `tran_type` varchar(100) DEFAULT NULL,
  `amount` varchar(100) DEFAULT NULL,
  `tran_date` date DEFAULT NULL,
  `bank` varchar(100) NOT NULL,
  `payee_address` varchar(100) NOT NULL,
  `received_name` varchar(100) NOT NULL,
  `status` varchar(100) NOT NULL,
  `Action` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`tran_id`, `account_id`, `tran_type`, `amount`, `tran_date`, `bank`, `payee_address`, `received_name`, `status`, `Action`) VALUES
(1, 2, 'Internal Transfer', '100000.00', '2024-02-02', 'My Bank', '0989 7876 4567 1234', 'Steven', 'Success', 'Transferred'),
(2, 3, 'External Transfer', '74000.00', '2024-01-09', 'AYA Bank', '4567 222 9999 7654', 'Soe Moe', 'Success', 'Received'),
(3, 1, 'External Transfer', '1200.0', '2024-06-10', 'KBZ Bank', '6789 234 1234 098', 'John', 'success', 'transferred'),
(4, 1, 'Internal Transfer', '34.0', '2024-06-10', 'My Bank', '4567 800840 99848', 'iue', 'success', 'transferred'),
(5, 1, 'External Transfer', '5600.0', '2024-06-14', 'AYA Bank', '098 7653 1111 7878', 'May', 'success', 'transferred'),
(6, 1, 'Internal Transfer', '8000.0', '2024-06-14', 'My Bank', '8909 4567 8901 2341', 'June', 'success', 'transferred');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `question` varchar(100) NOT NULL,
  `answer` varchar(100) NOT NULL,
  `date` date NOT NULL,
  `up_date` date NOT NULL,
  `user_type` varchar(100) DEFAULT NULL,
  `phone` varchar(100) NOT NULL,
  `address` varchar(100) NOT NULL,
  `birthday` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `email`, `username`, `password`, `question`, `answer`, `date`, `up_date`, `user_type`, `phone`, `address`, `birthday`) VALUES
(1, 'user@gmail.com', 'user', '5678user', 'What is your favourite sport?', 'football', '2024-06-02', '2024-06-05', 'user', '09987654321', 'Bogalay', '1998-06-03'),
(2, 'khinephoo3@gmail.com', 'Aye Myat', '123phoo', 'What is your favorite color?', 'green', '2024-06-01', '2024-06-01', 'admin', '09123456789', 'Yangon', '1996-04-16'),
(3, 'phoo@gmail.com', 'Phoo Phoo', '12345678', 'What is your favorite color?', 'red', '2024-06-07', '2024-06-07', 'user', '098765243636', 'Mandalay', '1982-06-10'),
(4, 'mgmg@gmail.com', 'Mg Mg', 'Asd123!@#', 'What is your favorite color?', 'blue', '2024-06-08', '2024-06-08', 'user', '09873656666', 'Yangon', '2005-06-21'),
(5, 'aungmyat@gmail.com', 'Aung Myat', 'aungmyat123', 'What is your school?', 'YUOE', '2024-07-24', '2024-07-24', 'user', '098276525242', 'Yangon', '1995-07-13'),
(6, 'moemoe@gmail.com', 'Moe Moe', 'moemoe123', 'What is your favorite food?', 'sushi', '2024-07-24', '2024-07-24', 'user', '095289248298', 'Mandalay', '1990-08-08'),
(7, 'kaungmyat@gmail.com', 'Kaung Myat', 'kaungmyat123', 'What is the name of your pet?', 'Kitty', '2024-07-24', '2024-07-24', 'user', '094372742892', 'Kamayut', '2002-07-24'),
(8, 'theingi@gmail.com', 'Theingi', 'theingi123', 'What is your favourite sport?', 'tennis', '2024-07-24', '2024-07-24', 'user', '093268462883', 'South Okkalapa', '1989-02-17'),
(9, 'kaythimyat@gmail.com', 'Kay Thi Myat', 'kaythimyat123', 'What is your favorite color?', 'Blue', '2024-07-24', '2024-07-24', 'user', '097257327827', 'North Okkalapa', '1982-10-19'),
(10, 'sumyat@gamil.com', 'Su Myat Aung', 'sumyat123', 'What is your favorite food?', 'fruit', '2024-07-24', '2024-07-24', 'user', '09656258267', 'Taungyi', '1996-09-25'),
(11, 'soemyint@gmail.com', 'Soe Myint', 'soemyint456', 'What is your favourite sport?', 'batminton', '2024-07-24', '2024-07-24', 'user', '09474683948', 'NgweSaung', '1993-08-12'),
(12, 'moesat@gmail.com', 'Moe Sat', 'moesat456', 'What is your favourite sport?', 'table tennis', '2024-07-24', '2024-07-24', 'user', '09576722873', 'Bagan', '1997-05-08'),
(13, 'kyiaung@gmail.com', 'Kyi Aung', 'kyiaung123', 'What is your favorite color?', 'grey', '2024-07-24', '2024-07-24', 'user', '098772748274', 'Yangon', '2001-12-09'),
(14, 'yinyinaung@gmail.com', 'Yin Yin Aung', 'yinyin123', 'What is your school?', 'BEHD1', '2024-07-24', '2024-07-24', 'user', '09826766356', 'Kalay', '1994-05-13'),
(15, 'thetmon@gmail.com', 'Thet Mon', 'thetmon123', 'What is your favorite color?', 'Brown', '2024-07-25', '2024-07-25', 'user', '09377282', 'Kamayut', '1987-12-30'),
(16, 'kaungkyaw@gamil.com', 'Kanung Kyaw', 'kaungkyaw123', 'What is your school?', 'UFL', '2024-07-25', '2024-07-25', 'user', '09782748724', 'Hlaing', '1977-02-17'),
(17, 'soemoe@gmail.com', 'Soe Moe', 'soemoe123', 'What is the name of your pet?', 'Gigi', '2024-07-25', '2024-07-25', 'user', '095327357263', 'Taung Gu', '1993-07-20'),
(18, 'myintmyat@gmail.com', 'Myint Myat', 'myintmyat345', 'What is your favourite sport?', 'football', '2024-07-25', '2024-07-25', 'user', '0936726428', 'Hlaing', '1991-06-25'),
(19, 'ayemyat@gmail.com', 'Aye Myat', 'ayemyat123', 'What is your favorite color?', 'Green', '2024-07-25', '2024-07-25', 'user', '0964874827', 'Bogalay', '1999-10-17'),
(20, 'laylay@gmail.com', 'Lay Lay', 'laylay123', 'What is your favorite food?', 'Sauce', '2024-07-25', '2024-07-25', 'user', '09225662764', 'Bago', '1992-04-30'),
(21, 'moesatt@gmail.com', 'Moe Satt', 'moesatt123', 'What is your favorite food?', 'Sweet', '2024-07-25', '2024-07-25', 'user', '0978724827', 'Bago', '1999-08-08'),
(22, 'toetoe@gmail.com', 'Toe Toe', 'toetoe123', 'What is the name of your pet?', 'mee mee', '2024-07-25', '2024-07-25', 'user', '0921345451', 'Daw Pone', '2000-07-14');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`account_id`),
  ADD KEY `user` (`user_id`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`tran_id`),
  ADD KEY `account_id` (`account_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accounts`
--
ALTER TABLE `accounts`
  MODIFY `account_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `tran_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `accounts`
--
ALTER TABLE `accounts`
  ADD CONSTRAINT `user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
