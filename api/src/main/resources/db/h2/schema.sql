DROP TABLE books IF EXISTS;
DROP TABLE categories IF EXISTS;

CREATE TABLE categories (
  id   BIGINT PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX categories_name ON categories (name);

CREATE TABLE books (
  id   BIGINT PRIMARY KEY,
  category_id INTEGER,
  author VARCHAR(80),
  title VARCHAR(80),
  rating DECIMAL(10, 2),
  price DECIMAL(10, 2),
  pages INTEGER,
  isbn10 VARCHAR(10),
  isbn13 VARCHAR(14),
  publisher VARCHAR(200),
  image VARCHAR(80),
  description VARCHAR(2000)
);
ALTER TABLE books ADD CONSTRAINT fk_category_books FOREIGN KEY (category_id) REFERENCES categories (id);