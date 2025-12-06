USE second_hand_book;

-- 插入新用户
INSERT INTO users (name, email, password) VALUES 
('Charlie', 'charlie@example.com', '123456'),
('David', 'david@example.com', '123456'),
('Eve', 'eve@example.com', '123456');

-- 插入新图书
INSERT INTO books (title, author, price, cover, description, seller_id, status, created_at) VALUES 
('三体全集', '刘慈欣', 68.00, '/picture/three-body.svg', '中国科幻基石，雨果奖作品', 1, 'onsale', DATE_SUB(NOW(), INTERVAL 5 DAY)),
('红楼梦', '曹雪芹', 45.50, '/picture/dream-red-mansion.svg', '中国古典四大名著之首', 2, 'onsale', DATE_SUB(NOW(), INTERVAL 3 DAY)),
('数学分析（上册）', '华东师范大学数学系', 38.00, '/picture/math-analysis.svg', '经典的数分教材，九成新', 3, 'onsale', DATE_SUB(NOW(), INTERVAL 2 DAY)),
('全球通史', '斯塔夫里阿诺斯', 88.00, '/picture/history-book.svg', '从史前史到21世纪', 3, 'onsale', DATE_SUB(NOW(), INTERVAL 1 DAY)),
('Java核心技术', 'Cay S. Horstmann', 99.00, '/picture/cs-book.svg', 'Java学习必读', 4, 'sold', DATE_SUB(NOW(), INTERVAL 10 DAY)),
('围城', '钱钟书', 25.00, '/picture/default-book.svg', '婚姻是一座围城', 4, 'onsale', NOW());

-- 插入一些订单
INSERT INTO orders (book_id, buyer_id, status, created_at) 
SELECT id, 5, 'completed', DATE_SUB(NOW(), INTERVAL 8 DAY) FROM books WHERE title = 'Java核心技术';

-- 插入评论
INSERT INTO comments (content, book_id, user_id, created_at) 
SELECT '这书成色怎么样？', id, 2, DATE_SUB(NOW(), INTERVAL 4 DAY) FROM books WHERE title = '三体全集';

INSERT INTO comments (content, book_id, user_id, created_at) 
SELECT '全新的吗？', id, 3, DATE_SUB(NOW(), INTERVAL 2 DAY) FROM books WHERE title = '三体全集';

INSERT INTO comments (content, book_id, user_id, created_at) 
SELECT '包邮吗亲？', id, 4, DATE_SUB(NOW(), INTERVAL 1 DAY) FROM books WHERE title = '红楼梦';

-- 插入举报
INSERT INTO reports (type, book_id, reporter_id, reason, status, created_at)
SELECT 'book', id, 2, '价格标错了太贵了', 'pending', NOW() FROM books WHERE title = '数学分析（上册）';
