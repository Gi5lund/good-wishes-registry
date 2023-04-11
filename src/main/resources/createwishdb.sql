create database wishlist;
use wishlist;
CREATE TABLE wish_user(
                          user_id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                          user_name varchar(100),
                          user_password varchar(100)
);

CREATE TABLE wish_list(
                          wish_list_id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                          wish_item_count int,
                          wish_list_name varchar(100),
                          occation varchar(100),
                          user_id int,
                          FOREIGN KEY (user_id) REFERENCES wish_user(user_id)
);

CREATE TABLE wish_list_items (
                                 wish_list_id int,
                                 item_line_id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                 item_name varchar(100),
                                 item_QTY int,
                                 item_description varchar(250),
                                 item_URL varchar(140),
                                 item_price decimal,
                                 item_reserved bit,
                                 item_reserved_by varchar(50),
                                 FOREIGN KEY (wish_list_id) REFERENCES wish_list(wish_list_id)
);