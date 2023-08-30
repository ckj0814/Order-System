use
`ruyuan_eshop_order`;
-- order_info
ALTER TABLE order_info DROP INDEX idx_business_identifier;
ALTER TABLE order_info DROP INDEX idx_gmt_create;
ALTER TABLE order_info DROP INDEX idx_parent_order_id;
ALTER TABLE order_info DROP INDEX idx_order_status;
ALTER TABLE order_info DROP INDEX idx_order_type;
ALTER TABLE order_info DROP INDEX idx_pay_amount;
ALTER TABLE order_info DROP INDEX idx_seller_id;
ALTER TABLE order_info DROP INDEX idx_user_id;

-- order_item
ALTER TABLE order_item DROP INDEX idx_product_name;
ALTER TABLE order_item DROP INDEX idx_seller_id;
ALTER TABLE order_item DROP INDEX idx_sku_code;

-- order_payment_detail
ALTER TABLE order_payment_detail DROP INDEX idx_out_trade_no;
ALTER TABLE order_payment_detail DROP INDEX idx_pay_status;
ALTER TABLE order_payment_detail DROP INDEX idx_pay_type;

-- order_delivery_detail
ALTER TABLE order_delivery_detail DROP INDEX idx_delivery_name;
ALTER TABLE order_delivery_detail DROP INDEX idx_order_id;
ALTER TABLE order_delivery_detail DROP INDEX idx_receiver_phone;