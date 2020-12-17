

insert into orders (order_id,order_number,address,creation_date,last_modified_date,card_number)
values(1000,'O1000','delhi dwarka',sysdate(),sysdate(),'C1234567');

insert into order_items (item_id,item_name,quantity,price,order_id)
values(2000,'item-1',2,100.99,1000);
insert into order_items (item_id,item_name,quantity,price,order_id)
values(2001,'item-2',1,99.99,1000);



