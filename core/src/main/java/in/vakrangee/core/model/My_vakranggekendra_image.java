package in.vakrangee.core.model;

import android.graphics.Bitmap;

/**
 * Created by Nileshd on 12/28/2016.
 */
public class My_vakranggekendra_image  {


    int _id;

    //  byte[] image;
    Bitmap image;
    String vkid;
    String getcurrenttime;
    String latlong;
    String imgetype;
    String remarks;
    String imageId;
    String imageHash;
    private boolean IsChangedPhoto;


//    private int someVariable;
//
//    public int getSomeVariable() {
//        return someVariable;
//    }
//
//    public void setSomeVariable(int someVariable) {
//        this.someVariable = someVariable;
//    }
private int someVariable;
    public My_vakranggekendra_image(int someField) {
        this.someVariable=someField;
    }



    public int getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(int someVariable) {
        this.someVariable = someVariable;
    }

    public My_vakranggekendra_image(int id, Bitmap bytea, String vkid, String currenttime, String latlng, String imgtype) {
        this._id = id;
        this.image = bytea;
        this.vkid = vkid;
        this.getcurrenttime = currenttime;
        this.latlong = latlng;
        this.imgetype = imgtype;
    }

    public My_vakranggekendra_image(Bitmap bytea, String vkid, String currenttime, String latlng, String imgtype) {

        this.image = bytea;
        this.vkid = vkid;
        this.getcurrenttime = currenttime;
        this.latlong = latlng;
        this.imgetype = imgtype;

    }

    public My_vakranggekendra_image() {
    }

    public String getImgetype() {
        return imgetype;
    }

    public void setImgetype(String imgetype) {
        this.imgetype = imgetype;
    }

    public String getVkid() {
        return vkid;
    }

    public void setVkid(String vkid) {
        this.vkid = vkid;
    }

    public String getGetcurrenttime() {
        return getcurrenttime;
    }

    public void setGetcurrenttime(String getcurrenttime) {
        this.getcurrenttime = getcurrenttime;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }

    // getting phone number
    public Bitmap getImage() {
        return this.image;
    }

    // setting phone number
    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageHash() {
        return imageHash;
    }

    public void setImageHash(String imageHash) {
        this.imageHash = imageHash;
    }

    public boolean isChangedPhoto() {
        return IsChangedPhoto;
    }

    public void setChangedPhoto(boolean changedPhoto) {
        IsChangedPhoto = changedPhoto;
    }
}
