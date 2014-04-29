PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE players (
    "Id" INTEGER,
    "Cardnum" INTEGER,
    "Name" TEXT NOT NULL,
    "Team" TEXT,
    "Points" INTEGER,
    "Rarity" TEXT,
    "Position1" TEXT,
    "PositionBonus1" INTEGER,
    "Position2" TEXT,
    "PositionBonus2" INTEGER,
    "Bats_Throws" TEXT,
    "Speed_IP" INTEGER,
    "OnBase_Control" INTEGER,
    "Icons" TEXT,
    "PU" INTEGER,
    "SO" INTEGER,
    "GB" INTEGER,
    "FB" INTEGER,
    "BB" INTEGER,
    "Single" INTEGER,
    "SinglePlus" INTEGER,
    "Double" INTEGER,
    "Triple" INTEGER,
    "HR" INTEGER
, "Image" TEXT);
INSERT INTO "players" VALUES(1,NULL,'Chris Iannetta','Angels',NULL,'C','C',4,NULL,NULL,'R',8,11,NULL,0,3,5,7,13,17,17,19,19,20,'chris_iannetta.png');
INSERT INTO "players" VALUES(2,NULL,'Albert Pujols','Angels',NULL,'C','1B',0,NULL,NULL,'R',10,10,NULL,0,1,3,6,10,16,16,18,18,19,'albert_pujols.png');
INSERT INTO "players" VALUES(3,NULL,'Howie Kendrick','Angels',NULL,'C','2B',2,NULL,NULL,'R',14,10,NULL,0,2,5,6,8,16,16,18,19,20,'howie_kendrick.png');
INSERT INTO "players" VALUES(4,NULL,'Erick Aybar','Angels',NULL,'C','SS',3,NULL,NULL,'S',16,10,NULL,0,1,4,6,8,16,17,19,20,21,'erick_aybar.png');
INSERT INTO "players" VALUES(5,NULL,'David Freese','Angels',NULL,'C','3B',1,NULL,NULL,'R',9,10,NULL,0,2,5,6,10,16,16,18,19,20,'david_freese.png');
INSERT INTO "players" VALUES(6,NULL,'Josh Hamilton','Angels',NULL,'C','LF/RF',1,NULL,NULL,'L',14,10,NULL,0,2,4,6,9,14,14,16,18,19,'josh_hamilton.png');
INSERT INTO "players" VALUES(7,NULL,'Mike Trout','Angels',700,'P','CF',3,'LF/RF',2,'R',20,13,NULL,0,1,3,5,8,12,14,16,18,19,'mike_trout.png');
INSERT INTO "players" VALUES(8,NULL,'Kole Calhoun','Angels',NULL,'C','LF/RF',2,NULL,NULL,'L',13,10,NULL,0,2,4,6,9,15,15,17,18,19,'kole_calhoun.png');
INSERT INTO "players" VALUES(9,NULL,'Raul Ibanez','Angels',NULL,'C','LF/RF',0,NULL,NULL,'L',8,10,NULL,0,2,4,7,11,15,15,17,17,18,'raul_ibanez.png');
INSERT INTO "players" VALUES(10,NULL,'Jered Weaver','Angels',NULL,'C','Starter',NULL,NULL,NULL,'R',6,5,NULL,1,5,9,16,17,19,19,23,23,24,'jered_weaver.png');
INSERT INTO "players" VALUES(11,NULL,'Mike Zunino','Mariners',NULL,'C','C',4,NULL,NULL,'R',11,9,NULL,0,2,4,7,11,18,18,19,19,20,'mike_zunino.png');
INSERT INTO "players" VALUES(12,NULL,'Justin Smoak','Mariners',NULL,'C','1B',0,NULL,NULL,'S',8,10,NULL,0,2,4,7,12,16,16,18,18,19,'justin_smoak.png');
INSERT INTO "players" VALUES(13,NULL,'Robinson Cano','Mariners',NULL,'P','2B',4,NULL,NULL,'L',14,12,NULL,0,1,3,5,9,15,16,18,18,19,'robinson_cano.png');
INSERT INTO "players" VALUES(14,NULL,'Brad Miller','Mariners',NULL,'C','2B/SS',3,NULL,NULL,'L',16,10,NULL,0,1,4,6,9,16,16,18,19,20,'brad_miller.png');
INSERT INTO "players" VALUES(15,NULL,'Kyle Seager','Mariners',NULL,'C','3B',1,NULL,NULL,'L',15,10,NULL,0,1,3,6,10,15,15,17,18,19,'kyle_seager.png');
INSERT INTO "players" VALUES(16,NULL,'Dustin Ackley','Mariners',NULL,'C','OF',1,'2B',2,'L',11,10,NULL,0,1,4,6,10,17,17,19,20,21,'dustin_ackley.png');
INSERT INTO "players" VALUES(17,NULL,'Abraham Almonte','Mariners',NULL,'C','CF',2,NULL,NULL,'S',13,10,NULL,0,2,5,6,9,16,17,19,19,20,'abraham_almonte.png');
INSERT INTO "players" VALUES(18,NULL,'Logan Morrison','Mariners',NULL,'C','1B',0,'LF/RF',1,'L',8,10,NULL,0,2,5,7,12,17,17,18,19,20,'logan_morrison.png');
INSERT INTO "players" VALUES(19,NULL,'Corey Hart','Mariners',NULL,'C','1B',0,'LF/RF',0,'R',13,10,NULL,0,2,4,6,9,14,15,17,18,19,'corey_hart.png');
INSERT INTO "players" VALUES(20,NULL,'Felix Hernandez','Mariners',NULL,'P','Starter',NULL,NULL,NULL,'R',7,5,NULL,0,5,12,16,18,22,22,26,26,27,'felix_hernandez.png');
COMMIT;
