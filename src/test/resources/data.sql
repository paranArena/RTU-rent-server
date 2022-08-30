insert into member (member_id, created_at, updated_at, activated, email, major, name, password, phone_number, student_id) values (1, '2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159', 1, 'admin', '사이버보안학과', '관리자','$2a$10$/6SvqR/zW3xy8GhQMTw4FuBpTCZ/bZyvZYaomhxLwenOqmBm1K5rq', '01012341234', '202020666');
insert into member (member_id, created_at, updated_at, activated, email, major, name, password, phone_number, student_id) values (2, '2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159', 1, 'test@ajou.ac.kr', '사이버보안학과', '일반유저','$2a$10$2V242CwFDf1pOetNUqOp/.tO3vGZullDBgNt3ZcSQVgeFmxyYlNSu', '01056785678', '202020677');
insert into member (member_id, created_at, updated_at, activated, email, major, name, password, phone_number, student_id) values (3, '2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159', 1, 'lhccccc@ajou.ac.kr', '사이버보안학과', '이해찬','$2a$10$7KCYGatbRCroDFhS0swLJeWVKM.ThzVBYndY3GEn4XCpD/g.bFpce', '01092883434', '201820688');

insert into member_authority (member_id, authority_name) values (1, 'ROLE_USER');
insert into member_authority (member_id, authority_name) values (1, 'ROLE_ADMIN');

insert into member_authority (member_id, authority_name) values (2, 'ROLE_USER');

insert into member_authority (member_id, authority_name) values (3, 'ROLE_USER');
insert into member_authority (member_id, authority_name) values (3, 'ROLE_ADMIN');

insert into club (club_id, created_at, updated_at, introduction,name,thumbnail_path) values(1,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',"2022년2학기 사이버 보안 학과 학생회","사보 학생회","www.test.com");
insert into club_member (club_member_id,created_at,updated_at,role,club_id,member_id) values(1,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',"OWNER",1,3);

insert into location (location_id,x,y)values(1,1,1);
insert into product (product_id,created_at,updated_at,category,caution,fifo_rental_period,image_path,name,price,reserve_rental_period,club_id,location_id) values(1,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',"전자기기","조심해서 다뤄주세요.",10,"www.image.com","노트북",1000000,10,1,1);

insert into item (item_id,created_at,updated_at,numbering,rental_policy,product_id)values(1,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',1,1,1);
insert into item (item_id,created_at,updated_at,numbering,rental_policy,product_id)values(2,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',1,1,1);
insert into item (item_id,created_at,updated_at,numbering,rental_policy,product_id)values(3,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',1,1,1);