create schema its340ProjectDB;
USE its340ProjectDB;

CREATE TABLE patients (
  patientID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  lastName VARCHAR(50),
  firstName VARCHAR(50),
  age INT,
  gender VARCHAR(10) DEFAULT NULL,
  blood_type VARCHAR(3) DEFAULT NULL,
  pain_level INT DEFAULT NULL);
  
CREATE TABLE allergy_history (
  AllergyID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patientID INT,
  Allergen VARCHAR(50));  
  
CREATE TABLE immunization_history (
  immunizationID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patientID INT,
  immunization VARCHAR(50));    
  
CREATE TABLE medication_history (
  medicationID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patientID INT,
  medication VARCHAR(50));   
  
CREATE TABLE hereditary_diseases_history (
  hereditary_diseaseID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patientID INT,
  hereditary_disease VARCHAR(50));   

 SELECT * FROM its340ProjectDB.patients;