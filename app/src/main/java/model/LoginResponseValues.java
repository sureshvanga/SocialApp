package model;


public class LoginResponseValues {

    private int isGmail;

    private String name;
    private String emailId;
    private String dob;
    private String toggleLine;
    private String aboutme;
    private String photoUrl;

    private String loginUsername;

    private static final LoginResponseValues holder = new LoginResponseValues();
    public static LoginResponseValues getInstance() {return holder;}


    public int getIsGmail() {
        return isGmail;
    }

    public void setIsGmail(int isGmail) {
        this.isGmail = isGmail;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getToggleLine() {
        return toggleLine;
    }

    public void setToggleLine(String toggleLine) {
        this.toggleLine = toggleLine;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }
}
