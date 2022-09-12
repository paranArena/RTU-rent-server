insert into authority (authority_name) values('ROLE_ADMIN');
insert into authority (authority_name) values('ROLE_USER');
-- 1번 member: 어드민 권한이 있음
insert into member (member_id, created_at, updated_at, activated, email, major, name, password, phone_number, student_id) values (1, '2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159', 1, 'admin', '사이버보안학과', '관리자','$2a$10$/6SvqR/zW3xy8GhQMTw4FuBpTCZ/bZyvZYaomhxLwenOqmBm1K5rq', '01012341234', '202020666');
-- 2번 member:  club.1 owner
insert into member (member_id, created_at, updated_at, activated, email, major, name, password, phone_number, student_id) values (2, '2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159', 1, 'member1@ajou.ac.kr', '사이버보안학과', '멤버이','$2a$10$7KCYGatbRCroDFhS0swLJeWVKM.ThzVBYndY3GEn4XCpD/g.bFpce', '01092883430', '201820680');
-- 3번 member:club.2 owner
insert into member (member_id, created_at, updated_at, activated, email, major, name, password, phone_number, student_id) values (3, '2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159', 1, 'member2@ajou.ac.kr', '사이버보안학과', '멤버삼','$2a$10$7KCYGatbRCroDFhS0swLJeWVKM.ThzVBYndY3GEn4XCpD/g.bFpce', '01092883431', '201820681');
-- 4번 member: club.1 admin
insert into member (member_id, created_at, updated_at, activated, email, major, name, password, phone_number, student_id) values (4, '2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159', 1, 'member3@ajou.ac.kr', '사이버보안학과', '멤버사','$2a$10$7KCYGatbRCroDFhS0swLJeWVKM.ThzVBYndY3GEn4XCpD/g.bFpce', '01092883432', '201820682');
-- 5번 member: club.1 USER
insert into member (member_id, created_at, updated_at, activated, email, major, name, password, phone_number, student_id) values (5, '2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159', 1, 'member4@ajou.ac.kr', '사이버보안학과', '멤버오','$2a$10$7KCYGatbRCroDFhS0swLJeWVKM.ThzVBYndY3GEn4XCpD/g.bFpce', '01092883433', '201820683');
-- 6번 member: noting
insert into member (member_id, created_at, updated_at, activated, email, major, name, password, phone_number, student_id) values (6, '2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159', 1, 'member5@ajou.ac.kr', '사이버보안학과', '멤버육','$2a$10$7KCYGatbRCroDFhS0swLJeWVKM.ThzVBYndY3GEn4XCpD/g.bFpce', '01092883434', '201820684');


insert into member_authority (member_id, authority_name) values (1, 'ROLE_USER');
insert into member_authority (member_id, authority_name) values (1, 'ROLE_ADMIN');

insert into member_authority (member_id, authority_name) values (2, 'ROLE_USER');

insert into member_authority (member_id, authority_name) values (3, 'ROLE_USER');
insert into member_authority (member_id, authority_name) values (4, 'ROLE_USER');
insert into member_authority (member_id, authority_name) values (5, 'ROLE_USER');
insert into member_authority (member_id, authority_name) values (6, 'ROLE_USER');
-- 1번 club:
insert into club (club_id, created_at, updated_at, introduction,name,thumbnail_path) values(1,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',"2022년2학기 사이버 보안 학과 학생회","사보 학생회","www.test.com");
insert into club (club_id, created_at, updated_at, introduction,name,thumbnail_path) values(2,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',"2022년2학기 사이버 보안 학과 학생회2","사보 학생회2","www.test2.com");

insert into club_member (club_member_id,created_at,updated_at,role,club_id,member_id) values(1,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',"OWNER",1,2);
insert into club_member (club_member_id,created_at,updated_at,role,club_id,member_id) values(2,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',"ADMIN",1,4);
insert into club_member (club_member_id,created_at,updated_at,role,club_id,member_id) values(3,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',"USER",1,5);
insert into club_member (club_member_id,created_at,updated_at,role,club_id,member_id) values(4,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',"OWNER",2,3);

-- 1번 Product 1번 club에 속해있음.
insert into location (location_id,name,x,y)values(1,"testLocation",1,1);
insert into product (product_id,created_at,updated_at,category,caution,fifo_rental_period,image_path,name,price,reserve_rental_period,club_id,location_id) values(1,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',"전자기기","조심해서 다뤄주세요.",10,"www.image.com","노트북",1000000,10,1,1);
-- item 1,2,3 은 Product1번의 Item들이다.
insert into item (item_id,created_at,updated_at,numbering,rental_policy,product_id)values(1,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',1,1,1);
insert into item (item_id,created_at,updated_at,numbering,rental_policy,product_id)values(2,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',1,1,1);
insert into item (item_id,created_at,updated_at,numbering,rental_policy,product_id)values(3,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',1,1,1);

-- 2번 Product 2번 클럽에 속해있음.
insert into location (location_id,name,x,y)values(2,"testLocation",2,2);
insert into product (product_id,created_at,updated_at,category,caution,fifo_rental_period,image_path,name,price,reserve_rental_period,club_id,location_id) values(2,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',"전자기기","조심해서 다뤄주세요.",10,"www.image.com","노트북",1000000,10,2,2);
-- 4,5,6번 item은 product2번의 item들이다.
insert into item (item_id,created_at,updated_at,numbering,rental_policy,product_id)values(4,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',1,1,2);
insert into item (item_id,created_at,updated_at,numbering,rental_policy,product_id)values(5,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',1,1,2);
insert into item (item_id,created_at,updated_at,numbering,rental_policy,product_id)values(6,'2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159',1,1,2);
