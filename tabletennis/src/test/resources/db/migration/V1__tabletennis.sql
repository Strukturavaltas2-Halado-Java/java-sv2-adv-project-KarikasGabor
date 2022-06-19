CREATE TABLE IF NOT EXISTS organizations
(
    org_id      BIGINT AUTO_INCREMENT NOT NULL,
    org_name    VARCHAR(255)          NOT NULL,
    org_address VARCHAR(255)          NOT NULL,
    contact     VARCHAR(255)          NOT NULL,
    email       VARCHAR(255)          NOT NULL,
    tel_number  VARCHAR(255)          NULL,
    CONSTRAINT pk_organizations PRIMARY KEY (org_id)
);

CREATE TABLE IF NOT EXISTS players
(
    player_id             BIGINT AUTO_INCREMENT NOT NULL,
    player_name           VARCHAR(255)          NOT NULL,
    player_birthdate      date                  NOT NULL,
    player_mother         VARCHAR(255)          NOT NULL,
    org_id                BIGINT                NULL,
    license_date          date                  NULL,
    license_validity_date date                  NULL,
    license_type          VARCHAR(255)          NULL,
    CONSTRAINT pk_players PRIMARY KEY (player_id)
);

ALTER TABLE players
    ADD CONSTRAINT FK_PLAYERS_ON_ORG FOREIGN KEY (org_id) REFERENCES organizations (org_id);