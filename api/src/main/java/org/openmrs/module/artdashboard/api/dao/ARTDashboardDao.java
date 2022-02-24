/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.artdashboard.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.artdashboard.Item;
import org.openmrs.module.artdashboard.Misc;
import org.openmrs.module.artdashboard.pojo.Lab;
import org.openmrs.module.artdashboard.pojo.Regimen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("artdashboard.ARTDashboardDao")
public class ARTDashboardDao {
	
	Connection con = null;
	
	public ARTDashboardDao() {
		Database.initConnection();
		
	}
	
	public JSONObject getRegimenData(String startDate, String endDate) {
            //indeed for some reason, doing a count was much slower than selecting the entire record and counting it. 
            //pretty strange.

            PreparedStatement stmt = null;
            ResultSet rs = null;
            Connection con = null;
            JSONObject regimenData = new JSONObject();
            
            List<Map<String,String>> allPatients = new ArrayList<>();
            
            try {
                /*
                    regimenData.put("ABC_3TC_LPVr", new JSONObject());
                    regimenData.put("ABC_3TC_DTG", new JSONObject());
                    regimenData.put("TDF_3TC_DTG", new JSONObject());
                    regimenData.put("AZT_3TC_LPV_r", new JSONObject());
                    regimenData.put("TDF_3TC_EFV", new JSONObject());
                    regimenData.put("ABC_3TC_EFV", new JSONObject());
                    regimenData.put("AZT_3TC_NVP", new JSONObject());
                    regimenData.put("ABC_3TC_ATV_r", new JSONObject());
                */
                   con = Database.connectionPool.getConnection();
                    //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
                    //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                    StringBuilder queryString = new StringBuilder("select patient.patient_id,  person.birthdate, obs.value_coded, encounter.encounter_datetime, " +
                        "concept_name.name " +
                        "FROM patient" +
                        "JOIN person ON person.person_id=patient.patient_id " +
                        "JOIN encounter ON encounter.patient_id=patient.patient_id AND encounter.form_id=27 " +
                        "JOIN obs ON obs.encounter_id=encounter.encounter_id AND obs.value_coded  IN (164506,164513,165702,164507,164514,165703) " +
                        "JOIN concept_name ON concept_name.concept_id=obs.value_coded " +
                        "WHERE patient.voided=0 AND " +
                        "encounter.encounter_datetime= (  SELECT MAX(encounter_datetime) FROM encounter lastpickup  AND encounter_datetime BETWEEN ? AND ? " +
                        " group by patient.patient_id");
                        
                    int i = 1;
                    //DateTime now = new DateTime(new Date());
                    //String nowString = now.toString("yyyy'-'MM'-'dd' 'HH':'mm");
                    stmt = con.prepareStatement(queryString.toString());
                    stmt.setString(i++, startDate);
                    stmt.setString(i++, endDate);
                    rs = stmt.executeQuery();
			
                    
			        while(rs.next())
                        {   
                            String age = Misc.getAge(rs.getString("birthdate"), rs.getString("encounter_datetime"));
                            String ageRange = Misc.getAgeRange(age);
                            int regimen = rs.getInt("value_coded");
                            String regimenLine = rs.getString("name");
                            //System.out.println(regimen);
                            
                           // String regimenKey = Misc.getRegimenLine(regimen);
                            String ageKey = Misc.getAgeBand(Integer.parseInt(age));
                            //System.out.println(regimenKey);
                            
                            if(!regimenData.has(regimenLine))
                            {
                                regimenData.put(regimenLine, new JSONObject());
                            }
                            
                            JSONObject ageData = (JSONObject)regimenData.get(regimenLine);
                            if(!ageData.has(ageKey))
                            {
                                ageData.put(ageKey, 0);
                            }
                            int ageCount = (int)ageData.get(ageKey);
                            ageCount++;
                            ageData.put(ageKey, ageCount);
                            regimenData.put(regimenLine, ageData);
                            //System.out.println(ageData.toString());
                            
                           
                        }
                        return regimenData;
            }
            catch (Exception ex) {
                    Database.handleException(ex);
                    return null;
            }
            finally {
                    Database.cleanUp(rs, stmt, con);
            }
	}
	
	public List<Regimen> getRegimenData2(String startDate, String endDate) {
            //indeed for some reason, doing a count was much slower than selecting the entire record and counting it. 
            //pretty strange.

            PreparedStatement stmt = null;
            ResultSet rs = null;
            
            List<Regimen> regimenData = new ArrayList<>();
            
            
            
            try {
               
                //regimenData.put("optimal_regimen", new JSONObject()); 
                //regimenData.put("sub_optimal_regimen", new JSONObject());
                //regimenData.put("misc", new JSONObject());
                con = Database.connectionPool.getConnection();
                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                StringBuilder queryString = new StringBuilder("select patient.patient_id AS patient_id, weightobs.obs_datetime AS weightDate, frequencyobs.value_coded AS frequency, patient_identifier.identifier AS pepfarId, regimenlineobs.value_coded AS regimenLine, weightobs.value_numeric AS weight, strengthobs.value_coded AS strength, dosageobs.value_numeric AS dosage, person_name.given_name, person_name.family_name,  person.birthdate, encounter.encounter_datetime, obs.value_coded, concept_name.name  \n" +
                                                            "FROM patient " +
                                                            
                                                            " JOIN obs weightobs ON weightobs.person_id=patient.patient_id AND weightobs.concept_id=5089 AND weightobs.voided=0 AND weightobs.obs_datetime=(SELECT MAX(obs_datetime) FROM obs WHERE obs.concept_id=5089 AND obs.person_id=weightobs.person_id AND obs.obs_datetime BETWEEN ? AND ? )"+
                                                            "JOIN person ON person.person_id=patient.patient_id " +
                                                            
                                                            "JOIN encounter ON encounter.patient_id=patient.patient_id AND encounter.form_id=27 " +
                                                            "JOIN person_name ON person_name.person_id=patient.patient_id  " + 	
                                                            " LEFT JOIN obs regimenlineobs ON regimenlineobs.concept_id=165708 AND regimenlineobs.person_id=patient.patient_id AND regimenlineobs.encounter_id=encounter.encounter_id AND regimenlineobs.voided=0 "+
                                                            " LEFT JOIN obs strengthobs ON strengthobs.concept_id=165725 AND strengthobs.encounter_id=encounter.encounter_id AND strengthobs.person_id=patient.patient_id AND strengthobs.obs_group_id IN (SELECT obs_id FROM obs WHERE concept_id=162240)"+
                                                            " LEFT JOIN obs dosageobs ON dosageobs.concept_id=166120 AND dosageobs.encounter_id=encounter.encounter_id AND dosageobs.person_id=patient.patient_id AND dosageobs.obs_group_id=strengthobs.obs_group_id  "+
                                                            " LEFT JOIN obs frequencyobs ON frequencyobs.concept_id=165723 AND frequencyobs.encounter_id=encounter.encounter_id AND frequencyobs.person_id=patient.patient_id AND frequencyobs.obs_group_id=strengthobs.obs_group_id  "+
                                                            "JOIN patient_identifier ON patient_identifier.patient_id=patient.patient_id AND patient_identifier.identifier_type=4 " +
                                                            "JOIN obs ON obs.encounter_id=encounter.encounter_id AND obs.concept_id IN (164506, 164513, 165702, 164507, 164514, 165703) " +
                                                            "JOIN concept_name ON concept_name.concept_id=obs.value_coded " +
                                                            "WHERE patient.voided=0 AND  " +
                                                            "encounter.encounter_datetime= (  SELECT MAX(encounter_datetime) FROM encounter lastpickup  " +
                                                            " WHERE lastpickup.patient_id=encounter.patient_id AND lastpickup.form_id=27 AND (lastpickup.encounter_datetime BETWEEN ? AND ?) ) " +
                                                            " group by patient.patient_id " );
                
                System.out.println(queryString.toString());

                int i = 1;
                //DateTime now = new DateTime(new Date());
                //String nowString = now.toString("yyyy'-'MM'-'dd' 'HH':'mm");
                stmt = con.prepareStatement(queryString.toString());
                stmt.setString(i++, startDate);
                stmt.setString(i++, endDate);
                stmt.setString(i++, startDate);
                stmt.setString(i++, endDate);
                rs = stmt.executeQuery();


                    while(rs.next())
                    {


                        String age = Misc.getAge(rs.getString("birthdate"), rs.getString("encounter_datetime"));
                        String ageRange = Misc.getAgeRange(age);
                        
                        int regimenConcept = rs.getInt("value_coded");
                        String regimenLine = rs.getString("name");
                        String regimenLine2 = Misc.getRegimenLine(rs.getInt("regimenLine"));
                        Regimen regimen = new Regimen();
                        regimen.setPatientAge(age);
                        regimen.setPickupDate(rs.getString("encounter_datetime"));
                        regimen.setRegimenConceptId(regimenConcept);
                        regimen.setRegimenLine(regimenLine);
                        regimen.setPatientWeight(rs.getString("weight"));
                        regimen.setFirstName(rs.getString("given_name"));
                        regimen.setLastName(rs.getString("family_name"));
                        regimen.setPatientId(rs.getString("pepfarId"));
                        regimen.setRegimenLine2(regimenLine2);
                        regimen.setPepfarId(rs.getString("pepfarId"));
                        regimen.setPatientId(rs.getString("patient_id"));
                        regimen.setWeightDate(rs.getString("weightDate"));
                        regimen.setDrugStrength(Misc.getDrugStrength(rs.getInt("strength")));
                        regimen.setDrugDosage(rs.getString("dosage")+" "+Misc.getDrugFrequency(rs.getInt("frequency")));
                        
                        regimenData.add(regimen);

                    }
                    return regimenData;
        }
        catch (Exception ex) {
                Database.handleException(ex);
                return null;
        }
        finally {
                Database.cleanUp(rs, stmt, con);
                //Database.cleanUp(rs, stmt, con);
        }
    }
	
	public List<Lab> getVlData(String startDate, String endDate) {
            //indeed for some reason, doing a count was much slower than selecting the entire record and counting it. 
            //pretty strange.

            PreparedStatement stmt = null;
            ResultSet rs = null;
            Connection con = null;
            List<Lab> labData = new ArrayList<>();
            
            try {
               
                //regimenData.put("optimal_regimen", new JSONObject()); 
                //regimenData.put("sub_optimal_regimen", new JSONObject());
                //regimenData.put("misc", new JSONObject());
                con = Database.connectionPool.getConnection();
                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                StringBuilder queryString = new StringBuilder("select patient_identifier.identifier AS pepfarId, encounter.encounter_datetime, patient.patient_id, person_name.given_name, person_name.family_name,  person.birthdate, vlobs.value_numeric AS vlresult, samplecollection.value_datetime AS samplecollectiondate from patient " +
                                       " JOIN person ON person.person_id=patient.patient_id " +
                                        " JOIN person_name ON person_name.person_id=patient.patient_id " +
                                        " JOIN patient_identifier ON patient_identifier.patient_id=patient.patient_id AND patient_identifier.identifier_type=4	" +
                                        " JOIN obs vlobs ON vlobs.concept_id=856 AND vlobs.person_id=patient.patient_id AND vlobs.voided=0 AND vlobs.value_numeric IS NOT NULL " +
                                        " LEFT JOIN obs samplecollection ON samplecollection.concept_id=159951 AND samplecollection.encounter_id=vlobs.encounter_id AND samplecollection.voided=0 " +
                                        " JOIN encounter ON encounter.encounter_id=vlobs.encounter_id " +
                                        " where patient.voided=0  " +
                                        " AND vlobs.obs_datetime=(SELECT MAX(obs_datetime) FROM obs WHERE concept_id=856 AND obs.person_id=vlobs.person_id AND obs.voided=0) " +
                                        " AND encounter.encounter_datetime BETWEEN ? AND ? " +
                                        " group by patient.patient_id" );
                 	
                System.out.println(queryString.toString());

                int i = 1;
                //DateTime now = new DateTime(new Date());
                //String nowString = now.toString("yyyy'-'MM'-'dd' 'HH':'mm");
                stmt = con.prepareStatement(queryString.toString());
                stmt.setString(i++, startDate);
                stmt.setString(i++, endDate);
                rs = stmt.executeQuery();
                
                while(rs.next())
                {

                    String age = Misc.getAge(rs.getString("birthdate"), rs.getString("encounter_datetime"));
                    String ageRange = Misc.getAgeRange(age);
                    Lab lab = new Lab();
                    lab.setPatientAge(age);

                    lab.setFirstName(rs.getString("given_name"));
                    lab.setLastName(rs.getString("family_name"));
                    lab.setPatientId(rs.getString("pepfarId"));
                    lab.setPepfarId(rs.getString("pepfarId"));
                    lab.setSampleCollectionDate(rs.getString("samplecollectiondate"));
                    lab.setVlResult(rs.getString("vlresult"));
                    labData.add(lab);
                }
                return labData;
        }
        catch (Exception ex) {
                Database.handleException(ex);
                return null;
        }
        finally {
                Database.cleanUp(rs, stmt, con);
        }
    }
	
	public List<Lab> getMMDData(String startDate, String endDate, String sixMonthsAgo) {
            //indeed for some reason, doing a count was much slower than selecting the entire record and counting it. 
            //pretty strange.

            PreparedStatement stmt = null;
            ResultSet rs = null;
            Connection con = null;
            List<Lab> labData = new ArrayList<>();
            
            
            
            try {
               
                //regimenData.put("optimal_regimen", new JSONObject()); 
                //regimenData.put("sub_optimal_regimen", new JSONObject());
                //regimenData.put("misc", new JSONObject());
                con = Database.connectionPool.getConnection();
                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                StringBuilder queryString = new StringBuilder("SELECT patient_identifier.identifier AS pepfarId,  IFNULL(mmdobs.value_coded, 0) AS mmd, artobs.value_datetime AS artStartDate, patient.patient_id, person_name.given_name, person_name.family_name,    person.birthdate, lastvl.obs_datetime AS vlvisitdate, lastvl.value_numeric AS vlresult, secondtolastvl.obs_datetime AS secondvldate,  secondtolastvl.value_numeric AS secondvlresult  from patient\n" +
                                "	JOIN person ON person.person_id=patient.patient_id\n" +
                                "	 JOIN person_name ON person_name.person_id=patient.patient_id \n" +
                                "	 JOIN patient_identifier ON patient_identifier.patient_id=patient.patient_id AND patient_identifier.identifier_type=4\n" +
                                "	LEFT JOIN obs artobs ON artobs.concept_id=159599 AND artobs.person_id=patient.patient_id AND artobs.value_datetime <  ? " +//six months ago
                                "	LEFT JOIN obs lastvl ON lastvl.person_id=patient.patient_id AND\n" +
                                "	lastvl.concept_id=856 AND lastvl.voided=0 AND\n" +
                                "	lastvl.obs_datetime=(SELECT obs_datetime FROM obs  WHERE obs.concept_id=856  AND voided=0 AND obs.person_id=lastvl.person_id AND obs.obs_datetime BETWEEN ? AND ?  ORDER BY obs_datetime DESC LIMIT 0,1  )\n" +
                                "	LEFT JOIN obs secondtolastvl ON secondtolastvl.person_id=patient.patient_id AND\n" +
                                "	secondtolastvl.concept_id=856 AND secondtolastvl.voided=0 AND\n" +
                                "	secondtolastvl.obs_datetime=(SELECT obs_datetime FROM obs  WHERE obs.concept_id=856  AND voided=0 AND obs.person_id=secondtolastvl.person_id AND obs.obs_datetime BETWEEN ? AND ?  ORDER BY obs_datetime DESC LIMIT 1,1  )\n" +
                                "     LEFT JOIN obs mmdobs ON ((mmdobs.concept_id=166148 AND  mmdobs.value_coded=166149) OR mmdobs.concept_id=166278) AND mmdobs.person_id=patient.patient_id\n" +
                                " AND mmdobs.obs_datetime=(SELECT  obs_datetime FROM obs  WHERE ((mmdobs.concept_id=166148 AND  mmdobs.value_coded=166149) OR mmdobs.concept_id=166278) AND voided=0 AND obs.person_id=mmdobs.person_id AND obs.obs_datetime BETWEEN ? AND ?  ORDER BY obs_datetime DESC LIMIT 0,1 ) "+
                                "	 where patient.voided=0  AND  TIMESTAMPDIFF(YEAR,person.birthdate, ?) > 5" + //end date
                                "     AND lastvl.value_numeric <1000 AND secondtolastvl.value_numeric <1000  " +
                                "     AND TIMESTAMPDIFF(MONTH,  secondtolastvl.obs_datetime, lastvl.obs_datetime) >=6 " +
                                "	group by patient.patient_id \n" +
                                "    \n" +
                                "	ORDER BY artStartDate ASC" );
                 	
                System.out.println(queryString.toString());

                int i = 1;
                //DateTime now = new DateTime(new Date());
                //String nowString = now.toString("yyyy'-'MM'-'dd' 'HH':'mm");
                stmt = con.prepareStatement(queryString.toString());
                stmt.setString(i++, sixMonthsAgo);
                stmt.setString(i++, startDate);
                stmt.setString(i++, endDate);
                stmt.setString(i++, startDate);
                stmt.setString(i++, endDate);
                 stmt.setString(i++, startDate);
                stmt.setString(i++, endDate);
                stmt.setString(i++, endDate);
                rs = stmt.executeQuery();
                
                while(rs.next())
                {

                    String age = Misc.getAge(rs.getString("birthdate"), endDate);
                    String ageRange = Misc.getAgeRange(age);
                    Lab lab = new Lab();
                    lab.setPatientAge(age);

                    lab.setFirstName(rs.getString("given_name"));
                    lab.setLastName(rs.getString("family_name"));
                    lab.setPatientId(rs.getString("pepfarId"));
                    lab.setPepfarId(rs.getString("pepfarId"));
                    lab.setArtStartDate(rs.getString("artStartDate"));
                    lab.setSampleCollectionDate(rs.getString("vlvisitdate"));
                    lab.setVlResult(rs.getString("vlresult"));
                    lab.setSecondToLastSampleCollectionDate(rs.getString("secondvldate"));
                    lab.setSecondToLastVl(rs.getString("secondvlresult"));
                    if(rs.getInt("mmd") == 0)
                    {
                        lab.setOnMMD("no");
                    }else{
                        lab.setOnMMD("yes");
                    }
                    
                    
                    labData.add(lab);
                }
                return labData;
        }
        catch (Exception ex) {
                Database.handleException(ex);
                return null;
        }
        finally {
                Database.cleanUp(rs, stmt, con);
        }
    }
	
	public List<Lab> getEACData(String startDate, String endDate) {
            //indeed for some reason, doing a count was much slower than selecting the entire record and counting it. 
            //pretty strange.

            PreparedStatement stmt = null;
            ResultSet rs = null;
            Connection con = null;
            List<Lab> labData = new ArrayList<>();
            
            
            try {
               
                //regimenData.put("optimal_regimen", new JSONObject()); 
                //regimenData.put("sub_optimal_regimen", new JSONObject());
                //regimenData.put("misc", new JSONObject());
                con = Database.connectionPool.getConnection();
                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                StringBuilder queryString = new StringBuilder("SELECT patient_identifier.identifier AS pepfarId, IFNULL(regimenobs.value_coded, 0) As regimenline, artobs.value_datetime AS artStartDate, eacenc.encounter_datetime, patient.patient_id, person_name.given_name, person_name.family_name,    person.birthdate, lastvl.obs_datetime AS vlvisitdate, lastvl.value_numeric AS vlresult, secondtolastvl.obs_datetime AS secondvldate,  secondtolastvl.value_numeric AS secondvlresult  from patient\n" +
                            "		 JOIN person ON person.person_id=patient.patient_id\n" +
                            "			  JOIN person_name ON person_name.person_id=patient.patient_id \n" +
                            "			  JOIN patient_identifier ON patient_identifier.patient_id=patient.patient_id AND patient_identifier.identifier_type=4\n" +
                            "			 LEFT JOIN obs artobs ON artobs.concept_id=159599 AND artobs.person_id=patient.patient_id \n" +
                            "			JOIN encounter eacenc ON eacenc.patient_id=patient.patient_id AND eacenc.form_id=69 AND eacenc.voided=0\n" +
                            "			AND eacenc.encounter_datetime=(SELECT encounter_datetime from encounter where form_id=69 AND encounter.patient_id=eacenc.patient_id AND encounter.voided=0 ORDER BY encounter.encounter_datetime DESC LIMIT 0,1)\n" +
                            "            LEFT JOIN obs regimenobs On regimenobs.concept_id=165708 AND regimenobs.value_coded=164507 AND\n" +
                            "            regimenobs.obs_datetime=(SELECT obs_datetime FROM obs  WHERE obs.concept_id=164507  AND voided=0 AND obs.person_id=regimenobs.person_id  ORDER BY obs_datetime DESC LIMIT 0,1  )\n" +
                            "            LEFT JOIN obs lastvl ON lastvl.person_id=patient.patient_id AND\n" +
                            "			lastvl.concept_id=856 AND lastvl.voided=0 AND\n" +
                            "			lastvl.obs_datetime=(SELECT obs_datetime FROM obs  WHERE obs.concept_id=856  AND voided=0 AND obs.person_id=lastvl.person_id AND obs.obs_datetime <= eacenc.encounter_datetime  ORDER BY obs_datetime DESC LIMIT 0,1  )\n" +
                            "			LEFT JOIN obs secondtolastvl ON secondtolastvl.person_id=patient.patient_id AND\n" +
                            "			secondtolastvl.concept_id=856 AND secondtolastvl.voided=0 AND\n" +
                            "			secondtolastvl.obs_datetime=(SELECT obs_datetime FROM obs  WHERE obs.concept_id=856  AND voided=0 AND obs.person_id=secondtolastvl.person_id AND obs.obs_datetime > eacenc.encounter_datetime  ORDER BY obs_datetime DESC LIMIT 0,1  )\n" +
                            "		\n" +
                            "			 where patient.voided=0 AND TIMESTAMPDIFF(YEAR,person.birthdate, ?) <= 150 \n" +
                            "GROUP BY patient.patient_id " );
                 	
                System.out.println(queryString.toString());

                int i = 1;
                //DateTime now = new DateTime(new Date());
                //String nowString = now.toString("yyyy'-'MM'-'dd' 'HH':'mm");
                stmt = con.prepareStatement(queryString.toString());
                stmt.setString(i++, endDate);
                rs = stmt.executeQuery();
                
                while(rs.next())
                {

                    String age = Misc.getAge(rs.getString("birthdate"), endDate);
                    String ageRange = Misc.getAgeRange(age);
                    Lab lab = new Lab();
                    lab.setPatientAge(age);

                    lab.setFirstName(rs.getString("given_name"));
                    lab.setLastName(rs.getString("family_name"));
                    lab.setPatientId(rs.getString("pepfarId"));
                    lab.setPepfarId(rs.getString("pepfarId"));
                    lab.setArtStartDate(rs.getString("artStartDate"));
                    lab.setSampleCollectionDate(rs.getString("vlvisitdate"));
                    lab.setVlResult(rs.getString("vlresult"));
                    lab.setSecondToLastSampleCollectionDate(rs.getString("secondvldate"));
                    lab.setSecondToLastVl(rs.getString("secondvlresult"));
                    lab.setRegimenLine(rs.getString("regimenline"));
                    lab.setEncounterDate(rs.getString("encounter_datetime"));
                    
                    
                    labData.add(lab);
                }
                return labData;
        }
        catch (Exception ex) {
                Database.handleException(ex);
                return null;
        }
        finally {
                Database.cleanUp(rs, stmt, con);
        }
    }
	
	public List<Lab> getCD4Data(String startDate, String endDate) {
            //indeed for some reason, doing a count was much slower than selecting the entire record and counting it. 
            //pretty strange.

            PreparedStatement stmt = null;
            ResultSet rs = null;
            Connection con = null;
            List<Lab> labData = new ArrayList<>();
            
            
            
            try {
               
                //regimenData.put("optimal_regimen", new JSONObject()); 
                //regimenData.put("sub_optimal_regimen", new JSONObject());
                //regimenData.put("misc", new JSONObject());
                con = Database.connectionPool.getConnection();
                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                StringBuilder queryString = new StringBuilder("SELECT patient_identifier.identifier AS pepfarId, cd4obs.value_numeric AS cd4, artobs.value_datetime AS artStartDate, labenc.encounter_datetime, patient.patient_id, person_name.given_name, person_name.family_name,    person.birthdate  from patient\n" +
                                "		 JOIN person ON person.person_id=patient.patient_id\n" +
                                "			  JOIN person_name ON person_name.person_id=patient.patient_id \n" +
                                "			  JOIN patient_identifier ON patient_identifier.patient_id=patient.patient_id AND patient_identifier.identifier_type=4\n" +
                                "			LEFT  JOIN obs artobs ON artobs.concept_id=159599 AND artobs.person_id=patient.patient_id \n" +
                                "			JOIN encounter labenc ON labenc.patient_id=patient.patient_id AND labenc.form_id=21 AND labenc.voided=0\n" +
                                "			AND labenc.encounter_datetime=(SELECT encounter_datetime from encounter where form_id=21 AND encounter.patient_id=labenc.patient_id AND encounter.voided=0 ORDER BY encounter.encounter_datetime DESC LIMIT 0,1)\n" +
                                "			JOIN obs cd4obs ON cd4obs.person_id=patient.patient_id AND\n" +
                                "			cd4obs.concept_id=5497 AND cd4obs.voided=0 AND\n" +
                                "			cd4obs.obs_datetime=(SELECT obs_datetime FROM obs  WHERE obs.concept_id=5497  AND voided=0 AND obs.person_id=cd4obs.person_id  ORDER BY obs_datetime DESC LIMIT 0,1  )\n" +
                                "			\n" +
                                "			 where patient.voided=0 AND labenc.encounter_datetime BETWEEn ? AND ? " +
                                "GROUP BY patient.patient_id " );
                 	
                System.out.println(queryString.toString());

                int i = 1;
                //DateTime now = new DateTime(new Date());
                //String nowString = now.toString("yyyy'-'MM'-'dd' 'HH':'mm");
                stmt = con.prepareStatement(queryString.toString());
                stmt.setString(i++, startDate);
                stmt.setString(i++, endDate);
                rs = stmt.executeQuery();
                
                while(rs.next())
                {

                    String age = Misc.getAge(rs.getString("birthdate"), endDate);
                    String ageRange = Misc.getAgeRange(age);
                    Lab lab = new Lab();
                    lab.setPatientAge(age);

                    lab.setFirstName(rs.getString("given_name"));
                    lab.setLastName(rs.getString("family_name"));
                    lab.setPatientId(rs.getString("pepfarId"));
                    lab.setPepfarId(rs.getString("pepfarId"));
                    lab.setArtStartDate(rs.getString("artStartDate"));
                    lab.setCd4Count(rs.getString("cd4"));
                    lab.setEncounterDate(rs.getString("encounter_datetime"));
                    
                    
                    labData.add(lab);
                }
                return labData;
        }
        catch (Exception ex) {
                Database.handleException(ex);
                return null;
        }
        finally {
                Database.cleanUp(rs, stmt, con);
        }
    }
	
	public List<Regimen> getActivePeds(String startDate, String endDate) {
	    // System.out.println(startDate);
        // System.out.println(endDate);	
         
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        List<Regimen> allPatients = new ArrayList<>();
        try {
                con = Database.connectionPool.getConnection();
                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                StringBuilder queryString = new StringBuilder("select patient.patient_id, person.birthdate,  TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) AS age,  person_name.given_name, person_name.family_name, patient_identifier.identifier FROM patient \n" +
                                " LEFT JOIN encounter pharmEnc ON pharmEnc.patient_id=patient.patient_id AND pharmEnc.form_id=27 " +
                                " JOIN person ON person.person_id=patient.patient_id " +
                                " JOIN person_name ON person_name.person_id=patient.patient_id " +
                                " LEFT JOIN patient_identifier ON patient_identifier.patient_id=patient.patient_id AND patient_identifier.identifier_type=4 " +
                                " LEFT JOIN obs refillobs ON refillobs.encounter_id=pharmEnc.encounter_id AND refillobs.concept_id=159368 AND refillobs.voided=0 " +
                                "  AND refillobs.obs_group_id IN (select obs_id from obs where concept_id=162240)  " +
                                " LEFT JOIN obs terminationobs ON terminationobs.concept_id=165586 AND terminationobs.person_id=patient.patient_id " +
                                " WHERE   " +
                                " DATE_ADD(pharmEnc.encounter_datetime,  INTERVAL (refillobs.value_numeric+28) DAY) >= ? " +
                                "   AND pharmEnc.encounter_datetime= ( " +
                                "  SELECT encounter_datetime FROM encounter where form_id=27 AND encounter.patient_id=pharmEnc.patient_id  AND " +
                                "   encounter_datetime <=? ORDER BY encounter_datetime DESC LIMIT 0,1 )  " +
                                "  AND (terminationobs.value_coded IS NULL OR terminationobs.value_coded !=1066 )  " +

                          " AND (TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) > 0 AND TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) <=150) GROUP BY patient.patient_id ") ;
            //                          " AND (TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) > 0 AND TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) <=100) GROUP BY patient.patient_id ");

                int i = 1;
                stmt = con.prepareStatement(queryString.toString());
                //stmt.setString(i++, startDate);
                stmt.setString(i++, endDate);
                stmt.setString(i++, endDate);
                rs = stmt.executeQuery();
                
                while (rs.next()) {
                    String patientId = rs.getString("patient_id");
                    String age = rs.getString("age");
                    String patientIdentifier = rs.getString("identifier");
                    String firstName = rs.getString("given_name");
                    String lastName = rs.getString("family_name");
                    Regimen regimen = new Regimen();
                    regimen.setFirstName(firstName);
                    regimen.setLastName(lastName);
                    regimen.setDob(rs.getString("birthdate"));
                    regimen.setPepfarId(patientIdentifier);
                    regimen.setPatientAge(age);
                    regimen.setPatientId(patientId);
                    
                    allPatients.add(regimen);
                }
                return allPatients;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
                Database.handleException(ex);
                return null;
        }
        finally {
                Database.cleanUp(rs, stmt, con);
        }
    }
	
	public List<Regimen> getTXML(String curQuarterendDate, String lastQuarterendDate) {
	    // System.out.println(startDate);
        // System.out.println(endDate);	
         
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        List<Regimen> allPatients = new ArrayList<>();
        try {
                con = Database.connectionPool.getConnection();
                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                StringBuilder queryString = new StringBuilder("select patient.patient_id, person.birthdate,  TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) AS age,  person_name.given_name, person_name.family_name, patient_identifier.identifier FROM patient \n" +
                                " LEFT JOIN encounter pharmEnc ON pharmEnc.patient_id=patient.patient_id AND pharmEnc.form_id=27 " +
                                " JOIN person ON person.person_id=patient.patient_id " +
                                " JOIN person_name ON person_name.person_id=patient.patient_id " +
                                " LEFT JOIN patient_identifier ON patient_identifier.patient_id=patient.patient_id AND patient_identifier.identifier_type=4 " +
                                " LEFT JOIN obs refillobs ON refillobs.encounter_id=pharmEnc.encounter_id AND refillobs.concept_id=159368 AND refillobs.voided=0 " +
                                "  AND refillobs.obs_group_id IN (select obs_id from obs where concept_id=162240)  " +
                                " LEFT JOIN obs terminationobs ON terminationobs.concept_id=165586 AND terminationobs.person_id=patient.patient_id " +
                                " WHERE   " +
                                " DATE_ADD(pharmEnc.encounter_datetime,  INTERVAL (refillobs.value_numeric+28) DAY) >= ? " +
                                "   AND pharmEnc.patient_id NOT IN ( " +
                                "  SELECT patient_id FROM encounter where form_id=27 AND encounter.patient_id=pharmEnc.patient_id  AND " +
                                "   encounter_datetime BETWEEN ? AND ? )  " +
                                "  AND (terminationobs.value_coded IS NULL OR terminationobs.value_coded !=1066)  " +

                          " AND (TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) > 0 AND TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) <=150) GROUP BY patient.patient_id ") ;
                        // " AND (TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) > 0 AND TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) <=100) GROUP BY patient.patient_id ");
                /*
                String query = "SELECT DISTINCT patient.patient_id, patient_identifier.identifier AS pepfar_id, person_attribute.value AS phone_number, obs.value_datetime AS next_date FROM patient"
                        + " LEFT JOIN patient_identifier ON patient_identifier.patient_id=patient.patient_id AND patient_identifier.identifier_type=4 "
                        + " LEFT JOIN person_attribute ON person_attribute.person_id=patient.patient_id AND person_attribute.person_attribute_type_id=8"
                        + " JOIN obs ON obs.person_id=patient.patient_id AND obs.concept_id=5096 AND DATE_ADD(obs.encounter_datetime,  INTERVAL 28 DAY) >= ? "
                        + " where patient.voided=0 AND person_attribute.voided=0 LIMIT 100";
                */
                int i = 1;
                stmt = con.prepareStatement(queryString.toString());
                //stmt.setString(i++, startDate);
                stmt.setString(i++, lastQuarterendDate);
                stmt.setString(i++, lastQuarterendDate);
                stmt.setString(i++, curQuarterendDate);
                rs = stmt.executeQuery();
                
                while (rs.next()) {
                    String patientId = rs.getString("patient_id");
                    String age = rs.getString("age");
                    String patientIdentifier = rs.getString("identifier");
                    String firstName = rs.getString("given_name");
                    String lastName = rs.getString("family_name");
                    Regimen regimen = new Regimen();
                    regimen.setFirstName(firstName);
                    regimen.setLastName(lastName);
                    regimen.setDob(rs.getString("birthdate"));
                    regimen.setPepfarId(patientIdentifier);
                    regimen.setPatientAge(age);
                    regimen.setPatientId(patientId);
                    
                    allPatients.add(regimen);
                }
                return allPatients;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
                Database.handleException(ex);
                return null;
        }
        finally {
                Database.cleanUp(rs, stmt, con);
        }
    }
	
	public List<Regimen> getPedsTxNew(String startDate, String endDate) {
	    System.out.println(startDate);
        System.out.println(endDate);	
         
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        List<Regimen> allPatients = new ArrayList<>();
        try {
                con = Database.connectionPool.getConnection();
                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                StringBuilder queryString = new StringBuilder("select patient.patient_id, " +
                    "        pid1.identifier as hospitalId, " +
                    "	 pid2.identifier as pepfarId, " +
                    "	person.gender," +
                    "	person.birthdate, " +
                    "	TIMESTAMPDIFF(YEAR,person.birthdate,curdate()) as Age," +
                    "	patient_program.date_enrolled as EnrollmentDate," +
                    "	MAX(IF(obs.concept_id=159599,DATE_FORMAT(obs.value_datetime,'%d-%b-%Y'), NULL)) as ARTStartDate " +
                    "	from patient " +
                    "	left join patient_identifier pid1 on(pid1.patient_id=patient.patient_id and pid1.identifier_type=5 and pid1.voided=0) " +
                    "	left join patient_identifier pid2 on(pid2.patient_id=patient.patient_id and pid2.identifier_type=4 and pid2.voided=0) " +
                    "	left join person on(person.person_id=patient.patient_id and person.voided=0) " +
                    "	left join patient_program on(patient_program.patient_id=patient.patient_id and patient_program.program_id=1 and patient_program.voided=0)" +
                    "	LEFT JOIN encounter enc on(enc.patient_id=patient.patient_id and enc.form_id IN(23,56) and enc.voided=0)" +
                    "	LEFT JOIN obs on(enc.encounter_id=obs.encounter_id and obs.voided=0) " +
                    "	left join concept_name cn1 on(obs.value_coded=cn1.concept_id and cn1.locale='en' and cn1.locale_preferred=1) " +
                    "	where patient.voided=0 and enc.voided=0 " +
                    "	GROUP BY patient.patient_id " +
                    "	HAVING " +
                    "    MAX(IF(obs.concept_id=159599,obs.value_datetime, NULL)) BETWEEN ? AND ?");

                int i = 1;
                stmt = con.prepareStatement(queryString.toString());
                //stmt.setString(i++, startDate);
                stmt.setString(i++, startDate);
                stmt.setString(i++, endDate);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    
                    String patientId = rs.getString("patient_id");
                    String age = rs.getString("age");
                    String patientIdentifier = rs.getString("pepfarId");
                    Regimen regimen = new Regimen();
                    regimen.setDob(rs.getString("birthdate"));
                    regimen.setArtStartDate(rs.getString("ARTStartDate"));
                    regimen.setPepfarId(patientIdentifier);
                    regimen.setPatientAge(age);
                    regimen.setPatientId(patientId);
                    allPatients.add(regimen);
                }
                return allPatients;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
                Database.handleException(ex);
                return null;
        }
        finally {
                Database.cleanUp(rs, stmt, con);
        }
    }
	
	public List<Regimen> getPedsTxPVLS(String oneYearAgo, String endDate) {
	
         
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        List<Regimen> allPatients = new ArrayList<>();
        try {
                con = Database.connectionPool.getConnection();
                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                StringBuilder queryString = new StringBuilder("select patient.patient_id,  person.birthdate, TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) AS age, samplecollectionobs.value_datetime AS sample_collection_date, vlobs.value_numeric AS vl,  person_name.given_name, person_name.family_name, patient_identifier.identifier, "+
                         " 	MAX(IF(obs.concept_id=159599,DATE_FORMAT(obs.value_datetime,'%d-%b-%Y'), NULL)) as ARTStartDate  FROM patient " +
                        "	LEFT JOIN encounter enc on(enc.patient_id=patient.patient_id and enc.form_id IN(23,56) and enc.voided=0)" +
                        "	LEFT JOIN obs on(enc.encounter_id=obs.encounter_id and obs.voided=0) " +
                                                            "	JOIN encounter labEnc ON labEnc.patient_id=patient.patient_id AND labEnc.form_id=21 AND " +
                                                            "  labEnc.encounter_datetime=( " +
                                                            " SELECT encounter_datetime FROM encounter where form_id=21 AND encounter.patient_id=labEnc.patient_id   " +
                                                            "  AND (encounter.encounter_datetime BETWEEN ? AND ?)  ORDER BY encounter_datetime DESC LIMIT 0,1 " +
                                                            "  )" +
                                                            "                         JOIN person ON person.person_id=patient.patient_id" +
                                                            "	JOIN person_name ON person_name.person_id=patient.patient_id " +
                                                            "	LEFT JOIN patient_identifier ON patient_identifier.patient_id=patient.patient_id AND patient_identifier.identifier_type=4 " +
                                                            "	JOIN obs vlobs ON vlobs.encounter_id=labEnc.encounter_id AND vlobs.concept_id=856 AND vlobs.voided=0 " +
                                                            "	JOIN obs samplecollectionobs ON samplecollectionobs.encounter_id=labEnc.encounter_id AND samplecollectionobs.concept_id=159951 " +
                                                            "  WHERE vlobs.value_numeric IS NOT NULL AND  (TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) > 0 AND TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) <=150) GROUP BY patient.patient_id ");

                int i = 1;
                stmt = con.prepareStatement(queryString.toString());
                //stmt.setString(i++, startDate);
                stmt.setString(i++, oneYearAgo);
                stmt.setString(i++, endDate);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    
                    String patientId = rs.getString("patient_id");
                    String age = rs.getString("age");
                    String patientIdentifier = rs.getString("identifier");
                    String firstName = rs.getString("given_name");
                    String lastName = rs.getString("family_name");
                    Regimen regimen = new Regimen();
                    regimen.setSampleCollectionDate(rs.getString("sample_collection_date"));
                    regimen.setDob(rs.getString("birthdate"));
                    regimen.setViralLoad(rs.getInt("vl"));
                    regimen.setArtStartDate(rs.getString("ARTStartDate"));
                    regimen.setFirstName(firstName);
                    regimen.setLastName(lastName);
                    regimen.setPepfarId(patientIdentifier);
                    regimen.setPatientAge(age);
                    regimen.setPatientId(patientId);
                    allPatients.add(regimen);
                }
                return allPatients;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
                Database.handleException(ex);
                return null;
        }
        finally {
                Database.cleanUp(rs, stmt, con);
        }
    }
	
	public List<Regimen> getPedsLTFU(String startDate, String endDate) {
	    System.out.println(startDate);
        System.out.println(endDate);	
         
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        List<Regimen> allPatients = new ArrayList<>();
        try {
                con = Database.connectionPool.getConnection();
                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                StringBuilder queryString = new StringBuilder("select patient.patient_id, person.birthdate,  TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) AS age,  person_name.given_name, person_name.family_name, patient_identifier.identifier FROM patient \n" +
                                " LEFT JOIN encounter pharmEnc ON pharmEnc.patient_id=patient.patient_id AND pharmEnc.form_id=27 " +
                                " JOIN person ON person.person_id=patient.patient_id " +
                                " JOIN person_name ON person_name.person_id=patient.patient_id " +
                                " LEFT JOIN patient_identifier ON patient_identifier.patient_id=patient.patient_id AND patient_identifier.identifier_type=4 " +
                                " LEFT JOIN obs refillobs ON refillobs.encounter_id=pharmEnc.encounter_id AND refillobs.concept_id=159368 AND refillobs.voided=0 " +
                                "  AND refillobs.obs_group_id IN (select obs_id from obs where concept_id=162240)  " +
                                " LEFT JOIN obs terminationobs ON terminationobs.concept_id=165586 AND terminationobs.person_id=patient.patient_id " +
                                " WHERE   " +
                                " DATE_ADD(pharmEnc.encounter_datetime,  INTERVAL (refillobs.value_numeric+28) DAY) < ? " +
                                "   AND pharmEnc.encounter_datetime= ( " +
                                "  SELECT encounter_datetime FROM encounter where form_id=27 AND encounter.patient_id=pharmEnc.patient_id  AND " +
                                "   encounter_datetime <=? ORDER BY encounter_datetime DESC LIMIT 0,1 )  " +
                                "  AND (terminationobs.value_coded IS NULL OR terminationobs.value_coded !=1066 )  " +
                                " AND (TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) > 0 AND TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) <=150) GROUP BY patient.patient_id ");

                int i = 1;
                stmt = con.prepareStatement(queryString.toString());
                //stmt.setString(i++, startDate);
                stmt.setString(i++, endDate);
                stmt.setString(i++, endDate);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    
                    String patientId = rs.getString("patient_id");
                    String age = rs.getString("age");
                    String patientIdentifier = rs.getString("identifier");
                    String firstName = rs.getString("given_name");
                    String lastName = rs.getString("family_name");
                    Regimen regimen = new Regimen();
                    regimen.setFirstName(firstName);
                    regimen.setLastName(lastName);
                    regimen.setPepfarId(patientIdentifier);
                    regimen.setPatientAge(age);
                    regimen.setPatientId(patientId);
                    
                    allPatients.add(regimen);
                }
                return allPatients;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
                Database.handleException(ex);
                return null;
        }
        finally {
                Database.cleanUp(rs, stmt, con);
        }
    }
	
	public List<Regimen> getTxRTT(String startDate, String endDate){
	    System.out.println(startDate);
        System.out.println(endDate);	
         
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        List<Regimen> allPatients = new ArrayList<>();
        try {
                con = Database.connectionPool.getConnection();
                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                StringBuilder queryString = new StringBuilder("select patient.patient_id, person.birthdate, spharmEnc.encounter_datetime, srefillobs.value_numeric, pharmEnc.encounter_datetime,   TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) AS age,  person_name.given_name, person_name.family_name, patient_identifier.identifier FROM patient " +
                                        "  JOIN encounter spharmEnc ON spharmEnc.patient_id=patient.patient_id AND spharmEnc.form_id=27 " +
                                        "   AND  spharmEnc.encounter_datetime= ( " +
                                        " SELECT encounter_datetime FROM encounter where form_id=27 AND encounter.patient_id=spharmEnc.patient_id  AND " +
                                        "  spharmEnc.encounter_datetime <=? ORDER BY encounter_datetime DESC LIMIT 1,1 )  " +
                                        "  JOIN encounter pharmEnc ON pharmEnc.patient_id=patient.patient_id AND pharmEnc.form_id=27 " +
                                        " AND  pharmEnc.encounter_datetime= ( " +
                                        "  SELECT encounter_datetime FROM encounter where form_id=27 AND encounter.patient_id=pharmEnc.patient_id  AND " +
                                        "  pharmEnc.encounter_datetime <=? ORDER BY encounter_datetime DESC LIMIT 0,1 )  " +
                                        "   JOIN obs srefillobs ON srefillobs.encounter_id=spharmEnc.encounter_id AND srefillobs.concept_id=159368 AND srefillobs.voided=0 " +
                                        "    AND srefillobs.obs_group_id IN (select obs_id from obs where concept_id=162240) " +
                                        "   JOIN obs refillobs ON refillobs.encounter_id=pharmEnc.encounter_id AND refillobs.concept_id=159368 AND refillobs.voided=0 " +
                                        "                                  AND refillobs.obs_group_id IN (select obs_id from obs where concept_id=162240)  " +
                                        "   JOIN person ON person.person_id=patient.patient_id " +
                                        "   JOIN person_name ON person_name.person_id=patient.patient_id " +
                                        "   LEFT JOIN patient_identifier ON patient_identifier.patient_id=patient.patient_id AND patient_identifier.identifier_type=4 " +
                                        "   LEFT JOIN obs terminationobs ON terminationobs.concept_id=165586 AND terminationobs.person_id=patient.patient_id " +
                                        "   WHERE  " +
                                        "    DATE_ADD(spharmEnc.encounter_datetime,  INTERVAL (refillobs.value_numeric+28) DAY) < pharmEnc.encounter_datetime" +
                                        "    AND " +
                                        "                                  (TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) > 0 AND TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) <=150) GROUP BY patient.patient_id ");

                int i = 1;
                stmt = con.prepareStatement(queryString.toString());
                //stmt.setString(i++, startDate);
                stmt.setString(i++, endDate);
                stmt.setString(i++, endDate);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    
                    String patientId = rs.getString("patient_id");
                    String age = rs.getString("age");
                    String patientIdentifier = rs.getString("identifier");
                    String firstName = rs.getString("given_name");
                    String lastName = rs.getString("family_name");
                    Regimen regimen = new Regimen();
                    regimen.setFirstName(firstName);
                    regimen.setLastName(lastName);
                    regimen.setPepfarId(patientIdentifier);
                    regimen.setPatientAge(age);
                    regimen.setPatientId(patientId);
                    
                    allPatients.add(regimen);
                }
                return allPatients;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
                Database.handleException(ex);
                return null;
        }
        finally {
                Database.cleanUp(rs, stmt, con);
        }
    }
	
	public List<Regimen> getTotalPtsScreened(String startDate, String endDate, int type){//type 1= 0-19, 2 above 19
	    System.out.println(startDate);
        System.out.println(endDate);	
         
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        List<Regimen> allPatients = new ArrayList<>();
        try {
                con = Database.connectionPool.getConnection();
                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                //stmt = Database.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);

                StringBuilder queryString = new StringBuilder("select patient.patient_id, person.birthdate,   TIMESTAMPDIFF(YEAR,person.birthdate,CURDATE()) AS age,  person_name.given_name, person_name.family_name, patient_identifier.identifier FROM patient " +
                                        "   JOIN person ON person.person_id=patient.patient_id " +
                                        "   JOIN person_name ON person_name.person_id=patient.patient_id " +
                                        "   LEFT JOIN patient_identifier ON patient_identifier.patient_id=patient.patient_id AND patient_identifier.identifier_type=4 " +
                                        "   WHERE  ");

                int i = 1;
                stmt = con.prepareStatement(queryString.toString());
                //stmt.setString(i++, startDate);
                stmt.setString(i++, endDate);
                stmt.setString(i++, endDate);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    
                    String patientId = rs.getString("patient_id");
                    String age = rs.getString("age");
                    String patientIdentifier = rs.getString("identifier");
                    String firstName = rs.getString("given_name");
                    String lastName = rs.getString("family_name");
                    Regimen regimen = new Regimen();
                    regimen.setFirstName(firstName);
                    regimen.setLastName(lastName);
                    regimen.setPepfarId(patientIdentifier);
                    regimen.setPatientAge(age);
                    regimen.setPatientId(patientId);
                    
                    allPatients.add(regimen);
                }
                return allPatients;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
                Database.handleException(ex);
                return null;
        }
        finally {
                Database.cleanUp(rs, stmt, con);
        }
    }
}
