SET CLUSTER '';
SET DEFAULT_TABLE_TYPE 0;
SET WRITE_DELAY 500;
SET DEFAULT_LOCK_TIMEOUT 2000;
SET CACHE_SIZE 16384;
;
CREATE USER IF NOT EXISTS "" SALT '1aea40d96e4912a7' HASH 'bd326299831655fa0d1a3c30da7a29f7329734266d80ea3838f25e0a1d0c6232' ADMIN;
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_9B7028E0_6755_4D70_9414_18C987002D40 START WITH 1 BELONGS_TO_TABLE;
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_FB070008_BB43_43AB_8024_0B629B24220F START WITH 1 BELONGS_TO_TABLE;
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_CD5CC165_B341_4ABE_B9AD_939BECADD07F START WITH 1 BELONGS_TO_TABLE;
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_56D803A4_9F5D_4F99_A3D0_B601E4EA5995 START WITH 1 BELONGS_TO_TABLE;
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_5DAD5B5F_2E17_4337_813D_36EB5A757FEA START WITH 1 BELONGS_TO_TABLE;
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_51B93CA0_408F_4990_907C_F15642580AB2 START WITH 1 BELONGS_TO_TABLE;
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_9293AE45_399F_4323_B84E_E795A35206D4 START WITH 3 BELONGS_TO_TABLE;
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_138C98E8_774C_46CB_88A8_EC51DBC5C227 START WITH 1 BELONGS_TO_TABLE;
CREATE SEQUENCE PUBLIC.SYSTEM_SEQUENCE_D03D087D_D293_4A14_8DD6_3210DE958E20 START WITH 1 BELONGS_TO_TABLE;
CREATE CACHED TABLE PUBLIC.BATCH(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_56D803A4_9F5D_4F99_A3D0_B601E4EA5995) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_56D803A4_9F5D_4F99_A3D0_B601E4EA5995,
    BOOKING_DATE DATE NOT NULL,
    CREATE_DATE DATE,
    CURRENT_QTY DECIMAL(12, 2) NOT NULL,
    DESCRIPTION VARCHAR(1000),
    EXPIRY_DATE DATE,
    FAKTURA_NO VARCHAR(100) NOT NULL,
    LINE_NO INTEGER,
    PRICE DECIMAL(16, 6) NOT NULL,
    SPECIFIER VARCHAR(255) NOT NULL,
    START_QTY DECIMAL(12, 2) NOT NULL,
    UNIT VARCHAR(255) NOT NULL,
    PRODUCT_ID BIGINT NOT NULL
);
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.BATCH;
CREATE CACHED TABLE PUBLIC.CONFIG_ENTRY(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_9293AE45_399F_4323_B84E_E795A35206D4) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_9293AE45_399F_4323_B84E_E795A35206D4,
    DESCRIPTION VARCHAR(2000),
    EDITABLE BOOLEAN,
    ENTRY_NAME VARCHAR(100) NOT NULL,
    ENTRY_VALUE VARCHAR(10000),
    VISIBLE BOOLEAN
);
-- 2 +/- SELECT COUNT(*) FROM PUBLIC.CONFIG_ENTRY;
INSERT INTO PUBLIC.CONFIG_ENTRY(ID, DESCRIPTION, EDITABLE, ENTRY_NAME, ENTRY_VALUE, VISIBLE) VALUES
(1, NULL, FALSE, '-3633d9c3:124e039936d:-8000', NULL, FALSE),
(2, 'dasdas', FALSE, '-3633d9c3:124e039936d:-7fff', NULL, FALSE);
CREATE CACHED TABLE PUBLIC.COURSE(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_5DAD5B5F_2E17_4337_813D_36EB5A757FEA) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_5DAD5B5F_2E17_4337_813D_36EB5A757FEA,
    COST DECIMAL(19, 2),
    COST_STRICT BOOLEAN,
    NAME VARCHAR(255) NOT NULL,
    MEAL_ID BIGINT NOT NULL
);
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.COURSE;
CREATE CACHED TABLE PUBLIC.DAY(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_D03D087D_D293_4A14_8DD6_3210DE958E20) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_D03D087D_D293_4A14_8DD6_3210DE958E20,
    DATA DATE,
    INNI_NO INTEGER,
    KADRA_NO INTEGER,
    UCZESTNICY_NO INTEGER,
    RATE DECIMAL(19, 2)
);
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.DAY;
CREATE CACHED TABLE PUBLIC.EXPENDITURE(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_CD5CC165_B341_4ABE_B9AD_939BECADD07F) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_CD5CC165_B341_4ABE_B9AD_939BECADD07F,
    CREATE_DATE TIMESTAMP NOT NULL,
    EXPENDITURE_DATE TIMESTAMP NOT NULL,
    QUANTITY DECIMAL(19, 2) NOT NULL,
    TYTULEM VARCHAR(255) NOT NULL,
    BATCH_ID BIGINT NOT NULL,
    COURSE_ID BIGINT
);
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.EXPENDITURE;
CREATE CACHED TABLE PUBLIC.MEAL(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_51B93CA0_408F_4990_907C_F15642580AB2) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_51B93CA0_408F_4990_907C_F15642580AB2,
    ADDITIONAL BOOLEAN,
    COST DECIMAL(19, 2),
    COST_STRICT BOOLEAN,
    NAME VARCHAR(255),
    INNI_NO INTEGER,
    KADRA_NO INTEGER,
    UCZESTNICY_NO INTEGER,
    DZIEN_ID BIGINT NOT NULL
);
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.MEAL;
CREATE CACHED TABLE PUBLIC.MEAL_COURSE(
    MEAL_ID BIGINT NOT NULL,
    DANIA_ID BIGINT NOT NULL
);
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.MEAL_COURSE;
CREATE CACHED TABLE PUBLIC.PLANNED_EXPENDITURE(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_FB070008_BB43_43AB_8024_0B629B24220F) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_FB070008_BB43_43AB_8024_0B629B24220F,
    QUANTITY DECIMAL(19, 2) NOT NULL,
    SPECIFIER VARCHAR(255),
    UNIT VARCHAR(255) NOT NULL,
    COURSE_ID BIGINT,
    PRODUCT_ID BIGINT NOT NULL
);
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.PLANNED_EXPENDITURE;
CREATE CACHED TABLE PUBLIC.PLANNED_EXPENDITURE_EXPENDITURE(
    PLANNED_EXPENDITURE_ID BIGINT NOT NULL,
    EXPENDITURES_ID BIGINT NOT NULL
);
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.PLANNED_EXPENDITURE_EXPENDITURE;
CREATE CACHED TABLE PUBLIC.PRODUCT(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_138C98E8_774C_46CB_88A8_EC51DBC5C227) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_138C98E8_774C_46CB_88A8_EC51DBC5C227,
    EXPIRY_DATE INTEGER NOT NULL CHECK (EXPIRY_DATE >= -1),
    NAME VARCHAR(50) NOT NULL,
    UNIT VARCHAR(50)
);
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.PRODUCT;
CREATE CACHED TABLE PUBLIC.PRODUCT_SEARCH_CACHE(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_9B7028E0_6755_4D70_9414_18C987002D40) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.SYSTEM_SEQUENCE_9B7028E0_6755_4D70_9414_18C987002D40,
    PRODUCT_ID BIGINT NOT NULL,
    PRODUCT_NAME VARCHAR(255) NOT NULL,
    SPECIFIER VARCHAR(255),
    UNIT VARCHAR(255)
);
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.PRODUCT_SEARCH_CACHE;
ALTER TABLE PUBLIC.PRODUCT_SEARCH_CACHE ADD CONSTRAINT PUBLIC.CONSTRAINT_3D PRIMARY KEY(ID);
ALTER TABLE PUBLIC.BATCH ADD CONSTRAINT PUBLIC.CONSTRAINT_3 PRIMARY KEY(ID);
ALTER TABLE PUBLIC.MEAL ADD CONSTRAINT PUBLIC.CONSTRAINT_2 PRIMARY KEY(ID);
ALTER TABLE PUBLIC.PRODUCT ADD CONSTRAINT PUBLIC.CONSTRAINT_18 PRIMARY KEY(ID);
ALTER TABLE PUBLIC.EXPENDITURE ADD CONSTRAINT PUBLIC.CONSTRAINT_6 PRIMARY KEY(ID);
ALTER TABLE PUBLIC.CONFIG_ENTRY ADD CONSTRAINT PUBLIC.CONSTRAINT_E PRIMARY KEY(ID);
ALTER TABLE PUBLIC.PLANNED_EXPENDITURE ADD CONSTRAINT PUBLIC.CONSTRAINT_5 PRIMARY KEY(ID);
ALTER TABLE PUBLIC.DAY ADD CONSTRAINT PUBLIC.CONSTRAINT_1 PRIMARY KEY(ID);
ALTER TABLE PUBLIC.COURSE ADD CONSTRAINT PUBLIC.CONSTRAINT_7 PRIMARY KEY(ID);
ALTER TABLE PUBLIC.MEAL_COURSE ADD CONSTRAINT PUBLIC.CONSTRAINT_35 UNIQUE(DANIA_ID);
ALTER TABLE PUBLIC.PRODUCT ADD CONSTRAINT PUBLIC.CONSTRAINT_185 UNIQUE(NAME);
ALTER TABLE PUBLIC.DAY ADD CONSTRAINT PUBLIC.CONSTRAINT_10 UNIQUE(DATA);
ALTER TABLE PUBLIC.PLANNED_EXPENDITURE_EXPENDITURE ADD CONSTRAINT PUBLIC.FKAD748E48356D1D9 FOREIGN KEY(EXPENDITURES_ID) REFERENCES PUBLIC.EXPENDITURE(ID) NOCHECK;
ALTER TABLE PUBLIC.MEAL_COURSE ADD CONSTRAINT PUBLIC.FK36E68C376C6F5658 FOREIGN KEY(DANIA_ID) REFERENCES PUBLIC.COURSE(ID) NOCHECK;
ALTER TABLE PUBLIC.PLANNED_EXPENDITURE ADD CONSTRAINT PUBLIC.FK53CF134462F1F166 FOREIGN KEY(COURSE_ID) REFERENCES PUBLIC.COURSE(ID) NOCHECK;
ALTER TABLE PUBLIC.EXPENDITURE ADD CONSTRAINT PUBLIC.FK6529D5FABD4D46E FOREIGN KEY(BATCH_ID) REFERENCES PUBLIC.BATCH(ID) NOCHECK;
ALTER TABLE PUBLIC.PLANNED_EXPENDITURE ADD CONSTRAINT PUBLIC.FK53CF134483C18F4E FOREIGN KEY(PRODUCT_ID) REFERENCES PUBLIC.PRODUCT(ID) NOCHECK;
ALTER TABLE PUBLIC.MEAL_COURSE ADD CONSTRAINT PUBLIC.FK36E68C376A066366 FOREIGN KEY(MEAL_ID) REFERENCES PUBLIC.MEAL(ID) NOCHECK;
ALTER TABLE PUBLIC.COURSE ADD CONSTRAINT PUBLIC.FK78A7CC3B6A066366 FOREIGN KEY(MEAL_ID) REFERENCES PUBLIC.MEAL(ID) NOCHECK;
ALTER TABLE PUBLIC.MEAL ADD CONSTRAINT PUBLIC.FK240BC3F7D7EB4E FOREIGN KEY(DZIEN_ID) REFERENCES PUBLIC.DAY(ID) NOCHECK;
ALTER TABLE PUBLIC.EXPENDITURE ADD CONSTRAINT PUBLIC.FK6529D5F62F1F166 FOREIGN KEY(COURSE_ID) REFERENCES PUBLIC.COURSE(ID) NOCHECK;
ALTER TABLE PUBLIC.PLANNED_EXPENDITURE_EXPENDITURE ADD CONSTRAINT PUBLIC.FKAD748E485DEE45D FOREIGN KEY(PLANNED_EXPENDITURE_ID) REFERENCES PUBLIC.PLANNED_EXPENDITURE(ID) NOCHECK;
ALTER TABLE PUBLIC.BATCH ADD CONSTRAINT PUBLIC.FK3C0DF1A83C18F4E FOREIGN KEY(PRODUCT_ID) REFERENCES PUBLIC.PRODUCT(ID) NOCHECK;
------------------------------------------------------------------------------------------------------------------------
--                                                                                                                    --
--                         RECZNE ZMIANY                                                                              --
--                                                                                                                    --
------------------------------------------------------------------------------------------------------------------------


ALTER TABLE CONFIG_ENTRY ADD CONSTRAINT CONFIG_ENTRY_NAME_UNIQUE UNIQUE(ENTRY_NAME);

CREATE TRIGGER CONFIG_ENTRY_UPDATE_TRIGGER BEFORE UPDATE ON CONFIG_ENTRY FOR EACH ROW CALL "cx.ath.jbzdak.zarlock.db.trigger.ConfigEntryTrigger";
CREATE TRIGGER CONFIG_ENTRY_DELETE_TRIGGER BEFORE DELETE ON CONFIG_ENTRY FOR EACH ROW CALL "cx.ath.jbzdak.zarlock.db.trigger.ConfigEntryTrigger";
CREATE TRIGGER EXPENDITURE_PRODUCT_INSERT_TRIGGER AFTER INSERT ON EXPENDITURE FOR EACH ROW CALL "cx.ath.jbzdak.zarlock.db.trigger.ExpenditureUpdateProductTrigger";
CREATE TRIGGER EXPENDITURE_PRODUCT_DELETE_TRIGGER AFTER DELETE ON EXPENDITURE FOR EACH ROW CALL "cx.ath.jbzdak.zarlock.db.trigger.ExpenditureUpdateProductTrigger";
CREATE TRIGGER EXPENDITURE_PRODUCT_UPDATE_TRIGGER AFTER UPDATE ON EXPENDITURE FOR EACH ROW CALL "cx.ath.jbzdak.zarlock.db.trigger.ExpenditureUpdateProductTrigger";

INSERT INTO CONFIG_ENTRY(DESCRIPTION, EDITABLE, ENTRY_NAME, ENTRY_VALUE, VISIBLE) VALUES('Wersja bazy danych', TRUE, 'DATABASE_VERSION', '30000', FALSE);