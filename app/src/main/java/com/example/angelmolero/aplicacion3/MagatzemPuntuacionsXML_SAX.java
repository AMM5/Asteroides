package com.example.angelmolero.aplicacion3;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MagatzemPuntuacionsXML_SAX implements MagatzemPuntuacions {

    //nom del fitxer on es guardaran les dades
    //data/data/com.example.aplicacio1/files/
    private static String FITXER = "puntuacions.xml";
    private Context context;
    //per guardar la informació llegida del fitxer XML
    private LlistaPuntuacions llista;
    //indica si la variable llista ja ha sigut llegida des de fitxer
    private boolean carregadaLlista;

    public MagatzemPuntuacionsXML_SAX(Context context) {
        this.context = context;
        llista = new LlistaPuntuacions();
        carregadaLlista = false;
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        try {
            //Comprovem si la variabl llista té les dades
            if (!carregadaLlista) {
                //Llegeix dades del fitxer
                llista.llegirXML(context.openFileInput(FITXER));
            }
        } catch (FileNotFoundException e) {
            //Si és la primera vegada l'arxiu no existirà i si es llançar
            //aquesta excepció, però no passa res, no tealitzem cap acció.
            Log.e("Asteroides", e.getMessage(), e);
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
        //afegeix la nova puntuació a la llista
        llista.nou(punts, nom, data);
        try {
            //escriu de nou tota la informació de la llista al fitxer
            llista.escriureXML(context.openFileOutput(FITXER, Context.MODE_PRIVATE));
        } catch (Exception e) {
            Log.e("Asreroides", e.getMessage(), e);
        }
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        try {
            //Comprovem si la variable llista té les dades
            if (!carregadaLlista) {
                //Llegeix dades del fitxer XML
                llista.llegirXML(context.openFileInput(FITXER));
            }
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }

        //retorna la llista en el format esperat  Vector.

        return llista.aVectorString();
    }

    private class LlistaPuntuacions {
        //Una altre classe interna
        private class Puntuacio {
            int punts;
            String nom;
            long data;
        }

        //Variable que realment conte les dades XML
        private List<Puntuacio> llistaPuntuacions;

        public LlistaPuntuacions() {
            llistaPuntuacions = new ArrayList<Puntuacio>();
        }

        //afegeix una nova puntuació a la llista
        public void nou(int punts, String nom, long data) {
            Puntuacio puntuacio = new Puntuacio();
            puntuacio.punts = punts;
            puntuacio.nom = nom;
            puntuacio.data = data;
            llistaPuntuacions.add(puntuacio);
        }

        //extreu la informació que interessa de la llista i
        //construeix un vector de strings amb la informació
        public Vector<String> aVectorString() {
            Vector<String> result = new Vector<String>();
            for (Puntuacio puntuacio:llistaPuntuacions) {
                result.add(puntuacio.nom+" "+puntuacio.punts);
            }
            return result;
        }

        public void llegirXML(InputStream entrada) throws Exception {
            SAXParserFactory fabrica = SAXParserFactory.newInstance();
            SAXParser parser = fabrica.newSAXParser();
            XMLReader lector = parser.getXMLReader();
            ManejadorXML manejadorXML = new ManejadorXML();
            lector.setContentHandler(manejadorXML);
            lector.parse(new InputSource(entrada));
            carregadaLlista = true;
        }

        public void escriureXML(OutputStream salida) {
            XmlSerializer serializador = Xml.newSerializer();
            try {
                serializador.setOutput(salida, "UTF-8");
                serializador.startDocument("UTF-8", true);
                serializador.startTag("", "llista_puntuacions");
                for (Puntuacio puntuacion : llistaPuntuacions) {
                    serializador.startTag("", "puntuacio");
                    serializador.attribute("", "data",
                            String.valueOf(puntuacion.data));
                    serializador.startTag("", "nom");
                    serializador.text(puntuacion.nom);
                    serializador.endTag("", "nom");
                    serializador.startTag("", "punts");
                    serializador.text(String.valueOf(puntuacion.punts));
                    serializador.endTag("", "punts");
                    serializador.endTag("", "puntuacio");
                }
                serializador.endTag("", "llista_puntuacions");
                serializador.endDocument();
            } catch (Exception e) {
                Log.e("Asteroides", e.getMessage(), e);
            }
        }

        class ManejadorXML extends DefaultHandler {
            //El manejador s'encarrega de generar la llista de puntuacions.
            private StringBuilder cadena;
            private Puntuacio puntuacio;

            @Override
            //Inicialitzar variables
            public void startDocument() throws SAXException {
                llistaPuntuacions = new ArrayList<Puntuacio>();
                cadena = new StringBuilder();
            }

            @Override
            public void startElement(String uri, String nombreLocal, String nombreCualif, Attributes atr) throws SAXException {
                //Cada vegada que comença un nou element reiniciem la cadena
                cadena.setLength(0);
                //tractar l'etiqueta puntuacio. Les altres descartades
                if (nombreLocal.equals("puntuacio")) {
                    //COença un nou objecte
                    puntuacio = new Puntuacio();
                    //llegir l'atribut data de l'etiqueta. Ho rebem per l'argument
                    puntuacio.data = Long.parseLong(atr.getValue("data"));
                }
            }

            @Override
            //Es crida quan apareix un text dins d'una etiqueta
            public void characters(char ch[], int inici, int lon) {
                //guardem el text dins un string i després el tractem
                cadena.append(ch, inici, lon);
                //SAX no garanteix que ens passarà tot el text en un sol event,
                //si el text és molt extens es realitzaran diferents cridades
                //a aquest mètode. per això el text es va acumulant amb append().
            }

            @Override
            //En funció de l'etiqueta que estiguem acabant realitzarem una tasca
            //diferent. Si es tracta de punts o noms utilitzarem el valor de la
            //variable cadena per actualitzar el valor corresponent de l'objecte.
            //Sí es tracta de puntuacio afegim l'objecte a la llista
            public void endElement(String uri, String nombreLocal,
                                   String nombreCualif) throws SAXException {
                if (nombreLocal.equals("punts")) {
                    puntuacio.punts = Integer.parseInt(cadena.toString());
                } else if (nombreLocal.equals("nom")) {
                    puntuacio.nom = cadena.toString();
                } else if (nombreLocal.equals("puntuacion")) {
                    llistaPuntuacions.add(puntuacio);
                }
            }
            @Override
            public void endDocument() throws SAXException {}
        }

    }//tanca LListaPuntuacions

}
