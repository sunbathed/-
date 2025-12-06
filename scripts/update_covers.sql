USE second_hand_book;

UPDATE books SET cover = '/picture/python-crash-course.jpg' WHERE title = 'Python编程：从入门到实践';
UPDATE books SET cover = '/picture/design-patterns.jpg' WHERE title = '设计模式：可复用面向对象软件的基础';
UPDATE books SET cover = '/picture/clean-code.jpg' WHERE title = 'Clean Code';
UPDATE books SET cover = '/picture/head-first-java.jpg' WHERE title = 'Head First Java';
UPDATE books SET cover = '/picture/intro-to-algo.jpg' WHERE title LIKE '%算法导论%';
UPDATE books SET cover = '/picture/pragmatic-programmer.jpg' WHERE title LIKE '%程序员修炼之道%';
UPDATE books SET cover = '/picture/mythical-man-month.jpg' WHERE title LIKE '%人月神话%';
UPDATE books SET cover = '/picture/art-of-computer-programming.jpg' WHERE title LIKE '%计算机程序设计艺术%';

-- Update some others to have variety even if not exact match
UPDATE books SET cover = '/picture/refactoring.jpg' WHERE title LIKE '%重构%';
