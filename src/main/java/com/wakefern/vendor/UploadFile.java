package com.wakefern.vendor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgApiWarnTime;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;

import jersey.repackaged.com.google.common.collect.Table.Cell;


@Path(MWGApplicationConstants.Requests.Cart.prefix)
public class UploadFile extends BaseService {

	private final static Logger logger = Logger.getLogger(UploadFile.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public UploadFile() {
        this.requestPath = MWGApplicationConstants.Requests.Cart.prefix + MWGApplicationConstants.Requests.Vendor.review;
    }
    
	@POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Vendor.review)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String mwgStoreID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken    		
	) {
		long startTime, endTime, actualTime;
		
		try {
			this.requestHeader = new MWGHeader(accept, contentType, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, mwgStoreID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
			
        	String trackData = LogUtil.getRequestData("mwgStoreID", mwgStoreID, "userID", userID, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType);
        	
        	startTime = System.currentTimeMillis();
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.vendor.GetVendorPromotions");
            
            // for warning any API call time exceeding its own upper limited time defined in mwgApiWarnTime.java
			endTime = System.currentTimeMillis();
			actualTime = endTime - startTime;
			if (actualTime > MwgApiWarnTime.CART_GET_CONTENTS.getWarnTime()) {
				logger.warn("The API call took " + actualTime + " ms to process the request, the warn time is " +
						MwgApiWarnTime.CART_GET_CONTENTS.getWarnTime() + " ms. The track data: " + trackData);
			}

			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
       
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {

        	LogUtil.addErrorMaps(e, MwgErrorType.VENDOR_PROMOTIONS);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "mwgStoreID", mwgStoreID, 
        			"userId", userID, "sessionToken", sessionToken, "accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }
	

	private void readCSV(String fileName, InputStream is){
        try {
//            FileInputStream excelFile = new FileInputStream(new File(fileName));
//            Workbook workbook = new XSSFWorkbook(excelFile);

            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            
            /**
             * CONVERTING TO CSV
             */
//            csvConverter(datatypeSheet);
            
//            for(int i=datatypeSheet.getFirstRowNum()+1; i<=datatypeSheet.getLastRowNum(); i++){
            
            
//            for(int i=datatypeSheet.getFirstRowNum()+1; i<=3; i++){
//            	Row ro = datatypeSheet.getRow(i);
//            	for(int j=ro.getFirstCellNum(); j<= ro.getLastCellNum(); j++){
//            		Cell ce = ro.getCell(j);
//            		if(j == 0){
//            			System.out.println(ce.getStringCellValue());
//            		}
//            	}
//            }
            
            
            
//            Iterator<Row> iterator = datatypeSheet.iterator();
//
//            while (iterator.hasNext()) {
//
//                Row currentRow = iterator.next();
//                Iterator<Cell> cellIterator = currentRow.iterator();
//
//                while (cellIterator.hasNext()) {
//
//                    Cell currentCell = cellIterator.next();
//                    //getCellTypeEnum shown as deprecated for version 3.15
//                    //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
//                    if (currentCell.getCellType() == CellType.STRING) {
//                        System.out.print(currentCell.getStringCellValue() + "--");
//                    } else if (currentCell.getCellType() == CellType.NUMERIC) {
//                        System.out.print(currentCell.getNumericCellValue() + "--");
//                    }
//                }
//                System.out.println();
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}

