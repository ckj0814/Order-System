use
`ruyuan_eshop_order`;
-- order_info
create
index idx_business_identifier
    on order_info (business_identifier);

create
index idx_gmt_create
    on order_info (gmt_create);

create
index idx_parent_order_id
    on order_info (parent_order_id);

create
index idx_order_status
    on order_info (order_status);

create
index idx_order_type
    on order_info (order_type);

create
index idx_pay_amount
    on order_info (pay_amount);

create
index idx_seller_id
    on order_info (seller_id);

create
index idx_user_id
    on order_info (user_id);

-- order_item
create
index idx_product_name
    on order_item (product_name);

create
index idx_seller_id
    on order_item (seller_id);

create
index idx_sku_code
    on order_item (sku_code);

-- order_payment_detail
create
index idx_out_trade_no
    on order_payment_detail (out_trade_no);

create
index idx_pay_status
    on order_payment_detail (pay_status);

create
index idx_pay_type
    on order_payment_detail (pay_type);

-- order_delivery_detail
create
index idx_delivery_name
    on order_delivery_detail (receiver_name);

create
index idx_order_id
    on order_delivery_detail (order_id);

create
index idx_receiver_phone
    on order_delivery_detail (receiver_phone);