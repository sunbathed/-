CREATE DATABASE IF NOT EXISTS second_hand_book DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE second_hand_book;

CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE books (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  author VARCHAR(100) NOT NULL,
  price DECIMAL(8,2) NOT NULL,
  cover VARCHAR(500),
  description TEXT,
  seller_id BIGINT NOT NULL,
  status ENUM('onsale','sold') DEFAULT 'onsale',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_seller FOREIGN KEY (seller_id) REFERENCES users(id)
);

CREATE TABLE orders (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  book_id BIGINT NOT NULL,
  buyer_id BIGINT NOT NULL,
  status ENUM('pending','completed') DEFAULT 'pending',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES books(id),
  CONSTRAINT fk_buyer FOREIGN KEY (buyer_id) REFERENCES users(id)
);

-- 初始用户
INSERT INTO users (name,email,password) VALUES
('Alice','alice@example.com','123456'),
('Bob','bob@example.com','123456');

-- 初始图书
INSERT INTO books (title,author,price,cover,description,seller_id) VALUES
('深入理解计算机系统','Randal E.Bryant',59.90,'/picture/cs-book.svg','经典 CSAPP，适合进阶',1),
('算法导论','Thomas H.Cormen',79.00,'/picture/algo-book.svg','算法必修课',1),
('JavaScript 高级程序设计','Matt Frisbie',45.00,'/picture/js-book.svg','前端红宝书',2),
('大学物理','Physics Dept.',1.50,'/picture/physics-book.svg','物理系教材',2);