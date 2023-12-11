public class Administrator {
    private int administratorId;
    private int userId;

    public Administrator() {
    }

    public Administrator(int administratorId, int userId) {
        this.administratorId = administratorId;
        this.userId = userId;
    }

    public int getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(int administratorId) {
        this.administratorId = administratorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


}
