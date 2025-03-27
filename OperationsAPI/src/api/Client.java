package api;

public class Client {
    private int clientId;
    private String clientName;
    private String contactInfo;

    public Client(int clientId, String clientName, String contactInfo) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.contactInfo = contactInfo;
    }
    public int getClientId() {
        return clientId;
    }
    public String getClientName() {
        return clientName;
    }
    public String getContactInfo() {
        return contactInfo;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
