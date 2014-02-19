--
-- ALTER table `product`
--

ALTER TABLE `product` CHANGE `PRODUCT_ID` `ID` BIGINT( 20 ) NOT NULL AUTO_INCREMENT;
--
-- ALTER table 'category'
--

ALTER TABLE `category` CHANGE `CATEGORY_ID` `ID` BIGINT( 20 ) NOT NULL AUTO_INCREMENT;

--
-- ALTER table 'sub_category'
--

ALTER TABLE `sub_category` CHANGE `SUB_CATEGORY_ID` `ID` BIGINT( 20 ) NOT NULL AUTO_INCREMENT;
--
-- ALTER table 'product_category'
--

ALTER TABLE `product_category` CHANGE `PRODUCT_CATEGORY_ID` `ID` BIGINT( 20 ) NOT NULL AUTO_INCREMENT;

--
-- ALTER table 'users'
--

ALTER TABLE `users` CHANGE `USER_ID` `ID` BIGINT( 20 ) NOT NULL AUTO_INCREMENT;

--
-- ALTER table 'url_rule'
--

ALTER TABLE `url_rule` CHANGE `URL_RULE_ID` `ID` BIGINT( 20 ) NOT NULL AUTO_INCREMENT;

--
-- ALTER table 'role'
--

ALTER TABLE `role` CHANGE `ROLE_ID` `ID` BIGINT( 20 ) NOT NULL AUTO_INCREMENT;
