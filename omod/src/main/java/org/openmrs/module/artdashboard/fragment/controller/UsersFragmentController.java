/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.artdashboard.fragment.controller;

import com.google.gson.Gson;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.openmrs.api.UserService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
// import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.openmrs.module.artdashboard.Misc;
import org.openmrs.module.artdashboard.api.dao.ARTDashboardDao;
import org.openmrs.module.artdashboard.api.dao.Database;
import org.openmrs.module.artdashboard.pojo.Lab;
import org.openmrs.module.artdashboard.pojo.Regimen;

/**
 *  * Controller for a fragment that shows all users  
 */
public class UsersFragmentController {
	
	ARTDashboardDao artDashboardDao = new ARTDashboardDao();
	
	public void controller(FragmentModel model, @SpringBean("userService") UserService service) {
		model.addAttribute("users", service.getAllUsers());
	}
	
	//total of pts confirmed tb
	public String getRegimenGraphData(HttpServletRequest request) {
		
		//Database.initConnection();
		//int adultsTestedPositive = htsDao.getAllAdultPatientsTestedPositieForHIV(startDate, endDate);
		//ARTDashboardDao artDashboardDao = new ARTDashboardDao();
		DateTime startDateTime = new DateTime("1990-01-01");
		DateTime endDateTime = new DateTime(new Date());
		
		if (request.getParameter("startDate") != null && !request.getParameter("startDate").equalsIgnoreCase("")) {
			startDateTime = new DateTime(request.getParameter("startDate"));
			endDateTime = new DateTime(request.getParameter("endDate"));
		}
		String startDate = startDateTime.toString("yyyy'-'MM'-'dd");
		String endDate = endDateTime.toString("yyyy'-'MM'-'dd");
		
		List<Regimen> regimenData = artDashboardDao.getRegimenData2(startDate, endDate);
		
		String json = new Gson().toJson(regimenData);
		//return "hello";
		
		return json;
		
	}
	
	//total of pts confirmed tb
	public String getLabGraphData(HttpServletRequest request) {
		
		//Database.initConnection();
		//int adultsTestedPositive = htsDao.getAllAdultPatientsTestedPositieForHIV(startDate, endDate);
		//ARTDashboardDao artDashboardDao = new ARTDashboardDao();
		DateTime startDateTime = new DateTime("1990-01-01");
		DateTime endDateTime = new DateTime(new Date());
		
		if (request.getParameter("startDate") != null && !request.getParameter("startDate").equalsIgnoreCase("")) {
			startDateTime = new DateTime(request.getParameter("startDate"));
			endDateTime = new DateTime(request.getParameter("endDate"));
		}
		String startDate = startDateTime.toString("yyyy'-'MM'-'dd");
		String endDate = endDateTime.toString("yyyy'-'MM'-'dd");
		
		List<Lab> labData = artDashboardDao.getVlData(startDate, endDate);
		
		String json = new Gson().toJson(labData);
		//return "hello";
		
		return json;
		
	}
	
	//total of pts confirmed tb
	public String getMmdGraphData(HttpServletRequest request) {
		
		//Database.initConnection();
		//int adultsTestedPositive = htsDao.getAllAdultPatientsTestedPositieForHIV(startDate, endDate);
		//ARTDashboardDao artDashboardDao = new ARTDashboardDao();
		DateTime startDateTime = new DateTime("1990-01-01");
		DateTime endDateTime = new DateTime(new Date());
		
		if (request.getParameter("startDate") != null && !request.getParameter("startDate").equalsIgnoreCase("")) {
			startDateTime = new DateTime(request.getParameter("startDate"));
			endDateTime = new DateTime(request.getParameter("endDate"));
		}
		DateTime sixMonthsAgoDateTime = endDateTime.minusMinutes(6);
		String startDate = startDateTime.toString("yyyy'-'MM'-'dd");
		String endDate = endDateTime.toString("yyyy'-'MM'-'dd");
		String sixMonthsAgo = sixMonthsAgoDateTime.toString("yyyy'-'MM'-'dd");
		
		List<Lab> mmdData = artDashboardDao.getMMDData(startDate, endDate, sixMonthsAgo);
		
		String json = new Gson().toJson(mmdData);
		//return "hello";
		
		return json;
		
	}
	
	//total of pts confirmed tb
	public String getEACGraphData(HttpServletRequest request) {
		
		//Database.initConnection();
		//int adultsTestedPositive = htsDao.getAllAdultPatientsTestedPositieForHIV(startDate, endDate);
		//ARTDashboardDao artDashboardDao = new ARTDashboardDao();
		DateTime startDateTime = new DateTime("1990-01-01");
		DateTime endDateTime = new DateTime(new Date());
		
		if (request.getParameter("startDate") != null && !request.getParameter("startDate").equalsIgnoreCase("")) {
			startDateTime = new DateTime(request.getParameter("startDate"));
			endDateTime = new DateTime(request.getParameter("endDate"));
		}
		DateTime sixMonthsAgoDateTime = endDateTime.minusMinutes(6);
		String startDate = startDateTime.toString("yyyy'-'MM'-'dd");
		String endDate = endDateTime.toString("yyyy'-'MM'-'dd");
		String sixMonthsAgo = sixMonthsAgoDateTime.toString("yyyy'-'MM'-'dd");
		
		List<Lab> eacData = artDashboardDao.getEACData(startDate, endDate);
		
		String json = new Gson().toJson(eacData);
		//return "hello";
		
		return json;
		
	}
	
	//total of pts confirmed tb
	public String getCD4GraphData(HttpServletRequest request) {
		
		//Database.initConnection();
		//int adultsTestedPositive = htsDao.getAllAdultPatientsTestedPositieForHIV(startDate, endDate);
		//ARTDashboardDao artDashboardDao = new ARTDashboardDao();
		DateTime startDateTime = new DateTime("1990-01-01");
		DateTime endDateTime = new DateTime(new Date());
		
		if (request.getParameter("startDate") != null && !request.getParameter("startDate").equalsIgnoreCase("")) {
			startDateTime = new DateTime(request.getParameter("startDate"));
			endDateTime = new DateTime(request.getParameter("endDate"));
		}
		DateTime sixMonthsAgoDateTime = endDateTime.minusMinutes(6);
		String startDate = startDateTime.toString("yyyy'-'MM'-'dd");
		String endDate = endDateTime.toString("yyyy'-'MM'-'dd");
		String sixMonthsAgo = sixMonthsAgoDateTime.toString("yyyy'-'MM'-'dd");
		
		List<Lab> cd4Data = artDashboardDao.getCD4Data(startDate, endDate);
		
		String json = new Gson().toJson(cd4Data);
		//return "hello";
		
		return json;
		
	}
	
	//total of pts confirmed tb
	public String getActivePts(HttpServletRequest request) {
		
		//Database.initConnection();
		//int adultsTestedPositive = htsDao.getAllAdultPatientsTestedPositieForHIV(startDate, endDate);
		//ARTDashboardDao artDashboardDao = new ARTDashboardDao();
		DateTime startDateTime = new DateTime("1990-01-01");
		DateTime endDateTime = new DateTime(new Date());
		
		if (request.getParameter("startDate") != null && !request.getParameter("startDate").equalsIgnoreCase("")) {
			startDateTime = new DateTime(request.getParameter("startDate"));
			endDateTime = new DateTime(request.getParameter("endDate"));
		}
		DateTime sixMonthsAgoDateTime = endDateTime.minusMinutes(6);
		String startDate = startDateTime.toString("yyyy'-'MM'-'dd");
		String endDate = endDateTime.toString("yyyy'-'MM'-'dd");
		String sixMonthsAgo = sixMonthsAgoDateTime.toString("yyyy'-'MM'-'dd");
		
		List<Regimen> txCurrData = artDashboardDao.getActivePeds(startDate, endDate);
		
		String json = new Gson().toJson(txCurrData);
		//return "hello";
		
		return json;
		
	}
	
	public String getTxNew(HttpServletRequest request) {
		
		//Database.initConnection();
		//int adultsTestedPositive = htsDao.getAllAdultPatientsTestedPositieForHIV(startDate, endDate);
		//ARTDashboardDao artDashboardDao = new ARTDashboardDao();
		
		DateTime endDateTime = new DateTime(new Date());
		
		int quarter = Misc.getFiscalQuarter(endDateTime.getMonthOfYear() + 1);
		String quarterStartDate = Misc.getQuarterStartDate(quarter, endDateTime.getYear());
		DateTime startDateTime = new DateTime(quarterStartDate);
		
		if (request.getParameter("startDate") != null && !request.getParameter("startDate").equalsIgnoreCase("")) {
			startDateTime = new DateTime(request.getParameter("startDate"));
			endDateTime = new DateTime(request.getParameter("endDate"));
		}
		DateTime sixMonthsAgoDateTime = endDateTime.minusMinutes(6);
		String startDate = startDateTime.toString("yyyy'-'MM'-'dd");
		String endDate = endDateTime.toString("yyyy'-'MM'-'dd");
		String sixMonthsAgo = sixMonthsAgoDateTime.toString("yyyy'-'MM'-'dd");
		
		List<Regimen> txNewData = artDashboardDao.getPedsTxNew(startDate, endDate);
		
		String json = new Gson().toJson(txNewData);
		//return "hello";
		
		return json;
		
	}
	
	public String getTxMl(HttpServletRequest request) {
		
		//Database.initConnection();
		//int adultsTestedPositive = htsDao.getAllAdultPatientsTestedPositieForHIV(startDate, endDate);
		//ARTDashboardDao artDashboardDao = new ARTDashboardDao();
		DateTime startDateTime = new DateTime("1990-01-01");
		DateTime endDateTime = new DateTime(new Date());
		
		if (request.getParameter("startDate") != null && !request.getParameter("startDate").equalsIgnoreCase("")) {
			startDateTime = new DateTime(request.getParameter("startDate"));
			endDateTime = new DateTime(request.getParameter("endDate"));
		}
		
		int currQuarter = Misc.getFiscalQuarter(endDateTime.getMonthOfYear() + 1);
		int previousQuarter = Misc.getPreviousQuarter(currQuarter);
		int previousQuarterYear = Misc.getPreviousQuarterYear(currQuarter, endDateTime.getYear());
		String quarterEndDate = Misc.getQuarterEndDate(previousQuarter, previousQuarterYear);
		String cQendDate = Misc.getQuarterEndDate(currQuarter, endDateTime.getYear());
		
		System.out.println("previous quarter" + quarterEndDate);
		
		DateTime sixMonthsAgoDateTime = endDateTime.minusMinutes(6);
		String startDate = startDateTime.toString("yyyy'-'MM'-'dd");
		String endDate = endDateTime.toString("yyyy'-'MM'-'dd");
		String sixMonthsAgo = sixMonthsAgoDateTime.toString("yyyy'-'MM'-'dd");
		
		//get patients active in the previous quarter
		// List<Regimen> previousTxCurrData = artDashboardDao.getActivePeds(startDate, quarterEndDate);
		List<Regimen> txMlData = artDashboardDao.getTXML(cQendDate, endDate);
		// List<Regimen> txMlData = artDashboardDao.getPedsLTFU(startDate, endDate);
		
		// String prevTxCurrJSONno = String.valueOf(previousTxCurrData.size());
		String txMlJSONno = String.valueOf(txMlData.size());
		
		// String prevTxCurrJSON = new Gson().toJson(previousTxCurrData);
		String txMlJSON = new Gson().toJson(txMlData);
		Map<String, String> jsonMap = new HashMap<String, String>();
		
		// jsonMap.put("previousTxCurr", prevTxCurrJSON);
		jsonMap.put("txMl", txMlJSON);
		
		// jsonMap.put("previousTxCurrno", prevTxCurrJSONno);
		jsonMap.put("txMlno", txMlJSONno);
		String json = new Gson().toJson(jsonMap);
		
		//return "hello";
		
		return json;
		
	}
	
	public String getTxPVLS(HttpServletRequest request) {
		
		//Database.initConnection();
		//int adultsTestedPositive = htsDao.getAllAdultPatientsTestedPositieForHIV(startDate, endDate);
		//ARTDashboardDao artDashboardDao = new ARTDashboardDao();
		DateTime startDateTime = new DateTime("1990-01-01");
		DateTime endDateTime = new DateTime(new Date());
		
		if (request.getParameter("startDate") != null && !request.getParameter("startDate").equalsIgnoreCase("")) {
			startDateTime = new DateTime(request.getParameter("startDate"));
			endDateTime = new DateTime(request.getParameter("endDate"));
		}
		
		int currQuarter = Misc.getFiscalQuarter(endDateTime.getMonthOfYear() + 1);
		int previousQuarter = Misc.getPreviousQuarter(currQuarter);
		int previousQuarterYear = Misc.getPreviousQuarterYear(currQuarter, endDateTime.getYear());
		String quarterEndDate = Misc.getQuarterEndDate(previousQuarter, previousQuarterYear);
		
		DateTime sixMonthsAgoDateTime = endDateTime.minusMinutes(6);
		String startDate = startDateTime.toString("yyyy'-'MM'-'dd");
		String endDate = endDateTime.toString("yyyy'-'MM'-'dd");
		String sixMonthsAgo = sixMonthsAgoDateTime.toString("yyyy'-'MM'-'dd");
		
		//get patients active in the previous quarter
		List<Regimen> txPVLSData = artDashboardDao.getPedsTxPVLS(startDate, quarterEndDate);
		
		String txPVLSDataJSON = new Gson().toJson(txPVLSData);
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("txPVLSDataJSON", txPVLSDataJSON);
		String json = new Gson().toJson(jsonMap);
		
		//return "hello";
		
		return json;
		
	}
	
	public String getTxRTT(HttpServletRequest request) {
		
		//Database.initConnection();
		//int adultsTestedPositive = htsDao.getAllAdultPatientsTestedPositieForHIV(startDate, endDate);
		//ARTDashboardDao artDashboardDao = new ARTDashboardDao();
		DateTime startDateTime = new DateTime("1990-01-01");
		DateTime endDateTime = new DateTime(new Date());
		
		if (request.getParameter("startDate") != null && !request.getParameter("startDate").equalsIgnoreCase("")) {
			startDateTime = new DateTime(request.getParameter("startDate"));
			endDateTime = new DateTime(request.getParameter("endDate"));
		}
		
		int currQuarter = Misc.getFiscalQuarter(endDateTime.getMonthOfYear() + 1);
		int previousQuarter = Misc.getPreviousQuarter(currQuarter);
		int previousQuarterYear = Misc.getPreviousQuarterYear(currQuarter, endDateTime.getMonthOfYear());
		String quarterEndDate = Misc.getQuarterEndDate(previousQuarter, previousQuarterYear);
		
		DateTime sixMonthsAgoDateTime = endDateTime.minusMinutes(6);
		String startDate = startDateTime.toString("yyyy'-'MM'-'dd");
		String endDate = endDateTime.toString("yyyy'-'MM'-'dd");
		String sixMonthsAgo = sixMonthsAgoDateTime.toString("yyyy'-'MM'-'dd");
		
		//get patients active in the previous quarter
		List<Regimen> previousLTFUData = artDashboardDao.getPedsLTFU(startDate, quarterEndDate);
		
		List<Regimen> txRTT = artDashboardDao.getTxRTT(startDate, endDate);
		
		String prevLTFUJSON = new Gson().toJson(previousLTFUData);
		String txRTTJSON = new Gson().toJson(txRTT);
		
		String prevLTFUno = String.valueOf(previousLTFUData.size());
		String txRTTno = String.valueOf(txRTT.size());
		
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("previousLTFU", prevLTFUJSON);
		jsonMap.put("txRTT", txRTTJSON);
		
		jsonMap.put("previousLTFUno", prevLTFUno);
		jsonMap.put("txRTTno", txRTTno);
		String json = new Gson().toJson(jsonMap);
		
		//return "hello";
		
		return json;
		
	}
}
