package sample;
public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String cnp;
    private String nume;
    private String prenume;
    private String adresa;
    private String nrTelefon;
    private String email;
    private String iban;
    private int nrContract;

    public User() {
    }

    public User(int userId, String username, String password, String role, String cnp, String nume, String prenume, String adresa, String nrTelefon, String email, String iban, int nrContract) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.cnp = cnp;
        this.nume = nume;
        this.prenume = prenume;
        this.adresa = adresa;
        this.nrTelefon = nrTelefon;
        this.email = email;
        this.iban = iban;
        this.nrContract = nrContract;
    }

    // Getter and Setter methods
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getNrTelefon() {
        return nrTelefon;
    }

    public void setNrTelefon(String nrTelefon) {
        this.nrTelefon = nrTelefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public int getNrContract() {
        return nrContract;
    }

    public void setNrContract(int nrContract) {
        this.nrContract = nrContract;
    }
}

