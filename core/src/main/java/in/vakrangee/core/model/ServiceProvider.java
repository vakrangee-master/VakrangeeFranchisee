package in.vakrangee.core.model;

/**
 * Created by Nileshd on 4/18/2016.
 */
public class ServiceProvider  {
    int _id;
    public int _name;
    public  byte[] _image;
    public int ServiceId;
    public String ServiceDescription;
    public    Byte[] bytes;
    byte[] profileImageInBytes;
    public String objectName;
    public ServiceProvider(int serviceId, String serviceDescription) {
        ServiceDescription = serviceDescription;
        ServiceId = serviceId;
    }

    public ServiceProvider(byte[] blob) {

    }

    public ServiceProvider(int name, byte[] image) {
        this._name = name;
        this._image = image;

    }


    public boolean isclicked=false;
    public int index;
    /*public String fanId;
    public String strAmount;*/

    public ServiceProvider(boolean isclicked,int index/*,String fanId,String strAmount*/)
    {
        this.index=index;
        this.isclicked=isclicked;
        /*this.fanId=fanId;
        this.strAmount=strAmount;*/
    }

    public Byte[] getAByte(){return  bytes;}
    public  void setBytes(Byte[] bytes)
    {
        bytes=bytes;
    }


    public int getServiceId() {
        return ServiceId;
    }

    public void setServiceId(int serviceId) {
        ServiceId = serviceId;
    }

    public String getServiceDescription() {
        return ServiceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        ServiceDescription = serviceDescription;
    }
    public ServiceProvider(String objectName){

        this.objectName = objectName;
    }
}
