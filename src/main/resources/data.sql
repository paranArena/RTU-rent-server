insert into member (member_id, created_at, updated_at, activated, email, major, name, password, phone_number, student_id) values (null, '2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159', 1, 'admin', '사이버보안학과', '관리자','$2a$10$/6SvqR/zW3xy8GhQMTw4FuBpTCZ/bZyvZYaomhxLwenOqmBm1K5rq', '01012341234', '202020666');
insert into member (member_id, created_at, updated_at, activated, email, major, name, password, phone_number, student_id) values (null, '2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159', 1, 'test@ajou.ac.kr', '사이버보안학과', '일반유저','$2a$10$2V242CwFDf1pOetNUqOp/.tO3vGZullDBgNt3ZcSQVgeFmxyYlNSu', '01056785678', '202020677');
insert into member (member_id, created_at, updated_at, activated, email, major, name, password, phone_number, student_id) values (null, '2022-08-15 01:58:19.540159', '2022-08-15 01:58:19.540159', 1, 'lhccccc@ajou.ac.kr', '사이버보안학과', '이해찬','$2a$10$7KCYGatbRCroDFhS0swLJeWVKM.ThzVBYndY3GEn4XCpD/g.bFpce', '01092883434', '201820688');

insert into authority (authority_name) values ('ROLE_USER');
insert into authority (authority_name) values ('ROLE_ADMIN');

insert into member_authority (member_id, authority_name) values (1, 'ROLE_USER');
insert into member_authority (member_id, authority_name) values (1, 'ROLE_ADMIN');

insert into member_authority (member_id, authority_name) values (2, 'ROLE_USER');

insert into member_authority (member_id, authority_name) values (3, 'ROLE_USER');
insert into member_authority (member_id, authority_name) values (3, 'ROLE_ADMIN');
