CREATE TABLE user(
	id INT PRIMARY KEY AUTO_INCREMENT,
	firstname VARCHAR(255),
	lastname VARCHAR(255),
	username VARCHAR(255) NOT NULL UNIQUE,
	gender TINYINT(1),
	date_of_birth DATE,
	address VARCHAR(511),
	zip VARCHAR(15),
	city VARCHAR(255),
	country VARCHAR(255),
	email VARCHAR(255) NOT NULL UNIQUE,
	salt VARCHAR(32) NOT NULL,
	password VARCHAR(45) NOT NULL
)

INSERT INTO user VALUES(1, 'Meva', 'Rakotondratsima', 'meva', 0, '1989-11-17', '76 rue des Mouettes', '76600', 'Le Havre', 'France', 'plip@plop.com', 'b7b7b7e7e9a4e639bcb0e9f6b2d5567a', '65932ea15759a926b02a7e99f17df2275aa45983');

CREATE TABLE permission(
	id INT PRIMARY KEY AUTO_INCREMENT,
	type VARCHAR(255) NOT NULL
)

INSERT INTO permission (id, type) VALUES (1, 'admin'), (2, 'work'), (3, 'read');

CREATE TABLE `group`(
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL
)

INSERT INTO `group` VALUES(1, 'Groupe de Meva 1');
INSERT INTO `group` VALUES(2, 'Groupe de Meva 2');

CREATE TABLE group_user(
	user_id INT NOT NULL references user(id),
	group_id INT NOT NULL references `group` (`id`),
	status VARCHAR(255),
	permission_id INT NOT NULL DEFAULT 3 references permission(id),
	PRIMARY KEY (user_id, group_id)
)

INSERT INTO group_user VALUES(1, 1, 'valide', 1);
INSERT INTO group_user VALUES(1, 2, 'valide', 1);

CREATE TABLE project(
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	server_url VARCHAR(255),
	group_id INT NOT NULL references `group` (`id`)
)

INSERT INTO project VALUES(1, 'Projet de demo 1', '127.0.0.1', 1);
INSERT INTO project VALUES(2, 'Projet de demo 2', '127.0.0.1', 2);

CREATE TABLE file(
	id INT PRIMARY KEY AUTO_INCREMENT,
	path VARCHAR(255) NOT NULL,
	project_id INT NOT NULL references project(id),
	parent INT DEFAULT NULL,
	is_dir TINYINT(1) DEFAULT 0
)
