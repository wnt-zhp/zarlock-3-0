UPDATE CONFIG_ENTRY SET ENTRY_VALUE = '30012' WHERE ENTRY_NAME = 'DATABASE_VERSION';

CREATE TRIGGER BATCH_CURRQTY_UPDATE_INSERT BEFORE UPDATE ON BATCH FOR EACH ROW CALL "cx.ath.jbzdak.zarlock.db.trigger.BatchUpdate";
CREATE TRIGGER BATCH_CURRQTY_UPDATE_UPDATE BEFORE INSERT ON BATCH FOR EACH ROW CALL "cx.ath.jbzdak.zarlock.db.trigger.BatchInsert";