UPDATE products
SET image_url = 'https://www.svgrepo.com/show/508699/landscape-placeholder.svg'
WHERE product_id IN (
    SELECT product_id
    FROM (
             SELECT product_id
             FROM products
             LIMIT 12
         ) AS temp
);