package sample;

public class SearchModel {

    String nume;
    String prenume;
    String rol;

    public SearchModel(String nume, String prenume, String rol) {
        this.nume = nume;
        this.prenume = prenume;
        this.rol = rol;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getRol() {
        return rol;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
