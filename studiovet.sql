-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: studiovet
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `animale`
--

DROP TABLE IF EXISTS `animale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `animale` (
  `chip` int NOT NULL,
  `nome` varchar(45) NOT NULL,
  `tipo` varchar(45) NOT NULL,
  `colore` varchar(45) NOT NULL,
  `razza` varchar(45) NOT NULL,
  `dataNascita` date NOT NULL,
  `usernameUtente` varchar(45) NOT NULL,
  PRIMARY KEY (`chip`),
  UNIQUE KEY `chip_UNIQUE` (`chip`),
  KEY `usernameUtente_idx` (`usernameUtente`),
  CONSTRAINT `usernameUtente` FOREIGN KEY (`usernameUtente`) REFERENCES `utente` (`username`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `animale`
--

LOCK TABLES `animale` WRITE;
/*!40000 ALTER TABLE `animale` DISABLE KEYS */;
INSERT INTO `animale` VALUES (100001,'Argo','Cane','Marrone','Labrador','2018-05-12','gCelentano'),(100002,'Mimi','Gatto','Bianco','Persiano','2020-03-07','gCelentano'),(100003,'Nero','Cane','Nero','Dobermann','2019-10-25','gCelentano'),(100004,'Paco','Uccello','Verde','Pappagallo','2021-06-15','gCelentano'),(100005,'Luna','Gatto','Grigio','Siberiano','2022-01-30','gCelentano'),(100006,'Birillo','Cane','Beige','Barboncino','2017-04-10','gGalli56'),(100007,'Zoe','Gatto','Tigrato','Europeo','2021-08-19','gGalli56'),(100008,'Cip','Uccello','Giallo','Canarino','2022-09-01','gGalli56'),(100009,'Toby','Cane','Nero','Meticcio','2016-11-05','gGalli56'),(100010,'Nina','Gatto','Bianco e Nero','Ragdoll','2023-02-12','gGalli56'),(100011,'Fido','Cane','Marrone','Bulldog','2015-07-20','lrRossi'),(100012,'Maya','Gatto','Nero','Bombay','2020-05-11','lrRossi'),(100013,'Kiko','Cane','Bianco','Maltese','2022-03-03','lrRossi'),(100014,'Lilly','Coniglio','Bianco','Nano','2021-12-25','lrRossi'),(100015,'Rocky','Cane','Grigio','Husky','2018-09-09','lrRossi'),(100016,'Bobby','Cane','Nero e Marrone','Bassotto','2021-04-14','mFalzea'),(100017,'Milu','Gatto','Grigio','Certosino','2019-02-27','mFalzea'),(100018,'Cleo','Cane','Beige','Carlino','2020-11-08','mFalzea'),(100019,'Fufi','Gatto','Rosso','Abissino','2023-03-01','mFalzea'),(100020,'Rex','Cane','Marrone','Terranova','2016-06-17','mFalzea'),(100021,'Oreo','Gatto','Bianco e Nero','British Shorthair','2022-07-22','verdMar2'),(100022,'Zampa','Cane','Nero','Border Collie','2021-09-14','verdMar2'),(100023,'Billo','Cane','Bianco','Jack Russell','2019-12-03','verdMar2'),(100024,'Luna','Gatto','Grigio','Maine Coon','2018-08-08','verdMar2'),(100025,'Jack','Coniglio','Marrone','Ariete','2020-01-20','verdMar2'),(100110,'Giuseppe','Cavallo','Marrone','Equina','2010-01-01','mProva');
/*!40000 ALTER TABLE `animale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `farmaco`
--

DROP TABLE IF EXISTS `farmaco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farmaco` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `produttore` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farmaco`
--

LOCK TABLES `farmaco` WRITE;
/*!40000 ALTER TABLE `farmaco` DISABLE KEYS */;
INSERT INTO `farmaco` VALUES (1,'Antibiotico Vet','Zoetis'),(2,'Antinfiammatorio Canino','Elanco'),(3,'Analgesico Felino','Bayer Animal Health'),(4,'Drontal Plus','Bayer Animal Health'),(5,'Frontline Combo','Boehringer Ingelheim'),(6,'Simparica','Zoetis'),(7,'Bravecto','MSD Animal Health'),(8,'Metacam','Boehringer Ingelheim'),(9,'Clavaseptin','Vetoquinol'),(10,'Revolution','Zoetis'),(11,'Rimadyl','Zoetis'),(12,'Dexdomitor','Orion Corporation'),(13,'Cerenia','Zoetis'),(14,'Zylkène','Vetoquinol'),(15,'Vetoryl','Dechra Pharmaceuticals'),(16,'Onsior','Elanco'),(17,'ProZinc','Boehringer Ingelheim'),(18,'Convenia','Zoetis'),(19,'Magnesio','Bayern'),(20,'Antibiotico per ratti','Bayern'),(21,'Giuseppe Antibiotico','Bayern');
/*!40000 ALTER TABLE `farmaco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `impiega`
--

DROP TABLE IF EXISTS `impiega`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `impiega` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idFarmaco` int NOT NULL,
  `idVisita` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idVisita_idx` (`idVisita`),
  KEY `idFarmaco_idx` (`idFarmaco`),
  CONSTRAINT `idFarmaco` FOREIGN KEY (`idFarmaco`) REFERENCES `farmaco` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `idVisita` FOREIGN KEY (`idVisita`) REFERENCES `visita` (`idVisita`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='		';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `impiega`
--

LOCK TABLES `impiega` WRITE;
/*!40000 ALTER TABLE `impiega` DISABLE KEYS */;
INSERT INTO `impiega` VALUES (1,1,3),(2,2,7),(3,3,5),(4,4,14),(5,5,8),(6,6,10),(7,8,6),(8,9,3),(9,11,12),(10,13,2),(11,15,5),(12,16,12),(13,18,9),(14,17,11),(15,10,4),(16,16,3);
/*!40000 ALTER TABLE `impiega` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prenotazione`
--

DROP TABLE IF EXISTS `prenotazione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prenotazione` (
  `data` date NOT NULL DEFAULT '1990-01-01',
  `orario` int NOT NULL,
  `idStato` int NOT NULL DEFAULT '0',
  `chipAnimale` int DEFAULT NULL,
  `idVisita` int DEFAULT NULL,
  PRIMARY KEY (`data`,`orario`),
  KEY `chipAnimale_idx` (`chipAnimale`),
  KEY `idStato_idx` (`idStato`),
  KEY `idVisita_idx` (`idVisita`),
  KEY `idVisitaPre_idx` (`idVisita`),
  CONSTRAINT `chipAnimale` FOREIGN KEY (`chipAnimale`) REFERENCES `animale` (`chip`) ON UPDATE CASCADE,
  CONSTRAINT `idStato` FOREIGN KEY (`idStato`) REFERENCES `stato` (`idStato`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prenotazione`
--

LOCK TABLES `prenotazione` WRITE;
/*!40000 ALTER TABLE `prenotazione` DISABLE KEYS */;
INSERT INTO `prenotazione` VALUES ('2023-05-27',10,4,100017,1),('2023-10-14',11,2,100017,2),('2023-11-08',15,4,100025,3),('2024-01-15',14,4,100001,4),('2024-03-10',9,2,100008,5),('2024-05-20',10,4,100014,6),('2024-07-01',11,4,100003,7),('2024-09-25',13,2,100009,8),('2024-11-18',16,4,100005,9),('2025-01-05',8,4,100002,10),('2025-02-22',9,2,100019,11),('2025-03-30',15,4,100020,12),('2025-04-10',14,4,100007,13),('2025-05-12',12,2,100010,14),('2025-06-15',11,4,100016,15),('2025-06-17',9,1,100001,NULL),('2025-06-17',10,4,100002,16),('2025-06-17',12,1,100003,NULL),('2025-06-18',8,1,100110,NULL),('2025-06-18',9,1,100004,NULL),('2025-06-18',10,0,NULL,NULL),('2025-06-18',11,1,100005,NULL),('2025-06-18',12,0,NULL,NULL),('2025-06-18',13,0,NULL,NULL),('2025-06-18',14,1,100006,NULL),('2025-06-18',15,0,NULL,NULL),('2025-06-18',16,0,NULL,NULL),('2025-06-18',17,0,NULL,NULL),('2025-06-19',8,1,100007,NULL),('2025-06-19',10,1,100008,NULL),('2025-06-19',11,1,100009,NULL),('2025-06-19',13,1,100010,NULL),('2025-06-21',9,1,100016,NULL),('2025-06-22',10,0,NULL,NULL),('2025-06-23',11,0,NULL,NULL),('2025-06-24',12,0,NULL,NULL),('2025-06-25',14,0,NULL,NULL),('2025-06-26',15,0,NULL,NULL),('2025-06-27',16,0,NULL,NULL),('2025-06-28',17,0,NULL,NULL),('2025-07-01',9,0,NULL,NULL),('2025-07-02',10,0,NULL,NULL),('2025-07-03',11,0,NULL,NULL),('2025-07-04',13,0,NULL,NULL),('2025-07-05',8,0,NULL,NULL),('2025-07-06',9,0,NULL,NULL),('2025-07-07',15,0,NULL,NULL),('2025-07-08',14,0,NULL,NULL),('2025-07-09',12,0,NULL,NULL),('2025-07-10',11,0,NULL,NULL),('2025-07-11',10,0,NULL,NULL),('2025-07-12',9,0,NULL,NULL);
/*!40000 ALTER TABLE `prenotazione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stato`
--

DROP TABLE IF EXISTS `stato`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stato` (
  `idStato` int NOT NULL,
  `nomeStato` varchar(45) NOT NULL,
  PRIMARY KEY (`idStato`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stato`
--

LOCK TABLES `stato` WRITE;
/*!40000 ALTER TABLE `stato` DISABLE KEYS */;
INSERT INTO `stato` VALUES (0,'Disponibile'),(1,'In attesa'),(2,'Annullato'),(3,'Confermato'),(4,'Terminata');
/*!40000 ALTER TABLE `stato` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipoutente`
--

DROP TABLE IF EXISTS `tipoutente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipoutente` (
  `idTipo` int NOT NULL,
  `nomeTipo` varchar(45) NOT NULL,
  PRIMARY KEY (`idTipo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipoutente`
--

LOCK TABLES `tipoutente` WRITE;
/*!40000 ALTER TABLE `tipoutente` DISABLE KEYS */;
INSERT INTO `tipoutente` VALUES (1,'Amministratore'),(2,'Veterinario'),(3,'Cliente');
/*!40000 ALTER TABLE `tipoutente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utente` (
  `username` varchar(45) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `cognome` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `immagineProfilo` varchar(45) DEFAULT NULL,
  `tipoUtente` int NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `tipoUtente_idx` (`tipoUtente`),
  CONSTRAINT `tipoUtente` FOREIGN KEY (`tipoUtente`) REFERENCES `tipoutente` (`idTipo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES ('dfontana','Davide','Fontana','davide.fontana@email.com','Test1A','default.png',2),('gCelentano','Giuseppe','Celentano','giuseppe.celentano@email.com','Test1A','img_1750150694108.jpg',3),('gGalli56','Giulia','Gialli','giulia.gialli@studiovet.it','pass456','default.png',3),('lrRossi','Luca','Rossi','luca.rossi@studiovet.it','password123','default.png',3),('mbarretta','Sal','Barretta','barrettasalvatore@outlook.it','Test1A','default.png',1),('mFalzea','Matteo','Falzea','mFalzea1@gmail.com','mFalzea1','default.png',3),('mProva','Matteo','Prova','mProva1@gmail.com','mProva1','img_1750150331167.jpg',3),('verdMar2','Marco','Verdi','marco.verdi@studiovet.it','admin789','default.png',3);
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visita`
--

DROP TABLE IF EXISTS `visita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visita` (
  `idVisita` int NOT NULL AUTO_INCREMENT,
  `tipoVisita` varchar(45) NOT NULL,
  `descrizione` varchar(255) NOT NULL,
  `costo` double NOT NULL,
  `usernameVeterinario` varchar(45) NOT NULL,
  PRIMARY KEY (`idVisita`),
  KEY `usernameVet_idx` (`usernameVeterinario`),
  CONSTRAINT `usernameVet` FOREIGN KEY (`usernameVeterinario`) REFERENCES `utente` (`username`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visita`
--

LOCK TABLES `visita` WRITE;
/*!40000 ALTER TABLE `visita` DISABLE KEYS */;
INSERT INTO `visita` VALUES (1,'Vaccinazione','Vaccino trivalente per cani adulti',45,'dfontana'),(2,'Controllo','Controllo post-operatorio a 10 giorni',30,'dfontana'),(3,'Intervento chirurgico','Rimozione corpo estraneo intestinale',250,'dfontana'),(4,'Vaccinazione','Vaccinazione antirabbica obbligatoria',40,'dfontana'),(5,'Controllo','Controllo generale annuale per gatto anziano',35,'dfontana'),(6,'Intervento chirurgico','Sterilizzazione femmina cane taglia media',180,'dfontana'),(7,'Controllo','Controllo dermatologico per prurito persistente',50,'dfontana'),(8,'Vaccinazione','Vaccino leishmania per cane',55,'dfontana'),(9,'Intervento chirurgico','Asportazione cisti cutanea',120,'dfontana'),(10,'Controllo','Visita ortopedica per zoppia posteriore',60,'dfontana'),(11,'Vaccinazione','Richiamo vaccino tetravalente per gatto',38,'dfontana'),(12,'Intervento chirurgico','Castrazione convalescente',150,'dfontana'),(13,'Controllo','Controllo oculistico per congiuntivite ricorrente',40,'dfontana'),(14,'Vaccinazione','Vaccino mix per cuccioli (Cimurro, Parvovirosi)',48,'dfontana'),(15,'Intervento chirurgico','Rimozione tartaro con anestesia',100,'dfontana'),(16,'Controllo','Mimi ha fatto un controllo alle zampe',20,'dfontana');
/*!40000 ALTER TABLE `visita` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'studiovet'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-17 11:59:08
