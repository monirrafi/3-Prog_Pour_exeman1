public class TableIndex {
    private long adr;
    private long taille;
    private int status;
    private Livre livre;
    public TableIndex(long adr, long taille, int status, Livre livre) {
        this.adr = adr;
        this.taille = taille;
        this.status = status;
        this.livre = livre;
    }
    public long getAdr() {
        return adr;
    }
    public void setAdr(long adr) {
        this.adr = adr;
    }
    public long getTaille() {
        return taille;
    }
    public void setTaille(long taille) {
        this.taille = taille;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public Livre getLivre() {
        return livre;
    }
    public void setLivre(Livre livre) {
        this.livre = livre;
    }

}
