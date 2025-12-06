USE second_hand_book;

-- 1. Apply covers to existing books
UPDATE books SET cover = '/picture/three-body.jpg' WHERE title = '三体全集';
UPDATE books SET cover = '/picture/hundred-years-solitude.jpg' WHERE title = '百年孤独';
UPDATE books SET cover = '/picture/to-live.jpg' WHERE title = '活着';
UPDATE books SET cover = '/picture/miracles-of-namiya.jpg' WHERE title = '解忧杂货店';
UPDATE books SET cover = '/picture/kite-runner.jpg' WHERE title = '追风筝的人';
UPDATE books SET cover = '/picture/white-night.jpg' WHERE title = '白夜行';
UPDATE books SET cover = '/picture/ordinary-world.jpg' WHERE title = '平凡的世界';
UPDATE books SET cover = '/picture/design-of-everyday-things.jpg' WHERE title LIKE '%设计书%';
UPDATE books SET cover = '/picture/dont-make-me-think.jpg' WHERE title LIKE '%点石成金%';
UPDATE books SET cover = '/picture/history-of-art.jpg' WHERE title LIKE '%艺术的故事%';
UPDATE books SET cover = '/picture/about-face.jpg' WHERE title LIKE '%About Face%';
UPDATE books SET cover = '/picture/grid-systems.jpg' WHERE title LIKE '%网格系统%';
UPDATE books SET cover = '/picture/catcher-in-the-rye.jpg' WHERE title LIKE '%麦田里的守望者%';
UPDATE books SET cover = '/picture/1984.jpg' WHERE title LIKE '%围城%'; -- 近似替代，因为围城封面不好找
UPDATE books SET cover = '/picture/docker-deep-dive.jpg' WHERE title LIKE '%Docker%';
UPDATE books SET cover = '/picture/k8s-up-and-running.jpg' WHERE title LIKE '%Kubernetes%';
UPDATE books SET cover = '/picture/go-programming.jpg' WHERE title LIKE '%Go语言%';

-- 2. Clean up books without real covers (to keep total around 30)
-- We want to keep books that have a cover ending in .jpg
-- Delete books that still have .svg cover (generated ones) or default
-- BUT be careful with foreign keys.
-- First, delete related reports, comments, orders for the books we want to delete.

CREATE TEMPORARY TABLE books_to_delete AS
SELECT id FROM books WHERE cover LIKE '%.svg' OR cover LIKE '%default%';

-- Delete dependencies
DELETE FROM reports WHERE book_id IN (SELECT id FROM books_to_delete);
DELETE FROM comments WHERE book_id IN (SELECT id FROM books_to_delete);
DELETE FROM orders WHERE book_id IN (SELECT id FROM books_to_delete);

-- Delete the books
DELETE FROM books WHERE id IN (SELECT id FROM books_to_delete);

-- Drop temp table
DROP TEMPORARY TABLE books_to_delete;
