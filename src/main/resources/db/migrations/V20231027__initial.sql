CREATE TABLE IF NOT EXISTS `transactions` (
    `id` BINARY(16) NOT NULL PRIMARY KEY,
    `entity_id` varchar(256),
    `data_location` varchar(256),
    `created_by` varchar(256),
    `status` varchar(256),
    `entity_hash` varchar(256)
);


CREATE TABLE `revinfo`(
    `rev`      INTEGER PRIMARY KEY NOT NULL,
    `revtstmp` BIGINT
);

CREATE TABLE `transactions_aud` (
    `id` BINARY(16),
    `rev` INTEGER REFERENCES `revinfo` (`rev`),
    `revend` INTEGER REFERENCES `revinfo` (`rev`),
    `revtype` SMALLINT,
    `revend_tstmp` TIMESTAMP,
    `entity_id` varchar(256),
    `data_location` varchar(256),
    `created_by` varchar(256),
    `status` varchar(256),
    `entity_hash` varchar(256),
    PRIMARY KEY (`id`, `rev`)
);