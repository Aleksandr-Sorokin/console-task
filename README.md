# console-task
���������� ��������� �� ���� ��� ����� **input.json** � **output.json** ��� ���� ���� **input.json** 

� **output.json** ����� ��������� ������ �������� **output.json** ������������ �� ���������.

���� ������ ������ ������� **��������������.json**, �� ��������� ����� � **��������������.json**

� ����� input.json ������ ���������� ��������� ������ � ������� json:

    {
    "criterias":[
        {"lastName": "������"}, 
        {"productName": "����������� ����", "minTimes": 5},
        {"minExpenses": 112, "maxExpenses": 4000},  
        {"badCustomers": 3}
        ] 
    }
**criterias** - �������� � �������� ���������� ������ ������

**lastName** - lastName ����� � ���� �� �������

**productName** - "productName" ����� � ���� ���������� ��������� ������� �� ����� **"minTimes"** ���

**minExpenses � maxExpenses** - ����� ����������� �������� �� ����� � ���� ��������� minExpenses � maxExpenses

**badCustomers** - ���������� ��������� ����������� �������� ������ �����

���

    {
        "startDate": "2020-01-14",
        "endDate": "2020-01-26"
    }
**startDate � endDate** - ������ �� ������� ����� ��������� ���������� ��� ����� �������� ����

**����� ��� ������ ����� � ����� ***console-task\***

    input.json
    inputstat.json
    inputstatwitherror.json
    inputwitherror.json

**���� input.json** - ���������� �������� � ������ ������� ***console-task/input.json***

### ������ ��������� ###
�� �� ��������� ������������� **MAVEN** � **DOCKER** ������� � ������ **\console-task**

��������� ��������������� �������

    mvn package
    docker-compose up --build -d
    java -jar target/console-task-1.0-SNAPSHOT.jar input.json output.json
    java -jar target/console-task-1.0-SNAPSHOT.jar input.json
������� � ����� **\console-task** � ������� ����� ������� ���� �� �������� ���������� ���� **output.json** � ��� ������ ���� ������
### ������� ��� �������� ������ ������ ### 

    java -jar target/console-task-1.0-SNAPSHOT.jar inputstat.json
    java -jar target/console-task-1.0-SNAPSHOT.jar inputstatwitherror.json
    java -jar target/console-task-1.0-SNAPSHOT.jar inputwitherror.json

### ��������� ������� docker-compose###
    docker-compose stop
    docker-compose down

**��� ����������**

    {
    "type" : "STAT",
    "totalDays" : 9,
    "customers" : [ {
    "name" : "������� ����",
    "purchases" : [ {
    "name" : "���������",
    "expenses" : 246.0
    }, {
    "name" : "���������� \"������\"",
    "expenses" : 63.0
    } ],
    "totalExpenses" : 309.0
    }, {
    "name" : "������ ??���",
    "purchases" : [ {
    "name" : "����������� ����",
    "expenses" : 270.0
    } ],
    "totalExpenses" : 270.0
    }, {
    "name" : "������ ���������",
    "purchases" : [ {
    "name" : "����������� ����",
    "expenses" : 108.0
    } ],
    "totalExpenses" : 108.0
    } ],
    "totalExpenses" : 687.0,
    "avgExpenses" : 229.0
    }

**��� ���������� �������**

    {
    "type" : "SEARCH",
    "results" : [ {
    "criteria" : {
    "lastName" : "?�����"
    },
    "results" : [ {
    "firstName" : "���������",
    "lastName" : "?�����"
    }, {
    "firstName" : "?���",
    "lastName" : "?�����"
    } ]
    }, {
    "criteria" : {
    "productName" : "����������� ����",
    "minTimes" : 5
    },
    "results" : [ {
    "firstName" : "���������",
    "lastName" : "?�����"
    }, {
    "firstName" : "?���",
    "lastName" : "������"
    } ]
    }, {
    "criteria" : {
    "minExpenses" : 112,
    "maxExpenses" : 4000
    },
    "results" : [ {
    "firstName" : "����",
    "lastName" : "������"
    }, {
    "firstName" : "?���",
    "lastName" : "?�����"
    }, {
    "firstName" : "?���",
    "lastName" : "������"
    }, {
    "firstName" : "���������",
    "lastName" : "?�����"
    }, {
    "firstName" : "����",
    "lastName" : "�������"
    } ]
    }, {
    "criteria" : {
    "badCustomers" : 3
    },
    "results" : [ {
    "firstName" : "���������",
    "lastName" : "������"
    }, {
    "firstName" : "����",
    "lastName" : "������"
    }, {
    "firstName" : "?���",
    "lastName" : "?�����"
    } ]
    } ]
    }

### ���� ������ ###

    DROP TABLE IF EXISTS purchase, product, customer;
    
    CREATE TABLE IF NOT EXISTS customer
    (
    customer_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(200) NOT NULL,
    last_name VARCHAR(200) NOT NULL
    );
    
    CREATE TABLE IF NOT EXISTS product
    (
    product_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    price DOUBLE PRECISION NOT NULL
    );
    
    CREATE TABLE IF NOT EXISTS purchase
    (
    purchase_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    volume INTEGER NOT NULL,
    purchase_date TIMESTAMP NOT NULL,
    CONSTRAINT purchase_customer_id_fk
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE,
    CONSTRAINT purchase_product_id_fk
    FOREIGN KEY (product_id) REFERENCES product (product_id) ON DELETE CASCADE
    ); 

### ������ ��� ������������ ###

    INSERT INTO customer (customer_id, first_name, last_name) VALUES (1, '���������', '������');
    INSERT INTO customer (customer_id, first_name, last_name) VALUES (2, '����', '������');
    INSERT INTO customer (customer_id, first_name, last_name) VALUES (3, '����', '������');
    INSERT INTO customer (customer_id, first_name, last_name) VALUES (4, '����', '������');
    INSERT INTO customer (customer_id, first_name, last_name) VALUES (5, '����', '�������');
    INSERT INTO customer (customer_id, first_name, last_name) VALUES (6, '���������', '�������');
    INSERT INTO customer (customer_id, first_name, last_name) VALUES (7, '���������', '������');
    
    INSERT INTO product (product_id, title, price) VALUES (1, '����������� ����', 54);
    INSERT INTO product (product_id, title, price) VALUES (2, '���������� "������"', 63);
    INSERT INTO product (product_id, title, price) VALUES (3, '���� 5-�', 49);
    INSERT INTO product (product_id, title, price) VALUES (4, '���� �����', 35);
    INSERT INTO product (product_id, title, price) VALUES (5, '���� ������', 42);
    INSERT INTO product (product_id, title, price) VALUES (6, '������', 150);
    INSERT INTO product (product_id, title, price) VALUES (7, '���������', 123);
    INSERT INTO product (product_id, title, price) VALUES (8, '������', 84);
    
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (1, 1, 1, 7, '2020-01-13');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (2, 1, 2, 1, '2020-01-13');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (3, 1, 3, 5, '2020-01-14');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (4, 2, 4, 5, '2020-01-14');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (5, 2, 8, 2, '2020-01-15');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (6, 3, 1, 5, '2020-01-16');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (7, 3, 6, 2, '2020-01-18');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (8, 3, 7, 1, '2020-01-19');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (9, 4, 1, 2, '2020-01-20');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (10, 4, 5, 2, '2020-01-21');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (11, 5, 6, 2, '2020-01-22');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (12, 5, 7, 2, '2020-01-23');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (13, 5, 2, 1, '2020-01-24');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (14, 5, 3, 2, '2020-01-25');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (15, 1, 6, 6, '2020-01-26');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (16, 1, 5, 2, '2020-01-27');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (17, 3, 1, 8, '2020-01-28');
    INSERT INTO purchase (purchase_id, customer_id, product_id, volume, purchase_date) VALUES (18, 7, 1, 2, '2020-01-17');
