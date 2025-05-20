INSERT INTO users (email, password, username, role) VALUES ('test@test.com', '$2a$12$ON55BqV189E7stqfgYZvaO9JDhYsA5NmNNVpp4.QvZ5ldQ6G.G6de', 'test', 'user');
INSERT INTO users (email, password, username, role) VALUES ('test1@test.com', '$2a$12$ON55BqV189E7stqfgYZvaO9JDhYsA5NmNNVpp4.QvZ5ldQ6G.G6de', 'test1', 'user');

INSERT INTO question (id, text) VALUES (1, 'Question 1');
INSERT INTO question (id, text) VALUES (2, 'Question 2');
INSERT INTO question (id, text) VALUES (3, 'Question 3');
INSERT INTO question (id, text) VALUES (4, 'Question 5');
INSERT INTO question (id, text) VALUES (5, 'Question 5');

INSERT INTO answer (id, is_correct, text, question_id) VALUES (1, true, 'answer 1', 1);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (2, false, 'answer 2', 1);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (3, false, 'answer 3', 1);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (4, false, 'answer 4', 1);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (5, false, ' answer5', 1);

INSERT INTO answer (id, is_correct, text, question_id) VALUES (6, true, 'answer 1', 2);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (7, false, 'answer 2', 2);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (8, false, 'answer 3', 2);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (9, false, 'answer 4', 2);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (10, false, ' answer5', 2);

INSERT INTO answer (id, is_correct, text, question_id) VALUES (11, true, 'answer 1', 3);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (12, false, 'answer 2', 3);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (13, false, 'answer 3', 3);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (14, false, 'answer 4', 3);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (15, false, ' answer5', 3);

INSERT INTO answer (id, is_correct, text, question_id) VALUES (16, true, 'answer 1', 4);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (17, false, 'answer 2', 4);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (18, false, 'answer 3', 4);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (19, false, 'answer 4', 4);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (20, false, ' answer5', 4);


INSERT INTO answer (id, is_correct, text, question_id) VALUES (21, true, 'answer 1', 5);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (22, false, 'answer 2', 5);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (23, false, 'answer 3', 5);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (24, false, 'answer 4', 5);
INSERT INTO answer (id, is_correct, text, question_id) VALUES (25, false, ' answer5', 5);
