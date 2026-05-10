create schema its340ProjectDB;
USE its340ProjectDB;

CREATE TABLE patients (
  patient_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  lastName VARCHAR(50),
  firstName VARCHAR(50),
  age INT,
  gender VARCHAR(10) DEFAULT NULL,
  blood_type VARCHAR(3) DEFAULT NULL,
  pain_level INT DEFAULT NULL);
  
CREATE TABLE allergen_history (
  allergyID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patientID INT NOT NULL,
  allergen VARCHAR(50),
  FOREIGN KEY (patientID) REFERENCES patients(patient_ID));  
  
CREATE TABLE immunization_history (
  immunizationID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patientID INT NOT NULL,
  immunization VARCHAR(50),
  FOREIGN KEY (patientID) REFERENCES patients(patient_ID));    
  
CREATE TABLE medication_history (
  medicationID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patientID INT NOT NULL,
  medication VARCHAR(50),
  FOREIGN KEY (patientID) REFERENCES patients(patient_ID));   
  
CREATE TABLE hereditary_disease_history (
  hereditary_diseaseID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patientID INT NOT NULL,
  hereditary_disease VARCHAR(50),
  FOREIGN KEY (patientID) REFERENCES patients(patient_ID));
  
CREATE TABLE symptom_history (
  symptomID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patientID INT NOT NULL,
  symptom VARCHAR(50),
  FOREIGN KEY (patientID) REFERENCES patients(patient_ID));     

 SELECT * FROM its340ProjectDB.patients;