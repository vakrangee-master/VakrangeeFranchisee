package in.vakrangee.core.service;

import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;

/**
 * Created by Reshma on 2/28/2018.
 */

public class ErrorDTO {

    @SerializedName("ECode")
    private String errCode;

    @SerializedName("EMsg")
    private String errMessage;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public JSONObject toJSONObject() throws Exception {
        JSONObject jObject = new JSONObject();

        jObject.put("ECode", getErrCode() == null ? JSONObject.NULL : getErrCode());
        jObject.put("EMsg", getErrMessage() == null ? JSONObject.NULL : getErrMessage());

        return jObject;
    }

    public String toJSONString() throws Exception {
        return toJSONObject().toString();
    }

    public Object toDtoObject(String jsonString) throws Exception {
        ErrorDTO errorDTO = new ErrorDTO();

        JSONObject jsonObject = new JSONObject(jsonString);

        errorDTO.setErrCode(jsonObject.getString("ECode"));
        errorDTO.setErrMessage(jsonObject.getString("EMsg"));

        return ((Object) jsonObject);
    }

    public String toString() {
        return "{\"ECode\":\"" + getErrCode() + "\",\"EMsg\":\"" + getErrMessage() + "\"}";
    }
}
