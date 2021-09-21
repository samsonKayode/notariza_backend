
CREATE TABLE IF NOT EXISTS logging_event
  (
    timestmp         BIGINT NOT NULL,
    formatted_message  TEXT NOT NULL,
    logger_name       VARCHAR(254) NOT NULL,
    level_string      VARCHAR(254) NOT NULL,
    thread_name       VARCHAR(254),
    reference_flag    SMALLINT,
    arg0              VARCHAR(254),
    arg1              VARCHAR(254),
    arg2              VARCHAR(254),
    arg3              VARCHAR(254),
    caller_filename   VARCHAR(254) NOT NULL,
    caller_class      VARCHAR(254) NOT NULL,
    caller_method     VARCHAR(254) NOT NULL,
    caller_line       CHAR(4) NOT NULL,
    event_id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
  );

CREATE TABLE IF NOT EXISTS logging_event_property
  (
    event_id	      BIGINT NOT NULL,
    mapped_key        VARCHAR(254) NOT NULL,
    mapped_value      TEXT,
    PRIMARY KEY(event_id, mapped_key),
    FOREIGN KEY (event_id) REFERENCES logging_event(event_id)
  );

CREATE TABLE IF NOT EXISTS logging_event_exception
  (
    event_id         BIGINT NOT NULL,
    i                SMALLINT NOT NULL,
    trace_line       VARCHAR(254) NOT NULL,
    PRIMARY KEY(event_id, i),
    FOREIGN KEY (event_id) REFERENCES logging_event(event_id)
  );

  CREATE TABLE IF NOT EXISTS roles (
    role_id int NOT NULL AUTO_INCREMENT,
    name varchar(255) UNIQUE,
    PRIMARY KEY (role_id)
  );

  CREATE TABLE IF NOT EXISTS service_cost_table (
    id int NOT NULL AUTO_INCREMENT,
    age_declaration int DEFAULT NULL,
    change_of_name int DEFAULT NULL,
    gift_deed int DEFAULT NULL,
    missing_item int DEFAULT NULL,
    notarize_document_per_page int DEFAULT NULL,
    power_of_attorney int DEFAULT NULL,
    user_type varchar(255) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_tlc6gafygkbey4wf1kxuqg17i (user_type)
  );


    insert IGNORE into roles (name) values('SYSADMIN');
    insert IGNORE into roles (name) values('GENERAL USER');
    insert IGNORE into service_cost_table(age_declaration, change_of_name, gift_deed, missing_item, notarize_document_per_page, power_of_attorney, user_type) values('10000','10000','50000','10000','20000','100000','GENERAL USER');

