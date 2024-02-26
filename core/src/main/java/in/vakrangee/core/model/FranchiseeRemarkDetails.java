package in.vakrangee.core.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FranchiseeRemarkDetails implements Serializable {

    // LOCATION
    @SerializedName("LOCATION")
    private String LOCATION;

    // PHOTO
    private String FRONTAGE_IMAGE;
    private String LEFT_WALL;
    private String FRONT_WALL;
    private String RIGHT_WALL;
    private String BACK_WALL;
    private String CEILING;
    private String FLOOR;
    private String FRONTAGE_OPPOSITE_SIDE_ROAD;
    private String FRONTAGE_LEFT_SIDE_ROAD;
    private String FRONTAGE_RIGHT_SIDE_ROAD;

    // NEXTGEN SITE VISIT
    // PREMISE DETAIL
    private String WIDTH;
    private String DEPTH;
    private String HEIGHT;
    private String FRONTAGE;
    private String AREA;
    @SerializedName(value = "Main_Signboard")
    private String MAIN_SIGNBOARD;
    @SerializedName(value = "Main_Entrance")
    private String MAIN_ENTRANCE;


    // PREMISE LOCATED AT
    private String PREMISE_LOCATED;
    private String PREMISE_FLOOR;
    private String PREMISE_STRUCTURE;
    private String PREMISE_ROOF;
    private String FRONTAGE_OBSTRUCTED;
    private String FOOTPATH;
    private String BATHROOM;
    private String PANTRY;

    // ADDRESS OF LOCATION VISITED
    private String ADDRESS;
    private String WARD_NO;
    private String STATE;
    private String DISTRICT;
    private String VTC;
    private String PIN;

    // PREMISE LEVEL
    private String PREMISE_LEVEL;

    // PREMISE SHAPE
    private String PREMISE_SHAPE;

    // CLOSEST BANK BRANCH
    private String BANK_BRANCH_1;
    private String BANK_BRANCH_2;
    private String BANK_BRANCH_3;

    // CLOSEST ATM
    private String ATM_1;
    private String ATM_2;
    private String ATM_3;

    // NextGen Site Work Commencement
    private String Work_Commencement_Date;
    private String Work_Completion_Expected_Date;

    // NextGen Site Visit New Fields
    private String Frontage_Photo_5ft;
    private String Frontage_Photo_10ft;
    private String Pillers;
    private String Windows;
    private String Beam;
    private String Adjacent_Shops;
    private String Multiple_Entries;
    private String Interior_Work_Status;
    private String Start_Date;
    private String Expected_Completion_Date;

    @SerializedName(value = "Logistic_payment_date", alternate = "Logistic_Payment_Date")
    private String Logistic_payment_date;
    private String Site_Layout_Sketch;
    private String GSTIN_Registered;
    private String GSTIN_Number;
    private String GSTIN_Address;
    private String GSTIN_Image;

    private String WELCOME_MAIL;
    private String CALL_RECEIVED;
    private String CONSENT_FOR_VB;
    private String LOGISTIC_PAYMENT;

    //provisional main signboard
    private String PMSignboardLength;
    private String PMSignboardWidth;

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public String getFRONTAGE_IMAGE() {
        return FRONTAGE_IMAGE;
    }

    public void setFRONTAGE_IMAGE(String FRONTAGE_IMAGE) {
        this.FRONTAGE_IMAGE = FRONTAGE_IMAGE;
    }

    public String getLEFT_WALL() {
        return LEFT_WALL;
    }

    public void setLEFT_WALL(String LEFT_WALL) {
        this.LEFT_WALL = LEFT_WALL;
    }

    public String getFRONT_WALL() {
        return FRONT_WALL;
    }

    public void setFRONT_WALL(String FRONT_WALL) {
        this.FRONT_WALL = FRONT_WALL;
    }

    public String getRIGHT_WALL() {
        return RIGHT_WALL;
    }

    public void setRIGHT_WALL(String RIGHT_WALL) {
        this.RIGHT_WALL = RIGHT_WALL;
    }

    public String getBACK_WALL() {
        return BACK_WALL;
    }

    public void setBACK_WALL(String BACK_WALL) {
        this.BACK_WALL = BACK_WALL;
    }

    public String getCEILING() {
        return CEILING;
    }

    public void setCEILING(String CEILING) {
        this.CEILING = CEILING;
    }

    public String getFLOOR() {
        return FLOOR;
    }

    public void setFLOOR(String FLOOR) {
        this.FLOOR = FLOOR;
    }

    public String getFRONTAGE_OPPOSITE_SIDE_ROAD() {
        return FRONTAGE_OPPOSITE_SIDE_ROAD;
    }

    public void setFRONTAGE_OPPOSITE_SIDE_ROAD(String FRONTAGE_OPPOSITE_SIDE_ROAD) {
        this.FRONTAGE_OPPOSITE_SIDE_ROAD = FRONTAGE_OPPOSITE_SIDE_ROAD;
    }

    public String getFRONTAGE_LEFT_SIDE_ROAD() {
        return FRONTAGE_LEFT_SIDE_ROAD;
    }

    public void setFRONTAGE_LEFT_SIDE_ROAD(String FRONTAGE_LEFT_SIDE_ROAD) {
        this.FRONTAGE_LEFT_SIDE_ROAD = FRONTAGE_LEFT_SIDE_ROAD;
    }

    public String getFRONTAGE_RIGHT_SIDE_ROAD() {
        return FRONTAGE_RIGHT_SIDE_ROAD;
    }

    public void setFRONTAGE_RIGHT_SIDE_ROAD(String FRONTAGE_RIGHT_SIDE_ROAD) {
        this.FRONTAGE_RIGHT_SIDE_ROAD = FRONTAGE_RIGHT_SIDE_ROAD;
    }

    public String getPREMISE_LOCATED() {
        return PREMISE_LOCATED;
    }

    public void setPREMISE_LOCATED(String PREMISE_LOCATED) {
        this.PREMISE_LOCATED = PREMISE_LOCATED;
    }

    public String getPREMISE_FLOOR() {
        return PREMISE_FLOOR;
    }

    public void setPREMISE_FLOOR(String PREMISE_FLOOR) {
        this.PREMISE_FLOOR = PREMISE_FLOOR;
    }

    public String getPREMISE_STRUCTURE() {
        return PREMISE_STRUCTURE;
    }

    public void setPREMISE_STRUCTURE(String PREMISE_STRUCTURE) {
        this.PREMISE_STRUCTURE = PREMISE_STRUCTURE;
    }

    public String getPREMISE_ROOF() {
        return PREMISE_ROOF;
    }

    public void setPREMISE_ROOF(String PREMISE_ROOF) {
        this.PREMISE_ROOF = PREMISE_ROOF;
    }

    public String getFRONTAGE_OBSTRUCTED() {
        return FRONTAGE_OBSTRUCTED;
    }

    public void setFRONTAGE_OBSTRUCTED(String FRONTAGE_OBSTRUCTED) {
        this.FRONTAGE_OBSTRUCTED = FRONTAGE_OBSTRUCTED;
    }

    public String getFOOTPATH() {
        return FOOTPATH;
    }

    public void setFOOTPATH(String FOOTPATH) {
        this.FOOTPATH = FOOTPATH;
    }

    public String getBATHROOM() {
        return BATHROOM;
    }

    public void setBATHROOM(String BATHROOM) {
        this.BATHROOM = BATHROOM;
    }

    public String getPANTRY() {
        return PANTRY;
    }

    public void setPANTRY(String PANTRY) {
        this.PANTRY = PANTRY;
    }

    public String getWIDTH() {
        return WIDTH;
    }

    public void setWIDTH(String WIDTH) {
        this.WIDTH = WIDTH;
    }

    public String getDEPTH() {
        return DEPTH;
    }

    public void setDEPTH(String DEPTH) {
        this.DEPTH = DEPTH;
    }

    public String getHEIGHT() {
        return HEIGHT;
    }

    public void setHEIGHT(String HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public String getFRONTAGE() {
        return FRONTAGE;
    }

    public void setFRONTAGE(String FRONTAGE) {
        this.FRONTAGE = FRONTAGE;
    }

    public String getAREA() {
        return AREA;
    }

    public void setAREA(String AREA) {
        this.AREA = AREA;
    }

    public String getPREMISE_LEVEL() {
        return PREMISE_LEVEL;
    }

    public void setPREMISE_LEVEL(String PREMISE_LEVEL) {
        this.PREMISE_LEVEL = PREMISE_LEVEL;
    }

    public String getPREMISE_SHAPE() {
        return PREMISE_SHAPE;
    }

    public void setPREMISE_SHAPE(String PREMISE_SHAPE) {
        this.PREMISE_SHAPE = PREMISE_SHAPE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getWARD_NO() {
        return WARD_NO;
    }

    public void setWARD_NO(String WARD_NO) {
        this.WARD_NO = WARD_NO;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getDISTRICT() {
        return DISTRICT;
    }

    public void setDISTRICT(String DISTRICT) {
        this.DISTRICT = DISTRICT;
    }

    public String getVTC() {
        return VTC;
    }

    public void setVTC(String VTC) {
        this.VTC = VTC;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getBANK_BRANCH_1() {
        return BANK_BRANCH_1;
    }

    public void setBANK_BRANCH_1(String BANK_BRANCH_1) {
        this.BANK_BRANCH_1 = BANK_BRANCH_1;
    }

    public String getBANK_BRANCH_2() {
        return BANK_BRANCH_2;
    }

    public void setBANK_BRANCH_2(String BANK_BRANCH_2) {
        this.BANK_BRANCH_2 = BANK_BRANCH_2;
    }

    public String getBANK_BRANCH_3() {
        return BANK_BRANCH_3;
    }

    public void setBANK_BRANCH_3(String BANK_BRANCH_3) {
        this.BANK_BRANCH_3 = BANK_BRANCH_3;
    }

    public String getATM_1() {
        return ATM_1;
    }

    public void setATM_1(String ATM_1) {
        this.ATM_1 = ATM_1;
    }

    public String getATM_2() {
        return ATM_2;
    }

    public void setATM_2(String ATM_2) {
        this.ATM_2 = ATM_2;
    }

    public String getATM_3() {
        return ATM_3;
    }

    public void setATM_3(String ATM_3) {
        this.ATM_3 = ATM_3;
    }

    //region NextGen Work Commencement
    public String getWork_Commencement_Date() {
        return Work_Commencement_Date;
    }

    public void setWork_Commencement_Date(String work_Commencement_Date) {
        Work_Commencement_Date = work_Commencement_Date;
    }

    public String getWork_Completion_Expected_Date() {
        return Work_Completion_Expected_Date;
    }

    public void setWork_Completion_Expected_Date(String work_Completion_Expected_Date) {
        Work_Completion_Expected_Date = work_Completion_Expected_Date;
    }
    //endregion

    //region NextGen Site Visit New Fields

    public String getFrontage_Photo_5ft() {
        return Frontage_Photo_5ft;
    }

    public void setFrontage_Photo_5ft(String frontage_Photo_5ft) {
        Frontage_Photo_5ft = frontage_Photo_5ft;
    }

    public String getFrontage_Photo_10ft() {
        return Frontage_Photo_10ft;
    }

    public void setFrontage_Photo_10ft(String frontage_Photo_10ft) {
        Frontage_Photo_10ft = frontage_Photo_10ft;
    }

    public String getPillers() {
        return Pillers;
    }

    public void setPillers(String pillers) {
        Pillers = pillers;
    }

    public String getWindows() {
        return Windows;
    }

    public void setWindows(String windows) {
        Windows = windows;
    }

    public String getBeam() {
        return Beam;
    }

    public void setBeam(String beam) {
        Beam = beam;
    }

    public String getAdjacent_Shops() {
        return Adjacent_Shops;
    }

    public void setAdjacent_Shops(String adjacent_Shops) {
        Adjacent_Shops = adjacent_Shops;
    }

    public String getMultiple_Entries() {
        return Multiple_Entries;
    }

    public void setMultiple_Entries(String multiple_Entries) {
        Multiple_Entries = multiple_Entries;
    }

    public String getInterior_Work_Status() {
        return Interior_Work_Status;
    }

    public void setInterior_Work_Status(String interior_Work_Status) {
        Interior_Work_Status = interior_Work_Status;
    }

    public String getStart_Date() {
        return Start_Date;
    }

    public void setStart_Date(String start_Date) {
        Start_Date = start_Date;
    }

    public String getExpected_Completion_Date() {
        return Expected_Completion_Date;
    }

    public void setExpected_Completion_Date(String expected_Completion_Date) {
        Expected_Completion_Date = expected_Completion_Date;
    }

    public String getLogistic_payment_date() {
        return Logistic_payment_date;
    }

    public void setLogistic_payment_date(String logistic_payment_date) {
        Logistic_payment_date = logistic_payment_date;
    }

    public String getSite_Layout_Sketch() {
        return Site_Layout_Sketch;
    }

    public void setSite_Layout_Sketch(String site_Layout_Sketch) {
        Site_Layout_Sketch = site_Layout_Sketch;
    }

    public String getGSTIN_Registered() {
        return GSTIN_Registered;
    }

    public void setGSTIN_Registered(String GSTIN_Registered) {
        this.GSTIN_Registered = GSTIN_Registered;
    }

    public String getGSTIN_Number() {
        return GSTIN_Number;
    }

    public void setGSTIN_Number(String GSTIN_Number) {
        this.GSTIN_Number = GSTIN_Number;
    }

    public String getGSTIN_Address() {
        return GSTIN_Address;
    }

    public void setGSTIN_Address(String GSTIN_Address) {
        this.GSTIN_Address = GSTIN_Address;
    }

    public String getGSTIN_Image() {
        return GSTIN_Image;
    }

    public void setGSTIN_Image(String GSTIN_Image) {
        this.GSTIN_Image = GSTIN_Image;
    }

    public String getMAIN_SIGNBOARD() {
        return MAIN_SIGNBOARD;
    }

    public void setMAIN_SIGNBOARD(String MAIN_SIGNBOARD) {
        this.MAIN_SIGNBOARD = MAIN_SIGNBOARD;
    }

    public String getMAIN_ENTRANCE() {
        return MAIN_ENTRANCE;
    }

    public void setMAIN_ENTRANCE(String MAIN_ENTRANCE) {
        this.MAIN_ENTRANCE = MAIN_ENTRANCE;
    }

    public String getWELCOME_MAIL() {
        return WELCOME_MAIL;
    }

    public void setWELCOME_MAIL(String WELCOME_MAIL) {
        this.WELCOME_MAIL = WELCOME_MAIL;
    }

    public String getCALL_RECEIVED() {
        return CALL_RECEIVED;
    }

    public void setCALL_RECEIVED(String CALL_RECEIVED) {
        this.CALL_RECEIVED = CALL_RECEIVED;
    }

    public String getCONSENT_FOR_VB() {
        return CONSENT_FOR_VB;
    }

    public void setCONSENT_FOR_VB(String CONSENT_FOR_VB) {
        this.CONSENT_FOR_VB = CONSENT_FOR_VB;
    }

    public String getLOGISTIC_PAYMENT() {
        return LOGISTIC_PAYMENT;
    }

    public void setLOGISTIC_PAYMENT(String LOGISTIC_PAYMENT) {
        this.LOGISTIC_PAYMENT = LOGISTIC_PAYMENT;
    }

    public String getPMSignboardLength() {
        return PMSignboardLength;
    }

    public void setPMSignboardLength(String PMSignboardLength) {
        this.PMSignboardLength = PMSignboardLength;
    }

    public String getPMSignboardWidth() {
        return PMSignboardWidth;
    }

    public void setPMSignboardWidth(String PMSignboardWidth) {
        this.PMSignboardWidth = PMSignboardWidth;
    }
    //endregion


    //region Get Combine Message For Site Visit
    public String getPremiseDetailRemarks() {
        StringBuilder strBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(WIDTH))
            strBuilder.append("<b>WIDTH:</b> &nbsp;&nbsp;" + WIDTH + "<br>");

        if (!TextUtils.isEmpty(DEPTH))
            strBuilder.append("<b>DEPTH:</b> &nbsp;&nbsp;" + DEPTH + "<br>");

        if (!TextUtils.isEmpty(HEIGHT))
            strBuilder.append("<b>HEIGHT:</b> &nbsp;&nbsp;" + HEIGHT + "<br>");

        if (!TextUtils.isEmpty(MAIN_ENTRANCE))
            strBuilder.append("<b>MAIN_ENTRANCE:</b> &nbsp;&nbsp;" + MAIN_ENTRANCE + "<br>");

        if (!TextUtils.isEmpty(FRONTAGE))
            strBuilder.append("<b>FRONTAGE:</b> &nbsp;&nbsp;" + FRONTAGE + "<br>");

        if (!TextUtils.isEmpty(MAIN_SIGNBOARD))
            strBuilder.append("<b>MAIN_SIGNBOARD:</b> &nbsp;&nbsp;" + MAIN_SIGNBOARD + "<br>");

        if (!TextUtils.isEmpty(AREA))
            strBuilder.append("<b>AREA:</b> &nbsp;&nbsp;" + AREA + "<br>");

        return strBuilder.toString();
    }

    public String getPremiseLocatedAtRemarks() {
        StringBuilder strBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(PREMISE_LOCATED))
            strBuilder.append("<b>PREMISE_LOCATED:</b> &nbsp;&nbsp;" + PREMISE_LOCATED + "<br>");
        if (!TextUtils.isEmpty(PREMISE_FLOOR))
            strBuilder.append("<b>PREMISE_FLOOR:</b> &nbsp;&nbsp;" + PREMISE_FLOOR + "<br>");
        if (!TextUtils.isEmpty(PREMISE_STRUCTURE))
            strBuilder.append("<b>PREMISE_STRUCTURE:</b> &nbsp;&nbsp;" + PREMISE_STRUCTURE + "<br>");
        if (!TextUtils.isEmpty(PREMISE_ROOF))
            strBuilder.append("<b>PREMISE_ROOF:</b> &nbsp;&nbsp;" + PREMISE_ROOF + "<br>");
        if (!TextUtils.isEmpty(FRONTAGE_OBSTRUCTED))
            strBuilder.append("<b>FRONTAGE_OBSTRUCTED:</b> &nbsp;&nbsp;" + FRONTAGE_OBSTRUCTED + "<br>");
        if (!TextUtils.isEmpty(FOOTPATH))
            strBuilder.append("<b>FOOTPATH:</b> &nbsp;&nbsp;" + FOOTPATH + "<br>");
        if (!TextUtils.isEmpty(BATHROOM))
            strBuilder.append("<b>BATHROOM:</b> &nbsp;&nbsp;" + BATHROOM + "<br>");
        if (!TextUtils.isEmpty(PANTRY))
            strBuilder.append("<b>PANTRY:</b> &nbsp;&nbsp;" + PANTRY + "<br>");
        if (!TextUtils.isEmpty(Pillers))
            strBuilder.append("<b>PILLERS:</b> &nbsp;&nbsp;" + Pillers + "<br>");
        if (!TextUtils.isEmpty(Windows))
            strBuilder.append("<b>WINDOWS:</b> &nbsp;&nbsp;" + Windows + "<br>");
        if (!TextUtils.isEmpty(Beam))
            strBuilder.append("<b>BEAM:</b> &nbsp;&nbsp;" + Beam + "<br>");
        if (!TextUtils.isEmpty(Adjacent_Shops))
            strBuilder.append("<b>ADJACENT_SHOPS:</b> &nbsp;&nbsp;" + Adjacent_Shops + "<br>");
        if (!TextUtils.isEmpty(Multiple_Entries))
            strBuilder.append("<b>MULTIPLE_ENTRIES:</b> &nbsp;&nbsp;" + Multiple_Entries + "<br>");

        return strBuilder.toString();
    }

    public String getAddressOfLocationVisitedRemarks() {
        StringBuilder strBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(ADDRESS))
            strBuilder.append("<b>ADDRESS:</b> &nbsp;&nbsp;" + ADDRESS + "<br>");
        if (!TextUtils.isEmpty(WARD_NO))
            strBuilder.append("<b>WARD_NO:</b> &nbsp;&nbsp;" + WARD_NO + "<br>");
        if (!TextUtils.isEmpty(STATE))
            strBuilder.append("<b>STATE:</b> &nbsp;&nbsp;" + STATE + "<br>");
        if (!TextUtils.isEmpty(DISTRICT))
            strBuilder.append("<b>DISTRICT:</b> &nbsp;&nbsp;" + DISTRICT + "<br>");
        if (!TextUtils.isEmpty(VTC))
            strBuilder.append("<b>VTC:</b> &nbsp;&nbsp;" + VTC + "<br>");
        if (!TextUtils.isEmpty(PIN))
            strBuilder.append("<b>PIN:</b> &nbsp;&nbsp;" + PIN + "<br>");

        return strBuilder.toString();
    }

    public String getPremiseLevelRemarks() {
        StringBuilder strBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(PREMISE_LEVEL))
            strBuilder.append("<b>PREMISE_LEVEL:</b> &nbsp;&nbsp;" + PREMISE_LEVEL + "<br>");

        return strBuilder.toString();
    }

    public String getPremiseShapeRemarks() {
        StringBuilder strBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(PREMISE_SHAPE))
            strBuilder.append("<b>PREMISE_SHAPE:</b> &nbsp;&nbsp;" + PREMISE_SHAPE + "<br>");
        if (!TextUtils.isEmpty(Site_Layout_Sketch))
            strBuilder.append("<b>SITE_LAYOUT_SKETCH:</b> &nbsp;&nbsp;" + Site_Layout_Sketch + "<br>");

        return strBuilder.toString();
    }

    public String getClosestBankBranchRemarks() {
        StringBuilder strBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(BANK_BRANCH_1))
            strBuilder.append("<b>BANK_BRANCH_1:</b> &nbsp;&nbsp;" + BANK_BRANCH_1 + "<br>");
        if (!TextUtils.isEmpty(BANK_BRANCH_2))
            strBuilder.append("<b>BANK_BRANCH_2:</b> &nbsp;&nbsp;" + BANK_BRANCH_2 + "<br>");
        if (!TextUtils.isEmpty(BANK_BRANCH_3))
            strBuilder.append("<b>BANK_BRANCH_3:</b> &nbsp;&nbsp;" + BANK_BRANCH_3 + "<br>");

        return strBuilder.toString();
    }

    public String getClosestBankATMRemarks() {
        StringBuilder strBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(ATM_1))
            strBuilder.append("<b>ATM_1:</b> &nbsp;&nbsp;" + ATM_1 + "<br>");
        if (!TextUtils.isEmpty(ATM_2))
            strBuilder.append("<b>ATM_2:</b> &nbsp;&nbsp;" + ATM_2 + "<br>");
        if (!TextUtils.isEmpty(ATM_3))
            strBuilder.append("<b>ATM_3:</b> &nbsp;&nbsp;" + ATM_3 + "<br>");

        return strBuilder.toString();
    }
    //endregion

    //region provisional main signboard
    public String getProvisionalMainSignborad() {
        StringBuilder strBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(PMSignboardLength))
            strBuilder.append("<b>Provisional Main Signboard Length:</b> &nbsp;&nbsp;" + PMSignboardLength + "<br>");
        if (!TextUtils.isEmpty(PMSignboardWidth))
            strBuilder.append("<b>Provisional Main Signboard Width:</b> &nbsp;&nbsp;" + PMSignboardWidth + "<br>");

        return strBuilder.toString();
    }

    //region Get Combine Message For Communication Confirmation
    public String getCommunicationStatus() {
        StringBuilder strBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(WELCOME_MAIL))
            strBuilder.append("<b>WELCOME_MAIL:</b> &nbsp;&nbsp;" + WELCOME_MAIL + "<br>");
        if (!TextUtils.isEmpty(CALL_RECEIVED))
            strBuilder.append("<b>CALL_RECEIVED:</b> &nbsp;&nbsp;" + CALL_RECEIVED + "<br>");

        return strBuilder.toString();
    }
    //endregion

    //region Get Consent Status
    public String getConsentStatus() {
        StringBuilder strBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(CONSENT_FOR_VB))
            strBuilder.append("<b>CONSENT_FOR_VB:</b> &nbsp;&nbsp;" + CONSENT_FOR_VB + "<br>");

        return strBuilder.toString();
    }
    //endregion

    //region Get Consent Status
    public String getLogisticsPaymentStatus() {
        StringBuilder strBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(LOGISTIC_PAYMENT))
            strBuilder.append("<b>LOGISTIC_PAYMENT:</b> &nbsp;&nbsp;" + LOGISTIC_PAYMENT + "<br>");
        if (!TextUtils.isEmpty(Logistic_payment_date))
            strBuilder.append("<b>LOGISTIC_PAYMENT_MADE:</b> &nbsp;&nbsp;" + Logistic_payment_date + "<br>");

        return strBuilder.toString();
    }
    //endregion

    //region Get Combine Message For Interior Work Status
    public String getInteriorWorkStatus() {
        StringBuilder strBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(Interior_Work_Status))
            strBuilder.append("<b>INTERIOR_WORK_STATUS:</b> &nbsp;&nbsp;" + Interior_Work_Status + "<br>");
        if (!TextUtils.isEmpty(Start_Date))
            strBuilder.append("<b>START_DATE:</b> &nbsp;&nbsp;" + Start_Date + "<br>");
        if (!TextUtils.isEmpty(Expected_Completion_Date))
            strBuilder.append("<b>EXPECTED_COMPLETION_DATE:</b> &nbsp;&nbsp;" + Expected_Completion_Date + "<br>");

        return strBuilder.toString();
    }
    //endregion

    //region Get GSTIN Detail
    public String getGSTINDetail() {
        StringBuilder strBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(GSTIN_Registered))
            strBuilder.append("<b>GSTIN_REGISTERED:</b> &nbsp;&nbsp;" + GSTIN_Registered + "<br>");
        if (!TextUtils.isEmpty(GSTIN_Number))
            strBuilder.append("<b>GSTIN_NUMBER:</b> &nbsp;&nbsp;" + GSTIN_Number + "<br>");
        if (!TextUtils.isEmpty(GSTIN_Address))
            strBuilder.append("<b>GSTIN_ADDRESS:</b> &nbsp;&nbsp;" + GSTIN_Address + "<br>");
        if (!TextUtils.isEmpty(GSTIN_Image))
            strBuilder.append("<b>GSTIN_CERTIFICATE_IMAGE:</b> &nbsp;&nbsp;" + GSTIN_Image + "<br>");

        return strBuilder.toString();
    }
    //endregion

    //region Get Combine Message For Site Work Commencement
    public String getWorkCommencementTooltip() {
        StringBuilder strBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(FRONTAGE_IMAGE))
            strBuilder.append("<b>FRONTAGE_IMAGE:</b> &nbsp;&nbsp;" + FRONTAGE_IMAGE + "<br>");
        if (!TextUtils.isEmpty(LEFT_WALL))
            strBuilder.append("<b>LEFT_WALL:</b> &nbsp;&nbsp;" + LEFT_WALL + "<br>");
        if (!TextUtils.isEmpty(FRONT_WALL))
            strBuilder.append("<b>FRONT_WALL:</b> &nbsp;&nbsp;" + FRONT_WALL + "<br>");
        if (!TextUtils.isEmpty(RIGHT_WALL))
            strBuilder.append("<b>RIGHT_WALL:</b> &nbsp;&nbsp;" + RIGHT_WALL + "<br>");
        if (!TextUtils.isEmpty(BACK_WALL))
            strBuilder.append("<b>BACK_WALL:</b> &nbsp;&nbsp;" + BACK_WALL + "<br>");
        if (!TextUtils.isEmpty(CEILING))
            strBuilder.append("<b>CEILING:</b> &nbsp;&nbsp;" + CEILING + "<br>");
        if (!TextUtils.isEmpty(FLOOR))
            strBuilder.append("<b>FLOOR:</b> &nbsp;&nbsp;" + FLOOR + "<br>");

        if (!TextUtils.isEmpty(Work_Commencement_Date))
            strBuilder.append("<b>Start Date:</b> &nbsp;&nbsp;" + Work_Commencement_Date + "<br>");
        if (!TextUtils.isEmpty(Work_Completion_Expected_Date))
            strBuilder.append("<b>Completion Date:</b> &nbsp;&nbsp;" + Work_Completion_Expected_Date + "<br>");

        return strBuilder.toString();
    }
    //endregion

}
