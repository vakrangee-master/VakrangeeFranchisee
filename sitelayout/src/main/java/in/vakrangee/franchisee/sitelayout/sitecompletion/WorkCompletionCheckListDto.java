package in.vakrangee.franchisee.sitelayout.sitecompletion;

import com.google.gson.annotations.SerializedName;

public class WorkCompletionCheckListDto {

    @SerializedName("work_completion_checklist_id")
    private int id;
    @SerializedName("element_name")
    private String elementName;
    @SerializedName("sub_element_name")
    private String subElementName;
    private int status = 0; // 0 - Not Done | 1 - Done

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getSubElementName() {
        return subElementName;
    }

    public void setSubElementName(String subElementName) {
        this.subElementName = subElementName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
