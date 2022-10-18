import java.awt.*;
import java.io.*;
import java.text.Normalizer;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import java.awt.event.*;


    
public class FrameApp extends JFrame {

	private JPanel contentPane;
	private JTable table;
    static JComboBox<String> comboBoxCategorie;
    static JComboBox<String> comboBoxNumeroLivre;
    static JComboBox<String> comboBoxNumeroAuteur;
     
    static BufferedReader tmpReadTexte;   
    static RandomAccessFile donnees;
    static File fichierBin;
    static 	JToolBar toolBar = new JToolBar();
    static HashMap<Integer,Long> addrMap = new HashMap<>();
    static String noAuteur=""; 
	static JComboBox<String> champAuteur = new  JComboBox<>();
    static 	JTableHeader entete;
	static GridBagConstraints gbc_tlBar;


    

    static final int TAILLE_TITRE = 166;
    static final int TAILLE_CAT = 14;
    static final String FICHIER_BIN = "src/Livres.bin";
    static final String FICHIER_TXT = "src/Livres.txt";
    static final String[] COLONNE = {"Numero","Titre","Numero Auteur","Annee","Pages","Cathegorie"};


 	/*******************************************************************************************
	 * constructeur de la classe
	 ***************************************************************************************************/
	public FrameApp() {
        charger();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 100, 1500, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{266, 62, 0};
		gbl_contentPane.rowHeights = new int[]{21, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);



		JScrollPane scrollPane = new JScrollPane();
	
        
		table = new JTable();
        table.setModel(imageTable());
        styleTable(Color.LIGHT_GRAY, Color.BLACK);
		scrollPane.setViewportView(table);

		gbc_tlBar = new GridBagConstraints();
		gbc_tlBar.insets = new Insets(0, 5, 10, 5);
		gbc_tlBar.anchor = GridBagConstraints.NORTHWEST;
		gbc_tlBar.gridx = 0;
		gbc_tlBar.gridy = 0;
		contentPane.add(toolBar, gbc_tlBar);
		
		JButton buttonLister = new JButton("Lister");
		buttonLister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

                try {
                    DefaultTableModel model = listerLivres();
                    table.setModel(model);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
			}
		});
		toolBar.add(buttonLister);
		
		JButton buttonModifier = new JButton("Modifier");
		buttonModifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
                    String strCle = JOptionPane.showInputDialog(null, "Entrez le numéro du livre a modifier");
                    int cle= Integer.parseInt(strCle);
                    if(!rechercheCle(cle)){
                            JOptionPane.showMessageDialog(null, "le livre du numéro "+ cle +" n' existe pas!!");
                            
                    }else{
            
                        modifierTitre(cle);
                        maj(listerLivresChoisis(strCle));
                        
                    }
//                    afficheAdresse();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
			}
		});
		toolBar.add(buttonModifier);
		
		JButton buttonSupprimer = new JButton("Supprimer");
		buttonSupprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
                    String strCle = JOptionPane.showInputDialog(null, "Entrez le numéro du livre a suprimer");
                    int cle= Integer.parseInt(strCle);
                    if(!rechercheCle(cle)){
                            JOptionPane.showMessageDialog(null, "le livre du numéro "+ cle +" n' existe pas!!");
                            
                    }else{
            
                        supprimer(cle);
                        maj(listerLivres());
                    }
                        
 //                    afficheAdresse();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
			}
		});
		toolBar.add(buttonSupprimer);
		
		JButton Ajouterbutton = new JButton("Ajouter");
		Ajouterbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
                try {
                    String strCle = JOptionPane.showInputDialog(null, "Entrez le numéro du livre a ajouter");
                    int cle= Integer.parseInt(strCle);
                    if(rechercheCle(cle)){
                            JOptionPane.showMessageDialog(null, "le livre du numéro "+ cle +"  existe déjà!!");
                            
                    }else{
            
                    ajouter(strCle);
                    maj(listerLivresChoisis(strCle));
                    }
 //                   afficheAdresse();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
			}
		});
		toolBar.add(Ajouterbutton);

		JButton bouttonQuitter = new JButton("Quitter");
		bouttonQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
		});

        btnStyle(buttonLister);
		btnStyle(buttonModifier);
		btnStyle(buttonSupprimer);
		btnStyle(Ajouterbutton);
        btnStyle(bouttonQuitter);

        try {
        String[] items = itemsListeDeroulante("cathegorie");
        
		comboBoxCategorie = new JComboBox<>(items);
        
		comboBoxCategorie.setToolTipText("");
		comboBoxCategorie.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
                    try {
                        DefaultTableModel model = listerLivresChoisis((String)comboBoxCategorie.getSelectedItem());
                        table.setModel(model);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
			}
		});
    
        toolBar.add(comboBoxCategorie);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
		
        try {
            String[] items = itemsListeDeroulante("numero");
            comboBoxNumeroLivre = new JComboBox<>(items);
            comboBoxNumeroLivre.setToolTipText("");
            comboBoxNumeroLivre.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    try {
                        DefaultTableModel model = listerLivresChoisis((String)comboBoxNumeroLivre.getSelectedItem());
                        table.setModel(model);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            toolBar.add(comboBoxNumeroLivre);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
            try {
                String[] items = itemsListeDeroulante("auteur");
                comboBoxNumeroAuteur = new JComboBox<>(items);
                comboBoxNumeroAuteur.setToolTipText("");
                comboBoxNumeroAuteur.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        try {
                            DefaultTableModel model = listerLivresChoisis((String)comboBoxNumeroAuteur.getSelectedItem());
                            table.setModel(model);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        }
                });
                toolBar.add(comboBoxNumeroAuteur);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                toolBar.add(bouttonQuitter);

        
 		gbc_tlBar.gridwidth = 2;
		gbc_tlBar.fill = GridBagConstraints.BOTH;
		gbc_tlBar.gridx = 0;
		gbc_tlBar.gridy = 1;
		contentPane.add(scrollPane, gbc_tlBar);
		
	}

 	/*******************************************************************************************
	 * les fonctions de l'application
	 ***************************************************************************************************/
    public void charger(){
        String nomFichier="",pathFichier="";
		final JFileChooser fc = new JFileChooser();


        String elems[] = new String[6];
        int numLivre, num_Auteur, annee, nbre_Pages;
        String titre="", categ="";
        File file = new File(FICHIER_BIN);
        try{
            if(!file.exists()){
                int val_retour = fc.showOpenDialog(this);
                if (val_retour == JFileChooser.APPROVE_OPTION) {
                    pathFichier= fc.getSelectedFile().getAbsolutePath();
                    nomFichier= fc.getSelectedFile().getName();
                
                    if(nomFichier.equals("livres.txt")){ 
                        tmpReadTexte = new BufferedReader(new FileReader(pathFichier));
                        long adr =0;
                        donnees = new RandomAccessFile(file, "rw");

                        String ligne = tmpReadTexte.readLine();
                        while(ligne != null ){
                            elems = ligne.split(";");
                            numLivre = Integer.parseInt(elems[0]);
                            titre = formaterString(elems[1],TAILLE_TITRE);
                            num_Auteur = Integer.parseInt(elems[2]);
                            annee = Integer.parseInt(elems[3]);
                            nbre_Pages = Integer.parseInt(elems[4]);
                            categ = formaterString(elems[5],TAILLE_CAT);

            
                            donnees.writeInt(numLivre);
                            donnees.writeUTF(titre);
                            donnees.writeInt(num_Auteur);
                            donnees.writeInt(annee);
                            donnees.writeInt(nbre_Pages);
                            donnees.writeUTF(categ);
                            
                            addrMap.put(numLivre,adr);
                            adr = donnees.getFilePointer();

                            
                            ligne = tmpReadTexte.readLine();
                        }
                        tmpReadTexte.close();
                    }else{
                        JOptionPane.showMessageDialog(null, "Le nom du fichier doit etre << livres.txt >>!!");
                        System.exit(0);
                    }    
                }    
            }else{
                long adr =0;

                donnees = new RandomAccessFile(file, "rw");
                    while (true) {// Boucle infinie
                        numLivre = donnees.readInt();
                        titre = donnees.readUTF();
                        num_Auteur = donnees.readInt();
                        annee = donnees.readInt();
                        nbre_Pages = donnees.readInt();
                        categ = donnees.readUTF();
                        addrMap.put(numLivre,adr);
                        adr = donnees.getFilePointer();

                    }
            }
            donnees.close();
        } catch (Exception e) {
            System.out.println("message en chargement "+e.getMessage());
        } 

}
 
public void maj(DefaultTableModel model) {
    try {
        table.setModel(model);
        DefaultComboBoxModel<String> modelCat = new DefaultComboBoxModel<>(itemsListeDeroulante("cathegorie"));
        comboBoxCategorie.removeAll();
        comboBoxCategorie.setModel(modelCat);
        DefaultComboBoxModel<String> modelNum = new DefaultComboBoxModel<>(itemsListeDeroulante("numero"));
        comboBoxNumeroLivre.removeAll();
        comboBoxNumeroLivre.setModel(modelNum);
        DefaultComboBoxModel<String> modelAut = new DefaultComboBoxModel<>(itemsListeDeroulante("auteur"));
        comboBoxNumeroAuteur.removeAll();
        comboBoxNumeroAuteur.setModel(modelAut);
    } catch (IOException e) {
        e.printStackTrace();
    }
    styleTable(Color.LIGHT_GRAY, Color.black);

    
}
    public DefaultTableModel listerLivres() throws IOException {
        int num, num_Auteur, annee, nbre_Pages;
        String titre, categ;
        DefaultTableModel model = new DefaultTableModel(COLONNE,0);

        donnees = new RandomAccessFile(FICHIER_BIN, "rw");
        try {
            long adr =0;
            //   while (true) {// Boucle infinie
                for(Integer key:addrMap.keySet()){
                   if(key>0){ 
                        adr = addrMap.get(key);
                        donnees.seek(adr);
                        num = donnees.readInt();
                        titre = donnees.readUTF();
                        num_Auteur = donnees.readInt();
                        annee = donnees.readInt();
                        nbre_Pages = donnees.readInt();
                        categ = donnees.readUTF();
                        model.addRow(new Object[]{num,titre,num_Auteur, annee, nbre_Pages,categ});
                    }				
	
           }
        } catch (IOException e) {
            System.out.println("Gros probléme! lister" + e.getMessage());
        } finally {
            donnees.close();
        }
       return model;
    }
    public DefaultTableModel listerLivresChoisis(String choix) throws IOException {
        int num, num_Auteur, annee, nbre_Pages;
        String titre, categ;
        //String[] COLONNE = {"Numero","Titre","Numero Auteur","Annee","Pages","Cathegorie"};
        DefaultTableModel model = new DefaultTableModel(COLONNE,0);

        donnees = new RandomAccessFile(FICHIER_BIN, "rw");
        try {
            long adr =0;
            //   while (true) {// Boucle infinie
            for(Integer key:addrMap.keySet()){
                   if(key>0){ 
                    adr = addrMap.get(key);
                    donnees.seek(adr);
                    num = donnees.readInt();
                   titre = donnees.readUTF();
                   num_Auteur = donnees.readInt();
                   annee = donnees.readInt();
                   nbre_Pages = donnees.readInt();
                   categ = donnees.readUTF();
                        if(choix.equals(categ)){
                        model.addRow(new Object[]{num,titre,num_Auteur, annee, nbre_Pages,categ});				
                        }else if(choix.equals(String.valueOf(num))){
                            model.addRow(new Object[]{num,titre,num_Auteur, annee, nbre_Pages,categ});				
                        }else if(choix.equals(String.valueOf(num_Auteur))){
                            model.addRow(new Object[]{num,titre,num_Auteur, annee, nbre_Pages,categ});				
                        }
                   }
           }
        } catch (IOException e) {
            System.out.println("Gros probléme! listerChoisi" + e.getMessage());
        } finally {
            donnees.close();
        }
        return model;
    }

    public String[] itemsListeDeroulante(String nomListederoulante) throws IOException {
        int num, num_Auteur, annee, nbre_Pages;
        String titre, categ;
        ArrayList<String> liste = new ArrayList<>();
        String precedent="",current ="";
        donnees = new RandomAccessFile(FICHIER_BIN, "rw");
        try {
            long adr =0;
            //   while (true) {// Boucle infinie
            for(Integer key:addrMap.keySet()){
                   if(key>0){ 
                    adr = addrMap.get(key);
                    donnees.seek(adr);
                   num = donnees.readInt();
                   titre = donnees.readUTF();
                   num_Auteur = donnees.readInt();
                   annee = donnees.readInt();
                   nbre_Pages = donnees.readInt();
                   categ = donnees.readUTF();
                        if(nomListederoulante.equals("cathegorie")){
                                current = categ;
                                liste.add(current);
                        }else if(nomListederoulante.equals("numero")){
                                current = String.valueOf(num);
                                liste.add(current);
                        }else if(nomListederoulante.equals("auteur")){
                            current = String.valueOf(num_Auteur);
                                liste.add(current);
                        }
                   }    
	
           }
        } catch (Exception e) {
            donnees.close();
        } 
        String[] listeRetourne = new String[0];
        if(liste.size()!=0){
            Collections.sort(liste);
            ArrayList<String> listeTmp = new ArrayList<>();
            listeTmp.add(liste.get(0));
            for(String str:liste){
                if(listeTmp.indexOf(str)==-1){
                    listeTmp.add(str);
                }
            } 
               
            listeRetourne = new String[listeTmp.size()];
            for(int i=0;i<listeTmp.size();i++){
                listeRetourne[i]=listeTmp.get(i);
            }
        }
        return listeRetourne;

    }

 	/*******************************************************************************************
	 * *****************************S A P 
	 ***************************************************************************************************/

    public void ajouter(String strCle) throws IOException{
            ArrayList<String> data = new ArrayList<>(){{add(strCle);add(null);add(null);add(null);add(null);add(null);}};
            String[] retour = fomulaireAjouter(data,new ArrayList<String>(){{add("Numéro");add("Titre");add("Année");add("Pages");}},"                         Entrez les informations du votre nouveau livre");
            if (retour != null){
                int num = Integer.parseInt(retour[0]);
                String titre = formaterString(retour[1],TAILLE_TITRE);
                int auteur = Integer.parseInt(retour[4]);
                int annee = Integer.parseInt(retour[2]);
                int pages = Integer.parseInt(retour[3]);
                String cathegorie = formaterString(retour[5],TAILLE_CAT);
                long adr = trouveAddresseVide();
                    try{
                        donnees = new RandomAccessFile(FICHIER_BIN, "rw");
                        if(adr==-1){
                            adr = donnees.length();
                        }else{
                            Integer cleDeleted=0;
                                for (java.util.Map.Entry<Integer,Long> entry : addrMap.entrySet()) { 
                                    if (entry.getValue()==adr) { 
                                        cleDeleted = entry.getKey();
                                    } 
                                }
                                addrMap.remove(cleDeleted);                    
                        }
                        addrMap.put(num,adr);
                        donnees.seek(adr);
                        donnees.writeInt(num);
                        donnees.writeUTF(titre);
                        donnees.writeInt(auteur);
                        donnees.writeInt(annee);
                        donnees.writeInt(pages);
                        donnees.writeUTF(cathegorie);

                    } catch (IOException e) {
                        System.out.println("Gros probléme! ajouter" + e.getMessage());
                    } finally {
                        donnees.close();
                    }
                }
                JOptionPane.showMessageDialog(null, "le livre numero "+ strCle + " est ajoute avec succes");
           
             
    }
    public void modifierTitre(int cle) throws IOException{
        String titre =JOptionPane.showInputDialog(null, "entrez le nouveau titre");
        titre = formaterString(titre,TAILLE_TITRE);
        long adr = trouverAdresse(cle);
            try{
                donnees = new RandomAccessFile(FICHIER_BIN, "rw");
                donnees.seek(adr+4);
                donnees.writeUTF(titre);

            } catch (IOException e) {
                System.out.println("Gros probléme! Modifier" + e.getMessage());
            } finally {
                donnees.close();
            }
        
                
    }

    public void supprimer(int cle) throws IOException {
            String titreVide="",cathegorieVide="";
            for(int i=0;i<TAILLE_TITRE;i++){
                titreVide +=" ";
            }
            for(int i=0;i<TAILLE_CAT;i++){
                cathegorieVide +=" ";
            }
        
            long adr = trouverAdresse(cle);
            try{
                donnees = new RandomAccessFile(FICHIER_BIN, "rw");
                addrMap.put(-1*cle,adr);
                addrMap.remove(cle);

                donnees.seek(adr);
                donnees.writeInt(-1*cle);
                donnees.writeUTF(titreVide);
                donnees.writeInt(0);
                donnees.writeInt(0);
                donnees.writeInt(0);
                donnees.writeUTF(cathegorieVide);
            } catch (IOException e) {
                System.out.println("Gros probléme! suprimer" + e.getMessage());
            } finally {
                donnees.close();
            }
            JOptionPane.showMessageDialog(null, "le livre numero "+ cle + " est suprime avec succes");
            
    }
    
    public String[] fomulaireAjouter(ArrayList<String> data,ArrayList<String> listeChamps,String titre) throws IOException {
        String[] retour = new String[6];
                Dimension d =new Dimension(350,20);
                Color cl = new Color(102,178,255);
                ArrayList<JTextField> listeJtxt = new ArrayList<>();
    
                JPanel panePrincipal = new JPanel(new GridBagLayout());
                JPanel gPane = new JPanel(new GridLayout(listeChamps.size()+2,1,0,5));
                GridBagConstraints c = new GridBagConstraints();	
                JLabel lblTitre = new JLabel(titre);
                lblTitre.setFont(new Font("Serif", Font.BOLD, 20));
                lblTitre.setForeground(Color.blue);
                    ButtonGroup groupeWeb = new ButtonGroup();
                    gPane.add(lblTitre);
                    for(int i=0;i<listeChamps.size();i++){
                        JPanel pane = new JPanel();
                        JTextField jtxt = new JTextField(data.get(i));
                        jtxt.setPreferredSize(d);
                        JLabel lbl = new JLabel(listeChamps.get(i));
                        lbl.setPreferredSize(new Dimension(50,20));
                        lbl.setLabelFor(jtxt);
                        listeJtxt.add(jtxt);
                        pane.add(lbl);
                        pane.add(jtxt);
                        gPane.add(pane);
        
                    }
                    c.weightx = 0.0;
                    c.gridx = 0;
                    c.gridy = 0;
                    c.gridwidth=1;
                    panePrincipal.add(gPane,c);
                    
                    if(listeChamps.size()>2){	
            
                    JLabel lblChoix = new JLabel("                    Choisissez une cathegorie ");
                    JPanel paneRadio = new JPanel(new GridLayout(2,1,0,5));
                    paneRadio.setBackground(cl);
                    JPanel paneElementradio = new JPanel();
                    paneElementradio.setBackground(cl);
                    JRadioButton vide = new JRadioButton("");
                    
                    vide.setBackground(cl);
                    groupeWeb.add(vide);
                    vide.setSelected(true);
                    paneElementradio.add(vide);
                    String[] listeCathegorie = itemsListeDeroulante("cathegorie");
                    int longueur = listeCathegorie.length;
                    for(int i=0;i<longueur;i++){
                        JRadioButton rBtn = new JRadioButton(listeCathegorie[i]);
                        rBtn.setBackground(cl);
                        groupeWeb.add(rBtn);
                        paneElementradio.add(rBtn);
                    }
                    paneRadio.add(lblChoix);
                    paneRadio.add(paneElementradio);
                    paneRadio.setPreferredSize(new Dimension(300,10));
                    c.ipadx = 150;      
                    c.ipady = 50;      
                    c.weightx = 0.0;
                    c.gridx = 0;
                    c.gridy = 1;
                    c.gridwidth=2;
                    panePrincipal.add(paneRadio,c);
                    JPanel paneAuteur = new JPanel();
                    champAuteur = new  JComboBox<>(itemsListeDeroulante("auteur"));
                    champAuteur.setPreferredSize(d);
                    champAuteur.addActionListener(new ActionListener() {     
                        @Override
                        public void actionPerformed(ActionEvent e) {
                           noAuteur = champAuteur.getSelectedItem().toString();      
                        }
                      });
                    JLabel lblAuteur = new JLabel("Auteur");
                    lblAuteur.setPreferredSize(new Dimension(50,20));
                    lblAuteur.setLabelFor(champAuteur);
                    paneAuteur.add(lblAuteur);
                    paneAuteur.add(champAuteur);
                    gPane.add(paneAuteur);
                    }	
    
        
                int res = JOptionPane.showConfirmDialog(null,panePrincipal,"Modification Livre",JOptionPane.YES_NO_CANCEL_OPTION);
                if(res == JOptionPane.YES_OPTION){
                    for(int i=0;i<listeJtxt.size();i++){
                        retour[i]= listeJtxt.get(i).getText();
                    }
                    if(noAuteur.equals("")){
                        retour[listeJtxt.size()] = "0";
    
                    }else{
                        retour[listeJtxt.size()] = noAuteur;
                    }
                    Enumeration<AbstractButton> allRadioButton=groupeWeb.getElements();  
                    while(allRadioButton.hasMoreElements())  
                    {  
                       JRadioButton temp=(JRadioButton)allRadioButton.nextElement();  
                       if(temp.isSelected())  
                       { 
                            retour[listeJtxt.size()+1]= temp.getText();  
                       }  
                    }            
                    
                }else{
                    retour = null;
                }  
            //}
        
        
        return retour;        
    
    }

	/*******************************************************************************************
	 * ***********************style***********************************************
	 ***************************************************************************************************/
   

   public DefaultTableModel imageTable() {
	entete = table.getTableHeader();
	entete.setFont(new Font("Serif", Font.BOLD, 18));
	entete.setBackground(Color.orange);//new Color(128,128,128));//new Color(105,105,105));
	entete.setForeground(Color.BLACK);

	//String[] column = {"Bienvenue à la gestion d'une bibliothéque "};
	//table.setRowHeight(558);
	DefaultTableModel model = new DefaultTableModel(COLONNE,0)
	{
		
		public Class getColumnClass(int column)
		{
			switch (column)
			{
				case 0: return Icon.class;
				default: return super.getColumnClass(column);
			}
		}
	};

	ImageIcon img =  new ImageIcon("src\\images\\livre2.jpg");
	model.addRow(new Object[]{img});

	return model;	
}
public void btnStyle(JButton btn){
	btn.setSize(new Dimension(200,20));
	btn.setBackground(new Color(12,128,144));		
	btn.setForeground(Color.white);
	btn.setFont( new Font("Serif", Font.BOLD, 18));
	btn.setOpaque(true);

}

public void styleTable(Color bgColor,Color pColor) {
    
	JTableHeader entete = table.getTableHeader();
	entete.setFont(new Font("Serif", Font.BOLD, 18));
	entete.setBackground(new Color(128,128,128));//new Color(105,105,105));
	entete.setForeground(Color.white);
	TableColumnModel columnModelEntete = entete.getColumnModel();
	columnModelEntete.getColumn(0).setPreferredWidth(1);
	columnModelEntete.getColumn(1).setPreferredWidth(200);
	columnModelEntete.getColumn(2).setPreferredWidth(1);
	columnModelEntete.getColumn(3).setPreferredWidth(1);
	columnModelEntete.getColumn(4).setPreferredWidth(1);
	columnModelEntete.getColumn(5).setPreferredWidth(50);
    
	TableColumnModel columnModel = table.getColumnModel();
	columnModel.getColumn(0).setPreferredWidth(1);
	columnModel.getColumn(1).setPreferredWidth(200);
	columnModel.getColumn(2).setPreferredWidth(1);
	columnModel.getColumn(3).setPreferredWidth(1);
	columnModel.getColumn(4).setPreferredWidth(1);
	columnModel.getColumn(5).setPreferredWidth(50);
	table.setBackground(bgColor);
	table.setForeground(pColor);
	table.setRowHeight(20);
	table.setFont(new Font("Serif", Font.BOLD, 18));
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
}




	/*******************************************************************************************
	 * main de l'application
	 ***************************************************************************************************/
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameApp frame = new FrameApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
 	/*******************************************************************************************
	 * les fonctions generale
	 ***************************************************************************************************/
    public void afficheAdresse() {
        JTextArea sortie = new JTextArea();
        for(Integer key:addrMap.keySet()){
            sortie.append(key + " " + addrMap.get(key)+"\n");
        }
        JOptionPane.showMessageDialog(null, sortie);
        
    }
    public long trouverAdresse(int cle) {
        long adr=-1;
        for(Integer key:addrMap.keySet()){
            if(key==cle){
                adr = addrMap.get(key);
                break;
            }
        }
        return adr;
    }
    public long trouveAddresseVide() {
		long adr=-1;
		for(Integer key:addrMap.keySet()){
			if(key<0){
				adr=addrMap.get(key);
				break;
			}
		} 
		return adr;
		
	}
    public boolean rechercheCle(int cle) {
        boolean retour=false;
        for(Integer key:addrMap.keySet()){
            if(key==cle){
                retour = true;
                break;
            }
        } 
        return retour;
       } 
       
    public static boolean avecAccent(String chaine){
        String strTemp = Normalizer.normalize(chaine, Normalizer.Form.NFD);
        if(chaine.equals(strTemp)){
          return false;
        }else{
          return true;
        }
        }
        public static long tailleMot(String titre,String cathegorie) {
            long taille = titre.length() + cathegorie.length() + 20;
            if(avecAccent(titre) && avecAccent(cathegorie)){
                taille+=2;
            }else if(avecAccent(titre) || avecAccent(cathegorie)){
                taille+=1;
            }
            return taille;
        }
        public static String formaterString(String leString, int TAILLE_MAX){
            int taille = TAILLE_MAX-leString.length();
            if(taille>=0){
                for(int i=0;i<taille;i++){
                    leString+=" ";
                }
            }else{
                leString = leString.substring(0, TAILLE_MAX);
            }
        
            return leString;
        }
        

}

    

