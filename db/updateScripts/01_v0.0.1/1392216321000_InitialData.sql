--
-- Dumping data for table `role`
--

INSERT INTO `ROLE` (`ROLE_ID`, `ROLE_NAME`, `ROLE_DESCRIPTION`) VALUES
(1, 'ROLE_ADMIN', ''),
(2, 'ROLE_USER', '');

--
-- Dumping data for table `url_rule`
--

INSERT INTO `URL_RULE` (`URL_RULE_ID`, `URL`, `METHOD`) VALUES
(1, '/login', 'GET'),
(2, '/home', 'GET');

--
-- Dumping data for table `role_url_rule`
--

INSERT INTO `ROLE_URL_RULE` (`ROLE_ID`, `URL_RULE_ID`) VALUES
(1, 2);

--
-- Dumping data for table `users`
--

INSERT INTO `USERS` (`USER_ID`, `USER_NAME`,`FIRST_NAME`,`LAST_NAME`, `PASSWORD`, `ENABLED`, `ACCOUNT_NON_EXPIRED`, `ACCOUNT_NON_LOCKED`, `CREDENTIALS_NON_EXPIRED`) VALUES
(1, 'sansar','sansar','mor', 'ea9a7d3f4c7faf7f7d5297059b8ba3e0ae8d0145', b'1', b'1', b'1', b'1');

--
-- Dumping data for 'users_role'
--

INSERT INTO `USERS_ROLE` (`USER_ID`, `ROLE_ID`) VALUES
(1, 1);
