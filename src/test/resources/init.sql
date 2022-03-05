CREATE TABLE MY_ORDER
(
    ID              INT unsigned AUTO_INCREMENT PRIMARY KEY,
    order_number    VARCHAR(255)                                                       NOT NULL,
    total_amount    DECIMAL(8, 2)                                                      NOT NULL,
    order_datetime  DATETIME                                                           NOT NULL,
    create_datetime DATETIME     DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    update_datetime DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    VERSION         INT unsigned DEFAULT 0                                             NOT NULL
);
