CREATE TABLE s_emp
(id                         NUMBER(7)
   CONSTRAINT s_emp_id_nn NOT NULL,
 last_name                  VARCHAR2(25)
   CONSTRAINT s_emp_last_name_nn NOT NULL,
 first_name                 VARCHAR2(25),
 userid                     VARCHAR2(8),
 start_date                 DATE,
 comments                   VARCHAR2(255),
 manager_id                 NUMBER(7),
 title                      VARCHAR2(25),
 dept_id                    NUMBER(7),
 salary                     NUMBER(11, 2),
 commission_pct             NUMBER(4, 2),
     CONSTRAINT s_emp_id_pk PRIMARY KEY (id),
     CONSTRAINT s_emp_userid_uk UNIQUE (userid),
     CONSTRAINT s_emp_commission_pct_ck
        CHECK (commission_pct IN (10, 12.5, 15, 17.5, 20)));

INSERT INTO s_emp VALUES (
  1, 'Velasquez', 'Carmen', 'cvelasqu',
   to_date('03-MAR-90 8:30', 'dd-mon-yy hh24:mi'), NULL, NULL, 'President',
   50, 2500, NULL);
INSERT INTO s_emp VALUES (
   2, 'Ngao', 'LaDoris', 'lngao',
   '08-MAR-90', NULL, 1, 'VP, Operations',
   41, 1450, NULL);
INSERT INTO s_emp VALUES (
   3, 'Nagayama', 'Midori', 'mnagayam',
   '17-JUN-91', NULL, 1, 'VP, Sales',
   31, 1400, NULL);
INSERT INTO s_emp VALUES (
   4, 'Quick-To-See', 'Mark', 'mquickto',
   '07-APR-90', NULL, 1, 'VP, Finance',
   10, 1450, NULL);
INSERT INTO s_emp VALUES (
   5, 'Ropeburn', 'Audry', 'aropebur',
   '04-MAR-90', NULL, 1, 'VP, Administration',
   50, 1550, NULL);
INSERT INTO s_emp VALUES (
   6, 'Urguhart', 'Molly', 'murguhar',
   '18-JAN-91', NULL, 2, 'Warehouse Manager',
   41, 1200, NULL);
INSERT INTO s_emp VALUES (
   7, 'Menchu', 'Roberta', 'rmenchu',
   '14-MAY-90', NULL, 2, 'Warehouse Manager',
   42, 1250, NULL);
INSERT INTO s_emp VALUES (
   8, 'Biri', 'Ben', 'bbiri',
   '07-APR-90', NULL, 2, 'Warehouse Manager',
   43, 1100, NULL);
INSERT INTO s_emp VALUES (
   9, 'Catchpole', 'Antoinette', 'acatchpo',
   '09-FEB-92', NULL, 2, 'Warehouse Manager',
   44, 1300, NULL);
INSERT INTO s_emp VALUES (
   10, 'Havel', 'Marta', 'mhavel',
   '27-FEB-91', NULL, 2, 'Warehouse Manager',
   45, 1307, NULL);
INSERT INTO s_emp VALUES (
   11, 'Magee', 'Colin', 'cmagee',
   '14-MAY-90', NULL, 3, 'Sales Representative',
   31, 1400, 10);
INSERT INTO s_emp VALUES (
   12, 'Giljum', 'Henry', 'hgiljum',
   '18-JAN-92', NULL, 3, 'Sales Representative',
   32, 1490, 12.5);
INSERT INTO s_emp VALUES (
   13, 'Sedeghi', 'Yasmin', 'ysedeghi',
   '18-FEB-91', NULL, 3, 'Sales Representative',
   33, 1515, 10);
INSERT INTO s_emp VALUES (
   14, 'Nguyen', 'Mai', 'mnguyen',
   '22-JAN-92', NULL, 3, 'Sales Representative',
   34, 1525, 15);
INSERT INTO s_emp VALUES (
   15, 'Dumas', 'Andre', 'adumas',
   '09-OCT-91', NULL, 3, 'Sales Representative',
   35, 1450, 17.5);
INSERT INTO s_emp VALUES (
   16, 'Maduro', 'Elena', 'emaduro',
   '07-FEB-92', NULL, 6, 'Stock Clerk',
   41, 1400, NULL);
INSERT INTO s_emp VALUES (
   17, 'Smith', 'George', 'gsmith',
   '08-MAR-90', NULL, 6, 'Stock Clerk',
   41, 940, NULL);
INSERT INTO s_emp VALUES (
   18, 'Nozaki', 'Akira', 'anozaki',
   '09-FEB-91', NULL, 7, 'Stock Clerk',
   42, 1200, NULL);
INSERT INTO s_emp VALUES (
   19, 'Patel', 'Vikram', 'vpatel',
   '06-AUG-91', NULL, 7, 'Stock Clerk',
   42, 795, NULL);
INSERT INTO s_emp VALUES (
   20, 'Newman', 'Chad', 'cnewman',
   '21-JUL-91', NULL, 8, 'Stock Clerk',
   43, 750, NULL);
INSERT INTO s_emp VALUES (
   21, 'Markarian', 'Alexander', 'amarkari',
   '26-MAY-91', NULL, 8, 'Stock Clerk',
   43, 850, NULL);
INSERT INTO s_emp VALUES (
   22, 'Chang', 'Eddie', 'echang',
   '30-NOV-90', NULL, 9, 'Stock Clerk',
   44, 800, NULL);
INSERT INTO s_emp VALUES (
   23, 'Patel', 'Radha', 'rpatel',
   '17-OCT-90', NULL, 9, 'Stock Clerk',
   34, 795, NULL);
INSERT INTO s_emp VALUES (
   24, 'Dancs', 'Bela', 'bdancs',
   '17-MAR-91', NULL, 10, 'Stock Clerk',
   45, 860, NULL);
INSERT INTO s_emp VALUES (
   25, 'Schwartz', 'Sylvie', 'sschwart',
   '09-MAY-91', NULL, 10, 'Stock Clerk',
   45, 1100, NULL);
COMMIT;