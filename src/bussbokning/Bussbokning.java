/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bussbokning;
import java.util.*;
/**
 *
 * @author mert.kuzucu
 */
public class Bussbokning {

    // Max antal platser.
    static int ANTALPLATSER = 21;
    // Funktionen samlar in data som är ett string fält med 21 sätten. Varje sätte separeras mellan personnum, fnamn, enamn och kön.

    public static void main(String[] args) {
        meny(skapaData());
    }

    // Skapa fält med 21 platser
    static String[] skapaData() {
        String[] info = new String[ANTALPLATSER];
        int i = 0;
        while (i < ANTALPLATSER) {
            info[i] = Integer.toString(i + 1) + "|0|0|0|0";
            i++;
        }
        return info;
    }
    // funtkionen meny är menyn för användaren som den kan hålla på med.
    static String[] meny(String[] info) {
        Scanner tagentbord = new Scanner(System.in);

        System.out.println("Skriv ett nummer.");
        System.out.println("1. Boka");
        System.out.println("2. Avboka");
        System.out.println("3. Hitta bokning");
        System.out.println("4. Visa passagerare");
        System.out.println("5. Visa lediga platser");
        System.out.println("6. Visa vinst");
        System.out.println("7. Sluta");

        int svar = tagentbord.nextInt();

        switch (svar) {
            case 1 -> skapaBokning(info);
            case 2 -> avBoka(info);
            case 3 -> hittaBokning(info);
            case 4 -> sorteraÅlder(info);
            case 5 -> visaLediga(oBokade(info, 1));
            case 6 -> visaVinst(info);
            // Avslutar programmet
            case 7 -> System.exit(0);
        }
        return meny(info);
    }
    // Funktionen kallas när användaren bokar ett sätte, har chans att välja fönsterplats eller ej

    static String[] skapaBokning(String[] info) {
        Scanner tagentbord = new Scanner(System.in);

        int fönster = fåFönster();
        String[] oBokadePlatser;

        if (fönster == 1) {
            // Om person vill sitta vid fönster
            oBokadePlatser = oBokade(info, 2);
        } else {
            // Vill inte sitta vid fönster
            oBokadePlatser = oBokade(info, 3);
        }
        visaLediga(oBokadePlatser);
        String sätte = fåSätte(tagentbord, oBokadePlatser);
        String personnummer = kollaPersonnummer();  // Kollar om personnum är taget.
        String fnamn = kollaNamn("Vad är ditt förnamn?");
        String enamn = kollaNamn("Vad är ditt efternamn?");
        String kön = kollaNamn("Vad är ditt kön? M/F");
        return boka(info, Integer.parseInt(sätte), platsStr (sätte, personnummer, fnamn, enamn, kön));
    }
    static String[] avBoka(String[] info) {
        Scanner tagentbord = new Scanner(System.in);
        System.out.println("Skriv ditt personnummer i formatet: YYYYMMDD eller för- och efternanmn.");

        return info;
    }
    // När användare letar efter plats.

    static void hittaBokning(String[] info) {
        Scanner tagentbord = new Scanner(System.in);
        System.out.println("Skriv ditt personnummer i formatet: YYYYMMDD eller skriv förnamn och efternamn");
        Bokning(info, tagentbord.nextLine(), false);
    }
//Updaterar platsStr för den valda platsen.
    static String[] boka(String[] info, int sätte, String platsStr) {
        info[sätte - 1] = platsStr;
        System.out.println("Ok.");
        return info;
    }
    // Retunerar antingen alla obokade platser, alla borttagna fönsterplatser eller alla normala platser.
    static String[] oBokade(String[] info, int typAvPlats) {
        String[] x = {};
        for (String platsStr : info) {
            String[] platsFält = platsStr.split("\\|");
            if (platsFält[1].equals("0")) {
                if (typAvPlats == 1) {
                    // alla platser
                    x = Element(x, platsStr);
                } else if (typAvPlats == 2 && valtFönster(Integer.parseInt(platsFält[0]))) {
                    // bara fönstersätten
                    x = Element(x, platsStr);
                } else if (typAvPlats == 3 && !valtFönster(Integer.parseInt(platsFält[0]))) {
                    // vanliga platser
                    x = Element(x, platsStr);
                }
            }
        }
        return x;
    }
    // Element för string fältet.
    static String[] Element(String[] fält, String y) {
        fält = Arrays.copyOf(fält, fält.length + 1);
        fält[fält.length - 1] = y;
        return fält;
    }
    // tar bort element ändringarna från förra funktionen

    static String[] taBort(String[] fält) {
        if (fält == null || fält.length == 0) {
            return fält;
        }
        String[] x = new String[fält.length - 1];
        for (int i = 0; i < x.length; i++) {
            x[i] = fält[i];
        }
        return x;
    }
// Visar vinsten av alla bokade platser.
    static void visaVinst(String[] info) {
        double Vinst = räknaVinst(passagerareÅlder(info));
        System.out.println("Vinst:" + String.format("%.2f", Vinst) + "Kr");
    }
    // Hämtar personens ålder

    static String[] passagerareÅlder(String[] info) {
        String x = "";
        for (String platsStr : info) {
            String[] platsFält = platsStr.split("\\|");
            if (!platsFält[1].equals("0")) {
                // Bokade platsen har personnummer
                x += platsFält[1].substring(0, 4) + "|";
            }
        }
        return x.split("\\|");
    }
    // Räknar vinsten baserat på alla passagerare.

    static double räknaVinst(String[] bokadePlatser) {
        double Vinst = 0;
        if ((bokadePlatser.length == 1 && bokadePlatser[0].equals("")) || bokadePlatser.length == 0) {
            // Om ingen plats är bokad
            return Vinst;
        } else {
            int bornYear = Integer.parseInt(bokadePlatser[bokadePlatser.length - 1]);
            Vinst = räknaPris(bornYear);
            // Kallar sig själv för att räkna vinst
            return Vinst + räknaVinst(taBort(bokadePlatser));
        }
    }
    // Räknar vinsten beroende på passagerarnas ålder

    static double räknaPris(int föddDatum) {
        int ålder = räknaÅlder(föddDatum);
        if (ålder < 18) {
            return 149.90;
        } else if (ålder > 69) {
            return 200.0;
        } else {
            return 299.90;
        }
    }
    // Räknar åldern

        // Samlar användarens svar in till en string

    static String platsStr(String sätte, String personnummer, String fNamn, String eNamn, String Kön){
        String Platsstr = sätte+"|"+personnummer+"|"+fNamn+"|"+eNamn+"|"+Kön;
        return Platsstr;
    }
    // Visar lediga platser

    static void visaLediga(String[] info){
        String sätten = "Lediga platser: ";
        for (String platsStr : info) {
            sätten += platsStr.split(":")[0] + ", ";
        }
        if (sätten.endsWith(", ")){
            System.out.println(sätten.substring(0, sätten.length()-2));
        }
        else
            System.out.println("Finns inga lediga platser kvar.");
    }
    // Gör så användaren kan söka efter sin bokning med sitt personnummer eller för- och efternamn
    // Denna funktion kallas när det önskas att avboka en plats.
    // Retunerar en uppdaterad version av platserna.
    static String Bokning(String[] info, String letaStr, boolean visaPlats){
        for (String platsStr : info) {
            String[] platsFält = platsStr.split("\\|");
            if(!letaStr.contains(" ")){
                // Letar efter personnummer, kollar även så att alla pn e unika

                if(platsFält[1].equals(letaStr))
                    if(visaPlats)
                        return platsFält[0];
                    else
                        return visaSätte(platsFält);
            }
            else
            {
                // LEtar efter namn och så att pn är unikt
                String[] namnFält = letaStr.split(" ",2);
                if(platsFält[2].equals(namnFält[0]) && platsFält[3].equals(namnFält[1]))
                    if(visaPlats)
                        return platsFält[0];
                    else
                        return visaSätte(platsFält);
            }
        }
        return "Hittades ej.";
    }
    // Tar bort bokninge genom att ändra bokadplats till 0
    static String[] Bokning(String[] info, String letaStr){
        String bokadPlats = Bokning(info, letaStr, true);

        if(bokadPlats.contains("Din bokning hittades ej-")){
            System.out.println(bokadPlats);
            return info;
        }
        // Tar bort bokningen genom att ändra värde -> 0
        return boka(info, Integer.parseInt(bokadPlats), platsStr(bokadPlats,"0","0","0","0"));
    }
        // Visar information om personen som obkat och boknigsninfo

    static String visaSätte(String[] platsFält){
        String sätte = "sätte:";
        String personnummer = platsFält[1];
        int föddDatum = Integer.parseInt(personnummer.substring(0, 4));
        int ålder = räknaÅlder(föddDatum);
        if(ålder < 18){
            personnummer = personnummer + " (under 18)";
        }
        if(valtFönster(Integer.parseInt(platsFält[0]))){
            sätte = "windowseat:";
        }
        String x = platsFält[2]+" "+platsFält[3]+" "+ personnummer + " " + sätte + platsFält[0];
        System.out.println(x);
        return x;
    }

    private static int räknaÅlder(int föddDatum) {
        return 0;
    }
    // Kollar vad som är fönstersätte och inte

    static boolean valtFönster(int sätte){
        if(sätte % 2 == 0 && sätte % 4 == 0 && sätte != 20){
            // Platsen har ett jämnt nummer
            return true;
        }
        else if(sätte % 4 == 1){
            // Platsen har ett udda nummer
            return true;
        }
        return false;
    }
    // När man vill se passagerare, sorteras äldst till yngst.

    static void sorteraÅlder(String[] info) {
        // variablen kollar pn
        String x = "";
        for (String platsStr : info) {
            String[] platsFält = platsStr.split("\\|");
            if (!platsFält[1].equals("0"))
                // Bokad plats har personnummer
                x += platsFält[1]+"|";
        }
        if(x.length() == 0)
            System.out.println("Alla platser lediga");

        String[] människor = x.split("\\|");
        int a = människor.length;
        for (int i = 0; i < a - 1; i++) {
            for (int j = 0; j < a - i - 1; j++) {
                if (Integer.parseInt(människor[j]) > Integer.parseInt(människor[j + 1])){
                    String temp = människor[j];
                     människor[j] = människor[j + 1];
                    människor[j + 1] = temp;
                }
            }
        }
        // Sorterar bokningarna
        for(String personnummer : människor){
            Bokning(info, personnummer, false);
        }
    }
    // Samlar informaiton från avnändaren och retunrerar platsen och ifall den är ledig
    static String fåSätte(Scanner tagentbord, String[] oBokadePlatser){
        System.out.println("Skriv ditt önskade nummer");
        String sätte = tagentbord.nextLine();
        for(String platsStr : oBokadePlatser){
            String[] platsFält = platsStr.split("\\|");
            if(platsFält[0].equals(sätte)){
                return sätte;
            }
        }
        System.out.println("Fel nummer");
        return fåSätte(tagentbord, oBokadePlatser);
    }
    // Kollar om personnummret är i rätt format.  "YYYYMMDD"

    static String kollaPersonnummer() {
        Scanner tagentbord = new Scanner(System.in);

        System.out.println("Skriv ditt personnumemr i formatet YYYYMMDD:");
        int pN = 0;
        try{
            pN = tagentbord.nextInt();
            String personnummer = Integer.toString(pN);
            if(personnummer.length() == 8 &&
                    pN > 17000000 &&
                    pN < 20230501){
                // checkar om inputen enbart är nummer och korrekt år
                return personnummer;
            }
        } catch(Exception e){
            System.out.println("använd bara nummer");

        }
        System.out.println("Fel format av personnummret.");
        return kollaPersonnummer();
    }
    // Legit checkar användarens namn

    static String kollaNamn(String text) {
        Scanner scan = new Scanner(System.in);

        System.out.println(text);
        String svar = scan.nextLine();
        if(svar.length() > 0){
            return svar;
        }
        System.out.println("Fel svar");
        return kollaNamn(text);
    }
    // Kollar om svaret är rätt.
    static int fåFönster(){
        Scanner tagentbord = new Scanner(System.in);

        System.out.println("Vill du ha en fönsterplats? 1 för Ja, 0 för nej");
        int fönster;
        try
        {
            fönster = tagentbord.nextInt();
        }catch (Exception e){
            System.out.println("Fel svar. 1 eller 0");
            return fåFönster();
        }
        if(fönster == 1 || fönster == 0){
            return fönster;
        }

        return fåFönster();}
}
