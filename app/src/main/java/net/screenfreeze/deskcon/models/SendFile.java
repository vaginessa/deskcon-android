package net.screenfreeze.deskcon.models;

/**
 * Created by switch87 on 17/11/17.
 */

public class SendFile {
    private String uuid;
    private String pname;
    private String type;
    private String[] filenames;

    public SendFile(String uuid, String pname, String type, String[] filenames) {

        this.uuid = uuid;
        this.pname = pname;
        this.type = type;
        this.filenames = filenames;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getFilenames() {
        return filenames;
    }

    public void setFilenames(String[] filenames) {
        this.filenames = filenames;
    }
}
