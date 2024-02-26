package in.vakrangee.core.commongui.status;

import java.io.Serializable;

public class NameStatusDto implements Serializable {

    private String name;
    private String status;
    private String statusCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
