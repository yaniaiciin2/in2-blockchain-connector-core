CREATE TABLE IF NOT EXISTS `transactions` (
    `id` BINARY(16) NOT NULL PRIMARY KEY,
    `entityid` varchar(256),
    `datalocation` varchar(256),
    `createdby` varchar(256),
    `status` varchar(256),
    `entityhash` varchar(256)
);


CREATE TABLE `REVINFO`(
    `rev`      INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
    `revtstmp` BIGINT
);

CREATE TABLE `transactions_AUD` (
    `id` BINARY(16),
    `rev` INTEGER REFERENCES `REVINFO` (`rev`),
    `revend` INTEGER REFERENCES `REVINFO` (`rev`),
    `revtype` SMALLINT,
    `revend_tstmp` TIMESTAMP,
    `entityid` varchar(256),
    `datalocation` varchar(256),
    `createdby` varchar(256),
    `status` varchar(256),
    `entityhash` varchar(256),
    PRIMARY KEY (`id`, `rev`)
);