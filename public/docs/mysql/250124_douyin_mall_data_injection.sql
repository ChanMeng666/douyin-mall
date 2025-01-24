-- Users data
INSERT INTO users (username, password, email, phone, status) VALUES
                                                                 ('john_doe', 'hashed_password_1', 'john.doe@gmail.com', '13800138000', 'active'),
                                                                 ('jane_smith', 'hashed_password_2', 'jane.smith@hotmail.com', '13900139000', 'active'),
                                                                 ('bob_wilson', 'hashed_password_3', 'bob.wilson@outlook.com', '13700137000', 'inactive'),
                                                                 ('alice_brown', 'hashed_password_4', 'alice.brown@yahoo.com', '13600136000', 'active'),
                                                                 ('charlie_davis', 'hashed_password_5', 'charlie.davis@gmail.com', '13500135000', 'active');

-- Products data
INSERT INTO products (name, description, price, stock, status) VALUES
                                                                   ('iPhone 14', '最新款苹果手机，支持5G网络', 5999.99, 100, 'active'),
                                                                   ('Nike运动鞋', '专业跑步鞋，舒适透气', 599.99, 200, 'active'),
                                                                   ('华为平板', '10.8英寸大屏，长续航', 2999.99, 50, 'active'),
                                                                   ('小米手环', '智能监测，防水设计', 199.99, 300, 'active'),
                                                                   ('MacBook Air', 'M2芯片，轻薄便携', 7999.99, 80, 'active'),
                                                                   ('AirPods Pro', '主动降噪，空间音频', 1799.99, 150, 'active'),
                                                                   ('机械键盘', '青轴机械键盘，RGB背光', 299.99, 100, 'active'),
                                                                   ('电竞椅', '人体工学，可调节靠背', 899.99, 30, 'active');
-- 初次进行前端验证时注入的数据
INSERT INTO products (name, description, price, stock) VALUES
                                                           ('iPhone 15', 'Latest iPhone model', 999.99, 100),
                                                           ('MacBook Pro', '16-inch laptop', 1999.99, 50),
                                                           ('AirPods Pro', 'Wireless earbuds', 249.99, 200);



-- Carts data
INSERT INTO carts (user_id) VALUES
                                (1),
                                (2),
                                (3),
                                (4),
                                (5);

-- Cart items data
INSERT INTO cart_items (cart_id, product_id, quantity, total_price) VALUES
                                                                        (1, 1, 1, 5999.99),
                                                                        (1, 3, 2, 5999.98),
                                                                        (2, 2, 1, 599.99),
                                                                        (3, 4, 3, 599.97),
                                                                        (4, 5, 1, 7999.99);

-- Orders data
INSERT INTO orders (user_id, total_amount, status, pay_type, pay_time) VALUES
                                                                           (1, 5999.99, 'paid', 1, '2025-01-23 14:30:00'),
                                                                           (2, 599.99, 'pending', 2, NULL),
                                                                           (3, 199.99, 'cancelled', 1, NULL),
                                                                           (4, 7999.99, 'paid', 3, '2025-01-23 16:45:00'),
                                                                           (1, 1799.99, 'paid', 2, '2025-01-23 18:20:00');

-- Order items data
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
                                                                    (1, 1, 1, 5999.99),
                                                                    (2, 2, 1, 599.99),
                                                                    (3, 4, 1, 199.99),
                                                                    (4, 5, 1, 7999.99),
                                                                    (5, 6, 1, 1799.99);

-- Settlements data
INSERT INTO settlements (order_id, amount, status) VALUES
                                                       (1, 5999.99, 'settled'),
                                                       (2, 599.99, 'unsettled'),
                                                       (4, 7999.99, 'settled'),
                                                       (5, 1799.99, 'settled');

-- Payments data
INSERT INTO payments (order_id, amount, status) VALUES
                                                    (1, 5999.99, 'paid'),
                                                    (3, 199.99, 'cancelled'),
                                                    (4, 7999.99, 'paid'),
                                                    (5, 1799.99, 'paid');

-- Order queries data
INSERT INTO order_queries (user_id, query, response) VALUES
                                                         (1, '我的订单什么时候发货？', '您的订单#1已于2025-01-23支付成功，预计48小时内发货'),
                                                         (2, '如何取消订单？', '您好，您可以在订单未发货前申请取消。进入订单详情页面，点击"取消订单"按钮即可。'),
                                                         (3, '订单退款多久到账？', '订单退款将在3-5个工作日内退回您的支付账户'),
                                                         (4, '能修改收货地址吗？', '抱歉，订单已发货，无法修改收货地址。建议您联系快递公司前往原地址取件。');