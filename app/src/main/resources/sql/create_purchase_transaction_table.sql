CREATE TABLE purchase_transaction (
    id bigint NOT NULL AUTO_INCREMENT,
    description varchar(50) NOT NULL,
    purchase_amount_usd decimal(38,2) NOT NULL,
    transaction_date date NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;
