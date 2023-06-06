
package application;
	
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;


// Konstanten im Spiel
// #########################
interface Konstanten {
	// Werte für eine Zelle
	final char SPIELER_R = 'R';
	final char SPIELER_G = 'G';
	final char FREI = ' ';
	final char ZUENDE = ' ';
	// Aussehen
	final Color FARBE_R = Color.PALEVIOLETRED;
	final Color FARBE_G = Color.PINK;
	final double STRICHTSTAERKE = 5.0;
}


// Klasse zum Zeichnen einer Zelle im Spielfeldes
// ###############################################
class Zelle extends Pane {
	
  private char inhalt;   // Der Inhalt der Zelle: ' ', 'X', 'O'
  private Main spiel;   // Die Zelle ist Teil des Spieles
     // Erlaubt uns die Weiterleitung von Klick-Events an das Spiel zur Überprüfung
  
  public Zelle(Main spiel) {
	this.spiel = spiel;
  	inhalt = Konstanten.FREI; // Zu Beginn ist die Zelle leer
  	// Die Referenz für CSS findet ihr: 
  	// => https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html
  	this.setStyle("-fx-border-color: black"); // Rahmen für das Feld
  	this.setPrefSize(2000, 2000); // Max Grösze
  	this.setOnMouseClicked(e -> handleMouseClick()); // Wenn man darauf klickt
  }
  
  public char getInhalt() {
  	return inhalt;
  } 
  
  /**
   * Ein Spielzug wird dargestellt
   * @param stein welcher Spieler hat gezogen 
   */
  public void zeichnen(char stein) {
	  // Wir merken uns den Spielstein
	  inhalt = stein;      
	  
	  Rectangle rec = new Rectangle(0, 0, this.getWidth() , this.getHeight() );
	  rec.widthProperty().bind(this.widthProperty());
	  rec.heightProperty().bind(this.heightProperty());
	  rec.setStrokeWidth(2);
	  rec.setStroke(Color.BLACK);
	  rec.setFill(Color.TAN);
	  if (inhalt ==  Konstanten.FREI) {
		  Ellipse ellipse = new Ellipse(
	  			  this.getWidth() / 2, 
	  			  this.getHeight() / 2, 
	  			  this.getWidth() / 2 - 10, 
	  			  this.getHeight() / 2 - 10);
			  
			  // Ursprung an die Mitte der Zelle binden
			  ellipse.centerXProperty().bind(this.widthProperty().divide(2));
			  ellipse.centerYProperty().bind(this.heightProperty().divide(2));
			  
			  // Größe an die Zellengröße binden
			  ellipse.radiusXProperty().bind(this.widthProperty().divide(2).subtract(10));        
			  ellipse.radiusYProperty().bind(this.heightProperty().divide(2).subtract(10)); 
			  
			  // Rahmen und Farben setzen
			  ellipse.setStroke(Color.WHITESMOKE);
			  ellipse.setStrokeWidth(Konstanten.STRICHTSTAERKE);
			  ellipse.setFill(Color.WHITESMOKE);
      
		  this.getChildren().addAll(rec,ellipse); 
	  }
	  else if (inhalt == Konstanten.SPIELER_R) {
		  Ellipse ellipse = new Ellipse(
	  			  this.getWidth() / 2, 
	  			  this.getHeight() / 2, 
	  			  this.getWidth() / 2 - 10, 
	  			  this.getHeight() / 2 - 10);
			  
			  // Ursprung an die Mitte der Zelle binden
			  ellipse.centerXProperty().bind(this.widthProperty().divide(2));
			  ellipse.centerYProperty().bind(this.heightProperty().divide(2));
			  
			  // Größe an die Zellengröße binden
			  ellipse.radiusXProperty().bind(this.widthProperty().divide(2).subtract(10));        
			  ellipse.radiusYProperty().bind(this.heightProperty().divide(2).subtract(10)); 
			  
			  // Rahmen und Farben setzen
			  ellipse.setStroke(Konstanten.FARBE_R);
			  ellipse.setStrokeWidth(Konstanten.STRICHTSTAERKE);
			  ellipse.setFill(Konstanten.FARBE_R);
      
		  this.getChildren().addAll(rec,ellipse); 
	  }
	  else if (inhalt == Konstanten.SPIELER_G) {
		  Ellipse ellipse = new Ellipse(
  			  this.getWidth() / 2, 
  			  this.getHeight() / 2, 
  			  this.getWidth() / 2 - 10, 
  			  this.getHeight() / 2 - 10);
		  
		  // Ursprung an die Mitte der Zelle binden
		  ellipse.centerXProperty().bind(this.widthProperty().divide(2));
		  ellipse.centerYProperty().bind(this.heightProperty().divide(2));
		  
		  // Größe an die Zellengröße binden
		  ellipse.radiusXProperty().bind(this.widthProperty().divide(2).subtract(10));        
		  ellipse.radiusYProperty().bind(this.heightProperty().divide(2).subtract(10)); 
		  
		  // Rahmen und Farben setzen
		  ellipse.setStroke(Konstanten.FARBE_G);
		  ellipse.setStrokeWidth(Konstanten.STRICHTSTAERKE);
		  ellipse.setFill(Konstanten.FARBE_G);
      
		  this.getChildren().addAll(rec,ellipse); 
	  }
  	}

  	private void handleMouseClick() {
	  // Teile dem Spiel mit, dass auf diese Zelle geklickt wurde
	  spiel.spielzug(this);
  	}
}

// Das TicTacToe Spiel
// #####################
public class Main extends Application {

	private char werZieht = Konstanten.SPIELER_R;

	// Das Spielfeld ist ein 2 dimensionales Array
	private Zelle[][] spielfeld =  new Zelle[6][7];

	// Ausgabefeld für die Meldungen des Programmes
	private Label lblAnzeige = new Label("X ist am Zug");

   	@Override
	public void start(Stage primaryStage) {
   		// GridPane für die Anzeige
   		GridPane tabelle = new GridPane(); 
   		// Das Spielfeld aufbauen
		for (int zeile = 0; zeile < 6; zeile++) {
			for (int spalte = 0; spalte < 7; spalte++) {
				Zelle zelle = new Zelle(this);
				// Wir merken uns die Zelle im Spielfeld Modell
				spielfeld[zeile][spalte] = zelle;
				spielfeld[zeile][spalte].zeichnen(Konstanten.FREI);
				// und tragen die geleiche Zelle in der Darstellung ein
				// Vorsicht: die Spalte kommt zuerst !
				tabelle.add(zelle, spalte, zeile);
				
			}
		}
        
		// Den Label in einer HBox zentriert darstellen
		HBox box = new HBox(10);
		box.setAlignment(Pos.BASELINE_CENTER);
	    box.getChildren().add(lblAnzeige);
	    
	    // Das Spielfeld und die Anzeige untereinander darstellen
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(tabelle);
		borderPane.setBottom(box);
		
		// und alles anzeigen
		Scene scene = new Scene(borderPane, 450, 450);
		
		primaryStage.setTitle("TicTacToe");
		primaryStage.setMinHeight(200 + 50);
		primaryStage.setMinWidth(200);
		primaryStage.setScene(scene);
		primaryStage.show();   
   	}

	/**
	 * Überprüft ob das Spielfeld voll ist
	 * @return true wenn voll
	 */
	public boolean feldIstVoll() {
		for (int zeile = 0; zeile < 6; zeile++) {
			for (int spalte = 0; spalte < 7; spalte++) {
				if (spielfeld[zeile][spalte].getInhalt() == Konstanten.FREI) {
					return false;
				}
			}
		}
	    return true;
	}

	/**
	 * 
	 * @param spieler X oder O
	 * @return true ... der Spieler hat gewonnen
	 */
	public boolean hatGewonnen(char spieler) {
		 
		// 4 gleiche in einer Zeile
	    for (int zeile = 0; zeile < 6; zeile++)
	    	 for (int spalte = 0; spalte <= 3; spalte++) {
	    		 if (   spielfeld[zeile][spalte+0].getInhalt() == spieler
	    				 && spielfeld[zeile][spalte+1].getInhalt() == spieler
	    				 && spielfeld[zeile][spalte+2].getInhalt() == spieler
	    				 && spielfeld[zeile][spalte+3].getInhalt() == spieler
	    			) {
	    			 return true;
	    		 	}
	    	 }
	    // 4 gleiche in eine Spalte
	    for (int spalte = 0; spalte < 7; spalte++)
	    	for (int zeile = 0; zeile <= 2; zeile++) 
	      if (   spielfeld[zeile+0][spalte].getInhalt() == spieler
	          && spielfeld[zeile+1][spalte].getInhalt() == spieler
	    	  && spielfeld[zeile+2][spalte].getInhalt() == spieler
	          && spielfeld[zeile+3][spalte].getInhalt() == spieler) {
	        return true;
	      }

	    // Diagonale1
	    for (int zeile = 0; zeile <= 2; zeile++)
	    	 for (int spalte = 0; spalte <= 3; spalte++) {
	    		 if (   spielfeld[zeile+0][spalte+0].getInhalt() == spieler 
	    				 && spielfeld[zeile+1][spalte+1].getInhalt() == spieler 
	    				 && spielfeld[zeile+2][spalte+2].getInhalt() == spieler
	    				 && spielfeld[zeile+3][spalte+3].getInhalt() == spieler) {
	    			 return true;
	    		 }
	    	 }
        // Diagonale2
	    for (int zeile = 0; zeile <= 2; zeile++)
	    	 for (int spalte = 0; spalte <= 3; spalte++) {
	    if (   spielfeld[zeile+0][spalte+3].getInhalt() == spieler
	        && spielfeld[zeile+1][spalte+2].getInhalt() == spieler
	        && spielfeld[zeile+2][spalte+1].getInhalt() == spieler
	        && spielfeld[zeile+3][spalte+0].getInhalt() == spieler) {
	      return true;
	    		}
	    	 }
	    return false;
	  }
	  
	private int getSpalte(Zelle z) {
		for (int zeile = 0; zeile < 6; zeile++)
			for (int spalte = 0; spalte < 7; spalte++) {
				if (spielfeld[zeile][spalte] == z) {
					return spalte;
				}
			}
		return -1;
	}
	
	private Zelle getUntersteFreieZell(int spalte) {
		for (int zeile = 5; zeile >= 0; zeile--) {
			Zelle akt = spielfeld[zeile][spalte];
			if (akt.getInhalt() == Konstanten.FREI) {
				return akt;
			}
		}
		return null;
	}
	
	
	  // Spieler hat auf die Zelle geklickt
      void spielzug(Zelle feld) {
		// Nur wenn das Feld noch FREI ist und das Spiel nicht zu Ende ist
	  	if (feld.getInhalt() == Konstanten.FREI && werZieht != Konstanten.ZUENDE) {
	  		int spalte = getSpalte(feld);
	  		Zelle freieZelle = getUntersteFreieZell(spalte);
	  		if (freieZelle == null) {
	  			return;
	  		}
	  		// Wir stellen den Spielzug dar
	  		freieZelle.zeichnen(werZieht);
	  		
	  		// wenn der Spieler gewonnen hat
	  		if (hatGewonnen(werZieht)) {
	  			lblAnzeige.setText(werZieht + " hat gewonnen: Das Spiel ist zu Ende");
	  			werZieht = Konstanten.ZUENDE; 
	  		}
	  		// Wenn das Spielfeld voll ist
	  		else if (feldIstVoll()) {
	  			lblAnzeige.setText("Das Spiel ist vobei - Keiner hat gewonnen");
	  			werZieht = Konstanten.ZUENDE; 
	  		}
	  		// ansonsten ist der nächste Spieler am Zug
	  		else {
	  			// Spieler wechseln
	  			if (werZieht == Konstanten.SPIELER_R) {
	  				werZieht = Konstanten.SPIELER_G;
	  			} else {
	  				werZieht = Konstanten.SPIELER_R;
	  			}
	  			lblAnzeige.setText(werZieht + " ist am Zug");
	  		}
	  	}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}








