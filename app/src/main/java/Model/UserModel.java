package Model;

import java.io.Serializable;

public class UserModel implements Serializable {

    private String email , firebaseId  ,name , phoneNo , salonId;

    public UserModel(String email, String firebaseId, String name, String phoneNo, String salonId) {
        this.email = email;
        this.firebaseId = firebaseId;
        this.name = name;
        this.phoneNo = phoneNo;
        this.salonId = salonId;
    }
    public UserModel(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getSalonId() {
        return salonId;
    }

    public void setSalonId(String salonId) {
        this.salonId = salonId;
    }
}
