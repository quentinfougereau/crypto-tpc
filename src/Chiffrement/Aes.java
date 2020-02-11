package Chiffrement;// -*- coding: utf-8 -*-;

import java.io.*;
import java.util.Random;

public class Aes {

	/* La clef courte K utilisée aujourd'hui est formée de 16 octets nuls */
	int longueur_de_la_clef = 16 ;
	byte K[] = {
        (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
        (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
    } ;

	/* Résultat du TP précédent : diversification de la clef courte K en une clef étendue W */

	static int Nr = 10;
	static int Nk = 4;
	int longueur_de_la_clef_etendue = 176;

	public byte W[] = { 
        (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
        (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
        (byte)0x62, (byte)0x63, (byte)0x63, (byte)0x63, (byte)0x62, (byte)0x63, (byte)0x63, (byte)0x63,
        (byte)0x62, (byte)0x63, (byte)0x63, (byte)0x63, (byte)0x62, (byte)0x63, (byte)0x63, (byte)0x63,
        (byte)0x9B, (byte)0x98, (byte)0x98, (byte)0xC9, (byte)0xF9, (byte)0xFB, (byte)0xFB, (byte)0xAA,
        (byte)0x9B, (byte)0x98, (byte)0x98, (byte)0xC9, (byte)0xF9, (byte)0xFB, (byte)0xFB, (byte)0xAA,
        (byte)0x90, (byte)0x97, (byte)0x34, (byte)0x50, (byte)0x69, (byte)0x6C, (byte)0xCF, (byte)0xFA,
        (byte)0xF2, (byte)0xF4, (byte)0x57, (byte)0x33, (byte)0x0B, (byte)0x0F, (byte)0xAC, (byte)0x99,
        (byte)0xEE, (byte)0x06, (byte)0xDA, (byte)0x7B, (byte)0x87, (byte)0x6A, (byte)0x15, (byte)0x81,
        (byte)0x75, (byte)0x9E, (byte)0x42, (byte)0xB2, (byte)0x7E, (byte)0x91, (byte)0xEE, (byte)0x2B,
        (byte)0x7F, (byte)0x2E, (byte)0x2B, (byte)0x88, (byte)0xF8, (byte)0x44, (byte)0x3E, (byte)0x09,
        (byte)0x8D, (byte)0xDA, (byte)0x7C, (byte)0xBB, (byte)0xF3, (byte)0x4B, (byte)0x92, (byte)0x90,
        (byte)0xEC, (byte)0x61, (byte)0x4B, (byte)0x85, (byte)0x14, (byte)0x25, (byte)0x75, (byte)0x8C,
        (byte)0x99, (byte)0xFF, (byte)0x09, (byte)0x37, (byte)0x6A, (byte)0xB4, (byte)0x9B, (byte)0xA7,
        (byte)0x21, (byte)0x75, (byte)0x17, (byte)0x87, (byte)0x35, (byte)0x50, (byte)0x62, (byte)0x0B,
        (byte)0xAC, (byte)0xAF, (byte)0x6B, (byte)0x3C, (byte)0xC6, (byte)0x1B, (byte)0xF0, (byte)0x9B,
        (byte)0x0E, (byte)0xF9, (byte)0x03, (byte)0x33, (byte)0x3B, (byte)0xA9, (byte)0x61, (byte)0x38,
        (byte)0x97, (byte)0x06, (byte)0x0A, (byte)0x04, (byte)0x51, (byte)0x1D, (byte)0xFA, (byte)0x9F,
        (byte)0xB1, (byte)0xD4, (byte)0xD8, (byte)0xE2, (byte)0x8A, (byte)0x7D, (byte)0xB9, (byte)0xDA,
        (byte)0x1D, (byte)0x7B, (byte)0xB3, (byte)0xDE, (byte)0x4C, (byte)0x66, (byte)0x49, (byte)0x41,
        (byte)0xB4, (byte)0xEF, (byte)0x5B, (byte)0xCB, (byte)0x3E, (byte)0x92, (byte)0xE2, (byte)0x11,
        (byte)0x23, (byte)0xE9, (byte)0x51, (byte)0xCF, (byte)0x6F, (byte)0x8F, (byte)0x18, (byte)0x8E
	};

	/* Le bloc à chiffrer aujourd'hui: 16 octets nuls */

    public byte State[] = {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
    };

	/* Matrice utilisée pour la multiplication */

    public byte[] matrix = {
            (byte)0x02, (byte)0x01, (byte)0x01, (byte)0x03, (byte)0x03, (byte)0x02, (byte)0x01, (byte)0x01,
            (byte)0x01, (byte)0x03, (byte)0x02, (byte)0x01, (byte)0x01, (byte)0x01, (byte)0x03, (byte)0x02
    };

    public byte[] inv_matrix = {
            (byte)0x0E, (byte)0x09, (byte)0x0D, (byte)0x0B, (byte)0x0B, (byte)0x0E, (byte)0x09, (byte)0x0D,
            (byte)0x0D, (byte)0x0B, (byte)0x0E, (byte)0x09, (byte)0x09, (byte)0x0D, (byte)0x0B, (byte)0x0E
    };

    public byte[] padding_array = {
            (byte)0x01,
            (byte)0x02, (byte)0x02,
            (byte)0x03, (byte)0x03, (byte)0x03,
            (byte)0x04, (byte)0x04, (byte)0x04, (byte)0x04,
            (byte)0x05, (byte)0x05, (byte)0x05, (byte)0x05, (byte)0x05,
            (byte)0x06, (byte)0x06, (byte)0x06, (byte)0x06, (byte)0x06, (byte)0x06,
            (byte)0x07, (byte)0x07, (byte)0x07, (byte)0x07, (byte)0x07, (byte)0x07, (byte)0x07,
            (byte)0x08, (byte)0x08, (byte)0x08, (byte)0x08, (byte)0x08, (byte)0x08, (byte)0x08, (byte)0x08,
            (byte)0x09, (byte)0x09, (byte)0x09, (byte)0x09, (byte)0x09, (byte)0x09, (byte)0x09, (byte)0x09,
            (byte)0x09,
            (byte)0x0A, (byte)0x0A, (byte)0x0A, (byte)0x0A, (byte)0x0A, (byte)0x0A, (byte)0x0A, (byte)0x0A,
            (byte)0x0A, (byte)0x0A,
            (byte)0x0B, (byte)0x0B,  (byte)0x0B, (byte)0x0B, (byte)0x0B, (byte)0x0B,  (byte)0x0B, (byte)0x0B,
            (byte)0x0B,  (byte)0x0B, (byte)0x0B,
            (byte)0x0C, (byte)0x0C, (byte)0x0C, (byte)0x0C, (byte)0x0C, (byte)0x0C, (byte)0x0C, (byte)0x0C,
            (byte)0x0C, (byte)0x0C, (byte)0x0C, (byte)0x0C,
            (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
            (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D, (byte)0x0D,
            (byte)0x0E, (byte)0x0E, (byte)0x0E, (byte)0x0E, (byte)0x0E, (byte)0x0E, (byte)0x0E, (byte)0x0E,
            (byte)0x0E, (byte)0x0E, (byte)0x0E, (byte)0x0E, (byte)0x0E, (byte)0x0E,
            (byte)0X0F, (byte)0X0F, (byte)0X0F, (byte)0X0F, (byte)0X0F, (byte)0X0F, (byte)0X0F, (byte)0X0F,
            (byte)0X0F, (byte)0X0F, (byte)0X0F, (byte)0X0F, (byte)0X0F, (byte)0X0F, (byte)0X0F,
            (byte)0x10, (byte)0x10, (byte)0x10, (byte)0x10, (byte)0x10, (byte)0x10, (byte)0x10, (byte)0x10,
            (byte)0x10, (byte)0x10, (byte)0x10, (byte)0x10, (byte)0x10, (byte)0x10, (byte)0x10, (byte)0x10
    };

    // Vecteur d'initialisation
    byte iv[] = {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
    };


	/* Programme principal */

	public static void main(String args[]) {

		Aes aes = new Aes();
        System.out.println("------------ CHIFFREMENT ------------");
        /*
        System.out.println("Le bloc \"State\" en entrée vaut : ") ;
        aes.afficher_le_bloc(aes.State) ;
         */
        byte[] fileBytes = aes.pkcs5("./butokuden.jpg");
        byte[] cryptedFile = new byte[fileBytes.length];
        byte[] randomIv = aes.generateRandomIv(16);
        System.arraycopy(randomIv, 0, aes.iv, 0, aes.iv.length);
        for (int i = 0; i < fileBytes.length; i+=16) {
            byte[] bloc = aes.getBloc(fileBytes, i, i + 15);
            //byte[] bloc = aes.State;
            bloc = xor(bloc, aes.iv);                                               // XOR avec le vecteur d'initialisation
            bloc = aes.chiffrer(bloc);
            System.arraycopy(bloc, 0, cryptedFile, i, bloc.length);
            System.arraycopy(bloc, 0, aes.iv, 0, bloc.length);       // Nouveau vecteur d'initialisation

        }
        System.out.printf("Vecteur d'initialisation : ");
        printBytes(randomIv);
        cryptedFile = aes.addRandomIv(cryptedFile, randomIv);
        aes.writeBytesToFile(cryptedFile, "./cbc-secret.jpg");

        /*
        aes.chiffrer();
        System.out.println("Le bloc \"State\" en sortie vaut : ") ;
        aes.afficher_le_bloc(aes.State) ;
         */

        /*
        aes.Create_Inv_SBox();

        System.out.println();
        System.out.println("------------ DECHIFFREMENT ------------");
        System.out.println("Le bloc \"State\" en entrée vaut : ") ;
        aes.afficher_le_bloc(aes.State) ;
        aes.dechiffrer();
        System.out.println("Le bloc \"State\" en sortie vaut : ") ;
        aes.afficher_le_bloc(aes.State) ;
         */

		/*
		// Exercice C.3
	    byte[] fileBytes = aes.pkcs5("./butokuden.jpg");
	    printBytes(fileBytes);
        System.out.printf("LENGTH : " + fileBytes.length);
        aes.writeBytesToFile(fileBytes, "./pkcs5-butokuden.jpg");
		 */
	}

	public void afficher_le_bloc(byte M[]) {
        for (int i=0; i<4; i++) { // Lignes 0 à 3
            System.out.print("          ");
            for (int j=0; j<4; j++) { // Colonnes 0 à 3
                System.out.print(String.format("%02X ", M[4*j+i]));
            }
            System.out.println();
        }
	}

	public byte[] chiffrer(byte[] bloc) {
        bloc = AddRoundKey(0, bloc);
        for (int i = 1; i < Nr; i++) {
            bloc = SubBytes(bloc);
            bloc = ShiftRows(bloc);
            bloc = MixColumns(bloc);
            bloc = AddRoundKey(i, bloc);
        }
        bloc = SubBytes(bloc);
        bloc = ShiftRows(bloc);
        bloc = AddRoundKey(Nr, bloc);
        return bloc;
	}

	/* Table de substitution déjà utilisée lors du TP précédent */

	public byte[] SBox = {
        (byte)0x63, (byte)0x7C, (byte)0x77, (byte)0x7B, (byte)0xF2, (byte)0x6B, (byte)0x6F, (byte)0xC5,
        (byte)0x30, (byte)0x01, (byte)0x67, (byte)0x2B, (byte)0xFE, (byte)0xD7, (byte)0xAB, (byte)0x76,
        (byte)0xCA, (byte)0x82, (byte)0xC9, (byte)0x7D, (byte)0xFA, (byte)0x59, (byte)0x47, (byte)0xF0,
        (byte)0xAD, (byte)0xD4, (byte)0xA2, (byte)0xAF, (byte)0x9C, (byte)0xA4, (byte)0x72, (byte)0xC0,
        (byte)0xB7, (byte)0xFD, (byte)0x93, (byte)0x26, (byte)0x36, (byte)0x3F, (byte)0xF7, (byte)0xCC,
        (byte)0x34, (byte)0xA5, (byte)0xE5, (byte)0xF1, (byte)0x71, (byte)0xD8, (byte)0x31, (byte)0x15,
        (byte)0x04, (byte)0xC7, (byte)0x23, (byte)0xC3, (byte)0x18, (byte)0x96, (byte)0x05, (byte)0x9A,
        (byte)0x07, (byte)0x12, (byte)0x80, (byte)0xE2, (byte)0xEB, (byte)0x27, (byte)0xB2, (byte)0x75,
        (byte)0x09, (byte)0x83, (byte)0x2C, (byte)0x1A, (byte)0x1B, (byte)0x6E, (byte)0x5A, (byte)0xA0,
        (byte)0x52, (byte)0x3B, (byte)0xD6, (byte)0xB3, (byte)0x29, (byte)0xE3, (byte)0x2F, (byte)0x84,
        (byte)0x53, (byte)0xD1, (byte)0x00, (byte)0xED, (byte)0x20, (byte)0xFC, (byte)0xB1, (byte)0x5B,
        (byte)0x6A, (byte)0xCB, (byte)0xBE, (byte)0x39, (byte)0x4A, (byte)0x4C, (byte)0x58, (byte)0xCF,
        (byte)0xD0, (byte)0xEF, (byte)0xAA, (byte)0xFB, (byte)0x43, (byte)0x4D, (byte)0x33, (byte)0x85,
        (byte)0x45, (byte)0xF9, (byte)0x02, (byte)0x7F, (byte)0x50, (byte)0x3C, (byte)0x9F, (byte)0xA8,
        (byte)0x51, (byte)0xA3, (byte)0x40, (byte)0x8F, (byte)0x92, (byte)0x9D, (byte)0x38, (byte)0xF5,
        (byte)0xBC, (byte)0xB6, (byte)0xDA, (byte)0x21, (byte)0x10, (byte)0xFF, (byte)0xF3, (byte)0xD2,
        (byte)0xCD, (byte)0x0C, (byte)0x13, (byte)0xEC, (byte)0x5F, (byte)0x97, (byte)0x44, (byte)0x17,
        (byte)0xC4, (byte)0xA7, (byte)0x7E, (byte)0x3D, (byte)0x64, (byte)0x5D, (byte)0x19, (byte)0x73,
        (byte)0x60, (byte)0x81, (byte)0x4F, (byte)0xDC, (byte)0x22, (byte)0x2A, (byte)0x90, (byte)0x88,
        (byte)0x46, (byte)0xEE, (byte)0xB8, (byte)0x14, (byte)0xDE, (byte)0x5E, (byte)0x0B, (byte)0xDB,
        (byte)0xE0, (byte)0x32, (byte)0x3A, (byte)0x0A, (byte)0x49, (byte)0x06, (byte)0x24, (byte)0x5C,
        (byte)0xC2, (byte)0xD3, (byte)0xAC, (byte)0x62, (byte)0x91, (byte)0x95, (byte)0xE4, (byte)0x79,
        (byte)0xE7, (byte)0xC8, (byte)0x37, (byte)0x6D, (byte)0x8D, (byte)0xD5, (byte)0x4E, (byte)0xA9,
        (byte)0x6C, (byte)0x56, (byte)0xF4, (byte)0xEA, (byte)0x65, (byte)0x7A, (byte)0xAE, (byte)0x08,
        (byte)0xBA, (byte)0x78, (byte)0x25, (byte)0x2E, (byte)0x1C, (byte)0xA6, (byte)0xB4, (byte)0xC6,
        (byte)0xE8, (byte)0xDD, (byte)0x74, (byte)0x1F, (byte)0x4B, (byte)0xBD, (byte)0x8B, (byte)0x8A,
        (byte)0x70, (byte)0x3E, (byte)0xB5, (byte)0x66, (byte)0x48, (byte)0x03, (byte)0xF6, (byte)0x0E,
        (byte)0x61, (byte)0x35, (byte)0x57, (byte)0xB9, (byte)0x86, (byte)0xC1, (byte)0x1D, (byte)0x9E,
        (byte)0xE1, (byte)0xF8, (byte)0x98, (byte)0x11, (byte)0x69, (byte)0xD9, (byte)0x8E, (byte)0x94,
        (byte)0x9B, (byte)0x1E, (byte)0x87, (byte)0xE9, (byte)0xCE, (byte)0x55, (byte)0x28, (byte)0xDF,
        (byte)0x8C, (byte)0xA1, (byte)0x89, (byte)0x0D, (byte)0xBF, (byte)0xE6, (byte)0x42, (byte)0x68,
        (byte)0x41, (byte)0x99, (byte)0x2D, (byte)0x0F, (byte)0xB0, (byte)0x54, (byte)0xBB, (byte)0x16};


	/* Table de substitution inversée */
    public byte[] Inv_SBox = new byte[256];

	/* Fonction mystérieuse qui calcule le produit de deux octets */

	byte gmul(byte a1, byte b1) {
		int a = Byte.toUnsignedInt(a1);
		int b = Byte.toUnsignedInt(b1);
		int p = 0;
		int hi_bit_set;
        for(int i = 0; i < 8; i++) {
            if((b & 1) == 1) p ^= a;
            hi_bit_set =  (a & 0x80);
            a <<= 1;
            if(hi_bit_set == 0x80) a ^= 0x1b;		
            b >>= 1;
        }
        return (byte) (p & 0xFF);
	}

	/* Partie à compléter pour ce TP */

	public byte[] SubBytes(byte[] bloc) {
        for (int i = 0; i < bloc.length; i++) {
            /* The values of the integral types are integers in the following ranges : For byte, from -128 to 127, inclusive */
            if (bloc[i] < 0) {
                bloc[i] = SBox[bloc[i] + 256];
            } else {
                bloc[i] = SBox[bloc[i]];
            }
        }
        return bloc;
    }
	
	public byte[] ShiftRows(byte[] bloc) {
        int length = bloc.length;
        byte[] res = new byte[length];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                res[4 * j + i] = bloc[((4 * j + i) + (4 * i)) % length];
            }
        }
        System.arraycopy(res, 0, bloc, 0, length);
        return bloc;
    }
	
	public byte[] MixColumns(byte[] bloc) {
	    byte[] vector = new byte[4];
        for (int i = 0; i < 4; i++) {
            for (int k = 0; k < 4; k++) {
                byte tmp = 0;
                for (int j = 0; j < 4; j++) {
                    tmp ^= gmul(bloc[4 * i + j], matrix[4 * j + k]);
                }
                vector[k] = tmp;
            }
            System.arraycopy(vector, 0, bloc, 4 * i, 4);
        }
        return bloc;
    }
	
	public byte[] AddRoundKey(int r, byte[] bloc) {
	    for (int i = 0; i < bloc.length; i++) {
            bloc[i] = (byte) (bloc[i] ^ W[longueur_de_la_clef * r + i]);
        }
	    return bloc;
    }

    public static void printBytes(byte[] bytes) {
        for (byte b : bytes) {
            System.out.printf("%02X ", b);
        }
        System.out.println();
    }

    public static void printBytes(byte[] bytes, int length) {
        for (int i = 0; i < length; i++) {
            System.out.printf("%02X ", bytes[i]);
        }
        System.out.println();
    }

    public void Create_Inv_SBox() {
	    byte tmp;
        for (int i = 0; i < SBox.length; i++) {
            /* The values of the integral types are integers in the following ranges : For byte, from -128 to 127, inclusive */
            tmp = (byte) i;
            if (tmp < 0) {
                tmp = (byte) (i + 256);
            }
            int index = SBox[i];
            if (index < 0) {
                Inv_SBox[index + 256] = tmp;
            } else {
                Inv_SBox[index] = tmp;
            }
        }
    }

    public void Inv_SubBytes() {
        for (int i = 0; i < State.length; i++) {
            /* The values of the integral types are integers in the following ranges : For byte, from -128 to 127, inclusive */
            if (State[i] < 0) {
                State[i] = Inv_SBox[State[i] + 256];
            } else {
                State[i] = Inv_SBox[State[i]];
            }
        }

    }



    public void Inv_MixColumns() {
        byte[] vector = new byte[4];
        for (int i = 0; i < 4; i++) {
            for (int k = 0; k < 4; k++) {
                byte tmp = 0;
                for (int j = 0; j < 4; j++) {
                    tmp ^= gmul(State[4 * i + j], inv_matrix[4 * j + k]);
                }
                vector[k] = tmp;
            }
            System.arraycopy(vector, 0, State, 4 * i, 4);
        }
    }

    public void Inv_ShiftRows() {
        int length = State.length;
        byte[] res = new byte[length];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                res[4 * j + i] = State[Math.floorMod((4 * j + i) - (4 * i), length)];
            }
        }
        System.arraycopy(res, 0, State, 0, length);
    }

    public void dechiffrer(byte[] bloc){
        AddRoundKey(Nr, bloc);
        for (int i = Nr - 1; i > 0; i--) {
            Inv_ShiftRows();
            Inv_SubBytes();
            AddRoundKey(i, bloc);
            Inv_MixColumns();
        }
        Inv_ShiftRows();
        Inv_SubBytes();
        AddRoundKey(0, bloc);
    }

    public byte[] pkcs5(String filename) {
        byte[] buffer = new byte[1024];
        int nbBytesRead;
        FileInputStream fis = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            fis = new FileInputStream(filename);
            while ((nbBytesRead = fis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, nbBytesRead);
            }
            int k = 16;
            int remainder = outputStream.size() % k;
            int index = 0;

            for (int i = 0; i < k - remainder; i++) {
                index += i;
            }

            outputStream.write(padding_array, index, k - remainder);
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    public byte[] getBloc(byte[] bytes, int debut, int fin) {
	    byte[] res = new byte[(fin - debut) + 1];
        if (fin - debut >= 0) System.arraycopy(bytes, debut, res, 0, (fin - debut) + 1);
        return res;
    }

    /*
    Effectue l'opération xor entre deux tableaux d'octets de même taille
    */
    public static byte[] xor(byte[] op1, byte[] op2) {
        byte[] res = new byte[op1.length];
        if (op1.length == op2.length) {
            for (int i = 0; i < op1.length; i++) {
                res[i] = (byte) (op1[i] ^ op2[i]);
            }
        }
        return res;
    }

    public byte[] generateRandomIv(int length) {
        byte[] res = new byte[length];
        for (int i = 0; i < length; i++) {
            Random random = new Random();
            res[i] = (byte) random.nextInt(255);
        }
        return res;
    }

    public void inv_pkcs5(String filename) {
        /* TODO */
    }

    public void writeBytesToFile(byte[] bytes, String output) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(output);
            fos.write(bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] addRandomIv(byte[] bytes, byte[] iv) {
        byte[] res = new byte[bytes.length + iv.length];
        System.arraycopy(iv, 0, res, 0, iv.length);
        System.arraycopy(bytes, 0, res, iv.length, bytes.length);
        return res;
    }

}

/*
 public byte State[] = {
        (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
        (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
    };

    public byte State[] = {
        (byte)0xA0, (byte)0xB1, (byte)0xC2, (byte)0xD3, (byte)0xA1, (byte)0xB2, (byte)0xC3, (byte)0xD0,
        (byte)0xA2, (byte)0xB3, (byte)0xC0, (byte)0xD1, (byte)0xA3, (byte)0xB0, (byte)0xC1, (byte)0xD2
    };
 */

/*
  $ make
  javac *.java 
  $ java Aes
  Le bloc "State" en entrée vaut : 
            00 00 00 00 
            00 00 00 00 
            00 00 00 00 
            00 00 00 00 
  Le bloc "State" en sortie vaut : 
            66 EF 88 CA 
            E9 8A 4C 34 
            4B 2C FA 2B 
            D4 3B 59 2E 
*/
