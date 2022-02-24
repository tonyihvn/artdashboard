/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.artdashboard.pojo;

/**
 * @author --
 */
public class Regimen {
	
	private int regimenConceptId;
	
	private String artStartDate;
	
	private String pepfarId;
	
	private String patientAge;
	
	private String dob;
	
	private String patientWeight;
	
	private String regimenLine;//this is actually the regimen
	
	private String regimenLine2;
	
	private String drugStrength;
	
	private String drugDosage;
	
	private String pickupDate;
	
	private String firstName;
	
	private String lastName;
	
	private String patientId;
	
	private String weightDate;
	
	private int viralLoad;
	
	private String sampleCollectionDate;
	
	public int getRegimenConceptId() {
		return regimenConceptId;
	}
	
	public void setRegimenConceptId(int regimenConceptId) {
		this.regimenConceptId = regimenConceptId;
	}
	
	public String getPatientAge() {
		return patientAge;
	}
	
	public void setPatientAge(String patientAge) {
		this.patientAge = patientAge;
	}
	
	public String getRegimenLine() {
		return regimenLine;
	}
	
	public void setRegimenLine(String regimenLine) {
		this.regimenLine = regimenLine;
	}
	
	public String getPickupDate() {
		return pickupDate;
	}
	
	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPatientId() {
		return patientId;
	}
	
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	
	public String getPatientWeight() {
		return patientWeight;
	}
	
	public void setPatientWeight(String patientWeight) {
		this.patientWeight = patientWeight;
	}
	
	public String getRegimenLine2() {
		return regimenLine2;
	}
	
	public void setRegimenLine2(String regimenLine2) {
		this.regimenLine2 = regimenLine2;
	}
	
	public String getDrugStrength() {
		return drugStrength;
	}
	
	public void setDrugStrength(String drugStrength) {
		this.drugStrength = drugStrength;
	}
	
	public String getDrugDosage() {
		return drugDosage;
	}
	
	public void setDrugDosage(String drugDosage) {
		this.drugDosage = drugDosage;
	}
	
	public String getPepfarId() {
		return pepfarId;
	}
	
	public void setPepfarId(String pepfarId) {
		this.pepfarId = pepfarId;
	}
	
	public String getWeightDate() {
		return weightDate;
	}
	
	public void setWeightDate(String weightDate) {
		this.weightDate = weightDate;
	}
	
	public String getArtStartDate() {
		return artStartDate;
	}
	
	public void setArtStartDate(String artStartDate) {
		this.artStartDate = artStartDate;
	}
	
	public String getDob() {
		return dob;
	}
	
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	public int getViralLoad() {
		return viralLoad;
	}
	
	public void setViralLoad(int viralLoad) {
		this.viralLoad = viralLoad;
	}
	
	public String getSampleCollectionDate() {
		return sampleCollectionDate;
	}
	
	public void setSampleCollectionDate(String sampleCollectionDate) {
		this.sampleCollectionDate = sampleCollectionDate;
	}
	
}
