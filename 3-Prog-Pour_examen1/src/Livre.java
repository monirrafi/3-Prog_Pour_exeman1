public class Livre {
    private int num = 0;
    private String  titre = "";
    private int auteur = 0;
    private int annee = 0;
    private int pages = 0;
    private String cathegorie = "";
    
    public Livre(int num, String titre, int auteur, int annee, int pages, String cathegorie) {
        this.num = num;
        this.titre = titre;
        this.auteur = auteur;
        this.annee = annee;
        this.pages = pages;
        this.cathegorie = cathegorie;
    }
    public Livre() {
    }
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public int getAuteur() {
        return auteur;
    }
    public void setAuteur(int auteur) {
        this.auteur = auteur;
    }
    public int getAnnee() {
        return annee;
    }
    public void setAnnee(int annee) {
        this.annee = annee;
    }
    public int getPages() {
        return pages;
    }
    public void setPages(int pages) {
        this.pages = pages;
    }
    public String getCathegorie() {
        return cathegorie;
    }
    public void setCathegorie(String cathegorie) {
        this.cathegorie = cathegorie;
    }
    public static String formatMot(String mot,int max) {
		String retour ="";
		int lng = mot.length();
		if(lng>= max){
			retour = mot.substring(0, max);
		}else{
			retour = mot;
			for(int i=0;i<max-lng;i++){
				retour += " ";
			}
			//retour +="\t";
		}
		return retour;
	}
    @Override
    public String toString() {
        return num + "\t" + formatMot(titre,100) + "\t" + auteur + "\t" + annee + "\t" + pages + "\t" + cathegorie + "\n";
    }
       
    
}
