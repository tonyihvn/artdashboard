/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.artdashboard.pojo;

/**
 * @author --
 */
public class Lab {
	
	private String artStartDate;
	
	private String pepfarId;
	
	private String patientAge;
	
	private String firstName;
	
	private String lastName;
	
	private String patientId;
	
	private String vlResult;
	
	private String sampleCollectionDate;
	
	private String secondToLastVl;
	
	private String secondToLastSampleCollectionDate;
	
	private String onMMD;
	
	private String regimenLine;
	
	private String encounterDate;
	
	private String cd4Count;
	
	public String getPepfarId() {
		return pepfarId;
	}
	
	public void setPepfarId(String pepfarId) {
		this.pepfarId = pepfarId;
	}
	
	public String getPatientAge() {
		return patientAge;
	}
	
	public void setPatientAge(String patientAge) {
		this.patientAge = patientAge;
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
	
	public String getVlResult() {
		return vlResult;
	}
	
	public void setVlResult(String vlResult) {
		this.vlResult = vlResult;
	}
	
	public String getSampleCollectionDate() {
		return sampleCollectionDate;
	}
	
	public void setSampleCollectionDate(String sampleCollectionDate) {
		this.sampleCollectionDate = sampleCollectionDate;
	}
	
	public String getSecondToLastVl() {
		return secondToLastVl;
	}
	
	public void setSecondToLastVl(String secondToLastVl) {
		this.secondToLastVl = secondToLastVl;
	}
	
	public String getSecondToLastSampleCollectionDate() {
		return secondToLastSampleCollectionDate;
	}
	
	public void setSecondToLastSampleCollectionDate(String secondToLastSampleCollectionDate) {
		this.secondToLastSampleCollectionDate = secondToLastSampleCollectionDate;
	}
	
	public String getOnMMD() {
		return onMMD;
	}
	
	public void setOnMMD(String onMMD) {
		this.onMMD = onMMD;
	}
	
	public String getArtStartDate() {
		return artStartDate;
	}
	
	public void setArtStartDate(String artStartDate) {
		this.artStartDate = artStartDate;
	}
	
	public String getRegimenLine() {
		return regimenLine;
	}
	
	public void setRegimenLine(String regimenLine) {
		this.regimenLine = regimenLine;
	}
	
	public String getEncounterDate() {
		return encounterDate;
	}
	
	public void setEncounterDate(String encounterDate) {
		this.encounterDate = encounterDate;
	}
	
	public String getCd4Count() {
		return cd4Count;
	}
	
	public void setCd4Count(String cd4Count) {
		this.cd4Count = cd4Count;
	}
	
}
